package com.cloud.provider.sms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.cloud.provider.sms.service.ISmsSendService;

@Service
public class SmsSendServiceImpl implements ISmsSendService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//rest模板
	@Autowired
	private RestTemplate restTemplate;

	//sms https访问Url
	@Value("${sms.https.url}")
	private String smsHttpsUrl;

	/**
	 * 短信发送（1）
	 * @param params
	 * @return JSONObject
	 */
	@Override
	public JSONObject submit(String params) {
		logger.info("(SmsSendService-submit)-短信发送-传入参数, smsHttpsUrl:{}, params:{}", this.smsHttpsUrl, params);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"));
		HttpEntity<String> httpEntity = new HttpEntity<String>(params, headers);
		JSONObject response = this.restTemplate.postForObject(this.smsHttpsUrl+"/services/susunrs/submit", httpEntity, JSONObject.class);
		logger.info("(SmsSendService-submit)-短信发送-boot返回信息, response:{}", JSONObject.toJSONString(response));
        return response;
	}

	/**
	 * 短信发送（2）
	 * @param params
	 * @return JSONObject
	 */
	@Override
	public JSONObject submitTo(String params) {
		logger.info("(SmsSendService-submitTo)-短信发送-传入参数, smsHttpsUrl:{}, params:{}", this.smsHttpsUrl, params);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"));
		HttpEntity<String> httpEntity = new HttpEntity<String>(params, headers);
		JSONObject response = this.restTemplate.postForObject(this.smsHttpsUrl+"/services/susunrs/submitto", httpEntity, JSONObject.class, params);
		logger.info("(SmsSendService-submitTo)-短信发送-boot返回信息, response:{}", JSONObject.toJSONString(response));
        return response;
	}

	/**
	 * 短信定时发送
	 * @param params
	 * @return
	 */
	@Override
	public JSONObject submitToTime(String params) {
		logger.info("(SmsSendService-submitToTime)-短信定时发送-传入参数, smsHttpsUrl:{}, params:{}", this.smsHttpsUrl, params);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"));
		HttpEntity<String> httpEntity = new HttpEntity<String>(params, headers);
		JSONObject response = this.restTemplate.postForObject(this.smsHttpsUrl+"/services/susunrs/submittotime", httpEntity, JSONObject.class);
		logger.info("(SmsSendService-submitToTime)-短信定时发送-boot返回信息, response:{}", JSONObject.toJSONString(response));
        return response;
	}

	/**
	 * 接收短信回执
	 * @param params
	 * @return JSONObject
	 */
	@Override
	public JSONObject report(String params) {
		logger.info("(SmsSendService-report)-接收短信回执-传入参数, smsHttpsUrl:{}, params:{}", this.smsHttpsUrl, params);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"));
		HttpEntity<String> httpEntity = new HttpEntity<String>(params, headers);
		JSONObject response = this.restTemplate.postForObject(this.smsHttpsUrl+"/services/susunrs/report", httpEntity, JSONObject.class);
		logger.info("(SmsSendService-report)-接收短信回执-boot返回信息, response:{}", JSONObject.toJSONString(response));
        return response;
	}

	/**
	 * 接收回复短信
	 * @param params
	 * @return JSONObject
	 */
	@Override
	public JSONObject reply(String params) {
		logger.info("(SmsSendService-reply)-接收回复短信-传入参数, smsHttpsUrl:{}, params:{}", this.smsHttpsUrl, params);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"));
		HttpEntity<String> httpEntity = new HttpEntity<String>(params, headers);
		JSONObject response = this.restTemplate.postForObject(this.smsHttpsUrl+"/services/susunrs/reply", httpEntity, JSONObject.class);
		logger.info("(SmsSendService-reply)-接收回复短信-boot返回信息, response:{}", JSONObject.toJSONString(response));
        return response;
	}

	/**
	 * 查询当前余额
	 * @param params
	 * @return JSONObject
	 */
	@Override
	public JSONObject balance(String params) {
		logger.info("(SmsSendService-balance)-查询当前余额-传入参数, smsHttpsUrl:{}, params:{}", this.smsHttpsUrl, params);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE+";charset=UTF-8"));
		HttpEntity<String> httpEntity = new HttpEntity<String>(params, headers);
		JSONObject response = this.restTemplate.postForObject(this.smsHttpsUrl+"/services/susunrs/balance", httpEntity, JSONObject.class);
		logger.info("(SmsSendService-balance)-查询当前余额-boot返回信息, response:{}", JSONObject.toJSONString(response));
        return response;
	}

}