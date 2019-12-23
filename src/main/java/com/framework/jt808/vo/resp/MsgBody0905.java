package com.framework.jt808.vo.resp;

/**
 * 通用应答
 */
public class MsgBody0905 {

	// 模板编码  DWORD 4字节	
	private int templateNo;
	// 文件校验码
	private int wjjym;
	// 配置xml文件信息长度
	private int fileLength;
	// 配置xml文件信息
	private String filePath;

	public MsgBody0905() {
	}

	public int getTemplateNo() {
		return templateNo;
	}

	public void setTemplateNo(int templateNo) {
		this.templateNo = templateNo;
	}

	public int getWjjym() {
		return wjjym;
	}

	public void setWjjym(int wjjym) {
		this.wjjym = wjjym;
	}

	public int getFileLength() {
		return fileLength;
	}

	public void setFileLength(int fileLength) {
		this.fileLength = fileLength;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	

}
