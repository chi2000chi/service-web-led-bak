package com.framework.webClient.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.framework.jt808.common.DataCache;
import com.framework.jt808.vo.resp.MsgBody0906;



@RestController
public class GetpPrevious0906 {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//获取一个站牌里所有线路 上一次发送的数据，在接收up mq的项目里调用接口取回数据，在站牌登录后就下发下车辆信息
	//@RequestMapping("/getPreviousCarMap")
	@GetMapping(value = "/getPreviousCarMap")
	public synchronized Map getLineEtaCacheMap(String str) {// str 存一个站牌的所有 站牌编号-线路id ,分隔
			
		Map<String,String> returnMap=new HashMap<String,String>();
		//logger.info("str:"+str);
		if(str == null || "".equals(str)) {// 传过来的是空串 返回null
			return null;
		}else {
			String[] keyList= str.split(",");
			for (String key:keyList) {
				if(key !=null && !"".equals(key)) {// key不能是空
					MsgBody0906 pCar= DataCache.previousCarMap.get(key);
					if(pCar != null) {// 能在map 缓存车辆里取到数据
						String pCarStr=JSON.toJSONString(pCar);
						returnMap.put(key, pCarStr);// 取出的数据存到返回的map里
					}
				}
			}
		}
		return returnMap;
	}
}
