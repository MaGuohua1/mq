package com.ma.rabbitMQ.message;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
/**
 * 
 * @author mgh_2
 *
 * @desription 经过默认的交换机直接接收消息
 */
public class Consumer {
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

		/* 声明一个队列，param1:队列名，param2:是否持久化，param3:是否独占，param4:是否删除，param5: null */
		channel.queueDeclare("queue", true, false, true, null);

		/* 创建消费者 */
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			/* 获取信息 */
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				String msg = new String(body, "utf-8");
				System.out.println("exchange [ " + envelope.getExchange() + " ] / routeKey [ "
						+ envelope.getRoutingKey() + " ] , the msg : " + msg);
			}
		};

		/* 基础的客户消费数据 */
		channel.basicConsume("queue", true, consumer);

	}
}
