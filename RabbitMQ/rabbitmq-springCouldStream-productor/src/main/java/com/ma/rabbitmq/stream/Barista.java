package com.ma.rabbitmq.stream;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Barista {

	@Output(value = "output_channel")
	MessageChannel output();
}
