package com.framework.jt808.vo;

import java.io.Serializable;
import java.util.Date;

public class StationInfo implements Cloneable,Serializable {
	//线裤id
	private String xlid;
	//线路名称
	private String xlmc;
	//站点序号
	private int zdxh;
	//运行方向
	private int fx;
	//站点名称
	private String zdmc;
	//距上站距离
	private int jshangzjl;
	//距下站距离
	private int jxiazjl;
	//距首站距离
	private int jshouzl;
	//修改时间
	private Date xgsj;
	
	//经度
	private String jd;
	
	//纬度
	private String wd;
	public String getXlid() {
		return xlid;
	}
	public void setXlid(String xlid) {
		this.xlid = xlid;
	}
	public String getXlmc() {
		return xlmc;
	}
	public void setXlmc(String xlmc) {
		this.xlmc = xlmc;
	}
	public int getZdxh() {
		return zdxh;
	}
	public void setZdxh(int zdxh) {
		this.zdxh = zdxh;
	}
	public int getFx() {
		return fx;
	}
	public void setFx(int fx) {
		this.fx = fx;
	}
	public String getZdmc() {
		return zdmc;
	}
	public void setZdmc(String zdmc) {
		this.zdmc = zdmc;
	}
	public int getJshangzjl() {
		return jshangzjl;
	}
	public void setJshangzjl(int jshangzjl) {
		this.jshangzjl = jshangzjl;
	}
	public int getJxiazjl() {
		return jxiazjl;
	}
	public void setJxiazjl(int jxiazjl) {
		this.jxiazjl = jxiazjl;
	}
	public int getJshouzl() {
		return jshouzl;
	}
	public void setJshouzl(int jshouzl) {
		this.jshouzl = jshouzl;
	}
	public Date getXgsj() {
		return xgsj;
	}
	public void setXgsj(Date xgsj) {
		this.xgsj = xgsj;
	}
	public String getJd() {
		return jd;
	}
	public void setJd(String jd) {
		this.jd = jd;
	}
	public String getWd() {
		return wd;
	}
	public void setWd(String wd) {
		this.wd = wd;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fx;
		result = prime * result + ((jd == null) ? 0 : jd.hashCode());
		result = prime * result + jshangzjl;
		result = prime * result + jshouzl;
		result = prime * result + jxiazjl;
		result = prime * result + ((wd == null) ? 0 : wd.hashCode());
		result = prime * result + ((xgsj == null) ? 0 : xgsj.hashCode());
		result = prime * result + ((xlid == null) ? 0 : xlid.hashCode());
		result = prime * result + ((xlmc == null) ? 0 : xlmc.hashCode());
		result = prime * result + ((zdmc == null) ? 0 : zdmc.hashCode());
		result = prime * result + zdxh;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StationInfo other = (StationInfo) obj;
		if (fx != other.fx)
			return false;
		if (jd == null) {
			if (other.jd != null)
				return false;
		} else if (!jd.equals(other.jd))
			return false;
		if (jshangzjl != other.jshangzjl)
			return false;
		if (jshouzl != other.jshouzl)
			return false;
		if (jxiazjl != other.jxiazjl)
			return false;
		if (wd == null) {
			if (other.wd != null)
				return false;
		} else if (!wd.equals(other.wd))
			return false;
		if (xgsj == null) {
			if (other.xgsj != null)
				return false;
		} else if (!xgsj.equals(other.xgsj))
			return false;
		if (xlid == null) {
			if (other.xlid != null)
				return false;
		} else if (!xlid.equals(other.xlid))
			return false;
		if (xlmc == null) {
			if (other.xlmc != null)
				return false;
		} else if (!xlmc.equals(other.xlmc))
			return false;
		if (zdmc == null) {
			if (other.zdmc != null)
				return false;
		} else if (!zdmc.equals(other.zdmc))
			return false;
		if (zdxh != other.zdxh)
			return false;
		return true;
	}
}
