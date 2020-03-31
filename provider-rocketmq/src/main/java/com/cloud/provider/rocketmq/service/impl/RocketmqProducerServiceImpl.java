package com.cloud.provider.rocketmq.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.cloud.common.exception.RocketmqException;
import com.cloud.provider.rocketmq.service.IRocketmqProducerService;

/**
 *
 * @author wei.yong
 */
@Service
public class RocketmqProducerServiceImpl implements IRocketmqProducerService {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
    private RocketMQTemplate rocketMQTemplate;

	private static final String TX_PGROUP_NAME = "myTxProducerGroup";

	// TODO rocketmq 生产者方法
	/*****************************************************rocketmq生产者方法*****************************************************/
	/**
	 * 同步发送消息，默认重试两次
	 * 消息发送方发出数据后，会在收到接收方发回响应之后才发下一个数据包的通讯方式。
	 * 应用场景：应用场景非常广泛，例如重要通知邮件、报名短信通知、营销短信系统等。
	 * @param destination 格式: `topicName:tags`
	 * @param message
	 * @return SendResult
	 */
	@Override
	public SendResult syncSend(String destination,Object content) {
		logger.info("(RocketmqProducerService-syncSend)-同步发送消息-传入参数, destination:{}, content:{}", destination, content);
		Message<?> message = MessageBuilder.withPayload(content).build();
		SendResult sendResult = rocketMQTemplate.syncSend(destination, message);
		return sendResult;
	}

	/**
	 * 顺序发送消息，不重试，通过指定hashkey实现顺序消费，同步的hashkey会按顺序消费
	 * 消息发送方发出数据后，会在收到接收方发回响应之后才发下一个数据包的通讯方式。
	 * 应用场景：应用场景非常广泛，例如重要通知邮件、报名短信通知、营销短信系统等。
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @param hashKey     使用哈希键来选择队列,例如: orderId, productId ...
	 * @return SendResult
	 */
	@Override
	public SendResult syncSendOrderly(String destination,Object content,String hashKey) {
		logger.info("(RocketmqProducerService-syncSendOrderly)-同步发送消息-传入参数, destination:{}, content:{}, hashKey:{}", destination, content, hashKey);
		Message<?> message = MessageBuilder.withPayload(content).build();
		SendResult sendResult = rocketMQTemplate.syncSendOrderly(destination, message, hashKey);
		return sendResult;
	}

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
	@Override
	public void asyncSend(String destination,Object content) {
		logger.info("(RocketmqProducerService-asyncSend)-异步发送消息-传入参数, destination:{}, content:{}", destination, content);
		Message<?> message = MessageBuilder.withPayload(content).build();
		rocketMQTemplate.asyncSend(destination, message, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
//	 			System.out.printf("%-10d OK %s %n", index,  sendResult.getMsgId());
			}
			@Override
			public void onException(Throwable e) {
//				System.out.printf("%-10d Exception %s %n", index, e);
			}
		});
	}

	/**
	 * 异步顺序发送消息
	 * 发送方发出数据后，不等接收方发回响应，接着发送下个数据包的通讯方式。
	 * 应用场景：异步发送一般用于链路耗时较长，对 RT 响应时间较为敏感的业务场景，例如用户视频上传后通知启动转码服务，转码完成后通知推送转码结果等。
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @param hashKey     使用哈希键来选择队列,例如: orderId, productId ...
	 * @return SendResult
	 */
	@Override
	public void asyncSendOrderly(String destination,Object content,String hashKey) {
		logger.info("(RocketmqProducerService-asyncSendOrderly)-异步顺序发送消息-传入参数, destination:{}, content:{}, hashKey:{}", destination, content, hashKey);
		Message<?> message = MessageBuilder.withPayload(content).build();
		rocketMQTemplate.asyncSendOrderly(destination, message, hashKey, new SendCallback() {
			@Override
			public void onSuccess(SendResult sendResult) {
//	 			System.out.printf("%-10d OK %s %n", index,  sendResult.getMsgId());
			}
			@Override
			public void onException(Throwable e) {
//				System.out.printf("%-10d Exception %s %n", index, e);
			}
		});
	}

	/**
	 * 单向发送消息
	 * 只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答。此方式发送消息的过程耗时非常短，一般在微秒级别。
	 * 应用场景：适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @return SendResult
	 */
	@Override
	public void sendOneway(String destination,Object content) {
		logger.info("(RocketmqProducerService-sendOneway)-单向发送消息-传入参数, destination:{}, content:{}", destination, content);
		Message<?> message = MessageBuilder.withPayload(content).build();
		rocketMQTemplate.sendOneWay(destination, message);
	}

	/**
	 * 单向顺序发送消息
	 * 只负责发送消息，不等待服务器回应且没有回调函数触发，即只发送请求不等待应答。此方式发送消息的过程耗时非常短，一般在微秒级别。
	 * 应用场景：适用于某些耗时非常短，但对可靠性要求并不高的场景，例如日志收集。
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @param hashKey     使用哈希键来选择队列,例如: orderId, productId ...
	 * @return SendResult
	 */
	@Override
	public void sendOneWayOrderly(String destination,Object content,String hashKey) {
		logger.info("(RocketmqProducerService-sendOneWayOrderly)-单向发送消息-传入参数, destination:{}, content:{}, hashKey:{}", destination, content, hashKey);
		Message<?> message = MessageBuilder.withPayload(content).build();
		rocketMQTemplate.sendOneWayOrderly(destination, message, hashKey);
	}

	/**
	 * 批量发送消息
	 * 批量发送可以提高发送性能，但有一定的限制：topic相同，waitStoreMsgOK相同，不支持延时发送，一批消息的大小不能大于 1M
	 * @param destination 格式: `topicName:tags`
	 * @param messages
	 * @return SendResult
	 */
	@Override
	public SendResult sendBatch(String destination,List<Message<?>> messages) {
		logger.info("(RocketmqProducerService-sendBatch)-批量发送消息-传入参数, destination:{}, messages:{}", destination, messages);
		SendResult sendResult = rocketMQTemplate.syncSend(destination, messages);
		return sendResult;
	}

	/**
	 * 延时发送消息
	 * 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
	 * 1级 2级 3级 4级 5级 6级 7级 8级 9级 10级 11级 12级 13级 14级 15级 16级 17级 18级
	 * @param destination 格式: `topicName:tags`
	 * @param content
	 * @param timeout
	 * @param delayLevel
	 * @return SendResult
	 */
	@Override
	public SendResult syncSendDelay(String destination,Object content,long timeout,Integer delayLevel) {
		logger.info("(RocketmqProducerService-sendDelay)-延时发送消息-传入参数, destination:{}, content:{}, timeout:{}, delayLevel:{}", destination, content, timeout, delayLevel);
		Message<?> message = MessageBuilder.withPayload(content).build();
		SendResult sendResult = rocketMQTemplate.syncSend(destination, message, timeout, delayLevel);
		return sendResult;
	}

	/**
	 * 事务消息
	 * @param topic
	 * @param tag
	 * @param message
	 * @return SendResult
	 * @throws RocketmqException
	 */
	@Override
	public TransactionSendResult sendTransaction(String txProducerGroup,String topic,String tag,Object content) {
		logger.info("(RocketmqProducerService-sendTransaction)-事务消息-传入参数, topic:{}, tag:{}, content:{}", topic, tag, content);
		Message<?> message = MessageBuilder.withPayload(content).build();
		TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(TX_PGROUP_NAME, topic, message, null);
		return transactionSendResult;
	}

    @RocketMQTransactionListener(txProducerGroup = TX_PGROUP_NAME)
    class TransactionListenerImpl implements RocketMQLocalTransactionListener {
        private AtomicInteger transactionIndex = new AtomicInteger(0);

        private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<String, Integer>();

        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            String transId = (String)msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
            System.out.printf("#### executeLocalTransaction is executed, msgTransactionId=%s %n", transId);
            int value = transactionIndex.getAndIncrement();
            int status = value % 3;
            localTrans.put(transId, status);
            if (status == 0) {
                // Return local transaction with success(commit), in this case,
                // this message will not be checked in checkLocalTransaction()
                System.out.printf("    # COMMIT # Simulating msg %s related local transaction exec succeeded! ### %n", msg.getPayload());
                return RocketMQLocalTransactionState.COMMIT;
            }

            if (status == 1) {
                // Return local transaction with failure(rollback) , in this case,
                // this message will not be checked in checkLocalTransaction()
                System.out.printf("    # ROLLBACK # Simulating %s related local transaction exec failed! %n", msg.getPayload());
                return RocketMQLocalTransactionState.ROLLBACK;
            }

            System.out.printf("    # UNKNOW # Simulating %s related local transaction exec UNKNOWN! \n");
            return RocketMQLocalTransactionState.UNKNOWN;
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            String transId = (String)msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID);
            RocketMQLocalTransactionState retState = RocketMQLocalTransactionState.COMMIT;
            Integer status = localTrans.get(transId);
            if (null != status) {
                switch (status) {
                    case 0:
                        retState = RocketMQLocalTransactionState.UNKNOWN;
                        break;
                    case 1:
                        retState = RocketMQLocalTransactionState.COMMIT;
                        break;
                    case 2:
                        retState = RocketMQLocalTransactionState.ROLLBACK;
                        break;
                }
            }
            System.out.printf("------ !!! checkLocalTransaction is executed once," +" msgTransactionId=%s, TransactionState=%s status=%s %n", transId, retState, status);
            return retState;
        }
    }

	// Define transaction listener with the annotation @RocketMQTransactionListener
//  @RocketMQTransactionListener(txProducerGroup = TX_PGROUP_NAME)
//  class TransactionListenerImpl implements RocketMQLocalTransactionListener {
//        @Override
//        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
//          // ... local transaction process, return bollback, commit or unknown
//          return RocketMQLocalTransactionState.UNKNOWN;
//        }
//
//        @Override
//        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
//          // ... check transaction status and return bollback, commit or unknown
//          return RocketMQLocalTransactionState.COMMIT;
//        }
//  }
	/*****************************************************rocketmq生产者方法*****************************************************/

	/**
	 * 关闭MQProducer
	 * @param producer
	 */
	public void closeProducer(MQProducer producer) {
		if(producer != null) {
			producer.shutdown();
		}
	}

}