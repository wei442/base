package com.ochain.provider.redis.boot;

import java.io.Serializable;

/**
 * boot基础请求
 * @author wei.yong
 */
public class BootRestRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//当前页
	private int pageNum = 1;

	//每页的数量
	private int pageSize = 10;

	//平台
	private String platform;

	//版本号
	private String appVersion;

	//imei号
	private String imei;

	//渠道号
	private String channel;

	//登录方式
	private String loginMode;

	//登录ip
    private String loginIp;

    //登录类型
    private Integer loginType;

    //日志类型
    private Integer logType;

	public int getPageNum() {
		return this.pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getPlatform() {
		return this.platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getChannel() {
		return this.channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getLoginMode() {
		return this.loginMode;
	}

	public void setLoginMode(String loginMode) {
		this.loginMode = loginMode;
	}

	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public Integer getLoginType() {
		return this.loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public Integer getLogType() {
		return this.logType;
	}

	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	@Override
	public String toString() {
		return "BootRestRequest [pageNum=" + pageNum + ", pageSize=" + pageSize + ", platform=" + platform
				+ ", appVersion=" + appVersion + ", imei=" + imei + ", channel=" + channel + ", loginMode=" + loginMode
				+ ", loginIp=" + loginIp + ", loginType=" + loginType + ", logType=" + logType + "]";
	}

}