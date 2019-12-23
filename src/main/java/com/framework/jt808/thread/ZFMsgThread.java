package com.framework.jt808.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.framework.util.DateUtils;
import com.framework.util.GpsDistanceUtils;
import com.framework.webClient.dao.Common808Dao;
import com.framework.webClient.schedulerTask.InstallSocket;
import com.framework.webClient.service.ICommon808Service;
import com.framework.webClient.util.CommonFunc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.entity.output.PlaceOutputEntity;
import com.framework.jt808.common.DataCache;
import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.jt808.vo.BusState;
import com.framework.jt808.vo.PackageData;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.resp.MsgBody0906;

public class ZFMsgThread implements Runnable {
	@Autowired(required = true)
	private ICommon808Service common808Service;
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private Map msgMap;
	private GpsDistanceUtils utils = new GpsDistanceUtils();
	public static volatile  boolean runFlg = true;
	private double  distance = 0;
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public ZFMsgThread(Map busMapTemp) {
		this.msgMap = busMapTemp;
		runFlg = true;
	}

	@Override
	public void run() {
		try {	
			while (runFlg) {
				if (msgMap != null && msgMap.size() > 0 && msgMap.values() != null) {
					long startTime = System.currentTimeMillis();
					Collection<PlaceOutputEntity> valueCollections = msgMap.values();
					List<PlaceOutputEntity> dataList = new LinkedList<PlaceOutputEntity>();
					Iterator<PlaceOutputEntity> iterator = valueCollections.iterator();
					while (iterator.hasNext()) {
						dataList.add(iterator.next());
					}
					logger.info("本次车辆数据:{} ", dataList.size());
					List<BusState> insertList = new LinkedList<BusState>();
					String currTime = df.format(new Date());
					for (PlaceOutputEntity gpsData : dataList) {
						if (gpsData.getDirection() != null && !"".equals(gpsData.getDirection())
								&& gpsData.getBusnumber() != null && !"".equals(gpsData.getBusnumber())
								&& gpsData.getStationno() != null && !"".equals(gpsData.getStationno())
								&& gpsData.getStationdistance() != null && !"".equals(gpsData.getStationdistance())
								&& gpsData.getLinename() != null && !"".equals(gpsData.getLinename())
								&& gpsData.getLatitude() != null && !"".equals(gpsData.getLatitude())
								&& gpsData.getLongitude() != null && !"".equals(gpsData.getLongitude())) {
							//删除超时车辆
							if(gpsData.getGpsdate() != null && !"".equals(gpsData.getGpsdate())
									&& gpsData.getGpstime() != null && !"".equals(gpsData.getGpstime())){
								long dateMethod = dateMethod(currTime,gpsData.getGpsdate()+" " +gpsData.getGpstime());
								if(dateMethod >= 3) {
									logger.info("延时大于3分钟:{},{}",gpsData.getBusname(),gpsData.getBusnumber());
									if(InstallSocket.busMapTemp != null && InstallSocket.busMapTemp.containsKey(gpsData.getBusnumber())) {
										InstallSocket.busMapTemp.remove(gpsData.getBusnumber());
									}
									continue;
								}
							}else {
								InstallSocket.busMapTemp.remove(gpsData.getBusnumber());
								continue;
							}
							
							//使用车载机报站序号   使用dlzbs进行区分，到站是1，离站是2
							if("1".equals(gpsData.getDlzbs()) || "2".equals(gpsData.getDlzbs())) {
								gpsData.setDirection(gpsData.getDirectionBZ());
								if(gpsData.getCjbzno() != null && !"".equals(gpsData.getCjbzno())) {
									gpsData.setStationno(gpsData.getCjbzno());
									gpsData.setNextstationno((Integer.parseInt(gpsData.getCjbzno())+1)+"");
								}
							}
							BusState currentBus = new BusState();
							currentBus.setLineName(gpsData.getLinename());
							currentBus.setBusName(gpsData.getBusname());//自编号

							currentBus.setBusNo(gpsData.getBusnumber());//车牌号
							currentBus.setDirection(gpsData.getDirection());
							currentBus.setLatitude(gpsData.getLatitude());
							currentBus.setLongitude(gpsData.getLongitude());
							//设置车辆拥挤度
							//老的拥挤度 按照车牌号存的
							//Integer congestion =DataCache.yjdMap.get(gpsData.getBusnumber());
							Integer congestion =DataCache.yjdMap.get(gpsData.getBusname());
							currentBus.setCongestion(congestion ==  null ? 1:congestion.intValue());
							//到离站标识(1-到站、2-离站)
							if("1".equals(gpsData.getDlzbs())) {
								if("1".equals(gpsData.getStationno())) {
									currentBus.setCurrentStationNo(1);
									currentBus.setNextStationNo(2);
								}else {
									currentBus.setCurrentStationNo(Integer.parseInt(gpsData.getStationno())-1);
									currentBus.setNextStationNo(Integer.parseInt(gpsData.getNextstationno())-1);
								}				
							}else {
								currentBus.setCurrentStationNo(Integer.parseInt(gpsData.getStationno()));
								if (null == gpsData.getNextstationno() || "".equals(gpsData.getNextstationno())) {
									currentBus.setNextStationNo(currentBus.getCurrentStationNo() + 1);
								} else {
									currentBus.setNextStationNo(Integer.parseInt(gpsData.getNextstationno()));
								}
							}
							StationInfo station = DataCache.stationMap.get(currentBus.getLineName()+"-"+currentBus.getDirection()+"-"+currentBus.getNextStationNo());
							if(station != null && station.getWd() != null && station.getJd() != null && 0 != station.getJxiazjl()) {
								distance = utils.earthDis(Double.valueOf(station.getWd()),Double.valueOf (station.getJd()),Double.valueOf(currentBus.getLatitude()) / 1000 / 1000, Double.valueOf(currentBus.getLongitude()) / 1000 / 1000);
								if(station.getJshangzjl()+Integer.parseInt(DataCache.validDistanceStr) >=distance) {

									currentBus.setSpeed(gpsData.getSpeed());
									currentBus.setGpsdate(gpsData.getGpsdate());
									currentBus.setGpstime(gpsData.getGpstime());
									currentBus.setAngle(gpsData.getAngle());
									currentBus.setHeight(gpsData.getHeight());
									//车辆绑定 下一站的经纬度入日志
									currentBus.setStationJd(station.getWd());
									currentBus.setStationWd(station.getJd());
									//车辆距离下一站距离
									currentBus.setNextDistence(distance);
									currentBus.setNextStationStance((int) distance);
									
									if(distance > Double.valueOf(DataCache.distanceStr)) {
										currentBus.setBusState(BusState.onRoad);
									}else {
										currentBus.setBusState(BusState.arrive);
									}	
									// 下一站距首站距离-偏距本站距离
									currentBus.setToFristStance(station.getJshouzl() - currentBus.getNextStationStance());
									insertList.add(currentBus);	
								}	
								station = null;
							}
						}
					}
					if (DataCache.busQueue != null && !DataCache.busQueue.isEmpty()) {
						DataCache.busQueue.clear();
					}
					logger.info("busQueue车辆数据:{} ", insertList.size());
					DataCache.busQueue.addAll(insertList);
					dataList = null;
					valueCollections = null;
					long endTime = System.currentTimeMillis();
					float excTime = (float) (endTime - startTime) / 1000;
					logger.info("刷新车辆缓存使用时间:{}s", excTime);
				}
				Thread.sleep(3*1000);
			}
		} catch (Exception e) {
			logger.info("ZFMsgThread 异常");
			e.printStackTrace();
		}
	}

	private long dateMethod(String  currTime ,String gpsTime) {
		
		try {
			Date date1 = df.parse(currTime);
			Date date2 = df.parse(gpsTime);
			long diff = date1.getTime() - date2.getTime();
			//计算两个时间之间差了多少分钟
			long minutes = diff / (1000 * 60);
			return minutes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
