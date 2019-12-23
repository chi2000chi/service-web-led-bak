package com.framework.webClient.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.framework.entity.output.DZOutputEntity;
import com.framework.entity.output.LZOutPutEntity;
import com.framework.entity.output.PlaceOutputEntity;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.resp.LedLine;
import com.framework.jt808.vo.resp.LedLineStation;
import com.framework.jt808.vo.resp.LedResourcesItem;
import com.framework.jt808.vo.resp.MsgBody0906;

public interface ICommon808Service {

	/**
	 * 根据LED编号查询LED信息及对应线裤信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectLedAndLineInfo(Map<String, Object> paramMap);
	
	
	/**
	 * 根据LED编号查询LED信息及对应线裤信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectLedAndLineInfoTmp(Map<String, Object> paramMap);
	
	
	/**
	 * 根基线路id，运行方向，查询站点列表
	 * @param qbMap
	 * @return
	 */
	public List<StationInfo> selectStaionListAll();

	/**
	 * 获取站点最后更新时间
	 * @return
	 */
	public String getLastGetStationDateTime();	
	
	/**
	 * 插入电子站牌上传参数
	 * @param paramMap
	 * @return
	 */
	public int insertUploadPara(Map<String, Object> paramMap);
	
	/**
	 * 上传电子站牌状态
	 * @param paramMap
	 * @return
	 */
	public int insertLedState(Map<String, Object> paramMap);

	public HashMap<String, Object> getLedParaByLedNo(String ledNo);

	public String getUnShowLine();
	

	public void updateCommonMsgBack(HashMap<String, Object> dataMap);

	public void insertCommonMsgBackDetail(HashMap<String, Object> dataMap);
	
	public void insertCommonMsgBack(Map<String, Object> paramMap);

	public void insertCommonMsgBackTest(Map<String, Object> paramMap);
	public void insertMsg0906(MsgBody0906 respMsgBody);


	public void insertDzLog(DZOutputEntity object);
	public void insertLzLog(LZOutPutEntity object);
	public void insertGPSLog(PlaceOutputEntity object);


	public void insertLedRegister(Map<String, Object> paramMap);


	public List<LedLine> getLedLineList(String ledno);


	public List<LedLineStation> getLedLineStationList(String ledno);


	public List<Map<String, Object>> selectSafeCodeList();


	public List<LedResourcesItem> getLedResourcesByLedNo(String ledno);
	
	//车辆距离下一站多少距离算进站
	public String getDistance();
	//车辆距离下一站距离 < 站间距离+ 此距离 才为合法车辆
	public String getValidDistance();


	public void insertLedMsgLogFromTemp(Map<String, Object> paramMap);

}
