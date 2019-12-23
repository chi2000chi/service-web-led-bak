package com.framework.jt808.vo.resp;

/**
 * 下发线路实时路况
 */
public class MsgBody0909 {
	// 线路编号
	private int lineNo;
	// 路况信息长度
	private int lkLength;
	//路况信息
	private String lkList;
	public MsgBody0909() {
	}
	public int getLineNo() {
		return lineNo;
	}
	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}
	public int getLkLength() {
		return lkLength;
	}
	public void setLkLength(int lkLength) {
		this.lkLength = lkLength;
	}
	public String getLkList() {
		return lkList;
	}
	public void setLkList(String lkList) {
		this.lkList = lkList;
	}
	
}
