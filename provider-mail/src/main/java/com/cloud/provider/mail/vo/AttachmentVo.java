package com.cloud.provider.mail.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class AttachmentVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fileName;

	private byte[] bytes;

}