package com.framework.jt808.vo.resp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电子站牌线路
 */
public class LedResourcesItem {

	@JsonProperty
	private String Name;

	@JsonProperty
	private String Url;
	 @JsonIgnore
	public String getName() {
		return Name;
	}
	 @JsonIgnore
	public void setName(String name) {
		Name = name;
	}
	 @JsonIgnore
	public String getUrl() {
		return Url;
	}
	 @JsonIgnore
	public void setUrl(String url) {
		Url = url;
	}

}
