package com.ma.springbootMQ.queue;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class QueueProductor {

	@Autowired
	private JmsMessagingTemplate JmsTemplate;
	
	public void send(Destination destination, Object message) {
		JmsTemplate.convertAndSend(destination, message);
	}
	
	@JmsListener(destination = "test.sendto")
	public void receive(String message) {
		System.out.println("result "+message);
	}
}
