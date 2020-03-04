package com.ma.rabbitMQ.limit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * 
 * @author mgh_2
 *
 * @desription 1、设置autoAck=false 2、设置限流：channel.basicQos(0, 1, false) 3、在客户端设置应答
 */
public class Consumer {
	private static final String ADDRS = "192.168.1.104";

	public static void main(String[] args) throws IOException, TimeoutException {
		String exchange = "test";
		String queue = "queue";
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

		// 声明交换器(交换机名,交换机类型,是否持久化,是否删除,是否是内部的,参数)
		channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true, true, false, null);

		// 声明一个队列,(队列名,是否持久化,是否独占,是否删除,参数)
		channel.queueDeclare(queue, true, false, true, null);

		// 队列和交换器绑定
		channel.queueBind(queue, exchange, routingKey);

		// 设置限流 param1=0：不限制消息大小，param2=1:一次处理一条消息，param3=false:设置客户端限流
		channel.basicQos(0, 1, false);

		// 创建消费者
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			// 获取信息
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
					throws IOException {
				System.out.println(consumerTag);
				System.out.println(envelope);
				System.out.println(properties);
				System.out.println(new String(body, "utf-8"));
				// 设置应答 false 不批量应答
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};

		// 基础的客户消费数据
		channel.basicConsume(queue, false, consumer);

	}
}
