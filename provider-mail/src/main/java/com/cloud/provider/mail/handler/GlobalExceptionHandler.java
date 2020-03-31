package com.cloud.provider.mail.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cloud.common.enums.mail.MailResultEnum;
import com.cloud.common.exception.EmailException;
import com.cloud.provider.mail.base.BaseRestMapResponse;

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
		if (e instanceof EmailException) {
			EmailException ex = (EmailException) e;
			logger.error("【全局异常处理】(GlobalExceptionHandler-exceptionHandler)-自定义异常, Exception = {}, message = {}", ex, ex.getMessage());
			return new BaseRestMapResponse(ex.getErrorCode(), ex.getMessage());
		}
		if (e instanceof MailException) {
			MailException ex = (MailException) e;
			logger.error("【全局异常处理】(GlobalExceptionHandler-exceptionHandler)-自定义异常, Exception = {}, message = {}", ex, ex.getMessage());
			return new BaseRestMapResponse(MailResultEnum.MAIL_SEND_FAIL);
		}
		if (e instanceof DataAccessException) {
			DataAccessException ex = (DataAccessException) e;
			logger.error("【全局异常处理】(GlobalExceptionHandler-exceptionHandler)-数据访问异常, Exception = {}, message = {}", ex, ex.getMessage());
			return new BaseRestMapResponse(MailResultEnum.SYSTEM_ERROR);
		}

		logger.error("【全局异常处理】(GlobalExceptionHandler-exceptionHandler)-未知异常, Exception = {}, message = {}", e, e.getMessage());
		String message = e.getMessage();
        String exceptionName = e.getClass() == null ? null : e.getClass().getSimpleName();
        String resultMessage = exceptionName == null ? "[" + message+ "]" : exceptionName + "[" + message + "]";
        return new BaseRestMapResponse(MailResultEnum.UNKNOWN_ERROR.getCode(), resultMessage);
	}

}
