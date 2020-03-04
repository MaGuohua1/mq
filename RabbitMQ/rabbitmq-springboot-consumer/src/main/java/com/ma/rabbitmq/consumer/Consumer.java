package com.ma.rabbitmq.consumer;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.ma.rabbitmq.vo.Order;
import com.rabbitmq.client.Channel;

@Component
public class Consumer {

	/**
	 * spring.rabbitmq.listener.order.queue.name=queue
	 * spring.rabbitmq.listener.order.durable=true
	 * spring.rabbitmq.listener.order.auto-delete=true
	 * spring.rabbitmq.listener.order.exchange.name=exchange
	 * spring.rabbitmq.listener.order.exchange.type=topic
	 * spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions=true
	 * spring.rabbitmq.listener.order.key=routingkey
	 * 
	 * @param order
	 * @param channel
	 * @param headers
	 * @throws IOException
	 */
	@RabbitHandler
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}", durable = "${spring.rabbitmq.listener.order.durable}", autoDelete = "${spring.rabbitmq.listener.order.auto-delete}"), exchange = @Exchange(name = "${spring.rabbitmq.listener.order.exchange.name}", type = "${spring.rabbitmq.listener.order.exchange.type}", autoDelete = "${spring.rabbitmq.listener.order.auto-delete}", ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"), key = "${spring.rabbitmq.listener.order.key}"))
	private void onMessage(@Payload Order order, Channel channel, @Headers Map<String, Object> headers)
			throws IOException {
		System.out.println("order ：" + order);
		long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
		channel.basicAck(tag, false);
	}

	@RabbitHandler
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "queue1", durable = "true", autoDelete = "true"), exchange = @Exchange(name = "exchange1", type = "topic", autoDelete = "true", ignoreDeclarationExceptions = "true"), key = "routingkey"))
	private <T> void onMessage(Message<T> message, Channel channel) throws IOException {
		System.out.println("Consumer ：" + message.getPayload());
		for (Entry<String, Object> entry : message.getHeaders().entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		long tag = (long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
		channel.basicAck(tag, false);
	}
}
