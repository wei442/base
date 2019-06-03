package com.cloud.provider.sms.service;

import com.alibaba.fastjson.JSONObject;

public interface ISmsSendService {

	/**
	 * 短信发送（1）
	 * @param params
	 * @return JSONObject
	 */
	public JSONObject submit(String params);

	/**
	 * 短信发送（2）
	 * @param params
	 * @return JSONObject
	 */
	public JSONObject submitTo(String params);

	/**
	 * 短信定时发送
	 * @param params
	 * @return JSONObject
	 */
	public JSONObject submitToTime(String params);

	/**
	 * 接收短信回执
	 * @param params
	 * @return JSONObject
	 */
	public JSONObject report(String params);

	/**
	 * 接收回复短信
	 * @param params
	 * @return JSONObject
	 */
	public JSONObject reply(String params);

	/**
	 * 查询当前余额
	 * @param params
	 * @return JSONObject
	 */
	public JSONObject balance(String params);

}