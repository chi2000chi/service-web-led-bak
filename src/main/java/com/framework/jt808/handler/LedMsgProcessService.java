package com.framework.jt808.handler;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.framework.common.JsonUtil;
import com.framework.jt808.common.DataCache;
import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.util.BitOperator;
import com.framework.jt808.util.HexStringUtils;
import com.framework.jt808.util.MsgDecoder;
import com.framework.jt808.util.MsgEncoder;
import com.framework.jt808.vo.BusState;
import com.framework.jt808.vo.PackageData;
import com.framework.jt808.vo.Session;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.PackageData.MsgHeader;
import com.framework.jt808.vo.resp.MsgBody0901;
import com.framework.jt808.vo.resp.MsgBody0902;
import com.framework.jt808.vo.resp.MsgBody0904;
import com.framework.jt808.vo.resp.MsgBody0905;
import com.framework.jt808.vo.resp.MsgBody0906;
import com.framework.jt808.vo.resp.MsgBody0907;
import com.framework.jt808.vo.resp.MsgBody0908;
import com.framework.jt808.vo.resp.MsgBody0909;
import com.framework.util.DateUtils;
import com.framework.webClient.dispatch.sender.LedDownSender;
import com.framework.webClient.service.ICommon808Service;
import com.framework.webClient.util.CommonFunc;
import com.framework.webClient.util.CommonUtils;
import com.framework.webClient.util.FtpUtil;
import com.framework.webClient.util.StringUtils;
import com.framework.jt808.vo.resp.MsgBody0605;

/**
 * 消息处理
 */
@Component
public class LedMsgProcessService extends BaseMsgProcessService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MsgEncoder msgEncoder;
	@Autowired
	private MsgDecoder msgDecoder;
	@Autowired
	private BitOperator bitOperator;
	public final  SessionManager sessionManager = SessionManager.getInstance();
	@Autowired
	private LedDownSender ledDownSender;
	@Autowired(required = true)
	private ICommon808Service common808Service;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMdd");
	// ftp服务器ip地址
	@Value("${ftpAddress}")
	private String ftpAddress;
	// 端口号
	@Value("${ftpPort}")
	private int ftpPort;
	// 内网端口号
	@Value("${ftpPortN}")
	private int ftpPortN;
	// 用户名
	@Value("${ftpName}")
	private String ftpName;
	// 密码
	@Value("${ftpPassWord}")
	private String ftpPassWord;
	// 图片路径
	@Value("${ftpBasePath}")
	private String ftpBasePath;
	//天气预报xml文件路径
	@Value("${ftpWeatherXmlPath}")
	private String ftpWeatherXmlPath;
	public Map ftpFileMap = new HashMap();
	
	//登录时候去接口取 缓存的车辆
	@Value("${dengludowncar}")
	private String dengluDownCar;

	private LedMsgProcessService() {
		log.info("----------LedMsgProcessService Initializing ----------------");
	}
	public void processRegisterMsg(PackageData packageData) throws Exception {
		
		Session session = sessionManager.findBySessionId(packageData.getMsgHeader().getLedNo());
		if (session == null) {
			session = Session.buildSession(packageData.getMsgHeader().getLedNo());
		}
		session.setLedNo(packageData.getMsgHeader().getLedNo());
		sessionManager.put(session.getLedNo(), session);
		String ledNo = packageData.getMsgHeader().getLedNo();
		Map<String, Object> qbMap = new HashMap<>();
		qbMap.put("ledNo", ledNo);
		qbMap.put("REGISTERINFO", Arrays.toString(packageData.getMsgBodyBytes()));
		qbMap.put("TABLENAME", "LED_REGISTER"+monthDateFormat.format(new Date()));
		common808Service.insertLedRegister(qbMap);
		List<Map<String, Object>> dataMapList = DataCache.ledQueue.stream().filter(p -> ledNo.equals(p.get("ledbh")))
				.collect(Collectors.toList());
		if (dataMapList != null && dataMapList.size() > 0) {
			List<Map<String, Object>> safeCodeMapList = DataCache.safeCodeQueue.stream().filter(p -> ledNo.equals(p.get("ledbh")))
					.collect(Collectors.toList());
			String safeCodeStr = "";
			if(safeCodeMapList == null || safeCodeMapList.size() == 0) {
				UUID uuid=UUID.randomUUID();
				safeCodeStr=uuid.toString();
				Map<String, Object> insertMap = new HashMap<>();
				insertMap.put("safecode", safeCodeStr);
				//common808Service.insertLedRegister(insertMap);
			}else {	
				safeCodeStr = safeCodeMapList.get(0).get("safecode").toString();
			}	
			generateMsg090BBody(packageData, safeCodeStr);
			generateMsg0905Body(packageData, dataMapList.get(0));
			//生成系统时间
			generateMsg090ABody(ledNo, simpleDateFormat.format(new Date()));
			if(!(dataMapList.size() == 1 &&  null ==dataMapList.get(0).get("xlbh"))) {
				// 下发线路列表 0x0907
				generateMsg0907Body(packageData, dataMapList);
				// 下发线路属性信息 0x0908
				generateMsg0908Body(packageData, dataMapList);
				// 下发线路路况信息 0x0908
				generateMsg0909Body(packageData, dataMapList);
				
				// 如果是从早上6到晚上21点之间的话  登录就从缓存上一次车辆信息的map里下发一次车辆信息
				////当前小时数
				Date date = new Date();
				SimpleDateFormat df = new SimpleDateFormat("HH");
				String str = df.format(date);
				int a = Integer.parseInt(str);// 小时数
				if(a >= 6 && a <= 21) {
					// 1 正式环境 heb_dzzp_web_test 项目上的代码
					/*for (Map<String, Object> dataMap : dataMapList) {// 循环站牌的线路，依次从缓存map中取出上一次发送的车辆信息
						int lineNo = Integer.parseInt(dataMap.get("xlbh").toString());//线路id
						String previousCarKey=ledNo+"-"+lineNo;//缓存map里的key
						if(DataCache.previousCarMap.containsKey(previousCarKey)) {// map 里有这个key，有上次存的数据，下发数据
							MsgBody0906 previousCarData0906 =DataCache.previousCarMap.get(previousCarKey);//存上次发的实体类
							generateMsg0906Body(ledNo, previousCarData0906);// 发送车辆信息
						}
					}*/
					// 2 正式环境  heb_dzzp_web 项目上的代码
					String param="";
					Set<String> set=new HashSet<String>();
					for (Map<String, Object> dataMap : dataMapList) {
						int lineNo = Integer.parseInt(dataMap.get("xlbh").toString());//线路id
						String oneKey=ledNo+"-"+lineNo;// 站牌一条线路 在 缓存里map 里的key
						set.add(oneKey);//key 放到set 里防止重复
					}
					// 循环set 拼接字符串
					if(!set.isEmpty()) {	
						for(String strSet:set) {
							param+=strSet+",";//所有的key逗号分隔，拼接成一个字符串
						}
					}
					if(param.length()>0 && !"".equals(param)) {//key 字符串 不为空的话
						param=param.substring(0, param.length()-1);// 去掉最后一个逗号
						String url=dengluDownCar;//调用的接口地址
						//log.info("url:"+url);
						String paramSend="str="+param;//发送到接口上的完整参数为 str= key,key....
						log.info("paramSend:"+paramSend);
						String resultStr=CommonUtils.SendGET(url,paramSend);//调用接口
						
						if(resultStr !=null && !"".equals(resultStr)) {// 接口返回有数据的话
							// 解析返回的数据
							com.alibaba.fastjson.JSONObject  jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resultStr);
							Map<String, String> itemMap = com.alibaba.fastjson.JSONObject.toJavaObject(jsonObject, Map.class);
							// 循环发送 返回的 0906信息
							for (Map.Entry<String, String> entry : itemMap.entrySet()) {
								String key =entry.getKey();//  返回的数据里的key
								//log.info("key:"+key);
								//System.out.println("key:"+key);
								String carDown=entry.getValue();// 返回的变成json字符串的 0906 类
								//log.info("carDown0906:"+carDown);
								if(carDown !=null && !"".equals(carDown)) {
									MsgBody0906 previous0906= JSON.parseObject(carDown, MsgBody0906.class);
									generateMsg0906Body(ledNo, previous0906);// 发送车辆信息
								}
						    }
						}
					}
				}
			}
			

		}
	}

	private  void generateMsg090BBody(PackageData packageData, String safeCodeStr) throws Exception {
		int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
		byte[] bs = msgEncoder.encode090BResp(packageData.getMsgHeader().getLedNo(), safeCodeStr, flowId);
		ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x090B, bs,safeCodeStr);
	}

	/**
	 * 下发线路属性信息 0x0908
	 * 
	 * @param packageData
	 * @param dataMapList
	 * @throws Exception
	 */
	public void generateMsg0909Body(PackageData packageData, List<Map<String, Object>> dataMapList) throws Exception {
		for (Map<String, Object> dataMap : dataMapList) {
			// 获取当前站牌编号
			String ledNo = packageData.getMsgHeader().getLedNo().trim();
			String flag = (String) dataMap.get("fx");// 方向
			String linename = (String) dataMap.get("xlmc");// 线路名称
			// String lukledNo= common808Service.getLuTest();//测试路况的站牌编号

			String xlbh = (String) dataMap.get("xlbh");// 线路编号
			// 绑定了线路的站牌才给下发
			if (xlbh != null && !"".equals(xlbh)) {

				String xlbhStandard = StringUtils.addZeroForNum(xlbh, 6);// 不足六位数的前面补零
				List<StationInfo> stationInfoList = DataCache.stationQueue.stream()
						.filter(s -> s.getFx() == Integer.parseInt((String) dataMap.get("fx"))
								&& s.getXlmc().equals((String) dataMap.get("xlmc")))
						.collect(Collectors.toList());
				if (stationInfoList != null && stationInfoList.size() > 0) {
					/*
					 * long startTime = System.currentTimeMillis();
					 * System.out.println("start time:"+startTime);
					 */
					String lkStr = "";
					if (true) {// 如果是测试路况的电子站牌
						// log.info("ledNo:"+ledNo);
						// log.info("linename:"+linename);
						// log.info("xlbh:"+xlbh);
						// log.info("flag:"+flag);
						String key = xlbhStandard + "-" + flag;
						// log.info("key:"+key);
						lkStr = DataCache.lkMap.get(key);
						// 返回的路况为空
						if (lkStr == null || lkStr.length() == 0) {// 调用接口报错，返回路况都是0
							Collections.sort(stationInfoList, (a, b) -> a.getZdxh() - b.getZdxh());
							// 拼接站点名称
							StringBuilder str = new StringBuilder();
							// （0：畅通，1:缓慢，2：拥堵）
							int count = stationInfoList.size();
							// Random rand = new Random();
							for (int i = 0; i < count - 1; i++) {
								StationInfo currentStation = stationInfoList.get(i);
								StationInfo nextStation = stationInfoList.get(i + 1);
								str.append(currentStation.getZdxh()).append(",").append(nextStation.getZdxh())
										.append(",").append("0").append(";");
							}
							lkStr = str.toString();
							// log.info("lkStr-null:"+lkStr);
							//System.out.println("lkStr-null:" + lkStr);
						} else {// 正常返回
								// log.info("lkStr-right:"+lkStr);
							//System.out.println("lkStr-right:" + lkStr);
						}
						// log.info("lkStr-finally:"+lkStr);
					} /*
						 * else {
						 * 
						 * Collections.sort(stationInfoList, (a, b) -> a.getZdxh() - b.getZdxh()); //
						 * 拼接站点名称 StringBuilder str = new StringBuilder(); //（0：畅通，1:缓慢，2：拥堵） int count
						 * = stationInfoList.size(); //Random rand = new Random(); for (int i = 0; i <
						 * count - 1; i++) { StationInfo currentStation = stationInfoList.get(i);
						 * StationInfo nextStation = stationInfoList.get(i+1);
						 * str.append(currentStation.getZdxh()).append(",").append(nextStation.getZdxh()
						 * ).append(",").append("0").append(";"); } lkStr = str.toString(); }
						 */

					MsgBody0909 respMsgBody = new MsgBody0909();
					// 线路编号
					respMsgBody.setLineNo(Integer.parseInt((String) dataMap.get("xlbh")));
					// 站点列表长度
					respMsgBody.setLkLength(lkStr.getBytes(JT808Consts.string_encoding).length + 1);
					// 站点列表
					respMsgBody.setLkList(lkStr);
					int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
					byte[] bs = msgEncoder.encode0909Resp(packageData, respMsgBody, flowId);
					/*
					 * if("000000119999".equals(ledNo)) {//测试站牌 log.info("lkmsg:"+lkStr); }
					 */
					ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(), flowId,
							JT808Consts.msg_id_0x0909, bs, JsonUtil.convertObj2String(respMsgBody));
					/*
					 * long endTime = System.currentTimeMillis();
					 * System.out.println("end_time:"+endTime); float excTime = (float) (endTime -
					 * startTime) / 1000; System.out.println("路况lukTiming 执行时间:"+excTime);
					 */
				}
			}
		}

		/*
		 * for (Map<String, Object> dataMap : dataMapList) {
		 * 
		 * List<StationInfo> stationInfoList = DataCache.stationQueue.stream() .filter(s
		 * -> s.getFx() == Integer.parseInt((String) dataMap.get("fx")) &&
		 * s.getXlmc().equals((String) dataMap.get("xlmc")))
		 * .collect(Collectors.toList()); if (stationInfoList != null &&
		 * stationInfoList.size() > 0) { Collections.sort(stationInfoList, (a, b) ->
		 * a.getZdxh() - b.getZdxh()); // 拼接站点名称 StringBuilder str = new
		 * StringBuilder(); //（0：畅通，1:缓慢，2：拥堵） int count = stationInfoList.size();
		 * //Random rand = new Random(); for (int i = 0; i < count - 1; i++) {
		 * StationInfo currentStation = stationInfoList.get(i); StationInfo nextStation
		 * = stationInfoList.get(i+1);
		 * str.append(currentStation.getZdxh()).append(",").append(nextStation.getZdxh()
		 * ).append(",").append("0").append(";"); } String lkStr = str.toString();
		 * MsgBody0909 respMsgBody = new MsgBody0909(); // 线路编号
		 * respMsgBody.setLineNo(Integer.parseInt((String) dataMap.get("xlbh"))); //
		 * 站点列表长度
		 * respMsgBody.setLkLength(lkStr.getBytes(JT808Consts.string_encoding).length +
		 * 1); // 站点列表 respMsgBody.setLkList(lkStr); int flowId =
		 * super.getFlowId(packageData.getMsgHeader().getLedNo()); byte[] bs =
		 * msgEncoder.encode0909Resp(packageData, respMsgBody, flowId);
		 * ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,
		 * JT808Consts.msg_id_0x0909, bs,JsonUtil.convertObj2String(respMsgBody)); } }
		 */
	}
	/**
	 * 统一应答
	 * 
	 * @param packageData
	 * @throws Exception
	 */
	public  void generateMsg0901Body(PackageData packageData) throws Exception {
		MsgBody0901 respMsgBody = new MsgBody0901();
		// 应答流水号
		respMsgBody.setReplyFlowId(packageData.getMsgHeader().getFlowId());
		// 应答ID
		respMsgBody.setReplyId(packageData.getMsgHeader().getMsgId());
		// 结果
		respMsgBody.setReplyCode(respMsgBody.success);

		int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
		byte[] bs = msgEncoder.encode0901Resp(packageData, respMsgBody, flowId);
		ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x0901, bs,JsonUtil.convertObj2String(respMsgBody));
		respMsgBody = null;
	}

	
	
	/**
	 * 下发设置显示屏分区配置文件 0x0905
	 * 
	 * @param packageData
	 * @param map
	 * @throws Exception
	 */
	public   void generateMsg0905ExtraBody(PackageData packageData, String filePath) throws Exception {
		MsgBody0905 respMsgBody = new MsgBody0905();
		// 模板编码
		respMsgBody.setTemplateNo(999);
		String fileContent = null;
		if(ftpFileMap.get(filePath) != null) {
			fileContent = (String) ftpFileMap.get(filePath);
		}else {
			// 获取文件内容，进行异或校验
			fileContent = FtpUtil.getFileContent(filePath.substring(filePath.lastIndexOf("/") + 1),
					ftpPort, ftpName, ftpPassWord, ftpBasePath);
			if(fileContent == null || "".equals(fileContent)) {
				//如果为空,则在取一次
				fileContent = FtpUtil.getFileContent(filePath.substring(filePath.lastIndexOf("/") + 1),
						ftpPort, ftpName, ftpPassWord, ftpBasePath);
			}
			ftpFileMap.put(filePath, fileContent);
		}
		// 读取ftp文件内容
		byte[] tem = fileContent.getBytes(JT808Consts.string_encoding);
		// 文件校验码
		respMsgBody.setWjjym(bitOperator.getCheckSum4JT808(tem, 0, tem.length));
		// 配置xml文件信息长度
		respMsgBody.setFileLength(filePath.getBytes(JT808Consts.string_encoding).length + 1);
		// 配置xml文件信息
		respMsgBody.setFilePath(filePath);
		int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
		byte[] bs = msgEncoder.encode0905Resp(packageData, respMsgBody, flowId);
		ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x0905, bs,JsonUtil.convertObj2String(respMsgBody));
	}
	
	
	/**
	 * 下发公告列表
	 * 
	 * @param ledbhList
	 * @throws Exception
	 */
	public  void service0905ToLed(String ledNo) throws Exception {
//		List<Map<String, Object>> dataMapList = DataCache.ledQueue.stream().filter(p -> ledNo.equals(p.get("ledbh")))
//				.collect(Collectors.toList());
//		PackageData packageData = new PackageData();
//		MsgHeader msgHeader = new MsgHeader();
//		msgHeader.setLedNo(ledNo);
//		packageData.setMsgHeader(msgHeader);
//		List<Map<String, Object>> safeCodeMapList = DataCache.safeCodeQueue.stream().filter(p -> ledNo.equals(p.get("ledbh")))
//				.collect(Collectors.toList());
//		String safeCodeStr = "";
//		safeCodeStr = safeCodeMapList.get(0).get("safecode").toString();	
//		generateMsg090BBody(packageData, safeCodeStr);
		List<Map<String, Object>> dataMapList = DataCache.ledQueue.stream().filter(p -> ledNo.equals(p.get("ledbh")))
				.collect(Collectors.toList());
		PackageData packageData = new PackageData();
		MsgHeader msgHeader = new MsgHeader();
		msgHeader.setLedNo(ledNo);
		packageData.setMsgHeader(msgHeader);
		this.generateMsg0905Body(packageData, dataMapList.get(0));	
	}
	
	/**
	 * 下发设置显示屏分区配置文件 0x0905
	 * 
	 * @param packageData
	 * @param map
	 * @throws Exception
	 */
	private  void generateMsg0905Body(PackageData packageData, Map<String, Object> map) throws Exception {
		MsgBody0905 respMsgBody = new MsgBody0905();
		// 模板编码
		respMsgBody.setTemplateNo(Integer.parseInt((map.get("mbbh")).toString()));
		// ftp文件路径
		String filePath = (String) map.get("mblj");
		String fileContent = null;
		if(ftpFileMap.get(filePath) != null) {
			fileContent = (String) ftpFileMap.get(filePath);
		}else {
			// 获取文件内容，进行异或校验
			//System.out.println("begin");
			fileContent = FtpUtil.getFileContent(filePath.substring(filePath.lastIndexOf("/") + 1),
					ftpPort, ftpName, ftpPassWord, ftpBasePath);
			/*System.out.println("fileContent first:"+fileContent);
			System.out.println("ftpPort:"+ftpPort);
			System.out.println("ftpName:"+ftpName);
			System.out.println("ftpPassWord:"+ftpPassWord);
			System.out.println("ftpBasePath:"+ftpBasePath);*/
			if(fileContent == null || "".equals(fileContent)) {
				//如果为空,则在取一次
				fileContent = FtpUtil.getFileContent(filePath.substring(filePath.lastIndexOf("/") + 1),
						ftpPort, ftpName, ftpPassWord, ftpBasePath);
				//System.out.println("fileContent second:"+fileContent);
			}
			ftpFileMap.put(filePath, fileContent);
		}		
		// 读取ftp文件内容
		byte[] tem = fileContent.getBytes(JT808Consts.string_encoding);
		// 文件校验码
		respMsgBody.setWjjym(bitOperator.getCheckSum4JT808(tem, 0, tem.length));
		// 配置xml文件信息长度
		respMsgBody.setFileLength(filePath.getBytes(JT808Consts.string_encoding).length + 1);
		// 配置xml文件信息
		respMsgBody.setFilePath(filePath);
		int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
		byte[] bs = msgEncoder.encode0905Resp(packageData, respMsgBody, flowId);

		ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x0905, bs,JsonUtil.convertObj2String(respMsgBody));
	}

	/**
	 * 下发线路列表 0x0907
	 * 
	 * @param packageData
	 * @param dataMapList
	 * @throws Exception
	 */
	private  void generateMsg0907Body(PackageData packageData, List<Map<String, Object>> dataMapList) throws Exception {
		// 按照显示顺序排序
		Collections.sort(dataMapList,
				(a, b) -> Integer.parseInt(a.get("zpxssx").toString()) - Integer.parseInt(b.get("zpxssx").toString()));
		StringBuilder str = new StringBuilder();
		dataMapList.forEach(item -> str.append(item.get("xlbh")== null ||"null".equals(item.get("xlbh"))? "":item.get("xlbh")).append(",").append(item.get("fristname")== null ||"null".equals(item.get("fristname"))? "":item.get("fristname")).append(",").append(item.get("secondname")== null ||"null".equals(item.get("secondname"))? "":item.get("secondname")).append(";"));
		// 线路列表
		String lineInfoList = str.toString();
		MsgBody0907 respMsgBody = new MsgBody0907();
		// 线路列表长度
		respMsgBody.setLineInfoLength(lineInfoList.getBytes(JT808Consts.string_encoding).length + 1);
		// 线路列表
		respMsgBody.setLineInfoList(lineInfoList);
		int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
		byte[] bs = msgEncoder.encode0907Resp(packageData, respMsgBody, flowId);
		ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x0907, bs,JsonUtil.convertObj2String(respMsgBody));
	}

	/**
	 * 下发线路属性信息 0x0908
	 * 
	 * @param packageData
	 * @param dataMapList
	 * @throws Exception
	 */
	private  void generateMsg0908Body(PackageData packageData, List<Map<String, Object>> dataMapList) throws Exception {
		for (Map<String, Object> dataMap : dataMapList) {
			if (DataCache.unShowLineList != null && DataCache.unShowLineList.size() >0 && dataMap.get("xlbh") != null && DataCache.unShowLineList.contains((String) dataMap.get("xlbh"))) {
				MsgBody0904 respMsgBody = new MsgBody0904();
				// 线路编号
				respMsgBody.setLineNo(Integer.parseInt((String) dataMap.get("xlbh")));
				// 本线路暂未开通实时公交功能，正在努力中,||线路查询请查看下方纸质线路信息
				respMsgBody.setMsgType(3);
				// 0非全屏1全屏
				respMsgBody.setFullScreenFlg(1);
				int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
				byte[] bs = msgEncoder.encode0904Resp(packageData, respMsgBody, flowId);
				ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x0904, bs,JsonUtil.convertObj2String(respMsgBody));
			} else {
				List<StationInfo> stationInfoList = DataCache.stationQueue.stream()
						.filter(s -> s.getFx() == Integer.parseInt((String) dataMap.get("fx"))
								&& s.getXlmc().equals((String) dataMap.get("xlmc"))).distinct()
						.collect(Collectors.toList());
				if (stationInfoList != null && stationInfoList.size() > 0) {
					List<StationInfo> busListClone = CommonFunc.depCopy(stationInfoList);
					busListClone = busListClone.stream().distinct().collect(Collectors.toList());
					
					// 拼接站点名称
					StringBuilder str = new StringBuilder();
					LinkedHashSet<StationInfo> hs = new LinkedHashSet<StationInfo>();
					Collections.sort(busListClone, (a, b) -> a.getZdxh() - b.getZdxh());
					for (StationInfo item : busListClone) {
						if (!hs.contains(item)) {
							str.append(item.getZdxh()).append(",").append(item.getZdmc()).append(",")
									.append((dataMap.get("fqzd") != null
											&& ((String) dataMap.get("fqzd")).equals(item.getZdmc())) ? "1" : "0")
									.append(";");
							hs.add(item);
						}
					}
					
					// 站点列表
					String stationInfoStr = str.toString();
					hs.clear();
					MsgBody0908 respMsgBody = new MsgBody0908();
					// 线路编号
					respMsgBody.setLineNo(Integer.parseInt(dataMap.get("xlbh").toString()));
					// 首班时间
					respMsgBody.setStartTime(String.valueOf(dataMap.get("starttime")));
					// 末班时间
					respMsgBody.setEndTime(String.valueOf(dataMap.get("endtime")));
					// 发车间隔
					respMsgBody.setFcjg(dataMap.get("fcjg") == null ? 10 : Integer.parseInt(dataMap.get("fcjg").toString()));
					// 票价
					respMsgBody.setPj(dataMap.get("pj") == null ? 2 : Integer.parseInt(dataMap.get("pj").toString()));
					// 上下行标识
					respMsgBody.setDirection(Integer.parseInt( dataMap.get("fx").toString()));
					// 线路方向
					respMsgBody.setLineDirection(Integer.parseInt( dataMap.get("xlfx").toString()));
					// 当前站号
					respMsgBody.setCurrentStationNo(((BigDecimal) dataMap.get("dqzx")).intValue());
					// 站点列表长度
					respMsgBody.setStationLength(stationInfoStr.getBytes(JT808Consts.string_encoding).length + 1);
					// 站点列表
					respMsgBody.setStationInfoList(stationInfoStr);
					int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
					byte[] bs = msgEncoder.encode0908Resp(packageData, respMsgBody, flowId);
					ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x0908, bs,JsonUtil.convertObj2String(respMsgBody));
					
					if(dataMap.get("zdtk") != null && "1".equals((String) dataMap.get("zdtk"))) {
						//1 越战
						MsgBody0904 respMsgBody1 = new MsgBody0904();
						//线路编号
						respMsgBody1.setLineNo(Integer.parseInt((String) dataMap.get("xlbh")));
						//XX因线网调整已不在本站停靠
						respMsgBody1.setMsgType(2);
						//0非全屏1全屏
						respMsgBody1.setFullScreenFlg(0);
						int flowId1 = super.getFlowId(packageData.getMsgHeader().getLedNo());
						byte[] bs1 = msgEncoder.encode0904Resp(packageData, respMsgBody1, flowId1);
						ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId1,JT808Consts.msg_id_0x0904,bs1,JsonUtil.convertObj2String(respMsgBody));
					}
				}
			}
		}
	}
	
	
	
	/**
	 * 下发线路属性信息 0x0904 末班车已过
	 * 
	 * @param packageData
	 * @param dataMapList
	 * @throws Exception
	 */
	private  void generateMsg09041Body(PackageData packageData, List<Map<String, Object>> dataMapList,String xlbh) throws Exception {
		for (Map<String, Object> dataMap : dataMapList) {
			String xlbhMap=dataMap.get("xlbh")+"";
			if(xlbhMap !=null && xlbh.equals(xlbhMap)) {
				MsgBody0904 respMsgBody = new MsgBody0904();
				// 线路编号
				respMsgBody.setLineNo(Integer.parseInt((String) dataMap.get("xlbh")));
				// 本方向末班车已通过本站
				respMsgBody.setMsgType(1);
				// 0非全屏1全屏
				respMsgBody.setFullScreenFlg(1);
				int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
				byte[] bs = msgEncoder.encode0904Resp(packageData, respMsgBody, flowId);
				ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x0904, bs,JsonUtil.convertObj2String(respMsgBody));
			}
		}
	}
	

	/**
	 * 下发设置显示屏分区配置文件 0x0905
	 * 
	 * @param packageData
	 * @throws Exception
	 */
	public  void generateMsg0906Body(String ledNo, MsgBody0906 respMsgBody) throws Exception {
		int flowId = super.getFlowId(ledNo);
		respMsgBody.setFlowid(flowId);
		//common808Service.insertMsg0906(respMsgBody);
		byte[] bs = msgEncoder.encode0906Resp(ledNo, respMsgBody, flowId);
		ledDownSender.processDzzpDown(ledNo,flowId,JT808Consts.msg_id_0x0906, bs,JsonUtil.convertObj2String(respMsgBody));
	}

	
	/**
	 * 下发设置显示屏分区配置文件 0x0905
	 * 
	 * @param packageData
	 * @throws Exception
	 */
	public  void generateMsg090ABody(String ledNo, String systemTime) throws Exception {

		int flowId = super.getFlowId(ledNo);
		byte[] bs = msgEncoder.encode090AResp(ledNo, systemTime, flowId);
		ledDownSender.processDzzpDown(ledNo,flowId,JT808Consts.msg_id_0x090A, bs,systemTime);
	}
	
	/**
	 * 处理心跳
	 * 
	 * @param packageData
	 * @throws Exception
	 */
	public  void processHeartMsg(PackageData packageData) throws Exception {
		Session session = sessionManager.findBySessionId(packageData.getMsgHeader().getLedNo());
		if (session == null) {
			session = Session.buildSession(packageData.getMsgHeader().getLedNo());
		}
		session.setLedNo(packageData.getMsgHeader().getLedNo());
		sessionManager.put(session.getLedNo(), session);
		HashMap<String, Object> dataMap = this.msgDecoder.decodeHeartMsg(packageData.getMsgBodyBytes());	
		dataMap.put("LEDBH", packageData.getMsgHeader().getLedNo());
		dataMap.put("TABLENAME", "LED_STATE"+monthDateFormat.format(new Date()));
		common808Service.insertLedState(dataMap);
		dataMap = null;

	}

	/**
	 * 上传心跳包
	 * 
	 * @param packageData
	 * @throws Exception
	 */
	public  void processUploadPara(PackageData packageData) throws Exception {
		Session session = sessionManager.findBySessionId(packageData.getMsgHeader().getLedNo());
		if (session == null) {
			session = Session.buildSession(packageData.getMsgHeader().getLedNo());
		}
		session.setLedNo(packageData.getMsgHeader().getLedNo());
		sessionManager.put(session.getLedNo(), session);
		HashMap<String, Object> dataMap = this.msgDecoder.decodeParaMsg(packageData.getMsgBodyBytes());
		// 实时下发电子站牌参数
		//generateMsg0902Body(packageData);
	}

	/**
	 * 生成下发参数数据
	 * 
	 * @param packageData
	 * @throws Exception
	 */
	private  void generateMsg0902Body(PackageData packageData) throws Exception {
		HashMap<String, Object> dataMap = common808Service.getLedParaByLedNo(packageData.getMsgHeader().getLedNo());
		MsgBody0902 data_0902 = new MsgBody0902();
		data_0902.paraIdInt_010A = JT808Consts.para_id_0x010A;
		data_0902.paraLength_010A = JT808Consts.dword_length;
		data_0902.paraMemo_010A = HexStringUtils.ipToLong(dataMap.get("010A").toString());

		data_0902.paraIdInt_0101 = JT808Consts.para_id_0x0101;
		data_0902.paraLength_0101 = JT808Consts.dword_length;
		data_0902.paraMemo_0101 = HexStringUtils.ipToLong(dataMap.get("0101").toString());

		data_0902.paraIdInt_010B = JT808Consts.para_id_0x010B;
		data_0902.paraLength_010B = JT808Consts.dword_length;
		data_0902.paraMemo_010B = HexStringUtils.ipToLong(dataMap.get("010B").toString());

		data_0902.paraIdInt_0108 = JT808Consts.para_id_0x0108;
		data_0902.paraLength_0108 = JT808Consts.dword_length;
		data_0902.paraMemo_0108 = HexStringUtils.ipToLong(dataMap.get("0108").toString());

		data_0902.paraIdInt_0103 = JT808Consts.para_id_0x0103;
		data_0902.paraLength_0103 = JT808Consts.word_length;
		data_0902.paraMemo_0103 = dataMap.get("0103").toString();

		data_0902.paraIdInt_0200 = JT808Consts.para_id_0x0200;
		data_0902.paraLength_0200 = HexStringUtils.toBytes(packageData.getMsgHeader().getLedNo()).length;
		data_0902.paraMemo_0200 = packageData.getMsgHeader().getLedNo();

		data_0902.paraIdInt_0207 = JT808Consts.para_id_0x0207;
		data_0902.paraMemo_0207 = dataMap.get("0207").toString();
		data_0902.paraLength_0207 = HexStringUtils.toBytes((String) data_0902.paraMemo_0207).length;

		data_0902.paraIdInt_0208 = JT808Consts.para_id_0x0208;
		data_0902.paraMemo_0208 = dataMap.get("0208").toString();
		data_0902.paraLength_0208 = HexStringUtils.toBytes((String) data_0902.paraMemo_0208).length;

		data_0902.paraIdInt_0209 = JT808Consts.para_id_0x0209;
		data_0902.paraMemo_0209 = dataMap.get("0209").toString();
		data_0902.paraLength_0209 = ((String) data_0902.paraMemo_0209).getBytes(JT808Consts.string_encoding).length;

		data_0902.paraIdInt_020A = JT808Consts.para_id_0x020A;
		data_0902.paraMemo_020A = dataMap.get("020A").toString();
		data_0902.paraLength_020A = ((String) data_0902.paraMemo_020A).getBytes(JT808Consts.string_encoding).length;

		data_0902.paraIdInt_020B = JT808Consts.para_id_0x020B;
		data_0902.paraMemo_020B = dataMap.get("020B").toString();
		data_0902.paraLength_020B = ((String) data_0902.paraMemo_020B).getBytes(JT808Consts.string_encoding).length;

		data_0902.paraIdInt_020C = JT808Consts.para_id_0x020C;
		data_0902.paraMemo_020C = dataMap.get("020C").toString();
		data_0902.paraLength_020C = ((String) data_0902.paraMemo_020C).getBytes(JT808Consts.string_encoding).length;

		data_0902.paraIdInt_020D = JT808Consts.para_id_0x020D;
		data_0902.paraLength_020D = JT808Consts.byte_length;
		data_0902.paraMemo_020D = dataMap.get("020D").toString();
		;

		data_0902.paraIdInt_020E = JT808Consts.para_id_0x020E;
		data_0902.paraLength_020E = JT808Consts.byte_length;
		data_0902.paraMemo_020E = dataMap.get("020E").toString();

		data_0902.paraIdInt_020F = JT808Consts.para_id_0x020F;
		data_0902.paraMemo_020F = dataMap.get("020F").toString();
		data_0902.paraLength_020F = ((String) data_0902.paraMemo_020F).getBytes(JT808Consts.string_encoding).length;

		data_0902.paraIdInt_0210 = JT808Consts.para_id_0x0210;
		data_0902.paraLength_0210 = JT808Consts.byte_length;
		data_0902.paraMemo_0210 = dataMap.get("0210").toString();

		data_0902.paraIdInt_0211 = JT808Consts.para_id_0x0211;
		data_0902.paraLength_0211 = JT808Consts.byte_length;
		data_0902.paraMemo_0211 = dataMap.get("0211").toString();

		int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
		byte[] bs = msgEncoder.encode0902Resp(packageData, data_0902, flowId);

		ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x0902, bs,JsonUtil.convertObj2String(data_0902));
	}

	/**
	 * 下发电子站牌参数查询 0x0903
	 * 
	 * @param packageData
	 * @param map
	 * @throws Exception
	 */
	private  void generateMsg0903Body(PackageData packageData) throws Exception {
		int flowId = super.getFlowId(packageData.getMsgHeader().getLedNo());
		byte[] bs = msgEncoder.encode0903Resp(packageData, flowId);

		ledDownSender.processDzzpDown(packageData.getMsgHeader().getLedNo(),flowId,JT808Consts.msg_id_0x0903, bs,"");
	}

	/**
	 * 下发参数列表
	 * 
	 * @param ledbhList
	 * @throws Exception
	 */
	public  void service0902ToLed(List<String> ledbhList) throws Exception {
		for (String ledbh : ledbhList) {
			PackageData packageData = new PackageData();
			MsgHeader msgHeader = new MsgHeader();
			msgHeader.setLedNo(ledbh);
			packageData.setMsgHeader(msgHeader);
			this.generateMsg0902Body(packageData);
		}
	}

	/**
	 * 下发公告列表
	 * 
	 * @param ledbhList
	 * @throws Exception
	 */
	public  void service0905ToLed(List<Map<String, Object>> ledbhList) throws Exception {
		List<Map<String, Object>> allDataList = common808Service.selectLedAndLineInfoTmp(new HashMap());
		for (Map<String, Object> ledMap : ledbhList) {
			PackageData packageData = new PackageData();
			MsgHeader msgHeader = new MsgHeader();
			msgHeader.setLedNo(String.valueOf(ledMap.get("ledbh")));
			packageData.setMsgHeader(msgHeader);
			List<Map<String, Object>> dataMapList = allDataList.stream()
					.filter(p -> (String.valueOf(ledMap.get("ledbh")).equals(p.get("ledbh")) && String.valueOf(ledMap.get("ggmc")).equals(p.get("ggmc")))).collect(Collectors.toList());
			if (dataMapList != null && dataMapList.size() > 0) {
				// 下发设置显示屏分区配置文件 0x0905
				this.generateMsg0905Body(packageData, dataMapList.get(0));
			}
		}
	}
	
	/**
	 * 电子站牌通用应答
	 * 
	 * @param packageData
	 * @throws Exception
	 */
	public  void ledCommonBack(PackageData packageData) throws Exception {
		
		HashMap<String, Object> dataMap = this.msgDecoder.decodeCommonBackMsg(packageData.getMsgBodyBytes());
		dataMap.put("LEDBH", packageData.getMsgHeader().getLedNo());
		dataMap.put("TABLENAME", "LED_MSGLOG_BACK"+monthDateFormat.format(new Date()));
		common808Service.updateCommonMsgBack(dataMap);
		dataMap= null;
	}

	/**
	 * 电子站牌通用应答
	 * 
	 * @param packageData
	 * @throws Exception
	 */
	public  void ledCommonBackDetail(PackageData packageData) throws Exception {
		
		HashMap<String, Object> dataMap = this.msgDecoder.decodeCommonBackDetailMsg(packageData.getMsgBodyBytes());
		dataMap.put("LEDBH", packageData.getMsgHeader().getLedNo());
		dataMap.put("TABLENAME", "LED_MSGLOG_INFOBACK"+monthDateFormat.format(new Date()));
		common808Service.insertCommonMsgBackDetail(dataMap);
		if(dataMap.get("RESULT") != null && "1".equals(dataMap.get("RESULT").toString())) {
			sendLedLine(packageData.getMsgHeader().getLedNo());
		}
		if(dataMap.get("RESULT") != null && "2".equals(dataMap.get("RESULT").toString())) {
			service0905ToLed(packageData.getMsgHeader().getLedNo());
		}
		dataMap= null;
	}

	
	/**
	 * 下发参数下发查询
	 * 
	 * @param ledidList
	 * @throws Exception
	 */
	public  void service0903ToLed(List<String> ledidList) throws Exception {
		for (String ledid : ledidList) {
			List<Map<String, Object>> dataMapList = DataCache.ledQueue.stream()
					.filter(p -> ledid.equals(p.get("ledid"))).collect(Collectors.toList());
			if (dataMapList != null && dataMapList.size() > 0) {
				PackageData packageData = new PackageData();
				MsgHeader msgHeader = new MsgHeader();
				msgHeader.setLedNo((String) dataMapList.get(0).get("ledbh"));
				packageData.setMsgHeader(msgHeader);
				// 下发参数查询
				this.generateMsg0903Body(packageData);
			}
		}
	}

	public void sendLedLine(String ledNo) throws Exception {
		List<Map<String, Object>> dataMapList = DataCache.ledQueue.stream().filter(p -> ledNo.equals(p.get("ledbh")))
				.collect(Collectors.toList());		
		if (dataMapList != null && dataMapList.size() > 0) {
			PackageData packageData = new PackageData();
			MsgHeader msgHeader = new MsgHeader();
			msgHeader.setLedNo(ledNo);
			packageData.setMsgHeader(msgHeader);
			// 下发线路列表 0x0907
			generateMsg0907Body(packageData, dataMapList);
			if(!(dataMapList.size() == 1 &&  null ==dataMapList.get(0).get("xlbh"))) {
				// 下发线路属性信息 0x0908
				generateMsg0908Body(packageData, dataMapList);
				// 下发线路路况信息 0x0908
				generateMsg0909Body(packageData, dataMapList);
			}
		}
	}
	
	//下发末班车已过信息
	public void sendLedLastBus(String ledNo,String xlbh) throws Exception {
		List<Map<String, Object>> dataMapList = DataCache.ledQueue.stream().filter(p -> ledNo.equals(p.get("ledbh")))
				.collect(Collectors.toList());		
		if (dataMapList != null && dataMapList.size() > 0) {
			PackageData packageData = new PackageData();
			MsgHeader msgHeader = new MsgHeader();
			msgHeader.setLedNo(ledNo);
			packageData.setMsgHeader(msgHeader);
			if(!(dataMapList.size() == 1 &&  null ==dataMapList.get(0).get("xlbh"))) {
				// 下发线路属性信息 0x0904
				generateMsg09041Body(packageData, dataMapList,xlbh);
			}
		}
	}
}
