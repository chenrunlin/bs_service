package com.bsit.common.utils;

public class Status {
	
	public static final String STATUS_OK = "1";
	public static final String STATUS_ERROR = "0";
	public static final String STATUS_IO_EXCEPTION = "-1";
	
	public static Status COMMON_STATUS_SEND_OK = new Status(STATUS_OK, "发送短信成功!");
	public static Status COMMON_STATUS_SEND_ERROR = new Status(STATUS_ERROR, "发送短信失败!");
	public static Status COMMON_STATUS_SAVE_FAIL = new Status(STATUS_IO_EXCEPTION, "发送短信成功，保存短信失败!");
	public static Status COMMON_STATUS_EXCEPRTION = new Status(STATUS_IO_EXCEPTION, "IO异常!");
	
	public static Status COMMON_STATUS_ERROR_PARAMETER = new Status("1001", "参数错误");
	public static Status COMMON_STATUS_ERROR_PROGRAM = new Status("1002", "程序异常");
	public static Status COMMON_STATUS_ERROR_REQUEST_TYPE = new Status("1003", "用户请求类型不正确！");
	
	public static Status COMMON_STATUS_SEND_REPEAT = new Status("2001", "1分钟内不能重复发送!");
	public static Status COMMON_STATUS_OVER_LIMIT = new Status("2002", "今日已超过短信5次限制!");
	
	public static Status VERIFICATION_STATUS_OK = new Status(STATUS_OK, "验证码正确!");
	public static Status VERIFICATION_STATUS_ERROR = new Status(STATUS_ERROR, "验证码错误!");
	public static Status VERIFICATION_STATUS_INVALID = new Status("3001", "验证码失效!");
	
	public static Status TELEPHONE_STATUS_INVALID = new Status("4001", "手机号码无效!");
	
	private String code;
	private String msg;

	public Status(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return "1".equals(code) ? "1" : code;
	}

	public String getMsg() {
		return "1".equals(code) ? msg : new StringBuffer(msg).toString();
	}

	public static Status getStatusWithArgs(Status status, Object... args) {
		return new Status(status.getCode(), String.format(status.getMsg(), args));
	}

}
