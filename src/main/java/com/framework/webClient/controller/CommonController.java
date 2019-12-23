package com.framework.webClient.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.framework.webClient.service.ICommonService;
import com.framework.webClient.util.ConstantUtil;

/**
 * 
 * 文件名 CommonController 描述 公共接口类
 * 
 * @author 吉庆 创建日期 2018年5月28日
 */
@RestController
public class CommonController {

	@Autowired(required=true)
	private ICommonService commonService;

	/**
	 * 
	 * 查询线路信息和站点信息
	 * @param paramMap
	 * @return
	 */
	@PostMapping(value = "/selectXlxx")
	public List<Map<String, Object>> selectXlxx(@RequestParam Map<String, Object> paramMap){
		List<Map<String, Object>> xlxxList = commonService.selectXlxx(paramMap);
		// 存在tag标记使用全部标志
		if (paramMap.containsKey(ConstantUtil.COMMON_TAG)) {
			Map<String, Object> qbMap = new HashMap<>();
			qbMap.put(ConstantUtil.COMMON_XLID, ConstantUtil.NULL_STRING);
			qbMap.put(ConstantUtil.COMMON_XLMC, ConstantUtil.NULL_STRING);
			qbMap.put(ConstantUtil.COMMON_LPMC, ConstantUtil.COMMON_QUANBU);
			xlxxList.add(0, qbMap);
		}
		return xlxxList;
	}
	
	/**
	 * 
	 * 查询线路信息和站点信息
	 * @param paramMap
	 * @return
	 */
	@PostMapping(value = "/selectZdxx")
	public List<Map<String, Object>> selectZdxx(@RequestParam Map<String, Object> paramMap){
		List<Map<String, Object>> zdxxList = commonService.selectZdxx(paramMap);
		// 存在tag标记使用全部标志
		if (paramMap.containsKey(ConstantUtil.COMMON_TAG)) {
			Map<String, Object> qbMap = new HashMap<>();
			qbMap.put("zdid", ConstantUtil.NULL_STRING);
			qbMap.put("zdmc", ConstantUtil.COMMON_QUANBU);
			zdxxList.add(0, qbMap);
		}
		return zdxxList;
	}
}
