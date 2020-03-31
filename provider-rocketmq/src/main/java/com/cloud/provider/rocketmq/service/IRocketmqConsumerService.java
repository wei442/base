package com.cloud.provider.rocketmq.service;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;

public interface IRocketmqConsumerService {

	/*****************************************************rocketmq消费者方法*****************************************************/
	/**
	 * 消费消息
	 * @param topic
	 * @param tag
	 * @return ConsumeOrderlyStatus
	 * @throws BootServiceException
	 */
	public ConsumeOrderlyStatus consumer(String topic,String tag);

	/**
	 * 订阅者消费广播消息
	 * @param topic
	 * @param tag
	 * @return ConsumeConcurrentlyStatus
	 * @throws BootServiceException
	 */
	public ConsumeConcurrentlyStatus consumerBroadcast(String topic,String tag);

	/**
	 * 消费过滤消息
	 * tag 可以帮助我们方便的选择我们想要的消息，但 tag 有个限制，一个消息只能设置一个tag，在某些场景下这就很不方便了，这时就可以使用 filter，rocketmq 支持使用 sql 语句的方式来进行消息过滤。
	 * 语法：1、数字比较 >, >=, <, <=, BETWEEN, =
	 * 2、字符比较 =, <>, IN
	 * 3、IS NULL 或者 IS NOT NULL
	 * 4、逻辑操作 AND,OR,NOT
	 * @param topic
	 * @param sql
	 * @return ConsumeConcurrentlyStatus
	 * @throws BootServiceException
	 */
	public ConsumeConcurrentlyStatus consumeFilter(String topic,String sql);
	/*****************************************************rocketmq消费者方法*****************************************************/

}