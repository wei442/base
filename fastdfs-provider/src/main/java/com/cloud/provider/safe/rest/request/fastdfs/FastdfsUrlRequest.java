package com.cloud.provider.safe.rest.request.fastdfs;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FastdfsUrlRequest implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "文件url")
	@NotBlank(message = "文件url不能为空")
	private String fileUrl;

}