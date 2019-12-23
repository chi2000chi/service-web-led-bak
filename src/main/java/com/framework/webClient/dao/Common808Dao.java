package com.framework.webClient.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.framework.entity.output.DZOutputEntity;
import com.framework.entity.output.LZOutPutEntity;
import com.framework.entity.output.PlaceOutputEntity;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.resp.LedLine;
import com.framework.jt808.vo.resp.LedLineStation;
import com.framework.jt808.vo.resp.LedResourcesItem;
import com.framework.jt808.vo.resp.MsgBody0906;


public interface Common808Dao {

	
	public List<Map<String, Object>> selectLedAndLineInfo(Map<String, Object> paramMap);
	
	public List<StationInfo> selectStaionListAll();
	
	public String getLastGetStationDateTime();

	public int insertUploadPara(Map<String, Object> paramMap);

	public int insertLedState(Map<String, Object> paramMap);

	public HashMap<String, Object> getLedParaByLedNo(String ledNo);

	public String getUnShowLine();
	
	public void updateCommonMsgBack(HashMap<String, Object> dataMap);

	public void insertCommonMsgBack(Map<String, Object> dataMap);
	
	public void insertCommonMsgBackTest(Map<String, Object> dataMap);
	
	public void insertCommonMsgBackDetail(Map<String, Object> dataMap);
	
	public List<Map<String, Object>> selectLedAndLineInfoTmp(Map<String, Object> paramMap);

	public void insertMsg0906(MsgBody0906 respMsgBody);

	public void insertDzLog(DZOutputEntity object);

	public void insertLzLog(LZOutPutEntity object);

	public void insertGPSLog(PlaceOutputEntity object);

	public void insertLedRegister(Map<String, Object> paramMap);

	public List<LedLine> getLedLineList(@Param("ledNo")String ledno);

	public List<LedLineStation> getLedLineStationList(@Param("ledNo")String ledno);

	public List<Map<String, Object>> selectSafeCodeList();

	public List<LedResourcesItem> getLedResourcesByLedNo(@Param("ledNo") String ledno);	
	
	//取车辆距离下一站多少距离算进站
	public String getDistance();
	// 车辆距离下一站距离 < 站间距离+ 此距离 才为合法车辆
	public String getValidDistance();

	public void insertLedMsgLogFromTemp(Map<String, Object> dataMap);


	
}
