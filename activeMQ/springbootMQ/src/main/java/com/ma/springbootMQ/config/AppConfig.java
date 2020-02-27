package com.ma.springbootMQ.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

@EnableJms
@Configuration
public class AppConfig {
    
	@Bean
	public ConnectionFactory connectionFactory() {
		return new ActiveMQConnectionFactory();
	}
	
	//广播
	@Bean
	public JmsListenerContainerFactory<?> topicListenerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(new ActiveMQConnectionFactory());
		factory.setPubSubDomain(true);
		return factory;
	}
	
}
