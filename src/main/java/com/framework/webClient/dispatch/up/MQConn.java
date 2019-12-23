package com.framework.webClient.dispatch.up;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

public class MQConn {
	private String host;
	private int port;
	private String username;
	private String password;
	private long connectionTimeout;
	private CachingConnectionFactory.CacheMode cacheMode;
	private int channelCacheSize;
	private long channelCheckoutTimeout;
	private String mqname;

	public String getMqname() {
		return mqname;
	}

	public void setMqname(String mqname) {
		this.mqname = mqname;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public CachingConnectionFactory.CacheMode getCacheMode() {
		return cacheMode;
	}

	public void setCacheMode(CachingConnectionFactory.CacheMode cacheMode) {
		this.cacheMode = cacheMode;
	}

	public int getChannelCacheSize() {
		return channelCacheSize;
	}

	public void setChannelCacheSize(int channelCacheSize) {
		this.channelCacheSize = channelCacheSize;
	}

	public long getChannelCheckoutTimeout() {
		return channelCheckoutTimeout;
	}

	public void setChannelCheckoutTimeout(long channelCheckoutTimeout) {
		this.channelCheckoutTimeout = channelCheckoutTimeout;
	}
}
