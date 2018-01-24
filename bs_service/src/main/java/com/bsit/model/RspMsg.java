package com.bsit.model;

import org.apache.log4j.Logger;

public class RspMsg {
	
	private String code;
	private String message;

	public RspMsg() {
		//构造函数
	}

	public RspMsg(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public RspMsg(Logger logger, String code, String message) {
		logger.info("bs-" + code + "：" + message);
		this.code = code;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String Message) {
		message = Message;
	}

	@Override
	public String toString() {
		return "RspMsg [code=" + code + ", message=" + message + "]";
	}

}
