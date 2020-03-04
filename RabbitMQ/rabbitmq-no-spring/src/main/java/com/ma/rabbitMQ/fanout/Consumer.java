package com.ma.rabbitMQ.fanout;

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
 * @desription 交换机fanout模式（面向队列广播模式），路由键不用匹配
 */
public class Consumer {
	private static final String ADDRS = "192.168.1.104";

	public static void main(String[] args) throws IOException, TimeoutException {
		String exchange = "test";
		String queue = "queue";
		String routingKey = "";

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

		/* 声明交换器 */
		channel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT, true, true, false, null);

		/* 声明一个队列，param1:队列名，param2:是否持久化，param3:是否独占，param4:是否删除，param5: null */
		channel.queueDeclare(queue, true, false, true, null);

		/* 队列和交换器绑定 */
		channel.queueBind(queue, exchange, routingKey);

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
		channel.basicConsume(queue, true, consumer);

	}
}
