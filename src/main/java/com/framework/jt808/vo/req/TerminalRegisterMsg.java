package com.framework.jt808.vo.req;

import java.util.Arrays;

import com.framework.jt808.vo.PackageData;


/**
 * 终端注册消息
 */
public class TerminalRegisterMsg extends PackageData {

	private TerminalRegInfo terminalRegInfo;

	public TerminalRegisterMsg() {
	}

	public TerminalRegisterMsg(PackageData packageData) {
		this();
		this.checkSum = packageData.getCheckSum();
		this.msgBodyBytes = packageData.getMsgBodyBytes();
		this.msgHeader = packageData.getMsgHeader();
	}

	public TerminalRegInfo getTerminalRegInfo() {
		return terminalRegInfo;
	}

	public void setTerminalRegInfo(TerminalRegInfo msgBody) {
		this.terminalRegInfo = msgBody;
	}

	@Override
	public String toString() {
		return "TerminalRegisterMsg [terminalRegInfo=" + terminalRegInfo + ", msgHeader=" + msgHeader
				+ ", msgBodyBytes=" + Arrays.toString(msgBodyBytes) + ", checkSum=" + checkSum 
				+ "]";
	}

	public static class TerminalRegInfo {
	
		private String ztbh;
		private byte ztlx;
		private byte csbh;
		private byte xlh;
		public String getZtbh() {
			return ztbh;
		}
		public void setZtbh(String ztbh) {
			this.ztbh = ztbh;
		}
		public byte getZtlx() {
			return ztlx;
		}
		public void setZtlx(byte ztlx) {
			this.ztlx = ztlx;
		}
		public byte getCsbh() {
			return csbh;
		}
		public void setCsbh(byte csbh) {
			this.csbh = csbh;
		}
		public byte getXlh() {
			return xlh;
		}
		public void setXlh(byte xlh) {
			this.xlh = xlh;
		}
	}
}
