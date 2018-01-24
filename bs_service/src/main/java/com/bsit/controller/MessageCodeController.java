package com.bsit.controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bsit.common.utils.BaseJsonObject;
import com.bsit.common.utils.Code;
import com.bsit.common.utils.DateUtil;
import com.bsit.common.utils.SendMessServlet_SD;
import com.bsit.common.utils.Status;
import com.bsit.common.utils.StringUtil;
import com.bsit.model.MessageCode;
import com.bsit.service.MessageCodeServiceI;

@Controller
@RequestMapping(value = "/messageCode")
public class MessageCodeController {
	
	private static final Logger logger = Logger.getLogger(MessageCodeController.class);
	
	@Autowired
	private MessageCodeServiceI messageService;
	
	/**
	 * 发送验证码
	 * @param mobileNo 用户电话号码
	 * @param requestType 用户请求类型,
	 * 01：注册，02：找回密码---(有次数限制)
	 * 		信息内容如：您好，您的验证码为9527，每日可获取5次验证码，90秒内操作有效，请及时操作！
	 * 03：验证身份 ---(没有次数限制)
	 * 		信息内容如：您好，您的验证码为9527，每日可获取5次验证码，90秒内操作有效，请及时操作！
	 * 101:提示信息,如：有新订单提交，请尽快处理
	 * 201：审核通过，202：审核不通过
	 * @return RspMsg
	 * @author runlin.chen
	 * @date 2016年5月18日上午10:08:59
	 */
	@RequestMapping(value = "/sendMessageCode", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonObject sendMessageCode(@RequestParam(value="mobileNo")String mobileNo, 
			@RequestParam(value="requestType")String requestType, @RequestParam(value="productName")String productName) {
		logger.info("-----发送短信开始----!");
		BaseJsonObject jsonObject = new BaseJsonObject();
		try {
			if (!StringUtil.isEmpty(mobileNo) && !StringUtil.isEmpty(requestType) && !StringUtil.isEmpty(productName)) {
				MessageCode message = messageService.getMessageCodeByLast(mobileNo, productName);
				if (requestType.equals("01") || requestType.equals("02")) {
					jsonObject = sendMessage(message, mobileNo, requestType, productName);
				} else if(requestType.equals("03")) {
					jsonObject = sendMessageNoCounts(mobileNo, requestType, productName);
				} else if(requestType.equals("101")) {	//提示信息
					jsonObject = sendMessageTips(mobileNo, requestType, productName);
				}  else if(requestType.equals("201")) {	//审核通过
					jsonObject = sendMessageTips(mobileNo, requestType, productName);
				}  else if(requestType.equals("202")) {	//审核不通过
					jsonObject = sendMessageTips(mobileNo, requestType, productName);
				} else {
					jsonObject.setStatus(Status.COMMON_STATUS_ERROR_REQUEST_TYPE);
				}
			} else {
				jsonObject.setStatus(Status.COMMON_STATUS_ERROR_PARAMETER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.trace("bs--1：查询失败，原因：系统异常，请联系管理员!");
			jsonObject.setStatus(Status.COMMON_STATUS_EXCEPRTION);
		} finally {
			logger.info("-----发送短信结束----!");
		}
		return jsonObject;
	}
	
	/**
	 * 发送包含验证码的短息，有次数限制
	 * @param message
	 * @param mobileNo
	 * @return
	 */
	private BaseJsonObject sendMessage(MessageCode message, String mobileNo, String requestType, String productName){
		BaseJsonObject jsonObject = new BaseJsonObject();
		// 获取今日此号码接收到短信的条数
		int count = messageService.getMessageCodeCount(mobileNo);
		if (count >= 5) {
			jsonObject.setStatus(Status.COMMON_STATUS_OVER_LIMIT);
		}
		String curTime = DateUtil.formatFromDateTime(new Date());
		if (message != null) {
			String createTime = message.getCreateTime();
			// 判断发送短信间隔时间是否超过1分钟,如果超过就不发送，否则发送
			long[] disti = DateUtil.getDistanceTimes(curTime, createTime);
			long diff = disti[2];
			if (diff < 1) {
				jsonObject.setStatus(Status.COMMON_STATUS_SEND_REPEAT);
			}
		}
		String contents = StringUtil.verificationCode();// 得到验证码
		jsonObject = sendAction(mobileNo, requestType, true, contents, productName);
		return jsonObject;
	}
	
	/**
	 * 发送包含验证码的短息，没有次数限制
	 * @param message
	 * @param mobileNo
	 * @return
	 */
	private BaseJsonObject sendMessageNoCounts(String mobileNo, String requestType, String productName){
		BaseJsonObject jsonObject = new BaseJsonObject();
		String contents = StringUtil.verificationCode();// 得到验证码
		jsonObject = sendAction(mobileNo, requestType, true, contents, productName);
		return jsonObject;
	}
	
	/**
	 * 短信发送提示信息
	 * @param mobileNo
	 * @return
	 */
	private BaseJsonObject sendMessageTips(String mobileNo, String requestType, String productName){
		BaseJsonObject jsonObject = new BaseJsonObject();
		jsonObject = sendAction(mobileNo, requestType, false, "", productName);
		return jsonObject;
	}
	
	/**
	 * 具体发送短信验证码的动作
	 * @param mobileNo
	 * @param hasCode	有无验证码
	 * @param contents	验证码
	 * @return
	 */
	private BaseJsonObject sendAction(String mobileNo, String requestType, boolean hasCode, String contents, String productName){
		BaseJsonObject jsonObject = new BaseJsonObject();
		SendMessServlet_SD sendMessServlet_SD = new SendMessServlet_SD();
		String msg = "";	//短信内容,数据库中存的内容
		if(hasCode) {
			msg = contents;
			jsonObject = sendMessServlet_SD.sendMessCalls(mobileNo, Code.REGISTRATION.replace("Captcha", contents));
		} else {
			if (requestType.equals("101")) {
				msg = Code.MES_TIPS;
			} else if (requestType.equals("201")){
				msg = Code.AUDIT_SUCC;
			} else if (requestType.equals("202")){
				msg = Code.AUDIT_FAIL;
			}
			jsonObject = sendMessServlet_SD.sendMessCalls(mobileNo, msg);
		}
		
		if (jsonObject.getCode().equals("1")) {
			MessageCode mes = new MessageCode();
			mes.setMobile(mobileNo);
			mes.setMessage(msg);
			mes.setCreateTime(DateUtil.formatFromDateTime(new Date()));
			mes.setProductName(productName);
			try{
				messageService.addMessageCode(mes);
				jsonObject.setStatus(Status.COMMON_STATUS_SEND_OK);
			} catch (Exception e) {
				jsonObject.setStatus(Status.COMMON_STATUS_SAVE_FAIL);
				e.printStackTrace();
			}
		}
		return jsonObject;
	}
	
	@RequestMapping(value = "/validateMessageCode", method = RequestMethod.POST)
	@ResponseBody
	public BaseJsonObject validateMessageCode(@RequestParam(value="mobileNo")String mobileNo, 
			@RequestParam(value="validateCode")String validateCode, @RequestParam(value="productName")String productName) {
		logger.info("-----比较验证码开始----!");
		BaseJsonObject jsonObject = new BaseJsonObject();
		try {
			if (!StringUtil.isEmpty(mobileNo) && !StringUtil.isEmpty(validateCode)) {
				jsonObject = validate(mobileNo, validateCode, productName);
			} else {
				jsonObject.setStatus(Status.COMMON_STATUS_ERROR_PARAMETER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.trace("bs--1：查询失败，原因：系统异常，请联系管理员!");
		} finally {
			logger.info("-----比较验证码结束----!");
		}
		return jsonObject;
	}
	
	/**
	* @Title: validate 
	* @Description: (验证短信码是否正确) 
	* @param mobileNo
	* @param validateCode
	* @param @return    设定文件 
	 */
	private BaseJsonObject validate(String mobileNo, String validateCode, String productName) {
		BaseJsonObject jsonObject = new BaseJsonObject();
		String curTime = DateUtil.formatFromDateTime(new Date());
		MessageCode message = messageService.getMessageCodeByLast(mobileNo, productName);
		if (message != null) {
			String createTime = message.getCreateTime();
			// 判断发送短信间隔时间是否超过1分钟,如果超过就不发送，否则发送
			long[] disti = DateUtil.getDistanceTimes(curTime, createTime);
			long diff = disti[2];
			System.out.println("有效期时间：" + diff + "分钟!");
			if (diff > 5) {
				jsonObject.setStatus(Status.VERIFICATION_STATUS_INVALID);
				return jsonObject;
			}
			String code = message.getMessage();
			if (!StringUtil.isEmpty(code)) {
				if(!code.equals(validateCode)) {
					jsonObject.setStatus(Status.VERIFICATION_STATUS_ERROR);
					return jsonObject;
				} else {
					jsonObject.setStatus(Status.VERIFICATION_STATUS_OK);
					return jsonObject;
				}
			}
		} else {
			jsonObject.setStatus(Status.TELEPHONE_STATUS_INVALID);
		}
		return jsonObject;
	}

}
