package com.ma;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ma.queue.QueueProductor;

public class TestXmlActiveMQ {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		QueueProductor queueProducter = (QueueProductor)context.getBean("queueProductor");
		queueProducter.send("test.queue", "this is test of queue");
//		TopicProductor topicProducter = (TopicProductor)context.getBean("topicProductor");
//		topicProducter.send("test.topic", "this is test of topic");
		context.close();
	}

}
