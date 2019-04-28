//package com.cloud.provider.redis.exception;
//
//import com.cloud.provider.redis.enums.RedisResultEnum;
//
///**
// * Redis异常 RedisException
// * @author wei.yong
// */
//public class RedisException extends Exception {
//
//	private static final long serialVersionUID = 1L;
//
//	private String errorCode;
//
//	public RedisException() {
//	}
//
//	public RedisException(Throwable e) {
//		super(e);
//	}
//
//	public RedisException(String errorCode) {
//	    this.errorCode = errorCode;
//	}
//
//	 public RedisException(String errorCode, String msg) {
//	    super(msg);
//	    this.errorCode = errorCode;
//	 }
//
//	public RedisException(String errorCode, Throwable e) {
//		super(e);
//	    this.errorCode = errorCode;
//	}
//
//	public RedisException(RedisResultEnum result) {
//		super(result.getMsg());
//		this.errorCode = result.getCode();
//	}
//
//	public String getErrorCode() {
//		return errorCode;
//	}
//
//	public void setErrorCode(String errorCode) {
//		this.errorCode = errorCode;
//	}
//
//}