package com.ma.config;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@ComponentScan("com.ma")
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
	 * (生产者)
	 * @return
	 */
	@Bean
	public JmsTemplate jmsTopicTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
		jmsTemplate.setPubSubDomain(true);
		return jmsTemplate;
	}

	/**
	 * 队列模式
	 * (生产者)
	 * @return
	 */
	@Bean
	public JmsTemplate jmsQueueTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
		jmsTemplate.setPubSubDomain(false);
		return jmsTemplate;
	}
}
