package com.bsit.service;

import com.bsit.model.MessageCode;

public interface MessageCodeServiceI {
	
	/**
	 * 把短信存入服务器
	 * @param userMessage
	 * @author runlin.chen
	 * @date 2016年5月18日上午10:02:47
	 */
	int addMessageCode(MessageCode message);
	
	/**
	 * 获取最新的短信信息
	 * @param userId
	 * @author runlin.chen
	 * @date 2016年5月18日上午10:03:53
	 */
	MessageCode getMessageCodeByLast(String mobile, String productName);
	
	/**
	 * 获取同一个手机得到的短信总数
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @author runlin.chen
	 * @date 2016年5月18日上午10:03:17
	 */
	int getMessageCodeCount(String mobile);

}
