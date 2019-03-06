package com.cloud.provider.redis.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.cloud.provider.redis.service.IBootRedisLockService;
import com.cloud.provider.redis.service.IBootRedisService;

/**
 * Redis 基础类  BootBaseController
 * @author wei.yong
 * @date 2017-09-14
 */
@RestController
public class BootBaseController {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	//redis Service
	@Autowired
	protected IBootRedisService redisService;

	//redisLock Service
	@Autowired
	protected IBootRedisLockService redisLockService;

}