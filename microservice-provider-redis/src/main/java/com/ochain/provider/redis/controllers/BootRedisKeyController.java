package com.ochain.provider.redis.controllers;

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
import com.ochain.common.constants.BootConstants;
import com.ochain.common.constants.RedisConstants;
import com.ochain.common.exception.BootServiceException;
import com.ochain.provider.redis.boot.BootRestMapResponse;
import com.ochain.provider.redis.rest.request.RedisRequest;
import com.ochain.provider.redis.service.IBootRedisService;

import redis.clients.jedis.SortingParams;

/**
 * Redis Key（键） BootRedisKeyController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/key")
public class BootRedisKeyController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//redis Service
	@Autowired
	private IBootRedisService redisService;

	// TODO 键(key) 命令工具方法
	/************************** jredis 键(key) 命令工具方法 ****************************/
	/**
	 * 删除一个key
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/del",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse del(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【删除一个key】(BootRedisKeyController-del)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.del(key);
			logger.info("===step2:【删除一个key】(BootRedisKeyController-del)-删除 key-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【删除一个key】(BootRedisKeyController-del)-删除key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【删除一个key】(BootRedisKeyController-del)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 删除多个key
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/del",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse delArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【删除多个key】(BootRedisKeyController-delArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.del(keys);
			logger.info("===step2:【删除多个key】(BootRedisKeyController-delArray)-删除多个key-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【删除多个key】(BootRedisKeyController-delArray)-删除多个 key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
        	if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
        		return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
        	}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【删除多个key】(BootRedisKeyController-delArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 序列化给定key，并返回被序列化的值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/dump",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse dump(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【序列化给定key】(BootRedisKeyController-dump)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		byte[] result = null;
		try {
			result = redisService.dump(key);
			logger.info("===step2:【序列化给定key】(BootRedisKeyController-dump)-序列化给定key, result:{}", result == null ? null : result.length);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【序列化给定key】(BootRedisKeyController-dump)-序列化给定key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【序列化给定key】(BootRedisKeyController-dump)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 检查key是否存在
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/exists",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse exists(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【检查key是否存在】(BootRedisKeyController-exists)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		boolean flag = false;
		try {
			flag = redisService.exists(key);
			logger.info("===step2:【检查key是否存在】(BootRedisKeyController-exists)-检查key, flag:{}", flag);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【检查key是否存在】(BootRedisKeyController-exists)-检查key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【检查key是否存在】(BootRedisKeyController-exists)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 检查多个key是否存在
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/exists",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse existsArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【检查多个key是否存在】(BootRedisKeyController-existsArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		long len = 0l;
		try {
			len = redisService.exists(keys);
			logger.info("===step2:【检查多个key是否存在】(BootRedisKeyController-existsArray)-检查多个key-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【检查多个key是否存在】(BootRedisKeyController-existsArray)-检查多个key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
        	if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
        		return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
        	}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【检查多个key是否存在】(BootRedisKeyController-existsArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置单个key的过期时间，以秒计
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/expire",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse expire(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置单个key的过期时间】(BootRedisKeyController-expire)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer seconds = req.getSeconds();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(seconds == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "seconds为空");
		}

		long len = 0l;
		try {
			len = redisService.expire(key, seconds);
			logger.info("===step2:【设置单个key的过期时间】(BootRedisKeyController-expire)-设置过期时间-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【设置单个key的过期时间】(BootRedisKeyController-expire)-设置过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【设置单个key的过期时间】(BootRedisKeyController-expire)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置单个key的过期时间，以秒计。时间参数是UNIX时间戳(unix timestamp)
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/expireAt",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse expireAt(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置单个key的过期时间】(BootRedisKeyController-expireAt)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long unixTime = req.getUnixTime();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(unixTime == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "unixTime为空");
		}

		long len = 0l;
		try {
			len = redisService.expireAt(key, unixTime);
			logger.info("===step2:【设置单个key的过期时间】(BootRedisKeyController-expireAt)-设置过期时间-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【设置单个key的过期时间】(BootRedisKeyController-expireAt)-设置过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【设置单个key的过期时间】(BootRedisKeyController-expireAt)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 查找所有符合给定模式的key
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/keys",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse keys(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【查找所有符合给定模式的key】(BootRedisKeyController-keys)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String pattern = req.getPattern();
		if(StringUtils.isBlank(pattern)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "pattern为空");
		}

		Set<String> result = null;
		try {
			result = redisService.keys(pattern);
			logger.info("===step2:【查找所有符合给定模式的key】(BootRedisKeyController-keys)-查找所有符合给定模式的key-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【查找所有符合给定模式的key】(BootRedisKeyController-keys)-查找所有符合给定模式的key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【查找所有符合给定模式的key】(BootRedisKeyController-keys))-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将key原子性地从当前实例传送到目标实例的指定数据库上
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/migrate",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse migrate(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将key原子性地从当前实例传送到目标实例的指定数据库上】(BootRedisKeyController-migrate)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String host = req.getHost();
		Integer port = req.getPort();
		String key = req.getKey();
		Integer destinationDb = req.getDestinationDb();
		Integer timeout = req.getTimeout();
		if(StringUtils.isBlank(host)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "host为空");
		} else if(port == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "port为空");
		} else if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(destinationDb == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "destinationDb为空");
		} else if(timeout == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "timeout为空");
		}

		String result = null;
		try {
			result = redisService.migrate(host, port, key, destinationDb, timeout);
			logger.info("===step2:【将key原子性地从当前实例传送到目标实例的指定数据库上】(BootRedisKeyController-migrate)-从当前实例传送到目标实例的指定数据库上-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【将key原子性地从当前实例传送到目标实例的指定数据库上】(BootRedisKeyController-migrate)-从当前实例传送到目标实例的指定数据库上-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将key原子性地从当前实例传送到目标实例的指定数据库上】(BootRedisKeyController-migrate)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将当前数据库的key移动到给定的数据库db当中
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/move",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse move(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将当前数据库的key移动到给定的数据库db当中】(BootRedisKeyController-move)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer dbIndex = req.getDbIndex();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(dbIndex == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "dbIndex为空");
		}

		long len = 0l;
		try {
			len = redisService.move(key, dbIndex);
			logger.info("===step2:【将当前数据库的key移动到给定的数据库db当中】(BootRedisKeyController-move)-当前数据库的key移动到给定的数据库db当中-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【将当前数据库的key移动到给定的数据库db当中】(BootRedisKeyController-move)-当前数据库的key移动到给定的数据库db当中-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【将当前数据库的key移动到给定的数据库db当中】(BootRedisKeyController-move)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 移除key的生存时间
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/persist",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse persist(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【移除key的生存时间】(BootRedisKeyController-persist)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.persist(key);
			logger.info("===step2:【移除key的生存时间】(BootRedisKeyController-persist)-移除key的生存时间-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【移除key的生存时间】(BootRedisKeyController-persist)-移除key的生存时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【移除key的生存时间】(BootRedisKeyController-persist)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置 key的过期时间(毫秒)
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/pexpire",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse pexpire(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置 key的过期时间(毫秒)】(BootRedisKeyController-pexpire)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long milliseconds = req.getMilliseconds();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(milliseconds == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "milliseconds为空");
		}

		long len = 0l;
		try {
			len = redisService.pexpire(key, milliseconds);
			logger.info("===step2:【设置 key的过期时间(毫秒)】(BootRedisKeyController-pexpire)-设置key的毫秒过期时间-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【设置 key的过期时间(毫秒)】(BootRedisKeyController-pexpire)-设置key的毫秒过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【用于设置 key的过期时间(毫秒)】(BootRedisKeyController-pexpire)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置 key的过期时间(毫秒)。时间参数是UNIX时间戳(unix timestamp)。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/pexpireAt",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse pexpireAt(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置 key的过期时间(毫秒)】(BootRedisKeyController-pexpireAt)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long millisecondsTimestamp = req.getMillisecondsTimestamp();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(millisecondsTimestamp == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "millisecondsTimestamp为空");
		}

		long len = 0l;
		try {
			len = redisService.pexpireAt(key, millisecondsTimestamp);
			logger.info("===step2:【设置 key的过期时间(毫秒)】(BootRedisKeyController-pexpireAt)-设置key的毫秒过期时间-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【设置 key的过期时间(毫秒)】(BootRedisKeyController-pexpireAt)-设置key的毫秒过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【用于设置 key的过期时间(毫秒)】(BootRedisKeyController-pexpireAt)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}


	/**
	 * 毫秒为单位返回 key的剩余生存时间
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/pttl",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse pttl(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【毫秒为单位返回key的剩余生存时间】(BootRedisKeyController-pttl)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.pttl(key);
			logger.info("===step2:【毫秒为单位返回key的剩余生存时间】(BootRedisKeyController-pttl)-key的剩余毫秒生存时间-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【毫秒为单位返回key的剩余生存时间】(BootRedisKeyController-pttl)-key的剩余毫秒生存时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【毫秒为单位返回key的剩余生存时间】(BootRedisKeyController-pttl)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 当前数据库中随机返回一个key
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/randomKey",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse randomKey(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【当前数据库中随机返回一个key】(BootRedisKeyController-randomKey)-传入参数为空");

		String result = null;
		try {
			result = redisService.randomKey();
			logger.info("===step2:【当前数据库中随机返回一个key】(BootRedisKeyController-randomKey)-随机返回一个 key-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【当前数据库中随机返回一个key】(BootRedisKeyController-randomKey)-随机返回一个 key-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【当前数据库中随机返回一个key】(BootRedisKeyController-randomKey)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 修改key的名称
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/rename",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse rename(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【修改key的名称】(BootRedisKeyController-rename)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String oldkey = req.getOldkey();
		String newkey = req.getNewkey();
		if(StringUtils.isBlank(oldkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "oldkey为空");
		} else if(StringUtils.isBlank(newkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "newkey为空");
		}

		String result = null;
		try {
			result = redisService.rename(oldkey, newkey);
			logger.info("===step2:【修改key的名称】(BootRedisKeyController-rename)-修改key的名称-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【修改key的名称】(BootRedisKeyController-rename)-修改key的名称-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【修改key的名称】(BootRedisKeyController-rename)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 新key不存在时修改key的名称
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/renamenx",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse renamenx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:新key不存在时修改key的名称】(BootRedisKeyController-renamenx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String oldkey = req.getOldkey();
		String newkey = req.getNewkey();
		if(StringUtils.isBlank(oldkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "oldkey为空");
		} else if(StringUtils.isBlank(newkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "newkey为空");
		}

		long len = 0l;
		try {
			len = redisService.renamenx(oldkey, newkey);
			logger.info("===step2:【新key不存在时修改key的名称】(BootRedisKeyController-renamenx)-新key不存在时修改key的名称-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【新key不存在时修改key的名称】(BootRedisKeyController-renamenx)-新key不存在时修改key的名称-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【新key不存在时修改key的名称】(BootRedisKeyController-renamenx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 反序列化给定的序列化值，并将它和给定的 key 关联
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/restore",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse restore(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【反序列化给定的序列化值】(BootRedisKeyController-restore)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer ttl = req.getTtl();
		byte[] serializedValue = req.getSerializedValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(ttl == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "ttl为空");
		} else if(serializedValue == null || serializedValue.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "ttl为空");
		}

		String result = null;
		try {
			result = redisService.restore(key, ttl, serializedValue);
			logger.info("===step2:【反序列化给定的序列化值】(BootRedisKeyController-restore)-反序列化给定的序列化值-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【反序列化给定的序列化值】(BootRedisKeyController-restore)-反序列化给定的序列化值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【反序列化给定的序列化值】(BootRedisKeyController-restore)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回或保存给定列表、集合、有序集合key中经过排序的元素
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sort",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sort(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回或保存给定列表、集合、有序集合key中经过排序的元素】(BootRedisKeyController-sort)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String sortingParameters = req.getSortingParameters();
		String dstkey = req.getDstkey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isNotBlank(sortingParameters)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "sortingParameters为空");
		} else if(StringUtils.isNotBlank(dstkey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "dstkey为空");
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
			logger.info("===step2:【返回或保存给定列表、集合、有序集合key中经过排序的元素】(BootRedisKeyController-sort)-返回经过排序的元素-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【【返回或保存给定列表、集合、有序集合key中经过排序的元素】(BootRedisKeyController-sort)-经过排序的元素-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【返回或保存给定列表、集合、有序集合key中经过排序的元素】(BootRedisKeyController-sort)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 以秒为单位返回key的剩余过期时间
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/ttl",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse ttl(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【以秒为单位返回key的剩余过期时间】(BootRedisKeyController-ttl)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.ttl(key);
			logger.info("===step2:【以秒为单位返回key的剩余过期时间】(BootRedisKeyController-ttl)-key的剩余秒生存时间-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【以秒为单位返回key的剩余过期时间】(BootRedisKeyController-ttl)-key的剩余秒生存时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【以秒为单位返回key的剩余过期时间】(BootRedisKeyController-ttl)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key储存的值的类型
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/type",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse type(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key储存的值的类型】(BootRedisKeyController-type)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		String result = null;
		try {
			result = redisService.type(key);
			logger.info("===step2:【key储存的值的类型】(BootRedisKeyController-type)-key储存的值的类型-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【key储存的值的类型】(BootRedisKeyController-type)-key储存的值的类型-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【key储存的值的类型】(BootRedisKeyController-type)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis 键(key) 命令工具方法 ****************************/

}