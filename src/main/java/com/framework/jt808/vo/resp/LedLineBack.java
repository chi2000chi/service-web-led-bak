package com.framework.jt808.vo.resp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电子站牌线路
 */
public class LedLineBack {
	//线路ID
	@JsonProperty
	private String ID;
	//线路主名称
	@JsonProperty
	private String Name;
	//线路副名称
	@JsonProperty
	private String SName;
	//运行方向
	@JsonProperty
	private int RunDirection;
	//首站
	@JsonProperty
	private String BeginStation;
	//末站
	@JsonProperty
	private String EndStation;
	//运行开始时间
	@JsonProperty
	private String BeginTime;
	//运行结束时间
	@JsonProperty
	private String EndTime;
	//发车间隔
	@JsonProperty
	private String Interval;
	//票价
	@JsonProperty
	private String Price;
	//当前站点序号
	@JsonProperty
	private int StationSN;
	//站点显示方向
	@JsonProperty
	private int StationDirection;
	//站点列表
	@JsonProperty
	private List<LedLineStationBack> Stations;
	//线裤显示公告
	@JsonProperty
	private LineMsg BaseMsg;
	 @JsonIgnore
	public String getID() {
		return ID;
	}
	 @JsonIgnore
	public void setID(String iD) {
		ID = iD;
	}
	 @JsonIgnore
	public String getName() {
		return Name;
	} 
	 @JsonIgnore
	public void setName(String name) {
		Name = name;
	}
	 @JsonIgnore
	public String getSName() {
		return SName;
	}
	 @JsonIgnore
	public void setSName(String sName) {
		SName = sName;
	}
	 @JsonIgnore
	public int getRunDirection() {
		return RunDirection;
	}
	 @JsonIgnore
	public void setRunDirection(int runDirection) {
		RunDirection = runDirection;
	}
	 @JsonIgnore
	public String getBeginStation() {
		return BeginStation;
	}
	 @JsonIgnore
	public void setBeginStation(String beginStation) {
		BeginStation = beginStation;
	}
	 @JsonIgnore
	public String getBeginTime() {
		return BeginTime;
	}
	 @JsonIgnore
	public void setBeginTime(String beginTime) {
		BeginTime = beginTime;
	}
	 @JsonIgnore
	public String getEndTime() {
		return EndTime;
	}
	 @JsonIgnore
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	 @JsonIgnore
	public String getInterval() {
		return Interval;
	}
	 @JsonIgnore
	public void setInterval(String interval) {
		Interval = interval;
	}
	 @JsonIgnore
	public String getPrice() {
		return Price;
	}
	 @JsonIgnore
	public void setPrice(String price) {
		Price = price;
	}
	 @JsonIgnore
	public int getStationSN() {
		return StationSN;
	}
	 @JsonIgnore
	public void setStationSN(int stationSN) {
		StationSN = stationSN;
	}
	 @JsonIgnore
	public int getStationDirection() {
		return StationDirection;
	}
	 @JsonIgnore
	public void setStationDirection(int stationDirection) {
		StationDirection = stationDirection;
	}
	 @JsonIgnore
	public List<LedLineStationBack> getStations() {
		return Stations;
	}
	 @JsonIgnore
	public void setStations(List<LedLineStationBack> stations) {
		Stations = stations;
	}
	 @JsonIgnore
	public String getEndStation() {
		return EndStation;
	}
	 @JsonIgnore
	public void setEndStation(String endStation) {
		EndStation = endStation;
	}
	 @JsonIgnore
	public LineMsg getBasemsg() {
		return BaseMsg;
	}
	 @JsonIgnore
	public void setBasemsg(LineMsg basemsg) {
		this.BaseMsg = basemsg;
	}
}
