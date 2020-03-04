package com.ma.rabbitmq.consumer;

import java.io.IOException;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

@Service
@EnableBinding
public class Consumer {

	@StreamListener("input_channel")
	public void onMessage(@SuppressWarnings("rawtypes") Message message) throws IOException {
		Channel channel = (Channel) message.getHeaders().get(AmqpHeaders.CHANNEL);
		System.out.println(message.getPayload());
		long deliveryTag = (long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
		channel.basicAck(deliveryTag, false);
	}
}
