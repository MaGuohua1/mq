package com.ma.config;

import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.ma.queue.QueueListener1;
import com.ma.queue.QueueListener2;
import com.ma.topic.TopicListener1;
import com.ma.topic.TopicListener2;

public class AppConfig {

	@Bean
	public ActiveMQConnectionFactory targetConnectionFactory() {
		return new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
	}

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(targetConnectionFactory());
		cachingConnectionFactory.setSessionCacheSize(100);
		return cachingConnectionFactory;
	}

	/**
	 * 发布订阅模式
	 * (消费者1)
	 * @return
	 */
	@Bean
	public DefaultMessageListenerContainer topicMessageListenerContainer1(TopicListener1 topicListener1) {
		DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
		defaultMessageListenerContainer.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
		defaultMessageListenerContainer.setMessageListener(topicListener1);
		defaultMessageListenerContainer.setDestinationName("test.topic");
		defaultMessageListenerContainer.setPubSubDomain(true);
		return defaultMessageListenerContainer;
	}
	/**
	 * 发布订阅模式
	 * (消费者2)
	 * @return
	 */
	@Bean
	public DefaultMessageListenerContainer topicMessageListenerContainer2(TopicListener2 topicListener2) {
		DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
		defaultMessageListenerContainer.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
		defaultMessageListenerContainer.setMessageListener(topicListener2);
		defaultMessageListenerContainer.setDestinationName("test.topic");
		defaultMessageListenerContainer.setPubSubDomain(true);
		return defaultMessageListenerContainer;
	}

	/**
	 * 队列模式
	 * (消费者1)
	 * @return
	 */
	@Bean
	public DefaultMessageListenerContainer queueMessageListenerContainer1(QueueListener1 queueListener1) {
		DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
		defaultMessageListenerContainer.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
		defaultMessageListenerContainer.setMessageListener(queueListener1);
		defaultMessageListenerContainer.setDestinationName("test.queue");
		defaultMessageListenerContainer.setPubSubDomain(false);
		return defaultMessageListenerContainer;
	}
	
	/**
	 * 队列模式
	 * (消费者2)
	 * @return
	 */
	@Bean
	public DefaultMessageListenerContainer queueMessageListenerContainer2(QueueListener2 queueListener2) {
		DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();
		defaultMessageListenerContainer.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		defaultMessageListenerContainer.setConnectionFactory(connectionFactory());
		defaultMessageListenerContainer.setMessageListener(queueListener2);
		defaultMessageListenerContainer.setDestinationName("test.queue");
		defaultMessageListenerContainer.setPubSubDomain(false);
		return defaultMessageListenerContainer;
	}
}
