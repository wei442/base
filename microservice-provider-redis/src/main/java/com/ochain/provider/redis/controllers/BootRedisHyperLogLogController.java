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
 * Redis hyperLogLog（字符串） BootRedisHyperLogLogController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/hyperLogLog")
public class BootRedisHyperLogLogController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO hyperLogLog 命令工具方法
	/************************** jredis hyperLogLog 命令工具方法 ****************************/
	/**
	 * 将一个数量的元素添加到指定的HyperLogLog里面
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/pfadd",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse pfadd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【一个数量的元素添加到指定的HyperLogLog里面】(BootRedisHyperLogLogController-pfadd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String element = req.getElement();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(element)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "element为空");
		}

		long len = 0;
		try {
			len = redisService.pfadd(key, element);
			logger.info("===step2:【一个数量的元素添加到指定的HyperLogLog里面】(BootRedisHyperLogLogController-pfadd)-添加到指定的HyperLogLog-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【一个数量的元素添加到指定的HyperLogLog里面】(BootRedisHyperLogLogController-pfadd)-添加到指定的HyperLogLog-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【一个数量的元素添加到指定的HyperLogLog里面】(BootRedisHyperLogLogController-pfadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将多个数量的元素添加到指定的HyperLogLog里面
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/pfadd",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse pfaddArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个数量的元素添加到指定的HyperLogLog里面】(BootRedisHyperLogLogController-pfaddArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] elements = req.getElements();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(elements == null || elements.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "elements为空");
		}

		long len = 0;
		try {
			len = redisService.pfadd(key, elements);
			logger.info("===step2:【多个数量的元素添加到指定的HyperLogLog里面】(BootRedisHyperLogLogController-pfaddArray)-添加到指定的HyperLogLog-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【多个数量的元素添加到指定的HyperLogLog里面】(BootRedisHyperLogLogController-pfaddArray)-添加到指定的HyperLogLog-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【多个数量的元素添加到指定的HyperLogLog里面】(BootRedisHyperLogLogController-pfadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回储存在给定键的 HyperLogLog 的近似基数
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/pfcount",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse pfcount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回储存在给定键的 HyperLogLog 的近似基数】(BootRedisHyperLogLogController-pfcount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0;
		try {
			len = redisService.pfcount(key);
			logger.info("===step2:【返回储存在给定键的 HyperLogLog 的近似基数】(BootRedisHyperLogLogController-pfcount)-HyperLogLog的近似基数-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回储存在给定键的 HyperLogLog 的近似基数】(BootRedisHyperLogLogController-pfcount)-HyperLogLog的近似基数-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回储存在给定键的 HyperLogLog 的近似基数】(BootRedisHyperLogLogController-pfcount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回储存在给定键的 HyperLogLog 的近似基数
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/pfcount",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse pfcountArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回储存在给定键的 HyperLogLog 的近似基数】(BootRedisHyperLogLogController-pfcountArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		long len = 0;
		try {
			len = redisService.pfcount(keys);
			logger.info("===step2:【返回储存在给定键的 HyperLogLog 的近似基数】(BootRedisHyperLogLogController-pfcountArray)-HyperLogLog的近似基数-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回储存在给定键的 HyperLogLog 的近似基数】(BootRedisHyperLogLogController-pfcountArray)-HyperLogLog的近似基数-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回储存在给定键的 HyperLogLog 的近似基数】(BootRedisHyperLogLogController-pfcountArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将一个HyperLogLog合并（merge）为一个HyperLogLog
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/pfmerge",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse pfmerge(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将一个HyperLogLog合并（merge）为一个HyperLogLog】(BootRedisHyperLogLogController-pfmerge)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String sourcekey = req.getSourcekey();
		if(StringUtils.isBlank(dstkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "dstkey为空");
		} else if(StringUtils.isBlank(sourcekey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "sourcekey为空");
		}

		String result = null;
		try {
			result = redisService.pfmerge(dstkey, sourcekey);
			logger.info("===step2:【将一个HyperLogLog合并（merge）为一个HyperLogLog】(BootRedisHyperLogLogController-pfmerge)-合并为一个HyperLogLog-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【将一个HyperLogLog合并（merge）为一个HyperLogLog】(BootRedisHyperLogLogController-pfmerge)-合并为一个HyperLogLog-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将一个HyperLogLog合并（merge）为一个HyperLogLog】(BootRedisHyperLogLogController-pfmerge)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将多个HyperLogLog合并（merge）为一个HyperLogLog
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/pfmerge",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse pfmergeArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将多个HyperLogLog合并（merge）为一个HyperLogLog】(BootRedisHyperLogLogController-pfmergeArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String[] sourcekeys = req.getSourcekeys();
		if(StringUtils.isBlank(dstkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "dstkey为空");
		} else if(sourcekeys == null || sourcekeys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "sourcekeys为空");
		}

		String result = null;
		try {
			result = redisService.pfmerge(dstkey, sourcekeys);
			logger.info("===step2:【将多个HyperLogLog合并（merge）为一个HyperLogLog】(BootRedisHyperLogLogController-pfmergeArray)-合并为多个HyperLogLog-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【将多个HyperLogLog合并（merge）为一个HyperLogLog】(BootRedisHyperLogLogController-pfmergeArray)-合并为多个HyperLogLog-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将多个HyperLogLog合并（merge）为一个HyperLogLog】(BootRedisHyperLogLogController-pfmergeArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/************************** jredis hyperLogLog 命令工具方法 ****************************/

}