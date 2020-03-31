package com.cloud.provider.mail.service;

import java.util.List;

import com.cloud.provider.mail.po.Mail;
import com.cloud.provider.mail.po.MailAttachment;
import com.cloud.provider.mail.rest.page.mail.MailPageRequest;
import com.github.pagehelper.Page;

public interface IMailService {

    /**
	 * 分页查询
	 * @param page
	 * @param param
	 * @return List<Mail>
	 */
	public List<Mail> selectListByPage(Page<?> page, MailPageRequest param);

	/**
	 * 不分页查询
	 * @param param
	 * @return List<Mail>
	 */
	public List<Mail> selectList(MailPageRequest param);

	/**
	 * 插入邮件及邮件附件
	 * @param mail
	 * @param mailAttachmentList
	 * @return Integer
	 */
	public Integer insert(Mail mail,List<MailAttachment> mailAttachmentList);

}