package com.ma.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ma.rabbitmq.config.AppConfig;
import com.ma.rabbitmq.quick.SendMessaage;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		SendMessaage sendMessaage = context.getBean(SendMessaage.class);
		sendMessaage.send();
		context.close();
	}
}
