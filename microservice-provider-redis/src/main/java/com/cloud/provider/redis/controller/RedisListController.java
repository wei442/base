package com.cloud.provider.redis.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.constants.RedisConstants;
import com.cloud.provider.redis.enums.RedisResultEnum;
import com.cloud.provider.redis.exception.RedisException;
import com.cloud.provider.redis.rest.request.RedisRequest;
import com.cloud.provider.redis.service.IRedisService;

import redis.clients.jedis.BinaryClient.LIST_POSITION;

/**
 * Redis List（列表） RedisListController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/redis/list")
public class RedisListController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//redis Service
	@Autowired
	private IRedisService redisService;

	// TODO List(列表) 命令工具方法
	/************************** jredis List(列表) 命令工具方法 ****************************/
	/**
	 * 不超时移除并返回列表的第一个元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/blpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse blpop(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【不超时移除并返回列表的第一个元素】(RedisListController-blpop)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.blpop(key);
			logger.info("===step2:【不超时移除并返回列表的第一个元素】(RedisListController-blpop)-不超时移除并返回列表-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【不超时移除并返回列表的第一个元素】(RedisListController-blpop)-不超时移除并返回列表-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【不超时移除并返回列表的第一个元素】(RedisListController-blpop)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 不超时移除并返回列表的第一个元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/blpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse blpopArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【不超时移除并返回列表的第一个元素】(RedisListController-blpopArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		String result = null;
		try {
			result = redisService.blpop(keys);
			logger.info("===step2:【不超时移除并返回列表的第一个元素】(RedisListController-blpopArray)-不超时移除并返回列表-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【不超时移除并返回列表的第一个元素】(RedisListController-blpopArray)-不超时移除并返回列表-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【不超时移除并返回列表的第一个元素】(RedisListController-blpopArray)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 超时移除并返回列表的第一个元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/timeout/blpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse blpopTimeout(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【超时移除并返回列表的第一个元素】(RedisListController-blpopTimeout)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer timeout = req.getTimeout();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(timeout == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "timeout为空");
		}

		String result = null;
		try {
			result = redisService.blpop(timeout, key);
			logger.info("===step2:【超时移除并返回列表的第一个元素】(RedisListController-blpopTimeout)-超时移除并返回列表-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【超时移除并返回列表的第一个元素】(RedisListController-blpopTimeout)-超时移除并返回列表-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【超时移除并返回列表的第一个元素】(RedisListController-blpopTimeout)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 超时移除并返回列表的第一个元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/timeout/array/blpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse blpopTimeoutArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【超时移除并返回列表的第一个元素】(RedisListController-blpopTimeoutArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		Integer timeout = req.getTimeout();
		String[] keys = req.getKeys();
		if(timeout == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "timeout为空");
		} else if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.blpop(timeout, keys);
			logger.info("===step2:【超时移除并返回列表的第一个元素】(RedisListController-blpopTimeoutArray)-超时移除并返回列表-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【超时移除并返回列表的第一个元素】(RedisListController-blpopTimeoutArray)-超时移除并返回列表-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【超时移除并返回列表的第一个元素】(RedisListController-blpopTimeoutArray)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除并返回列表的第一个元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/brpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse brpop(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除并返回列表的第一个元素】(RedisListController-brpop)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.brpop(key);
			logger.info("===step2:【移除并返回列表的第一个元素】(RedisListController-brpop)-移除并返回列表的第一个元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除并返回列表的第一个元素】(RedisListController-brpop)-移除并返回列表的第一个元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【移除并返回列表的第一个元素】(RedisListController-brpop)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除并返回列表的第一个元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/brpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse brpopArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除并返回列表的第一个元素】(RedisListController-brpopArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		String result = null;
		try {
			result = redisService.blpop(keys);
			logger.info("===step2:【移除并返回列表的第一个元素】(RedisListController-brpopArray)-移除并返回列表的第一个元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【【移除并返回列表的第一个元素】(RedisListController-brpopArray)-移除并返回列表的第一个元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:移除并返回列表的第一个元素】(RedisListController-brpopArray)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 超时移除并返回列表的第一个元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/timeout/brpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse brpopTimeout(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【超时移除并返回列表的第一个元素】(RedisListController-brpopTimeout)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer timeout = req.getTimeout();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(timeout == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "timeout为空");
		}

		String result = null;
		try {
			result = redisService.brpop(timeout, key);
			logger.info("===step2:【超时移除并返回列表的第一个元素】(RedisListController-brpopTimeout)-移除并返回列表的第一个元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【超时移除并返回列表的第一个元素】(RedisListController-brpopTimeout)-移除并返回列表的第一个元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【超时移除并返回列表的第一个元素】(RedisListController-brpopTimeout)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 超时移除并返回列表的第一个元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/timeout/array/brpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse brpopTimeoutArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【超时移除并返回列表的第一个元素】(RedisListController-brpopTimeoutArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		Integer timeout = req.getTimeout();
		String[] keys = req.getKeys();
		if(timeout == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "timeout为空");
		} else if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.blpop(timeout, keys);
			logger.info("===step2:【超时移除并返回列表的第一个元素】(RedisListController-brpopTimeoutArray)-移除并返回列表的第一个元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【超时移除并返回列表的第一个元素】(RedisListController-brpopTimeoutArray)-移除并返回列表的第一个元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【超时移除并返回列表的第一个元素】(RedisListController-timeoutbrpopArray)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/brpoplpush",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse brpoplpush(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除并返回列表 key的头元素】(RedisListController-brpoplpush)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String source = req.getSource();
		String destination = req.getDestination();
		Integer timeout = req.getTimeout();
		if(StringUtils.isBlank(source)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "source为空");
		} else if(StringUtils.isBlank(destination)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "destination为空");
		} else if(timeout == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "timeout为空");
		}

		String result = null;
		try {
			result = redisService.brpoplpush(source, destination, timeout);
			logger.info("===step2:【移除并返回列表 key的头元素】(RedisListController-brpoplpush)-移除并返回列表的第一个元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除并返回列表 key的头元素】(RedisListController-brpoplpush)-移除并返回列表的第一个元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【移除并返回列表 key的头元素】(RedisListController-brpoplpush)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 通过索引获取列表中的元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/lindex",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lindex(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【通过索引获取列表中的元素】(RedisListController-lindex)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long index = req.getIndex();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.lindex(key, index);
			logger.info("===step2:【通过索引获取列表中的元素】(RedisListController-lindex)-通过索引获取列表中的元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【通过索引获取列表中的元素】(RedisListController-lindex)-通过索引获取列表中的元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【通过索引获取列表中的元素】(RedisListController-lindex)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 列表的元素前或者后插入元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/linsert",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse linsert(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【列表的元素前或者后插入元素】(RedisListController-linsert)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long index = req.getIndex();
		String where = req.getWhere();
		String pivot = req.getPivot();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(index == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "index为空");
		} else if(StringUtils.isBlank(where)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "where为空");
		} else if(StringUtils.isBlank(pivot)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "pivot为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			if(StringUtils.equalsIgnoreCase(where, "before")) {
				len = redisService.linsert(key, LIST_POSITION.BEFORE, pivot, value);
			} else if(StringUtils.equalsIgnoreCase(where, "after")) {
				len = redisService.linsert(key, LIST_POSITION.AFTER, pivot, value);
			}
			logger.info("===step2:【列表的元素前或者后插入元素】(RedisListController-linsert)-列表的元素前或者后插入元素-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【列表的元素前或者后插入元素】(RedisListController-linsert)-列表的元素前或者后插入元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【列表的元素前或者后插入元素】(RedisListController-linsert)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回列表的长度
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/llen",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse llen(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回列表的长度】(RedisListController-llen)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.llen(key);
			logger.info("===step2:【返回列表的长度】(RedisListController-llen)-返回列表的长度-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【返回列表的长度】(RedisListController-llen)-返回列表的长度-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回列表的长度】(RedisListController-llen)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除并返回列表 key的头元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/lpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lpop(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除并返回列表 key的头元素】(RedisListController-lpop)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.lpop(key);
			logger.info("===step2:【移除并返回列表 key的头元素】(RedisListController-lpop)-移除并返回列表的第一个元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除并返回列表 key的头元素】(RedisListController-lpop)-移除并返回列表的第一个元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【移除并返回列表 key的头元素】(RedisListController-lpop)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将一个值插入到列表头部
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/lpush",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lpush(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将一个值插入到列表头部】(RedisListController-lpush)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.lpush(key, value);
			logger.info("===step2:【将一个值插入到列表头部】(RedisListController-lpush)-将一个值插入到列表头部-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【将一个值插入到列表头部】(RedisListController-lpush)-将一个值插入到列表头部-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【将一个值插入到列表头部】(RedisListController-lpush)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 多个值插入到列表头部
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/lpush",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lpushArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个值插入到列表头部】(RedisListController-lpushArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] values = req.getValues();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(values == null || values.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "values为空");
		}

		long len = 0l;
		try {
			len = redisService.lpush(key, values);
			logger.info("===step2:【多个值插入到列表头部】(RedisListController-lpushArray)-多个值插入到列表头部-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【【多个值插入到列表头部】(RedisListController-lpushArray)-多个值插入到列表头部-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【多个值插入到列表头部】(RedisListController-lpushArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将数组值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。
	 * 和 RPUSH 命令相反，当 key 不存在时， RPUSHX 命令什么也不做
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/lpushx",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lpushx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将单个组值插入到列表表尾】(RedisListController-lpushx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.lpushx(key, value);
			logger.info("===step2:【将单个值插入到列表表尾】(RedisListController-lpushx)-组值插入到列表表尾-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【将单个值插入到列表表尾】(RedisListController-lpushx)-组值插入到列表表尾-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【将单个值插入到列表表尾】(RedisListController-lpushx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将数组值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。
	 * 和 RPUSH 命令相反，当 key 不存在时， RPUSHX 命令什么也不做
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/lpushx",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lpushxArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将多个值插入到列表表尾】(RedisListController-lpushxArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] values = req.getValues();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(values == null || values.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "values为空");
		}

		long len = 0l;
		try {
			len = redisService.lpushx(key, values);
			logger.info("===step2:【将多个值插入到列表表尾】(RedisListController-lpushxArray)-多值插入到列表表尾-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【将多个值插入到列表表尾】(RedisListController-lpushxArray)-多值插入到列表表尾-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【将多个值插入到列表表尾】(RedisListController-lpushxArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 列表指定区间内的元素，区间以偏移量 START 和 END 指定
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/lrange",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lrange(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【列表指定区间内的元素】(RedisListController-lrange)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long from = req.getFrom();
		Long to = req.getTo();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(from == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "from为空");
		} else if(to == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "to为空");
		}

		List<String> result = null;
		try {
			result = redisService.lrange(key, from, to);
			logger.info("===step2:【列表指定区间内的元素】(RedisListController-lrange)-列表指定区间内的元素-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【列表指定区间内的元素】(RedisListController-lrange)-列表指定区间内的元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【列表指定区间内的元素】(RedisListController-lrange)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 根据参数count的值，移除列表中与参数value相等的元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/lrem",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lrem(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【根据参数count的值，移除列表中与参数value相等的元素】(RedisListController-lrem)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long count = req.getCount();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(count == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "count为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.lrem(key, count, value);
			logger.info("===step2:【根据参数count的值，移除列表中与参数value相等的元素】(RedisListController-lrem))-列表指定区间内的元素-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【根据参数count的值，移除列表中与参数value相等的元素】(RedisListController-lrem)-列表指定区间内的元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【根据参数count的值，移除列表中与参数value相等的元素】(RedisListController-lrem)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 通过索引来设置元素的值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/lset",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse lset(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【通过索引来设置元素的值】(RedisListController-lset)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long index = req.getIndex();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(index == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "index为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		String result = null;
		try {
			result = redisService.lset(key, index, value);
			logger.info("===step2:【通过索引来设置元素的值】(RedisListController-lset)-通过索引来设置元素的值-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【通过索引来设置元素的值】(RedisListController-lset)-通过索引来设置元素的值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【通过索引来设置元素的值】(RedisListController-lset)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 列表进行修剪
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/ltrim",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse ltrim(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【列表进行修剪】(RedisListController-ltrim)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(start == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "start为空");
		} else if(end == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "end为空");
		}

		String result = null;
		try {
			result = redisService.ltrim(key, start, end);
			logger.info("===step2:【列表进行修剪】(RedisListController-ltrim)-列表进行修剪-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【列表进行修剪】(RedisListController-ltrim)-列表进行修剪-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【列表进行修剪】(RedisListController-ltrim)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除并返回列表的最后一个元素。
	 * 获取队列数据，不阻塞
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/rpop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse rpop(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除并返回列表的最后一个元素】(RedisListController-rpop)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.rpop(key);
			logger.info("===step2:【移除并返回列表的最后一个元素】(RedisListController-rpop)-移除并返回列表的最后一个元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除并返回列表的最后一个元素】(RedisListController-rpop)-移除并返回列表的最后一个元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【【移除并返回列表的最后一个元素】(RedisListController-rpop)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 列表中的最后一个元素(尾元素)弹出
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/rpoplpush",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse rpoplpush(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【列表中的最后一个元素(尾元素)弹出】(RedisListController-rpoplpush)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String srckey = req.getSrckey();
		String dstkey = req.getDstkey();
		if(StringUtils.isBlank(srckey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "srckey为空");
		} else if(StringUtils.isBlank(dstkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dstkey为空");
		}

		String result = null;
		try {
			result = redisService.rpoplpush(srckey, dstkey);
			logger.info("===step2:【列表中的最后一个元素(尾元素)弹出】(RedisListController-rpoplpush)-移除并返回列表的第一个元素-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【列表中的最后一个元素(尾元素)弹出】(RedisListController-rpoplpush)-移除并返回列表的第一个元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【列表中的最后一个元素(尾元素)弹出】(RedisListController-rpoplpush)-返回信息, redisResultResponse:{}", redisResponse);
		return redisResponse;
	}


	/**
	 * 多个值插入到列表尾部(最右边)
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/rpush",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse rpush(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个值插入到列表尾部(最右边)】(RedisListController-rpush)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.rpush(key, value);
			logger.info("===step2:【多个值插入到列表尾部(最右边)】(RedisListController-rpush)-多个值插入到列表尾部-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【多个值插入到列表尾部(最右边)】(RedisListController-rpush)-多个值插入到列表尾部-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【多个值插入到列表尾部(最右边)】(RedisListController-rpush)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 多个值插入到列表尾部(最右边)
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/rpush",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse rpushArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【多个值插入到列表尾部(最右边)】(RedisListController-rpushArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] values = req.getValues();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(values == null || values.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "values为空");
		}

		long len = 0l;
		try {
			len = redisService.rpush(key, values);
			logger.info("===step2:【多个值插入到列表尾部(最右边)】(RedisListController-rpushArray)-多个值插入到列表尾部-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【多个值插入到列表尾部(最右边)】(RedisListController-rpushArray)-多个值插入到列表尾部-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【多个值插入到列表尾部(最右边)】(RedisListController-rpushArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将数组值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。
	 * 和 RPUSH 命令相反，当 key 不存在时， RPUSHX 命令什么也不做
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/rpushx",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse rpushx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将单个组值插入到列表表尾】(RedisListController-rpushx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.rpushx(key, value);
			logger.info("===step2:【将单个组值插入到列表表尾】(RedisListController-rpushx)-组值插入到列表表尾-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【将单个组值插入到列表表尾】(RedisListController-rpushx)-组值插入到列表表尾-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【将单个组值插入到列表表尾】(RedisListController-rpushx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将数组值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。
	 * 和 RPUSH 命令相反，当 key 不存在时， RPUSHX 命令什么也不做
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/rpushx",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse rpushxArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将多个值插入到列表表尾】(RedisListController-rpushxArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] values = req.getValues();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(values == null || values.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "values为空");
		}

		long len = 0l;
		try {
			len = redisService.rpush(key, values);
			logger.info("===step2:【将单个值插入到列表表尾】(RedisListController-rpushxArray)-组值插入到列表表尾-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【将单个值插入到列表表尾】(RedisListController-rpushxArray)-组值插入到列表表尾-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【将单个值插入到列表表尾】(RedisListController-rpushxArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis List(列表) 命令工具方法 ****************************/

}