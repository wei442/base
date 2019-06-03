package com.cloud.provider.rocketmq.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.RedisConstants;
import com.cloud.provider.rocketmq.base.BaseRestMapResponse;
import com.cloud.provider.rocketmq.rest.request.RocketmqConsumerRequest;
import com.cloud.provider.rocketmq.service.IRocketmqConsumerService;

/**
 * Rocketmq Consumer（消费者） BootRocketmqConsumerController
 * @author wei.yong
 */
@RestController
@RequestMapping("/rocketmq/consumer")
public class RocketmqConsumerController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//rocketmq消费者 Service
	@Autowired
	private IRocketmqConsumerService rocketmqConsumerService;

	/**
	 * 消费消息
	 * @param req
	 * @param request
	 * @param response
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/consumer",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse consumer(
		@RequestBody RocketmqConsumerRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【消费消息】(BootRocketmqConsumerController-consumer)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String topic = req.getTopic();
		String tag = req.getTag();

		ConsumeOrderlyStatus consumeOrderlyStatus = null;
		consumeOrderlyStatus = rocketmqConsumerService.consumer(topic, tag);
		logger.info("===step2:【消费消息】(BootRocketmqConsumerController-consumer)-消费消息-返回信息, consumeOrderlyStatus:{}", consumeOrderlyStatus);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		rocketmqResponse.put(RedisConstants.RESULT, consumeOrderlyStatus);
		logger.info("===step3:【消费消息】(BootRocketmqConsumerController-consumer)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}


	/**
	 * 订阅者消费广播消息
	 * @param req
	 * @param request
	 * @param response
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/consumerBroadcast",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse consumerBroadcast(
		@RequestBody RocketmqConsumerRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【消费广播消息】(BootRocketmqConsumerController-consumerBroadcast)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String topic = req.getTopic();
		String tag = req.getTag();

		ConsumeConcurrentlyStatus consumeConcurrentlyStatus = null;
		consumeConcurrentlyStatus = rocketmqConsumerService.consumerBroadcast(topic, tag);
		logger.info("===step2:【消费广播消息】(BootRocketmqConsumerController-consumerBroadcast)-消费广播消息-返回信息, consumeConcurrentlyStatus:{}", consumeConcurrentlyStatus);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		rocketmqResponse.put(RedisConstants.RESULT, consumeConcurrentlyStatus);
		logger.info("===step3:【消费广播消息】(BootRocketmqConsumerController-consumerBroadcast)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}

	/**
	 * 消费过滤消息
	 * @param req
	 * @param request
	 * @param response
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/consumeFilter",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse consumeFilter(
		@RequestBody RocketmqConsumerRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【消费过滤消息】(BootRocketmqConsumerController-consumeFilter)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String topic = req.getTopic();
		String sql = req.getSql();

		ConsumeConcurrentlyStatus consumeConcurrentlyStatus = null;
		consumeConcurrentlyStatus = rocketmqConsumerService.consumeFilter(topic, sql);
		logger.info("===step2:【消费过滤消息】(BootRocketmqConsumerController-consumeFilter)-消费过滤消-返回信息, consumeConcurrentlyStatus:{}", consumeConcurrentlyStatus);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		rocketmqResponse.put(RedisConstants.RESULT, consumeConcurrentlyStatus);
		logger.info("===step3:【消费过滤消息】(BootRocketmqConsumerController-consumeFilter)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}

}