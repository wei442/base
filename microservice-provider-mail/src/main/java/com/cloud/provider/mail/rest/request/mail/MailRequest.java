package com.cloud.provider.mail.rest.request.mail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import com.cloud.provider.mail.po.Mail;
import com.cloud.provider.mail.po.MailAttachment;
import com.google.common.base.Converter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MailRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	//邮件发件人
	@ApiModelProperty(value = "邮件发件人")
	private String mailFrom;

	//邮件收件人
    @ApiModelProperty(value = "邮件收件人")
	@NotBlank(message = "邮件收件人不能为空")
	private String mailTo;

	//邮件抄送人
    @ApiModelProperty(value = "邮件抄送人")
	private String mailCc;

	//邮件密送人
    @ApiModelProperty(value = "邮件密送人")
	private String mailBcc;

	//邮件主题
    @ApiModelProperty(value = "邮件主题")
	@NotBlank(message = "邮件主题不能为空")
	private String subject;

	//邮件内容
    @ApiModelProperty(value = "邮件内容")
	@NotBlank(message = "邮件内容不能为空")
	private String text;

    @ApiModelProperty(value = "邮件状态")
    private Integer status;

    @ApiModelProperty(value = "邮件发送时间")
    private Date sendTime;

    @ApiModelProperty(value = "邮件类型")
    private Integer mailType;

    @ApiModelProperty(value = "邮件来源")
    private String source;

    @ApiModelProperty(value = "邮件附件")
	private List<MailAttachmentRequest> mailAttachmentList;

    /**
	 * 实体转换
	 * @return Mail
	 */
	public Mail convertToMail() {
		MailConvert convert = new MailConvert();
		return convert.doForward(this);
	}

	/**
     * 实体列表转换
     * @return List<MailAttachment>
     */
    public List<MailAttachment> convertToMailAttachmentList() {
    	MailAttachmentConvert convert = new MailAttachmentConvert();
    	List<MailAttachment> mailAttachmentListNew = null;
    	if(mailAttachmentList != null && !mailAttachmentList.isEmpty()) {
    		mailAttachmentListNew = new ArrayList<MailAttachment>(mailAttachmentList.size());
    		ListIterator<MailAttachmentRequest> it = mailAttachmentList.listIterator();
    		while(it.hasNext()) {
    			MailAttachmentRequest mailAttachmentRequest = it.next();
    			mailAttachmentListNew.add(convert.doForward(mailAttachmentRequest));
    		}
    	}
    	return mailAttachmentListNew;
    }

	/**
	 * req转换实体
	 * @author wei.yong
	 */
	private static class MailConvert extends Converter<MailRequest, Mail> {

		@Override
		protected Mail doForward(MailRequest mailRequest) {
			Mail mail = new Mail();
			BeanUtils.copyProperties(mailRequest, mail);
			return mail;
		}

		@Override
		protected MailRequest doBackward(Mail b) {
			return null;
		}

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