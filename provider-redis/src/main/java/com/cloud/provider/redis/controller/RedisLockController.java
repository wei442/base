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
import com.cloud.common.constants.CommConstants;
import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.common.exception.RedisException;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.rest.request.RedisRequest;

import io.swagger.annotations.Api;

/**
 * Redis 分布式 lock（锁） RedisLockController
 * @author wei.yong
 * @date 2017-09-14
 */
@Api(tags = "分布式锁")
@RestController
@RequestMapping("/redis/distributedLock")
public class RedisLockController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 锁定
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/lock",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lock(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【锁定】(RedisLockController-lock)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		boolean flag = false;
		try {
			flag = redisLockService.lock(key);
			logger.info("===step2:【锁定】(RedisLockController-lock)-锁定-返回信息, flag:{}", flag);
		} catch (RedisException e) {
			logger.error("===step2.1:【锁定】(RedisLockController-lock)-锁定-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, flag);
		logger.info("===step3:【锁定】(RedisLockController-lock)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 释放锁
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/unlock",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse unlock(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【释放锁】(RedisLockController-unlock)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		boolean flag = false;
		try {
			flag = redisLockService.unlock(key);
			logger.info("===step2:【释放锁】(RedisLockController-unlock)-释放锁-返回信息, flag:{}", flag);
		} catch (RedisException e) {
			logger.error("===step2.1:【释放锁】(RedisLockController-unlock)-释放锁-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, flag);
		logger.info("===step3:【释放锁】(RedisLockController-unlock)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

}