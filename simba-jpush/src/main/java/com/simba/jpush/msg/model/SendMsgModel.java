package com.simba.jpush.msg.model;

import java.util.Map;

/**
 * 发送信息对象
 * 
 * @author caozhejun
 *
 */
public class SendMsgModel {

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 值对象
	 */
	private Map<String, String> values;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SendMsgModel [mobile=");
		builder.append(mobile);
		builder.append(", values=");
		builder.append(values);
		builder.append("]");
		return builder.toString();
	}

}
