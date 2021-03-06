package com.ma.dlq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class DlqProductor {

	/* 默认的连接用户 */
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;
	/* 默认的连接密码 */
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;
	/* 默认的连接地址 */
	private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	private static final int SUM = 3;

	public static void main(String[] args) {
		/* 连接工厂 */
		ConnectionFactory connectionFactory;
		/* 连接 */
		Connection connection = null;
		/* 会话 */
		Session session;
		/* 消息目的地 */
		Destination destination;
		/* 消息生产者 */
		MessageProducer messageProducer;
		/* 消息 */
		Message message;
		/* 实例化连接工厂 */
		connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKERURL);
		try {
			/* 通过工厂获取连接 */
			connection = connectionFactory.createConnection();
			/* 启动连接 */
			connection.start();
			/* 创建session，第一个参数表示是否启动事务，第二个参数表示是否自动确认 */
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			/* 创建名为helloworld的消息队列 */
//			destination = session.createTopic("hello-world");// 消息广播,一对多，acticeMQ关闭，广播消息消失，没有订阅的客户无法收到消息
			destination = session.createQueue("dlq-queue");// 消息发布，一对一，acticeMQ关闭，消息不丢失
			/* 创建消息生产者 */
			messageProducer = session.createProducer(destination);
			/* 生产者消息持久化设置(默认持久) */
//			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			/* 循环发送消息 */
			for (int i = 0; i < SUM; i++) {
				String msg = "dlq消息" + i + " " + System.currentTimeMillis();
				/* 创建文本消息 */
				message = session.createTextMessage(msg);
				/* 发送消息 */
				messageProducer.send(message);
				System.out.println("发送" + msg);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
