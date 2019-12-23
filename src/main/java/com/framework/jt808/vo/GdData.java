package com.framework.jt808.vo;

public class GdData {
	// GPS方向角
	private String gps_angle;
	// GPS时间，格式：20180710155842
	private String gps_time;
	// 站点ID
	private String station_id;
	// 接收时间，格式：20180806170146
	private String receive_time;
	// 经度坐标，84坐标系。如：115.321233
	private String lon;
	// 数据源编号
	private String source;
	// 城市区号
	private String city_code;
	// GPS速度，单位m/s
	private String gps_speed;
	// 线路ID
	private String line_id;
	// 信号类型。取值：0-GPS、1-到离站、2-GPS+到离站
	private String signal_type;
	// GPS高度，单位米
	private String gps_altitude;
	// 站点序号
	private String station_order;
	// 车辆唯一标识
	private String car_id;
	// 纬度坐标，84坐标系。如：39.522321
	private String lat;
	// 上下行。取值：1-上行、2-下行
	private String direction;
	public String getGps_angle() {
		return gps_angle;
	}
	public void setGps_angle(String gps_angle) {
		this.gps_angle = gps_angle;
	}
	public String getGps_time() {
		return gps_time;
	}
	public void setGps_time(String gps_time) {
		this.gps_time = gps_time;
	}
	public String getStation_id() {
		return station_id;
	}
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}
	public String getReceive_time() {
		return receive_time;
	}
	public void setReceive_time(String receive_time) {
		this.receive_time = receive_time;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getCity_code() {
		return city_code;
	}
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}
	public String getGps_speed() {
		return gps_speed;
	}
	public void setGps_speed(String gps_speed) {
		this.gps_speed = gps_speed;
	}
	public String getLine_id() {
		return line_id;
	}
	public void setLine_id(String line_id) {
		this.line_id = line_id;
	}
	public String getSignal_type() {
		return signal_type;
	}
	public void setSignal_type(String signal_type) {
		this.signal_type = signal_type;
	}
	public String getGps_altitude() {
		return gps_altitude;
	}
	public void setGps_altitude(String gps_altitude) {
		this.gps_altitude = gps_altitude;
	}
	public String getStation_order() {
		return station_order;
	}
	public void setStation_order(String station_order) {
		this.station_order = station_order;
	}
	public String getCar_id() {
		return car_id;
	}
	public void setCar_id(String car_id) {
		this.car_id = car_id;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
}
