package com.ma.rabbitmq.productor;

import java.util.Map;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.ma.rabbitmq.vo.Order;

@Component
public class Productor {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	final ConfirmCallback confirmCallback = new ConfirmCallback() {
		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause) {
			System.out.println("correlationData : " + correlationData);
			System.out.println("ack : " + ack);
			if (!ack) {
				System.out.println("cause : " + cause);
			}
		}
	};

	final ReturnCallback returnCallback = new ReturnCallback() {
		@Override
		public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText,
				String exchange, String routingKey) {
			System.out.println("=======message : " + message.getBody());
			System.out.println("=======replyCode : " + replyCode);
			System.out.println("=======replyText : " + replyText);
			System.out.println("=======exchange : " + exchange);
			System.out.println("=======routingKey : " + routingKey);
		}
	};

	public void send(Order order) {
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		rabbitTemplate.convertAndSend("exchange", "routingkey", order,
				new CorrelationData(System.currentTimeMillis() + ""));
	}
	
	public <T> void send(T message, Map<String, Object> properties) {
		MessageHeaders headers = new MessageHeaders(properties);
		Message<T> msg = MessageBuilder.createMessage(message, headers);
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		rabbitTemplate.convertAndSend("exchange1", "routingkey", msg,
				new CorrelationData(System.currentTimeMillis() + ""));
	}
	
}
