package com.framework.webClient.service;

import java.util.List;
import java.util.Map;

/**
 * 
 * 文件名 CommonService
 * 描述 公共接口
 * @author 吉庆
 * 创建日期 2018年5月28日
 */
public interface ICommonService {

	/**
	 * 
	 * 查询线路信息
	 * @param paramMap xlid 线路id
	 * @return
	 */
	public List<Map<String, Object>> selectXlxx(Map<String, Object> paramMap);
	
	/**
	 * 
	 * 查询站点信息
	 * @param paramMap xlid 线路id
	 * @return
	 */
	public List<Map<String, Object>> selectZdxx(Map<String, Object> paramMap);
	
	/**
	 * 
	 * 根据线路名称和站点组合查询运行方向
	 * @param paramMap xlmc 线路名称，FangXiangZuhe 方向组合（当前站点名称->下一站名称）
	 * @return
	 */
	public String selectFxByXlmcAndFangXiangZuhe(Map<String, Object> paramMap);
	
}
