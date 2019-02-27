package com.ochain.provider.redis.controllers;

import java.util.List;

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

import redis.clients.jedis.BitOP;

/**
 * Redis String（字符串） BootRedisStringController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/string")
public class BootRedisStringController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO String(字符串) 命令工具方法
	/************************** jredis String(字符串) 命令工具方法 ****************************/
	/**
	 * 追加key值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/append",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse append(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【追加key值】(BootRedisStringController-append)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		long len = 0l;
		try {
			len = redisService.append(key, value);
			logger.info("===step2:【追加key值】(BootRedisStringController-append)-追加key值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【追加key值】(BootRedisStringController-append)-追加key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【【追加key值】(BootRedisStringController-append)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 计算给定字符串中，被设置为1的比特位的数量。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/bitcount",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse bitcount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【计算给定字符串中设置为1的比特位的数量】(BootRedisStringController-bitcount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.bitcount(key);
			logger.info("===step2:【计算给定字符串中设置为1的比特位的数量】(BootRedisStringController-bitcount)-给定字符串中设置为1的比特位的数量-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【计算给定字符串中设置为1的比特位的数量】(BootRedisStringController-bitcount)-给定字符串中设置为1的比特位的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【计算给定字符串中设置为1的比特位的数量】(BootRedisStringController-bitcount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 计算给定字符串中，被设置为1的比特位的数量。
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/start/end/bitcount",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse bitcountStartEnd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【计算给定字符串中设置为1的比特位的数量】(BootRedisStringController-bitcountStartEnd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long start = req.getStart();
		Long end = req.getEnd();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(start == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "start为空");
		} else if(end == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "end为空");
		}

		long len = 0l;
		try {
			len = redisService.bitcount(key, start, end);
			logger.info("===step2:【计算给定字符串中设置为1的比特位的数量】(BootRedisStringController-bitcountStartEnd)-给定字符串中设置为1的比特位的数量-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【计算给定字符串中设置为1的比特位的数量】(BootRedisStringController-bitcountStartEnd)-给定字符串中设置为1的比特位的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【计算给定字符串中设置为1的比特位的数量】(BootRedisStringController-bitcountStartEnd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 对一个保存二进制位的字符串key进行位元操作
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/bitop",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse bitop(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【对一个保存二进制位的字符串key进行位元操作】(BootRedisStringController-bitop)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String operation = req.getOperation();
		String dstKey = req.getDstkey();
		String srcKey = req.getSrckey();
		if(StringUtils.isBlank(operation)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "operation为空");
		} else if(StringUtils.isBlank(dstKey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "dstKey为空");
		} else if(StringUtils.isBlank(srcKey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "srcKey为空");
		}

		long len = 0l;
		try {
			if(StringUtils.equalsIgnoreCase(operation, "and")) {
				len = redisService.bitop(BitOP.AND, dstKey, srcKey);
			} else if(StringUtils.equalsIgnoreCase(operation, "or")) {
				len = redisService.bitop(BitOP.OR, dstKey, srcKey);
			} else if(StringUtils.equalsIgnoreCase(operation, "xor")) {
				len = redisService.bitop(BitOP.XOR, dstKey, srcKey);
			} else if(StringUtils.equalsIgnoreCase(operation, "not")) {
				len = redisService.bitop(BitOP.NOT, dstKey, srcKey);
			}
			logger.info("===step2:【对一个保存二进制位的字符串key进行位元操作】(BootRedisStringController-bitop)-一个保存二进制位的字符串key进行位元操-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【对一个保存二进制位的字符串key进行位元操作】(BootRedisStringController-bitop)-一个保存二进制位的字符串key进行位元操-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【对一个保存二进制位的字符串key进行位元操作】(BootRedisStringController-bitop)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 对多个保存二进制位的字符串key进行位元操作
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/bitop",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse bitopArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【对多个保存二进制位的字符串key进行位元操作】(BootRedisStringController-bitopArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String operation = req.getOperation();
		String dstKey = req.getDstkey();
		String[] srcKeys = req.getSrckeys();
		if(StringUtils.isBlank(operation)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "operation为空");
		} else if(StringUtils.isBlank(dstKey)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "dstKey为空");
		} else if(srcKeys == null || srcKeys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "srcKeys为空");
		}

		long len = 0l;
		try {
			if(StringUtils.equals(operation, "AND")) {
				len = redisService.bitop(BitOP.AND, dstKey, srcKeys);
			} else if(StringUtils.equals(operation, "OR")) {
				len = redisService.bitop(BitOP.OR, dstKey, srcKeys);
			} else if(StringUtils.equals(operation, "XOR")) {
				len = redisService.bitop(BitOP.XOR, dstKey, srcKeys);
			} else if(StringUtils.equals(operation, "NOT")) {
				len = redisService.bitop(BitOP.NOT, dstKey, srcKeys);
			}
			logger.info("===step2:【对多个保存二进制位的字符串key进行位元操作】(BootRedisStringController-bitopArray)-多个保存二进制位的字符串key进行位元操-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【对多个保存二进制位的字符串key进行位元操作】(BootRedisStringController-bitopArray)-多个保存二进制位的字符串key进行位元操-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【对多个保存二进制位的字符串key进行位元操作】(BootRedisStringController-bitopArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将一个字符串看作是一个由二进制位组成的数组
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/bitfield",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse bitfield(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将一个字符串看作是一个由二进制位组成的数组】(BootRedisStringController-bitfield)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String argument = req.getArgument();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(argument)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "argument为空");
		}

		List<Long> result = null;
		try {
			result = redisService.bitfield(key, argument);
			logger.info("===step2:【将一个字符串看作是一个由二进制位组成的数组】(BootRedisStringController-bitfield)-一个字符串看作是一个由二进制位组成的数组-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【将一个字符串看作是一个由二进制位组成的数组】(BootRedisStringController-bitfield)-一个字符串看作是一个由二进制位组成的数组-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将一个字符串看作是一个由二进制位组成的数组】(BootRedisStringController-bitfield)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将一个字符串看作是一个由二进制位组成的数组
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/bitfield",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse bitfieldArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将一个字符串看作是一个由二进制位组成的数组】(BootRedisStringController-bitfieldArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] arguments = req.getArguments();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(arguments == null || arguments.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "arguments为空");
		}

		List<Long> result = null;
		try {
			result = redisService.bitfield(key, arguments);
			logger.info("===step2:【将一个字符串看作是一个由二进制位组成的数组】(BootRedisStringController-bitfieldArray)-一个字符串看作是一个由二进制位组成的数组-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【将一个字符串看作是一个由二进制位组成的数组】(BootRedisStringController-bitfieldArray)-一个字符串看作是一个由二进制位组成的数组-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将一个字符串看作是一个由二进制位组成的数组】(BootRedisStringController-bitfieldArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key中储存的数字值减一
	 * @param key
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/decr",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse decr(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key中储存的数字值减一】(BootRedisStringController-decr)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.decr(key);
			logger.info("===step2:【key中储存的数字值减一】(BootRedisStringController-decr)-储存数字值减一-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【key中储存的数字值减一】(BootRedisStringController-decr)-储存数字值减一-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key中储存的数字值减一】(BootRedisStringController-decr)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key所储存的值减去减量值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/decrBy",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse decrBy(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key所储存的值减去减量值】(BootRedisStringController-decrBy)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		long len = 0l;
		try {
			len = redisService.decrBy(key, new Integer(value));
			logger.info("===step2:【key所储存的值减去减量值】(BootRedisStringController-decrBy)-储存的值减去减量值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【key所储存的值减去减量值】(BootRedisStringController-decrBy)-储存的值减去减量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key所储存的值减去减量值】(BootRedisStringController-decrBy)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 获取 key值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/get",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse get(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取 key值】(BootRedisStringController-get)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key空");
		}

		String result = null;
		try {
			result = redisService.get(key);
			logger.info("===step2:【获取 key值】(BootRedisStringController-get)-获取 key值-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【获取 key值】(BootRedisStringController-get)-获取 key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【获取 key值】(BootRedisStringController-get)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 对key所储存的字符串值，获取指定偏移量上的位(bit)
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/getbit",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse getbit(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【对key所储存的字符串值获取指定偏移量上的位】(BootRedisStringController-getbit)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long offset = req.getOffset();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key空");
		} else if(offset == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "offset空");
		}

		boolean flag = false;
		try {
			flag = redisService.getbit(key, offset);
			logger.info("===step2:【对key所储存的字符串值获取指定偏移量上的位】(BootRedisStringController-getbit)-获取指定偏移量上的位-返回信息, flag:{}", flag);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【对key所储存的字符串值获取指定偏移量上的位】(BootRedisStringController-getbit)-获取指定偏移量上的位-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【对key所储存的字符串值获取指定偏移量上的位】(BootRedisStringController-getbit)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}


	/**
	 * 获取存储在指定 key中字符串的子字符串
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/getrange",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse getrange(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取存储在指定 key中字符串的子字符串】(BootRedisStringController-getrange)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long startOffset = req.getStartOffset();
		Long endOffset = req.getEndOffset();

		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(startOffset == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "startOffset为空");
		} else if(endOffset == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "endOffset为空");
		}

		String result = null;
		try {
			result = redisService.getrange(key, startOffset, endOffset);
			logger.info("===step2:【获取存储在指定 key中字符串的子字符串】(BootRedisStringController-getrange)-获取存储在指定 key中字符串的子字符串-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【获取存储在指定 key中字符串的子字符串】(BootRedisStringController-getrange)-获取存储在指定 key中字符串的子字符串-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【获取存储在指定 key中字符串的子字符串】(BootRedisStringController-getrange)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 指定key值返回key旧值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/getSet",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse getSet(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【指定key值返回key旧值】(BootRedisStringController-getSet)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		String result = null;
		try {
			result = redisService.getSet(key, value);
			logger.info("===step2:【指定key值返回key旧值】(BootRedisStringController-getSet)-获取 key值返回key旧值-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【指定key值返回key旧值】(BootRedisStringController-getSet)-获取 key值返回key旧值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【指定key值返回key旧值】(BootRedisStringController-getSet)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key中储存的数字值增一
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/incr",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse incr(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:key中储存的数字值增一】(BootRedisStringController-incr)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.incr(key);
			logger.info("===step2:【key中储存的数字值增一】(BootRedisStringController-incr)-储存数字值增一-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【key中储存的数字值增一】(BootRedisStringController-incr)-储存数字值增一-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key中储存的数字值增一】(BootRedisStringController-incr)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key所储存的值减去指定的增量值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/incrBy",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse incrBy(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key所储存的值减去指定的增量值】(BootRedisStringController-incrBy)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(value == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		long len = 0l;
		try {
			len = redisService.incrBy(key, new Integer(value));
			logger.info("===step2:【key所储存的值减去指定的增量值】(BootRedisStringController-incrBy))-储存值减去增量值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【key所储存的值减去指定的增量值】(BootRedisStringController-incrBy)-储存值减去增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key所储存的值减去指定的增量值】(BootRedisStringController-incrBy)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key中所储存的值加上浮点数增量值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/incrByFloat",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse incrByFloat(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key中所储存的值加上浮点数增量值】(BootRedisStringController-incrByFloat)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(value == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		double len = 0d;
		try {
			len = redisService.incrByFloat(key, new Double(value));
			logger.info("===step2:【key中所储存的值加上浮点数增量值】(BootRedisStringController-incrByFloat)-key所储存的值减去指定的增量值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【key中所储存的值加上浮点数增量值】(BootRedisStringController-incrByFloat)-key所储存的值减去指定的增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key中所储存的值加上浮点数增量值】(BootRedisStringController-incrByFloat)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回一个key值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/mget",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse mget(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回一个key值】(BootRedisStringController-mget)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "合约地址为空");
		}

		List<String> result = null;
		try {
			result = redisService.mget(key);
			logger.info("===step2:【返回一个key值】(BootRedisStringController-mget)-返回key值-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回一个key值】(BootRedisStringController-mget)-返回 key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回一个key值】(BootRedisStringController-mget)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回多个key的值
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/array/mget",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse mgetArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回多个key的值】(BootRedisStringController-mgetArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keys为空");
		}

		List<String> result = null;
		try {
			result = redisService.mget(keys);
			logger.info("===step2:【返回多个key的值】(BootRedisStringController-mgetArray)-获取 key值-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【返回多个key的值】(BootRedisStringController-mgetArray)-获取 key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回多个key的值】(BootRedisStringController-mgetArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 同时设置多个 key-value
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/mset",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse mset(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【同时设置多个 key-value】(BootRedisStringController-mset)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keysvalues = req.getKeysvalues();
		if(keysvalues == null || keysvalues.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keysvalues为空");
		}

		String result = null;
		try {
			result = redisService.mset(keysvalues);
			logger.info("===step2:【同时设置多个 key-value】(BootRedisStringController-mset)-同时设置多个 key-value-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【同时设置多个 key-value】(BootRedisStringController-mset)-同时设置多个 key-value-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【同时设置多个 key-value】(BootRedisStringController-mset)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 同时设置多个 key-value
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/msetnx",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse msetnx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【同时设置多个 key-value】(BootRedisStringController-msetnx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keysvalues = req.getKeysvalues();
		if(keysvalues == null || keysvalues.length == 0) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "keysvalues为空");
		}

		String result = null;
		try {
			result = redisService.mset(keysvalues);
			logger.info("===step2:【同时设置多个 key-value】(BootRedisStringController-msetnx)-获取 key值-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【同时设置多个 key-value】(BootRedisStringController-msetnx)-获取 key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【同时设置多个 key-value】(BootRedisStringController-msetnx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 毫秒为单位设置key的生存时间
	 * @param key
	 * @param seconds
	 * @param value
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/psetex",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse psetex(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【毫秒为单位设置key的生存时间】(BootRedisStringController-psetex)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long milliseconds = req.getMilliseconds();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(milliseconds == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "seconds为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		String result = null;
		try {
			result = redisService.psetex(key, milliseconds, value);
			logger.info("===step2:【毫秒为单位设置key的生存时间】(BootRedisStringController-psetex)-毫秒为单位设置生存时间-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【毫秒为单位设置key的生存时间】(BootRedisStringController-psetex)-毫秒为单位设置的生存时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【毫秒为单位设置key的生存时间】(BootRedisStringController-psetex)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置key值
	 * 如果 key 已经存储其他值， SET 就覆写旧值，且无视类型
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/set",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse set(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置key值】(BootRedisStringController-set)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		String result = null;
		try {
			result = redisService.set(key, value);
			logger.info("===step2:【设置key值】(BootRedisStringController-set)-设置key值-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【设置key值】(BootRedisStringController-set)-设置key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【设置key值】(BootRedisStringController-set)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 对key所储存的字符串值，设置或清除指定偏移量上的位(bit)
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/setbit",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse setbit(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【对key所储存的字符串值设置或清除指定偏移量上的位(bit)】(BootRedisStringController-setbit)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long offset = req.getOffset();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(offset == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "offset为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		boolean flag = false;
		try {
			flag = redisService.setbit(key, offset, value);
			logger.info("===step2:【对key所储存的字符串值设置或清除指定偏移量上的位(bit)】(BootRedisStringController-setbit)-设置或清除指定偏移量上的位-返回信息, flag:{}", flag);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【【对key所储存的字符串值设置或清除指定偏移量上的位(bit)】(BootRedisStringController-setbit)-设置或清除指定偏移量上的位-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【对key所储存的字符串值设置或清除指定偏移量上的位(bit)】(BootRedisStringController-setbit)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置key值和过期时间
	 * 如果 key 已经存储其他值， SET 就覆写旧值，且无视类型
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/setex",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse setex(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置key值和过期时间】(BootRedisStringController-setex)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer seconds = req.getSeconds();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(seconds == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "seconds为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		String result = null;
		try {
			result = redisService.setex(key, seconds, value);
			logger.info("===step2:【设置key值和过期时间】(BootRedisStringController-setex)-设置key值和过期时间-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【设置key值和过期时间】(BootRedisStringController-setex)-设置key值和过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【设置key值和过期时间】(BootRedisStringController-setex)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 在指定的 key不存在时，为 key设置指定的值;若给定的 key 已经存在，则 SETNX 不做任何动作
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/setnx",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse setnx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置key值】(BootRedisStringController-setnx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		long len = 0l;
		try {
			len = redisService.setnx(key, value);
			logger.info("===step2:【设置key值】(BootRedisStringController-setnx)-设置key值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【设置key值】(BootRedisStringController-setnx)-设置key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【设置key值】(BootRedisStringController-setnx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 指定的字符串覆盖给定key所储存的字符串值
	 * @param key
	 * @param offset
	 * @param value
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/setrange",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse setrange(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【指定的字符串覆盖给定key所储存的字符串值】(BootRedisStringController-setrange)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long offset = req.getOffset();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		} else if(offset == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "offset为空");
		} else if(StringUtils.isBlank(value)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "value为空");
		}

		long len = 0l;
		try {
			len = redisService.setrange(key, offset, value);
			logger.info("===step2:【指定的字符串覆盖给定key所储存的字符串值】(BootRedisStringController-setrange)-设置key值-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【指定的字符串覆盖给定key所储存的字符串值】(BootRedisStringController-setrange)-设置key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【指定的字符串覆盖给定key所储存的字符串值】(BootRedisStringController-setrange)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 获取key所储存的字符串值的长度
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/strlen",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse strlen(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取key所储存的字符串值的长度】(BootRedisStringController-strlen)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "key为空");
		}

		long len = 0l;
		try {
			len = redisService.strlen(key);
			logger.info("===step2:【获取key所储存的字符串值的长度】(BootRedisStringController-strlen)-获取key所储存的字符串值的长度-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【获取key所储存的字符串值的长度】(BootRedisStringController-strlen)-获取key所储存的字符串值的长度-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【获取key所储存的字符串值的长度】(BootRedisStringController-strlen)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis String(字符串) 命令工具方法 ****************************/

}