package com.cloud.provider.rocketmq.service;

import java.util.List;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.springframework.messaging.Message;

import com.cloud.common.exception.RocketmqException;

public interface IRocketmqProducerService {

	/*****************************************************rocketmq生产者方法*****************************************************/
	/**
	 * 同步发送消息，默认重试两次
	 * 消息发送方发出数据后，会在收到接收方发回响应之后才发下一个数据包的通讯方式。
	 * 应用场景：应用场景非常广泛，例如重要通知邮件、报名短信通知、营销短信系统等。
	 * @param destination 格式: `topicName:tags`
	 * @param message
	 * @return SendResult
	 */
	public SendResult syncSend(String destination,Object content);

	/**
	 * 顺序发送消息，不重试，通过指定hashkey实现顺序消费，同步的hashkey会按顺序消费
	 * 消息发送方发出数据后，会在收到接收方发回响应之后才发下一个数据包的通讯方式。
	 * 应用场景：应用场景非常广泛，例如重要通知邮件、报名短信通知、营销短信系统等。
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @param hashKey     使用哈希键来选择队列,例如: orderId, productId ...
	 * @return SendResult
	 */
	public SendResult syncSendOrderly(String destination,Object content,String hashKey);

	/**
	 * 异步发送消息
	 * 发送方发出数据后，不等接收方发回响应，接着发送下个数据包的通讯方式。
	 * 应用场景：异步发送一般用于链路耗时较长，对 RT 响应时间较为敏感的业务场景，例如用户视频上传后通知启动转码服务，转码完成后通知推送转码结果等。
	 *@param destination 格式: `topicName:tags`
	 * @param tag
	 * @param message
	 * @return SendResult
	 * @throws RocketmqException
	 */
	public void asyncSend(String destination,Object content);

	/**
	 * 异步顺序发送消息
	 * 发送方发出数据后，不等接收方发回响应，接着发送下个数据包的通讯方式。
	 * 应用场景：异步发送一般用于链路耗时较长，对 RT 响应时间较为敏感的业务场景，例如用户视频上传后通知启动转码服务，转码完成后通知推送转码结果等。
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @param hashKey     使用哈希键来选择队列,例如: orderId, productId ...
	 * @return SendResult
	 */
	public void asyncSendOrderly(String destination,Object content,String hashKey);

	/**
	 * 单向发送消息
	 * 只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答。此方式发送消息的过程耗时非常短，一般在微秒级别。
	 * 应用场景：适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @return SendResult
	 */
	public void sendOneway(String destination,Object content);

	/**
	 * 单向顺序发送消息
	 * 只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答。此方式发送消息的过程耗时非常短，一般在微秒级别。
	 * 应用场景：适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @param hashKey     使用哈希键来选择队列,例如: orderId, productId ...
	 * @return SendResult
	 */
	public void sendOneWayOrderly(String destination,Object content,String hashKey);

	/**
	 * 批量发送消息
	 * 批量发送可以提高发送性能，但有一定的限制：topic相同，waitStoreMsgOK相同，不支持延时发送，一批消息的大小不能大于 1M
	 * @param destination 格式: `topicName:tags`
	 * @param messages
	 * @return SendResult
	 */
	public SendResult sendBatch(String destination,List<Message<?>> messages);

	/**
	 * 延时发送消息
	 * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @param timeout
	 * @param delayLevel
	 * @return SendResult
	 */
	public SendResult syncSendDelay(String destination,Object content,long timeout,Integer delayLevel);

	/**
	 * 事务消息
	 * @param topic
	 * @param tag
	 * @param message
	 * @return SendResult
	 * @throws RocketmqException
	 */
	public TransactionSendResult sendTransaction(String txProducerGroup,String topic,String tag,Object content);
	/*****************************************************rocketmq生产者方法*****************************************************/

}