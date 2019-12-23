package com.framework.jt808.thread;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.entity.output.PlaceOutputEntity;
import com.framework.jt808.common.DataCache;
import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.jt808.vo.BusState;
import com.framework.jt808.vo.PackageData;
import com.framework.webClient.dispatch.receiver.LedUpReceiver;

public class HeartMsgThread implements Runnable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map heartMap;
	private LedMsgProcessService msgProcessService;
	public static boolean runFlg = false;
	private List<PackageData> dataList;
	private Collection<PackageData> valueCollections= null;
	public HeartMsgThread() {
		 
	}

	public HeartMsgThread(Map heartMapTemp, LedMsgProcessService msgProcessService) {
		heartMap = heartMapTemp;
		this.msgProcessService = msgProcessService;
		dataList = new LinkedList<PackageData>();
	}

	@Override
	public void run() {
		try {
			runFlg = true;
			while (true) {
				if (heartMap != null && heartMap.size() > 0 && heartMap.values() != null) {
					long startTime = System.currentTimeMillis();
					valueCollections  = heartMap.values();				
					Iterator<PackageData> iterator = valueCollections.iterator();
					while (iterator.hasNext()) {
						dataList.add(iterator.next());
					}
					for (PackageData gpsData : dataList) {
						msgProcessService.processHeartMsg(gpsData);
					}
					dataList.clear();
					valueCollections = null;
					long endTime = System.currentTimeMillis();
					float excTime = (float) (endTime - startTime) / 1000;
					logger.info("心跳存储操作时间:{}s ", excTime);
					LedUpReceiver.heartMapTemp.clear();
				}
				Thread.sleep(1000 * 60 * 3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
