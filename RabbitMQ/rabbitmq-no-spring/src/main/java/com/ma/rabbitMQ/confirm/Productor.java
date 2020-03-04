package com.ma.rabbitMQ.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 
 * @author mgh_2
 *
 * @desription 交换机需要指定确认模式，并且添加确认监听
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

		// 发布消息
		String msg = "hello rabbitmq send confirm message";
		channel.basicPublish(exchange, routingKey, null, msg.getBytes("utf-8"));

		// 添加信息监听
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

		
	}
}
