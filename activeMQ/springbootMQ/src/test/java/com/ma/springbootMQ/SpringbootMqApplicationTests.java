package com.ma.springbootMQ;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.EnableJms;

import com.ma.springbootMQ.queue.QueueProductor;
import com.ma.springbootMQ.topic.TopicProductor;
@EnableJms
@SpringBootTest
class SpringbootMqApplicationTests {

	@Autowired
	private QueueProductor queueProductor;
	@Autowired
	private TopicProductor topicProductor;

	@Test
	void contextLoads() {
		testTopic();
		testQueue();
	}


	private void testTopic() {
		Destination topic = new ActiveMQTopic("test.topic");
		for (int i = 0; i < 5; i++) {
			topicProductor.send(topic, ("hello worldvvvv" + i).getBytes());
		}
	}
	
	
	public void testQueue() {
		Destination queue = new ActiveMQQueue("test.queue");
		for (int i = 0; i < 5; i++) {
			queueProductor.send(queue, ("hello world" + i).getBytes());
		}
	}

}
