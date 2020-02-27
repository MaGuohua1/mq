package com.ma.springbootMQ.queue;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class QueueConsumer {

	@JmsListener(destination = "test.queue")
	@SendTo("test.sendto")
	public String receive(Object message) {
		String result = null;
		try {
			if (message instanceof TextMessage) {
				result = ((TextMessage) message).getText();
				// System.out.println("QueueListener1 " + result);
			}
			if (message instanceof BytesMessage) {
				int len = -1;
				byte[] b = new byte[1024];
				BytesMessage bm = (BytesMessage) message;
				StringBuffer buffer = new StringBuffer();
				while ((len = bm.readBytes(b)) != -1) {
					String str = new String(b, 0, len);
					buffer.append(str);
				}
				result = buffer.toString();
				// System.out.println(buffer.toString());
			}
			// do something
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return result;
	}
}
