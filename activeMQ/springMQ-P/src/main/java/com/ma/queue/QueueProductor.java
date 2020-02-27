package com.ma.queue;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.ma.response.GetResponse;
/**
 * 
 * @author mgh_2
 *
 * @desription 生产者发送消息，并且设置信息回复队列设置
 */
@Component
public class QueueProductor {

	@Autowired
	private JmsTemplate jmsQueueTemplate;
	@Autowired
	private GetResponse getResponse;

	public void send(String queueName, Object message) {
		jmsQueueTemplate.send(queueName, session -> send(message, session));
	}

	private Message send(Object message, Session session) throws JMSException {
		if (message instanceof String) {
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText((String) message);
			System.out.println(message);
			setResponse(session, textMessage);
			return textMessage;
		}
		if (message instanceof byte[]) {
			BytesMessage bytesMessage = session.createBytesMessage();
			bytesMessage.writeBytes((byte[]) message);
			setResponse(session, bytesMessage);
			return bytesMessage;
		}
		return null;
	}

	//设置获得处理结果的途径
	private void setResponse(Session session, Message message) throws JMSException {
		Destination tempQueue = session.createTemporaryQueue();
		MessageConsumer tempConsumer = session.createConsumer(tempQueue);//建立结果会话
		tempConsumer.setMessageListener(getResponse);//设置结果监听器
		message.setJMSCorrelationID(System.currentTimeMillis() + "");//设置信息唯一标识
		message.setJMSReplyTo(tempQueue);//设置返回结果队列
	}
}
