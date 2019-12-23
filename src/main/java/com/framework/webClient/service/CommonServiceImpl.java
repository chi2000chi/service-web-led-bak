package com.framework.webClient.service;

import java.util.List;
import java.util.Map;
import org.apache.tools.ant.util.LeadPipeInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.framework.webClient.dao.CommonDao;
import com.framework.webClient.dao.LedAreaFileAttrDao;

/**
 * 
 * 文件名 CommonService
 * 描述 公共接口
 * @author 吉庆
 * 创建日期 2018年5月28日
 */
@Service
public class CommonServiceImpl implements ICommonService {

	@Autowired(required = true)
	private CommonDao commonDao;

	@Autowired(required = true)
	private LedAreaFileAttrDao ledAreaFileAttrDao;

	/**
	 * 查询线路信息
	 */
	@Override
	public List<Map<String, Object>> selectXlxx(Map<String, Object> paramMap) {
		return commonDao.selectXlxx(paramMap);
	}

	/**
	 * 查询站点信息
	 */
	@Override
	public List<Map<String, Object>> selectZdxx(Map<String, Object> paramMap) {
		return commonDao.selectZdxx(paramMap);
	}

	@Override
	public String selectFxByXlmcAndFangXiangZuhe(Map<String, Object> paramMap) {
		return commonDao.selectFxByXlmcAndFangXiangZuhe(paramMap);
	}


}
