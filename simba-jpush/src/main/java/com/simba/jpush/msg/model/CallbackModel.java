package com.simba.jpush.msg.model;

/**
 * 回调参数对象
 * 
 * @author caozhejun
 *
 */
public class CallbackModel {

	/**
	 * 随机长整数
	 */
	private long nonce;

	/**
	 * 签名，结合 appKey、appMasterSecret、nonce、timestamp 生成 将
	 * appKey、appMasterSecret、nonce、timestamp 的值代入生成字符串
	 * appKey={appKey}&appMasterSecret={appMasterSecret}&nonce={nonce}&timestamp={timestamp}；
	 * 对生成的字符串进行 sha1 加密；
	 */
	private String signature;

	/**
	 * 当前时间戳，毫秒值
	 */
	private long timestamp;

	/**
	 * 通知类型（SMS_REPORT/SMS_REPLY/SMS_TEMPLATE）
	 */
	private String type;

	/**
	 * 通知内容，json 字符串，开发者可以根据 type 反序列化 data
	 */
	private String data;

	public long getNonce() {
		return nonce;
	}

	public void setNonce(long nonce) {
		this.nonce = nonce;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CallbackModel [nonce=");
		builder.append(nonce);
		builder.append(", signature=");
		builder.append(signature);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", type=");
		builder.append(type);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
