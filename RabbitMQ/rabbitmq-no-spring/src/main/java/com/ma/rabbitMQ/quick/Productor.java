package com.ma.rabbitMQ.quick;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * 
 * @author mgh_2
 *
 * @desription 经过默认的交换机直接把消息发送给消费者
 */
public class Productor {

	private static final String ADDRS = "192.168.1.104";

	public static void main(String[] args) throws IOException, TimeoutException {

		/* 创建一个rabbitMQ连接工厂 */
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(ADDRS);
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/test");
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");

		/* 获取一个连接 */
		Connection connection = connectionFactory.newConnection();

		/* 创建一个信道 */
		Channel channel = connection.createChannel();

		/* 发布消息 */
		String msg = "send a massage use rabbitMQ";
		for (int i = 0; i < 5; i++) {
			channel.basicPublish("", "queue", null, msg.getBytes("utf-8"));
		}

		channel.close();
		connection.close();
	}
}
