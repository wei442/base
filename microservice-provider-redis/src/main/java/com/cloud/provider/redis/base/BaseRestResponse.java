package com.cloud.provider.redis.base;

import java.io.Serializable;

import com.cloud.provider.redis.constants.RedisConstants;

/**
 * base返回
 * @author wei.yong
 */
public class BaseRestResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
     * 响应码
     */
	protected String bootCode;

    /**
     * 响应信息
     */
	protected String bootMsg;

	public BaseRestResponse() {
		this.bootCode = RedisConstants.OK;
		this.bootMsg = RedisConstants.OK_MSG;
	}

	public BaseRestResponse(String code,String msg) {
		this.bootCode = code;
		this.bootMsg = msg;
	}

	public String getBootCode() {
		return this.bootCode;
	}

	public void setBootCode(String bootCode) {
		this.bootCode = bootCode;
	}

	public String getBootMsg() {
		return this.bootMsg;
	}

	public void setBootMsg(String bootMsg) {
		this.bootMsg = bootMsg;
	}

	@Override
	public String toString() {
		return "BootRestResponse [bootCode=" + bootCode + ", bootMsg=" + bootMsg + "]";
	}

}