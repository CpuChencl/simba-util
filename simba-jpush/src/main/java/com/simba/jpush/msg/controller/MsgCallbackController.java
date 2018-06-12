package com.simba.jpush.msg.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simba.exception.BussException;
import com.simba.framework.util.applicationcontext.ApplicationContextUtil;
import com.simba.framework.util.code.EncryptUtil;
import com.simba.framework.util.json.FastJsonUtil;
import com.simba.framework.util.json.JsonResult;
import com.simba.jpush.msg.interfaces.CallbackDealInterface;
import com.simba.jpush.msg.model.CallbackModel;
import com.simba.jpush.msg.model.ReceiveMsg;
import com.simba.jpush.msg.model.SendResult;
import com.simba.jpush.msg.model.TemplateAuditResult;

/**
 * 极光短信回调Controller
 * 
 * @author caozhejun
 *
 */
@RestController
@RequestMapping("/msgCallback")
public class MsgCallbackController {

	private static final Log logger = LogFactory.getLog(MsgCallbackController.class);

	@Value("${jpush.key}")
	private String key;

	@Value("${jpush.secret}")
	private String secret;

	@RequestMapping("/receive")
	public String receive(CallbackModel model, String echostr) {
		if (StringUtils.isNotEmpty(echostr)) {
			return echostr;
		}
		check(model);
		CallbackDealInterface callback = ApplicationContextUtil.getBean(CallbackDealInterface.class);
		if (callback == null) {
			logger.warn("没有实现处理极光短信回调的实现类");
			return new JsonResult().toJson();
		}
		String type = model.getType();
		String data = model.getData();
		if ("SMS_REPLY".equals(type)) {
			ReceiveMsg msg = FastJsonUtil.toObject(data, ReceiveMsg.class);
			callback.receiveMsg(msg);
		} else if ("SMS_REPORT".equals(type)) {
			SendResult result = FastJsonUtil.toObject(data, SendResult.class);
			callback.sendResult(result);
		} else if ("SMS_TEMPLATE".equals(type)) {
			TemplateAuditResult result = FastJsonUtil.toObject(data, TemplateAuditResult.class);
			callback.templateAuditResult(result);
		} else {
			throw new BussException("极光短信推送的回调类型错误:" + type);
		}
		return new JsonResult().toJson();
	}

	private void check(CallbackModel model) {
		logger.info("接收到极光短信的回调:" + model.toString());
		String sign = model.getSignature();
		String text = "appKey=" + key + "&appMasterSecret=" + secret + "&nonce=" + model.getNonce() + "&timestamp=" + model.getTimestamp();
		String result = EncryptUtil.sha1(text);
		logger.info("计算后结果:" + result);
		if (!sign.equalsIgnoreCase(result)) {
			throw new BussException("极光短信签名错误:计算后结果[" + result + "],传回来的数据[" + sign + "]");
		}
	}

}
