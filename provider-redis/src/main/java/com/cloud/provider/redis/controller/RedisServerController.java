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

import com.cloud.common.constants.CommConstants;
import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.common.exception.RedisException;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.rest.request.RedisRequest;

import io.swagger.annotations.Api;

/**
 * Redis Server（服务器） RedisServerController
 * @author wei.yong
 * @date 2017-09-14
 */
@Api(tags = "服务器")
@RestController
@RequestMapping("/redis/server")
public class RedisServerController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Connection(连接) 命令工具方法
	/************************** jredis Connection(连接) 命令工具方法 ****************************/
	/**
	 * 执行一个AOF文件重写操作
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/bgrewriteaof",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse bgrewriteaof(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【执行一个AOF文件重写操作】(RedisServerController-bgrewriteaof)-传入参数为空");

		String result = null;
		try {
			result = redisService.bgrewriteaof();
			logger.info("===step2:【执行一个AOF文件重写操作】(RedisServerController-bgrewriteaof))-执行重写-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【执行一个AOF文件重写操作】(RedisServerController-bgrewriteaof)-执行重写-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【执行一个AOF文件重写操作】(RedisServerController-bgrewriteaof)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 保存当前数据库的数据到磁盘
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/bgsave",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse bgsave(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【保存当前数据库的数据到磁盘】(RedisServerController-bgsave)-传入参数为空");

		String result = null;
		try {
			result = redisService.bgsave();
			logger.info("===step2:【保存当前数据库的数据到磁盘】(RedisServerController-bgsave)-保存当前数据库的数据到磁盘-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【保存当前数据库的数据到磁盘】(RedisServerController-bgsave)-保存当前数据库的数据到磁盘-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【保存当前数据库的数据到磁盘】(RedisServerController-bgsave)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 当前数据库的key的数量
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/dbSize",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse dbSize(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【当前数据库的key的数量】(RedisServerController-dbSize)-传入参数为空");

		long len = 0l;
		try {
			len = redisService.dbSize();
			logger.info("===step2:【当前数据库的key的数量】(RedisServerController-dbSize)-当前key的数量-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【当前数据库的key的数量】(RedisServerController-dbSize)-当前key的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, len);
		logger.info("===step3:【当前数据库的key的数量】(RedisServerController-dbSize)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 关于Redis服务器的各种信息和统计数
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/info",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse info(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【关于Redis服务器的各种信息和统计数】(RedisServerController-info)-传入参数为空");

		String result = null;
		try {
			result = redisService.discard();
			logger.info("===step2:【关于Redis服务器的各种信息和统计数】(RedisServerController-info)-各种信息和统计数-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【关于Redis服务器的各种信息和统计数】(RedisServerController-info)-各种信息和统计数-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【关于Redis服务器的各种信息和统计数】(RedisServerController-info)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 执行一个同步保存
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/save",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse save(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【执行一个同步保存】(RedisServerController-save)-传入参数为空");

		String result = null;
		try {
			result = redisService.save();
			logger.info("===step2:【执行一个同步保存】(RedisServerController-save)-同步保存-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【执行一个同步保存接】(RedisServerController-save)-同步保存-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【执行一个同步保存】(RedisServerController-save)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 停止所有客户端
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/shutdown",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse shutdown(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【停止所有客户端】(RedisServerController-shutdown)-传入参数为空");

		String result = null;
		try {
			result = redisService.shutdown();
			logger.info("===step2:【停止所有客户端】(RedisServerController-shutdown)-停止所有客户端-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【停止所有客户端】(RedisServerController-shutdown)-停止所有客户端-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【停止所有客户端】(RedisServerController-shutdown)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}


	/**
	 * 动态地修改复制(replication)功能
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/slaveof",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse slaveof(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【动态地修改复制功能】(RedisServerController-slaveof)-传入参数为空");

		String host = req.getHost();
		Integer port = req.getPort();
		if(StringUtils.isBlank(host)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "host为空");
		} else if(port == null) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "port为空");
		}

		String result = null;
		try {
			result = redisService.slaveof(host, port);
			logger.info("===step2:【动态地修改复制功能】(RedisServerController-slaveof)-修改复制功能-返回信息, result:{}", result);
		} catch (RedisException e) {
			logger.error("===step2.1:【动态地修改复制功能】(RedisServerController-slaveof)-修改复制功能-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【动态地修改复制功能】(RedisServerController-slaveof)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 复制功能内部命令
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/sync",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sync(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【复制功能内部命令】(RedisServerController-sync)-传入参数为空");

		try {
			redisService.sync();
			logger.info("===step2:【复制功能内部命令】(RedisServerController-sync)-复制内部命令-返回信息为空");
		} catch (RedisException e) {
			logger.error("===step2.1:【复制功能内部命令】(RedisServerController-sync)-复制内部命令-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		logger.info("===step3:【复制功能内部命令】(RedisServerController-sync)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 当前服务器时间
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/time",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse time(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【当前服务器时间】(RedisServerController-time)-传入参数为空");

		List<String> result = null;
		try {
			result = redisService.time();
			logger.info("===step2:【当前服务器时间】(RedisServerController-time)-服务器时间-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (RedisException e) {
			logger.error("===step2.1:【当前服务器时间】(RedisServerController-time)-服务器时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, result);
		logger.info("===step3:【当前服务器时间】(RedisServerController-time)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/************************** jredis Connection(连接) 命令工具方法 ****************************/

}