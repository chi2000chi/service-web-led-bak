package com.framework.jt808.vo.resp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电子站牌线路
 */
public class LedResourcesItemArray {

	@JsonProperty
	private String Name;

	@JsonProperty
	private List<String> Url;
	 @JsonIgnore
	public String getName() {
		return Name;
	}
	 @JsonIgnore
	public void setName(String name) {
		Name = name;
	}
	 @JsonIgnore
	public List<String> getUrl() {
		return Url;
	}
	 @JsonIgnore
	public void setUrl(List<String> url) {
		Url = url;
	}
}
