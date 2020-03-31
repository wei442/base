package com.cloud.provider.rocketmq.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ListIterator;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.common.exception.RocketmqException;
import com.cloud.provider.rocketmq.service.IRocketmqConsumerService;

@Service
public class RocketmqConsumerServiceImpl implements IRocketmqConsumerService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DefaultMQPushConsumer defaultMQPushConsumer;

	@Autowired
	private DefaultMQPullConsumer defaultMQPullConsumer;

	// TODO rocketmq 消费者方法
	/*****************************************************rocketmq消费者方法*****************************************************/
	/**
	 * 消费消息
	 * @param topic
	 * @param tag
	 * @return ConsumeOrderlyStatus
	 * @throws RocketmqException
	 */
	@Override
	public ConsumeOrderlyStatus consumer(String topic,String tag) {
		logger.info("(BootRocketmqConsumerService-consumer)-消费消息-传入参数, topic:{}, tag:{}", topic, tag);
		try {
			// Subscribe one more more topics to consume.
			defaultMQPushConsumer.subscribe(topic, tag);
			defaultMQPushConsumer.registerMessageListener(new MessageListenerOrderly() {
	            @Override
	            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
	                context.setAutoCommit(false);

	                //模拟业务逻辑处理中...
	                ListIterator<MessageExt> it = msgs.listIterator();
	                while(it.hasNext()) {
	                	MessageExt messageExt = it.next();
	                	String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
	                	logger.info("(BootRocketmqConsumerService-consumer)-消费消息-传入参数, body:{}", body);
	                }
	                try {
	                    //模拟业务逻辑处理中...
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }

	                return ConsumeOrderlyStatus.SUCCESS;
	            }
	        });
			defaultMQPushConsumer.start();
		} catch (MQClientException e) {
			logger.error("(BootRocketmqConsumerService-consumer)-消费消息-异常, Exception={}, message={}", e, e.getMessage());
		} finally {
			//关闭连接
			closePushConsumer(defaultMQPushConsumer);
		}
		return ConsumeOrderlyStatus.SUCCESS;
	}

	/**
	 * 订阅者消费广播消息
	 * @param topic
	 * @param tag
	 * @return ConsumeConcurrentlyStatus
	 * @throws RocketmqException
	 */
	@Override
	public ConsumeConcurrentlyStatus consumerBroadcast(String topic,String tag) {
		logger.info("(BootRocketmqConsumerService-consumerBroadcast)-同步发送消息-传入参数, topic:{}, tag:{}", topic, tag);
		try {
			defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
	        //set to broadcast mode
			defaultMQPushConsumer.setMessageModel(MessageModel.BROADCASTING);
			defaultMQPushConsumer.subscribe(topic, tag);
			defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
	            @Override
	            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	            }
	        });
			defaultMQPushConsumer.start();
		} catch (MQClientException e) {
			logger.error("(BootRocketmqConsumerService-consumerBroadcast)-消费信息-异常, Exception={}, message={}", e, e.getMessage());
		} finally {
			//关闭连接
			closePushConsumer(defaultMQPushConsumer);
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

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
	 * @throws RocketmqException
	 */
	@Override
	public ConsumeConcurrentlyStatus consumeFilter(String topic,String sql) {
		logger.info("(BootRocketmqConsumerService-consumeFilter)-消费过滤消息-传入参数, topic:{}, sql:{}", topic, sql);
		try {
			defaultMQPushConsumer.subscribe(topic, MessageSelector.bySql(sql));
			defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
	            @Override
	            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

	            	 //模拟业务逻辑处理中...
	                ListIterator<MessageExt> it = msgs.listIterator();
	                while(it.hasNext()) {
	                	MessageExt messageExt = it.next();
	                	String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
	                	logger.info("(BootRocketmqConsumerService-consumeFilter)-消费消息-传入参数, body:{}", body);
	                }
	                try {
	                    //模拟业务逻辑处理中...
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }

	                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	            }
	        });

	        //Launch the consumer instance.
			defaultMQPushConsumer.start();
		} catch (MQClientException e) {
			logger.error("(BootRocketmqConsumerService-consumeFilter)-消费过滤消息-异常, Exception={}, message={}", e, e.getMessage());
		} finally {
			//关闭连接
			closePushConsumer(defaultMQPushConsumer);
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	/*****************************************************rocketmq消费者方法*****************************************************/

	/**
	 * 关闭MQPushConsumer
	 * @param consumer
	 */
	public void closePushConsumer(MQPushConsumer consumer) {
		if(consumer != null) {
			consumer.shutdown();
		}
	}


}