package com.cloud.provider.mail.rest.page.mail;

import com.cloud.provider.mail.base.BaseRestRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttachmentLogPageRequest extends BaseRestRequest {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "附件名称")
	private String attachmentName;

	@ApiModelProperty(value = "附件rul")
    private String attachmentUrl;

	@ApiModelProperty(value = "附件类型")
    private Integer attachmentType;

	@ApiModelProperty(value = "附件内容")
    private String content;

}