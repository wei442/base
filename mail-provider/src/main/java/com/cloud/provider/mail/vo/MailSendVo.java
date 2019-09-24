package com.cloud.provider.mail.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class MailSendVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mobile;

	private String content;

	private String time;

}