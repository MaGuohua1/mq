package com.ma.replyto;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReplyTo {

	@Autowired
	private JmsTemplate jmsTemplate;

	public void send(String result, Message message) {
		try {
			jmsTemplate.send(message.getJMSReplyTo(), session -> send(result, session));
		} catch (JmsException | JMSException e) {
			e.printStackTrace();
		}

	}

	private Message send(Object message, Session session) throws JMSException {
		if (message instanceof String) {
			TextMessage textMessage = session.createTextMessage();
			String sm = (String) message;
			textMessage.setText(sm);
			System.out.println("ReplyTo " + sm);
			return textMessage;
		}
		if (message instanceof byte[]) {
			BytesMessage bytesMessage = session.createBytesMessage();
			byte[] bm = (byte[]) message;
			bytesMessage.writeBytes(bm);
			System.out.println("ReplyTo " + bm);
			return bytesMessage;
		}
		return null;
	}

}
