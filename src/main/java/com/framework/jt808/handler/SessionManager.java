package com.framework.jt808.handler;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import com.framework.jt808.vo.Session;

public class SessionManager {

	private static volatile SessionManager instance = null;
	private Map<String, Session> sessionIdMap;
	private Map<String, String> ledMap;

	public static SessionManager getInstance() {
		if (instance == null) {
			synchronized (SessionManager.class) {
				if (instance == null) {
					instance = new SessionManager();
				}
			}
		}
		return instance;
	}

	public SessionManager() {
		this.sessionIdMap = new ConcurrentHashMap<>();
		this.ledMap = new ConcurrentHashMap<>();
	}

	public boolean containsKey(String sessionId) {
		return sessionIdMap.containsKey(sessionId);
	}

	public boolean containsSession(Session session) {
		return sessionIdMap.containsValue(session);
	}

	public synchronized Session findBySessionId(String id) {
		return sessionIdMap.get(id);
	}

	public Session findByLedNo(String ledNo) {
		String sessionId = this.ledMap.get(ledNo);
		if (sessionId == null)
			return null;
		return this.findBySessionId(sessionId);
	}

	public synchronized  Session put(String key, Session value) {
		if (value.getLedNo() != null && !"".equals(value.getLedNo().trim())) {
			this.ledMap.put(value.getLedNo(), value.getLedNo());
		}
		return sessionIdMap.put(key, value);
	}

	public  synchronized  Session removeBySessionId(String sessionId) {
		if (sessionId == null)
			return null;
		Session session = sessionIdMap.remove(sessionId);
		if (session == null)
			return null;
		if (session.getLedNo() != null)
			this.ledMap.remove(session.getLedNo());
		return session;
	}

	public Set<String> keySet() {
		return sessionIdMap.keySet();
	}

	public void forEach(BiConsumer<? super String, ? super Session> action) {
		sessionIdMap.forEach(action);
	}

	public Set<Entry<String, Session>> entrySet() {
		return sessionIdMap.entrySet();
	}

	public List<Session> toList() {
		return this.sessionIdMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
	}

}