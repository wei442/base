package com.cloud.provider.mail.rest.request.mail;

import java.io.Serializable;

import org.springframework.beans.BeanUtils;

import com.cloud.provider.mail.po.MailAttachment;
import com.google.common.base.Converter;

import lombok.Data;

@Data
public class MailAttachmentRequest implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

    private String fileName;

    private String filePath;

    /**
	 * 实体转换
	 * @return MailAttachment
	 */
	public MailAttachment convertToMailAttachment() {
		MailAttachmentConvert convert = new MailAttachmentConvert();
		return convert.doForward(this);
	}

	/**
	 * req转换实体
	 * @author wei.yong
	 */
	private static class MailAttachmentConvert extends Converter<MailAttachmentRequest, MailAttachment> {

		@Override
		protected MailAttachment doForward(MailAttachmentRequest mailAttachmentRequest) {
			MailAttachment mailAttachment = new MailAttachment();
			BeanUtils.copyProperties(mailAttachmentRequest, mailAttachment);
			return mailAttachment;
		}

		@Override
		protected MailAttachmentRequest doBackward(MailAttachment b) {
			return null;
		}

	}

}