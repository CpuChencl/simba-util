package com.simba.jpush.msg.util;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.simba.exception.BussException;
import com.simba.jpush.msg.model.SendMsgModel;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.JSMSClient;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.common.model.BatchSMSPayload;
import cn.jsms.api.common.model.BatchSMSPayload.Builder;
import cn.jsms.api.common.model.BatchSMSResult;
import cn.jsms.api.common.model.RecipientPayload;
import cn.jsms.api.common.model.SMSPayload;
import cn.jsms.api.schedule.model.ScheduleResult;
import cn.jsms.api.schedule.model.ScheduleSMSPayload;
import cn.jsms.api.schedule.model.ScheduleSMSResult;
import cn.jsms.api.template.SendTempSMSResult;
import cn.jsms.api.template.TempSMSResult;
import cn.jsms.api.template.TemplatePayload;

/**
 * jpush短信工具类
 * 
 * @author caozhejun
 *
 */
@Component
public class JpushMsgUtil {

	@Value("${jpush.key}")
	private String key;

	@Value("${jpush.secret}")
	private String secret;

	@Value("${jpush.timeout}")
	private int timeout;

	private JSMSClient client;

	private boolean enabled = false;

	public JSMSClient getClient() {
		return client;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@PostConstruct
	private void init() {
		if (StringUtils.isEmpty(secret) || StringUtils.isEmpty(key)) {
			return;
		}
		client = new JSMSClient(secret, key);
		enabled = true;
	}

	/**
	 * 发送验证码手机短信
	 * 
	 * @param mobile
	 *            手机号
	 * @param templateId
	 *            模板ID
	 * @return
	 * @throws APIRequestException
	 * @throws APIConnectionException
	 */
	public String sendSMSCode(String mobile, int templateId) throws APIConnectionException, APIRequestException {
		SMSPayload payload = SMSPayload.newBuilder().setMobileNumber(mobile).setTempId(templateId).setTTL(timeout).build();
		SendSMSResult result = client.sendSMSCode(payload);
		return result.getMessageId();
	}

	/**
	 * 检查验证码是否正确
	 * 
	 * @param messageId
	 *            发送短信时返回的消息id
	 * @param code
	 *            验证码
	 * @return
	 * @throws APIRequestException
	 * @throws APIConnectionException
	 */
	public boolean check(String messageId, String code) throws APIConnectionException, APIRequestException {
		ValidSMSResult result = client.sendValidSMSCode(messageId, code);
		return result.getIsValid();
	}

	/**
	 * 发送语音验证码
	 * 
	 * @param mobile
	 *            手机号
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public String sendVoiceSMSCode(String mobile) throws APIConnectionException, APIRequestException {
		SMSPayload payload = SMSPayload.newBuilder().setMobileNumber(mobile).setTTL(timeout).setVoiceLang(2).build();
		SendSMSResult result = client.sendVoiceSMSCode(payload);
		return result.getMessageId();
	}

	/**
	 * 发送语音验证码
	 * 
	 * @param mobile
	 *            手机号
	 * @param code
	 *            语音验证码的值，验证码仅支持 4-8 个数字
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public String sendVoiceSMSCode(String mobile, String code) throws APIConnectionException, APIRequestException {
		SMSPayload payload = SMSPayload.newBuilder().setMobileNumber(mobile).setTTL(timeout).setVoiceLang(2).setCode(code).build();
		SendSMSResult result = client.sendVoiceSMSCode(payload);
		return result.getMessageId();
	}

	/**
	 * 发送模板消息
	 * 
	 * @param mobile
	 *            手机号
	 * @param templateId
	 *            模板ID
	 * @param values
	 *            模板中的值
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public String sendTemplateSMS(String mobile, int templateId, Map<String, String> values) throws APIConnectionException, APIRequestException {
		SMSPayload payload = SMSPayload.newBuilder().setMobileNumber(mobile).setTTL(timeout).setTempId(templateId).setTempPara(values).build();
		SendSMSResult result = client.sendTemplateSMS(payload);
		return result.getMessageId();
	}

	/**
	 * 批量发送短信
	 * 
	 * @param templateId
	 *            模板ID
	 * @param senders
	 *            发送者列表
	 * @return
	 * @throws APIRequestException
	 * @throws APIConnectionException
	 */
	public BatchSMSResult sendBatchTemplateSMS(int templateId, List<SendMsgModel> senders) throws APIConnectionException, APIRequestException {
		Builder builder = BatchSMSPayload.newBuilder().setTempId(templateId);
		senders.forEach((SendMsgModel sender) -> {
			builder.addRecipient(RecipientPayload.newBuilder().setMobile(sender.getMobile()).setTempPara(sender.getValues()).build());
		});
		BatchSMSPayload payload = builder.build();
		return client.sendBatchTemplateSMS(payload);
	}

	/**
	 * 创建模板 模板内容，注意：根据运营商规定下发短信的内容不能超过350字符
	 * 
	 * @param template
	 *            模板内容，如 您好，您的验证码是{{code}}，2分钟内有效！
	 * @param type
	 *            模板类型，1为验证码类，2为通知类，3为营销类
	 * @param remark
	 *            备注，长度限制为500字符
	 * @return
	 * @throws APIRequestException
	 * @throws APIConnectionException
	 */
	public int createTemplate(String template, int type, String remark) throws APIConnectionException, APIRequestException {
		checkTemplate(template, type, remark);
		TemplatePayload payload = TemplatePayload.newBuilder().setRemark(remark).setTemplate(template).setTTL(timeout).setType(type).build();
		SendTempSMSResult result = client.createTemplate(payload);
		return result.getTempId();
	}

	private void checkTemplate(String template, int type, String remark) {
		if (template.length() > 350) {
			throw new BussException("模板内容不能超过350字符");
		}
		if (type < 1 || type > 3) {
			throw new BussException("模板类型，1为验证码类，2为通知类，3为营销类");
		}
		if (remark != null && remark.length() > 500) {
			throw new BussException("备注，长度限制为500字符");
		}
	}

	/**
	 * 修改短信模板
	 * 
	 * @param templateId
	 *            模板ID
	 * @param template
	 *            模板内容，如 您好，您的验证码是{{code}}，2分钟内有效！
	 * @param type
	 *            模板类型，1为验证码类，2为通知类，3为营销类
	 * @param remark
	 *            备注，长度限制为500字符
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public int updateTemplate(int templateId, String template, int type, String remark) throws APIConnectionException, APIRequestException {
		checkTemplate(template, type, remark);
		TemplatePayload payload = TemplatePayload.newBuilder().setRemark(remark).setTempId(templateId).setTemplate(template).setTTL(timeout).setType(type).build();
		SendTempSMSResult result = client.updateTemplate(payload, templateId);
		return result.getTempId();
	}

	/**
	 * 检查模板状态
	 * 
	 * @param templateId
	 *            模板ID
	 * @return 状态，0为审核中，1为审核通过，2为审核不通过
	 * @throws APIRequestException
	 * @throws APIConnectionException
	 */
	public int checkTemplateStatus(int templateId) throws APIConnectionException, APIRequestException {
		TempSMSResult result = client.checkTemplate(templateId);
		return result.getStatus();
	}

	/**
	 * 删除模板
	 * 
	 * @param templateId
	 *            模板ID
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public void deleteTemplate(int templateId) throws APIConnectionException, APIRequestException {
		client.deleteTemplate(templateId);
	}

	/**
	 * 定时发送短信
	 * 
	 * @param date
	 *            发送时间 格式为 yyyy-MM-dd HH:mm:ss
	 * @param mobile
	 *            手机号
	 * @param templateId
	 *            模板ID
	 * @param values
	 *            模板中的值
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public String sendScheduleSMS(String date, String mobile, int templateId, Map<String, String> values) throws APIConnectionException, APIRequestException {
		ScheduleSMSPayload payload = ScheduleSMSPayload.newBuilder().setMobileNumber(mobile).setSendTime(date).setTempId(templateId).setTempPara(values).build();
		ScheduleResult result = client.sendScheduleSMS(payload);
		return result.getScheduleId();
	}

	/**
	 * 批量定时发送短信
	 * 
	 * @param date
	 *            发送时间 格式为 yyyy-MM-dd HH:mm:ss
	 * @param templateId
	 *            模板ID
	 * @param senders
	 *            发送者列表
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public BatchSMSResult sendBatchScheduleSMS(String date, int templateId, List<SendMsgModel> senders) throws APIConnectionException, APIRequestException {
		cn.jsms.api.schedule.model.ScheduleSMSPayload.Builder builder = ScheduleSMSPayload.newBuilder().setTempId(templateId).setSendTime(date);
		senders.forEach((SendMsgModel sender) -> {
			builder.addRecipient(RecipientPayload.newBuilder().setMobile(sender.getMobile()).setTempPara(sender.getValues()).build());
		});
		ScheduleSMSPayload payload = builder.build();
		return client.sendBatchScheduleSMS(payload);
	}

	/**
	 * 单条定时短信修改
	 * 
	 * @param scheduleId
	 *            定时任务ID
	 * @param date
	 *            发送时间 格式为 yyyy-MM-dd HH:mm:ss
	 * @param mobile
	 *            手机号
	 * @param templateId
	 *            模板ID
	 * @param values
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public String updateScheduleSMS(String scheduleId, String date, String mobile, int templateId, Map<String, String> values) throws APIConnectionException, APIRequestException {
		ScheduleSMSPayload payload = ScheduleSMSPayload.newBuilder().setMobileNumber(mobile).setSendTime(date).setTempId(templateId).setTempPara(values).build();
		ScheduleResult result = client.updateScheduleSMS(payload, scheduleId);
		return result.getScheduleId();
	}

	/**
	 * 批量定时短信修改
	 * 
	 * @param scheduleId
	 *            定时任务ID
	 * @param date
	 *            发送时间 格式为 yyyy-MM-dd HH:mm:ss
	 * @param templateId
	 *            模板ID
	 * @param senders
	 *            发送者列表
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public BatchSMSResult updateBatchScheduleSMS(String scheduleId, String date, int templateId, List<SendMsgModel> senders) throws APIConnectionException, APIRequestException {
		cn.jsms.api.schedule.model.ScheduleSMSPayload.Builder builder = ScheduleSMSPayload.newBuilder().setTempId(templateId).setSendTime(date);
		senders.forEach((SendMsgModel sender) -> {
			builder.addRecipient(RecipientPayload.newBuilder().setMobile(sender.getMobile()).setTempPara(sender.getValues()).build());
		});
		ScheduleSMSPayload payload = builder.build();
		return client.updateBatchScheduleSMS(payload, scheduleId);
	}

	/**
	 * 定时任务查询
	 * 
	 * @param scheduleId
	 *            定时任务ID
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public ScheduleSMSResult getScheduleSMS(String scheduleId) throws APIConnectionException, APIRequestException {
		return client.getScheduleSMS(scheduleId);
	}

	/**
	 * 删除定时任务
	 * 
	 * @param scheduleId
	 *            定时任务ID
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public void deleteScheduleSMS(String scheduleId) throws APIConnectionException, APIRequestException {
		client.deleteScheduleSMS(scheduleId);
	}

	/**
	 * 查询短信余量
	 * 
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public int getSMSBalance() throws APIConnectionException, APIRequestException {
		return client.getSMSBalance().getDevBalance();
	}

	/**
	 * 查询应用余量
	 * 
	 * @return
	 * @throws APIConnectionException
	 * @throws APIRequestException
	 */
	public int getAppSMSBalance() throws APIConnectionException, APIRequestException {
		return client.getAppSMSBalance().getAppBalance();
	}
}