/**  
 * @Title  LedListController.java
 * @Package com.framework.webClient.controller
 * @author 吉庆
 * @date 2018年8月27日
 */
package com.framework.webClient.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.thrift.service.gen.JtlSysUser;
import com.framework.util.StringConvertUtils;
import com.framework.webClient.entity.LedCsszb;
import com.framework.webClient.service.ICommonService;
import com.framework.webClient.service.ILedCsszbManageService;
import com.framework.webClient.service.ILedLedManageService;
import com.framework.webClient.service.ILedParaManageService;
import com.framework.webClient.service.ILedTemplateManageService;
import com.framework.webClient.service.ILedUploadParaManageService;
import com.framework.webClient.util.ConstantUtil;
import com.framework.webClient.util.ImportExcel2007Util;
import com.framework.webClient.util.JsonUtils;
import com.framework.webClient.util.UUIDTools;
import com.framework.webClient.util.WebDataUtil;

/**
 * 文件名 LedListController 描述 电子站牌列表
 * 
 * @auther 吉庆 创建日期 2018年8月27日
 */
@RestController
public class LedListController {

	@Autowired(required = true)
	private ILedLedManageService ledLedManageService;

	@Autowired(required = true)
	private ILedParaManageService ledParaManageService;
	
	@Autowired
	private ILedCsszbManageService ledcsszbmanageservice;
	
	@Autowired
	private ILedTemplateManageService ledtemplatemanageservice;
	
	@Autowired(required=true)
	private ICommonService commonService;
	
	@Autowired
	private ILedUploadParaManageService ledUploadParaManageService;
	
	@Autowired
	private LedMsgProcessService ledMsgProcessService;

	/**
	 * 
	 * 初始化电子站牌列表页面
	 * 
	 * @return
	 */
	@GetMapping(value = "/ledlist")
	public ModelAndView ledList() {
		ModelAndView mv = new ModelAndView("ledlist");
		return mv;
	}
	
	
	/**
	 * 
	 * 初始化电子站牌列表页面(公交处增加站牌用)
	 * 
	 * @return
	 */
	@GetMapping(value = "/ledlistadd")
	public ModelAndView ledListAdd() {
		ModelAndView mv = new ModelAndView("ledlistadd");
		return mv;
	}
	

	/**
	 * 
	 * 查询电子站牌列表
	 * 
	 * @param paramMap
	 * @return
	 */
	@PostMapping(value = "/selectLedList")
	public List<Map<String, Object>> selectLedList(@RequestParam Map<String, Object> paramMap) {
		List<Map<String, Object>> ledList = new ArrayList<>();
		try {
			if (paramMap.containsKey("zxtx") && paramMap.get("zxzt").equals("1")) {
				paramMap.put("zxzt", "在线");
			} else if (paramMap.containsKey("zxtx") && paramMap.get("zxzt").equals("0")) {
				paramMap.put("zxzt", "离线");
			}
			ledList = ledLedManageService.selectLedList(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ledList;
		}
		return ledList;
	}
	
	/**
	 * 
	 * 查询电子站牌列表(公交处增加站牌用)
	 * 
	 * @param paramMap
	 * @return
	 */
	@PostMapping(value = "/selectLedListAdd")
	public List<Map<String, Object>> selectLedListAdd(@RequestParam Map<String, Object> paramMap) {
		List<Map<String, Object>> ledList = new ArrayList<>();
		try {
			/*if (paramMap.containsKey("zxtx") && paramMap.get("zxzt").equals("1")) {
				paramMap.put("zxzt", "在线");
			} else if (paramMap.containsKey("zxtx") && paramMap.get("zxzt").equals("0")) {
				paramMap.put("zxzt", "离线");
			}*/
			ledList = ledLedManageService.selectLedListAdd(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			return ledList;
		}
		return ledList;
	}
	

	/**
	 *
	 * 保存电子站牌信息
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/saveLedList")
	public Map<String, Object> saveLedList(@RequestParam Map<String, Object> paramMap, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			// 获取用户信息
			//JtlSysUser jtlSysUser = WebDataUtil.getSysUserInfo(session);
			// 获取用户信息
			//String userName = jtlSysUser.getUserName();
			//paramMap.put("xgr", userName);
			//paramMap.put("cjr", userName);
			//简易版的设置
			paramMap.put("xgr", "easy");
			paramMap.put("cjr", "easy");
			
			
			paramMap.put("bz", "无");
			String addListString = String.valueOf(paramMap.get("addList"));
			String delListString = String.valueOf(paramMap.get("delList"));
			String updateListString = String.valueOf(paramMap.get("updateList"));
			List<Map<String, Object>> addList = JsonUtils.convertString2Obj(addListString, List.class);
			List<Map<String, Object>> delList = JsonUtils.convertString2Obj(delListString, List.class);
			List<Map<String, Object>> updateList = JsonUtils.convertString2Obj(updateListString, List.class);
			paramMap.put("addList", addList);
			paramMap.put("delList", delList);
			paramMap.put("updateList", updateList);
			// 执行数据存储
			int flag = ledLedManageService.saveLedList(paramMap);
			if (flag == 1) {
				resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ONE);
				resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_SUCCESS);
			} else  if (flag == 0) {
				resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
				resultMap.put(ConstantUtil.MSG, "站牌编号重复，禁止添加");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
			resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_WZYY_FAILURE);
		}
		return resultMap;
	}

	/**
	 * 
	 * 删除电子站牌信息
	 * 
	 * @param paramMap
	 * @return
	 */
	@PostMapping(value = "/delLedList")
	public Map<String, Object> delLedList(@RequestParam Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			int flag = ledLedManageService.delLedList(paramMap);
			if (flag == 1) {
				resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ONE);
				resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_SUCCESS);
			} else {
				resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
				resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_FAILURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
			resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_WZYY_FAILURE);
		}
		return resultMap;
	}

	/**
	 * 
	 * 电子站牌查询参数
	 * 
	 * @return
	 */
	@PostMapping(value = "/selectLedParaInfo")
	public Map<String, Object> selectLedParaInfo(@RequestParam Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String ledid = String.valueOf(paramMap.get("ledid"));
			if (StringConvertUtils.isEmpty(ledid)) {
				resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
				resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_FAILURE);
				return resultMap;
			}
			// 调用0x0903协议
			List<String> ledidList = new ArrayList<>();
			ledidList.add(ledid);
			ledMsgProcessService.service0903ToLed(ledidList);
			// 延时5秒后执行，防止调用0x0903协议后，查询upload库中的数据为空，出现数据异常情况
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 调用0x0903协议后，业务修改为在ledUploadPara表中查询，存入ledParaMap中
			Map<String, Object> ledParaMap = ledUploadParaManageService.selectLedUploadParaInfoByLedid(paramMap);
			// 将返回值插入到数据库
			if (ledParaMap != null && !ledParaMap.isEmpty()) {
				resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ONE);
				resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_SUCCESS);
				// 入库
				int flag = ledParaManageService.updateLedParaByLedid(ledParaMap);
				if (flag == 1) {
					resultMap.put(ConstantUtil.STATUS, ConstantUtil.COMMONS_ONE);
					resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_SUCCESS);
					Map<String, Object> returnLedPara = ledParaManageService.selectLedParaInfo(paramMap);
					resultMap.putAll(returnLedPara);
				} else {
					resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
					resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_FAILURE);
				}
			} else {
				resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ONE);
				resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_SUCCESS);
				Map<String, Object> returnLedPara = ledParaManageService.selectLedParaInfo(paramMap);
				resultMap.putAll(returnLedPara);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
			resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_WZYY_FAILURE);
		}
		return resultMap;
	}

	/**
	 * 
	 * 保存或保存并下发参数
	 * 
	 * @param paramMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping(value = "/saveOrSaveSendLedPara")
	public Map<String, Object> saveOrSaveSendLedPara(@RequestParam Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String ledidListString = String.valueOf(paramMap.get("ledidList"));
			String ledParaString = String.valueOf(paramMap.get("ledPara"));
			List<String> ledList = JsonUtils.convertString2Obj(ledidListString, List.class);
			Map<String, Object> ledParaMap = JsonUtils.convertString2Obj(ledParaString, Map.class);
			List<Map<String, Object>> ledParaList = new ArrayList<>();
			// 整理数据
			for (String ledid : ledList) {
				Map<String, Object> paraTempMap = new HashMap<>();
				paraTempMap.put("ledid", ledid);
				paraTempMap.put("tcpycfwqipdz", ledParaMap.get("edittcpip"));
				paraTempMap.put("yctcpfwdk", ledParaMap.get("edittcpport"));
				paraTempMap.put("sbyxksrq", ledParaMap.get("editsbyxsjks"));
				paraTempMap.put("sbyxjzrq", ledParaMap.get("editsbyxsjjs"));
				paraTempMap.put("xhsjdkssj", ledParaMap.get("editmtxhsjd1"));
				paraTempMap.put("xhsjdjzsj", ledParaMap.get("editmtxhsjd2"));
				paraTempMap.put("sbwdfwks", String.valueOf(ledParaMap.get("editsbwkfw")).substring(0, String.valueOf(ledParaMap.get("editsbwkfw")).lastIndexOf(",")));
				paraTempMap.put("sbwdfwjz", String.valueOf(ledParaMap.get("editsbwkfw")).substring(String.valueOf(ledParaMap.get("editsbwkfw")).lastIndexOf(",") + 1));
				paraTempMap.put("wbfsks", String.valueOf(ledParaMap.get("editwbfs")).substring(0, String.valueOf(ledParaMap.get("editwbfs")).lastIndexOf(",")));
				paraTempMap.put("wbfsjz", String.valueOf(ledParaMap.get("editwbfs")).substring(String.valueOf(ledParaMap.get("editwbfs")).lastIndexOf(",") + 1));
				paraTempMap.put("hlfsks", String.valueOf(ledParaMap.get("edithlfs")).substring(0, String.valueOf(ledParaMap.get("edithlfs")).lastIndexOf(",")));
				paraTempMap.put("hlfsjz", String.valueOf(ledParaMap.get("edithlfs")).substring(String.valueOf(ledParaMap.get("edithlfs")).lastIndexOf(",") + 1));
				paraTempMap.put("jwmkks", String.valueOf(ledParaMap.get("editjwmk")).substring(0, String.valueOf(ledParaMap.get("editjwmk")).lastIndexOf(",")));
				paraTempMap.put("jwmkjz", String.valueOf(ledParaMap.get("editjwmk")).substring(String.valueOf(ledParaMap.get("editjwmk")).lastIndexOf(",") + 1));
				paraTempMap.put("kzms", ledParaMap.get("editkzms"));
				paraTempMap.put("dwfw", ledParaMap.get("editdwfw"));
				paraTempMap.put("sbglbhks", String.valueOf(ledParaMap.get("editsbglbh")).substring(0, String.valueOf(ledParaMap.get("editsbglbh")).lastIndexOf(",")));
				paraTempMap.put("sbglbhjz", String.valueOf(ledParaMap.get("editsbglbh")).substring(String.valueOf(ledParaMap.get("editsbglbh")).lastIndexOf(",") + 1));
				paraTempMap.put("sbsjbh", ledParaMap.get("editsbsjbh"));
				paraTempMap.put("sbfwzq", ledParaMap.get("editsbfwcq"));
				ledParaList.add(paraTempMap);
				// 更新数据
				int flag = ledParaManageService.updateLedParaByLedid(paraTempMap);
				if (flag == 1) {
					resultMap.put(ConstantUtil.STATUS, ConstantUtil.COMMONS_ONE);
					resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_SUCCESS);
				} else {
					resultMap.put(ConstantUtil.STATUS, ConstantUtil.COMMONS_ZERO);
					resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_FAILURE);
				}
			}
			// 判断是否调用0x0902协议
			if (String.valueOf(paramMap.get("isSend")).equals("true")) {
				// 调用协议，发送数据集合 电子站牌编号
				List<String> ledbhList = ledLedManageService.selectLedbhByLedidList(ledList);
				ledMsgProcessService.service0902ToLed(ledbhList);
				resultMap.put(ConstantUtil.MSG, "保存成功，并下发参数");

			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
			resultMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_WZYY_FAILURE);
		}
		return resultMap;
	}

	/**
	 * 
	 * 导入电子站牌
	 * 
	 * @param request
	 * @param file
	 *            导入的文件对象
	 * @returntish 提示消息
	 */
	@PostMapping(value = ConstantUtil.IMPORTLEDEXCEL)
	public Map<String, Object> importLedExcel(HttpServletRequest request, @RequestParam(ConstantUtil.FILE) MultipartFile file, HttpSession session) {
		Map<String, Object> resMap = new HashMap<>();
		// 存放解析完的excel数据
		List<Map<String, Object>> importList = new ArrayList<Map<String, Object>>();
		// 存放解析完的标题数据
		Map<String, Object> titleStringMap = new HashMap<String, Object>();
		String title = null;
		// 声明并初始化Excel文件中列名的集合
		List<String> colList = new ArrayList<String>();
		// 以下列名顺序需要与Excel文件中列名顺序匹配
		String[] titleArray = ConstantUtil.ANY_CONSTANT_IMPORT_TITLEVAR.split(ConstantUtil.DOUHAO);
		colList = Arrays.asList(titleArray);
		// 判断导入的文件是否为空
		if (null != file) {
			// 验证导入文件标题是否与导入模板匹配
			try {
				titleStringMap = ImportExcel2007Util.getImportExcelTitle(ConstantUtil.ANY_CONSTANT_IMPORT_DATAROW, file.getInputStream(),
						ConstantUtil.ANY_CONSTANT_IMPORT_COUNTCOL, null);
				if (null != titleStringMap) {
					// 判断是否有错误消息写入Map中
					if (null == titleStringMap.get(ConstantUtil.ERRORMSG)) {
						// 获取标题
						title = String.valueOf(titleStringMap.get(ConstantUtil.TITLESTRING));
						// 判断导入标题是否与模板标题匹配
						if (ConstantUtil.ANY_CONSTANT_IMPORT_TITLE.equals(title)) {
							// 将导入的Excel文件内容存储到List中
							importList = ImportExcel2007Util.doImmportExcel(ConstantUtil.ANY_CONSTANT_IMPORT_DATAROW, file.getInputStream(),
									ConstantUtil.ANY_CONSTANT_IMPORT_COUNTCOL, colList);
							if (!importList.isEmpty()) {
								// 调用验证方法
								resMap = validateXcjh(formatImportExcelData(importList));
								// 通过校验，执行保存操作
								if (resMap.get(ConstantUtil.STATUS).equals(ConstantUtil.STATUS_ONE)) {
									@SuppressWarnings("unchecked")
									List<Map<String, Object>> paramList = (List<Map<String, Object>>) resMap.get("data");
									for (Map<String, Object> paramMap : paramList) {
										// 验证通过后进行保存操作// 获取用户信息
										JtlSysUser jtlSysUser = WebDataUtil.getSysUserInfo(session);
										// 获取用户信息
										String userName = jtlSysUser.getUserName();
										paramMap.put("xgr", userName);
										paramMap.put("cjr", userName);
										paramMap.put("bz", "无");
										paramMap.put("addList", paramMap.get("stationString"));
										// 默认导入的电子站牌启用状态都是禁用
										paramMap.put("qyzt", ConstantUtil.COMMONS_ZERO);
										paramMap.remove("stationString");
									}
									
									// 执行数据批量存储
									int flag = ledLedManageService.saveInLedList(resMap);
									if (flag == 1) {
										resMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ONE);
										resMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_SUCCESS);
									} else {
										resMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
										resMap.put(ConstantUtil.MSG, ConstantUtil.COMMON_FAILURE);
									}
								}
							}
						} else {
							resMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
							resMap.put(ConstantUtil.MSG, ConstantUtil.IMPORTEXCEL_TITLE_ERROR);
						}
					} else {
						resMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
						resMap.put(ConstantUtil.MSG, titleStringMap.get(ConstantUtil.ERRORMSG));
					}
				} else {
					resMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
					resMap.put(ConstantUtil.MSG, ConstantUtil.IMPORTEXCEL_TITLE_NULL);
				}
			} catch (Exception e) {
				e.printStackTrace();
				resMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
				resMap.put(ConstantUtil.MSG, ConstantUtil.IMPORTEXCEL_FAILE);
			}
		}
		return resMap;
	}
	
	/**
	 * 格式化数据，用来整合成验证方法需要的数据格式
	 * 
	 * @param paramList
	 * @return
	 */
	public Map<String, Object> formatImportExcelData(List<Map<String, Object>> paramList){
		Map<String, Object> resultMap = new HashMap<>();
		// 存放电子站牌编号集合，用来判断是否有重复的
		List<String> ledbhList = new ArrayList<>();
		List<Map<String, Object>> formList = new ArrayList<>();
		// 用来区别是否是新的数据，不用空是防止用户填写空编号，出现问题。
		String validateLedbh = "valdateLedbh";
		Map<String, Object> formatMap = new HashMap<>();
		// 关联站点列表字段
		String stationString = "";
		// 对导出数据进行整理
		for(Map<String, Object> temp : paramList) {
			String ledbh = String.valueOf(temp.get("ledbh"));
			// 根据站点校验方向信息 用xlmc和fangXiangZuhe 模糊匹配sql整合好的数据
			String fx = String.valueOf(temp.get("fx"));
			
			if (StringConvertUtils.isEmpty(fx)) {
				fx = "";
			}
			// 不相同，那么清空，相同，那么就继续拼装数据
			if (!ledbh.equals(validateLedbh)) {
				validateLedbh = ledbh;
				ledbhList.add(ledbh);
				if (formatMap != null && !formatMap.isEmpty()) {
					formList.add(formatMap);
				}
				stationString = "";
				formatMap = new HashMap<>();
				formatMap.put("ledmc", temp.get("ledmc"));
				formatMap.put("ledbh", temp.get("ledbh"));
				formatMap.put("jd", temp.get("jd"));
				formatMap.put("wd", temp.get("wd"));
				formatMap.put("ledfx", ConstantUtil.SHANGXING);
				formatMap.put("wg", temp.get("wg"));
				formatMap.put("bdsbipdz", temp.get("bdsbipdz"));
				formatMap.put("zwym", temp.get("zwym"));
				formatMap.put("csmbmc", temp.get("csmbmc"));
				formatMap.put("fqmbmc", temp.get("fqmbmc"));
				formatMap.put(ConstantUtil.SHEETNAME, temp.get(ConstantUtil.SHEETNAME));
			}
			// 组合数据
			stationString = stationString + temp.get("xlmc") + "," + fx + "," + temp.get("dqzdmc") + ";";
			formatMap.put("stationString", stationString);
		}
		// 添加最后一条记录数据组合完成的map
		formList.add(formatMap);
		// 清洗数据，去掉stationString中最后的分号;
		for (Map<String, Object> temp : formList) {
			temp.put("stationString", String.valueOf(temp.get("stationString")).substring(0, String.valueOf(temp.get("stationString")).lastIndexOf(";")));
		}
		// 检验电子站牌编号是否在模版中重复，没有重复的就能保证在保存的时候，插入的电子站牌编号是有效数据
		Set<String> set = new HashSet<>();
	    ledbhList.stream().forEach(p -> {
	        set.add(p);
	    });
	    if (set.size() != ledbhList.size()) {
	        resultMap.put("errorMSG", "导入模版中存在重复电子站牌编号，请删除");
	    }
	    resultMap.put("ledbhList", ledbhList);
	    resultMap.put("formList", formList);
		return resultMap;
	}

	/**
	 * 
	 * 校验导入行车计划明细的方法
	 * 
	 * @param paramList
	 *            导入的站牌信息
	 * @return 返回status状态
	 * @return message消息
	 * @return datas数据
	 * @throws Exception
	 */
	public Map<String, Object> validateXcjh(Map<String, Object> formMap) throws Exception {
		Map<String, Object> resMap = new HashMap<>();
		// 存放错误提示消息
		String errorMsg = String.valueOf(formMap.get("errorMSG"));
		// 存放导入的所有的电子站牌编号
		@SuppressWarnings("unchecked")
		List<String> ledbhList = (List<String>) formMap.get("ledbhList");
		// 需要保存的电子站牌集合
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> paramList = (List<Map<String, Object>>) formMap.get("formList");
		// 存在的电子站牌id
		List<String> ledidList = new ArrayList<>();
		// 存放参数模版集合
		List<String> csmbList = new ArrayList<>();
		if (StringConvertUtils.isEmpty(errorMsg)) {
			// 查询数据库中存在的电子站牌，用来做筛选处理，并将存在的电子站牌id保存到数据集合中
			Map<String, Object> ledbhidMap = ledLedManageService.selectLedbhAndIdByLedbhList(ledbhList);
			// 错误消息
			errorMsg = ConstantUtil.NULL_STRING;
			// 用来提示用户第几行有问题
			int i = 0;
			// IP正则表达式
			String reg = ConstantUtil.IPREG;
			Pattern pattern = Pattern.compile(reg);
			// 校验数据
			for (Map<String, Object> rowMap : paramList) {
				// 第i条记录
				i++;
				// 获取sheet页名称
				String sheetName = String.valueOf(rowMap.get(ConstantUtil.SHEETNAME));
				// 站牌名称
				String ledmc = String.valueOf(rowMap.get("ledmc"));
				// 站牌编号
				String ledbh = String.valueOf(rowMap.get("ledbh"));
				// 经度
				String jd = String.valueOf(rowMap.get("jd"));
				// 纬度
				String wd = String.valueOf(rowMap.get("wd"));
				// 方向
				String ledfx = String.valueOf(rowMap.get("ledfx"));
				// 网关
				String wg = String.valueOf(rowMap.get("wg"));
				// 本地设备IP地址
				String bdsbipdz = String.valueOf(rowMap.get("bdsbipdz"));
				// 子网掩码
				String zwym = String.valueOf(rowMap.get("zwym"));
				// 关联站点列表
				String stationString = String.valueOf(rowMap.get("stationString"));
				// 判断是否需要重新生成电子站牌id
				if (StringConvertUtils.isEmpty(ledbhidMap.get(ledbh))) {
					rowMap.put("ledid", UUIDTools.getUUID());
				} else {
					rowMap.put("ledid", ledbhidMap.get(ledbh));
					ledidList.add(String.valueOf(ledbhidMap.get(ledbh)));
				}
				
				// 校验电子站牌名称
				if (StringConvertUtils.isEmpty(ledmc)) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，站牌名称不能为空;" + ConstantUtil.HUANHANG;
				} 
				Map<String, Object> valMap = new HashMap<>();
				// 校验电子站牌编号
				if (StringConvertUtils.isEmpty(ledbh)) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，站牌编号不能为空;" + ConstantUtil.HUANHANG;
				} else if (ledmc.length() > 40) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，站牌编号不能超过40位;" + ConstantUtil.HUANHANG;
				}
				valMap.put("tag", "1");
				valMap.put("ledmc", ledbh);
				// 校验经度
				if (StringConvertUtils.isEmpty(jd)) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，经度不能为空;" + ConstantUtil.HUANHANG;
				} else if (jd.length() < 1 || jd.length() > 13) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，经度不正确，请输入1-13位数据;" + ConstantUtil.HUANHANG;
				}
				// 校验纬度
				if (StringConvertUtils.isEmpty(wd)) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，经度不能为空;" + ConstantUtil.HUANHANG;
				} else if (wd.length() < 1 || wd.length() > 13) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，经度不正确，请输入1-13位数据;" + ConstantUtil.HUANHANG;
				}
				// 校验方向
				if (StringConvertUtils.isEmpty(ledfx)) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，方向不能为空;" + ConstantUtil.HUANHANG;
				} else if (!ledfx.equals("上行") && !ledfx.equals("下行")) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，方向不是上行或下行，不允许添加!;" + ConstantUtil.HUANHANG;
				} else if (ledfx.equals("上行")) {
					rowMap.put("ledfx", "0");
				} else if (ledfx.equals("下行")) {
					rowMap.put("ledfx", "1");
				}
				Map<String, Object> csmap = new HashMap<>();
				csmap.put("mbmc", rowMap.get("csmbmc"));
				csmap.put("tag", "1");
				// 根据参数模版名称查询参数模版ID
				List<LedCsszb> ledcsszb = ledcsszbmanageservice.selectLedCsszbByMbinfo(csmap);
				csmap.remove("mbmc");
				csmap.remove("tag");
				// 校验是否存在参数模版名称
				if (ledcsszb == null || ledcsszb.isEmpty()) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，参数模版名称不存在;" + ConstantUtil.HUANHANG;
				} else {
					for (LedCsszb cs : ledcsszb) {
						rowMap.put("csmbid", cs.getMbid());
						csmbList.add(cs.getMbid());
					}
				}
				// 校验网关
				Matcher wgm = pattern.matcher(wg);
				if (StringConvertUtils.isEmpty(wg)) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，网关不能为空;" + ConstantUtil.HUANHANG;
				} else if (!wgm.find()) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，网关不正确;" + ConstantUtil.HUANHANG;
				}
				// 校验本地设备IP地址
				Matcher bdsbipdzm = pattern.matcher(bdsbipdz);
				if (StringConvertUtils.isEmpty(bdsbipdz)) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，本地设备IP地址不能为空;" + ConstantUtil.HUANHANG;
				} else if (!bdsbipdzm.find()) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，本地设备IP地址不正确;" + ConstantUtil.HUANHANG;
				}
				// 校验子网掩码
				Matcher zwymm = pattern.matcher(zwym);
				if (StringConvertUtils.isEmpty(zwym)) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，子网掩码不能为空;" + ConstantUtil.HUANHANG;
				} else if (!zwymm.find()) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，子网掩码不正确;" + ConstantUtil.HUANHANG;
				}
				
				Map<String, Object> fqmap = new HashMap<>();
				fqmap.put("mbmc", rowMap.get("fqmbmc"));
				fqmap.put("tag", "1");
				// 根据分区模版名称查询分区模版ID
				List<Map<String, Object>> dataList =  ledtemplatemanageservice.selectLedTemplateByInfo(fqmap);
				fqmap.remove("tag");
				fqmap.remove("mbmc");
				// 校验是否存在分区模版名称
				if (dataList == null || dataList.isEmpty()) {
					errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，分区模版名称不存在;" + ConstantUtil.HUANHANG;
				} else {
					for (Map<String, Object> fqxx : dataList) {
						rowMap.put("fqmbid", fqxx.get("mbid"));
					}
				}
				stationString = stationString.replace("，", ",").replaceAll("；", ";");
				List<Map<String, Object>> stationList = new ArrayList<>();
				String str[] = stationString.split(";");
				List<String> list = new ArrayList<>();
				list = Arrays.asList(str);
				int j = 0;
				for (String s : list) {
					j++;
					int apache = StringUtils.countMatches(s, ",");
					if (apache != 2) {
						errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，关联站点列表缺少数据;" + ConstantUtil.HUANHANG;
					} else {
						
						Map<String, Object> stationTempMap = new HashMap<>();
						String valXlmc = s.substring(0, s.indexOf(","));
						String valFx = s.substring(s.indexOf(",") + 1, s.lastIndexOf(","));
						String valZdmc = s.substring(s.lastIndexOf(",") + 1);
						
						stationTempMap.put("valXlmc", valXlmc);
						List<Map<String, Object>> selectXlxx = commonService.selectXlxx(stationTempMap);
						// 校验线路是否存在
						if (selectXlxx == null || selectXlxx.isEmpty()) {
							errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，关联站点列表线路不能为空;" + ConstantUtil.HUANHANG;
						} else {
							// 取得线路编号
							for (Map<String, Object> xlxx : selectXlxx) {
								stationTempMap.put("xlbh", xlxx.get("xlmc"));
							}
						}

						stationTempMap.put("fx", valFx);
						// 校验方向
						if (StringConvertUtils.isEmpty(valFx)) {
							errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，关联站点列表站点的方向不能为空!;" + ConstantUtil.HUANHANG;
						} else if (!valFx.equals("上行") && !valFx.equals("下行")) {
							errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，关联站点列表站点方向请填写‘上行’或‘下行’!;" + ConstantUtil.HUANHANG;
						} else if (valFx.equals("上行")) {
							stationTempMap.put("fx", "0");
							stationTempMap.put("zdfx", "0");
						} else if (valFx.equals("下行")) {
							stationTempMap.put("fx", "1");
							stationTempMap.put("zdfx", "1");
						}
						
						stationTempMap.put("valZdmc", valZdmc);
						List<Map<String, Object>> selectZdxx = commonService.selectZdxx(stationTempMap);
						stationTempMap.remove("zdfx");
						stationTempMap.remove("valXlmc");
						stationTempMap.remove("valZdmc");
						// 校验线路是否存在
						if (selectZdxx == null || selectZdxx.isEmpty()) {
							errorMsg = errorMsg + sheetName + ConstantUtil.MESSAGE_DE_DI + i + "条记录，关联站点列表该线路下不存在该站点;" + ConstantUtil.HUANHANG;
						} else {
							// 取得线路编号
							for (Map<String, Object> zdxx : selectZdxx) {
								stationTempMap.put("zdbh", zdxx.get("zdid"));
								stationTempMap.put("dqzx", zdxx.get("zdxh"));
								stationTempMap.put("zpxssx", j);
							}
						}
						stationTempMap.put("xlmc", valXlmc);
						stationTempMap.put("zdmc", valZdmc);
						stationList.add(stationTempMap);
					}
					rowMap.put("stationString", stationList);
				}
			}
		}
		
		// 判断是否有错误消息 返回导入的数据
		if (errorMsg.length() > 0) {
			resMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ZERO);
			String name = ConstantUtil.IMPORT_ERROR_MESSAGE;
			// 写出错误消息
			creatTxtFile(name);
			writeTxtFile(errorMsg);
			resMap.put(ConstantUtil.MSG, errorMsg);
			resMap.put(ConstantUtil.FILEPATH, filenameTemp);
		} else 	{
			resMap.put(ConstantUtil.STATUS, ConstantUtil.STATUS_ONE);
			resMap.put(ConstantUtil.MSG, ConstantUtil.IMPORTEXCEL_SUCCESS);
			resMap.put(ConstantUtil.COMMON_DATA, paramList);
			resMap.put("ledidList", ledidList);
			resMap.put("csmbList", csmbList);
		}

		return resMap;
	}

	// 消息存储路径
	private static String filenameTemp;
	
	/**
	 * 
	 * 创建写入文本
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static boolean creatTxtFile(String name) throws IOException {

//		String filePath = ClassUtils.getDefaultClassLoader().getResource(ConstantUtil.NULL_STRING).getPath() + ConstantUtil.STATIC_FRONTEND_PROJECT_TEMPLATE;
		File path = new File(ResourceUtils.getURL("classpath:").getPath());
		if(!path.exists()) path = new File("");
		File upload =  new File(path.getAbsolutePath() + ConstantUtil.STATIC_FRONTEND_PROJECT_TEMPLATE);
		if(!upload.exists()) upload.mkdirs();
		boolean flag = false;
		filenameTemp = java.net.URLDecoder.decode(upload.getAbsolutePath().replaceAll("!", ConstantUtil.NULL_STRING), ConstantUtil.UTF_8) + File.separator+name + ConstantUtil.WORD_TXT;
		File filename = new File(filenameTemp);
		if (!filename.exists()) {
			filename.createNewFile();
			flag = true;
		}
		return flag;
	}
	
	  @RequestMapping(value = ConstantUtil.DOWNERROEFILE)
	  public ResponseEntity<FileSystemResource> downErrorFile(HttpServletRequest request) throws Exception {
			File path = new File(ResourceUtils.getURL("classpath:").getPath());
			if(!path.exists()) path = new File("");
			File upload =  new File(path.getAbsolutePath() + ConstantUtil.STATIC_FRONTEND_PROJECT_TEMPLATE);
			if(!upload.exists()) upload.mkdirs();
			filenameTemp = java.net.URLDecoder.decode(upload.getAbsolutePath().replaceAll("!", ConstantUtil.NULL_STRING), ConstantUtil.UTF_8) + File.separator+ConstantUtil.IMPORT_ERROR_MESSAGE + ConstantUtil.WORD_TXT;
			File file = new File(filenameTemp);
			String userAgent = request.getHeader(ConstantUtil.USER_AGENT).toLowerCase();
			String fileName = ConstantUtil.ERRORFILENAME ;
			if (userAgent.contains(ConstantUtil.MSIE) || userAgent.contains(ConstantUtil.LIKE_GECKO)) {
				fileName = URLEncoder.encode(fileName, ConstantUtil.UTF_8);
			} else {
				fileName = new String(fileName.getBytes(ConstantUtil.UTF_8), ConstantUtil.ISO_8859_1);
			}
			if (file.exists()) {
				return export(file,fileName);
			}else {
				return null;
			}
	        
	    }
	  
	  public ResponseEntity<FileSystemResource> export(File file,String fileName) {
	        if (file == null) {
	            return null;
	        }
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	        headers.add("Content-Disposition", "attachment; filename="+fileName+".txt");
	        headers.add("Pragma", "no-cache");
	        headers.add("Expires", "0");
	        return ResponseEntity
	                .ok()
	                .headers(headers)
	                .contentLength(file.length())
	                .contentType(MediaType.parseMediaType("application/octet-stream"))
	                .body(new FileSystemResource(file));
	    }
	/**
	 * 
	 * 向文本中写入内容
	 * @param newStr
	 * @return
	 * @throws IOException
	 */
	public static boolean writeTxtFile(String newStr) throws IOException {
		// 先读取原有文件内容，然后进行写入操作
		boolean flag = false;
		String filein = newStr + ConstantUtil.HUANHANG;
 
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
 
		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			// 文件路径
			File file = new File(filenameTemp);
			// 将文件读入输入流
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();
 
			buf.append(filein);
 
			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			flag = true;
		} catch (IOException e1) {
			throw e1;
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return flag;
	}
}
