package com.framework.jt808.thread;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.jt808.vo.PackageData;

public class UpLoadParaThread implements Runnable {
	ConcurrentLinkedQueue<PackageData> msgQueue;
	LedMsgProcessService msgProcessService;
	public static ReentrantLock dealLock = new ReentrantLock(true);
	public static boolean runFlg = false;
	public UpLoadParaThread() {
	}

	public UpLoadParaThread(ConcurrentLinkedQueue<PackageData> msgQueue, LedMsgProcessService msgProcessService) {
		this.msgQueue = msgQueue;
		this.msgProcessService = msgProcessService;
	}

	PackageData packageData= null;
	@Override
	public void run() {
	
		try {
			runFlg = true;
			while (true) {
				Thread.sleep(1); 
				if(!msgQueue.isEmpty()) {
					packageData = msgQueue.poll();
					dealQueque(packageData);
					packageData = null;
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

	void dealQueque(PackageData packageData) {
		if(packageData != null) {
			try {
				msgProcessService.processUploadPara(packageData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
