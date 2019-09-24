package com.cloud.provider.mail.rest.request.mail;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;

import com.cloud.provider.mail.vo.AttachmentVo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MailSendRequest implements Serializable {

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

	//邮件来源
    @ApiModelProperty(value = "邮件收件人")
    private String source;

	//邮件附件
    @ApiModelProperty(value = "邮件附件")
	private List<AttachmentVo> attachmentList;

}