package com.framework.webClient.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.entity.output.DZOutputEntity;
import com.framework.entity.output.LZOutPutEntity;
import com.framework.entity.output.PlaceOutputEntity;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.resp.LedLine;
import com.framework.jt808.vo.resp.LedLineStation;
import com.framework.jt808.vo.resp.LedResourcesItem;
import com.framework.jt808.vo.resp.MsgBody0906;
import com.framework.webClient.dao.Common808Dao;
import com.framework.webClient.dao.CommonDao;

/**
 * 
 * 电子站牌下发查询接口
 * @author ly
 *
 */
@Service
public class Common808ServiceImpl implements ICommon808Service {

	@Autowired(required = true)
	private Common808Dao commonDao;

	@Override
	public List<Map<String, Object>> selectLedAndLineInfo(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return commonDao.selectLedAndLineInfo(paramMap);
	}

	@Override
	public List<StationInfo> selectStaionListAll() {
		// TODO Auto-generated method stub
		return commonDao.selectStaionListAll();
	}

	@Override
	public String getLastGetStationDateTime() {
		// TODO Auto-generated method stub
		return commonDao.getLastGetStationDateTime();
	}

	@Override
	public int insertUploadPara(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return commonDao.insertUploadPara(paramMap);
	}

	@Override
	public int insertLedState(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return commonDao.insertLedState(paramMap);
	}

	@Override
	public HashMap<String, Object> getLedParaByLedNo(String ledNo) {
		// TODO Auto-generated method stub
		return commonDao.getLedParaByLedNo(ledNo);
	}

	@Override
	public String getUnShowLine() {
		// TODO Auto-generated method stub
		return commonDao.getUnShowLine();
	}

	@Override
	public void updateCommonMsgBack(HashMap<String, Object> dataMap) {
		commonDao.updateCommonMsgBack(dataMap);	
	}

	@Override
	public void insertCommonMsgBack(Map<String, Object> dataMap) {
		// TODO Auto-generated method stub
		commonDao.insertCommonMsgBack(dataMap);	
	}
	@Override
	public void insertCommonMsgBackTest(Map<String, Object> dataMap) {
		// TODO Auto-generated method stub
		commonDao.insertCommonMsgBackTest(dataMap);	
	}
	
	
	@Override
	public List<Map<String, Object>> selectLedAndLineInfoTmp(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return commonDao.selectLedAndLineInfoTmp(paramMap);
	}

	@Override
	public void insertMsg0906(MsgBody0906 respMsgBody) {
		// TODO Auto-generated method stub
		commonDao.insertMsg0906(respMsgBody);	
	}

	@Override
	public void insertDzLog(DZOutputEntity object) {
		commonDao.insertDzLog(object);			
	}

	@Override
	public void insertLzLog(LZOutPutEntity object) {
		commonDao.insertLzLog(object);			
	}

	@Override
	public void insertGPSLog(PlaceOutputEntity object) {
		commonDao.insertGPSLog(object);			
	}

	@Override
	public void insertLedRegister(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		commonDao.insertLedRegister(paramMap);
	}

	@Override
	public List<LedLine> getLedLineList(String ledno) {
		// TODO Auto-generated method stub
		return commonDao.getLedLineList(ledno);
	}

	@Override
	public List<LedLineStation> getLedLineStationList(String ledno) {
		// TODO Auto-generated method stub
		return commonDao.getLedLineStationList(ledno);
	}

	@Override
	public List<Map<String, Object>> selectSafeCodeList() {
		// TODO Auto-generated method stub
		return commonDao.selectSafeCodeList();
	}

	@Override
	public List<LedResourcesItem> getLedResourcesByLedNo(String ledno) {
		// TODO Auto-generated method stub
		return commonDao.getLedResourcesByLedNo(ledno);
	}

	@Override
	public String getDistance() {
		return commonDao.getDistance();
	}

	@Override
	public String getValidDistance() {
		return commonDao.getValidDistance();
	}

	@Override
	public void insertCommonMsgBackDetail(HashMap<String, Object> dataMap) {
		// TODO Auto-generated method stub
		 commonDao.insertCommonMsgBackDetail(dataMap);
	}

	@Override
	public void insertLedMsgLogFromTemp(Map<String, Object> dataMap) {
		// TODO Auto-generated method stub
		commonDao.insertLedMsgLogFromTemp(dataMap);
	}
}
