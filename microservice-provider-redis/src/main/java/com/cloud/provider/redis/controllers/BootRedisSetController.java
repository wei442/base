package com.cloud.provider.redis.controllers;

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
import com.cloud.provider.redis.boot.BootRestMapResponse;
import com.cloud.provider.redis.rest.request.RedisRequest;
import com.ochain.common.constants.BootConstants;
import com.ochain.common.constants.RedisConstants;
import com.ochain.common.exception.BootServiceException;

import redis.clients.jedis.ScanResult;

/**
 * Redis Set（集合） BootRedisSetController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/set")
public class BootRedisSetController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Set(集合) 命令工具方法
	/************************** redis Set(集合) 命令工具方法 ****************************/
	/**
	 * 一个成员元素加入集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sadd",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sadd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【一个成员元素加入集合】(BootRedisSetController-sadd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		long len = 0l;
		try {
			len = redisService.sadd(key, member);
			logger.info("===step2:【一个成员元素加入集合】(BootRedisSetController-sadd)-成员元素加入集合-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【一个成员元素加入集合】(BootRedisSetController-sadd)-成员元素加入集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【一个成员元素加入集合】(BootRedisSetController-sadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 多个成员元素加入集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/sadd",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse saddArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个成员元素加入集合】(BootRedisSetController-saddArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] members = req.getMembers();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(members == null || members.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "members为空");
		}

		long len = 0l;
		try {
			len = redisService.sadd(key, members);
			logger.info("===step2:【多个成员元素加入集合】(BootRedisSetController-saddArray)-成员元素加入集合-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【多个成员元素加入集合】(BootRedisSetController-saddArray)-成员元素加入集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【多个成员元素加入集合】(BootRedisSetController-saddArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合元素数量
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/scard",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse scard(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合元素数量】(BootRedisSetController-scard)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.scard(key);
			logger.info("===step2:【返回集合元素数量】(BootRedisSetController-sadd)-集合元素数量-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回集合元素数量】(BootRedisSetController-sadd)-集合元素数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回集合元素数量】(BootRedisSetController-sadd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合之间差集
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sdiff",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sdiff(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合之间差集】(BootRedisSetController-sdiff)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sdiff(key);
			logger.info("===step2:【返回集合之间差集】(BootRedisSetController-sdiff)-集合差集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回集合之间差集】(BootRedisSetController-sdiff)-集合差集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回集合之间差集】(BootRedisSetController-sdiff)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合之间差集
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/sdiff",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sdiffArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合之间差集】(BootRedisSetController-sdiffArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sdiff(keys);
			logger.info("===step2:【返回集合之间差集】(BootRedisSetController-sdiffArray)-集合差集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回集合之间差集】(BootRedisSetController-sdiffArray)-集合差集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回集合之间差集】(BootRedisSetController-sdiffArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 集合之间的差集存储在指定的集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sdiffstore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sdiffstore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【集合之间的差集存储在指定的集合】(BootRedisSetController-sdiffstore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String dstkey = req.getDstkey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(dstkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "dstkey为空");
		}

		long len = 0l;
		try {
			len = redisService.sdiffstore(dstkey, key);
			logger.info("===step2:【集合之间的差集存储在指定的集合】(BootRedisSetController-sdiffstore)-差集存储-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【集合之间的差集存储在指定的集合】(BootRedisSetController-sdiffstore)-差集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【集合之间的差集存储在指定的集合】(BootRedisSetController-sdiffstore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合之间的差集存储在指定的集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/sdiffstore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sdiffstoreArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合之间的差集存储在指定的集合】(BootRedisSetController-sdiffstoreArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.sdiffstore(dstkey, keys);
			logger.info("===step2:【给定集合之间的差集存储在指定的集合】(BootRedisSetController-sdiffstoreArray)-差集存储-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【给定集合之间的差集存储在指定的集合】(BootRedisSetController-sdiffstoreArray)-差集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合之间的差集存储在指定的集合】(BootRedisSetController-sdiffstoreArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回给定所有给定集合交集
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sinter",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sinter(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回给定所有给定集合交集】(BootRedisSetController-sinter)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sinter(key);
			logger.info("===step2:【返回给定所有给定集合交集】(BootRedisSetController-sinter)-集合交集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回给定所有给定集合交集】(BootRedisSetController-sinter)-集合交集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回给定所有给定集合交集】(BootRedisSetController-sinter)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回给定所有给定集合交集
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/sinter",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sinterArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回给定所有给定集合交集】(BootRedisSetController-sinterArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sdiff(keys);
			logger.info("===step2:【返回给定所有给定集合交集】(BootRedisSetController-sinterArray)-集合交集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回给定所有给定集合交集】(BootRedisSetController-sinterArray)-集合交集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回给定所有给定集合交集】(BootRedisSetController-sinterArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合之间的交集存储在指定的集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sinterstore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sinterstore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合之间的交集存储在指定的集合】(BootRedisSetController-sdiffstore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.sinterstore(dstkey, key);
			logger.info("===step2:【给定集合之间的交集存储在指定的集合】(BootRedisSetController-sdiffstore)-交集存储-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【给定集合之间的交集存储在指定的集合中】(BootRedisSetController-sdiffstore)-交集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合之间的交集存储在指定的集合】(BootRedisSetController-sdiffstore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合之间的差集存储在指定的集合
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/sinterstore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sinterstoreArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合之间的交集存储在指定的集合中】(BootRedisSetController-sinterstoreArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.sinterstore(dstkey, keys);
			logger.info("===step2:【给定集合之间的交集存储在指定的集合中】(BootRedisSetController-sinterstoreArray)-交集存储-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【给定集合之间的交集存储在指定的集合中】(BootRedisSetController-sinterstoreArray)-交集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合之间的交集存储在指定的集合中】(BootRedisSetController-sinterstoreArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 判断成员元素是否是集合的成员
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sismember",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sismember(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【判断成员元素是否是集合的成员】(BootRedisSetController-sismember)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		boolean flag = false;
		try {
			flag = redisService.sismember(key, member);
			logger.info("===step2:【判断成员元素是否是集合的成员】(BootRedisSetController-sismember)-判断成员-返回信息, flag:{}", flag);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【判断成员元素是否是集合的成】(BootRedisSetController-sismember)-判断成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【判断成员元素是否是集合的成员】(BootRedisSetController-sismember)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合中的所有的成员
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/smembers",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse smembers(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【集合中的所有的成员】(BootRedisSetController-smembers)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.smembers(key);
			logger.info("===step2:【集合中的所有的成员】(BootRedisSetController-smembers)-集合中的所有的成员-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【集合中的所有的成员】(BootRedisSetController-smembers)-集合中的所有的成员-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【集合中的所有的成员】(BootRedisSetController-smembers)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 成员移动
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/smove",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse smove(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【成员移动】(BootRedisSetController-smove)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String srckey = req.getSrckey();
		String dstkey = req.getDstkey();
		String member = req.getMember();
		if(StringUtils.isBlank(srckey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(dstkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "dstkey为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		long len = 0l;
		try {
			len = redisService.smove(srckey, dstkey, member);
			logger.info("===step2:【成员移动】(BootRedisSetController-smove)-成员移动-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【成员移动】(BootRedisSetController-smove)-成员移动-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【成员移动】(BootRedisSetController-smove)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除并返回集合中的一个随机元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/spop",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse spop(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除并返回集合中的一个随机元素】(BootRedisSetController-spop)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		String result = null;
		try {
			result = redisService.spop(key);
			logger.info("===step2:【移除并返回集合中的一个随机元素】(BootRedisSetController-spop)-一个随机元素-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【移除并返回集合中的一个随机元素】(BootRedisSetController-spop)-一个随机元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【移除并返回集合中的一个随机元素】(BootRedisSetController-spop)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除并返回集合中的多个随机元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/count/spop",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse spopCount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除并返回集合中的多个随机元素】(BootRedisSetController-spopCount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long count = req.getCount();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(count == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "count为空");
		}

		Set<String> result = null;
		try {
			result = redisService.spop(key, count);
			logger.info("===step2:【移除并返回集合中的多个随机元素】(BootRedisSetController-spopCount)-多个随机元素-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【移除并返回集合中的多个随机元素】(BootRedisSetController-spopCount)-多个随机元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【移除并返回集合中的多个随机元素】(BootRedisSetController-spopCount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合中的一个随机元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/srandmember",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse srandmember(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合中的一个随机元素素】(BootRedisSetController-srandmember)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		String result = null;
		try {
			result = redisService.srandmember(key);
			logger.info("===step2:【返回集合中的一个随机元素素】(BootRedisSetController-srandmember)-一个随机元素-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回集合中的一个随机元素素】(BootRedisSetController-srandmember)-一个随机元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回集合中的一个随机元素素】(BootRedisSetController-srandmember)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回集合中的一个随机元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/count/srandmember",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse srandmemberCount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回集合中的一个随机元素】(BootRedisSetController-srandmemberCount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long count = req.getCount();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(count == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "count为空");
		}

		List<String> result = null;
		try {
			result = redisService.srandmember(key, count.intValue());
			logger.info("===step2:【返回集合中的一个随机元素】(BootRedisSetController-srandmemberCount)-一个随机元素-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回集合中的一个随机元素】(BootRedisSetController-srandmemberCount)-一个随机元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回集合中的一个随机元素】(BootRedisSetController-srandmemberCount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除集合中的一个成员元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/srem",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse srem(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除集合中的一个成员元素】(BootRedisSetController-srem)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String member = req.getMember();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(member)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "member为空");
		}

		long len = 0l;
		try {
			len = redisService.srem(key, member);
			logger.info("===step2:【移除集合中的一个成员元素】(BootRedisSetController-srem)-移除一个成员元素-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【移除集合中的一个成员元素】(BootRedisSetController-srem)-移除一个成员元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除集合中的一个成员元素】(BootRedisSetController-srem)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除集合中的多个成员元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/srem",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sremArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除集合中的多个成员元素】(BootRedisSetController-sremArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] members = req.getMembers();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(members == null || members.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "members为空");
		}

		long len = 0l;
		try {
			len = redisService.srem(key, members);
			logger.info("===step2:【移除集合中的多个成员元素】(BootRedisSetController-sremArray))-移除多个成员元素-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【移除集合中的多个成员元素】(BootRedisSetController-sremArray)-移除多个成员元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除集合中的多个成员元素】(BootRedisSetController-sremArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回给定集合的并集
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sunion",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sunion(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回给定集合的并集】(BootRedisSetController-sunion)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sunion(key);
			logger.info("===step2:【返回给定集合的并集】(BootRedisSetController-sunion)-集合并集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回给定集合的并集】(BootRedisSetController-sunion)-集合并集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回给定集合的并集】(BootRedisSetController-sunion)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回给定所有给定集合交集
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/sunion",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sunionArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回给定集合的并集】(BootRedisSetController-sunionArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		Set<String> result = null;
		try {
			result = redisService.sunion(keys);
			logger.info("===step2:【返回给定集合的并集】(BootRedisSetController-sunionArray)-集合并集-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回给定集合的并集】(BootRedisSetController-sunionArray)-集合并集-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回给定集合的并集】(BootRedisSetController-sunionArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合的并集存储指定的集合
	 * @param dstkey
	 * @param key
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sunionstore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sunionstore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合的并集存储指定的集合】(BootRedisSetController-sunionstore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(dstkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "dstkey为空");
		}

		long len = 0l;
		try {
			len = redisService.sunionstore(dstkey, key);
			logger.info("===step2:【给定集合的并集存储指定的集合】(BootRedisSetController-sunionstore)-并集存储-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【给定集合的并集存储指定的集合】(BootRedisSetController-sunionstore)-并集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合的并集存储指定的集合】(BootRedisSetController-sunionstore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 给定集合之间的差集存储在指定的集合
	 * @param dstkey
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/sunionstore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sunionstoreArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【给定集合的并集存储指定的集合】(BootRedisSetController-sunionstoreArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String dstkey = req.getDstkey();
		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.sunionstore(dstkey, keys);
			logger.info("===step2:【给定集合的并集存储指定的集合】(BootRedisSetController-sunionstoreArray)-并集存储-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【给定集合的并集存储指定的集合】(BootRedisSetController-sunionstoreArray)-并集存储-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【给定集合的并集存储指定的集合】(BootRedisSetController-sunionstoreArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 迭代集合键中的元素
	 * @param key
	 * @param cursor
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sscan",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sscan(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【迭代集合键中的元素】(BootRedisSetController-sscan)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String cursor = req.getCursor();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(cursor)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "cursor为空");
		}

		ScanResult<String> result = null;
		try {
			result = redisService.sscan(key, cursor);
			logger.info("===step2:【迭代集合键中的元素】(BootRedisSetController-sscan)-迭代集合-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【迭代集合键中的元素】(BootRedisSetController-sscan)-迭代集合-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【迭代集合键中的元素】(BootRedisSetController-sscan)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis Set(集合) 命令工具方法 ****************************/

}