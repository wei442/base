package com.cloud.provider.rocketmq.rest.request;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.messaging.Message;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RocketmqProducerBatchRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "目的")
	@NotBlank(message = "目的不能为空")
	private String destination;

	@ApiModelProperty(value = "消息内容")
	@NotEmpty(message = "消息内容不能为空")
	private List<Message<?>> messages;

}