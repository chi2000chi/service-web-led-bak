package com.framework.jt808.vo.resp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电子站牌线路
 */
public class LedResources {
	//站点列表
	@JsonProperty
	private List<LedResourcesItemArray> Resources;
	 @JsonIgnore
	public List<LedResourcesItemArray> getResources() {
		return Resources;
	}
	 @JsonIgnore
	public void setResources(List<LedResourcesItemArray> resources) {
		Resources = resources;
	}
	
}
