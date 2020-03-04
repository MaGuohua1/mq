package com.ma.rabbitMQ.dlx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
 * @desription 设置死信队列
 */
public class Consumer {
	private static final String ADDRS = "192.168.1.104";

	public static void main(String[] args) throws IOException, TimeoutException {
		String dlx_exchange = "dlx.echange";
		String dlx_queue = "dlx.queue";
		String dlx_routingKey = "#";
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
		
		//声明死信交换机,队列和绑定
		channel.exchangeDeclare(dlx_exchange, BuiltinExchangeType.TOPIC, true, true, false, null);
		channel.queueDeclare(dlx_queue, true, false, true, null);
		channel.queueBind(dlx_queue, dlx_exchange, dlx_routingKey);
		
		//设置交换机参数(死信队列)
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("x-dead-letter-exchange", dlx_exchange);
		arguments.put("x-dead-letter-routing-key", "dlx_info");

		// 声明交换器(交换机名,交换机类型,是否持久化,是否删除,是否是内部的,参数)
		channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT, true, true, false, null);
		channel.queueDeclare(queue, true, false, true, arguments);
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
				if ((Integer) properties.getHeaders().get("num") == 1) {
					channel.basicNack(envelope.getDeliveryTag(), false, true);//第二个参数：是否批量，第三个参数：是否重新投回队列
				}
				// 设置应答 false 不批量应答
				channel.basicAck(envelope.getDeliveryTag(), false);
			}
		};

		// 基础的客户消费数据
		channel.basicConsume(queue, false, consumer);

//		channel.close();
//		connection.close();
	}
}
