package com.framework.webClient.schedulerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.framework.entity.input.HeartbeatEntity;
import com.framework.entity.input.UserLoginEntity;
import com.framework.entity.output.DZOutputEntity;
import com.framework.entity.output.KLOutputEntity;
import com.framework.entity.output.LZOutPutEntity;
import com.framework.entity.output.PlaceOutputEntity;
import com.framework.entity.output.PlanToStartOutputEntity;
import com.framework.jt808.common.DataCache;
import com.framework.jt808.thread.ReBackMsgThread;
import com.framework.jt808.thread.ZFMsgThread;
import com.framework.jt808.vo.BusState;
import com.framework.jt808.vo.StationInfo;
import com.framework.redis.SerializeUtil;
import com.framework.util.DateUtils;
import com.framework.webClient.service.ICommon808Service;
import com.framework.webClient.util.CommonFunc;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class InstallSocket extends Thread {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private ICommon808Service common808Service;
	InputStream inputStream = null;
	public static ConcurrentHashMap<String, PlaceOutputEntity> busMapTemp = new ConcurrentHashMap<String, PlaceOutputEntity>();
	public static Socket socket;
	private String serverIp;

	private String serverPort;

	private String loginUser;

	private String loginPassWord;
	public static volatile  boolean runFlg = true;
	public InstallSocket(ICommon808Service common808Service) {
		this.common808Service = common808Service;
		runFlg = true;
	}

	public void init() throws IOException {
		serverIp = CommonFunc.getConfigString("forward_socket_server_ip");
		serverPort = CommonFunc.getConfigString("forward_socket_server_port");
		loginUser = CommonFunc.getConfigString("forward_socket_login_user");
		loginPassWord = CommonFunc.getConfigString("forward_socket_login_password");
		connect();
		refreshTimer();
		ZFMsgThread dealQueue = new ZFMsgThread(busMapTemp);
		new Thread(dealQueue).start();
	}

	public void connect() throws IOException {
		socket = new Socket();
		socket.connect(new InetSocketAddress(serverIp, Integer.parseInt(serverPort)));
		UserLoginEntity ul = new UserLoginEntity();
		ul.setUserame(loginUser);
		ul.setPasswd(loginPassWord);
		socket.getOutputStream().write(SerializeUtil.serialize(ul));
		inputStream = socket.getInputStream();

	}

	public void refreshTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					if (runFlg) {
						HeartbeatEntity heartbeatEntity = new HeartbeatEntity();
						heartbeatEntity.setTimestamp(String.valueOf(System.currentTimeMillis()));
						logger.info("发送心跳");
						if (socket == null || !socket.isConnected() || socket.isClosed()) {
							connect();
						}
						socket.getOutputStream().write(SerializeUtil.serialize(heartbeatEntity));
					}else {
						timer.cancel();
						socket.close();	
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					socket = null;
				}
			}
		}, 0, 1000 * 30);
	}

	public void run() {
		logger.info("开始执行线程");
		while (runFlg) {
			try {
				if (socket == null || !socket.isConnected() || socket.isClosed())
					connect();
				// logger.info("接收数据{}");
				ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				Object object = objectInputStream.readObject();
				// gps
				if (object instanceof PlaceOutputEntity) {
					// common808Service.insertGPSLog((PlaceOutputEntity) object);
					// 数据类型转换
					PlaceOutputEntity gpsData = (PlaceOutputEntity) object;
						if(gpsData.getLinename()!= null && "56".equals(gpsData.getLinename())) {
							busMapTemp.put(gpsData.getBusnumber(), gpsData);
						}else {
							if (gpsData.getBusstatus() != null && "0".equals(gpsData.getBusstatus())) {
								// 代表车在终点，可以删除数据
								busMapTemp.put(gpsData.getBusnumber(), gpsData);
							}
						}
				}
				// 客流
				if (object instanceof KLOutputEntity) {
					// 数据类型转换
					KLOutputEntity klData = (KLOutputEntity) object;
					// 老拥挤度 用车牌号存的
					//if ( klData.getCongestion()!= null &&  !"".equals(klData.getCongestion())&& klData.getBusnumber() != null && !"".equals(klData.getBusnumber())) {
					if ( klData.getCongestion()!= null &&  !"".equals(klData.getCongestion())&& klData.getBusname() != null && !"".equals(klData.getBusname())) {
						/*String busname1=klData.getBusname();
						String congestion1= klData.getCongestion();
						System.out.println("busname:"+busname1);
						System.out.println("congestion:"+congestion1);*/
						Integer congestion = Integer.parseInt(klData.getCongestion()) - 1;
						// 老拥挤度 用车牌号存的
						//DataCache.yjdMap.put(klData.getBusnumber(), congestion);
						DataCache.yjdMap.put(klData.getBusname(), congestion);
					}
				}
				// 计划发车
				if (object instanceof PlanToStartOutputEntity) {
					// 数据类型转换
					PlanToStartOutputEntity data = (PlanToStartOutputEntity) object;
					DataCache.readySendBusMap.put(data.getLinename() + "-" + data.getDirection(), data.getEntitys());
				}
			} catch (Exception e) {
				logger.error("转发服务异常", e);
				socket = null;
				// ZFMsgThread.runFlg = false;
			}
		}
	}

	/**
	 * 得到当前时间 yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @return 当前时间
	 */
	private String currTime() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currTime = df.format(date);
		return currTime;
	}

	/**
	 * 计算时间差（单位：分钟）
	 * 
	 * @param lastReceiveTime
	 * @return
	 */
	private long dateMethod(String gpsTime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date1 = df.parse(currTime());
			Date date2 = df.parse(gpsTime);
			long diff = date1.getTime() - date2.getTime();
			// 计算两个时间之间差了多少分钟
			long minutes = diff / (1000 * 60);
			return minutes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
