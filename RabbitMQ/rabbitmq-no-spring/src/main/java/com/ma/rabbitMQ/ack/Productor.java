package com.ma.rabbitMQ.ack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;

/**
 * 
 * @author mgh_2
 *
 * @desription basicPublish中mandatory-true 监听器接收到路由不可达的信息，添加返回监听
 */
public class Productor {

	private static final String ADDRS = "192.168.1.104";

	public static void main(String[] args) throws IOException, TimeoutException {
		String exchange = "test";
		String routingKey = "error";

		// 创建ConnectionFactory
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(ADDRS);
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/test");
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");

		// 获取一个连接
		Connection connection = connectionFactory.newConnection();

		// 创建一个信道
		Channel channel = connection.createChannel();

		// 指定消息投递模式:消息的确认模式
		channel.confirmSelect();

		// 发布消息mandatory-true 监听器接收到路由不可达的信息
		String msg = "hello rabbitmq send confirm message";
		/* 设置信息属性 */
		Map<String, Object> headers = new HashMap<>();
		headers.put("num",1);
		BasicProperties properties = new AMQP.BasicProperties().builder()
				.deliveryMode(2)//消息是否持久 2-持久，1-不持久
				.contentEncoding("UTF-8")
				.expiration("10000")//过期时间
				.headers(headers)
				.build();
		channel.basicPublish(exchange, routingKey, true, properties, msg.getBytes("utf-8"));

		// 添加信息确认监听
		channel.addConfirmListener(new ConfirmListener() {

			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("--------nn ack------");
			}

			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				System.out.println("--------ack----------");
			}
		});

		//添加消息返回监听
		channel.addReturnListener(new ReturnListener() {
			
			@Override
			public void handleReturn(int replyCode,
		            String replyText,
		            String exchange,
		            String routingKey,
		            BasicProperties properties,
		            byte[] body)
					throws IOException {
				System.out.println("-------- return-----------");
				System.out.println("replyCode"+replyCode);
				System.out.println("replyText"+replyText);
				System.out.println("exchange"+exchange);
				System.out.println("routingKey"+routingKey);
				System.out.println("properties"+properties);
				System.out.println("body"+body);
			}
		});
	}
}
