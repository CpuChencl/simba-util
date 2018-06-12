package com.simba.util;

import java.io.File;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 发送邮件工具类
 * 
 * @author caozhejun
 *
 */
@Component
public class EmailUtil {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.from}")
	private String from;

	/**
	 * 发送普通邮件
	 * 
	 * @param toEmail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param text
	 *            内容
	 */
	public void send(String toEmail, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(toEmail);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

	/**
	 * 发送html内容的邮件
	 * 
	 * @param toEmail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param html
	 *            内容
	 * @throws MessagingException
	 */
	public void sendWithHtml(String toEmail, String subject, String html) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom(from);
		helper.setTo(toEmail);
		helper.setSubject(subject);
		helper.setText(html, true);
		mailSender.send(mimeMessage);
	}

	/**
	 * 发送带附件的邮件
	 * 
	 * @param toEmail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param text
	 *            内容
	 * @param attachments
	 *            附件列表
	 * @throws MessagingException
	 */
	public void sendWithAttachment(String toEmail, String subject, String text, List<File> attachments)
			throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom(from);
		helper.setTo(toEmail);
		helper.setSubject(subject);
		helper.setText(text, true);
		for (File file : attachments) {
			helper.addAttachment(file.getName(), new FileSystemResource(file));
		}
		mailSender.send(mimeMessage);
	}

	/**
	 * 发送带嵌入静态资源的邮件
	 * 
	 * @param toEmail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param text
	 *            内容
	 * @param files
	 *            嵌入式文件(在内容中匹配cid:file.getName())
	 * @throws MessagingException
	 */
	public void sendWithInlineFile(String toEmail, String subject, String text, List<File> files)
			throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom(from);
		helper.setTo(toEmail);
		helper.setSubject(subject);
		helper.setText(text, true);
		for (File file : files) {
			helper.addInline(file.getName(), new FileSystemResource(file));
		}
		mailSender.send(mimeMessage);
	}

	/**
	 * 发送送带附件和嵌入静态资源的邮件
	 * 
	 * @param toEmail
	 *            收件人邮箱
	 * @param subject
	 *            主题
	 * @param text
	 *            内容
	 * @param attachments
	 *            附件列表
	 * @param files
	 *            嵌入式文件(在内容中匹配cid:file.getName())
	 * @throws MessagingException
	 */
	public void sendWithAttachmentAndInlineFile(String toEmail, String subject, String text, List<File> attachments,
			List<File> files) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom(from);
		helper.setTo(toEmail);
		helper.setSubject(subject);
		helper.setText(text, true);
		for (File file : attachments) {
			helper.addAttachment(file.getName(), new FileSystemResource(file));
		}
		for (File file : files) {
			helper.addInline(file.getName(), new FileSystemResource(file));
		}
		mailSender.send(mimeMessage);
	}
}
