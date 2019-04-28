package com.cloud.provider.redis.controller;

import java.util.List;

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
import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.common.exception.RedisException;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.constants.RedisConstants;
import com.cloud.provider.redis.rest.request.RedisRequest;

import redis.clients.jedis.Transaction;

/**
 * Redis Transaction（事务） RedisTransactionController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/redis/transaction")
public class RedisTransactionController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Transaction(事务) 命令工具方法
	/************************** jredis Transaction(事务) 命令工具方法 ****************************/
	/**
	 * 取消事务
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/discard",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse discard(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【取消事务】(RedisTransactionController-discard)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String result = null;
		try {
			result = redisService.discard();
			logger.info("===step2:【取消事务】(RedisTransactionController-discard)-取消事务-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【取消事务】(RedisTransactionController-discard)-取消事务-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【取消事务】(RedisTransactionController-discard)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 执行所有事务块内的命令
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/exec",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse exec(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【执行所有事务块内的命令】(RedisTransactionController-exec)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		List<Object> result = null;
		try {
			result = redisService.exec();
			logger.info("===step2:【执行所有事务块内的命令】(RedisTransactionController-exec)-执行所有事务块内的命令-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【执行所有事务块内的命令】(RedisTransactionController-exec)-执行所有事务块内的命令-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【执行所有事务块内的命令】(RedisTransactionController-exec)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 标记一个事务块的开始
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/multi",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse multi(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【标记一个事务块的开始】(RedisTransactionController-multi)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		Transaction result = null;
		try {
			result = redisService.multi();
			logger.info("===step2:【标记一个事务块的开始】(RedisTransactionController-multi)-标记一个事务块的开始-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【标记一个事务块的开始】(RedisTransactionController-multi)-标记一个事务块的开始-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【标记一个事务块的开始】(RedisTransactionController-multi)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 取消WATCH命令对所有key的监视
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/unwatch",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse unwatch(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【取消WATCH命令对所有key的监视】(RedisTransactionController-unwatch)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String result = null;
		try {
			result = redisService.unwatch();
			logger.info("===step2:【取消WATCH命令对所有key的监视】(RedisTransactionController-unwatch)-取消WATCH监视-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【取消WATCH命令对所有key的监视】(RedisTransactionController-unwatch)-取消WATCH监视-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【取消WATCH命令对所有key的监视】(RedisTransactionController-unwatch)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 监视一个key
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/watch",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse watch(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【监视一个key】(RedisTransactionController-watch)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key空");
		}

		String result = null;
		try {
			result = redisService.watch(key);
			logger.info("===step2:【监视一个key】(RedisTransactionController-watch)-监视一个key-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【监视一个key】(RedisTransactionController-watch)-监视一个key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【监视一个key】(RedisTransactionController-watch)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 监视多个key
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/watch",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse watchArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【监视多个key】(RedisTransactionController-watchArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys空");
		}

		String result = null;
		try {
			result = redisService.watch(keys);
			logger.info("===step2:【监视多个key】(RedisTransactionController-watchArray))-监视多个key-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【监视多个key】(RedisTransactionController-watchArray)-监视多个key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【监视多个key】(RedisTransactionController-watchArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis Transaction(事务) 命令工具方法 ****************************/

}