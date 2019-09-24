package com.cloud.provider.rocketmq.rest.request;

import com.cloud.provider.rocketmq.base.BaseRestRequest;

public class RocketmqConsumerRequest extends BaseRestRequest {

	private static final long serialVersionUID = 1L;

	private String topic;

	private String tag;

	private String sql;

	public String getTopic() {
		return this.topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSql() {
		return this.sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String toString() {
		return "RocketmqConsumerRequest [topic=" + topic + ", tag=" + tag + ", sql=" + sql + "]";
	}

}