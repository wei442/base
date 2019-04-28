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
import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.common.exception.RedisException;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.constants.RedisConstants;
import com.cloud.provider.redis.rest.request.RedisRequest;

/**
 * Redis hyperLogLog（字符串） RedisHyperLogLogController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/redis/hyperLogLog")
public class RedisHyperLogLogController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO hyperLogLog 命令工具方法
	/************************** jredis hyperLogLog 命令工具方法 ****************************/
	/**
	 * 将一个数量的元素添加到指定的HyperLogLog里面
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/pfadd",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse pfadd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【一个数量的元素添加到指定的HyperLogLog里面】(RedisHyperLogLogController-pfadd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String element = req.getElement();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(element)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "element为空");
		}

		long len = 0;
		try {
			len = redisService.pfadd(key, element);
			logger.info("===step2:【一个数量的元素添加到指定的HyperLogLog里面】(RedisHyperLogLogController-pfadd)-添加到指定的HyperLogLog-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【一个数量的元素添加到指定的HyperLogLog里面】(RedisHyperLogLogController-pfadd)-添加到指定的HyperLogLog-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【一个数量的元素添加到指定的HyperLogLog里面】(RedisHyperLogLogController-pfadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将多个数量的元素添加到指定的HyperLogLog里面
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/pfadd",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse pfaddArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个数量的元素添加到指定的HyperLogLog里面】(RedisHyperLogLogController-pfaddArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] elements = req.getElements();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(elements == null || elements.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "elements为空");
		}

		long len = 0;
		try {
			len = redisService.pfadd(key, elements);
			logger.info("===step2:【多个数量的元素添加到指定的HyperLogLog里面】(RedisHyperLogLogController-pfaddArray)-添加到指定的HyperLogLog-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【多个数量的元素添加到指定的HyperLogLog里面】(RedisHyperLogLogController-pfaddArray)-添加到指定的HyperLogLog-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【多个数量的元素添加到指定的HyperLogLog里面】(RedisHyperLogLogController-pfadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回储存在给定键的 HyperLogLog 的近似基数
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/pfcount",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse pfcount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回储存在给定键的 HyperLogLog 的近似基数】(RedisHyperLogLogController-pfcount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0;
		try {
			len = redisService.pfcount(key);
			logger.info("===step2:【返回储存在给定键的 HyperLogLog 的近似基数】(RedisHyperLogLogController-pfcount)-HyperLogLog的近似基数-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【返回储存在给定键的 HyperLogLog 的近似基数】(RedisHyperLogLogController-pfcount)-HyperLogLog的近似基数-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回储存在给定键的 HyperLogLog 的近似基数】(RedisHyperLogLogController-pfcount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回储存在给定键的 HyperLogLog 的近似基数
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/pfcount",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse pfcountArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回储存在给定键的 HyperLogLog 的近似基数】(RedisHyperLogLogController-pfcountArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		long len = 0;
		try {
			len = redisService.pfcount(keys);
			logger.info("===step2:【返回储存在给定键的 HyperLogLog 的近似基数】(RedisHyperLogLogController-pfcountArray)-HyperLogLog的近似基数-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【返回储存在给定键的 HyperLogLog 的近似基数】(RedisHyperLogLogController-pfcountArray)-HyperLogLog的近似基数-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回储存在给定键的 HyperLogLog 的近似基数】(RedisHyperLogLogController-pfcountArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将一个HyperLogLog合并（merge）为一个HyperLogLog
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/pfmerge",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse pfmerge(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将一个HyperLogLog合并（merge）为一个HyperLogLog】(RedisHyperLogLogController-pfmerge)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String sourcekey = req.getSourcekey();
		if(StringUtils.isBlank(dstkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dstkey为空");
		} else if(StringUtils.isBlank(sourcekey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "sourcekey为空");
		}

		String result = null;
		try {
			result = redisService.pfmerge(dstkey, sourcekey);
			logger.info("===step2:【将一个HyperLogLog合并（merge）为一个HyperLogLog】(RedisHyperLogLogController-pfmerge)-合并为一个HyperLogLog-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【将一个HyperLogLog合并（merge）为一个HyperLogLog】(RedisHyperLogLogController-pfmerge)-合并为一个HyperLogLog-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将一个HyperLogLog合并（merge）为一个HyperLogLog】(RedisHyperLogLogController-pfmerge)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将多个HyperLogLog合并（merge）为一个HyperLogLog
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/pfmerge",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse pfmergeArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将多个HyperLogLog合并（merge）为一个HyperLogLog】(RedisHyperLogLogController-pfmergeArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String[] sourcekeys = req.getSourcekeys();
		if(StringUtils.isBlank(dstkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dstkey为空");
		} else if(sourcekeys == null || sourcekeys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "sourcekeys为空");
		}

		String result = null;
		try {
			result = redisService.pfmerge(dstkey, sourcekeys);
			logger.info("===step2:【将多个HyperLogLog合并（merge）为一个HyperLogLog】(RedisHyperLogLogController-pfmergeArray)-合并为多个HyperLogLog-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【将多个HyperLogLog合并（merge）为一个HyperLogLog】(RedisHyperLogLogController-pfmergeArray)-合并为多个HyperLogLog-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将多个HyperLogLog合并（merge）为一个HyperLogLog】(RedisHyperLogLogController-pfmergeArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/************************** jredis hyperLogLog 命令工具方法 ****************************/

}