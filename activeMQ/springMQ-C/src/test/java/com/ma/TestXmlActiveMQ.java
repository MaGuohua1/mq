package com.ma;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestXmlActiveMQ {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	}
}
