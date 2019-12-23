package com.framework.webClient.schedulerTask;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.framework.entity.input.HeartbeatEntity;
import com.framework.entity.output.PlaceOutputEntity;
import com.framework.entity.output.PlanToStartEntity;
import com.framework.jt808.common.DataCache;
import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.jt808.thread.Msg0906Thread;
import com.framework.jt808.vo.BusState;
import com.framework.jt808.vo.Session;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.resp.MsgBody0906;
import com.framework.redis.SerializeUtil;
import com.framework.util.DateUtils;
import com.framework.util.GpsDistanceUtils;
import com.framework.webClient.service.ICommon808Service;
import com.framework.webClient.service.ILedMsgManageService;
import com.framework.webClient.util.CommonFunc;

@Component
@Order(1)
public class DataRefreshRunner implements ApplicationRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static ConcurrentLinkedQueue<MsgBody0906> msg0906Queue = new ConcurrentLinkedQueue<MsgBody0906>();
	@Autowired
	private LedMsgProcessService msgProcessService;
	@Autowired(required = true)
	private ICommon808Service common808Service;
	@Autowired
	private ILedMsgManageService ledMsgManageService;
	public  ConcurrentHashMap<String, String> lineBusStateMap = new ConcurrentHashMap<String, String>();
	public  ConcurrentHashMap<String, List<BusState>> lineBusMap = new ConcurrentHashMap<String, List<BusState>>();


	int count = 0;
	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		
		DataCache.ledQueue.addAll(common808Service.selectLedAndLineInfo(new HashMap()));
		DataCache.stationQueue.addAll(common808Service.selectStaionListAll());
		if(DataCache.stationQueue != null && !DataCache.stationQueue.isEmpty()) {
			DataCache.stationMap.clear();
			for (StationInfo station : DataCache.stationQueue) {
				DataCache.stationMap.put(station.getXlmc()+"-"+station.getFx()+"-"+station.getZdxh(), station);
	        }
		}
		DataCache.lastGetStationDateTime = common808Service.getLastGetStationDateTime();
		String unShowLineStr = common808Service.getUnShowLine();
		if (unShowLineStr != null && !"".equals(unShowLineStr.trim())) {
			DataCache.unShowLineList = Arrays.asList(unShowLineStr.split(","));
		}
		DataCache.ledMsgList = ledMsgManageService.selectLedMsgInfo();
		//车辆距离下一站距离大于此参数即为进站
		DataCache.distanceStr=common808Service.getDistance();
		//车辆距离下一站距离 < 站间距离+ 此距离 才为合法车辆
		DataCache.validDistanceStr=common808Service.getValidDistance();
		DataCache.safeCodeQueue.addAll(common808Service.selectSafeCodeList());
		
		// 整理高德跟线路数据 到缓存 list
		arrangeGaodeList();
		
		//转发服务线程
		InstallSocket install = new InstallSocket(common808Service);
		install.init();
		install.start();
		
		//定时下发线程
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					lineBusStateMap.clear();
					lineBusMap.clear();
					List<Map<String, Object>> ledList  =DataCache.ledQueue.stream().collect(Collectors.toList());
					String dateTimeNow = DateUtils.getTimeFormat(new Date());
					threadLedSend(ledList, 250,dateTimeNow);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("0906process异常", e);
				}
			}
		//}, 1000 * 4, 1000 * 8); 
		}, 1000 * 4, 1000 * 10); 
		
		
	
	}

	private void threadLedSend(List<Map<String, Object>> ledList, int threadSize,String dateTimeNow) {
		if(ledList == null || ledList.size() == 0 ) return ;
        // 总数据条数
        int dataSize = ledList.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++) {
			final  List<Map<String, Object>> subList;
			 if (i == threadNum - 1) {
	                if (special) {
	                    break;
	                }
	                subList = ledList.subList(threadSize * i, dataSize);
	            } else {
	            	subList = ledList.subList(threadSize * i, threadSize * (i + 1));
	        }
			 executorService.execute(new Runnable() {
				@Override
				public void run() {
					try {						
						for (Map<String, Object> dataMap : subList) {
								
							if (dataMap.get("xlbh") != null 
									&& dataMap.get("ledbh") != null && dataMap.get("xlmc") != null
									&& dataMap.get("fx") != null && dataMap.get("dqzx") != null) {
								if ((DataCache.unShowLineList != null && DataCache.unShowLineList.size() >0 && dataMap.get("xlbh") != null && DataCache.unShowLineList.contains((String) dataMap.get("xlbh"))) ||(dataMap.get("zdtk") != null && "1".equals((String) dataMap.get("zdtk"))))
									continue;
								// 每一个电子站牌的数据
								String ledNo = dataMap.get("ledbh").toString();
								String lineName = dataMap.get("xlmc").toString();
								String fx = dataMap.get("fx").toString();
								int stationNo = Integer.parseInt(dataMap.get("dqzx").toString());
								int lineNo = Integer.parseInt(dataMap.get("xlbh").toString());
								//Session session = msgProcessService.sessionManager.findBySessionId(ledNo);
								//if (session !=null) {
								if (true) {	
									List<BusState> allBusList = lineBusMap.get(lineName+"-"+fx);
									if(allBusList == null || allBusList.size() ==0) {
										allBusList = DataCache.busQueue.stream()
												.filter(p -> p.getLineName().equals(lineName) && p.getDirection().equals(fx))
												.collect(Collectors.toList());
										lineBusMap.put(lineName+"-"+fx,allBusList);
									}
									
									// 当期电子站牌对应线路的所有车辆数据
									if (allBusList != null && allBusList.size() > 0) {
										// lineBusStateMap 是为了 取 busstate字段，下发数据里的一个字段
										List<BusState> busListClone = CommonFunc.depCopy(allBusList);
										//当前线路车辆列表
										String busState = lineBusStateMap.get(lineName+"-"+fx);
										if(busState == null || "".equals(busState)){
											//车辆距离首站距离从小到大排序
											Collections.sort(busListClone, (a, b) -> a.getToFristStance()-b.getToFristStance());
											StringBuilder str = new StringBuilder();
											//当前线路车辆列表
											busListClone.forEach(item->str.append(getStateStr(item)).append(";"));
											busState = str.toString();
											lineBusStateMap.put(lineName+"-"+fx,str.toString());
										}
										//logger.info("线路车辆数据:{}", busState);
										List<BusState> busList = busListClone.stream()
												.filter(p -> p.getNextStationNo() <= stationNo)
												.collect(Collectors.toList());
										//// 取上次发送的要跟这次发送数据对比的  key
										String previousCarKey=ledNo+"-"+lineNo;
										if (busList != null && busList.size() > 0) {	
											//按照车辆驶离首站距离,从大到小排序
											Collections.sort(busList, (a, b) -> b.getToFristStance()-a.getToFristStance());
											
											StationInfo station = DataCache.stationMap.get(lineName+"-"+fx+"-"+stationNo);
											if(station != null) {
												MsgBody0906 respMsgBody = new MsgBody0906();
												
												// 线路编号
												respMsgBody.setLineNo(lineNo);
												// 线路名称长度
												respMsgBody.setLineNameLength(lineName.getBytes(JT808Consts.string_encoding).length + 1);
												// 线路名称
												respMsgBody.setLineName(lineName);
												// 当前线路车辆数
												respMsgBody.setBusCount(busListClone.size());
												if(busList.size() > 0) {
													BusState state = busList.get(0);
													respMsgBody.setArriveDistance1(station.getJshouzl()-state.getToFristStance());	
													respMsgBody.setArriveStationCount1(stationNo-state.getCurrentStationNo());
													// 取到站时间
													String xlbhStr=dataMap.get("xlbh").toString();//取出线路编号 str 格式
													String xlbhStandard=com.framework.webClient.util.StringUtils.addZeroForNum(xlbhStr, 6);//不足六位数，线路id前补0
													String zdbh=dataMap.get("zdbh").toString();//取出站点编号 str 格式
													String stationKey=xlbhStandard+"-"+fx+"-"+zdbh;// 缓存里取到这个站点前车辆的时间的key
													//System.out.println("stationKey:"+stationKey);
													if(DataCache.dzTimeMap.containsKey(stationKey)) {// 缓存里有这个线路的信息
														
														Map dzMap= DataCache.dzTimeMap.get(stationKey);//取出到这个站点的 车辆 到站时间 map
														System.out.println("dzMap:"+dzMap);
														//用车辆自编号去map 里取到站时间
														String busName=state.getBusName();
														System.out.println("busName:"+busName);
														Object dzTime =dzMap.get(busName);//到站时间
														System.out.println("dzTime:"+dzTime);
														if(dzTime == null) {
															logger.info("no car match dzTime");
															logger.info("stationKey:"+stationKey);
															logger.info("ledNo:"+ledNo);
															logger.info("busNoTime:"+busName);
															respMsgBody.setArriveTime1(999999);
														}else {
															int dzTimeCar=(Integer)dzTime;
															respMsgBody.setArriveTime1(dzTimeCar);
														}
													}else {
														logger.info("no car match stationKey");
														logger.info("stationKey:"+stationKey);
														respMsgBody.setArriveTime1(0);
													}
													//todo lijiepeng 
													/*if(StringUtils.isNotBlank(state.getSpeed()) && 0 != Integer.parseInt(new java.text.DecimalFormat("0").format(Double.parseDouble(state.getSpeed())))) {
														//respMsgBody.setArriveTime1(respMsgBody.getArriveStationCount1() * 5);	
														respMsgBody.setArriveTime1(0);
													}else {
														//respMsgBody.setArriveTime1(respMsgBody.getArriveStationCount1() * 5);	
														respMsgBody.setArriveTime1(0);
													}	*/	
													respMsgBody.setArriveYJD1(state.getCongestion());
													if(stationNo != state.getNextStationNo()) {
															respMsgBody.setArriveBusState1(4);	
													}else {
														if (BusState.onRoad.equals(state.getBusState())) {
															respMsgBody.setArriveBusState1(4);	
														}else {
															respMsgBody.setArriveBusState1(0);	
														}												
													}
													respMsgBody.setGpstime(state.getGpstime());
													respMsgBody.setArriveBusNo1(state.getBusNo());
													respMsgBody.setArriveBusNo1Length(respMsgBody.getArriveBusNo1().getBytes(JT808Consts.string_encoding).length + 1);
													//最近的车距离下一站距离
													respMsgBody.setNextDistence(state.getNextDistence());
													//车辆的经纬度
													respMsgBody.setBusJD(state.getLongitude());//经度
													respMsgBody.setBusWd(state.getLatitude());//维度
													//车辆要到达的下一个站点的经纬度
													respMsgBody.setNextStationJd(state.getStationJd());//经度
													respMsgBody.setNextStationWd(state.getStationWd());//维度
												}else {
													respMsgBody.setArriveDistance1(0);
													respMsgBody.setArriveTime1(0);
													respMsgBody.setArriveStationCount1(0);
													respMsgBody.setArriveYJD1(0);
													respMsgBody.setArriveBusState1(3);	
													respMsgBody.setArriveBusNo1("00");
													respMsgBody.setArriveBusNo1Length(respMsgBody.getArriveBusNo1().getBytes(JT808Consts.string_encoding).length + 1);
												
												}
												if(busList.size() > 1) {
													BusState state = busList.get(1);
													respMsgBody.setArriveDistance2(station.getJshouzl()-state.getToFristStance());	
													respMsgBody.setArriveStationCount2(stationNo-state.getCurrentStationNo());
													//respMsgBody.setArriveTime2(respMsgBody.getArriveStationCount2() * 5);																		
													respMsgBody.setArriveTime2(0);
													respMsgBody.setArriveYJD2(state.getCongestion());
													if (stationNo != state.getNextStationNo()) {
														respMsgBody.setArriveBusState2(4);
													} else {
														if (BusState.onRoad.equals(state.getBusState())) {
															respMsgBody.setArriveBusState2(4);
														} else {
															respMsgBody.setArriveBusState2(0);
														}
													}								
													respMsgBody.setArriveBusNo2(state.getBusNo());
													respMsgBody.setArriveBusNo2Length(respMsgBody.getArriveBusNo2().getBytes(JT808Consts.string_encoding).length + 1);
												
												}else {
													respMsgBody.setArriveDistance2(0);
													respMsgBody.setArriveTime2(0);
													respMsgBody.setArriveStationCount2(0);
													respMsgBody.setArriveYJD2(0);
													respMsgBody.setArriveBusState2(3);
													respMsgBody.setArriveBusNo2("00");
													respMsgBody.setArriveBusNo2Length(respMsgBody.getArriveBusNo2().getBytes(JT808Consts.string_encoding).length + 1);
												}
												if(busList.size() > 2){
													BusState state = busList.get(2);
													respMsgBody.setArriveDistance3(station.getJshouzl()-state.getToFristStance());	
													respMsgBody.setArriveStationCount3(stationNo-state.getCurrentStationNo());
													//respMsgBody.setArriveTime3(respMsgBody.getArriveStationCount3() * 5);																											
													respMsgBody.setArriveTime3(0);
													respMsgBody.setArriveYJD3(state.getCongestion());
													if (stationNo != state.getNextStationNo()) {
														respMsgBody.setArriveBusState3(4);
													} else {
														if (BusState.onRoad.equals(state.getBusState())) {
															respMsgBody.setArriveBusState3(4);	
														}else {
															respMsgBody.setArriveBusState3(0);	
														}
													}	
													respMsgBody.setArriveBusNo3(state.getBusNo());
													respMsgBody.setArriveBusNo3Length(respMsgBody.getArriveBusNo3().getBytes(JT808Consts.string_encoding).length + 1);
												
												}else {
													respMsgBody.setArriveDistance3(0);
													respMsgBody.setArriveTime3(0);
													respMsgBody.setArriveStationCount3(0);
													respMsgBody.setArriveYJD3(0);
													respMsgBody.setArriveBusState3(3);
													respMsgBody.setArriveBusNo3("00");
													respMsgBody.setArriveBusNo3Length(respMsgBody.getArriveBusNo3().getBytes(JT808Consts.string_encoding).length + 1);
												
												}								
												// 末班车状态 todo
												respMsgBody.setLastBusState(0);
												List<PlanToStartEntity> datalist = (List<PlanToStartEntity>) DataCache.readySendBusMap.get(lineName+"-"+fx);
//												if(datalist != null) {
//													Collections.sort(datalist, new Comparator<PlanToStartEntity>() {
//											            public int compare(PlanToStartEntity a, PlanToStartEntity b) {
//											             return a.getTimeplane().compareTo(b.getTimeplane());
//											         }});
//													// 最近一趟待发车发车时间  todo数据值不对
//													Date date =DateUtils.formatDate(datalist.get(0).getTimeplane(), DateUtils.DATETIME_DEFAULT_FORMAT);
//													respMsgBody.setNextFCSJ(DateUtils.getTimeLiteFormat(date).replace(":", ""));
//												}else {
													respMsgBody.setLastBusState(1);
													respMsgBody.setNextFCSJ("0000");
												//}
												// 车辆实时位置信息列表长度
												respMsgBody.setBusStateLength(busState.getBytes(JT808Consts.string_encoding).length + 1);
												// 车辆实时位置信息列表
												respMsgBody.setBusState(busState);
												respMsgBody.setFx(fx);
												respMsgBody.setLedNo(ledNo);
												respMsgBody.setStationNo(stationNo);
												respMsgBody.setStartTime(dateTimeNow);
												//// 对比上次发送的数据，一样的就不发
												//这次打算要发的数据
												String thisCarData=respMsgBody.toIsSameString();
												if(!DataCache.previousCarMap.containsKey(previousCarKey)) {// map 里没有这个key，说明是第一次发送这个站牌这条线路数据，下发数据
													logger.info("map no key:"+lineNo);
													//logger.info("thisCarData:"+thisCarData);
													msgProcessService.generateMsg0906Body(ledNo, respMsgBody);
													DataCache.previousCarMap.put(previousCarKey, respMsgBody);//发送完数据，更新map
													//DataCache.previousCarMap.put(previousCarKey, thisCarData);//发送完数据，更新map
												}else {// 这个站牌这条线路已经发送过数据了
													//String previousCarData =DataCache.previousCarMap.get(previousCarKey);// 这个站牌这条线路上次发送的数据
													MsgBody0906 previousCarData0906 =DataCache.previousCarMap.get(previousCarKey);//存上次发的实体类
													//logger.info("thisCarData:"+thisCarData);
													String previousCarData=previousCarData0906.toIsSameString();
													//logger.info("previousCarData:"+previousCarData);
													if(!thisCarData.equals(previousCarData)) {// 这次发送的数据跟上次不一样
														msgProcessService.generateMsg0906Body(ledNo, respMsgBody);
														DataCache.previousCarMap.put(previousCarKey, respMsgBody);//发送完数据，更新map
														//DataCache.previousCarMap.put(previousCarKey, thisCarData);//发送完数据，更新map
													}
												}
												////
												//msgProcessService.generateMsg0906Body(ledNo, respMsgBody);
												////////msg0906Queue.add(respMsgBody);
											}
										}else {  
											MsgBody0906 respMsgBody = new MsgBody0906();
											// 线路编号
											respMsgBody.setLineNo(lineNo);
											// 线路名称长度
											respMsgBody.setLineNameLength(lineName.getBytes(JT808Consts.string_encoding).length + 1);
											// 线路名称
											respMsgBody.setLineName(lineName);
											// 当前线路车辆数
											respMsgBody.setBusCount(busListClone.size());
											respMsgBody.setArriveDistance1(0);
											respMsgBody.setArriveTime1(0);
											respMsgBody.setArriveStationCount1(0);
											respMsgBody.setArriveYJD1(0);
											respMsgBody.setArriveBusState1(3);	
											respMsgBody.setArriveBusNo1("00");
											respMsgBody.setArriveBusNo1Length(respMsgBody.getArriveBusNo1().getBytes(JT808Consts.string_encoding).length + 1);
										
											respMsgBody.setArriveDistance2(0);
											respMsgBody.setArriveTime2(0);
											respMsgBody.setArriveStationCount2(0);
											respMsgBody.setArriveYJD2(0);
											respMsgBody.setArriveBusState2(3);
											respMsgBody.setArriveBusNo2("00");
											respMsgBody.setArriveBusNo2Length(respMsgBody.getArriveBusNo2().getBytes(JT808Consts.string_encoding).length + 1);
										
											respMsgBody.setArriveDistance3(0);
											respMsgBody.setArriveTime3(0);
											respMsgBody.setArriveStationCount3(0);
											respMsgBody.setArriveYJD3(0);
											respMsgBody.setArriveBusState3(3);
											respMsgBody.setArriveBusNo3("00");
											respMsgBody.setArriveBusNo3Length(respMsgBody.getArriveBusNo3().getBytes(JT808Consts.string_encoding).length + 1);
											//最近的车距离下一站距离
											respMsgBody.setNextDistence(0D);
											//车辆的经度
											respMsgBody.setBusJD("0");
											//车辆的维度
											respMsgBody.setBusWd("0");
											//车辆开往的下一个站点的经度
											respMsgBody.setNextStationJd("0");
											//车辆开往的下一个站点的维度
											respMsgBody.setNextStationWd("0");
											// 末班车状态 todo
											respMsgBody.setLastBusState(0);
//											List<PlanToStartEntity> datalist = (List<PlanToStartEntity>) DataCache.readySendBusMap.get(lineName+"-"+fx);
//											if(datalist != null) {
//												Collections.sort(datalist, new Comparator<PlanToStartEntity>() {
//										            public int compare(PlanToStartEntity a, PlanToStartEntity b) {
//										             return a.getTimeplane().compareTo(b.getTimeplane());
//										         }});
//												// 最近一趟待发车发车时间  todo数据值不对
//												Date date =DateUtils.formatDate(datalist.get(0).getTimeplane(), DateUtils.DATETIME_DEFAULT_FORMAT);
//												respMsgBody.setNextFCSJ(DateUtils.getTimeLiteFormat(date).replace(":", ""));
//											}else {
												respMsgBody.setLastBusState(1);
												respMsgBody.setNextFCSJ("0000");
											//}
											// 车辆实时位置信息列表长度
											respMsgBody.setBusStateLength(busState.getBytes(JT808Consts.string_encoding).length + 1);
											// 车辆实时位置信息列表
											respMsgBody.setBusState(busState);
											respMsgBody.setFx(fx);
											respMsgBody.setLedNo(ledNo);
											respMsgBody.setStationNo(stationNo);
											respMsgBody.setStartTime(dateTimeNow);
											//// 对比上次发送的数据，一样的就不发
											//这次打算要发的数据
											String thisCarData=respMsgBody.toIsSameString();
											if(!DataCache.previousCarMap.containsKey(previousCarKey)) {// map 里没有这个key，说明是第一次发送这个站牌这条线路数据，下发数据
												logger.info("map no key"+lineNo);
												//logger.info("thisCarData:"+thisCarData);
												msgProcessService.generateMsg0906Body(ledNo, respMsgBody);
												DataCache.previousCarMap.put(previousCarKey, respMsgBody);//发送完数据，更新map
												//DataCache.previousCarMap.put(previousCarKey, thisCarData);//发送完数据，更新map
											}else {// 这个站牌这条线路已经发送过数据了
												//String previousCarData =DataCache.previousCarMap.get(previousCarKey);// 这个站牌这条线路上次发送的数据
												MsgBody0906 previousCarData0906 =DataCache.previousCarMap.get(previousCarKey);//存上次发的实体类
												//logger.info("thisCarData:"+thisCarData);
												String previousCarData=previousCarData0906.toIsSameString();
												//String previousCarData =DataCache.previousCarMap.get(previousCarKey);// 这个站牌这条线路上次发送的数据
												//logger.info("previousCarData:"+previousCarData);
												if(!thisCarData.equals(previousCarData)) {// 这次发送的数据跟上次不一样
													msgProcessService.generateMsg0906Body(ledNo, respMsgBody);
													DataCache.previousCarMap.put(previousCarKey, respMsgBody);//发送完数据，更新map
													//DataCache.previousCarMap.put(previousCarKey, thisCarData);//发送完数据，更新map
												}
											}
											////
											//msgProcessService.generateMsg0906Body(ledNo, respMsgBody);
											///////msg0906Queue.add(respMsgBody);
										}
										busListClone= null;
										busState=null;
									}
									allBusList= null;
								}						
							}				
						}
					
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				private String getStateStr(BusState busState) {	
					//到站的话，车辆位置在下一站站点显示
					if(BusState.arrive.equals(busState.getBusState())) {
						return busState.getNextStationNo()+",1";
					}else {
						return busState.getCurrentStationNo()+",2";
					}					
				}
			});	
		}
		
		executorService.shutdownNow();
		executorService = null;
	}
	
	// 整理高德跟线路数据 到缓存 list
	public void arrangeGaodeList() {
		logger.info("load gaodelinestationlist");
		try {
			//清空 高德线网 关联关系缓存
			if(DataCache.GaodeTimeLineStationList !=null) {
				DataCache.GaodeTimeLineStationList.clear();
			}
			//所有线路站点原始数据 --目前取的是 106,59
			List<Map<String, String>> lineInfo=ledMsgManageService.queryTimeLine();
			// 中间map 存 高德线路id  为key  value 为 一个map key 为高德线路id-站点id  value 为 线网线路id-方向-线网站点id
			Map<String, Map> lineInfoStanderd=new HashMap<String, Map>();
			
			if(lineInfo!=null && !lineInfo.isEmpty()) {
				for(Map<String, String> mapline:lineInfo) {
					String gline=mapline.get("GLINE");//高德线路id
					String xline=mapline.get("XLINE");//六位数 线网线路id
					String fx=mapline.get("FX");// 方向
					String gstation=mapline.get("GSTATION");// 高德站点id
					String xstation=mapline.get("XSTATION");//线网站点id
					//Map gStationX =lineInfoStanderd.get(gline);
					
					//判断 key存在否
					if(lineInfoStanderd.containsKey(gline)) {// 存在
						// 存 key 为高德线路id-站点id  value 为 线网线路id-方向-线网站点id
						Map  stationMap=lineInfoStanderd.get(gline);
						stationMap.put(gline+"-"+gstation,xline+"-"+fx+"-"+xstation);
					}else {// key 不存在
						Map  stationMap=new HashMap();
						stationMap.put(gline+"-"+gstation,xline+"-"+fx+"-"+xstation);
						lineInfoStanderd.put(gline, stationMap);
					}
				}
				
				//System.out.println(lineInfoStanderd);
				//遍历 map 数据存 List<Map<String, Map>> GaodeTimeList 里
				for (Map.Entry<String, Map> entry : lineInfoStanderd.entrySet()) {
					String gaodeline=entry.getKey();//存的高德线路id
					Map matchMap=entry.getValue();// 线网线路站点 跟高德线路站点 匹配map
					Map<String,Object> intoGaodeTimeList=new HashMap<String,Object>(); //每条线路 key gaodeline存高德lineid key matchMap 存线网线路站点 与高德线路站点匹配 map
					intoGaodeTimeList.put("gaodeline",gaodeline);// 整理好的map 放 传入 调用高德接口的 list		
					intoGaodeTimeList.put("matchMap",matchMap);
					DataCache.GaodeTimeLineStationList.add(intoGaodeTimeList);
				}
				System.out.println("time line station list:"+DataCache.GaodeTimeLineStationList);
				
				
				
			}
		} catch (Exception e) {
			logger.info("$$$$$$");
			e.printStackTrace();
		}
	
	}
}