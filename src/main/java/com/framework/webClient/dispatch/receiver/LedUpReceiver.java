package com.framework.webClient.dispatch.receiver;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.framework.entity.output.DZOutputEntity;
import com.framework.entity.output.PlaceOutputEntity;
import com.framework.jt808.common.DataCache;
import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.jt808.thread.CommonBackMsgThread;
import com.framework.jt808.thread.CommonMsgThread;
import com.framework.jt808.thread.HeartMsgThread;
import com.framework.jt808.thread.ReBackMsgThread;
import com.framework.jt808.thread.RegisterThread;
import com.framework.jt808.thread.UpLoadParaThread;
import com.framework.jt808.util.HexStringUtils;
import com.framework.jt808.util.JT808ProtocolUtils;
import com.framework.jt808.util.MsgDecoder;
import com.framework.jt808.vo.BusState;
import com.framework.jt808.vo.PackageData;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.PackageData.MsgHeader;
import com.framework.util.AmqpUtils;
import com.framework.util.DateUtils;
import com.framework.webClient.dispatch.sender.LedDownSender;

/***
 * 电子站牌上传数据MQ
 * 
 * @author ly
 *
 */
@Component

public class LedUpReceiver {

	public static ConcurrentLinkedQueue<PackageData> registerMsgQueue = new ConcurrentLinkedQueue<PackageData>();
	public static ConcurrentHashMap<String, PackageData> heartMapTemp = new ConcurrentHashMap<String, PackageData>();

	public static ConcurrentLinkedQueue<PackageData> uploadParaMsgQueue = new ConcurrentLinkedQueue<PackageData>();
	public static ConcurrentLinkedQueue<PackageData> commonBackMsgQueue = new ConcurrentLinkedQueue<PackageData>();
	public static ConcurrentLinkedQueue<PackageData> reBackMsgQueue = new ConcurrentLinkedQueue<PackageData>();

	public static ConcurrentLinkedQueue<PackageData> commonBackInfoMsgQueue = new ConcurrentLinkedQueue<PackageData>();

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private MsgDecoder decoder;
	@Autowired
	private LedMsgProcessService msgProcessService;
	@Autowired
	private JT808ProtocolUtils jt808ProtocolUtil;
	int i = 0;
	// private ExecutorService cachedThreadPool = Executors.newFixedThreadPool(20);

	//@RabbitListener(queues = JT808Consts.led_up_queueName)
	//原来的接收方式
	//public void processDzzpUp(String msg) {
	public void processDzzpUp(byte[] msgAry) {	
		//System.out.println("msg:"+msg);
		//新up q数据方式
		String msg=new String(msgAry);
		//
		 
		try {
			byte[] bytes = HexStringUtils.toBytes(msg);
			// 接收消息时转义
			bytes = this.jt808ProtocolUtil.doEscape4Receive(bytes, 1, bytes.length);
			// 解析消息
			PackageData pkg = this.decoder.bytes2PackageData(bytes);
			if (JT808Consts.msg_id_0x0601 != pkg.getMsgHeader().getMsgId()) {
				reBackMsgQueue.add(pkg);
				if (!ReBackMsgThread.runFlg) {
					ReBackMsgThread dealQueue = new ReBackMsgThread(reBackMsgQueue, msgProcessService);
					// excutorService.execute(dealQueue);
					new Thread(dealQueue).start();
				}
			}
			processPackageData(pkg);
		} catch (Exception e) {
			logger.info("######");
			logger.info("byte[]:"+Arrays.toString(msgAry));
			logger.info("msg:"+msg);
			e.printStackTrace();
			logger.error("method： process error,msg={}!!!", msg, e);
		}
	}
		
	

	/**
	 * 
	 * 处理业务逻辑
	 * 
	 * @param packageData
	 * @throws Exception
	 * 
	 */
	private void processPackageData(PackageData packageData) throws Exception {

		if (JT808Consts.msg_id_0x0602 == packageData.getMsgHeader().getMsgId()) {
			registerMsgQueue.add(packageData);
			if (!RegisterThread.runFlg) {
				RegisterThread dealQueue = new RegisterThread(registerMsgQueue, msgProcessService);
				// excutorService.execute(dealQueue);
				new Thread(dealQueue).start();
			}
		}
		if (JT808Consts.msg_id_0x0603 == packageData.getMsgHeader().getMsgId()) {
			heartMapTemp.put(packageData.getMsgHeader().getLedNo(), packageData);
			if (!HeartMsgThread.runFlg) {
				HeartMsgThread dealQueue = new HeartMsgThread(heartMapTemp, msgProcessService);
				// excutorService.execute(dealQueue);
				new Thread(dealQueue).start();
			}
		}
		if (JT808Consts.msg_id_0x0605 == packageData.getMsgHeader().getMsgId()) {
			uploadParaMsgQueue.add(packageData);
			if (!UpLoadParaThread.runFlg) {
				UpLoadParaThread dealQueue = new UpLoadParaThread(uploadParaMsgQueue, msgProcessService);
				// excutorService.execute(dealQueue);
				new Thread(dealQueue).start();
			}
		}
		if (JT808Consts.msg_id_0x0601 == packageData.getMsgHeader().getMsgId()) {
			commonBackMsgQueue.add(packageData);
			if (!CommonMsgThread.runFlg) {
				CommonMsgThread dealQueue = new CommonMsgThread(commonBackMsgQueue, msgProcessService);
				// excutorService.execute(dealQueue);
				new Thread(dealQueue).start();
			}
		}
		if (JT808Consts.msg_id_0x0607 == packageData.getMsgHeader().getMsgId()) {
			commonBackInfoMsgQueue.add(packageData);
			if (!CommonBackMsgThread.runFlg) {
				CommonBackMsgThread dealQueue = new CommonBackMsgThread(commonBackInfoMsgQueue, msgProcessService);
				new Thread(dealQueue).start();
			}
		}
	}
}
