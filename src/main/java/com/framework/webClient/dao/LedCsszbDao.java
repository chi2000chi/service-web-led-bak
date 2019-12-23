package com.framework.webClient.dao;import java.util.List;import java.util.Map;import com.framework.webClient.entity.LedCsszb;/** * @author 吉庆 * @date Thu Aug 16 00:00:00 CST 2018 */public interface LedCsszbDao {	/**	 * 通过Id查询详细信息	 * @param id  id编号 	 */	public List<LedCsszb> selectLedCsszbByMbinfo(Map<String, Object> paramMap);	/**	 * 通过Id删除该条数据	 * @param id  id编号 	 */	public int  deleteLedCsszbById(String mbid);	/**	 * 通过Id修改该条数据	 * @param model  实体封装类 	 */	public int updateLedCsszbById(LedCsszb ledcsszb);	/**	 * 新增一条数据	 * @param model  实体封装类 	 */	public int insertLedCsszb(LedCsszb ledcsszb);		/**	 * 	 * 查询参数模版下拉列表	 * @return	 */	public List<Map<String, Object>> selectCsmbCombox();		/**	 * 	 * 批量查询参数模版信息	 * @param paramList	 * @return	 */	public List<LedCsszb> selectCsmbInCsmbid(List<String> paramList);}