package com.ma.queue;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.replyto.ReplyTo;

@Component
public class QueueListener1 implements MessageListener {

	@Autowired
	private ReplyTo replyTo;

	@Override
	public void onMessage(Message message) {
		try {
			String result = null;
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
			replyTo.send(result, message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
