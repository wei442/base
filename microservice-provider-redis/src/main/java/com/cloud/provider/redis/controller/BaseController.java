package com.cloud.provider.redis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.provider.redis.service.IRedisLockService;
import com.cloud.provider.redis.service.IRedisService;

/**
 * Redis 基础类  BaseController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
public class BaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//redis Service
	@Autowired
	protected IRedisService redisService;

	//redisLock Service
	@Autowired
	protected IRedisLockService redisLockService;

}