package com.ma.springbootMQ.topic;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class TopicProductor {

	@Autowired
	private JmsMessagingTemplate JmsTemplate;
	
	public void send(Destination destination, Object message) {
		JmsTemplate.convertAndSend(destination, message);
	}
}
