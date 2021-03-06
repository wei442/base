package com.cloud.provider.sms.rest.request.sms;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SmsSendTimeRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "手机号码")
	@NotBlank(message = "手机号码不能为空")
	private String mobile;

	@ApiModelProperty(value = "发送内容")
	@NotBlank(message = "发送内容不能为空")
	private String content;

	@ApiModelProperty(value = "发送时间")
	@NotNull(message = "发送时间不能为空")
	private String sendTime;


}