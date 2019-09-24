package com.cloud.provider.sms.rest.request.sms;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.cloud.provider.sms.po.Sms;
import com.google.common.base.Converter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SmsRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "手机号码")
	@NotBlank(message = "手机号码不能为空")
	private String mobile;

	@ApiModelProperty(value = "发送内容")
	@NotBlank(message = "发送内容不能为空")
	private String content;

	@ApiModelProperty(value = "来源")
	private String source;

	/**
	 * 实体转换
	 * @return Sms
	 */
	public Sms convertToSms() {
		SmsConvert convert = new SmsConvert();
		return convert.doForward(this);
	}

	/**
	 * req转换实体
	 * @author wei.yong
	 */
	private static class SmsConvert extends Converter<SmsRequest, Sms> {

		@Override
		protected Sms doForward(SmsRequest smsRequest) {
			Sms sms = new Sms();
			BeanUtils.copyProperties(smsRequest, sms);
			return sms;
		}

		@Override
		protected SmsRequest doBackward(Sms b) {
			return null;
		}

	}

}