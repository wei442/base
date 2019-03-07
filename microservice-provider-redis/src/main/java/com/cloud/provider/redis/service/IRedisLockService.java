package com.cloud.provider.redis.service;

import com.cloud.provider.redis.exception.RedisException;

public interface IRedisLockService {

	/**
     * 锁定
     * @param key
     * @return boolean
     */
    public boolean lock(String key) throws RedisException;

    /**
     * 释放锁
     * @param key
     * @return boolean
     */
	public boolean unlock(String key) throws RedisException;

}