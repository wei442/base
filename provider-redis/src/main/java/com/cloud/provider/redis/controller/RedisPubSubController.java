package com.cloud.provider.redis.controller;

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
import com.cloud.common.constants.CommConstants;
import com.cloud.common.enums.redis.RedisResultEnum;
import com.cloud.common.exception.RedisException;
import com.cloud.provider.redis.base.BaseRestMapResponse;
import com.cloud.provider.redis.rest.request.RedisRequest;

import io.swagger.annotations.Api;

/**
 * Redis Pub/Sub（发布/订阅） RedisPubSubController
 * @author wei.yong
 * @date 2017-09-14
 */
@Api(tags = "地理位置")
@RestController
@RequestMapping("/redis/pubSub")
public class RedisPubSubController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	// TODO Pub/Sub（发布/订阅) 命令工具方法
	/************************** jredis Pub/Sub（发布/订阅) 命令工具方法 ****************************/
	/**
	 * 信息发送到指定的频道
	 * @param req
	 * @param request
	 * @param response
	 * @return RestMapResponse
	 */
	@RequestMapping(value="/publish",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse publish(
		@RequestBody RedisRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【信息发送到指定的频道】(RedisPubSubController-publish)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String channel = req.getChannel();
		String message = req.getMessage();
		if(StringUtils.isBlank(channel)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "channel为空");
		} else if(StringUtils.isBlank(message)) {
			return new BaseRestMapResponse(RedisResultEnum.REDIS_FIELD_EMPTY.getCode(), "message为空");
		}

		long len = 0l;
		try {
			len = redisService.publish(channel, message);
			logger.info("===step2:【信息发送到指定的频道】(RedisPubSubController-publish)-信息发送到频道-返回信息, len:{}", len);
		} catch (RedisException e) {
			logger.error("===step2.1:【信息发送到指定的频道】(RedisPubSubController-publish)-信息发送到频道-事务性异常, Exception = {}, message = {}", e, e.getMessage());
			String errorCode = e.getErrorCode();
			if(StringUtils.equals(errorCode, RedisResultEnum.REDIS_ERROR.getCode())) {
				return new BaseRestMapResponse(RedisResultEnum.REDIS_ERROR);
			}
		}

		BaseRestMapResponse redisResponse = new BaseRestMapResponse();
		redisResponse.put(CommConstants.RESULT, len);
		logger.info("===step3:【信息发送到指定的频道】(RedisPubSubController-publish)-返回信息, redisResponse:{}", redisResponse);
		return redisResponse;
	}
	/************************** jredis Pub/Sub（发布/订阅) 命令工具方法 ****************************/

}