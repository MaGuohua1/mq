package com.ma.rabbitmq.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Barista {
	
	@Input(value = "input_channel")
	SubscribableChannel input();

}
