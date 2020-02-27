package com.ma.topic;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class TopicProductor {

	@Autowired
	private JmsTemplate jmsTopicTemplate;

	public void send(String queueName, Object message) {
		if (message instanceof String) {
			jmsTopicTemplate.send(queueName, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TextMessage textMessage = session.createTextMessage();
					textMessage.setText((String) message);
					System.out.println(queueName + "====" + message);
					return textMessage;
				}
			});
		}
		if (message instanceof byte[]) {
			jmsTopicTemplate.send(queueName, new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					BytesMessage bytesMessage = session.createBytesMessage();
					bytesMessage.writeBytes((byte[]) message);
					return bytesMessage;
				}
			});
		}
	}
}
