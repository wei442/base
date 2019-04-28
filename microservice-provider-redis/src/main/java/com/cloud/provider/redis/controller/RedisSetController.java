package com.cloud.provider.redis.controller;

import java.util.List;
import java.util.Set;

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

import redis.clients.jedis.ScanResult;

/**
 * Redis Set（集合） RedisSetController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/redis/set")
public class RedisSetController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Set(集合) 命令工具方法
	/************************** redis Set(集合) 命令工具方法 ****************************/
	/**
	 * 一个成员元素加入集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sadd",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sadd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【一个成员元素加入集合】(RedisSetController-sadd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		long len = 0l;
		try {
			len = redisService.sadd(key, member);
			logger.info("===step2:【一个成员元素加入集合】(RedisSetController-sadd)-成员元素加入集合-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【一个成员元素加入集合】(RedisSetController-sadd)-成员元素加入集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【一个成员元素加入集合】(RedisSetController-sadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 多个成员元素加入集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/sadd",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse saddArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个成员元素加入集合】(RedisSetController-saddArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] members = req.getMembers();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(members == null || members.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "members为空");
		}

		long len = 0l;
		try {
			len = redisService.sadd(key, members);
			logger.info("===step2:【多个成员元素加入集合】(RedisSetController-saddArray)-成员元素加入集合-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【多个成员元素加入集合】(RedisSetController-saddArray)-成员元素加入集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【多个成员元素加入集合】(RedisSetController-saddArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合元素数量
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/scard",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse scard(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合元素数量】(RedisSetController-scard)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.scard(key);
			logger.info("===step2:【返回集合元素数量】(RedisSetController-sadd)-集合元素数量-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【返回集合元素数量】(RedisSetController-sadd)-集合元素数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回集合元素数量】(RedisSetController-sadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合之间差集
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sdiff",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sdiff(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合之间差集】(RedisSetController-sdiff)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sdiff(key);
			logger.info("===step2:【返回集合之间差集】(RedisSetController-sdiff)-集合差集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回集合之间差集】(RedisSetController-sdiff)-集合差集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回集合之间差集】(RedisSetController-sdiff)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合之间差集
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/sdiff",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sdiffArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合之间差集】(RedisSetController-sdiffArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sdiff(keys);
			logger.info("===step2:【返回集合之间差集】(RedisSetController-sdiffArray)-集合差集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回集合之间差集】(RedisSetController-sdiffArray)-集合差集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回集合之间差集】(RedisSetController-sdiffArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 集合之间的差集存储在指定的集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sdiffstore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sdiffstore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【集合之间的差集存储在指定的集合】(RedisSetController-sdiffstore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String dstkey = req.getDstkey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(dstkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dstkey为空");
		}

		long len = 0l;
		try {
			len = redisService.sdiffstore(dstkey, key);
			logger.info("===step2:【集合之间的差集存储在指定的集合】(RedisSetController-sdiffstore)-差集存储-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【集合之间的差集存储在指定的集合】(RedisSetController-sdiffstore)-差集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【集合之间的差集存储在指定的集合】(RedisSetController-sdiffstore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合之间的差集存储在指定的集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/sdiffstore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sdiffstoreArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合之间的差集存储在指定的集合】(RedisSetController-sdiffstoreArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.sdiffstore(dstkey, keys);
			logger.info("===step2:【给定集合之间的差集存储在指定的集合】(RedisSetController-sdiffstoreArray)-差集存储-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【给定集合之间的差集存储在指定的集合】(RedisSetController-sdiffstoreArray)-差集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合之间的差集存储在指定的集合】(RedisSetController-sdiffstoreArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回给定所有给定集合交集
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sinter",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sinter(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回给定所有给定集合交集】(RedisSetController-sinter)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sinter(key);
			logger.info("===step2:【返回给定所有给定集合交集】(RedisSetController-sinter)-集合交集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回给定所有给定集合交集】(RedisSetController-sinter)-集合交集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回给定所有给定集合交集】(RedisSetController-sinter)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回给定所有给定集合交集
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/sinter",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sinterArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回给定所有给定集合交集】(RedisSetController-sinterArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sdiff(keys);
			logger.info("===step2:【返回给定所有给定集合交集】(RedisSetController-sinterArray)-集合交集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回给定所有给定集合交集】(RedisSetController-sinterArray)-集合交集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回给定所有给定集合交集】(RedisSetController-sinterArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合之间的交集存储在指定的集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sinterstore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sinterstore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合之间的交集存储在指定的集合】(RedisSetController-sdiffstore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.sinterstore(dstkey, key);
			logger.info("===step2:【给定集合之间的交集存储在指定的集合】(RedisSetController-sdiffstore)-交集存储-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【给定集合之间的交集存储在指定的集合中】(RedisSetController-sdiffstore)-交集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合之间的交集存储在指定的集合】(RedisSetController-sdiffstore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合之间的差集存储在指定的集合
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/sinterstore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sinterstoreArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合之间的交集存储在指定的集合中】(RedisSetController-sinterstoreArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.sinterstore(dstkey, keys);
			logger.info("===step2:【给定集合之间的交集存储在指定的集合中】(RedisSetController-sinterstoreArray)-交集存储-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【给定集合之间的交集存储在指定的集合中】(RedisSetController-sinterstoreArray)-交集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合之间的交集存储在指定的集合中】(RedisSetController-sinterstoreArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 判断成员元素是否是集合的成员
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sismember",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sismember(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【判断成员元素是否是集合的成员】(RedisSetController-sismember)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		boolean flag = false;
		try {
			flag = redisService.sismember(key, member);
			logger.info("===step2:【判断成员元素是否是集合的成员】(RedisSetController-sismember)-判断成员-返回信息, flag:{}", flag);
		} catch (RedisException e) {
			logger.error("===step2.1:【判断成员元素是否是集合的成】(RedisSetController-sismember)-判断成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【判断成员元素是否是集合的成员】(RedisSetController-sismember)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合中的所有的成员
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/smembers",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse smembers(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【集合中的所有的成员】(RedisSetController-smembers)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.smembers(key);
			logger.info("===step2:【集合中的所有的成员】(RedisSetController-smembers)-集合中的所有的成员-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【集合中的所有的成员】(RedisSetController-smembers)-集合中的所有的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【集合中的所有的成员】(RedisSetController-smembers)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 成员移动
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/smove",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse smove(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【成员移动】(RedisSetController-smove)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String srckey = req.getSrckey();
		String dstkey = req.getDstkey();
		String member = req.getMember();
		if(StringUtils.isBlank(srckey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(dstkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dstkey为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		long len = 0l;
		try {
			len = redisService.smove(srckey, dstkey, member);
			logger.info("===step2:【成员移动】(RedisSetController-smove)-成员移动-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【成员移动】(RedisSetController-smove)-成员移动-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【成员移动】(RedisSetController-smove)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除并返回集合中的一个随机元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/spop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse spop(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除并返回集合中的一个随机元素】(RedisSetController-spop)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.spop(key);
			logger.info("===step2:【移除并返回集合中的一个随机元素】(RedisSetController-spop)-一个随机元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除并返回集合中的一个随机元素】(RedisSetController-spop)-一个随机元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【移除并返回集合中的一个随机元素】(RedisSetController-spop)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除并返回集合中的多个随机元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/count/spop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse spopCount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除并返回集合中的多个随机元素】(RedisSetController-spopCount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long count = req.getCount();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(count == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "count为空");
		}

		Set<String> result = null;
		try {
			result = redisService.spop(key, count);
			logger.info("===step2:【移除并返回集合中的多个随机元素】(RedisSetController-spopCount)-多个随机元素-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【移除并返回集合中的多个随机元素】(RedisSetController-spopCount)-多个随机元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【移除并返回集合中的多个随机元素】(RedisSetController-spopCount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合中的一个随机元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/srandmember",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse srandmember(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合中的一个随机元素素】(RedisSetController-srandmember)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.srandmember(key);
			logger.info("===step2:【返回集合中的一个随机元素素】(RedisSetController-srandmember)-一个随机元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【返回集合中的一个随机元素素】(RedisSetController-srandmember)-一个随机元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回集合中的一个随机元素素】(RedisSetController-srandmember)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合中的一个随机元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/count/srandmember",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse srandmemberCount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合中的一个随机元素】(RedisSetController-srandmemberCount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long count = req.getCount();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(count == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "count为空");
		}

		List<String> result = null;
		try {
			result = redisService.srandmember(key, count.intValue());
			logger.info("===step2:【返回集合中的一个随机元素】(RedisSetController-srandmemberCount)-一个随机元素-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回集合中的一个随机元素】(RedisSetController-srandmemberCount)-一个随机元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回集合中的一个随机元素】(RedisSetController-srandmemberCount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除集合中的一个成员元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/srem",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse srem(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除集合中的一个成员元素】(RedisSetController-srem)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "member为空");
		}

		long len = 0l;
		try {
			len = redisService.srem(key, member);
			logger.info("===step2:【移除集合中的一个成员元素】(RedisSetController-srem)-移除一个成员元素-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除集合中的一个成员元素】(RedisSetController-srem)-移除一个成员元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除集合中的一个成员元素】(RedisSetController-srem)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除集合中的多个成员元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/srem",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sremArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除集合中的多个成员元素】(RedisSetController-sremArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] members = req.getMembers();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(members == null || members.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "members为空");
		}

		long len = 0l;
		try {
			len = redisService.srem(key, members);
			logger.info("===step2:【移除集合中的多个成员元素】(RedisSetController-sremArray))-移除多个成员元素-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除集合中的多个成员元素】(RedisSetController-sremArray)-移除多个成员元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除集合中的多个成员元素】(RedisSetController-sremArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回给定集合的并集
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sunion",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sunion(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回给定集合的并集】(RedisSetController-sunion)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sunion(key);
			logger.info("===step2:【返回给定集合的并集】(RedisSetController-sunion)-集合并集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回给定集合的并集】(RedisSetController-sunion)-集合并集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回给定集合的并集】(RedisSetController-sunion)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回给定所有给定集合交集
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/sunion",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sunionArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回给定集合的并集】(RedisSetController-sunionArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sunion(keys);
			logger.info("===step2:【返回给定集合的并集】(RedisSetController-sunionArray)-集合并集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回给定集合的并集】(RedisSetController-sunionArray)-集合并集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回给定集合的并集】(RedisSetController-sunionArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合的并集存储指定的集合
	 * @param dstkey
	 * @param key
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sunionstore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sunionstore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合的并集存储指定的集合】(RedisSetController-sunionstore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(dstkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dstkey为空");
		}

		long len = 0l;
		try {
			len = redisService.sunionstore(dstkey, key);
			logger.info("===step2:【给定集合的并集存储指定的集合】(RedisSetController-sunionstore)-并集存储-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【给定集合的并集存储指定的集合】(RedisSetController-sunionstore)-并集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合的并集存储指定的集合】(RedisSetController-sunionstore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合之间的差集存储在指定的集合
	 * @param dstkey
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/sunionstore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sunionstoreArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合的并集存储指定的集合】(RedisSetController-sunionstoreArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.sunionstore(dstkey, keys);
			logger.info("===step2:【给定集合的并集存储指定的集合】(RedisSetController-sunionstoreArray)-并集存储-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【给定集合的并集存储指定的集合】(RedisSetController-sunionstoreArray)-并集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合的并集存储指定的集合】(RedisSetController-sunionstoreArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 迭代集合键中的元素
	 * @param key
	 * @param cursor
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sscan",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sscan(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【迭代集合键中的元素】(RedisSetController-sscan)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String cursor = req.getCursor();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(cursor)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "cursor为空");
		}

		ScanResult<String> result = null;
		try {
			result = redisService.sscan(key, cursor);
			logger.info("===step2:【迭代集合键中的元素】(RedisSetController-sscan)-迭代集合-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【迭代集合键中的元素】(RedisSetController-sscan)-迭代集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【迭代集合键中的元素】(RedisSetController-sscan)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis Set(集合) 命令工具方法 ****************************/

}