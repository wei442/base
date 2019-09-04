package com.cloud.provider.redis.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.common.exception.RedisException;
import com.cloud.provider.redis.base.BaseRestMapResponse;

import redis.clients.jedis.exceptions.JedisException;

/**
 * @author S.J.
 * @date 2018/06/28
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@InitBinder
    public void initBinder(WebDataBinder binder) {
    }

	/**
	 * 异常处理
	 * @param e
	 * @return BootRestMapResponse
	 */
	@ExceptionHandler(value = Exception.class)
	public BaseRestMapResponse exceptionHandler(Exception e) {
		if (e instanceof RedisException) {
			RedisException ex = (RedisException) e;
			logger.error("【全局异常处理】(GlobalExceptionHandler-exceptionHandler)-自定义异常, Exception = {}, message = {}", ex, ex.getMessage());
			return new BaseRestMapResponse(ex.getErrorCode(), ex.getMessage());
		}
		if (e instanceof JedisException) {
			JedisException ex = (JedisException) e;
			logger.error("【全局异常处理】(GlobalExceptionHandler-exceptionHandler)-数据访问异常, Exception = {}, message = {}", ex, ex.getMessage());
			return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
		}

		logger.error("【全局异常处理】(GlobalExceptionHandler-exceptionHandler)-未知异常, Exception = {}, message = {}", e, e.getMessage());
		String message = e.getMessage();
        String exceptionName = e.getClass() == null ? null : e.getClass().getSimpleName();
        String resultMessage = exceptionName == null ? "[" + message+ "]" : exceptionName + "[" + message + "]";
        return new BaseRestMapResponse(RedisResultEnum.UNKNOWN_ERROR.getCode(), resultMessage);
	}

}
