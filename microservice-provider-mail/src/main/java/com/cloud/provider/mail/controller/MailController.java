package com.cloud.provider.mail.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.PageConstants;
import com.cloud.provider.mail.base.BaseRestMapResponse;
import com.cloud.provider.mail.page.PageHelperUtil;
import com.cloud.provider.mail.po.Mail;
import com.cloud.provider.mail.po.MailAttachment;
import com.cloud.provider.mail.rest.page.mail.MailPageRequest;
import com.cloud.provider.mail.rest.request.mail.MailRequest;
import com.cloud.provider.mail.service.IMailService;
import com.cloud.provider.mail.vo.mail.MailVo;
import com.github.pagehelper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 邮件 MailController
 * @author wei.yong
 */
@Api(tags = "邮件")
@RestController
@RequestMapping("/mail")
public class MailController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//邮件 Service
	@Autowired
	private IMailService mailService;

	/**
	 * 分页查询
	 * @param req
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "分页查询邮件列表")
	@RequestMapping(value="/selectListByPage",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse selectListByPage(
		@RequestBody MailPageRequest req) {
		logger.info("===step1:【分页查询邮件列表】(MailController-selectListByPage)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		Integer pageNum = req.getPageNum();
		Integer pageSize = req.getPageSize();

		Page<?> page = new Page<>(pageNum, pageSize);
		List<Mail> list = mailService.selectListByPage(page, req);
		logger.info("===step2:【分页查询邮件列表】(MailController-selectListByPage)-分页查询邮件列表, list.size:{}", list == null ? null : list.size());
		List<MailVo> dataList = new MailVo().convertToMailVoList(list);

		BaseRestMapResponse mailResponse = new BaseRestMapResponse();
		mailResponse.put(PageConstants.PAGE, PageHelperUtil.INSTANCE.getPageVo(list));
		mailResponse.put(PageConstants.DATA_LIST, dataList);
		logger.info("===step3:【分页查询邮件列表】(MailController-selectListByPage)-返回信息, mailResponse:{}", mailResponse);
		return mailResponse;
	}

	/**
	 * 添加邮件
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "添加邮件")
	@RequestMapping(value="/insert",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse insert(@Validated @RequestBody MailRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【插入邮件】(MailController-insert)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		Mail mail = req.convertToMail();
		List<MailAttachment> mailAttachmentList = req.convertToMailAttachmentList();
        mail.setHost(this.mailHost);
        mail.setUserName(this.mailUsername);
        mail.setPassword(this.mailPassword);
        mail.setMailFrom(this.mailUsername);
		int i = mailService.insert(mail, mailAttachmentList);
		logger.info("===step2:【插入邮件】(MailController-insert)-插入邮件, i:{}", i);

        BaseRestMapResponse mailResponse = new BaseRestMapResponse();
		logger.info("===step3:【插入邮件】(MailController-insert)-返回信息, mailResponse:{}", mailResponse);
		return mailResponse;
	}

}