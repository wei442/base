package com.cloud.provider.redis.controller;

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
import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.common.exception.RedisException;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.constants.RedisConstants;
import com.cloud.provider.redis.rest.request.RedisRequest;

import redis.clients.jedis.ScanResult;

/**
 * Redis Hash（哈希表） RedisHashController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/redis/hash")
public class RedisHashController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Hash(哈希表) 命令工具方法
	/************************** jredis Hash(哈希表) 命令工具方法 ****************************/
	/**
	 * 删除哈希表key中的一个字段
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hdel",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hdel(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【删除哈希表key中的一个字段】(RedisHashController-hdel)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "field为空");
		}

		long len = 0l;
		try {
			len = redisService.hdel(key, field);
			logger.info("===step2:【删除哈希表key中的一个字段】(RedisHashController-hdel)-删除哈希表key中的指定字段-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【删除哈希表key中的一个指定字段】(RedisHashController-hdel)-删除哈希表key中的指定字段-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【删除哈希表key中的一个字段】(RedisHashController-hdel)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 删除哈希表key中的多个字段
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/hdel",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hdelArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【删除哈希表key中的多个字段】(RedisHashController-hdelArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] fields = req.getFields();
		if(fields == null || fields.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "fields为空");
		}

		long len = 0l;
		try {
			len = redisService.hdel(key, fields);
			logger.info("===step2:【删除哈希表key中的多个字段】(RedisHashController-hdelArray)-删除哈希表key中的多个字段-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【删除哈希表key中的多个字段】(RedisHashController-hdelArray)-删除哈希表key中的多个字段-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
        	if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
        		return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
        	}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【删除哈希表key中的多个字段】(RedisHashController-hdelArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 查看哈希表的指定字段是否存在
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hexists",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hexists(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【查看哈希表的指定字段是否存在】(RedisHashController-hexists)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "field为空");
		}

		boolean flag = false;
		try {
			flag = redisService.hexists(key, field);
			logger.info("===step2:【查看哈希表的指定字段是否存在】(RedisHashController-hexists)-查看哈希表的指定字段是否存在-返回信息, flag:{}", flag);
		} catch (RedisException e) {
			logger.error("===step2.1:【查看哈希表的指定字段是否存在】(RedisHashController-hexists)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【查看哈希表的指定字段是否存在】(RedisHashController-hexists)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表中指定字段的值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hget",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hget(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表中指定字段的值】(RedisHashController-hget)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "field为空");
		}

		String result = null;
		try {
			result = redisService.hget(key, field);
			logger.info("===step2:【 查看哈希表的指定字段是否存在】(RedisHashController-hexists)-查看哈希表的指定字段是否存在-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【 查看哈希表的指定字段是否存在】(RedisHashController-hexists)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【 查看哈希表的指定字段是否存在】(RedisHashController-hexists)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表中所有的字段和值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hgetAll",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hgetAll(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表中所有的字段和值】(RedisHashController-hgetAll)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		Map<String, String> result = null;
		try {
			result = redisService.hgetAll(key);
			logger.info("===step2:【返回哈希表中所有的字段和值】(RedisHashController-hgetAll)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【 查看哈希表的指定字段是否存在】(RedisHashController-hexists)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回哈希表中所有的字段和值】(RedisHashController-hgetAll)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 哈希表中的字段值加上指定增量值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hincrBy",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hincrBy(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【哈希表中的字段值加上指定增量值】(RedisHashController-hincrBy))-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "field为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.hincrBy(key, field, new Integer(value));
			logger.info("===step2:【哈希表中的字段值加上指定增量值】(RedisHashController-hincrBy)-哈希表中的字段值加上指定增量值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【哈希表中的字段值加上指定增量值】(RedisHashController-hincrBy)-哈希表中的字段值加上指定增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【哈希表中的字段值加上指定增量值】(RedisHashController-hincrBy)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 哈希表中的字段值加上指定浮点数增量值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hincrByFloat",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hincrByFloat(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【哈希表中的字段值加上指定浮点数增量值】(RedisHashController-hincrByFloat)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "field为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		double len = 0d;
		try {
			len = redisService.hincrByFloat(key, field, new Double(value));
			logger.info("===step2:【哈希表中的字段值加上指定浮点数增量值】(RedisHashController-hincrByFloat)-哈希表中的字段值加上指定增量值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【哈希表中的字段值加上指定浮点数增量值】(RedisHashController-hincrByFloat)-哈希表中的字段值加上指定增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【哈希表中的字段值加上指定浮点数增量值】(RedisHashController-hincrByFloat)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 获取哈希表中的所有字段名
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hkeys",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hkeys(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取哈希表中的所有字段名】(RedisHashController-hkeys)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		Set<String> result = null;
		try {
			result = redisService.hkeys(key);
			logger.info("===step2:【获取哈希表中的所有字段名】(RedisHashController-hkeys)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【获取哈希表中的所有字段名】(RedisHashController-hkeys)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【获取哈希表中的所有字段名】(RedisHashController-hkeys)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 获取哈希表中字段的数量
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hlen",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hlen(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取哈希表中字段的数量】(RedisHashController-hlen)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.hlen(key);
			logger.info("===step2:【获取哈希表中字段的数量】(RedisHashController-hlen))-删除哈希表key中的指定字段-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【获取哈希表中字段的数量】(RedisHashController-hlen)-删除哈希表key中的指定字段-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【获取哈希表中字段的数量】(RedisHashController-hlen)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表中一个给定字段的值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hmget",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hmget(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表中一个给定字段的值】(RedisHashController-hmget)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "field为空");
		}

		List<String> result = null;
		try {
			result = redisService.hmget(key, field);
			logger.info("===step2:【返回哈希表中一个给定字段的值】(RedisHashController-hmget)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回哈希表中一个给定字段的值】(RedisHashController-hmget)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回哈希表中一个给定字段的值】(RedisHashController-hmget)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表中多个给定字段的值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/hmget",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hmgetArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表中多个给定字段的值】(RedisHashController-hmgetArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] fields = req.getFields();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(fields == null || fields.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "fields为空");
		}

		List<String> result = null;
		try {
			result = redisService.hmget(key, fields);
			logger.info("===step2:【返回哈希表中多个给定字段的值】(RedisHashController-hmgetArray)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回哈希表中多个给定字段的值】(RedisHashController-hmget)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回哈希表中多个给定字段的值】(RedisHashController-hmgetArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 多个字段-值对设置到哈希表
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hmset",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hmset(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个字段-值对设置到哈希表】(RedisHashController-hmset)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Map<String, String> hash = req.getHash();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(hash == null || hash.isEmpty()) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "hash为空");
		}

		String result = null;
		try {
			result = redisService.hmset(key, hash);
			logger.info("===step2:【多个字段-值对设置到哈希表】(RedisHashController-hmset)-查看哈希表的指定字段是否存在-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【多个字段-值对设置到哈希表】(RedisHashController-hmset)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【多个字段-值对设置到哈希表】(RedisHashController-hmset))-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 哈希表中的字段赋值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hset",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hset(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【哈希表中的字段赋值】(RedisHashController-hset)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "field为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.hset(key, field, value);
			logger.info("===step2:【哈希表中的字段赋值】(RedisHashController-hset)-哈希表中的字段值加上指定增量值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【哈希表中的字段赋值】(RedisHashController-hset)-哈希表中的字段值加上指定增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【哈希表中的字段赋值】(RedisHashController-hset)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 哈希表中的字段赋值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hsetnx",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hsetnx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【哈希表中的字段赋值】(RedisHashController-hsetnx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String field = req.getField();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(field)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "field为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.hset(key, field, value);
			logger.info("===step2:【哈希表中的字段赋值】(RedisHashController-hsetnx)-哈希表中的字段值加上指定增量值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【哈希表中的字段赋值】(RedisHashController-hsetnx)-哈希表中的字段值加上指定增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【哈希表中的字段赋值】(RedisHashController-hsetnx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回哈希表所有字段的值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hvals",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hvals(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回哈希表所有字段的值】(RedisHashController-hvals)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		List<String> result = null;
		try {
			result = redisService.hvals(key);
			logger.info("===step2:【返回哈希表所有字段的值】(RedisHashController-hvals)-查看哈希表的指定字段是否存在-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回哈希表所有字段的值】(RedisHashController-hvals)-查看哈希表的指定字段是否存在-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回哈希表所有字段的值】(RedisHashController-hvals)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 迭代集合键中的元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/hscan",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse hscan(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【迭代集合键中的元素】(RedisHashController-hscan)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String cursor = req.getCursor();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(cursor)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "cursor为空");
		}

		ScanResult<Map.Entry<String, String>> result = null;
		try {
			result = redisService.hscan(key, cursor);
			logger.info("===step2:【迭代集合键中的元素】(RedisHashController-hscan)-迭代集合键中的元素-返回信息, result.size:{}", result == null ? null : result.getResult().size());
		} catch (RedisException e) {
			logger.error("===step2.1:【迭代集合键中的元素】(RedisHashController-hscan)-迭代集合键中的元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【迭代集合键中的元素】(RedisHashController-hscan)--返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis Hash(哈希表) 命令工具方法 ****************************/

}