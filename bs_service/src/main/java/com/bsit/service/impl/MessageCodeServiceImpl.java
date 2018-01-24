package com.bsit.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bsit.common.utils.DateUtil;
import com.bsit.mapper.MessageCodeMapper;
import com.bsit.model.MessageCode;
import com.bsit.service.MessageCodeServiceI;

@Service(value="messageCodeService")
public class MessageCodeServiceImpl implements MessageCodeServiceI {
	
	@Autowired
	private MessageCodeMapper messageCodeMapper;

	@Override
	public int addMessageCode(MessageCode message) {
		return messageCodeMapper.addMessageCode(message);
	}

	@Override
	public MessageCode getMessageCodeByLast(String mobile, String productName) {
		return messageCodeMapper.getMessageCodeByLast(mobile, productName);
	}

	@Override
	public int getMessageCodeCount(String mobile) {
		String nowDate = DateUtil.formatFromDate(new Date());
		String startTime = nowDate + " 00:00:00";
		String endTime = nowDate + " 23:59:59";
		Map<String , String> map = new HashMap<String,String>();
		map.put("mobile", mobile);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return messageCodeMapper.getMessageCodeCount(map);
	}

}
