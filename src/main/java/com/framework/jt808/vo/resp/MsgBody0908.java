package com.framework.jt808.vo.resp;

/**
 * 通用应答
 */
public class MsgBody0908 {
	// 线路编号
	private int lineNo;
	//首班时间
	private String startTime;
	//末班时间
	private String endTime;
	// 发车间隔
	private int fcjg;
	// 票价
	private int pj;
	// 上下行
	private int direction;
	// 当前站号
	private int currentStationNo;
	// 线路方向0908新加
	private int lineDirection;
	// 站点列表长度
	private int stationLength;
	//站点列表
	private String stationInfoList;
	public MsgBody0908() {
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getFcjg() {
		return fcjg;
	}
	public void setFcjg(int fcjg) {
		this.fcjg = fcjg;
	}
	public int getPj() {
		return pj;
	}
	public void setPj(int pj) {
		this.pj = pj;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getCurrentStationNo() {
		return currentStationNo;
	}
	public void setCurrentStationNo(int currentStationNo) {
		this.currentStationNo = currentStationNo;
	}
	public int getStationLength() {
		return stationLength;
	}
	public void setStationLength(int stationLength) {
		this.stationLength = stationLength;
	}
	public String getStationInfoList() {
		return stationInfoList;
	}
	public void setStationInfoList(String stationInfoList) {
		this.stationInfoList = stationInfoList;
	}
	public int getLineDirection() {
		return lineDirection;
	}
	public void setLineDirection(int lineDirection) {
		this.lineDirection = lineDirection;
	}
	
	
}
