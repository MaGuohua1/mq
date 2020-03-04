package com.ma.rabbitmq.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ma.rabbitmq.adapter.MessageDelegate;
import com.ma.rabbitmq.converter.ImageMessageConverter;
import com.ma.rabbitmq.converter.PDFMessageConverter;
import com.ma.rabbitmq.converter.TextMessageConverter;
import com.ma.rabbitmq.vo.Order;

@Configuration
@ComponentScan({ "com.ma.rabbitmq" })
public class AppConfig {
	@Bean
	public RabbitTemplate tabbitTemplate() {
		return new RabbitTemplate(connectionFactory());
	}

	@Bean
	public SimpleMessageListenerContainer messageListener() {
		SimpleMessageListenerContainer messageListener = new SimpleMessageListenerContainer(connectionFactory());
		messageListener.setQueues(directQueue(), fanoutQueue(), topicQueue());
		messageListener.setConcurrentConsumers(1);// 当前消费者数量
		messageListener.setMaxConcurrentConsumers(5);// 最大消费者数量
		messageListener.setExposeListenerChannel(true);// 设置公开的监听信道
		messageListener.setDefaultRequeueRejected(false);// 是否重回队列
		messageListener.setAcknowledgeMode(AcknowledgeMode.AUTO);// 设置
		/*
		//消费者监听模式
		messageListener.setMessageListener(new ChannelAwareMessageListener() {
			@Override
			public void onMessage(Message message, Channel channel) throws Exception {
				System.out.println("消费者：" + new String(message.getBody()));
			}
		});
		*/

		// 适配器方式，默认方法handleMessage
		MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
		// 指定方法名：consumeMessage
		adapter.setDefaultListenerMethod("consumeMessage");
		// 队列和方法绑定
		Map<String, String> queueOrTagToMethodName = new HashMap<String, String>();
		queueOrTagToMethodName.put("queue1", "method1");
		queueOrTagToMethodName.put("queue2", "method2");
		adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
		
		//=========================================================================
		
		// ext content(全局转换器)
		ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter();
		
		TextMessageConverter textConverter = new TextMessageConverter();
		converter.addDelegate("text", textConverter);
		converter.addDelegate("html/text", textConverter);
		converter.addDelegate("xml/text", textConverter);
		converter.addDelegate("text/plain", textConverter);
		
		//------------------------------------------------------
		// 支持json格式的转换器(支持Java对象转换)
		Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		// 支持java对象多映射转换
		DefaultJackson2JavaTypeMapper mapper = new DefaultJackson2JavaTypeMapper();
		HashMap<String, Class<?>> idClassMapping = new HashMap<String, Class<?>>();
		idClassMapping.put("order", Order.class);
		mapper.setIdClassMapping(idClassMapping);
		jsonConverter.setJavaTypeMapper(mapper);
		//------------------------------------------------------
		
		converter.addDelegate("json", jsonConverter);
		converter.addDelegate("application/json", jsonConverter);
		
		ImageMessageConverter imageConverter = new ImageMessageConverter();
		converter.addDelegate("image/png", imageConverter);
		converter.addDelegate("image", imageConverter);
		
		PDFMessageConverter pdfConverter = new PDFMessageConverter();
		converter.addDelegate("application/pdf", pdfConverter);
		
		adapter.setMessageConverter(converter);
		
		messageListener.setMessageListener(adapter);
		//=========================================================================

		messageListener.setConsumerTagStrategy(new ConsumerTagStrategy() {// 设置消费者标签策略
			@Override
			public String createConsumerTag(String queue) {
				return queue + UUID.randomUUID().toString();
			}
		});// 消费者标签策略
		return messageListener;
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange("direct", true, true);
	}

	@Bean
	public Queue directQueue() {
		return new Queue("queue1", true, false, true, null);
	}

	@Bean
	public Binding bindDirect() {
		return BindingBuilder.bind(directQueue()).to(directExchange()).with("routingKey");
	}

	@Bean
	public FanoutExchange fanoutExchange() {
		return new FanoutExchange("fanout", true, true);
	}

	@Bean
	public Queue fanoutQueue() {
		return new Queue("queue2", true, false, true, null);
	}

	@Bean
	public Binding bindFanout() {
		return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
	}

	@Bean
	public TopicExchange topicExchange() {
		return new TopicExchange("topic", true, true);
	}

	@Bean
	public Queue topicQueue() {
		return new Queue("queue3", true, false, true, null);
	}

	@Bean
	public Binding bindTopic() {
		return BindingBuilder.bind(topicQueue()).to(topicExchange()).with("routingkey");
	}

	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses("192.168.1.104:5672");
		connectionFactory.setVirtualHost("/test");
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");
		return connectionFactory;
	}

	@Bean
	public RabbitAdmin rabbitAdmin() {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());
		rabbitAdmin.setAutoStartup(true);
		return rabbitAdmin;
	}

}
