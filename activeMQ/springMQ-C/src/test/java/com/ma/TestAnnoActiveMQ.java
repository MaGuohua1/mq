package com.ma;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ma.config.AppConfig;

public class TestAnnoActiveMQ {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new AnnotationConfigApplicationContext(AppConfig.class);
	}
}
