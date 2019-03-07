package com.cloud.provider.redis.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.constants.RedisConstants;
import com.cloud.provider.redis.enums.RedisResultEnum;
import com.cloud.provider.redis.exception.RedisException;
import com.cloud.provider.redis.rest.request.RedisRequest;

/**
 * Redis Connection（连接） RedisConnectionController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/redis/connection")
public class RedisConnectionController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Connection(连接) 命令工具方法
	/************************** jredis Connection(连接) 命令工具方法 ****************************/
	/**
	 * 检测给定的密码和配置文件中的密码是否相符
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/auth",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse auth(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【检测给定的密码和配置文件中的密码是否相符】(RedisConnectionController-auth)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String password = req.getPassword();
		if(StringUtils.isBlank(password)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "password为空");
		}

		String result = null;
		try {
			result = redisService.auth(password);
			logger.info("===step2:【检测给定的密码和配置文件中的密码是否相符】(RedisConnectionController-auth)-检测密码-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【检测给定的密码和配置文件中的密码是否相符】(RedisConnectionController-auth)-检测密码-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【检测给定的密码和配置文件中的密码是否相符】(RedisConnectionController-auth)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 打印给定的字符串
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/echo",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse echo(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【打印给定的字符串】(RedisConnectionController-echo)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String string = req.getString();
		if(StringUtils.isBlank(string)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "string为空");
		}

		String result = null;
		try {
			result = redisService.echo(string);
			logger.info("===step2:【打印给定的字符串】(RedisConnectionController-echo)-打印字符串-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【打印给定的字符串】(RedisConnectionController-echo)-打印字符串-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【打印给定的字符串】(RedisConnectionController-echo)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 客户端向 Redis服务器发送一个PING
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/ping",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse ping(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【客户端向 Redis服务器发送一个PING】(RedisConnectionController-ping)-传入参数为空");

		String result = null;
		try {
			result = redisService.ping();
			logger.info("===step2:【客户端向 Redis服务器发送一个PING】(RedisConnectionController-ping)-发送PING-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【客户端向 Redis服务器发送一个PING】(RedisConnectionController-ping)-发送PING-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【客户端向 Redis服务器发送一个PING】(RedisConnectionController-ping)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 关闭与当前客户端与redis服务的连接
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/quit",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse quit(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【关闭与当前客户端与redis服务的连接】(RedisConnectionController-quit)-传入参数为空");

		String result = null;
		try {
			result = redisService.ping();
			logger.info("===step2:【关闭与当前客户端与redis服务的连接】(RedisConnectionController-quit)-关闭连接-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【关闭与当前客户端与redis服务的连接】(RedisConnectionController-quit)-关闭连接-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【关闭与当前客户端与redis服务的连接】(RedisConnectionController-quit)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 切换到指定的数据库
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/select",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse select(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【切换到指定的数据库】(RedisConnectionController-select)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		Long index = req.getIndex();
		if(index == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "index为空");
		}

		String result = null;
		try {
			result = redisService.select(index.intValue());
			logger.info("===step2:【切换到指定的数据库】(RedisConnectionController-select)-切换数据库-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【切换到指定的数据库】(RedisConnectionController-select)-切换数据库-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【切换到指定的数据库】(RedisConnectionController-select)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis Connection(连接) 命令工具方法 ****************************/

}