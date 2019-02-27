package com.ochain.provider.redis.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.ochain.common.constants.BootConstants;
import com.ochain.common.constants.RedisConstants;
import com.ochain.common.exception.BootServiceException;
import com.ochain.provider.redis.boot.BootRestMapResponse;
import com.ochain.provider.redis.rest.request.RedisRequest;

/**
 * Redis Pub/Sub（发布/订阅） BootRedisPubSubController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
@RequestMapping("/boot/redis/pubSub")
public class BootRedisPubSubController extends BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Pub/Sub（发布/订阅) 命令工具方法
	/************************** jredis Pub/Sub（发布/订阅) 命令工具方法 ****************************/
	/**
	 * 信息发送到指定的频道
	 * @param req
	 * @param request
	 * @param response
	 * @return BootRestMapResponse
	 */
	@RequestMapping(value="/publish",method={RequestMethod.POST})
	@ResponseBody
	public BootRestMapResponse publish(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【信息发送到指定的频道】(BootRedisPubSubController-publish)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String channel = req.getChannel();
		String message = req.getMessage();
		if(StringUtils.isBlank(channel)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "channel为空");
		} else if(StringUtils.isBlank(message)) {
			return new BootRestMapResponse(BootConstants.REDIS_FIELD_EMPTY, "message为空");
		}

		long len = 0l;
		try {
			len = redisService.publish(channel, message);
			logger.info("===step2:【信息发送到指定的频道】(BootRedisPubSubController-publish)-信息发送到频道-返回信息, len:{}", len);
		} catch (BootServiceException e) {
			logger.error("===step2.1:【信息发送到指定的频道】(BootRedisPubSubController-publish)-信息发送到频道-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, BootConstants.REDIS_ERROR)) {
				return new BootRestMapResponse(BootConstants.REDIS_ERROR, BootConstants.REDIS_ERROR_MSG);
			}
		}

		BootRestMapResponse redisResponse = new BootRestMapResponse();
		redisResponse.put(RedisConstants.RESULT, len);
		logger.info("===step3:【信息发送到指定的频道】(BootRedisPubSubController-publish)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis Pub/Sub（发布/订阅) 命令工具方法 ****************************/

}