package com.cloud.provider.redis.controller;

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
import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.common.exception.RedisException;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.constants.RedisConstants;
import com.cloud.provider.redis.rest.request.RedisRequest;

import redis.clients.jedis.BitOP;

/**
 * Redis String（字符串） RedisStringController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/redis/string")
public class RedisStringController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO String(字符串) 命令工具方法
	/************************** jredis String(字符串) 命令工具方法 ****************************/
	/**
	 * 追加key值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/append",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse append(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【追加key值】(RedisStringController-append)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.append(key, value);
			logger.info("===step2:【追加key值】(RedisStringController-append)-追加key值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【追加key值】(RedisStringController-append)-追加key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【【追加key值】(RedisStringController-append)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 计算给定字符串中，被设置为1的比特位的数量。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/bitcount",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse bitcount(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【计算给定字符串中设置为1的比特位的数量】(RedisStringController-bitcount)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.bitcount(key);
			logger.info("===step2:【计算给定字符串中设置为1的比特位的数量】(RedisStringController-bitcount)-给定字符串中设置为1的比特位的数量-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【计算给定字符串中设置为1的比特位的数量】(RedisStringController-bitcount)-给定字符串中设置为1的比特位的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【计算给定字符串中设置为1的比特位的数量】(RedisStringController-bitcount)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 计算给定字符串中，被设置为1的比特位的数量。
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/start/end/bitcount",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse bitcountStartEnd(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【计算给定字符串中设置为1的比特位的数量】(RedisStringController-bitcountStartEnd)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

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

		long len = 0l;
		try {
			len = redisService.bitcount(key, start, end);
			logger.info("===step2:【计算给定字符串中设置为1的比特位的数量】(RedisStringController-bitcountStartEnd)-给定字符串中设置为1的比特位的数量-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【计算给定字符串中设置为1的比特位的数量】(RedisStringController-bitcountStartEnd)-给定字符串中设置为1的比特位的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【计算给定字符串中设置为1的比特位的数量】(RedisStringController-bitcountStartEnd)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 对一个保存二进制位的字符串key进行位元操作
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/bitop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse bitop(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【对一个保存二进制位的字符串key进行位元操作】(RedisStringController-bitop)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String operation = req.getOperation();
		String dstKey = req.getDstkey();
		String srcKey = req.getSrckey();
		if(StringUtils.isBlank(operation)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "operation为空");
		} else if(StringUtils.isBlank(dstKey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dstKey为空");
		} else if(StringUtils.isBlank(srcKey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "srcKey为空");
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
			logger.info("===step2:【对一个保存二进制位的字符串key进行位元操作】(RedisStringController-bitop)-一个保存二进制位的字符串key进行位元操-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【对一个保存二进制位的字符串key进行位元操作】(RedisStringController-bitop)-一个保存二进制位的字符串key进行位元操-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【对一个保存二进制位的字符串key进行位元操作】(RedisStringController-bitop)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 对多个保存二进制位的字符串key进行位元操作
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/bitop",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse bitopArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【对多个保存二进制位的字符串key进行位元操作】(RedisStringController-bitopArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String operation = req.getOperation();
		String dstKey = req.getDstkey();
		String[] srcKeys = req.getSrckeys();
		if(StringUtils.isBlank(operation)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "operation为空");
		} else if(StringUtils.isBlank(dstKey)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "dstKey为空");
		} else if(srcKeys == null || srcKeys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "srcKeys为空");
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
			logger.info("===step2:【对多个保存二进制位的字符串key进行位元操作】(RedisStringController-bitopArray)-多个保存二进制位的字符串key进行位元操-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【对多个保存二进制位的字符串key进行位元操作】(RedisStringController-bitopArray)-多个保存二进制位的字符串key进行位元操-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【对多个保存二进制位的字符串key进行位元操作】(RedisStringController-bitopArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将一个字符串看作是一个由二进制位组成的数组
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/bitfield",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse bitfield(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将一个字符串看作是一个由二进制位组成的数组】(RedisStringController-bitfield)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String argument = req.getArgument();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(argument)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "argument为空");
		}

		List<Long> result = null;
		try {
			result = redisService.bitfield(key, argument);
			logger.info("===step2:【将一个字符串看作是一个由二进制位组成的数组】(RedisStringController-bitfield)-一个字符串看作是一个由二进制位组成的数组-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【将一个字符串看作是一个由二进制位组成的数组】(RedisStringController-bitfield)-一个字符串看作是一个由二进制位组成的数组-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将一个字符串看作是一个由二进制位组成的数组】(RedisStringController-bitfield)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 将一个字符串看作是一个由二进制位组成的数组
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/bitfield",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse bitfieldArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【将一个字符串看作是一个由二进制位组成的数组】(RedisStringController-bitfieldArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String[] arguments = req.getArguments();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(arguments == null || arguments.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "arguments为空");
		}

		List<Long> result = null;
		try {
			result = redisService.bitfield(key, arguments);
			logger.info("===step2:【将一个字符串看作是一个由二进制位组成的数组】(RedisStringController-bitfieldArray)-一个字符串看作是一个由二进制位组成的数组-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【将一个字符串看作是一个由二进制位组成的数组】(RedisStringController-bitfieldArray)-一个字符串看作是一个由二进制位组成的数组-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【将一个字符串看作是一个由二进制位组成的数组】(RedisStringController-bitfieldArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key中储存的数字值减一
	 * @param key
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/decr",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse decr(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key中储存的数字值减一】(RedisStringController-decr)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.decr(key);
			logger.info("===step2:【key中储存的数字值减一】(RedisStringController-decr)-储存数字值减一-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【key中储存的数字值减一】(RedisStringController-decr)-储存数字值减一-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key中储存的数字值减一】(RedisStringController-decr)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key所储存的值减去减量值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/decrBy",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse decrBy(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key所储存的值减去减量值】(RedisStringController-decrBy)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.decrBy(key, new Integer(value));
			logger.info("===step2:【key所储存的值减去减量值】(RedisStringController-decrBy)-储存的值减去减量值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【key所储存的值减去减量值】(RedisStringController-decrBy)-储存的值减去减量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key所储存的值减去减量值】(RedisStringController-decrBy)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 获取 key值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/get",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse get(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取 key值】(RedisStringController-get)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key空");
		}

		String result = null;
		try {
			result = redisService.get(key);
			logger.info("===step2:【获取 key值】(RedisStringController-get)-获取 key值-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【获取 key值】(RedisStringController-get)-获取 key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【获取 key值】(RedisStringController-get)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 对key所储存的字符串值，获取指定偏移量上的位(bit)
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/getbit",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse getbit(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【对key所储存的字符串值获取指定偏移量上的位】(RedisStringController-getbit)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long offset = req.getOffset();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key空");
		} else if(offset == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "offset空");
		}

		boolean flag = false;
		try {
			flag = redisService.getbit(key, offset);
			logger.info("===step2:【对key所储存的字符串值获取指定偏移量上的位】(RedisStringController-getbit)-获取指定偏移量上的位-返回信息, flag:{}", flag);
		} catch (RedisException e) {
			logger.error("===step2.1:【对key所储存的字符串值获取指定偏移量上的位】(RedisStringController-getbit)-获取指定偏移量上的位-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【对key所储存的字符串值获取指定偏移量上的位】(RedisStringController-getbit)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}


	/**
	 * 获取存储在指定 key中字符串的子字符串
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/getrange",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse getrange(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取存储在指定 key中字符串的子字符串】(RedisStringController-getrange)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long startOffset = req.getStartOffset();
		Long endOffset = req.getEndOffset();

		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(startOffset == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "startOffset为空");
		} else if(endOffset == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "endOffset为空");
		}

		String result = null;
		try {
			result = redisService.getrange(key, startOffset, endOffset);
			logger.info("===step2:【获取存储在指定 key中字符串的子字符串】(RedisStringController-getrange)-获取存储在指定 key中字符串的子字符串-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【获取存储在指定 key中字符串的子字符串】(RedisStringController-getrange)-获取存储在指定 key中字符串的子字符串-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【获取存储在指定 key中字符串的子字符串】(RedisStringController-getrange)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 指定key值返回key旧值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/getSet",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse getSet(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【指定key值返回key旧值】(RedisStringController-getSet)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		String result = null;
		try {
			result = redisService.getSet(key, value);
			logger.info("===step2:【指定key值返回key旧值】(RedisStringController-getSet)-获取 key值返回key旧值-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【指定key值返回key旧值】(RedisStringController-getSet)-获取 key值返回key旧值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【指定key值返回key旧值】(RedisStringController-getSet)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key中储存的数字值增一
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/incr",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse incr(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:key中储存的数字值增一】(RedisStringController-incr)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.incr(key);
			logger.info("===step2:【key中储存的数字值增一】(RedisStringController-incr)-储存数字值增一-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【key中储存的数字值增一】(RedisStringController-incr)-储存数字值增一-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key中储存的数字值增一】(RedisStringController-incr)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key所储存的值减去指定的增量值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/incrBy",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse incrBy(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key所储存的值减去指定的增量值】(RedisStringController-incrBy)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(value == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.incrBy(key, new Integer(value));
			logger.info("===step2:【key所储存的值减去指定的增量值】(RedisStringController-incrBy))-储存值减去增量值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【key所储存的值减去指定的增量值】(RedisStringController-incrBy)-储存值减去增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key所储存的值减去指定的增量值】(RedisStringController-incrBy)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * key中所储存的值加上浮点数增量值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/incrByFloat",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse incrByFloat(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【key中所储存的值加上浮点数增量值】(RedisStringController-incrByFloat)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(value == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		double len = 0d;
		try {
			len = redisService.incrByFloat(key, new Double(value));
			logger.info("===step2:【key中所储存的值加上浮点数增量值】(RedisStringController-incrByFloat)-key所储存的值减去指定的增量值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【key中所储存的值加上浮点数增量值】(RedisStringController-incrByFloat)-key所储存的值减去指定的增量值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【key中所储存的值加上浮点数增量值】(RedisStringController-incrByFloat)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回一个key值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/mget",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse mget(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回一个key值】(RedisStringController-mget)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "合约地址为空");
		}

		List<String> result = null;
		try {
			result = redisService.mget(key);
			logger.info("===step2:【返回一个key值】(RedisStringController-mget)-返回key值-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回一个key值】(RedisStringController-mget)-返回 key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回一个key值】(RedisStringController-mget)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 返回多个key的值
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/array/mget",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse mgetArray(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【返回多个key的值】(RedisStringController-mgetArray)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keys = req.getKeys();
		if(keys == null || keys.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keys为空");
		}

		List<String> result = null;
		try {
			result = redisService.mget(keys);
			logger.info("===step2:【返回多个key的值】(RedisStringController-mgetArray)-获取 key值-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【返回多个key的值】(RedisStringController-mgetArray)-获取 key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【返回多个key的值】(RedisStringController-mgetArray)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 同时设置多个 key-value
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/mset",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse mset(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【同时设置多个 key-value】(RedisStringController-mset)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keysvalues = req.getKeysvalues();
		if(keysvalues == null || keysvalues.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keysvalues为空");
		}

		String result = null;
		try {
			result = redisService.mset(keysvalues);
			logger.info("===step2:【同时设置多个 key-value】(RedisStringController-mset)-同时设置多个 key-value-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【同时设置多个 key-value】(RedisStringController-mset)-同时设置多个 key-value-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【同时设置多个 key-value】(RedisStringController-mset)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 同时设置多个 key-value
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/msetnx",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse msetnx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【同时设置多个 key-value】(RedisStringController-msetnx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String[] keysvalues = req.getKeysvalues();
		if(keysvalues == null || keysvalues.length == 0) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "keysvalues为空");
		}

		String result = null;
		try {
			result = redisService.mset(keysvalues);
			logger.info("===step2:【同时设置多个 key-value】(RedisStringController-msetnx)-获取 key值-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【同时设置多个 key-value】(RedisStringController-msetnx)-获取 key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【同时设置多个 key-value】(RedisStringController-msetnx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 毫秒为单位设置key的生存时间
	 * @param key
	 * @param seconds
	 * @param value
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/psetex",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse psetex(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【毫秒为单位设置key的生存时间】(RedisStringController-psetex)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long milliseconds = req.getMilliseconds();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(milliseconds == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "seconds为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		String result = null;
		try {
			result = redisService.psetex(key, milliseconds, value);
			logger.info("===step2:【毫秒为单位设置key的生存时间】(RedisStringController-psetex)-毫秒为单位设置生存时间-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【毫秒为单位设置key的生存时间】(RedisStringController-psetex)-毫秒为单位设置的生存时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【毫秒为单位设置key的生存时间】(RedisStringController-psetex)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置key值
	 * 如果 key 已经存储其他值， SET 就覆写旧值，且无视类型
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/set",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse set(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置key值】(RedisStringController-set)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		String result = null;
		try {
			result = redisService.set(key, value);
			logger.info("===step2:【设置key值】(RedisStringController-set)-设置key值-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【设置key值】(RedisStringController-set)-设置key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【设置key值】(RedisStringController-set)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 对key所储存的字符串值，设置或清除指定偏移量上的位(bit)
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/setbit",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse setbit(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【对key所储存的字符串值设置或清除指定偏移量上的位(bit)】(RedisStringController-setbit)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long offset = req.getOffset();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(offset == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "offset为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		boolean flag = false;
		try {
			flag = redisService.setbit(key, offset, value);
			logger.info("===step2:【对key所储存的字符串值设置或清除指定偏移量上的位(bit)】(RedisStringController-setbit)-设置或清除指定偏移量上的位-返回信息, flag:{}", flag);
		} catch (RedisException e) {
			logger.error("===step2.1:【【对key所储存的字符串值设置或清除指定偏移量上的位(bit)】(RedisStringController-setbit)-设置或清除指定偏移量上的位-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, flag);
		logger.info("===step3:【对key所储存的字符串值设置或清除指定偏移量上的位(bit)】(RedisStringController-setbit)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 设置key值和过期时间
	 * 如果 key 已经存储其他值， SET 就覆写旧值，且无视类型
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/setex",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse setex(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置key值和过期时间】(RedisStringController-setex)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Integer seconds = req.getSeconds();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(seconds == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "seconds为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		String result = null;
		try {
			result = redisService.setex(key, seconds, value);
			logger.info("===step2:【设置key值和过期时间】(RedisStringController-setex)-设置key值和过期时间-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【设置key值和过期时间】(RedisStringController-setex)-设置key值和过期时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【设置key值和过期时间】(RedisStringController-setex)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 在指定的 key不存在时，为 key设置指定的值;若给定的 key 已经存在，则 SETNX 不做任何动作
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/setnx",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse setnx(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【设置key值】(RedisStringController-setnx)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.setnx(key, value);
			logger.info("===step2:【设置key值】(RedisStringController-setnx)-设置key值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【设置key值】(RedisStringController-setnx)-设置key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【设置key值】(RedisStringController-setnx)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 指定的字符串覆盖给定key所储存的字符串值
	 * @param key
	 * @param offset
	 * @param value
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/setrange",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse setrange(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【指定的字符串覆盖给定key所储存的字符串值】(RedisStringController-setrange)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		Long offset = req.getOffset();
		String value = req.getValue();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		} else if(offset == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "offset为空");
		} else if(StringUtils.isBlank(value)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "value为空");
		}

		long len = 0l;
		try {
			len = redisService.setrange(key, offset, value);
			logger.info("===step2:【指定的字符串覆盖给定key所储存的字符串值】(RedisStringController-setrange)-设置key值-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【指定的字符串覆盖给定key所储存的字符串值】(RedisStringController-setrange)-设置key值-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【指定的字符串覆盖给定key所储存的字符串值】(RedisStringController-setrange)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 获取key所储存的字符串值的长度
	 * @param key
	 * @param value
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/strlen",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse strlen(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【获取key所储存的字符串值的长度】(RedisStringController-strlen)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String key = req.getKey();
		if(StringUtils.isBlank(key)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "key为空");
		}

		long len = 0l;
		try {
			len = redisService.strlen(key);
			logger.info("===step2:【获取key所储存的字符串值的长度】(RedisStringController-strlen)-获取key所储存的字符串值的长度-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【获取key所储存的字符串值的长度】(RedisStringController-strlen)-获取key所储存的字符串值的长度-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【获取key所储存的字符串值的长度】(RedisStringController-strlen)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis String(字符串) 命令工具方法 ****************************/

}