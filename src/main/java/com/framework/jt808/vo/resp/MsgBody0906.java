package com.framework.jt808.vo.resp;

/**
 * 实时线路指示
 */
public class MsgBody0906 {

	// 线路长度 DWORD 4字节	
	private int lineNo;
	// 线路名称长度
	private int lineNameLength;
	// 线路名称
	private String lineName;
	// 当前线路车辆数	
	private int busCount;
	
	// 最近的车辆距离本站的距离	
	private int arriveDistance1;
	// 最近的车辆距离本站时间	
	private int arriveTime1;
	// 最近的车辆距离本站的站数
	private int arriveStationCount1;
	// 最近的车辆车上的拥挤度
	private int arriveYJD1;
	// 显示车辆状态
	private int arriveBusState1;
	private int arriveBusNo1Length;
	//最近的车的车牌号
	private String arriveBusNo1;
	
	// 次近的车辆距离本站的距离	
	private int arriveDistance2;
	// 次近的车辆距离本站时间	
	private int arriveTime2;
	// 次近的车辆距离本站的站数
	private int arriveStationCount2;
	// 次近的车辆车上的拥挤度
	private int arriveYJD2;
	// 次近的车辆车上的拥挤度
	private int arriveBusState2;
	private int arriveBusNo2Length;
	private String arriveBusNo2;
	
	// 第三近的车辆距离本站的距离	
	private int arriveDistance3;
	// 第三近的车辆距离本站时间	
	private int arriveTime3;
	// 第三近的车辆距离本站的站数
	private int arriveStationCount3;
	// 第三近的车辆车上的拥挤度
	private int arriveYJD3;
	// 第三近的车辆车上的拥挤度
	private int arriveBusState3;
	private int arriveBusNo3Length;
	private String arriveBusNo3;
	
	// 末班车状态
	private int lastBusState;
	
	// 最近一趟待发车发车时间
	private String nextFCSJ;
	
	// 车辆实时位置信息列表长度
	private int busStateLength;
	
	//车辆实时位置信息列表
	private String busState;
	
	
	//方向
	private String fx;
	//电子站牌编号
	private String ledNo;
	//当前站序
	private int stationNo;
	//当前流水号
	private int flowid;
	
	//电子站牌编号
	private String gpstime;
	//电子站牌编号
	private String startTime;
	//车辆距离下一站距离
	private Double nextDistence;
	//车辆的经度
	private String busJD;
	//车辆的维度
	private String busWd;
	//车辆到达的下一个站点的经度
	private String nextStationJd;
	//车辆到达的下一个站点的维度
	private String nextStationWd;
	public MsgBody0906() {
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public int getLineNameLength() {
		return lineNameLength;
	}
	public void setLineNameLength(int lineNameLength) {
		this.lineNameLength = lineNameLength;
	}
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public int getBusCount() {
		return busCount;
	}
	public void setBusCount(int busCount) {
		this.busCount = busCount;
	}
	public int getArriveDistance1() {
		return arriveDistance1;
	}
	public void setArriveDistance1(int arriveDistance1) {
		this.arriveDistance1 = arriveDistance1;
	}
	public int getArriveTime1() {
		return arriveTime1;
	}
	public void setArriveTime1(int arriveTime1) {
		this.arriveTime1 = arriveTime1;
	}
	public int getArriveStationCount1() {
		return arriveStationCount1;
	}
	public void setArriveStationCount1(int arriveStationCount1) {
		this.arriveStationCount1 = arriveStationCount1;
	}
	public int getArriveYJD1() {
		return arriveYJD1;
	}
	public void setArriveYJD1(int arriveYJD1) {
		this.arriveYJD1 = arriveYJD1;
	}
	public int getArriveBusState1() {
		return arriveBusState1;
	}
	public void setArriveBusState1(int arriveBusState1) {
		this.arriveBusState1 = arriveBusState1;
	}
	public int getArriveDistance2() {
		return arriveDistance2;
	}
	public void setArriveDistance2(int arriveDistance2) {
		this.arriveDistance2 = arriveDistance2;
	}
	public int getArriveTime2() {
		return arriveTime2;
	}
	public void setArriveTime2(int arriveTime2) {
		this.arriveTime2 = arriveTime2;
	}
	public int getArriveStationCount2() {
		return arriveStationCount2;
	}
	public void setArriveStationCount2(int arriveStationCount2) {
		this.arriveStationCount2 = arriveStationCount2;
	}
	public int getArriveYJD2() {
		return arriveYJD2;
	}
	public void setArriveYJD2(int arriveYJD2) {
		this.arriveYJD2 = arriveYJD2;
	}
	public int getArriveBusState2() {
		return arriveBusState2;
	}
	public void setArriveBusState2(int arriveBusState2) {
		this.arriveBusState2 = arriveBusState2;
	}
	public int getArriveDistance3() {
		return arriveDistance3;
	}
	public void setArriveDistance3(int arriveDistance3) {
		this.arriveDistance3 = arriveDistance3;
	}
	public int getArriveTime3() {
		return arriveTime3;
	}
	public void setArriveTime3(int arriveTime3) {
		this.arriveTime3 = arriveTime3;
	}
	public int getArriveStationCount3() {
		return arriveStationCount3;
	}
	public void setArriveStationCount3(int arriveStationCount3) {
		this.arriveStationCount3 = arriveStationCount3;
	}
	public int getArriveYJD3() {
		return arriveYJD3;
	}
	public void setArriveYJD3(int arriveYJD3) {
		this.arriveYJD3 = arriveYJD3;
	}
	public int getArriveBusState3() {
		return arriveBusState3;
	}
	public void setArriveBusState3(int arriveBusState3) {
		this.arriveBusState3 = arriveBusState3;
	}
	public int getLastBusState() {
		return lastBusState;
	}
	public void setLastBusState(int lastBusState) {
		this.lastBusState = lastBusState;
	}
	public String getNextFCSJ() {
		return nextFCSJ;
	}
	public void setNextFCSJ(String nextFCSJ) {
		this.nextFCSJ = nextFCSJ;
	}
	public int getBusStateLength() {
		return busStateLength;
	}
	public void setBusStateLength(int busStateLength) {
		this.busStateLength = busStateLength;
	}
	public String getBusState() {
		return busState;
	}
	public void setBusState(String busState) {
		this.busState = busState;
	}
	public int getArriveBusNo1Length() {
		return arriveBusNo1Length;
	}
	public void setArriveBusNo1Length(int arriveBusNo1Length) {
		this.arriveBusNo1Length = arriveBusNo1Length;
	}
	public String getArriveBusNo1() {
		return arriveBusNo1;
	}
	public void setArriveBusNo1(String arriveBusNo1) {
		this.arriveBusNo1 = arriveBusNo1;
	}
	public int getArriveBusNo2Length() {
		return arriveBusNo2Length;
	}
	public void setArriveBusNo2Length(int arriveBusNo2Length) {
		this.arriveBusNo2Length = arriveBusNo2Length;
	}
	public String getArriveBusNo2() {
		return arriveBusNo2;
	}
	public void setArriveBusNo2(String arriveBusNo2) {
		this.arriveBusNo2 = arriveBusNo2;
	}
	public int getArriveBusNo3Length() {
		return arriveBusNo3Length;
	}
	public void setArriveBusNo3Length(int arriveBusNo3Length) {
		this.arriveBusNo3Length = arriveBusNo3Length;
	}
	public String getArriveBusNo3() {
		return arriveBusNo3;
	}
	public void setArriveBusNo3(String arriveBusNo3) {
		this.arriveBusNo3 = arriveBusNo3;
	}
	public String getFx() {
		return fx;
	}
	public void setFx(String fx) {
		this.fx = fx;
	}
	public String getLedNo() {
		return ledNo;
	}
	public void setLedNo(String ledNo) {
		this.ledNo = ledNo;
	}
	public int getStationNo() {
		return stationNo;
	}
	public void setStationNo(int stationNo) {
		this.stationNo = stationNo;
	}
	public int getFlowid() {
		return flowid;
	}
	public void setFlowid(int flowid) {
		this.flowid = flowid;
	}
	public String getGpstime() {
		return gpstime;
	}
	public void setGpstime(String gpstime) {
		this.gpstime = gpstime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public Double getNextDistence() {
		return nextDistence;
	}
	public void setNextDistence(Double nextDistence) {
		this.nextDistence = nextDistence;
	}
	public String getBusJD() {
		return busJD;
	}
	public void setBusJD(String busJD) {
		this.busJD = busJD;
	}
	public String getBusWd() {
		return busWd;
	}
	public void setBusWd(String busWd) {
		this.busWd = busWd;
	}
	public String getNextStationJd() {
		return nextStationJd;
	}
	public void setNextStationJd(String nextStationJd) {
		this.nextStationJd = nextStationJd;
	}
	public String getNextStationWd() {
		return nextStationWd;
	}
	public void setNextStationWd(String nextStationWd) {
		this.nextStationWd = nextStationWd;
	}
	/*// 第一到第三辆车的 站数，状态，车辆编号
	public String toIsSameString() {
		return "arriveStationCount1=" + arriveStationCount1 + ", arriveBusState1=" + arriveBusState1
				+ ", arriveBusNo1=" + arriveBusNo1 + ", arriveStationCount2=" + arriveStationCount2
				+ ", arriveBusState2=" + arriveBusState2 + ", arriveBusNo2=" + arriveBusNo2 + ", arriveStationCount3="
				+ arriveStationCount3 + ", arriveBusState3=" + arriveBusState3 + ", arriveBusNo3=" + arriveBusNo3 ;
	}*/
	public String toIsSameString() {
		return "arriveTime1=" + arriveTime1 + ", arriveStationCount1=" + arriveStationCount1
				+ ", arriveBusState1=" + arriveBusState1 + ", arriveBusNo1=" + arriveBusNo1 + ", arriveStationCount2="
				+ arriveStationCount2 + ", arriveBusState2=" + arriveBusState2 + ", arriveStationCount3="
				+ arriveStationCount3 + ", arriveBusState3=" + arriveBusState3 + ", arriveBusNo3=" + arriveBusNo3 ;
	}
	
	
}
