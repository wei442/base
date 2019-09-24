package com.cloud.provider.rocketmq.rest.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RocketmqProducerOrderRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "目的")
	@NotBlank(message = "目的不能为空")
	private String destination;

	@ApiModelProperty(value = "内容")
	@NotBlank(message = "内容不能为空")
	private Object content;

	@ApiModelProperty(value = "哈希键")
	@NotBlank(message = "哈希键不能为空")
	private String hashKey;

//	@ApiModelProperty(value = "超时时间")
//	@NotBlank(message = "超时时间不能为空")
//	private long timeout;
//
//	@ApiModelProperty(value = "延迟级别")
//	@NotBlank(message = "延迟级别不能为空")
//	private Integer delayLevel;
//
//	@ApiModelProperty(value = "消息内容")
//	@NotBlank(message = "消息内容不能为空")
//	private List<Message<?>> messages;


}