package com.framework.jt808.handler;

import com.framework.jt808.vo.Session;

public class BaseMsgProcessService {

	protected SessionManager sessionManager;

	public BaseMsgProcessService() {
		this.sessionManager = SessionManager.getInstance();
	}



	protected int getFlowId(String ledNo, int defaultValue) {
		Session session = this.sessionManager.findBySessionId(ledNo);
		if (session == null) {
			return defaultValue;
		}

		return session.currentFlowId();
	}

	protected synchronized int getFlowId(String ledNo) {
		return this.getFlowId(ledNo, 0);
	}
}
