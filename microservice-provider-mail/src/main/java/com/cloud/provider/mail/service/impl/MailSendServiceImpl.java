package com.cloud.provider.mail.service.impl;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.cloud.provider.mail.service.IMailSendService;

@Service
public class MailSendServiceImpl implements IMailSendService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//邮件发送
	@Autowired
    private JavaMailSender mailSender;

	/**
	 * 发送简单邮件
	 * @param simpleMailMessage
	 */
	@Override
	public void sendSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		logger.info("(MailSendService-sendSimpleMailMessage)-传入参数, simpleMailMessage:{}", simpleMailMessage);
		mailSender.send(simpleMailMessage);
	}

	/**
	 * 创建附件信息
	 * @return MimeMessage
	 */
	@Override
	public MimeMessage createMimeMessage() {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		logger.info("(MailSendService-createMimeMessage)-返回信息, mimeMessage:{}", mimeMessage);
		return mimeMessage;
	}

	/**
	 * 发送附件邮件
	 * @param mimeMessage
	 */
	@Override
	public void sendMimeMessage(MimeMessage mimeMessage) {
		logger.info("(MailSendService-sendMimeMessage)-传入参数, mimeMessage:{}", mimeMessage);
		mailSender.send(mimeMessage);
	}

}