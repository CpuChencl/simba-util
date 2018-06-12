package com.simba.jpush.msg.model;

/**
 * 模板审核结果
 * 
 * @author caozhejun
 *
 */
public class TemplateAuditResult {

	/**
	 * 模板ID
	 */
	private int tempId;

	/**
	 * 模板状态，1代表审核通过，2代表审核不通过
	 */
	private int status;

	/**
	 * 审核不通过的原因
	 */
	private String refuseReason;

	public int getTempId() {
		return tempId;
	}

	public void setTempId(int tempId) {
		this.tempId = tempId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TemplateAuditResult [tempId=");
		builder.append(tempId);
		builder.append(", status=");
		builder.append(status);
		builder.append(", refuseReason=");
		builder.append(refuseReason);
		builder.append("]");
		return builder.toString();
	}

}
