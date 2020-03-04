package com.ma.rabbitmq.send;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.ma.rabbitmq.stream.Barista;

@Service
@EnableBinding(Barista.class)
public class Send {

	@Autowired
	private Barista barista;
	
	public void sendMessage(Object message,Map<String,Object> properties) {
		MessageHeaders headers = new MessageHeaders(properties);
		headers.put("aaa", 111);
		Message<Object> msg = MessageBuilder.createMessage(message, headers);
		boolean b = barista.output().send(msg);
		System.out.println("send a message： "+message+", sendStatus: "+b);

	}
}
