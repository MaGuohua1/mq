package com.ma.springbootMQ.queue;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueConsumer2 {

	@JmsListener(destination = "test.queue")
	public void receive(Object message) {
		try {
			if (message instanceof TextMessage) {
				System.out.println("QueueListener1 " + ((TextMessage) message).getText());
			}
			if (message instanceof BytesMessage) {
				int len = -1;
				byte[] b = new byte[1024];
				BytesMessage bm = (BytesMessage) message;
				while ((len = bm.readBytes(b)) != -1) {
					System.out.println(getClass().getName() + " " + new String(b, 0, len));
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
