package com.cloud.provider.redis.controller;

import java.util.Set;

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

import redis.clients.jedis.SortingParams;

/**
 * Redis Key（键） RedisKeyController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/redis/key")
public class RedisKeyController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//redis Service
	@Autowired
	private IRedisService redisService;

	// TODO 键(key) 命令工具方法
	/************************** jredis 键(key) 命令工具方法 ****************************/
	/**
	 * 删除一个key
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/del",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse del(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【删除一个key】(RedisKeyController-del)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.del(key);
			logger.info("===step2:【删除一个key】(RedisKeyController-del)-删除 key-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【删除一个key】(RedisKeyController-del)-删除key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【删除一个key】(RedisKeyController-del)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 删除多个key
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/del",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse delArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【删除多个key】(RedisKeyController-delArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.del(keys);
			logger.info("===step2:【删除多个key】(RedisKeyController-delArray)-删除多个key-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【删除多个key】(RedisKeyController-delArray)-删除多个 key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
        	if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
        		return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
        	}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【删除多个key】(RedisKeyController-delArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 序列化给定key，并返回被序列化的值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/dump",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse dump(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【序列化给定key】(RedisKeyController-dump)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		byte[] result = null;
		try {
			result = redisService.dump(key);
			logger.info("===step2:【序列化给定key】(RedisKeyController-dump)-序列化给定key, result:{}", result == null ? null : result.length);
		} catch (RedisException e) {
			logger.error("===step2.1:【序列化给定key】(RedisKeyController-dump)-序列化给定key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【序列化给定key】(RedisKeyController-dump)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 检查key是否存在
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/exists",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse exists(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【检查key是否存在】(RedisKeyController-exists)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		boolean flag = false;
		try {
			flag = redisService.exists(key);
			logger.info("===step2:【检查key是否存在】(RedisKeyController-exists)-检查key, flag:{}", flag);
		} catch (RedisException e) {
			logger.error("===step2.1:【检查key是否存在】(RedisKeyController-exists)-检查key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【检查key是否存在】(RedisKeyController-exists)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 检查多个key是否存在
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/exists",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse existsArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【检查多个key是否存在】(RedisKeyController-existsArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.exists(keys);
			logger.info("===step2:【检查多个key是否存在】(RedisKeyController-existsArray)-检查多个key-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【检查多个key是否存在】(RedisKeyController-existsArray)-检查多个key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
        	if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
        		return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
        	}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【检查多个key是否存在】(RedisKeyController-existsArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置单个key的过期时间，以秒计
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/expire",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse expire(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置单个key的过期时间】(RedisKeyController-expire)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer seconds = req.getSeconds();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(seconds == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "seconds为空");
		}

		long len = 0l;
		try {
			len = redisService.expire(key, seconds);
			logger.info("===step2:【设置单个key的过期时间】(RedisKeyController-expire)-设置过期时间-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【设置单个key的过期时间】(RedisKeyController-expire)-设置过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【设置单个key的过期时间】(RedisKeyController-expire)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置单个key的过期时间，以秒计。时间参数是UNIX时间戳(unix timestamp)
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/expireAt",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse expireAt(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置单个key的过期时间】(RedisKeyController-expireAt)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long unixTime = req.getUnixTime();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(unixTime == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "unixTime为空");
		}

		long len = 0l;
		try {
			len = redisService.expireAt(key, unixTime);
			logger.info("===step2:【设置单个key的过期时间】(RedisKeyController-expireAt)-设置过期时间-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【设置单个key的过期时间】(RedisKeyController-expireAt)-设置过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【设置单个key的过期时间】(RedisKeyController-expireAt)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 查找所有符合给定模式的key
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/keys",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse keys(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【查找所有符合给定模式的key】(RedisKeyController-keys)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String pattern = req.getPattern();
		if(StringUtils.isBlank(pattern)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "pattern为空");
		}

		Set<String> result = null;
		try {
			result = redisService.keys(pattern);
			logger.info("===step2:【查找所有符合给定模式的key】(RedisKeyController-keys)-查找所有符合给定模式的key-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【查找所有符合给定模式的key】(RedisKeyController-keys)-查找所有符合给定模式的key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【查找所有符合给定模式的key】(RedisKeyController-keys))-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将key原子性地从当前实例传送到目标实例的指定数据库上
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/migrate",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse migrate(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将key原子性地从当前实例传送到目标实例的指定数据库上】(RedisKeyController-migrate)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String host = req.getHost();
		Integer port = req.getPort();
		String key = req.getKey();
		Integer destinationDb = req.getDestinationDb();
		Integer timeout = req.getTimeout();
		if(StringUtils.isBlank(host)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "host为空");
		} else if(port == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "port为空");
		} else if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(destinationDb == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "destinationDb为空");
		} else if(timeout == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "timeout为空");
		}

		String result = null;
		try {
			result = redisService.migrate(host, port, key, destinationDb, timeout);
			logger.info("===step2:【将key原子性地从当前实例传送到目标实例的指定数据库上】(RedisKeyController-migrate)-从当前实例传送到目标实例的指定数据库上-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【将key原子性地从当前实例传送到目标实例的指定数据库上】(RedisKeyController-migrate)-从当前实例传送到目标实例的指定数据库上-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将key原子性地从当前实例传送到目标实例的指定数据库上】(RedisKeyController-migrate)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将当前数据库的key移动到给定的数据库db当中
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/move",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse move(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将当前数据库的key移动到给定的数据库db当中】(RedisKeyController-move)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer dbIndex = req.getDbIndex();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(dbIndex == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dbIndex为空");
		}

		long len = 0l;
		try {
			len = redisService.move(key, dbIndex);
			logger.info("===step2:【将当前数据库的key移动到给定的数据库db当中】(RedisKeyController-move)-当前数据库的key移动到给定的数据库db当中-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【将当前数据库的key移动到给定的数据库db当中】(RedisKeyController-move)-当前数据库的key移动到给定的数据库db当中-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【将当前数据库的key移动到给定的数据库db当中】(RedisKeyController-move)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除key的生存时间
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/persist",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse persist(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除key的生存时间】(RedisKeyController-persist)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.persist(key);
			logger.info("===step2:【移除key的生存时间】(RedisKeyController-persist)-移除key的生存时间-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【移除key的生存时间】(RedisKeyController-persist)-移除key的生存时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除key的生存时间】(RedisKeyController-persist)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置 key的过期时间(毫秒)
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/pexpire",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse pexpire(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置 key的过期时间(毫秒)】(RedisKeyController-pexpire)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long milliseconds = req.getMilliseconds();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(milliseconds == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "milliseconds为空");
		}

		long len = 0l;
		try {
			len = redisService.pexpire(key, milliseconds);
			logger.info("===step2:【设置 key的过期时间(毫秒)】(RedisKeyController-pexpire)-设置key的毫秒过期时间-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【设置 key的过期时间(毫秒)】(RedisKeyController-pexpire)-设置key的毫秒过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【用于设置 key的过期时间(毫秒)】(RedisKeyController-pexpire)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置 key的过期时间(毫秒)。时间参数是UNIX时间戳(unix timestamp)。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/pexpireAt",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse pexpireAt(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置 key的过期时间(毫秒)】(RedisKeyController-pexpireAt)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long millisecondsTimestamp = req.getMillisecondsTimestamp();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(millisecondsTimestamp == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "millisecondsTimestamp为空");
		}

		long len = 0l;
		try {
			len = redisService.pexpireAt(key, millisecondsTimestamp);
			logger.info("===step2:【设置 key的过期时间(毫秒)】(RedisKeyController-pexpireAt)-设置key的毫秒过期时间-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【设置 key的过期时间(毫秒)】(RedisKeyController-pexpireAt)-设置key的毫秒过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【用于设置 key的过期时间(毫秒)】(RedisKeyController-pexpireAt)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}


	/**
	 * 毫秒为单位返回 key的剩余生存时间
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/pttl",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse pttl(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【毫秒为单位返回key的剩余生存时间】(RedisKeyController-pttl)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.pttl(key);
			logger.info("===step2:【毫秒为单位返回key的剩余生存时间】(RedisKeyController-pttl)-key的剩余毫秒生存时间-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【毫秒为单位返回key的剩余生存时间】(RedisKeyController-pttl)-key的剩余毫秒生存时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【毫秒为单位返回key的剩余生存时间】(RedisKeyController-pttl)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 当前数据库中随机返回一个key
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/randomKey",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse randomKey(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【当前数据库中随机返回一个key】(RedisKeyController-randomKey)-传入参数为空");

		String result = null;
		try {
			result = redisService.randomKey();
			logger.info("===step2:【当前数据库中随机返回一个key】(RedisKeyController-randomKey)-随机返回一个 key-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【当前数据库中随机返回一个key】(RedisKeyController-randomKey)-随机返回一个 key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【当前数据库中随机返回一个key】(RedisKeyController-randomKey)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 修改key的名称
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/rename",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse rename(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【修改key的名称】(RedisKeyController-rename)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String oldkey = req.getOldkey();
		String newkey = req.getNewkey();
		if(StringUtils.isBlank(oldkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "oldkey为空");
		} else if(StringUtils.isBlank(newkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "newkey为空");
		}

		String result = null;
		try {
			result = redisService.rename(oldkey, newkey);
			logger.info("===step2:【修改key的名称】(RedisKeyController-rename)-修改key的名称-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【修改key的名称】(RedisKeyController-rename)-修改key的名称-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【修改key的名称】(RedisKeyController-rename)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 新key不存在时修改key的名称
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/renamenx",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse renamenx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:新key不存在时修改key的名称】(RedisKeyController-renamenx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String oldkey = req.getOldkey();
		String newkey = req.getNewkey();
		if(StringUtils.isBlank(oldkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "oldkey为空");
		} else if(StringUtils.isBlank(newkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "newkey为空");
		}

		long len = 0l;
		try {
			len = redisService.renamenx(oldkey, newkey);
			logger.info("===step2:【新key不存在时修改key的名称】(RedisKeyController-renamenx)-新key不存在时修改key的名称-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【新key不存在时修改key的名称】(RedisKeyController-renamenx)-新key不存在时修改key的名称-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【新key不存在时修改key的名称】(RedisKeyController-renamenx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 反序列化给定的序列化值，并将它和给定的 key 关联
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/restore",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse restore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【反序列化给定的序列化值】(RedisKeyController-restore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer ttl = req.getTtl();
		byte[] serializedValue = req.getSerializedValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(ttl == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "ttl为空");
		} else if(serializedValue == null || serializedValue.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "ttl为空");
		}

		String result = null;
		try {
			result = redisService.restore(key, ttl, serializedValue);
			logger.info("===step2:【反序列化给定的序列化值】(RedisKeyController-restore)-反序列化给定的序列化值-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【反序列化给定的序列化值】(RedisKeyController-restore)-反序列化给定的序列化值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【反序列化给定的序列化值】(RedisKeyController-restore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回或保存给定列表、集合、有序集合key中经过排序的元素
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sort",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sort(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回或保存给定列表、集合、有序集合key中经过排序的元素】(RedisKeyController-sort)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String sortingParameters = req.getSortingParameters();
		String dstkey = req.getDstkey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isNotBlank(sortingParameters)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "sortingParameters为空");
		} else if(StringUtils.isNotBlank(dstkey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dstkey为空");
		}

		SortingParams parameters = new SortingParams();
		long len = 0l;
		try {
			if(StringUtils.equalsIgnoreCase(sortingParameters, "asc")) {
				len = redisService.sort(key, parameters.asc(), dstkey);
			} else if(StringUtils.equalsIgnoreCase(sortingParameters, "desc")) {
				len = redisService.sort(key, parameters.desc(), dstkey);
			} else if(StringUtils.equalsIgnoreCase(sortingParameters, "alpha")) {
				len = redisService.sort(key, parameters.alpha(), dstkey);
			} else {
				len = redisService.sort(key, parameters.nosort(), dstkey);
			}
			logger.info("===step2:【返回或保存给定列表、集合、有序集合key中经过排序的元素】(RedisKeyController-sort)-返回经过排序的元素-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【【返回或保存给定列表、集合、有序集合key中经过排序的元素】(RedisKeyController-sort)-经过排序的元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回或保存给定列表、集合、有序集合key中经过排序的元素】(RedisKeyController-sort)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以秒为单位返回key的剩余过期时间
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/ttl",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse ttl(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【以秒为单位返回key的剩余过期时间】(RedisKeyController-ttl)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.ttl(key);
			logger.info("===step2:【以秒为单位返回key的剩余过期时间】(RedisKeyController-ttl)-key的剩余秒生存时间-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【以秒为单位返回key的剩余过期时间】(RedisKeyController-ttl)-key的剩余秒生存时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【以秒为单位返回key的剩余过期时间】(RedisKeyController-ttl)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key储存的值的类型
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/type",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse type(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key储存的值的类型】(RedisKeyController-type)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		String result = null;
		try {
			result = redisService.type(key);
			logger.info("===step2:【key储存的值的类型】(RedisKeyController-type)-key储存的值的类型-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【key储存的值的类型】(RedisKeyController-type)-key储存的值的类型-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【key储存的值的类型】(RedisKeyController-type)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis 键(key) 命令工具方法 ****************************/

}