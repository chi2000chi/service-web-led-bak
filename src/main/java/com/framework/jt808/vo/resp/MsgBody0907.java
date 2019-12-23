package com.framework.jt808.vo.resp;

/**
 * 通用应答
 */
public class MsgBody0907 {

	// 配置xml文件信息长度
	private int lineInfoLength;
	
	// 配置xml文件信息
	private String lineInfoList;

	public MsgBody0907() {
	}
	
	public int getLineInfoLength() {
		return lineInfoLength;
	}

	public void setLineInfoLength(int lineInfoLength) {
		this.lineInfoLength = lineInfoLength;
	}

	public String getLineInfoList() {
		return lineInfoList;
	}

	public void setLineInfoList(String lineInfoList) {
		this.lineInfoList = lineInfoList;
	}
}
