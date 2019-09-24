package com.cloud.provider.sms.service;

import java.util.List;

import com.cloud.provider.sms.po.Sms;
import com.cloud.provider.sms.rest.request.page.sms.SmsPageRequest;
import com.github.pagehelper.Page;

public interface ISmsService {

    /**
	 * 分页查询
	 * @param page
	 * @param param
	 * @return List<Sms>
	 */
	public List<Sms> selectListByPage(Page<?> page, SmsPageRequest param);

	/**
	 * 插入短信信息
	 * @param sms
	 * @return Integer
	 */
	public Integer insert(Sms sms);

	/**
	 * 修改短信信息
	 * @param sms
	 * @return Integer
	 */
	public Integer modify(Sms sms);

}