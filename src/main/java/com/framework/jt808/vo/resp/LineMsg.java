package com.framework.jt808.vo.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 电子站牌线路站点
 */
public class LineMsg {
	// 线路编号
	@JsonProperty
	private String BgColor;
	// 电子站牌编号
	@JsonProperty
	private String FontColor;
	//站点ID
	@JsonProperty
	private String FontSize;
	//站点名称
	@JsonProperty
	private String Msg;
	//分区票价
	@JsonProperty
	private String MsgArea;
	
	 @JsonIgnore
	public String getBgColor() {
		return BgColor;
	}
	 @JsonIgnore
	public void setBgColor(String bgColor) {
		this.BgColor = bgColor;
	}
	 @JsonIgnore
	public String getFontColor() {
		return FontColor;
	}
	 @JsonIgnore
	public void setFontColor(String fontcolor) {
		this.FontColor = fontcolor;
	}
	 @JsonIgnore
	public String getFontSize() {
		return FontSize;
	}
	 @JsonIgnore
	public void setFontSize(String fontsize) {
		this.FontSize = fontsize;
	}
	 @JsonIgnore
	public String getMsg() {
		return Msg;
	}
	 @JsonIgnore
	public void setMsg(String msg) {
		this.Msg = msg;
	}
	 @JsonIgnore
	public String getMsgArea() {
		return MsgArea;
	}
	 @JsonIgnore
	public void setMsgArea(String msgArea) {
		this.MsgArea = msgArea;
	}
}
