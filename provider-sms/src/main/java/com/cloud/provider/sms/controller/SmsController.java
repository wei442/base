package com.cloud.provider.sms.controller;

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
import com.cloud.provider.sms.base.BaseRestMapResponse;
import com.cloud.provider.sms.page.PageHelperUtil;
import com.cloud.provider.sms.po.Sms;
import com.cloud.provider.sms.rest.request.page.sms.SmsPageRequest;
import com.cloud.provider.sms.rest.request.sms.SmsRequest;
import com.cloud.provider.sms.service.ISmsService;
import com.cloud.provider.sms.vo.sms.SmsVo;
import com.github.pagehelper.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 短信 SmsController
 * @author wei.yong
 */
@Api(tags = "短信")
@RestController
@RequestMapping("/sms")
public class SmsController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//短信 Service
	@Autowired
	private ISmsService smsService;

	/**
	 * 分页查询
	 * @param req
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "分页查询短信列表")
	@RequestMapping(value="/selectListByPage",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse selectListByPage(
		@RequestBody SmsPageRequest req) {
		logger.info("===step1:【分页查询短信列表】(SmsController-selectListByPage)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		Integer pageNum = req.getPageNum();
		Integer pageSize = req.getPageSize();

		Page<?> page = new Page<>(pageNum, pageSize);
		List<Sms> list = smsService.selectListByPage(page, req);
		logger.info("===step2:【分页查询短信列表】(SmsController-selectListByPage)-分页查询短信列表, list.size:{}", list == null ? null : list.size());
		List<SmsVo> dataList = new SmsVo().convertToSmsVoList(list);

		BaseRestMapResponse smsResponse = new BaseRestMapResponse();
		smsResponse.put(PageConstants.PAGE, PageHelperUtil.INSTANCE.getPageVo(list));
		smsResponse.put(PageConstants.DATA_LIST, dataList);
		logger.info("===step3:【分页查询短信列表】(SmsController-selectListByPage)-返回信息, smsResponse:{}", smsResponse);
		return smsResponse;
	}

	/**
	 * 添加短信
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "添加短信")
	@RequestMapping(value="/insert",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse insert(
		@Validated @RequestBody SmsRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【添加短信】(SmsController-insert)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		Sms sms = req.convertToSms();
		int i = smsService.insert(sms);
		logger.info("===step2:【添加短信】(SmsController-insert)-插入短信, i:{}", i);

		BaseRestMapResponse smsResponse = new BaseRestMapResponse();
		logger.info("===step3:【添加短信】(SmsController-insert)-返回信息, smsResponse:{}", smsResponse);
		return smsResponse;
	}

}