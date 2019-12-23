package com.framework.jt808.vo.resp;

/**
 * 电子站牌线路站点
 */
public class LedLineStation {
	// 运行方向
	private int yxfx;
	// 线路编号
	private int lineNo;
	// 电子站牌编号
	private String ledid;
	// 站点ID
	private String Id;
	// 点名称
	private String Name;
	// 分区票价
	private String PriceArea;
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public String getLedid() {
		return ledid;
	}
	public void setLedid(String ledid) {
		this.ledid = ledid;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPriceArea() {
		return PriceArea;
	}
	public void setPriceArea(String priceArea) {
		PriceArea = priceArea;
	}
	public int getYxfx() {
		return yxfx;
	}
	public void setYxfx(int yxfx) {
		this.yxfx = yxfx;
	}
}
