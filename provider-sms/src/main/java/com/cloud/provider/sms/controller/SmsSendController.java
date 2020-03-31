package com.cloud.provider.sms.controller;

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
import com.cloud.provider.sms.base.BaseRestMapResponse;
import com.cloud.provider.sms.rest.request.sms.SmsRequest;
import com.cloud.provider.sms.service.ISmsSendService;

import io.swagger.annotations.Api;

/**
 * 短信发送 SmsSendController
 * @author wei.yong
 */
@Api(tags = "短信发送")
@RestController
@RequestMapping("/sms/send")
public class SmsSendController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//短信
	@Autowired
	private ISmsSendService smsSendService;

	/**
	 * 短信发送
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/sendSms",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sendSms(@Validated @RequestBody SmsRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【短信发送】(BootSmsController-sendSms)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String mobile = req.getMobile();
		String content  = req.getContent();
//		if(StringUtils.isBlank(mobile)) {
//			return new BaseRestResponse(BootConstants.SMS_FIELD_EMPTY, "mobile为空");
//		} else if(StringUtils.isBlank(content)) {
//			return new BaseRestResponse(BootConstants.SMS_FIELD_EMPTY, "content为空");
//		}
//		if(StringUtils.length(mobile) != 11) {
//			return new BaseRestMapResponse(BootConstants.SMS_FIELD_EMPTY, "mobile错误");
//		}

//		List<NameValuePair> nameValuePairLists = new ArrayList<NameValuePair>();
//		nameValuePairLists.add(new BasicNameValuePair("loginid", smsLoginid));
//		nameValuePairLists.add(new BasicNameValuePair("password", smsPassword));
//		nameValuePairLists.add(new BasicNameValuePair("receiver", mobile));
//		nameValuePairLists.add(new BasicNameValuePair("content", content));
//
//	    String params = URLEncodedUtils.format(nameValuePairLists, StandardCharsets.UTF_8);
//
//	    Date sendTime = new Date();
//		//发送短信
//	    JSONObject jsonInfo = smsSendService.submit(params);
//		logger.info("===step2:【短信发送】(BootSmsController-sendSms)-调用smsService-返回信息, jsonInfo:{}", jsonInfo);
//
//		JSONObject result =  jsonInfo.getJSONObject("result");
//
//		String code = Objects.toString(result.get(Constants.CODE), "");
//		String text = Objects.toString(result.get(Constants.TEXT), "");

//		Sms sms = new Sms();
//		//插入数据库
//		sms.setMobile(mobile);
//		sms.setContent(content);
//		sms.setSmsType(SqlSmsConstants.SQL_SMS_TYPE_SINGLE);
//		sms.setSendTime(sendTime);
//		sms.setSource(source);
//		try {
//			sms = smsService.insertSms(sms);
//			logger.info("=====step3:【短信发送】(BootSmsController-sendSms)-插入短信-返回信息, sms:{}", sms);
//		} catch (BootServiceException e) {
//			logger.error("===step3.0.1:【短信发送】(BootSmsController-sendSms)-插入短信-事务性异常, Exception = {}, message = {}", e, e.getMessage());
//			String errorCode = e.getErrorCode();
//        	if(StringUtils.equals(errorCode, BootConstants.BOOT_SYSTEM_ERROR)) {
//        		return new BaseRestMapResponse(BootConstants.BOOT_SYSTEM_ERROR, BootConstants.BOOT_SYSTEM_ERROR_MSG);
//        	}
//		}

//		if (!StringUtils.equals(code, Constants.CC_OK)) {
//			sms.setStatus(SqlSmsConstants.SQL_SMS_STATUS_FAIL);
//				int i = smsService.modifySms(sms);
//				logger.info("=====step3.1:【短信发送】(BootSmsController-sendSms)-修改短信-返回信息, i:{}", i);
//
//			return new BaseRestMapResponse(BootConstants.SMS_ERROR, BootConstants.SMS_ERROR_MSG);
//		}

		BaseRestMapResponse smsSendResponse = new BaseRestMapResponse();
//		SmsResponse smsSendResponse = new SmsResponse();
//		smsSendResponse.setText(text);
		logger.info("=====step4:【短信发送】(BootSmsController-sendSms)-返回信息, smsSendResponse:{}", smsSendResponse);
		return smsSendResponse;
	}

//	/**
//	 * 短信发送群发
//	 * @param req
//	 * @param request
//	 * @param response
//	 * @return
//	 */
//	@RequestMapping(value="/groupSendSms",method={RequestMethod.POST})
//	@ResponseBody
//	public BaseRestMapResponse groupSendSms(@Validated @RequestBody SmsSendTimeRequest req,
//		BindingResult bindingResult) {
//		logger.info("===step1:【群发短信】(BootSmsController-groupSendSms)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));
//
//		String mobile = req.getMobile();
//		String content  = req.getContent();
////		if(StringUtils.isBlank(mobile)) {
////			return new BaseRestResponse(BootConstants.SMS_FIELD_EMPTY, "mobile为空");
////		} else if(StringUtils.isBlank(content)) {
////			return new BaseRestResponse(BootConstants.SMS_FIELD_EMPTY, "content为空");
////		}
//
//		String[] mobiles = StringUtils.split(mobile, Constants.SEMICOLON);
//
//		List<NameValuePair> nameValuePairLists = new ArrayList<NameValuePair>();
//		nameValuePairLists.add(new BasicNameValuePair("loginid", smsLoginid));
//		nameValuePairLists.add(new BasicNameValuePair("password", smsPassword));
//		nameValuePairLists.add(new BasicNameValuePair("receiver", mobile));
//		nameValuePairLists.add(new BasicNameValuePair("content", content));
//
//	    String params = URLEncodedUtils.format(nameValuePairLists, StandardCharsets.UTF_8);
//
//		// 发送短信
//	    JSONObject jsonInfo = smsSendService.submitTo(params);
//		logger.info("===step2:【群发短信】(BootSmsController-submitTo)-调用smsService, jsonInfo:{}", jsonInfo);
//
////		JSONObject result =  jsonInfo.getJSONObject("result");
////		String code = Objects.toString(result.get(Constants.CODE), "");
////		String text = Objects.toString(result.get(Constants.TEXT), "");
////
////		//存入数据库信息
////		Sms sms = null;
////		if(mobiles != null && mobiles.length >0) {
////			for(int i = 0;i < mobiles.length;i++) {
////			    sms = new Sms();
////				String phone = mobiles[i];
////				//插入数据库
////				sms.setMobile(phone);
////				sms.setSmsType(SqlSmsConstants.SQL_SMS_TYPE_GROUP);
////				sms.setContent(content);
////
////				try {
////					sms = smsService.insertSms(sms);
////					logger.info("=====step3."+i+":【群发短信】(BootSmsController-groupSendSms)-插入短信信息-返回信息, sms:{}", sms);
////				} catch (BootServiceException e) {
////					logger.error("=====step3."+i+".1:【群发短信】(BootSmsController-groupSendSms)-插入短信-事务性异常, Exception = {}, message = {}", e, e.getMessage());
////				}
////			}
////		}
////
////		if (!StringUtils.equals(code, Constants.CC_OK)) {
////			return new BaseRestResponse(BootConstants.SMS_ERROR, BootConstants.SMS_ERROR_MSG);
////		}
//
//		BaseRestMapResponse smsSendResponse = new BaseRestMapResponse();
////		SmsResponse smsSendResponse = new SmsResponse();
////		smsSendResponse.setText(text);
//		logger.info("=====step4:【群发短信】(BootSmsController-groupSendSms)-返回信息, smsSendResponse:{}", smsSendResponse);
//		return smsSendResponse;
//	}
//
//	/**
//	 * 短信定时发送
//	 * @param req
//	 * @param bindingResult
//	 * @return BaseRestResponse
//	 */
//	@RequestMapping(value="/timeSendSms",method={RequestMethod.POST})
//	@ResponseBody
//	public BaseRestMapResponse timeSendSms(@Validated @RequestBody SmsSendTimeRequest req,
//		BindingResult bindingResult) {
//		logger.info("===step1:【短信定时发送】(BootSmsController-timeSendSms)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));
//
//		String mobile = req.getMobile();
//		String content  = req.getContent();
//		String sendTime  = req.getSendTime();
////		if(StringUtils.isBlank(mobile)) {
////			return new BaseRestResponse(BootConstants.SMS_FIELD_EMPTY, "mobile为空");
////		} else if(StringUtils.isBlank(content)) {
////			return new BaseRestResponse(BootConstants.SMS_FIELD_EMPTY, "content为空");
////		} else if(StringUtils.isBlank(sendTime)) {
////			return new BaseRestResponse(BootConstants.SMS_FIELD_EMPTY, "sendTime为空");
////		}
//
//		String[] mobiles = StringUtils.split(mobile, Constants.SEMICOLON);
//
//		List<NameValuePair> nameValuePairLists = new ArrayList<NameValuePair>();
//		nameValuePairLists.add(new BasicNameValuePair("loginid", smsLoginid));
//		nameValuePairLists.add(new BasicNameValuePair("password", smsPassword));
//		nameValuePairLists.add(new BasicNameValuePair("receiver", mobile));
//		nameValuePairLists.add(new BasicNameValuePair("content", content));
//		nameValuePairLists.add(new BasicNameValuePair("sendTime", sendTime));
//
//	    String params = URLEncodedUtils.format(nameValuePairLists, StandardCharsets.UTF_8);
//
//		// 发送定时短信
//	    JSONObject jsonInfo = smsSendService.submitToTime(params);
//		logger.info("===step2:【短信定时发送】(BootSmsController-timeSendSms)-调用smsService, jsonInfo:{}", jsonInfo);
//
//		JSONObject result =  jsonInfo.getJSONObject("result");
//
////		String code = Objects.toString(result.get(Constants.CODE), "");
////		String text = Objects.toString(result.get(Constants.TEXT), "");
//
////		//存入数据库信息
////		Sms sms = null;
////		if(mobiles != null && mobiles.length >0) {
////			for(int i = 0;i < mobiles.length;i++) {
////				sms = new Sms();
////				String telephone = mobiles[i];
////				//插入数据库
////				sms.setMobile(telephone);
////				sms.setContent(content);
////				sms.setSmsType(SqlSmsConstants.SQL_SMS_TYPE_TIME);
////
////				try {
////					sms = smsService.insertSms(sms);
////					logger.info("=====step3."+i+":【短信定时发送】(BootSmsController-timeSendSms)-插入短信-返回信息, sms:{}", sms);
////				} catch (BootServiceException e) {
////					logger.error("=====step3."+i+".1:【短信定时发送】(BootSmsController-timeSendSms)-插入短信-事务性异常, Exception = {}, message = {}", e, e.getMessage());
////				}
////			}
////		}
////
////		if (!StringUtils.equals(code, Constants.CC_OK)) {
////			return new BaseRestResponse(BootConstants.SMS_ERROR, BootConstants.SMS_ERROR_MSG);
////		}
//
//		BaseRestMapResponse smsSendResponse = new BaseRestMapResponse();
//
////		smsSendResponse.setText(text);
//		logger.info("=====step4:【短信定时发送】(BootSmsController-timeSendSms)-返回信息, smsSendResponse:{}", smsSendResponse);
//		return smsSendResponse;
//	}

}