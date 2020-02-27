package com.ma.springbootMQ.topic;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TopicConsumer2 {

	@JmsListener(destination = "test.topic", containerFactory = "topicListenerFactory")
	public void receive(Object message) {
		try {
			if (message instanceof TextMessage) {
				System.out.println("TopicConsumer " + ((TextMessage) message).getText());
			}
			if (message instanceof BytesMessage) {
				int len = -1;
				byte[] b = new byte[1024];
				BytesMessage bm = (BytesMessage) message;
				while ((len = bm.readBytes(b)) != -1) {
					System.out.println("TopicConsumer1 "+new String(b, 0, len));
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
