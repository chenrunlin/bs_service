package com.bsit.common.utils;

import java.util.Date;

public class BaseJsonObject {
	
	private String code;
	private String msg;
	private Date time = new Date();
	private Object object;

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getTime() {
		return time;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public void setStatus(Status status) {
		this.code = status.getCode();
		this.msg = status.getMsg();
	}
}
