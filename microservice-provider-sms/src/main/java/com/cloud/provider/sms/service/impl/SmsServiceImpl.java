package com.cloud.provider.sms.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.common.enums.safe.SafeResultEnum;
import com.cloud.provider.sms.dao.SmsMapper;
import com.cloud.provider.sms.po.Sms;
import com.cloud.provider.sms.po.SmsExample;
import com.cloud.provider.sms.rest.request.page.sms.SmsPageRequest;
import com.cloud.provider.sms.service.ISmsService;
import com.cloud.provider.sms.util.Assert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class SmsServiceImpl implements ISmsService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//短信 Mapper
	@Autowired
	private SmsMapper smsMapper;

    /**
	 * 分页查询
	 * @param page
	 * @param param
	 * @return List<Sms>
	 */
	@Override
	public List<Sms> selectListByPage(Page<?> page, SmsPageRequest param) {
		logger.info("(SmsService-selectListByPage)-分页查询-传入参数, page:{}, param:{}", page, param);
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		SmsExample example = new SmsExample();
		example.setOrderByClause(" id desc ");
		SmsExample.Criteria criteria = example.createCriteria();
		if(param != null) {
			if(StringUtils.isNotBlank(param.getMobile())) {
				criteria.andMobileEqualTo(param.getMobile());
			}
		}
		List<Sms> list = smsMapper.selectByExample(example);
		return list;
	}

	/**
	 * 插入短信信息
	 * @param sms
	 * @return Integer
	 */
	@Override
	public Integer insert(Sms sms) {
		logger.info("(SmsService-insert)-插入短信信息-传入参数, sms:{}", sms);
		sms.setCreateTime(new Date());
		sms.setUpdateTime(new Date());
		int i = smsMapper.insertSelective(sms);
		Assert.thanOrEqualZreo(i, SafeResultEnum.DATABASE_ERROR);
		return i;
	}

	/**
	 * 修改短信信息
	 * @param sms
	 * @return Integer
	 */
	@Override
	public Integer modify(Sms sms) {
		logger.info("(SmsService-modify)-修改短信信息-传入参数, sms:{}", sms);
		sms.setUpdateTime(new Date());
		int i = smsMapper.updateByPrimaryKey(sms);
		Assert.thanOrEqualZreo(i, SafeResultEnum.DATABASE_ERROR);
		return i;
	}

}