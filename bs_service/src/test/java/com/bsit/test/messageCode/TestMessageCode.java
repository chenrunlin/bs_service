package com.bsit.test.messageCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;

import com.bsit.common.utils.BaseJsonObject;
import com.bsit.common.utils.DateUtil;
import com.bsit.common.utils.HttpUtil;
import com.bsit.model.MessageCode;
import com.bsit.service.MessageCodeServiceI;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring.xml" ,"classpath*:/spring-mvc.xml"})
public class TestMessageCode {
	
	@Autowired
	private MessageCodeServiceI messageCodeService;
	
//	@Test
	public void testGetMessageCode(){
		MessageCode message = new MessageCode();
		message.setMobile("13572484225");
		message.setMessage("9254");
		message.setCreateTime(DateUtil.formatFromDateTime(new Date()));
		try{
			messageCodeService.addMessageCode(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void testSendMessage(){
		String result = "fail";
		boolean status = send();
		if(status) {
    		result = "successful";
    	}
    	System.out.println("the result of sending message is " + result);
	}

	public boolean send(){
		String mobile = "13572484225";
		String url_sendMessage = "http://localhost:8080/bs_service/messageCode/sendMessageCode";
		String res_sendMessage = "";
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobileNo", mobile);
		params.put("requestType", "01");
		params.put("productName", "CHUANG");
		res_sendMessage = HttpUtil.sendPost(url_sendMessage, params);
		JSONObject jsonobject = JSONObject.fromObject(res_sendMessage);
		BaseJsonObject jsonObject = (BaseJsonObject) JSONObject.toBean(jsonobject, BaseJsonObject.class);
		
		if(jsonObject == null){
			return false;
		}
		if(!jsonObject.getCode().equals("1")){
			return false;
		}
		return true;
	}
	
//	@Test
	public void testAudit(){
		String result = "fail";
		boolean status = audit();
		if(status) {
    		result = "successful";
    	}
    	System.out.println("the result of sending message is " + result);
	}

	public boolean audit(){
		String mobile = "13572484225";
		String url_sendMessage = "http://121.43.37.101:38080/bs_service/messageCode/sendMessageCode";
		String res_sendMessage = "";
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobileNo", mobile);
		params.put("requestType", "201");
		params.put("productName", "CHUANG");
		res_sendMessage = HttpUtil.sendPost(url_sendMessage, params);
		JSONObject jsonobject = JSONObject.fromObject(res_sendMessage);
		BaseJsonObject jsonObject = (BaseJsonObject) JSONObject.toBean(jsonobject, BaseJsonObject.class);
		
		if(jsonObject == null){
			return false;
		}
		if(!jsonObject.getCode().equals("1")){
			return false;
		}
		return true;
	}
	
//	@Test
	public void testValidate(){
		String result = "fail";
		boolean status = validate();
		if(status) {
    		result = "successful";
    	}
    	System.out.println("the result of sending message is " + result);
	}

	public boolean validate(){
		String mobile = "13572484225";
		String url_sendMessage = "http://localhost:8080/bs_service/messageCode/validateMessageCode";
		String res_sendMessage = "";
		Map<String, String> params = new HashMap<String, String>();
		params.put("mobileNo", mobile);
		params.put("validateCode", "7145");
		params.put("productName", "CHUANG");
		res_sendMessage = HttpUtil.sendPost(url_sendMessage, params);
		JSONObject jsonobject = JSONObject.fromObject(res_sendMessage);
		BaseJsonObject jsonObject = (BaseJsonObject) JSONObject.toBean(jsonobject, BaseJsonObject.class);
		
		if(jsonObject == null){
			return false;
		}
		if(!jsonObject.getCode().equals("1")){
			return false;
		}
		return true;
	}
	
//	@Test
	public void testMessage(){
		MessageCode message = messageCodeService.getMessageCodeByLast("13572484225", "CHUANG");
		System.out.println(message.toString());
	}
	
}
