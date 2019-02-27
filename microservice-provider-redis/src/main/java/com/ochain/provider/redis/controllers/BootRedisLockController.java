package com.ochain.provider.redis.controllers;

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

/**
 * Redis 分布式 lock（锁） BootRedisLockController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/distributedLock")
public class BootRedisLockController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 锁定
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/lock",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse lock(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【锁定】(BootRedisLockController-lock)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		boolean flag = false;
		try {
			flag = redisLockService.lock(key);
			logger.info("===step2:【锁定】(BootRedisLockController-lock)-锁定-返回信息, flag:{}", flag);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【锁定】(BootRedisLockController-lock)-锁定-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【锁定】(BootRedisLockController-lock)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 释放锁
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/unlock",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse unlock(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【释放锁】(BootRedisLockController-unlock)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		boolean flag = false;
		try {
			flag = redisLockService.unlock(key);
			logger.info("===step2:【释放锁】(BootRedisLockController-unlock)-释放锁-返回信息, flag:{}", flag);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【释放锁】(BootRedisLockController-unlock)-释放锁-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【释放锁】(BootRedisLockController-unlock)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

}