package com.ma.response;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;
/**
 * 
 * @author mgh_2
 *
 * @desription	监听消息结果
 */
@Component
public class GetResponse implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				System.out.println("GetResponse " + ((TextMessage) message).getText());
			}
			if (message instanceof BytesMessage) {
				int len = -1;
				byte[] b = new byte[1024];
				BytesMessage bm = (BytesMessage) message;
				while ((len = bm.readBytes(b)) != -1) {
					System.out.println("GetResponse " + new String(b, 0, len));
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
