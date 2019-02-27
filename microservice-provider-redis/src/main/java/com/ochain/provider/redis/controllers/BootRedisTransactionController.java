package com.ochain.provider.redis.controllers;

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
import com.ochain.common.constants.BootConstants;
import com.ochain.common.constants.RedisConstants;
import com.ochain.common.exception.BootServiceException;
import com.ochain.provider.redis.boot.BootRestMapResponse;
import com.ochain.provider.redis.rest.request.RedisRequest;

import redis.clients.jedis.Transaction;

/**
 * Redis Transaction（事务） BootRedisTransactionController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/transaction")
public class BootRedisTransactionController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Transaction(事务) 命令工具方法
	/************************** jredis Transaction(事务) 命令工具方法 ****************************/
	/**
	 * 取消事务
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/discard",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse discard(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【取消事务】(BootRedisTransactionController-discard)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String result = null;
		try {
			result = redisService.discard();
			logger.info("===step2:【取消事务】(BootRedisTransactionController-discard)-取消事务-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【取消事务】(BootRedisTransactionController-discard)-取消事务-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【取消事务】(BootRedisTransactionController-discard)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 执行所有事务块内的命令
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/exec",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse exec(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【执行所有事务块内的命令】(BootRedisTransactionController-exec)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		List<Object> result = null;
		try {
			result = redisService.exec();
			logger.info("===step2:【执行所有事务块内的命令】(BootRedisTransactionController-exec)-执行所有事务块内的命令-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【执行所有事务块内的命令】(BootRedisTransactionController-exec)-执行所有事务块内的命令-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【执行所有事务块内的命令】(BootRedisTransactionController-exec)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 标记一个事务块的开始
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/multi",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse multi(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【标记一个事务块的开始】(BootRedisTransactionController-multi)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		Transaction result = null;
		try {
			result = redisService.multi();
			logger.info("===step2:【标记一个事务块的开始】(BootRedisTransactionController-multi)-标记一个事务块的开始-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【标记一个事务块的开始】(BootRedisTransactionController-multi)-标记一个事务块的开始-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【标记一个事务块的开始】(BootRedisTransactionController-multi)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 取消WATCH命令对所有key的监视
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/unwatch",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse unwatch(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【取消WATCH命令对所有key的监视】(BootRedisTransactionController-unwatch)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String result = null;
		try {
			result = redisService.unwatch();
			logger.info("===step2:【取消WATCH命令对所有key的监视】(BootRedisTransactionController-unwatch)-取消WATCH监视-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【取消WATCH命令对所有key的监视】(BootRedisTransactionController-unwatch)-取消WATCH监视-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【取消WATCH命令对所有key的监视】(BootRedisTransactionController-unwatch)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 监视一个key
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/watch",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse watch(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【监视一个key】(BootRedisTransactionController-watch)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key空");
		}

		String result = null;
		try {
			result = redisService.watch(key);
			logger.info("===step2:【监视一个key】(BootRedisTransactionController-watch)-监视一个key-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【监视一个key】(BootRedisTransactionController-watch)-监视一个key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【监视一个key】(BootRedisTransactionController-watch)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 监视多个key
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/watch",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse watchArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【监视多个key】(BootRedisTransactionController-watchArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys空");
		}

		String result = null;
		try {
			result = redisService.watch(keys);
			logger.info("===step2:【监视多个key】(BootRedisTransactionController-watchArray))-监视多个key-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【监视多个key】(BootRedisTransactionController-watchArray)-监视多个key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【监视多个key】(BootRedisTransactionController-watchArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis Transaction(事务) 命令工具方法 ****************************/

}