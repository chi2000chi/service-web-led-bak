package com.framework.jt808.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.framework.jt808.vo.BusState;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.resp.MsgBody0906;

public class DataCache {
	//车辆状态队列
	public static ConcurrentLinkedQueue<BusState> busQueue = new ConcurrentLinkedQueue<BusState>();
	//待发车队列
	public static ConcurrentHashMap<String, Object> readySendBusMap = new ConcurrentHashMap<String, Object>();
	// 站点队列，定时刷新
	public static Queue<StationInfo> stationQueue = new ConcurrentLinkedQueue<StationInfo>();
	// 最后接收时间，判断时候更新数据
	public static String lastGetStationDateTime = null;
	
	// 站点队列，定时刷新
	public static Queue<Map<String, Object>> ledQueue = new ConcurrentLinkedQueue<Map<String, Object>>();
	
	// 未开通线路列表
	public static List<String> unShowLineList = new LinkedList<String>();
	

	public static List<Map<String, Object>> ledMsgList;
	
	//安全码队列
	public static Queue<Map<String, Object>> safeCodeQueue = new ConcurrentLinkedQueue<Map<String, Object>>();
	//车辆距离下一站距离大于此参数即为进站
	public static String distanceStr;
	//车辆距离下一站距离 < 站间距离+ 此距离 才为合法车辆
	public static String validDistanceStr;
	
	public static ConcurrentHashMap<String, StationInfo> stationMap = new ConcurrentHashMap<String, StationInfo>();
	
	public static ConcurrentLinkedQueue<Map<String, Object>> saveMessageQueue = new ConcurrentLinkedQueue<Map<String, Object>>();
	
	//转发服务过来的拥挤度数据
	public static ConcurrentHashMap<String, Integer> yjdMap = new ConcurrentHashMap<String, Integer>();
	//存路况的map，一分钟一刷新
	public static ConcurrentHashMap<String, String> lkMap = new ConcurrentHashMap<String, String>();
	//存一个站牌一条线路 上一次发送的数据，用来跟这次发送的数据做比对，数据一致的就不发送，不一致的更新map
	public static ConcurrentHashMap<String, MsgBody0906> previousCarMap = new ConcurrentHashMap<String, MsgBody0906>();
	//public static ConcurrentHashMap<String, String> previousCarMap = new ConcurrentHashMap<String, String>();
	
	
	
	//存到站时间的map key 线路编号-方向-站点id value map 里面key 到这个站点前车辆自编号 value 时间（分钟）
	public static ConcurrentHashMap<String, Map> dzTimeMap = new ConcurrentHashMap<String, Map>();
	
	
	//map 里 gaodeline key对应value 存高德线路id  matchMap key 对应value 存一个map map 里存  存 key 为高德线路id-站点id  value 为 线网线路id-方向-线网站点id
	public static List<Map<String, Object>> GaodeTimeLineStationList=new ArrayList<Map<String, Object>>();
}
