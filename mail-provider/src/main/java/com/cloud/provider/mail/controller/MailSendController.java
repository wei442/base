package com.cloud.provider.mail.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ListIterator;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.enums.mail.MailResultEnum;
import com.cloud.provider.mail.base.BaseRestMapResponse;
import com.cloud.provider.mail.rest.request.mail.MailSendRequest;
import com.cloud.provider.mail.service.IMailSendService;
import com.cloud.provider.mail.vo.AttachmentVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 邮件发送 MailSendController
 * @author wei.yong
 * @data 2017/4/10
 */
@Api(tags = "邮件发送")
@RestController
@RequestMapping("/mail/send")
public class MailSendController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//邮件发送 Service
	@Autowired
	private IMailSendService mailSendService;

	/**
	 * 发送普通邮件
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "发送普通邮件")
	@RequestMapping(value="/simple",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sendSimple(@Validated @RequestBody MailSendRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【发送普通邮件】(MailController-sendSimple)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String mailTo = req.getMailTo();
		String subject = req.getSubject();
		String text = req.getText();
		String mailBcc = req.getMailBcc();
		String mailCc = req.getMailCc();

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(this.mailUsername);
		simpleMailMessage.setTo(mailTo);
		simpleMailMessage.setSubject(subject);
		simpleMailMessage.setText(text);
		if(StringUtils.isNotBlank(mailBcc)) simpleMailMessage.setBcc(mailBcc);
		if(StringUtils.isNotBlank(mailCc)) simpleMailMessage.setCc(mailCc);

		mailSendService.sendSimpleMailMessage(simpleMailMessage);
		logger.info("===step2:【发送普通邮件】(MailController-sendSimple)-邮件信息, simpleMailMessage:{}", simpleMailMessage);

		BaseRestMapResponse mailSendResponse = new BaseRestMapResponse();
		logger.info("===step3:【发送普通邮件】(MailController-sendSimple)-返回信息, mailSendResponse:{}", mailSendResponse);
		return mailSendResponse;
	}

	/**
	 * 发送附件邮件
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "发送附件邮件")
	@RequestMapping(value="/mime",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sendMime(@Validated @RequestBody MailSendRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【发送附件邮件】(MailController-sendMime)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String mailTo = req.getMailTo();
		String subject = req.getSubject();
		String text = req.getText();
		String mailBcc = req.getMailBcc();
		String mailCc = req.getMailCc();
		List<AttachmentVo> attachmentList = req.getAttachmentList();

		MimeMessage mimeMessage = mailSendService.createMimeMessage();
		boolean multipart = false;
		if(attachmentList != null && !attachmentList.isEmpty()) {
			 multipart = true;
		 }
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, multipart, StandardCharsets.UTF_8.toString());
			messageHelper.setFrom(new InternetAddress(this.mailUsername, this.mailPersonal, StandardCharsets.UTF_8.toString()));
			messageHelper.setTo(this.convertInternetAddress(mailTo));
	        messageHelper.setSubject(subject);
	        messageHelper.setText(text);
	        if(StringUtils.isNotBlank(mailBcc)) messageHelper.setBcc(this.convertInternetAddress(mailBcc));
	        if(StringUtils.isNotBlank(mailCc)) messageHelper.setCc(this.convertInternetAddress(mailCc));

	        if(attachmentList != null && !attachmentList.isEmpty()) {
	        		ListIterator<AttachmentVo> it = attachmentList.listIterator();
	        		while(it.hasNext()) {
	        			AttachmentVo mailAttachmentVo = it.next();
	        			String fileName = mailAttachmentVo.getFileName();
	        			byte[] bytes = mailAttachmentVo.getBytes();
	        			messageHelper.addAttachment(fileName, new ByteArrayResource(bytes));
	        		}
	        }
			logger.info("===step2:【发送附件邮件】(MailController-sendMime)-邮件信息, messageHelper:{}", messageHelper);
		} catch (MessagingException | UnsupportedEncodingException e) {
			logger.error("===step2.1:【发送附件邮件】(MailController-sendMime)-邮件信息-异常, Exception = {}, message = {}", e, e.getMessage());
			return new BaseRestMapResponse(MailResultEnum.MAIL_SEND_FAIL);
		}

		mailSendService.sendMimeMessage(mimeMessage);

        BaseRestMapResponse mailSendResponse = new BaseRestMapResponse();
		logger.info("===step3:【发送附件邮件】(MailController-sendMime)-返回信息, mailSendResponse:{}", mailSendResponse);
		return mailSendResponse;
	}

	/**
	 * 发送html邮件
	 * @param req
	 * @param request
	 * @param response
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "发送html邮件")
	@RequestMapping(value="/html",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sendHtml(@Validated @RequestBody MailSendRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【发送html邮件】(MailController-sendHtml)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String mailTo = req.getMailTo();
		String subject = req.getSubject();
		String text = req.getText();
		String mailBcc = req.getMailBcc();
		String mailCc = req.getMailCc();
		List<AttachmentVo> attachmentList = req.getAttachmentList();

		MimeMessage mimeMessage = mailSendService.createMimeMessage();
		boolean multipart = false;
		if(attachmentList != null && !attachmentList.isEmpty()) {
			 multipart = true;
		 }
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, multipart, StandardCharsets.UTF_8.toString());
			messageHelper.setFrom(new InternetAddress(this.mailUsername, this.mailPersonal, StandardCharsets.UTF_8.toString()));
			messageHelper.setTo(this.convertInternetAddress(mailTo));
	        messageHelper.setSubject(subject);
	        // true 为 HTML 邮件
	        messageHelper.setText(text, true);
	        if(StringUtils.isNotBlank(mailBcc)) messageHelper.setBcc(this.convertInternetAddress(mailBcc));
	        if(StringUtils.isNotBlank(mailCc)) messageHelper.setCc(this.convertInternetAddress(mailCc));

			if(attachmentList != null && !attachmentList.isEmpty()) {
				ListIterator<AttachmentVo> it = attachmentList.listIterator();
				while(it.hasNext()) {
					AttachmentVo mailAttachmentVo = it.next();
					String fileName = mailAttachmentVo.getFileName();
					byte[] bytes = mailAttachmentVo.getBytes();
					messageHelper.addAttachment(fileName, new ByteArrayResource(bytes));
				}
			}
			logger.info("===step2:【发送html邮件】(MailController-sendHtmlMail)-邮件信息, messageHelper:{}", messageHelper);
		} catch (MessagingException | UnsupportedEncodingException e) {
			logger.error("===step2.1:【发送html邮件】(MailController-sendHtmlMail)-邮件信息-异常, Exception = {}, message = {}", e, e.getMessage());
			return new BaseRestMapResponse(MailResultEnum.MAIL_SEND_FAIL);
		}

		mailSendService.sendMimeMessage(mimeMessage);

		BaseRestMapResponse mailSendResponse = new BaseRestMapResponse();
		logger.info("===step3:【发送html邮件】(MailController-sendHtmlMail)-返回信息, mailSendResponse:{}", mailSendResponse);
		return mailSendResponse;
	}

}