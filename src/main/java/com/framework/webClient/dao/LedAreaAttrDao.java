package com.framework.webClient.dao;import java.util.List;import java.util.Map;/** * @author 吉庆 * @date Fri Aug 17 00:00:00 CST 2018 */public interface LedAreaAttrDao {	public int deleteLedAreaAttrById(Map<String, Object> paramMap);		/**	 * 	 * 添加分区属性表	 * @param paramList	 * @return	 */	public int insertLedAreaAttr(List<Map<String, Object>> paramList);}