package com.simba.jpush.msg.model;

import java.util.Date;

/**
 * 下行消息送达状态
 * 
 * @author caozhejun
 *
 */
public class SendResult {

	/**
	 * API 调用的时候返回的 msg_id
	 */
	private String msgId;

	/**
	 * 发送状态返回码 4001 发送成功 4002 被叫手机号码为运营商黑名单，需联系运营商处理 4003
	 * 手机终端问题，手机关机、停机等，请确认手机状态是否正常 4004 被叫手机号码为空号，请核实手机号码是否合规 4005 可发送短信余量不足
	 * 4006 发送超频 4100 其他错误
	 */
	private int status;

	/**
	 * 短信送达时间
	 */
	private Date receiveTime;

	/**
	 * 短信送达手机号
	 */
	private String phone;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SendResult [msgId=");
		builder.append(msgId);
		builder.append(", status=");
		builder.append(status);
		builder.append(", receiveTime=");
		builder.append(receiveTime);
		builder.append(", phone=");
		builder.append(phone);
		builder.append("]");
		return builder.toString();
	}

}
