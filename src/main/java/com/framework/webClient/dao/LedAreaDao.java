package com.framework.webClient.dao;import java.util.List;import java.util.Map;/** * @author 吉庆 * @date Fri Aug 17 00:00:00 CST 2018 */public interface LedAreaDao {	public int deleteLedAreaById(Map<String, Object> paramMap);		/**	 * 	 * 添加分区表	 * @param paramMap	 * @return	 */	public int insertLedArea(List<Map<String, Object>> paramMap);}