package com.cloud.provider.redis.controllers;

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

import com.cloud.provider.redis.boot.BootRestMapResponse;
import com.cloud.provider.redis.rest.request.RedisRequest;
import com.ochain.common.constants.BootConstants;
import com.ochain.common.constants.RedisConstants;
import com.ochain.common.exception.BootServiceException;

/**
 * Redis Server（服务器） BootRedisServerController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/server")
public class BootRedisServerController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Connection(连接) 命令工具方法
	/************************** jredis Connection(连接) 命令工具方法 ****************************/
	/**
	 * 执行一个AOF文件重写操作
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/bgrewriteaof",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse bgrewriteaof(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【执行一个AOF文件重写操作】(BootRedisServerController-bgrewriteaof)-传入参数为空");

		String result = null;
		try {
			result = redisService.bgrewriteaof();
			logger.info("===step2:【执行一个AOF文件重写操作】(BootRedisServerController-bgrewriteaof))-执行重写-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【执行一个AOF文件重写操作】(BootRedisServerController-bgrewriteaof)-执行重写-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【执行一个AOF文件重写操作】(BootRedisServerController-bgrewriteaof)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 保存当前数据库的数据到磁盘
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/bgsave",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse bgsave(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【保存当前数据库的数据到磁盘】(BootRedisServerController-bgsave)-传入参数为空");

		String result = null;
		try {
			result = redisService.bgsave();
			logger.info("===step2:【保存当前数据库的数据到磁盘】(BootRedisServerController-bgsave)-保存当前数据库的数据到磁盘-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【保存当前数据库的数据到磁盘】(BootRedisServerController-bgsave)-保存当前数据库的数据到磁盘-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【保存当前数据库的数据到磁盘】(BootRedisServerController-bgsave)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 当前数据库的key的数量
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/dbSize",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse dbSize(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【当前数据库的key的数量】(BootRedisServerController-dbSize)-传入参数为空");

		long len = 0l;
		try {
			len = redisService.dbSize();
			logger.info("===step2:【当前数据库的key的数量】(BootRedisServerController-dbSize)-当前key的数量-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【当前数据库的key的数量】(BootRedisServerController-dbSize)-当前key的数量-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【当前数据库的key的数量】(BootRedisServerController-dbSize)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 关于Redis服务器的各种信息和统计数
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/info",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse info(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【关于Redis服务器的各种信息和统计数】(BootRedisServerController-info)-传入参数为空");

		String result = null;
		try {
			result = redisService.discard();
			logger.info("===step2:【关于Redis服务器的各种信息和统计数】(BootRedisServerController-info)-各种信息和统计数-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【关于Redis服务器的各种信息和统计数】(BootRedisServerController-info)-各种信息和统计数-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【关于Redis服务器的各种信息和统计数】(BootRedisServerController-info)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 执行一个同步保存
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/save",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse save(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【执行一个同步保存】(BootRedisServerController-save)-传入参数为空");

		String result = null;
		try {
			result = redisService.save();
			logger.info("===step2:【执行一个同步保存】(BootRedisServerController-save)-同步保存-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【执行一个同步保存接】(BootRedisServerController-save)-同步保存-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【执行一个同步保存】(BootRedisServerController-save)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 停止所有客户端
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/shutdown",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse shutdown(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【停止所有客户端】(BootRedisServerController-shutdown)-传入参数为空");

		String result = null;
		try {
			result = redisService.shutdown();
			logger.info("===step2:【停止所有客户端】(BootRedisServerController-shutdown)-停止所有客户端-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【停止所有客户端】(BootRedisServerController-shutdown)-停止所有客户端-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【停止所有客户端】(BootRedisServerController-shutdown)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}


	/**
	 * 动态地修改复制(replication)功能
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/slaveof",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse slaveof(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【动态地修改复制功能】(BootRedisServerController-slaveof)-传入参数为空");

		String host = req.getHost();
		Integer port = req.getPort();
		if(StringUtils.isBlank(host)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "host为空");
		} else if(port == null) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "port为空");
		}

		String result = null;
		try {
			result = redisService.slaveof(host, port);
			logger.info("===step2:【动态地修改复制功能】(BootRedisServerController-slaveof)-修改复制功能-返回信息, result:{}", result);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【动态地修改复制功能】(BootRedisServerController-slaveof)-修改复制功能-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【动态地修改复制功能】(BootRedisServerController-slaveof)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 复制功能内部命令
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/sync",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse sync(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【复制功能内部命令】(BootRedisServerController-sync)-传入参数为空");

		try {
			redisService.sync();
			logger.info("===step2:【复制功能内部命令】(BootRedisServerController-sync)-复制内部命令-返回信息为空");
		} catch (BootServiceException e) {
			logger.error("===step2.1:【复制功能内部命令】(BootRedisServerController-sync)-复制内部命令-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		logger.info("===step3:【复制功能内部命令】(BootRedisServerController-sync)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/**
	 * 当前服务器时间
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/time",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse time(
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【当前服务器时间】(BootRedisServerController-time)-传入参数为空");

		List<String> result = null;
		try {
			result = redisService.time();
			logger.info("===step2:【当前服务器时间】(BootRedisServerController-time)-服务器时间-返回信息, result.size:{}", result == null ? null : result.size());
		} catch (BootServiceException e) {
			logger.error("===step2.1:【当前服务器时间】(BootRedisServerController-time)-服务器时间-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, result);
		logger.info("===step3:【当前服务器时间】(BootRedisServerController-time)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}

	/************************** jredis Connection(连接) 命令工具方法 ****************************/

}