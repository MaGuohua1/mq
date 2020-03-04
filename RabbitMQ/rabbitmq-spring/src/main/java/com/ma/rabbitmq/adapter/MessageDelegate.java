package com.ma.rabbitmq.adapter;

import java.io.File;
import java.util.Map;

import com.ma.rabbitmq.vo.Order;

public class MessageDelegate {

	public void handleMessage(byte[] body) {
		System.out.println("默认方法，消息内容：" + new String(body));
	}

	public void consumeMessage(byte[] body) {
		System.out.println("修改方法名，消息内容：" + new String(body));
	}

	public void consumeMessage(String body) {
		System.out.println("修改方法名，自定义消息转换器，消息内容：" + body);
	}

	public void method1(byte[] body) {
		System.out.println("byte[] method1 : " + new String(body));
	}
	
	public void method1(String body) {
		System.out.println("method1 : " + body);
	}

	public void method2(Map<?, ?> body) {
		System.out.println("Map Jackson2JsonMessageConverter : " + body);
	}
	public void method2(Order order) {
		System.out.println("order Jackson2JsonMessageConverter : " + order);
	}

	public void consumeMessage(File body) {
		System.out.println("Jackson2JsonMessageConverter : " + body.getName());
	}
}
