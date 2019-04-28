package com.cloud.provider.redis.base;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.provider.redis.constants.RedisConstants;

/**
 * base map返回
 * @author wei.yong
 */
public class BaseRestMapResponse extends JSONObject implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public BaseRestMapResponse() {
		this.put(RedisConstants.RET_CODE, RedisConstants.OK);
		this.put(RedisConstants.RET_MSG, RedisConstants.OK_MSG);
	}

	public BaseRestMapResponse(String code,String msg) {
		this.put(RedisConstants.RET_CODE, RedisConstants.OK);
		this.put(RedisConstants.RET_MSG, RedisConstants.OK_MSG);
	}

	public BaseRestMapResponse(RedisResultEnum enums) {
		this.put(RedisConstants.RET_CODE, enums.getCode());
		this.put(RedisConstants.RET_MSG, enums.getMsg());
	}

}