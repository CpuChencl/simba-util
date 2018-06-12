package com.simba.jpush.msg.model;

import java.util.Date;

/**
 * 上行消息内容
 * 
 * @author caozhejun
 *
 */
public class ReceiveMsg {

	/**
	 * 主叫号码（用户手机号码）
	 */
	private String phone;

	/**
	 * 消息送达到极光平台的时间
	 */
	private Date replyTime;

	/**
	 * 用户回复的消息内容
	 */
	private String content;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReceiveMsg [phone=");
		builder.append(phone);
		builder.append(", replyTime=");
		builder.append(replyTime);
		builder.append(", content=");
		builder.append(content);
		builder.append("]");
		return builder.toString();
	}

}
