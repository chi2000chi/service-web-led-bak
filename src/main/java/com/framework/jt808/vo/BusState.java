package com.framework.jt808.vo;

import java.io.Serializable;

public class BusState implements Cloneable,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//车辆状态
	//途中
	public static final String onRoad = "3";
	//到站
	public static final String arrive = "1";
	//离站
	public static final String leave = "2";
	//时间
	public static final String dataTimeSplit = " ";
	//线路名称
	private String lineName;
	//车辆名称
	private String busName;
	//车辆编号
	private String busNo;
	//运行方向
	private String direction;
	//最后一包数据接收时间，包括到站，离站，GPS
	private String lastReceiveTime;
	//车辆状态，根据最后一包数据计算，根据lastReceiveTime 0预到站，1进站，2离站
	private String busState;
	//当前站序号
	private int currentStationNo;
	//下一站序号
	private int nextStationNo;
	//距离首站距离  计算规则：当前离站距离+当前站点距首站距离，到离站的时候，为站点到首站距离，GPS时，最近站点离首站距离+离当前站距离
	private int toFristStance;
//	//离开本站距离
//	private int toCurrentStationStance;
//	//距离下站距离
//	private int toNextStationStance;
	private int nextStationStance;
	//拥挤度  默认1 一般   服务传过来的值 需要减一
	private int congestion = 1;
	
	//瞬时速度
	private String speed;
	//GPS数据产生日期
	private String gpsdate;
	//GPS数据产生时间
	private String gpstime;
	//纬度
	private String latitude;
	//经度
	private String longitude;
	//车辆要到的下一站的经度
	private String stationJd;
	public String getStationJd() {
		return stationJd;
	}
	public void setStationJd(String stationJd) {
		this.stationJd = stationJd;
	}
	public String getStationWd() {
		return stationWd;
	}
	public void setStationWd(String stationWd) {
		this.stationWd = stationWd;
	}
	//车辆要到的下一站的维度
	private String stationWd;
	//方位角
	private String angle;	
	//方高度
	private String height;
	//车辆距离下一站距离
	private Double nextDistence;
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}
	public String getBusNo() {
		return busNo;
	}
	public void setBusNo(String busNo) {
		this.busNo = busNo;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getLastReceiveTime() {
		return lastReceiveTime;
	}
	public void setLastReceiveTime(String lastReceiveTime) {
		this.lastReceiveTime = lastReceiveTime;
	}
	public String getBusState() {
		return busState;
	}
	public void setBusState(String busState) {
		this.busState = busState;
	}
	public int getCurrentStationNo() {
		return currentStationNo;
	}
	public void setCurrentStationNo(int currentStationNo) {
		this.currentStationNo = currentStationNo;
	}
	public int getNextStationNo() {
		return nextStationNo;
	}
	public void setNextStationNo(int nextStationNo) {
		this.nextStationNo = nextStationNo;
	}
	public int getToFristStance() {
		return toFristStance;
	}
	public void setToFristStance(int toFristStance) {
		this.toFristStance = toFristStance;
	}

	public int getCongestion() {
		return congestion;
	}
	public void setCongestion(int congestion) {
		this.congestion = congestion;
	}

	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getGpsdate() {
		return gpsdate;
	}
	public void setGpsdate(String gpsdate) {
		this.gpsdate = gpsdate;
	}
	public String getGpstime() {
		return gpstime;
	}
	public void setGpstime(String gpstime) {
		this.gpstime = gpstime;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAngle() {
		return angle;
	}
	public void setAngle(String angle) {
		this.angle = angle;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}

	public Double getNextDistence() {
		return nextDistence;
	}
	public void setNextDistence(Double nextDistence) {
		this.nextDistence = nextDistence;
	}
	public int getNextStationStance() {
		return nextStationStance;
	}
	public void setNextStationStance(int nextStationStance) {
		this.nextStationStance = nextStationStance;
	}
	@Override
	protected Object clone() {
		BusState state = null;
		try {
			state = (BusState) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return state;
	}
}
