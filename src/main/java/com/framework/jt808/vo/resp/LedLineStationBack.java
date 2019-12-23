package com.framework.jt808.vo.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电子站牌线路站点
 */
public class LedLineStationBack {
	// 站点ID
	@JsonProperty
	private String Id;
	// 点名称
	@JsonProperty
	private String Name;
	// 分区票价
	@JsonProperty
	private String PriceArea;
	 @JsonIgnore
	public String getId() {
		return Id;
	}
	 @JsonIgnore
	public void setId(String id) {
		Id = id;
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
	public String getPriceArea() {
		return PriceArea;
	}
	 @JsonIgnore
	public void setPriceArea(String priceArea) {
		PriceArea = priceArea;
	}
}
