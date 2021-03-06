package com.ma.messageReliebility;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class TransactionConsumerAscyn2 {
	/* 默认的连接用户 */
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	/* 默认的连接密码 */
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	/* 默认的连接地址 */
	private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;

	public static void main(String[] args) {
		/* 连接工厂 */
		ConnectionFactory connectionFactory;
		/* 连接 */
		Connection connection = null;
		/* 会话 */
		Session session;
		/* 消息目的地 */
		Destination destination;
		/* 消息消费者 */
		MessageConsumer consumer;
		/* 实例化连接工厂 */
		connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKERURL);
		try {
			/* 通过工厂获取连接 */
			connection = connectionFactory.createConnection();
			/* 启动连接 */
			connection.start();
			/* 创建session，第一个参数表示是否启动事务，第二个参数表示是否自动确认 */
			// AUTO_ACKNOWLEDGE：自动确认
			// CLIENT_ACKNOWLEDGE：客户端手动确认
			// DUPS_OK_ACKNOWLEDGE：批量自动确认
			// SESSION_TRANSACTED：createSession设置额为true的时候，就开启事务
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			/* 创建名为helloworld的消息队列 */
//			destination = session.createTopic("hello-world");// 消息广播,一对多，acticeMQ关闭，广播消息消失，没有订阅的客户无法收到消息
			destination = session.createQueue("hello-world-queue");// 消息发布，一对一，acticeMQ关闭，消息不丢失
			/* 创建消息消费者 */
			consumer = session.createConsumer(destination);
			/* 设置消费者监听器，监听消息 */
			consumer.setMessageListener(message -> {// 异步获取
				try {
					System.out.println("TransactionConsumerAscyn2 接收" + ((TextMessage) message).getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			});
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
