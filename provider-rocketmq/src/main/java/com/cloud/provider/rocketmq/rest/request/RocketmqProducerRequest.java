package com.cloud.provider.rocketmq.rest.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RocketmqProducerRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "目的")
	@NotBlank(message = "目的不能为空")
	private String destination;

	@ApiModelProperty(value = "内容")
	@NotBlank(message = "内容不能为空")
	private Object content;

}