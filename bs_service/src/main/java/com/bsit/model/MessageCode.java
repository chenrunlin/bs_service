package com.bsit.model;

public class MessageCode{
	
	private String id;
	private String mobile;			//手机号
	private String message;			//验证码内容
	private String createTime;		//注册时间
	private String productName;		//项目名称
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	@Override
	public String toString() {
		return "MessageCode [id=" + id + ", mobile=" + mobile + ", message="
				+ message + ", createTime=" + createTime + ", productName="
				+ productName + "]";
	}

}
