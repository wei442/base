package com.cloud.provider.rocketmq.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.CommConstants;
import com.cloud.provider.rocketmq.base.BaseRestMapResponse;
import com.cloud.provider.rocketmq.rest.request.RocketmqProducerBatchRequest;
import com.cloud.provider.rocketmq.rest.request.RocketmqProducerDelayRequest;
import com.cloud.provider.rocketmq.rest.request.RocketmqProducerOrderRequest;
import com.cloud.provider.rocketmq.rest.request.RocketmqProducerRequest;
import com.cloud.provider.rocketmq.service.IRocketmqProducerService;

/**
 * Rocketmq Producer（生产者） RocketmqProducerController
 * @author wei.yong
 */
@RestController
@RequestMapping("/rocketmq/producer")
public class RocketmqProducerController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//rocketmq生产者 Service
	@Autowired
	private IRocketmqProducerService rocketmqProducerService;

	/**
	 * 同步发布消息
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/syncSend",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse syncSend(
		@Validated @RequestBody RocketmqProducerRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【同步发布消息】(RocketmqProducerController-syncSend)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String destination = req.getDestination();
		Object content = req.getContent();

		SendResult sendResult = rocketmqProducerService.syncSend(destination, content);
		logger.info("===step2:【同步发布消息】(RocketmqProducerController-syncSend)-同步发布消息, sendResult:{}", sendResult);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		rocketmqResponse.put(CommConstants.RESULT, sendResult);
		logger.info("===step3:【同步发布消息】(RocketmqProducerController-syncSend)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}

	/**
	 * 同步顺序发布消息
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/syncSendOrderly",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse syncSendOrderly(
		@Validated @RequestBody RocketmqProducerOrderRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【同步顺序发布消息】(RocketmqProducerController-syncSendOrderly)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String destination = req.getDestination();
		Object content = req.getContent();
		String hashKey = req.getHashKey();

		SendResult sendResult = rocketmqProducerService.syncSendOrderly(destination, content, hashKey);
		logger.info("===step2:【同步顺序发布消息】(RocketmqProducerController-syncSendOrderly)-同步发布消息, sendResult:{}", sendResult);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		rocketmqResponse.put(CommConstants.RESULT, sendResult);
		logger.info("===step3:【同步顺序发布消息】(RocketmqProducerController-syncSendOrderly)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}


	/**
	 * 异步发布消息
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/asyncSend",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse asyncSend(
		@Validated @RequestBody RocketmqProducerRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【异步发布消息】(RocketmqProducerController-asyncSend)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String destination = req.getDestination();
		Object content = req.getContent();

		rocketmqProducerService.asyncSend(destination, content);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		logger.info("===step2:【异步发布消息】(RocketmqProducerController-asyncSend)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}

	/**
	 * 异步顺序发布消息
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/asyncSendOrderly",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse asyncSendOrderly(
		@Validated @RequestBody RocketmqProducerOrderRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【异步顺序发布消息】(RocketmqProducerController-asyncSendOrderly)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String destination = req.getDestination();
		Object content = req.getContent();
		String hashKey = req.getHashKey();

		rocketmqProducerService.asyncSendOrderly(destination, content, hashKey);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		logger.info("===step2:【异步顺序发布消息】(RocketmqProducerController-asyncSendOrderly)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}


	/**
	 * 单向发送消息
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/sendOneway",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sendOneway(
		@Validated @RequestBody RocketmqProducerRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【单向发送消息】(RocketmqProducerController-sendOneway)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String destination = req.getDestination();
		Object content = req.getContent();

		rocketmqProducerService.sendOneway(destination, content);
		logger.info("===step2:【单向发送消息】(RocketmqProducerController-sendOneway)-单向发送消息-不返回信息");

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		logger.info("===step3:【单向发送消息】(RocketmqProducerController-sendOneway)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}

	/**
	 * 单向顺序发送消息
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/sendOneWayOrderly",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sendOneWayOrderly(
		@Validated @RequestBody RocketmqProducerOrderRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【单向顺序发送消息】(RocketmqProducerController-sendOneWayOrderly)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String destination = req.getDestination();
		Object content = req.getContent();
		String hashKey = req.getHashKey();

		rocketmqProducerService.sendOneWayOrderly(destination, content, hashKey);
		logger.info("===step2:【单向顺序发送消息】(RocketmqProducerController-sendOneWayOrderly)-单向发送消息-不返回信息");

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		logger.info("===step3:【单向顺序发送消息】(RocketmqProducerController-sendOneWayOrderly)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}

	/**
	 * 批量发送消息
	 * @param req
	 * @param bindingResult
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/sendBatch",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sendBatch(
		@Validated @RequestBody RocketmqProducerBatchRequest req,
		BindingResult bindingResult) {
		logger.info("===step1:【批量发送消息】(RocketmqProducerController-sendBatch)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String destination = req.getDestination();
		List<Message<?>> messages = req.getMessages();

		SendResult sendResult = rocketmqProducerService.sendBatch(destination, messages);
		logger.info("===step2:【同步发布消息】(RocketmqProducerController-sendSync)-同步发布消息, sendResult:{}", sendResult);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		rocketmqResponse.put(CommConstants.RESULT, sendResult);
		logger.info("===step3:【批量发送消息】(RocketmqProducerController-sendBatch)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}

	/**
	 * 延时发送消息
	 * @param req
	 * @param request
	 * @param response
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/syncSendDelay",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse syncSendDelay(
		@RequestBody RocketmqProducerDelayRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【延时发送消息】(RocketmqProducerController-syncSendDelay)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String destination = req.getDestination();
		Object content = req.getContent();
		long timeout = req.getTimeout();
		Integer delayLevel = req.getDelayLevel();

		SendResult sendResult = rocketmqProducerService.syncSendDelay(destination, content, timeout, delayLevel);
		logger.info("===step2:【延时发送消息】(RocketmqProducerController-syncSendDelay)-延时发送消息, sendResult:{}", sendResult);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
		rocketmqResponse.put(CommConstants.RESULT, sendResult);
		logger.info("===step3:【延时发送消息】(RocketmqProducerController-syncSendDelay)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}

	/**
	 * 事务消息
	 * @param req
	 * @param request
	 * @param response
	 * @return BaseRestMapResponse
	 */
	@RequestMapping(value="/sendTransaction",method={RequestMethod.POST})
	@ResponseBody
	public BaseRestMapResponse sendTransaction(
		@RequestBody RocketmqProducerRequest req,
		HttpServletRequest request, HttpServletResponse response) {
		logger.info("===step1:【事务消息】(RocketmqProducerController-sendTransaction)-传入参数, req:{}, json:{}", req, JSONObject.toJSONString(req));

		String destination = req.getDestination();
		Object content = req.getContent();
//		String hashKey = req.getHashKey();
//		long timeout = req.getTimeout();
//		Integer delayLevel = req.getDelayLevel();

//		SendResult sendResult = rocketmqProducerService.sendTransaction(txProducerGroup, topic, tag, content);
//		logger.info("===step2:【同步发布消息】(RocketmqProducerController-sendSync)-同步发布消息, sendResult:{}", sendResult);

		BaseRestMapResponse rocketmqResponse = new BaseRestMapResponse();
//		rocketmqResponse.put(CommConstants.RESULT, sendResult);
		logger.info("===step3:【事务消息】(RocketmqProducerController-sendTransaction)-返回信息, rocketmqResponse:{}", rocketmqResponse);
		return rocketmqResponse;
	}

}