package com.framework.webClient.dispatch.up;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource("classpath:rabbitmq.properties")
public class RabbitConfig {
	// 注入到实体MQConn
	@Bean("mqConn")
	@ConfigurationProperties(prefix = "file.rabbitmq")
	public MQConn fileWorkRabbitFactory() {
		MQConn mqConn = new MQConn();
		return mqConn;
	}

	@Bean("cachingConnectionFactory")
	public CachingConnectionFactory connectionFactory(@Qualifier("mqConn") MQConn mqConnFile) {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
		cachingConnectionFactory.setHost(mqConnFile.getHost());
		cachingConnectionFactory.setPort(mqConnFile.getPort());
		cachingConnectionFactory.setUsername(mqConnFile.getUsername());
		cachingConnectionFactory.setPassword(mqConnFile.getPassword());
		cachingConnectionFactory.setVirtualHost("/");
		cachingConnectionFactory.setPublisherConfirms(true);
		if (mqConnFile.getCacheMode() != null) {
			cachingConnectionFactory.setCacheMode(mqConnFile.getCacheMode());
		}
		if (mqConnFile.getChannelCacheSize() > 0) {
			cachingConnectionFactory.setChannelCacheSize(mqConnFile.getChannelCacheSize());
		}
		if (mqConnFile.getChannelCheckoutTimeout() > 0) {
			cachingConnectionFactory.setChannelCheckoutTimeout(mqConnFile.getChannelCheckoutTimeout());
		}
		return cachingConnectionFactory;
	}

	@Bean
	@Scope("prototype")
	public MessageListener msgReceiver() {
		return new MessageListener();
	}

	/**
	 * 创建监听器，监听队列
	 * 
	 * @param packMsgReceiver 监听方法
	 * @return 监听器
	 */
	@Bean
	public SimpleMessageListenerContainer messageListenerContainer(MessageListener msgReceiver,
			@Qualifier("cachingConnectionFactory") CachingConnectionFactory cachingConnectionFactory/*,@Qualifier("mqConn") MQConn mqConnFile*/) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cachingConnectionFactory);
		// 监听队列的名称
		//从配置文件取mq名称
		/*String mqName=mqConnFile.getMqname();// q_wifikl_dzzp
		container.setQueueNames(mqName);*/
		container.setQueueNames("q_wifikl_dzzp");
		container.setExposeListenerChannel(true);
		// 设置每个消费者获取的最大消息数量
		//container.setPrefetchCount(100);
		// 消费者的个数
		container.setConcurrentConsumers(1);
		// 设置确认模式为手工确认
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		container.setMessageListener(msgReceiver);
		return container;
	}

}
