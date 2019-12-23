package com.framework.webClient.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.framework.webClient.service.ILedMsgManageService;

/**
 * 
 * 文件名 CommonController 描述 公共接口类
 * 
 * @author 吉庆 创建日期 2018年5月28日
 */
@RestController
public class LedMsgController {

	@Autowired(required=true)
	private ILedMsgManageService ledMsgManageService;

	/**
	 * 
	 * 查询线路信息和站点信息
	 * @param paramMap
	 * @return
	 */
	@PostMapping(value = "/selectLedMsg0906Byledno")
	public List<Map<String, Object>> selectLedMsg0906Byledno(@RequestParam String ledno){
		return ledMsgManageService.selectLedMsg0906Byledno(ledno);
	}
	
}
