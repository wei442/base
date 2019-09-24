package com.cloud.provider.safe.vo.fastdfs;

import java.io.Serializable;

import lombok.Data;

@Data
public class FastdfsDownVo implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String fileName;

    private byte[] bytes;

}