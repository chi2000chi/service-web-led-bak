package com.framework.webClient.service;import java.util.List;import java.util.Map;/** * @author 吉庆 * @date Mon Sep 10 00:00:00 CST 2018 */public interface ILedTemplateTempManageService {	/**	 * 	 * 查询公告信息	 * 	 * @param paramMap	 * @return	 */	public List<Map<String, Object>> selectLedTemplateTempByInfo(Map<String, Object> paramMap);		/**	 * 	 * 分区模版ID和分区类型查询分区公告设置信息	 * 	 * @param mbid 模版ID	 * @param fqlx 分区类型	 * @return	 */	public Map<String, Object> selectGgszByMbidAndFqlx(Map<String, Object> paramMap);		/**	 * 	 * 保存模版详情设置数据	 * 	 * @param mbid 模版ID	 * @return	 */	public Map<String, Object> saveMbTempinfo(Map<String, Object> paramMap);		/**	 * 通过Id删除该条数据	 * 	 * @param mbid 模版ID	 */	public int deleteLedTemplateTempById(String mbid);		/**	 * 	 * 查询公告关联电子站牌ID和名称	 * @param paramMap	 * @return	 */	public Map<String, Object> selectLedidAndLedmcBymcobhTemp(Map<String, Object> paramMap);		/**	 * 	 * 更新公告发布时间	 * @param paramMap	 * @return	 */	public int updateLedTemplateTempFbsjByMbid(Map<String, Object> paramMap);}