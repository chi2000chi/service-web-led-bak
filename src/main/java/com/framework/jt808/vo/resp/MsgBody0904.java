package com.framework.jt808.vo.resp;

/**
 * 通用应答
 */
public class MsgBody0904 {
	
	// 线路长度 DWORD 4字节	
	private int lineNo;
	// 公告类别
	private int msgType;
	// 公告全屏显示
	private int fullScreenFlg;

	public MsgBody0904() {
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getFullScreenFlg() {
		return fullScreenFlg;
	}

	public void setFullScreenFlg(int fullScreenFlg) {
		this.fullScreenFlg = fullScreenFlg;
	}
}
