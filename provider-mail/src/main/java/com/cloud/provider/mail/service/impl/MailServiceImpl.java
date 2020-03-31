package com.cloud.provider.mail.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.provider.mail.dao.MailAttachmentMapper;
import com.cloud.provider.mail.dao.MailMapper;
import com.cloud.provider.mail.po.Mail;
import com.cloud.provider.mail.po.MailAttachment;
import com.cloud.provider.mail.po.MailExample;
import com.cloud.provider.mail.rest.page.mail.MailPageRequest;
import com.cloud.provider.mail.service.IMailService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class MailServiceImpl implements IMailService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//邮件 Mapper
	@Autowired
	private MailMapper mailMapper;

	//邮件附件 Mapper
	@Autowired
	private MailAttachmentMapper mailAttachmentMapper;

    /**
	 * 分页查询
	 * @param page
	 * @param param
	 * @return List<Mail>
	 */
	@Override
	public List<Mail> selectListByPage(Page<?> page, MailPageRequest param) {
		logger.info("(MailService-selectListByPage)-分页查询-传入参数, page:{}, param:{}", page, param);
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		MailExample example = new MailExample();
		example.setOrderByClause(" id desc ");
		MailExample.Criteria criteria = example.createCriteria();
		if(param != null) {
		}
		List<Mail> list = mailMapper.selectByExample(example);
		return list;
	}

	/**
	 * 不分页查询
	 * @param param
	 * @return List<Mail>
	 */
	@Override
	public List<Mail> selectList(MailPageRequest param) {
		logger.info("(MailService-selectList)-不分页查询-传入参数, param:{}", param);
		MailExample example = new MailExample();
		example.setOrderByClause(" id desc ");
		MailExample.Criteria criteria = example.createCriteria();
		if(param != null) {
		}
		List<Mail> list = mailMapper.selectByExample(example);
		return list;
	}

	/**
	 * 插入邮件及邮件附件
	 * @param mail
	 * @param mailAttachmentList
	 * @return Integer
	 */
	@Override
	public Integer insert(Mail mail,List<MailAttachment> mailAttachmentList) {
		logger.info("(MailService-insert)-插入邮件附件列表-传入参数, mail:{}, mailAttachmentList:{}", mail, mailAttachmentList);
		mail.setSendTime(new Date());
		mail.setCreateTime(new Date());
		mail.setUpdateTime(new Date());
		int i = mailMapper.insertSelective(mail);
		Integer mailId = mail.getId();

		if(mailAttachmentList != null && !mailAttachmentList.isEmpty()) {
    		ListIterator<MailAttachment> it = mailAttachmentList.listIterator();
    		while(it.hasNext()) {
    			MailAttachment mailAttachment = it.next();
    			mailAttachment.setMailId(mailId);
    			mailAttachment.setCreateTime(new Date());
    			mailAttachment.setUpdateTime(new Date());

    			mailAttachmentMapper.insertSelective(mailAttachment);
    		}
		}

		return i;
	}

 	/**
  	 * 写入文件
  	 * @param createFile
  	 * @param bytes
  	 * @return boolean
  	 */
  	public boolean writeFile(File createFile,byte[] bytes) {
  		logger.info("(MailService-writeFile)-写入文件-传入参数, createFile = {}", createFile);
          try {
          	//写入文件
  			FileUtils.writeByteArrayToFile(createFile, bytes, false);
  		} catch (IOException e) {
  			logger.error("(MailService-writeFile)-写入文件-异常, Exception = {}, message = {}", e, e.getMessage());
  			return false;
  		}
  	    return true;
  	}

}