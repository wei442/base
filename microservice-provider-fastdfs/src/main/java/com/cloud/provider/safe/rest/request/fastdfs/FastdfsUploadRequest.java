package com.cloud.provider.safe.rest.request.fastdfs;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FastdfsUploadRequest implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "文件名称")
	@NotBlank(message = "文件名称不能为空")
	private String fileName;

	@ApiModelProperty(value = "文件大小")
	@NotNull(message = "文件大小不能为空")
	private Long fileSize;

	@ApiModelProperty(value = "文件url")
	@NotEmpty(message = "文件url为空")
	private byte[] bytes;

}