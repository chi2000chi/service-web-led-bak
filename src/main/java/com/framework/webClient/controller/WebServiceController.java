package com.framework.webClient.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.jt808.common.DataCache;
import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.resp.LedLine;
import com.framework.jt808.vo.resp.LedLineBack;
import com.framework.jt808.vo.resp.LedLineStation;
import com.framework.jt808.vo.resp.LedLineStationBack;
import com.framework.jt808.vo.resp.LedResources;
import com.framework.jt808.vo.resp.LedResourcesItem;
import com.framework.jt808.vo.resp.LedResourcesItemArray;
import com.framework.jt808.vo.resp.LineMsg;
import com.framework.webClient.dispatch.receiver.LedUpReceiver;
import com.framework.webClient.service.ICommon808Service;
import com.framework.webClient.service.ILedMsgManageService;

/**
 * 
 * 文件名 CommonController 描述 公共接口类
 * 
 * @author 吉庆 创建日期 2018年5月28日
 */
@RestController
public class WebServiceController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired(required=true)
	private ILedMsgManageService ledMsgManageService;

	@Autowired
	private LedMsgProcessService ledMsgProcessService;
	ConcurrentHashMap<String, StationInfo> stationMap = new ConcurrentHashMap<String, StationInfo>();
	@Autowired
	private ICommon808Service common808Service;
	
	
	/**
	 * 
	 * 查询线路信息和站点信息
	 * @param paramMap
	 * @return
	 */
	@GetMapping(value = "/send0905")
	public void send0905(@RequestParam String ledno){
		try {
			ledMsgProcessService.service0905ToLed(ledno);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 查询线路信息和站点信息
	 * @param paramMap
	 * @return
	 */
	@GetMapping(value = "/sendLedLine")
	public void sendLedLine(@RequestParam String ledno){
		try {
			ledMsgProcessService.sendLedLine(ledno);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 下发末班车已过信息
	 * @param paramMap
	 * @return
	 */
	@GetMapping(value = "/sendLedLastBus")
	public void sendLedLastBus(@RequestParam String ledno,String xlbh){
		try {
			ledMsgProcessService.sendLedLastBus(ledno,xlbh);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 
	 * 查询线路信息和站点信息
	 * @param paramMap
	 * @return
	 */
	@GetMapping(value = "/refreshData")
	public void refreshData(){
		try {			

			logger.info("refreshDataTimer 开始 ");
			long startTime = System.currentTimeMillis();
			List<StationInfo> dataList = common808Service.selectStaionListAll();
			if(DataCache.stationQueue !=null && !DataCache.stationQueue.isEmpty()) {
				DataCache.stationQueue.clear();	
			}
			logger.info("refreshDataTimer 站点数列表:{}", dataList.size());
			DataCache.stationQueue.addAll(dataList);
			if(dataList != null && dataList.size() > 0) {		
				stationMap.clear();
				for (StationInfo station : dataList) {
					stationMap.put(station.getXlmc()+"-"+station.getFx()+"-"+station.getZdxh(), station);
		        }
				DataCache.stationMap.clear();
				DataCache.stationMap.putAll(stationMap);
			}
			logger.info("refreshDataTimer 站点Map个数:{}", DataCache.stationMap.size());
			String unShowLineStr = common808Service.getUnShowLine();
			if(unShowLineStr != null && !"".equals(unShowLineStr.trim())) {
				DataCache.unShowLineList = Arrays.asList(unShowLineStr.split(","));
			}
			DataCache.ledMsgList = ledMsgManageService.selectLedMsgInfo();
			List<Map<String, Object>> safeCodeList = common808Service.selectSafeCodeList();
			if(DataCache.safeCodeQueue !=null && !DataCache.safeCodeQueue.isEmpty()) {
				DataCache.safeCodeQueue.clear();
			}
			DataCache.safeCodeQueue.addAll(safeCodeList);
			logger.info("refreshDataTimer 截止");
			long endTime = System.currentTimeMillis();
			float excTime = (float) (endTime - startTime) / 1000;
			logger.info("refreshDataTimer 执行时间:{}s", excTime);
		} catch (Exception e) {
			logger.info("ZFMsgThread 异常");
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value = "/selectLedLineByLedNo")
	public String selectLedLineByLedNo(@RequestParam String ledno,@RequestParam String safeCode) throws JsonGenerationException, JsonMappingException, IOException{
		if(ledno == null || "".equals(ledno.trim()) || safeCode == null || "".equals(safeCode.trim())) return "";
		List<Map<String, Object>> dataMapList = DataCache.safeCodeQueue.stream().filter(p -> ledno.equals(p.get("ledbh")))
				.collect(Collectors.toList());
		if(dataMapList == null || dataMapList.size() == 0) return "";
		Map<String, Object> data = dataMapList.get(0);
		if(!safeCode.equals(data.get("safecode").toString())) return "";
		List<LedLine> ledLineList = common808Service.getLedLineList(ledno);
		List<LedLineStation> ledLineStationList = common808Service.getLedLineStationList(ledno);
		if(ledLineList == null ||ledLineList.size() == 0) return "";
		List<LedLineStation> currlineStation  = null;
		long startTime = System.currentTimeMillis();
		for(LedLine ledLine : ledLineList) {
			if(ledLineStationList != null &&ledLineStationList.size() > 0) {
				currlineStation = ledLineStationList.stream()
						.filter(s -> s.getLedid().equals(ledLine.getLedid())
						&& s.getYxfx()==ledLine.getRunDirection() && ledLine.getID().equals(String.valueOf(s.getLineNo()))).distinct()
				.collect(Collectors.toList());
				if(currlineStation != null &&currlineStation.size() > 0) {
					List<LedLineStationBack> resultList = currlineStation.stream().map(temp -> {
						LedLineStationBack obj = new LedLineStationBack();
						obj.setId(temp.getId());
			            obj.setName(temp.getName());
			            obj.setPriceArea(temp.getPriceArea());
			            return obj;
			        }).collect(Collectors.toList());
					Collections.sort(resultList, (a, b) -> Integer.parseInt(a.getId()) - Integer.parseInt(b.getId()));
					if(resultList.get(0) != null) {
						ledLine.setBeginStation(resultList.get(0).getName());
					}
					if(resultList.get(resultList.size() - 1) != null) {
						ledLine.setEndStation(resultList.get(resultList.size() - 1).getName());
					}
					ledLine.setStations(resultList);
				}
			}
			if(ledLine.getZdtk() == 1) {
				Map<String, Object> dataMap = DataCache.ledMsgList.stream().filter(s -> "2".equals(s.get("msgno").toString())).findAny().orElse(null); 
				if(dataMap != null) {
					LineMsg lineMsg = new LineMsg();
					lineMsg.setBgColor(dataMap.get("bgcolor").toString());
					lineMsg.setFontColor(dataMap.get("fontcolor").toString());
					lineMsg.setFontSize(dataMap.get("fontsize").toString());
					lineMsg.setMsg(dataMap.get("msginfo").toString());
					lineMsg.setMsgArea(dataMap.get("showarea").toString());
					ledLine.setLedMsg(lineMsg);
				}
			}
			if (DataCache.unShowLineList != null && DataCache.unShowLineList.size() >0 && DataCache.unShowLineList.contains(ledLine.getID())) {
				Map<String, Object> dataMap = DataCache.ledMsgList.stream().filter(s -> "3".equals(s.get("msgno").toString())).findAny().orElse(null); 
				if(dataMap != null) {
					LineMsg lineMsg = new LineMsg();
					lineMsg.setBgColor(dataMap.get("bgcolor").toString());
					lineMsg.setFontColor(dataMap.get("fontcolor").toString());
					lineMsg.setFontSize(dataMap.get("fontsize").toString());
					lineMsg.setMsg(dataMap.get("msginfo").toString());
					lineMsg.setMsgArea(dataMap.get("showarea").toString());
					ledLine.setLedMsg(lineMsg);
				}	
			}
		}
		List<LedLineBack> resultList = ledLineList.stream().map(temp -> {
			LedLineBack obj = new LedLineBack();
			obj.setID(temp.getID());
            obj.setName(temp.getName());
            obj.setSName(temp.getSName());
            obj.setRunDirection(temp.getRunDirection());
            obj.setBeginStation(temp.getBeginStation());
            obj.setEndStation(temp.getEndStation());
            obj.setBeginTime(temp.getBeginTime());
            obj.setEndTime(temp.getEndTime());
            obj.setInterval(temp.getInterval());
            obj.setPrice(temp.getPrice());
            obj.setStationSN(temp.getStationSN());
            obj.setStationDirection(temp.getStationDirection());
            obj.setStations(temp.getStations());
            obj.setBasemsg(temp.getLedMsg());
            return obj;
        }).collect(Collectors.toList());
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter stringEmp = new StringWriter();
        objectMapper.writeValue(stringEmp, resultList);
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
	//	logger.info("执行时间:{}s ", excTime);
		return stringEmp.toString();
	}
	
	

	@RequestMapping(value = "/selectLedResourcesByLedNo")
	public String selectLedResourcesByLedNo(@RequestParam String ledno,@RequestParam String safeCode) throws JsonGenerationException, JsonMappingException, IOException{
		if(ledno == null || "".equals(ledno.trim()) || safeCode == null || "".equals(safeCode.trim())) return "";
		List<Map<String, Object>> dataMapList = DataCache.safeCodeQueue.stream().filter(p -> ledno.equals(p.get("ledbh")))
				.collect(Collectors.toList());
		if(dataMapList == null || dataMapList.size() == 0) return "";
		Map<String, Object> data = dataMapList.get(0);
		if(!safeCode.equals(data.get("safecode").toString())) return "";
		List<LedResourcesItem> ledResourseList = common808Service.getLedResourcesByLedNo(ledno);
		long startTime = System.currentTimeMillis();
		Map<String, List<LedResourcesItem>> detailsMap01 = ledResourseList.stream()
				.collect(Collectors.groupingBy(LedResourcesItem::getName));
		LedResources resourse = new LedResources();
		List<LedResourcesItemArray> resourcesList = new ArrayList<LedResourcesItemArray>();
		 for (String key : detailsMap01.keySet()) {
			 List<LedResourcesItem> itemAry =  detailsMap01.get(key);
			 LedResourcesItemArray item = new LedResourcesItemArray();
			 item.setName(key);
			 List<String> usrList=itemAry.stream().map(LedResourcesItem::getUrl).collect(Collectors.toList());
			 item.setUrl(usrList);
			 resourcesList.add(item);
	     }
		resourse.setResources(resourcesList);
		ObjectMapper objectMapper = new ObjectMapper();
		StringWriter stringEmp = new StringWriter();
        objectMapper.writeValue(stringEmp, resourse);
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
	//	logger.info("执行时间:{}s ", excTime);
		return stringEmp.toString();
	}
}
