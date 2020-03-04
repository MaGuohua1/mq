package com.ma.rabbitmq.quick;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ma.rabbitmq.vo.Order;

@Component
public class SendMessaage {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send() {

		MessageProperties messageProperties = new MessageProperties();
		messageProperties.getHeaders().put("num", 1);
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
		Message message = new Message("this is first spring rabbit".getBytes(), messageProperties);

		// 发送文本消息
		rabbitTemplate.convertAndSend("direct", "routingKey", message, new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().getHeaders().put("ddd", "sss");
				return message;
			}
		});

		// 发送json消息
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		Order order = new Order(11, "jim");
		ObjectMapper mapper = new ObjectMapper();
		byte[] orderStr = null;
		try {
			orderStr = mapper.writeValueAsBytes(order);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(new String(orderStr));
		message = new Message(orderStr, messageProperties);
		rabbitTemplate.convertAndSend("fanout", "", message);

		// 发送java对象
//		messageProperties.getHeaders().put("__TypeId__", "com.ma.rabbitmq.vo.Order");
//		rabbitTemplate.convertAndSend("fanout", "routingkey", message);

		// 发送java对象映射转换
		messageProperties.getHeaders().put("__TypeId__", "order");
		rabbitTemplate.convertAndSend("fanout", "routingkey", message);

		// 发送图片
		try {
			messageProperties.setContentType("image/png");
			byte[] bs = Files.readAllBytes(Paths.get("E:/mgh_2/Pictures", "xsj.jpg"));
			message = new Message(bs, messageProperties);
			rabbitTemplate.convertAndSend("topic", "routingkey", message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 发送pdf
		try {
			messageProperties.setContentType("application/pdf");
			byte[] bs = Files.readAllBytes(Paths.get("E:/mgh_2/Videos/数据结构与算法算法分析/Java数据结构和算法.（第二版）.pdf"));
			message = new Message(bs, messageProperties);
			rabbitTemplate.convertAndSend("topic", "routingkey", message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
