package com.ochain.provider.redis.service;

import com.ochain.common.exception.BootServiceException;

public interface IBootRedisLockService {

	/**
     * 锁定
     * @param key
     * @return boolean
     */
    public boolean lock(String key) throws BootServiceException;

    /**
     * 释放锁
     * @param key
     * @return boolean
     */
	public boolean unlock(String key) throws BootServiceException;

}