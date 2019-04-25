package com.cloud.provider.safe.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.provider.safe.base.BaseRestMapResponse;
import com.cloud.provider.safe.rest.request.fastdfs.FastdfsUploadRequest;
import com.cloud.provider.safe.rest.request.fastdfs.FastdfsUrlRequest;
import com.cloud.provider.safe.vo.fastdfs.FastdfsDownVo;
import com.cloud.provider.safe.vo.fastdfs.FastdfsVo;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * fastdfs管理 FastdfsController
 * @author wei.yong
 * @ClassName: FastdfsController
 */
@Api(tags = "fastdfs")
@RestController
@RequestMapping("/fastdfs")
public class FastdfsController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    /**
     * 上传文件
     * @param file
     * @param bindingResult
     * @return BaseRestMapResponse
     */
	@ApiOperation(value = "上传文件")
	@RequestMapping(value="/uploadFile",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse uploadFile(
		@Validated @RequestBody FastdfsUploadRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【上传文件】(FastdfsController-uploadFile)-请求参数, req:{}", req);

		byte[] bytes = req.getBytes();
		String fileName = req.getFileName();
		Long fileSize = req.getFileSize();

		InputStream inputStream = new ByteArrayInputStream(bytes);
		StorePath storePath = fastFileStorageClient.uploadFile(inputStream, fileSize, fileName, null);
        logger.info("===step2:【上传文件】(FastdfsController-uploadFile)-上传文件, storePath:{}", storePath);

		String fileUrl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
		FastdfsVo fastdfsVo = new FastdfsVo();
		fastdfsVo.setGroup(storePath.getGroup());
		fastdfsVo.setFileUrl(fileUrl);

		//返回信息
		BaseRestMapResponse fastdfsResponse = new BaseRestMapResponse();
		fastdfsResponse.putAll((JSONObject) JSONObject.toJSON(fastdfsVo));
	    logger.info("===step3:【上传文件】(FastdfsController-uploadFile)-返回信息, fastdfsResponse:{}", fastdfsResponse);
	    return fastdfsResponse;
	}

    /**
     * 上传图片
     * @param file
     * @param bindingResult
     * @return BaseRestMapResponse
     */
	@ApiOperation(value = "上传图片")
	@RequestMapping(value="/uploadImage",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse uploadImage(
		@Validated @RequestBody FastdfsUploadRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【上传图片】(FastdfsController-uploadImage)-请求参数, req:{}", req);

		byte[] bytes = req.getBytes();
		String fileName = req.getFileName();
		Long fileSize = req.getFileSize();

		InputStream inputStream = new ByteArrayInputStream(bytes);
		StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(inputStream, fileSize, fileName, null);
        logger.info("===step2:【上传图片】(FastdfsController-uploadImage)-上传文件, storePath:{}", storePath);

		String fileUrl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
		FastdfsVo fastdfsVo = new FastdfsVo();
		fastdfsVo.setGroup(storePath.getGroup());
		fastdfsVo.setFileUrl(fileUrl);

		//返回信息
		BaseRestMapResponse fastdfsResponse = new BaseRestMapResponse();
		fastdfsResponse.putAll((JSONObject) JSONObject.toJSON(fastdfsVo));
	    logger.info("===step3:【上传图片】(FastdfsController-uploadImage)-返回信息, fastdfsResponse:{}", fastdfsResponse);
	    return fastdfsResponse;
	}

	/**
	 * 获取文件
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "获取文件")
	@RequestMapping(value="/getFile",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse getFile(
		@Validated @RequestBody FastdfsUrlRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【获取文件】(FastdfsController-getFile)-请求参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String fileUrl = req.getFileUrl();
		StorePath storePath = StorePath.parseFromUrl(fileUrl);
		FileInfo fileInfo = fastFileStorageClient.queryFileInfo(storePath.getGroup(), storePath.getPath());
		logger.info("===step2:【获取文件】(FastdfsController-getFile)-获取文件, fileInfo:{}", fileInfo);

		//返回信息
		BaseRestMapResponse fastdfsResponse = new BaseRestMapResponse();
		fastdfsResponse.putAll((JSONObject) JSONObject.toJSON(fileInfo));
	    logger.info("===step3:【获取文件】(FastdfsController-getFile)-返回信息, fastdfsResponse:{}", fastdfsResponse);
	    return fastdfsResponse;
	}

	/**
	 * 下载文件
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "下载文件")
	@RequestMapping(value="/downloadFile",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse downloadFile(
		@Validated @RequestBody FastdfsUrlRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【下载文件】(FastdfsController-downloadFile)-请求参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String fileUrl = req.getFileUrl();
		StorePath storePath = StorePath.parseFromUrl(fileUrl);
		logger.info("===step1:【下载文件】(FastdfsController-downloadFile)-请求参数, storePath.getGroup():{}, storePath.getFullPath():{}", storePath.getGroup(), storePath.getFullPath());
		byte[] bytes = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), null);
		logger.info("===step2:【下载文件】(FastdfsController-downloadFile)-下载文件, bytes.length:{}", bytes == null ? null : bytes.length);
		String fileName = StringUtils.substringAfterLast(fileUrl, "/");

        FastdfsDownVo fastdfsDownVo = new FastdfsDownVo();
        fastdfsDownVo.setFileName(fileName);
        fastdfsDownVo.setBytes(bytes);

        //返回信息
      	BaseRestMapResponse fastdfsResponse = new BaseRestMapResponse();
        fastdfsResponse.putAll((JSONObject) JSONObject.toJSON(fastdfsDownVo));
	    logger.info("===step3:【下载文件】(FastdfsController-downloadFile)-返回信息, fastdfsResponse:{}", fastdfsResponse);
	    return fastdfsResponse;
	}

	/**
	 * 删除文件
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@ApiOperation(value = "删除文件")
	@RequestMapping(value="/deleteFile",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse deleteFile(
		@Validated @RequestBody FastdfsUrlRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【删除文件】(FastdfsController-deleteFile)-请求参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String fileUrl = req.getFileUrl();
		StorePath storePath = StorePath.parseFromUrl(fileUrl);
		fastFileStorageClient.deleteFile(storePath.getGroup(), storePath.getPath());

		//返回信息
		BaseRestMapResponse fastdfsResponse = new BaseRestMapResponse();
		logger.info("===step2:【删除文件】(FastdfsController-deleteFile)-返回信息, fastdfsResponse:{}", fastdfsResponse);
		return fastdfsResponse;
	}

}