package com.ma.durableMQ;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 
 * @author mgh_2
 *
 * @desription topic消息持久化订阅
 */
public class DurableConsumerAscyn {
	/* 默认的连接用户 */
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	/* 默认的连接密码 */
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	/* 默认的连接地址 */
	private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	private static final String CLIENT_ID = "leson";

	public static void main(String[] args) {
		/* 连接工厂 */
		ConnectionFactory connectionFactory;
		/* 连接 */
		Connection connection = null;
		/* 会话 */
		Session session = null;
		/* 消息目的地 */
		Topic destination;
		/* 消息消费者 */
		TopicSubscriber consumer = null;
		/* 实例化连接工厂 */
		connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKERURL);
		try {
			/* 通过工厂获取连接 */
			connection = connectionFactory.createConnection();
			/* 设置客户端id */
			connection.setClientID(CLIENT_ID);
			/* 启动连接 */
			connection.start();
			/* 创建session,第一个参数表示是否启动事务，第二个参数表示是否自动确认 */
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			/* 创建名为helloworld的消息队列 */
			destination = session.createTopic("hello-world");// 消息广播,一对多，acticeMQ关闭，广播消息消失，没有订阅的客户无法收到消息
//			destination = session.createQueue("hello-world-queue");//消息发布，一对一，acticeMQ关闭，消息不丢失
			/* 创建消息消费者 */
			consumer = session.createDurableSubscriber(destination, CLIENT_ID);
			/* 设置消费者监听器，监听消息 */
			consumer.setMessageListener(message -> {// 异步获取
				try {
					System.out.println("接收" + ((TextMessage) message).getText());
//					message.acknowledge();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			});
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
//			try {
//				connection.close();
//			} catch (JMSException e) {
//				e.printStackTrace();
//			}
		}
	}
}
