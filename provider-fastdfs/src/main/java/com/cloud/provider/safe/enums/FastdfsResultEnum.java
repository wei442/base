package com.cloud.provider.safe.enums;

public enum FastdfsResultEnum {

	UNKNOWN_ERROR("-1", "unknown exception"),
	FASTDFS_FIELD_EMPTY("6030001", "传入参数为空"),
	FASTDFS_ERROR("6030002", "fastdfs错误"),
	FASTDFS_NULL_ERROR("6030003", "fastdfs空信息错误"),

	FASTDFS_FILE_INFO_ERROR("6030004", "文件信息失败"),
	FASTDFS_UPLOAD_FILE_ERROR("6030005", "上传文件失败"),
	FASTDFS_UPLOAD_IMAGE_ERROR("6030006", "上传图片失败"),
	FASTDFS_DELETE_FILE_ERROR("6030007", "删除文件失败"),
	FASTDFS_DOWNLOAD_ERROR("6030008", "文件下载失败"),

	;

	private String code;

	private String msg;

	private FastdfsResultEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return this.msg;
	}

}