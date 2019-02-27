package com.ochain.provider.redis.controllers;

import java.util.List;
import java.util.Map;
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
import com.ochain.common.constants.BootConstants;
import com.ochain.common.constants.RedisConstants;
import com.ochain.common.exception.BootServiceException;
import com.ochain.provider.redis.boot.BootRestMapResponse;
import com.ochain.provider.redis.rest.request.RedisRequest;

import redis.clients.jedis.ScanResult;

/**
 * Redis Hash（哈希表） BootRedisHashController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/hash")
public class BootRedisHashController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Hash(哈希表) 命令工具方法
	/************************** jredis Hash(哈希表) 命令工具方法 ****************************/
	/**
	 * 删除哈希表key中的一个字段
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hdel",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hdel(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【删除哈希表key中的一个字段】(BootRedisHashController-hdel)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "field为空");
		}

		long len = 0l;
		try {
			len = redisService.hdel(key, field);
			logger.info("===step2:【删除哈希表key中的一个字段】(BootRedisHashController-hdel)-删除哈希表key中的指定字段-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【删除哈希表key中的一个指定字段】(BootRedisHashController-hdel)-删除哈希表key中的指定字段-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【删除哈希表key中的一个字段】(BootRedisHashController-hdel)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 删除哈希表key中的多个字段
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/hdel",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hdelArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【删除哈希表key中的多个字段】(BootRedisHashController-hdelArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] fields = req.getFields();
		if(fields == null || fields.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "fields为空");
		}

		long len = 0l;
		try {
			len = redisService.hdel(key, fields);
			logger.info("===step2:【删除哈希表key中的多个字段】(BootRedisHashController-hdelArray)-删除哈希表key中的多个字段-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【删除哈希表key中的多个字段】(BootRedisHashController-hdelArray)-删除哈希表key中的多个字段-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
        	if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
        		return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
        	}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【删除哈希表key中的多个字段】(BootRedisHashController-hdelArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 查看哈希表的指定字段是否存在
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hexists",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hexists(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【查看哈希表的指定字段是否存在】(BootRedisHashController-hexists)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "field为空");
		}

		boolean flag = false;
		try {
			flag = redisService.hexists(key, field);
			logger.info("===step2:【查看哈希表的指定字段是否存在】(BootRedisHashController-hexists)-查看哈希表的指定字段是否存在-返回信息, flag:{}", flag);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【查看哈希表的指定字段是否存在】(BootRedisHashController-hexists)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【查看哈希表的指定字段是否存在】(BootRedisHashController-hexists)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表中指定字段的值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hget",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hget(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表中指定字段的值】(BootRedisHashController-hget)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "field为空");
		}

		String result = null;
		try {
			result = redisService.hget(key, field);
			logger.info("===step2:【 查看哈希表的指定字段是否存在】(BootRedisHashController-hexists)-查看哈希表的指定字段是否存在-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【 查看哈希表的指定字段是否存在】(BootRedisHashController-hexists)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【 查看哈希表的指定字段是否存在】(BootRedisHashController-hexists)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表中所有的字段和值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hgetAll",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hgetAll(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表中所有的字段和值】(BootRedisHashController-hgetAll)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		Map<String, String> result = null;
		try {
			result = redisService.hgetAll(key);
			logger.info("===step2:【返回哈希表中所有的字段和值】(BootRedisHashController-hgetAll)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【 查看哈希表的指定字段是否存在】(BootRedisHashController-hexists)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回哈希表中所有的字段和值】(BootRedisHashController-hgetAll)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 哈希表中的字段值加上指定增量值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hincrBy",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hincrBy(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【哈希表中的字段值加上指定增量值】(BootRedisHashController-hincrBy))-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "field为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		long len = 0l;
		try {
			len = redisService.hincrBy(key, field, new Integer(value));
			logger.info("===step2:【哈希表中的字段值加上指定增量值】(BootRedisHashController-hincrBy)-哈希表中的字段值加上指定增量值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【哈希表中的字段值加上指定增量值】(BootRedisHashController-hincrBy)-哈希表中的字段值加上指定增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【哈希表中的字段值加上指定增量值】(BootRedisHashController-hincrBy)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 哈希表中的字段值加上指定浮点数增量值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hincrByFloat",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hincrByFloat(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【哈希表中的字段值加上指定浮点数增量值】(BootRedisHashController-hincrByFloat)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "field为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		double len = 0d;
		try {
			len = redisService.hincrByFloat(key, field, new Double(value));
			logger.info("===step2:【哈希表中的字段值加上指定浮点数增量值】(BootRedisHashController-hincrByFloat)-哈希表中的字段值加上指定增量值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【哈希表中的字段值加上指定浮点数增量值】(BootRedisHashController-hincrByFloat)-哈希表中的字段值加上指定增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【哈希表中的字段值加上指定浮点数增量值】(BootRedisHashController-hincrByFloat)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 获取哈希表中的所有字段名
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hkeys",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hkeys(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取哈希表中的所有字段名】(BootRedisHashController-hkeys)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.hkeys(key);
			logger.info("===step2:【获取哈希表中的所有字段名】(BootRedisHashController-hkeys)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【获取哈希表中的所有字段名】(BootRedisHashController-hkeys)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【获取哈希表中的所有字段名】(BootRedisHashController-hkeys)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 获取哈希表中字段的数量
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hlen",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hlen(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取哈希表中字段的数量】(BootRedisHashController-hlen)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.hlen(key);
			logger.info("===step2:【获取哈希表中字段的数量】(BootRedisHashController-hlen))-删除哈希表key中的指定字段-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【获取哈希表中字段的数量】(BootRedisHashController-hlen)-删除哈希表key中的指定字段-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【获取哈希表中字段的数量】(BootRedisHashController-hlen)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表中一个给定字段的值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hmget",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hmget(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表中一个给定字段的值】(BootRedisHashController-hmget)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "field为空");
		}

		List<String> result = null;
		try {
			result = redisService.hmget(key, field);
			logger.info("===step2:【返回哈希表中一个给定字段的值】(BootRedisHashController-hmget)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回哈希表中一个给定字段的值】(BootRedisHashController-hmget)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回哈希表中一个给定字段的值】(BootRedisHashController-hmget)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表中多个给定字段的值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/hmget",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hmgetArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表中多个给定字段的值】(BootRedisHashController-hmgetArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] fields = req.getFields();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(fields == null || fields.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "fields为空");
		}

		List<String> result = null;
		try {
			result = redisService.hmget(key, fields);
			logger.info("===step2:【返回哈希表中多个给定字段的值】(BootRedisHashController-hmgetArray)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回哈希表中多个给定字段的值】(BootRedisHashController-hmget)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回哈希表中多个给定字段的值】(BootRedisHashController-hmgetArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 多个字段-值对设置到哈希表
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hmset",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hmset(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个字段-值对设置到哈希表】(BootRedisHashController-hmset)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Map<String, String> hash = req.getHash();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(hash == null || hash.isEmpty()) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "hash为空");
		}

		String result = null;
		try {
			result = redisService.hmset(key, hash);
			logger.info("===step2:【多个字段-值对设置到哈希表】(BootRedisHashController-hmset)-查看哈希表的指定字段是否存在-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【多个字段-值对设置到哈希表】(BootRedisHashController-hmset)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【多个字段-值对设置到哈希表】(BootRedisHashController-hmset))-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 哈希表中的字段赋值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hset",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hset(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【哈希表中的字段赋值】(BootRedisHashController-hset)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "field为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		long len = 0l;
		try {
			len = redisService.hset(key, field, value);
			logger.info("===step2:【哈希表中的字段赋值】(BootRedisHashController-hset)-哈希表中的字段值加上指定增量值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【哈希表中的字段赋值】(BootRedisHashController-hset)-哈希表中的字段值加上指定增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【哈希表中的字段赋值】(BootRedisHashController-hset)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 哈希表中的字段赋值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hsetnx",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hsetnx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【哈希表中的字段赋值】(BootRedisHashController-hsetnx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "field为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		long len = 0l;
		try {
			len = redisService.hset(key, field, value);
			logger.info("===step2:【哈希表中的字段赋值】(BootRedisHashController-hsetnx)-哈希表中的字段值加上指定增量值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【哈希表中的字段赋值】(BootRedisHashController-hsetnx)-哈希表中的字段值加上指定增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【哈希表中的字段赋值】(BootRedisHashController-hsetnx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表所有字段的值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hvals",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hvals(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表所有字段的值】(BootRedisHashController-hvals)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		List<String> result = null;
		try {
			result = redisService.hvals(key);
			logger.info("===step2:【返回哈希表所有字段的值】(BootRedisHashController-hvals)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回哈希表所有字段的值】(BootRedisHashController-hvals)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回哈希表所有字段的值】(BootRedisHashController-hvals)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 迭代集合键中的元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/hscan",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse hscan(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【迭代集合键中的元素】(BootRedisHashController-hscan)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String cursor = req.getCursor();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(cursor)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "cursor为空");
		}

		ScanResult<Map.Entry<String, String>> result = null;
		try {
			result = redisService.hscan(key, cursor);
			logger.info("===step2:【迭代集合键中的元素】(BootRedisHashController-hscan)-迭代集合键中的元素-返回信息, result.size:{}", result == null ? null : result.getResult().size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【迭代集合键中的元素】(BootRedisHashController-hscan)-迭代集合键中的元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【迭代集合键中的元素】(BootRedisHashController-hscan)--返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis Hash(哈希表) 命令工具方法 ****************************/

}