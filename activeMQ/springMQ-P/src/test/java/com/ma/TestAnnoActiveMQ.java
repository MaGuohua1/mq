package com.ma;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ma.config.AppConfig;
import com.ma.queue.QueueProductor;
import com.ma.topic.TopicProductor;

public class TestAnnoActiveMQ {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		QueueProductor queueProducter = (QueueProductor)context.getBean("queueProductor");
		queueProducter.send("test.queue", "this is test of queue");
		TopicProductor topicProducter = (TopicProductor)context.getBean("topicProductor");
		topicProducter.send("test.topic", "this is test of topic");
		context.close();
	}

}
