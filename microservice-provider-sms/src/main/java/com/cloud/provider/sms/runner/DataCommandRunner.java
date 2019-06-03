package com.cloud.provider.sms.runner;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 用户数据启动加载
 * @author wei.yong
 */
@Component
public class DataCommandRunner implements CommandLineRunner {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 运行
	 */
	@Override
	public void run(String... args) throws Exception {
	}

	/**
	 * 销毁
	 * 睡眠1秒
	 */
	@PreDestroy
    public void destory() {
		long begintime = System.currentTimeMillis();
    	logger.info("【DataCommandRunner-destroy】-用户数据销毁开始,请等待1秒钟,程序将自动退出!");
    	try {
    		//睡眠2,000毫秒(2秒)
    		Thread.sleep(2000);
		} catch (InterruptedException e) {
			logger.error("【DataCommandRunner-destroy】-用户数据销毁-事务性异常, Exception = {}, message = {}", e, e.getMessage());
		}
        long endtime = System.currentTimeMillis();
        logger.info("【DataCommandRunner-destroy】-用户数据销毁完成, 耗时"+(endtime-begintime)+"ms！");
    }

}