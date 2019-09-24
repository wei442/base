package com.cloud.provider.rocketmq.rest.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RocketmqProducerDelayRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "目的")
	@NotBlank(message = "目的不能为空")
	private String destination;

	@ApiModelProperty(value = "内容")
	@NotBlank(message = "内容不能为空")
	private Object content;

	@ApiModelProperty(value = "超时时间")
	@NotNull(message = "超时时间不能为空")
	private long timeout;

	@ApiModelProperty(value = "延迟级别")
	@NotNull(message = "延迟级别不能为空")
	private Integer delayLevel;

}