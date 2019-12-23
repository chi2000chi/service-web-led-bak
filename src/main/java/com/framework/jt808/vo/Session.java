package com.framework.jt808.vo;

import java.net.SocketAddress;

/**
 * 终端会话
 */
public class Session {


	private String ledNo;

	private int currentFlowId = 0;
	// private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	// 客户端上次的连接时间，该值改变的情况:
	// 1. terminal --> server 心跳包
	// 2. terminal --> server 数据包
	private long lastCommunicateTimeStamp = 0l;

	public Session() {
	}


	public static Session buildSession(String ledNo) {
		Session session = new Session();
		session.setLedNo(ledNo);
		session.setLastCommunicateTimeStamp(System.currentTimeMillis());
		return session;
	}

	public long getLastCommunicateTimeStamp() {
		return lastCommunicateTimeStamp;
	}

	public void setLastCommunicateTimeStamp(long lastCommunicateTimeStamp) {
		this.lastCommunicateTimeStamp = lastCommunicateTimeStamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ledNo == null) ? 0 : ledNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Session other = (Session) obj;
		if (ledNo == null) {
			if (other.ledNo != null)
				return false;
		} else if (!ledNo.equals(other.ledNo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Session [ledNo=" + ledNo + "]";
	}

	public  int currentFlowId() {
		if (currentFlowId >= 0xffff)
			currentFlowId = 0;
		return currentFlowId++;
	}


	public String getLedNo() {
		return ledNo;
	}


	public void setLedNo(String ledNo) {
		this.ledNo = ledNo;
	}

}