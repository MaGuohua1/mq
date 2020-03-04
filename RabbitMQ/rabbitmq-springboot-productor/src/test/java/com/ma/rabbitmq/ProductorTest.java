package com.ma.rabbitmq;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ma.rabbitmq.productor.Productor;
import com.ma.rabbitmq.vo.Order;

@SpringBootTest
class ProductorTest {

	@Test
	void contextLoads() {
	}

	@Autowired
	private Productor productor;

	@Test
	public void testSend1() {
		HashMap<String, Object> map = new HashMap<>();
		map.put("num", "1234");
		map.put("time", "2000");
		productor.send("this is a springboot test", map);
	}

	@Test
	public void testSend2() {
		Order order = new Order();
		order.setId(1);
		order.setName("order");
		productor.send(order);
	}

}
