package com.framework.jt808.thread;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.jt808.vo.PackageData;
import com.framework.jt808.vo.resp.MsgBody0906;

public class Msg0906Thread implements Runnable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	   ConcurrentLinkedQueue<MsgBody0906> msgQueue;
	   LedMsgProcessService msgProcessService;
	   public static boolean runFlg = false;
	    public Msg0906Thread() {
	    }

	    public Msg0906Thread(ConcurrentLinkedQueue<MsgBody0906> msgQueue,LedMsgProcessService msgProcessService) {
	        this.msgQueue = msgQueue;
	        this.msgProcessService = msgProcessService;
	    }
	    
	    MsgBody0906 respMsgBody = null;
	    @Override
		public void run() {	
			try {
				runFlg = true;
				while (true) {
					Thread.sleep(1);
					if(!msgQueue.isEmpty()) {
						respMsgBody = msgQueue.poll();
						dealQueque(respMsgBody);
						respMsgBody = null;
					}	
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 

		}

		void dealQueque(MsgBody0906 respMsgBody) {
			if(respMsgBody != null) {
				try {
					msgProcessService.generateMsg0906Body(respMsgBody.getLedNo(), respMsgBody);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
}
