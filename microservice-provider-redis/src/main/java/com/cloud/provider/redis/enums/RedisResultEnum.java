package com.cloud.provider.redis.enums;

public enum RedisResultEnum {

	UNKNOWN_ERROR("-1", "unknown exception"),
	REDIS_FIELD_EMPTY("6010001", "传入参数为空"),
	REDIS_ERROR("6020002", "redis错误"),
	REDIS_NULL_ERROR("6020003", "redis空信息错误");

	private String code;

	private String msg;

	private RedisResultEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return this.msg;
	}

}