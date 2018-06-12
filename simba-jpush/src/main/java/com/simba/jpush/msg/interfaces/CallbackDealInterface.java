package com.simba.jpush.msg.interfaces;

import com.simba.jpush.msg.model.ReceiveMsg;
import com.simba.jpush.msg.model.SendResult;
import com.simba.jpush.msg.model.TemplateAuditResult;

/**
 * 模板回调处理接口
 * 
 * @author caozhejun
 *
 */
public interface CallbackDealInterface {

	/**
	 * 处理发送短信结果
	 * 
	 * @param result
	 */
	void sendResult(SendResult result);

	/**
	 * 处理用户发送过来的短信
	 * 
	 * @param msg
	 */
	void receiveMsg(ReceiveMsg msg);

	/**
	 * 处理模板审核结果
	 * 
	 * @param result
	 */
	void templateAuditResult(TemplateAuditResult result);

}
