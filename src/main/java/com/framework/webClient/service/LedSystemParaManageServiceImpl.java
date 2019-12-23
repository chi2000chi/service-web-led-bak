package com.framework.webClient.service;import java.util.ArrayList;import java.util.Arrays;import java.util.HashMap;import java.util.Iterator;import java.util.List;import java.util.Map;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import com.framework.webClient.dao.CommonDao;import com.framework.webClient.dao.LedSystemParaDao;/** *  * 文件名  LedSystemParaManageServiceImpl * 描述  TODO(这里用一句话描述这个类的作用) * @auther 吉庆 * 创建日期  2018年11月15日 */@Servicepublic class LedSystemParaManageServiceImpl implements ILedSystemParaManageService {	@Autowired(required = true)	private LedSystemParaDao   ledsystemparadao;		@Autowired(required = true)	private CommonDao commonDao;	@Override	public Map<String, Object> selectSystemPara() {		Map<String, Object> resultMap = new HashMap<>();		// 查询所有线路		List<Map<String, Object>> xlxxList = commonDao.selectXlxx(null);		// 查询系统设置的信息		List<Map<String, Object>> systemParaList = ledsystemparadao.selectSystemPara();		// 使用的线路字符串列表		List<String> usedXlxxStringList = new ArrayList<>();		// 使用的线路列表		List<Map<String, Object>> usedXlxxList = new ArrayList<>();		// 广告显示屏切换时间		String ledAdShowTime = null;		// 线路显示屏切换时间		String ledLineShowTime = null;		for (Map<String, Object> systemParaMap : systemParaList) {			if (systemParaMap.get("paraNo").equals("un_show_line")) {				String unShowLine = String.valueOf(systemParaMap.get("paraValue"));				usedXlxxStringList = Arrays.asList(unShowLine.split(","));			}			if (systemParaMap.get("paraNo").equals("led_ad_show_time")) {				ledAdShowTime = String.valueOf(systemParaMap.get("paraValue"));			}			if (systemParaMap.get("paraNo").equals("led_line_show_time")) {				ledLineShowTime = String.valueOf(systemParaMap.get("paraValue"));			}		}		// 去掉已经使用的线路信息		if (usedXlxxStringList != null && !usedXlxxStringList.isEmpty()) {			// 移除站牌列表中已经分配的电子站牌			Iterator<Map<String, Object>> it = xlxxList.iterator();			while(it.hasNext()){				Map<String, Object> x = it.next();				for (String temp : usedXlxxStringList) {					if(String.valueOf(x.get("xlmc")).equals(temp)){						usedXlxxList.add(x);						it.remove();					} 				}			}		}		resultMap.put("usedXlxxList", usedXlxxList);		resultMap.put("xlxxList", xlxxList);		resultMap.put("ledAdShowTime", ledAdShowTime);		resultMap.put("ledLineShowTime", ledLineShowTime);		return resultMap;	}	/**	 * 	 * 根据paraNo更新	 * @param paramMap	 * @return	 */	@Override	@Transactional	public int updateSystemPara(Map<String, Object> paramMap) throws Exception {		for (Map.Entry<String, Object> entry : paramMap.entrySet()) {			Map<String, Object> saveMap = new HashMap<>();			saveMap.put("paraNo", entry.getKey());			saveMap.put("paraValue", paramMap.get(entry.getKey()));			ledsystemparadao.updateSystemPara(saveMap);		}		return 1;	}}