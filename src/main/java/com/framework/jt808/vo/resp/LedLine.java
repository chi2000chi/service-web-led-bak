package com.framework.jt808.vo.resp;

import java.util.List;

/**
 * 电子站牌线路
 */
public class LedLine {
	private String xlmc;
	//电子站牌编号id
	private String ledid;
	//当前站点停靠
	private int zdtk;
	//线路ID
	private String ID;
	//线路主名称
	private String Name;
	//线路副名称
	private String SName;
	//运行方向
	private int RunDirection;
	//首站
	private String BeginStation;
	//末站
	private String EndStation;
	//运行开始时间
	private String BeginTime;
	//运行结束时间
	private String EndTime;
	//发车间隔
	private String Interval;
	//票价
	private String Price;
	//当前站点序号
	private int StationSN;
	//站点显示方向
	private int StationDirection;
	//站点列表
	private List<LedLineStationBack> Stations;
	//线裤显示公告
	private LineMsg ledMsg;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getSName() {
		return SName;
	}
	public void setSName(String sName) {
		SName = sName;
	}
	public int getRunDirection() {
		return RunDirection;
	}
	public void setRunDirection(int runDirection) {
		RunDirection = runDirection;
	}
	public String getBeginStation() {
		return BeginStation;
	}
	public void setBeginStation(String beginStation) {
		BeginStation = beginStation;
	}
	public String getBeginTime() {
		return BeginTime;
	}
	public void setBeginTime(String beginTime) {
		BeginTime = beginTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	public String getInterval() {
		return Interval;
	}
	public void setInterval(String interval) {
		Interval = interval;
	}
	public String getPrice() {
		return Price;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public int getStationSN() {
		return StationSN;
	}
	public void setStationSN(int stationSN) {
		StationSN = stationSN;
	}
	public int getStationDirection() {
		return StationDirection;
	}
	public void setStationDirection(int stationDirection) {
		StationDirection = stationDirection;
	}
	public List<LedLineStationBack> getStations() {
		return Stations;
	}
	public void setStations(List<LedLineStationBack> stations) {
		Stations = stations;
	}
	public String getLedid() {
		return ledid;
	}
	public void setLedid(String ledid) {
		this.ledid = ledid;
	}
	public String getEndStation() {
		return EndStation;
	}
	public void setEndStation(String endStation) {
		EndStation = endStation;
	}
	public LineMsg getLedMsg() {
		return ledMsg;
	}
	public void setLedMsg(LineMsg ledMsg) {
		this.ledMsg = ledMsg;
	}
	public int getZdtk() {
		return zdtk;
	}
	public void setZdtk(int zdtk) {
		this.zdtk = zdtk;
	}
	public String getXlmc() {
		return xlmc;
	}
	public void setXlmc(String xlmc) {
		this.xlmc = xlmc;
	}
}
