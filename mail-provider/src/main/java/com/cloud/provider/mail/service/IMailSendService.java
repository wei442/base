package com.cloud.provider.mail.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

public interface IMailSendService {

	/**
	 * 发送简单邮件
	 * @param simpleMailMessage
	 */
	public void sendSimpleMailMessage(SimpleMailMessage simpleMailMessage);

	/**
	 * 创建附件信息
	 * @return MimeMessage
	 */
	public MimeMessage createMimeMessage();

	/**
	 * 发送附件邮件
	 * @param mimeMessage
	 */
	public void sendMimeMessage(MimeMessage mimeMessage);

}