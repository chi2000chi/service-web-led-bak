package com.framework.webClient.schedulerTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.framework.entity.output.PlanToStartEntity;
import com.framework.jt808.common.DataCache;
import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.jt808.thread.Msg0906Thread;
import com.framework.jt808.thread.RegisterThread;
import com.framework.jt808.thread.ZFMsgThread;
import com.framework.jt808.vo.BusState;
import com.framework.jt808.vo.PackageData;
import com.framework.jt808.vo.Session;
import com.framework.jt808.vo.StationInfo;
import com.framework.jt808.vo.PackageData.MsgHeader;
import com.framework.jt808.vo.resp.MsgBody0906;
import com.framework.util.DateUtils;
import com.framework.util.GpsDistanceUtils;
import com.framework.webClient.dispatch.receiver.LedUpReceiver;
import com.framework.webClient.service.ICommon808Service;
import com.framework.webClient.service.ILedMsgManageService;
import com.framework.webClient.util.CommonFunc;
import com.framework.webClient.util.CommonUtils;
import com.framework.webClient.util.ConstantUtil;
import com.framework.webClient.util.FtpUtil;


@Component
public class SchedulerTask /*implements CommandLineRunner*/ {  // CommandLineRunner 项目启动运行 run 方法里的内容
	private static ConcurrentLinkedQueue<MsgBody0906>  msg0906Queue = new ConcurrentLinkedQueue<MsgBody0906>();
    
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private LedMsgProcessService msgProcessService;
	@Autowired
	private ILedMsgManageService ledMsgManageService;
	@Autowired(required = true)
	private ICommon808Service common808Service;
	@Autowired
	private GpsDistanceUtils utils;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	ConcurrentHashMap<String, StationInfo> stationMap = new ConcurrentHashMap<String, StationInfo>();
	// 消息存储路径
	private static String filenameTemp;
	// ftp服务器ip地址
	@Value("${ftpAddress}")
	private String ftpAddress;
	// 端口号
	@Value("${ftpPort}")
	private int ftpPort;
	// 用户名
	@Value("${ftpName}")
	private String ftpName;
	// 密码
	@Value("${ftpPassWord}")
	private String ftpPassWord;
	// 图片路径
	@Value("${ftpBasePath}")
	private String ftpBasePath;
	// 天气
	@Value("${gaode.weather}")
	private String gaodeweather;
	// 天气图片公网路径
	@Value("${weather_public_image_ip_port}")
	private String weatherPublicImageIpPort;
		
	//天气预报xml文件路径
	@Value("${ftpWeather11XmlPath}")
	private String ftpWeather11XmlPath;
	
	//天气预报xml文件路径
	@Value("${ftpWeather22XmlPath}")
	private String ftpWeather22XmlPath;
	
	//天气预报xml文件路径
	@Value("${ftpWeatherXmlPath}")
	private String ftpWeatherXmlPath;
	
	//路况接口地址
	@Value("${gaodeLk}")
	private String gaodeLk;
	
	//到站时间接口地址
	@Value("${gaodeDZ}")
	private String gaodeDZ;
	
	//private long startTime;
	
	//private long startTime1;
	
	private long startTime;
	
	private long endTime;

	
	
	
	//@Scheduled(cron = "0 0/30 * * * ?")
	public void systemTiming() {	
		try {
			// 电子站牌
			if (DataCache.ledQueue != null && !DataCache.ledQueue.isEmpty()) {
				List<Map<String, Object>> ledList = (List<Map<String, Object>>)DataCache.ledQueue.stream().collect(Collectors.toList());
                List<String> lenbhList = new LinkedList<String>();
                ledList.forEach(item -> lenbhList.add(item.get("ledbh").toString()));
                if(lenbhList != null && lenbhList.size() > 0) {
                    List<String> distinctList = lenbhList.stream().distinct().collect(Collectors.toList());
                    this.threadSendSystemTiming(distinctList, 1500);
                }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("生成数据异常", e);
		}	
	}
	
	
	//路况信息3分钟一下发
	//@Scheduled(cron = "0 0/3 * * * ?")
	public void lukTiming() {
		try {
			//step1 更新缓存
			//System.out.println("刷新路况");
			logger.info("刷新路况");
			//long startTime = System.currentTimeMillis();
			//System.out.println("startTime:"+startTime);
			//String url="http://10.1.30.13:20050/getlineetamap";
			String url=gaodeLk;
			String resultStr=CommonUtils.SendGET(url, "");
			com.alibaba.fastjson.JSONObject  jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resultStr);
			Map<String, String> itemMap = com.alibaba.fastjson.JSONObject.toJavaObject(jsonObject, Map.class);
			//更新缓存
			CommonUtils.mapCopy(itemMap, DataCache.lkMap);
			
			//Long endTime = System.currentTimeMillis();
			//System.out.println("endtime:" + endTime);
			
			//step2 下发路况
			//System.out.println("下发路况");
			//long startTime1 = System.currentTimeMillis();
			//System.out.println("startTime1:"+startTime1);
			logger.info("路况lukTiming 开始 ");
			// 电子站牌
			if (DataCache.ledQueue != null && !DataCache.ledQueue.isEmpty()) {
				List<Map<String, Object>> ledList = (List<Map<String, Object>>)DataCache.ledQueue.stream().collect(Collectors.toList());
                List<String> lenbhList = new LinkedList<String>();
                ledList.forEach(item -> lenbhList.add(item.get("ledbh").toString()));
                if(lenbhList != null && lenbhList.size() > 0) {
                    List<String> distinctList = lenbhList.stream().distinct().collect(Collectors.toList());
                    //this.threadSendLukTiming(distinctList, 100);
                    this.threadSendLukSingle(distinctList, 15);// distinctList-站牌列表  15-每个站牌发送完线程sleep的时间 （毫秒）
                }
			}
			
			
			
			//long endTime = System.currentTimeMillis();
			//float excTime = (float) (endTime - startTime) / 1000;
			//logger.info("路况lukTiming 执行时间:{}s", excTime);
		} catch (Exception e) {
			logger.error("路况异常", e);
		}	
	}
	
	//到站时间 10s 取一次
	
	//@Scheduled(cron = "0/10 * * * * ?")
	public void dzTiming() {
		try {
			//step1 更新缓存
			//System.out.println("刷新路况");
			logger.info("刷新到站时间");
			//long startTime = System.currentTimeMillis();
			//System.out.println("startTime:"+startTime);
			//String url="http://10.1.30.13:20050/getlineetamap";
			String url=gaodeDZ;
			String resultStr=CommonUtils.SendGET(url, "");
			com.alibaba.fastjson.JSONObject  jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resultStr);
			Map<String, Map> itemMap = com.alibaba.fastjson.JSONObject.toJavaObject(jsonObject, Map.class);
			//更新缓存
			CommonUtils.mapCopy(itemMap, DataCache.dzTimeMap);
			
		} catch (Exception e) {
			logger.error("======");
			logger.error("到站信息异常", e);
		}	
	}
	
	
	
	/**
	 * 
	 * 更新电子站牌的天气信息,每30分钟更新一次
	 * @throws Exception 
	 * 
	 */

	//@Scheduled(cron = "0 0/30 * * * ?")
	public void getGdWeatherTimer() throws Exception {
		// 获取高德地图哈尔滨市实时天气信息
        StringBuffer strBuf = new StringBuffer();
        String url = gaodeweather;
        URL u = new URL(url);
        URLConnection con = u.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream(), "utf-8"));
        String line = null;
        while ((line = in.readLine()) != null) {
            strBuf.append(line);
        }
        in.close();
        // 获取 高德天气
        String outRoomWeaterJson = strBuf.toString();
        // 使用json 解析
        JSONObject weathJsonObj = JSONObject.fromObject(outRoomWeaterJson);
        JSONArray lives = weathJsonObj.getJSONArray("lives");

        // 获取天气状况 例如:晴
        String weatherStr2 = String.valueOf(lives.getJSONObject(0).get("weather"));
        // 拼接图片路径
        String weatherImagePath = "http://" + weatherPublicImageIpPort + "/frontend/project/images/weather/" + weatherStr2 + ".png";
        // 拼接完成的的天气字符串 例如:晴 -2℃ 西南风4级
        String weatherStr3 = weatherStr2 + " " +
                             String.valueOf(lives.getJSONObject(0).get("temperature")) + "℃" + " " +
                             String.valueOf(lives.getJSONObject(0).get("winddirection")) + "风" +
                             String.valueOf(lives.getJSONObject(0).get("windpower")) + "级";
		// 获取weather.html中的信息
        Resource resource = new ClassPathResource("static/frontend/project/template/weather.html");
        InputStream fis = resource.getInputStream();
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader bf = new BufferedReader(isr);
        String content = "";
        StringBuilder sb = new StringBuilder();
        while (content != null) {
        	content = bf.readLine();
        	if (content == null) {
        		break;
        	}
        	sb.append(content.trim());
        }
        bf.close();

        String fileStr = sb.toString();
       
        // 将信息拼接到字符串中
        String htmlStr22 = fileStr.replace("{date}", CommonFunc.getNowDateNYR()).replace("{week}", CommonFunc.getWeekOfDate())
        		.replace("{weatherImagePath}", weatherImagePath).replace("{weatherStr3}", weatherStr3);
        // 替换font-size: 16px;为font-size: 28px;
        String htmlStr11 = fileStr.replace("{date}", CommonFunc.getNowDateNYR()).replace("{week}", CommonFunc.getWeekOfDate())
        		.replace("{weatherImagePath}", weatherImagePath).replace("{weatherStr3}", weatherStr3).replace("font-size: 16px;", "font-size: 28px;");

        // 上传到ftp上的文件名
        String name = ConstantUtil.WEATHERNAME22;
        // 为了适应立式和挂式的电子站牌，在这里新声明了一个名字，样式直接在上一块代码中直接替换。然后重新创建文件，并写入内容。
        String name11 = ConstantUtil.WEATHERNAME11;
        creatTxtFile(name);
        creatTxtFile(name11);
        // 将拼接好的字符串上传到ftp,文件名必须是weather.html或者weather11.html
		writeTxtFile(name, htmlStr22);
		writeTxtFile(name11, htmlStr11);
		try {
			// 电子站牌
			if (DataCache.ledQueue != null && !DataCache.ledQueue.isEmpty()) {
				List<Map<String, Object>> ledList = (List<Map<String, Object>>)DataCache.ledQueue.stream().collect(Collectors.toList());
                List<String> lenbhList = new LinkedList<String>();
                ledList.forEach(item -> lenbhList.add(item.get("ledbh").toString()));
                List<String> distinctList = lenbhList.stream().distinct().collect(Collectors.toList());
                this.threadWeatherSend(distinctList, 1500);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("生成数据异常", e);
		}
	}
	
	/**
	 * 1点关闭线程
	 * 
	 * @throws IOException
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void stopSocket() {
		logger.info("============关闭线程================");		
		//停止接收转发服务线程
		InstallSocket.runFlg = false;
		//停止转发处理线程
		ZFMsgThread.runFlg = false;

		InstallSocket.busMapTemp.clear();
		DataCache.busQueue.clear();

	}
	
	/**
	 * 4点启动线程
	 * 
	 * @throws IOException
	 */
	@Scheduled(cron = "0 0 4 * * ?")
	public void startSocket() {
		try {
			logger.info("============启动线程================");
			InstallSocket.busMapTemp.clear();
			//转发服务线程
			InstallSocket install = new InstallSocket(common808Service);
			install.init();
			install.start();
		} catch (IOException e) {
			logger.info("============启动线程出现异常================");
			e.printStackTrace();
		}	
	}
	
	/**
	 * 刷新数据
	 * @throws IOException
	 */
	//@Scheduled(cron = "0 0 2 * * ?")
	public void refreshData(){
		try {			
			//清空待发车
			DataCache.readySendBusMap.clear();
			//清空当天已上线电子站牌
			DataCache.ledQueue.forEach(item->msgProcessService.sessionManager.removeBySessionId(item.get("ledbh").toString()));
			//InstallSocket.closeFlg = 1;
			LedUpReceiver.registerMsgQueue.clear();
			LedUpReceiver.heartMapTemp.clear();
			LedUpReceiver.uploadParaMsgQueue.clear();
			LedUpReceiver.commonBackMsgQueue.clear();
			LedUpReceiver.reBackMsgQueue.clear();
			LedUpReceiver.commonBackInfoMsgQueue.clear();
			DataCache.saveMessageQueue.clear();	
			msgProcessService.ftpFileMap.clear();
			DataCache.yjdMap.clear();
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
	
	
	//@Scheduled(cron = "0 0/5 * * * ?")
	public void refreshLedDataTimer() {
		logger.info("refreshLedDataTimer 开始 ");
		long startTime = System.currentTimeMillis();
		List<Map<String, Object>> dataList= common808Service.selectLedAndLineInfo(new HashMap());
		if(DataCache.ledQueue !=null && !DataCache.ledQueue.isEmpty()) {
			DataCache.ledQueue.clear();
		}	
		DataCache.ledQueue.addAll(dataList);
		//车辆距离下一站距离小于此参数即为进站
		DataCache.distanceStr=common808Service.getDistance();
		//车辆距离下一站距离 < 站间距离+ 此距离 才为合法车辆
		DataCache.validDistanceStr=common808Service.getValidDistance();
		long endTime = System.currentTimeMillis();
		float excTime = (float) (endTime - startTime) / 1000;
		logger.info("refreshLedDataTimer 执行时间:{}s", excTime);
	}
	

	
	private void threadWeatherSend(List<String> ledList, final int threadSize) {
		if(ledList == null || ledList.size() == 0 ) return ;
        // 总数据条数
        int dataSize = ledList.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++) {
			List<String> subList;
			 if (i == threadNum - 1) {
	                if (special) {
	                    break;
	                }
	                subList = ledList.subList(threadSize * i, dataSize);
	            } else {
	            	subList = ledList.subList(threadSize * i, threadSize * (i + 1));
	        }
			Callable<String> task = new Callable<String>() {
				@Override
				public String call() throws Exception {					
					for (String ledNo : subList) {	
							Session session = msgProcessService.sessionManager.findBySessionId(ledNo);
							if (session != null) {	
								PackageData packageData = new PackageData();
								MsgHeader msgHeader = new MsgHeader();
								msgHeader.setLedNo(ledNo);
								packageData.setMsgHeader(msgHeader);	
								//if(ledNo.startsWith("00000011")) {
								//	msgProcessService.generateMsg0905ExtraBody(packageData,ftpWeather11XmlPath);
								//}else if(ledNo.startsWith("00000022")) {
									msgProcessService.generateMsg0905ExtraBody(packageData,ftpWeather22XmlPath);
								//}else {
								//	msgProcessService.generateMsg0905ExtraBody(packageData,ftpWeatherXmlPath);
								//}
						}
					}
					return "";
				}
			};
			executorService.submit(task);
		}
		executorService.shutdown();
	}


	
	/**
	 * 
	 * 创建写入文本
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static boolean creatTxtFile(String name) throws IOException {

		File path = new File(ResourceUtils.getURL("classpath:").getPath());
		if(!path.exists()) path = new File("");
		File upload =  new File(path.getAbsolutePath() + ConstantUtil.STATIC_FRONTEND_PROJECT_TEMPLATE);
		if(!upload.exists()) upload.mkdirs();
		boolean flag = false;
		filenameTemp = java.net.URLDecoder.decode(upload.getAbsolutePath().replaceAll("!", ConstantUtil.NULL_STRING), ConstantUtil.UTF_8) + File.separator + name + ".html";
		File filename = new File(filenameTemp);
		if (!filename.exists()) {
			filename.createNewFile();
			flag = true;
		}
		return flag;
	}

	/**
	 * 
	 * 向文本中写入内容并上传到服务器
	 * 
	 * @param newStr
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> writeTxtFile(String fileName, String newStr) throws IOException {
		Map<String, Object> resultMap = new HashMap<>();
		// 先读取原有文件内容,然后进行写入操作
		boolean flag = false;
		String filein = newStr;
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
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();

			buf.append(filein);

			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			// 上传到FTP
			FtpUtil.uploadFile(fileName + ".html", fis, ftpPort, ftpName, ftpPassWord, ftpBasePath);
			flag = true;
		} catch (IOException e1) {
			e1.printStackTrace();
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
		resultMap.put("flag", flag);
		resultMap.put("file-path", "ftp://" + ftpName + ":" + ftpPassWord + "@" + ftpAddress + ":" + ftpPort + ftpBasePath + fileName + ".html");
		return resultMap;
	}
	

	
	private void threadSendSystemTiming(List<String> ledList, int threadSize) {
		if(ledList == null || ledList.size() == 0 ) return ;
        // 总数据条数
        int dataSize = ledList.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++) {
			 List<String> subList;
			 if (i == threadNum - 1) {
	                if (special) {
	                    break;
	                }
	                subList = ledList.subList(threadSize * i, dataSize);
	            } else {
	            	subList = ledList.subList(threadSize * i, threadSize * (i + 1));
	        }
			Callable<String> task = new Callable<String>() {
				@Override
				public String call() throws Exception {					
					for (String ledNo : subList) {	
						Session session = msgProcessService.sessionManager.findBySessionId(ledNo);
						if (session != null) {							
							msgProcessService.generateMsg090ABody(ledNo, simpleDateFormat.format(new Date()));
						}				
					}
					return "";
				}
			};
			executorService.submit(task);
		}
		executorService.shutdown();		
	}
	// 多线程发送路况
	private void threadSendLukTiming(List<String> ledList, int threadSize) {
		if(ledList == null || ledList.size() == 0 ) return ;
        // 总数据条数
        int dataSize = ledList.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++) {
			 List<String> subList;
			 if (i == threadNum - 1) {
	                if (special) {
	                    break;
	                }
	                subList = ledList.subList(threadSize * i, dataSize);
	            } else {
	            	subList = ledList.subList(threadSize * i, threadSize * (i + 1));
	            }
			Callable<String> task = new Callable<String>() {
				@Override
				public String call() throws Exception {					
					for (String ledNo : subList) {	
						Session session = msgProcessService.sessionManager.findBySessionId(ledNo);
						if (session != null) {
						//if("000000119999".equals(ledNo)) {
							
							PackageData packageData = new PackageData();
							MsgHeader msgHeader = new MsgHeader();
							msgHeader.setLedNo(ledNo);
							packageData.setMsgHeader(msgHeader);
							List<Map<String, Object>> dataMapList = DataCache.ledQueue.stream().filter(p -> ledNo.equals(p.get("ledbh")))
									.collect(Collectors.toList());
							msgProcessService.generateMsg0909Body(packageData,dataMapList);
						//}
						}				
					}
					
					/*Long endTime = System.currentTimeMillis();
					
					float excTime = (float) (endTime - startTime) / 1000;
					logger.info("alltime lk down:"+excTime);*/
					//Thread.sleep(200); 原来是200毫秒 
					Thread.sleep(500); 
					
					return "";
				}
			};
			executorService.submit(task);
		}
		executorService.shutdown();		
	}
	
	// 单线程池在循环时间内平均下发路况信息
	private void threadSendLukSingle(List<String> ledList, int sleepTime) {
		//System.out.println("thread single start");
		logger.info("thread single start");
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
			singleThreadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						for (String ledNo : ledList) {	
							Session session = msgProcessService.sessionManager.findBySessionId(ledNo);
							if (session != null) {
								//if("000000119999".equals(ledNo)) {
								
								PackageData packageData = new PackageData();
								MsgHeader msgHeader = new MsgHeader();
								msgHeader.setLedNo(ledNo);
								packageData.setMsgHeader(msgHeader);
								List<Map<String, Object>> dataMapList = DataCache.ledQueue.stream().filter(p -> ledNo.equals(p.get("ledbh")))
										.collect(Collectors.toList());
								msgProcessService.generateMsg0909Body(packageData,dataMapList);
								Thread.sleep(sleepTime); 
								/*Long endTime = System.currentTimeMillis();
								System.out.println("endTime:"+endTime);*/
								//}
							}				
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		//}
		singleThreadExecutor.shutdown();
		

	}

	
 
	// 高德到站时间在本地执行
	//车辆到站时间
	@Scheduled(cron = "0/10 * * * * ?")
	public void getClTimeCache() {

		System.out.println("start car time");
		try {
				
			threadGetArriveTiming(DataCache.GaodeTimeLineStationList,100);
		} catch (Exception e) {
			logger.info("@@@@@@");
			e.printStackTrace();
		}
	
	}
	
	private void threadGetArriveTiming(List<Map<String, Object>> lineList, int threadSize) {

		if (lineList == null || lineList.size() == 0)
			return;
		// 总数据条数
		int dataSize = lineList.size();
		// 线程数
		int threadNum = dataSize / threadSize + 1;
		// 定义标记,过滤threadNum为整数
		boolean special = dataSize % threadSize == 0;
		ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
		for (int i = 0; i < threadNum; i++) {
			List<Map<String, Object>> subList;
			if (i == threadNum - 1) {
				if (special) {
					break;
				}
				subList = lineList.subList(threadSize * i, dataSize);
			} else {
				subList = lineList.subList(threadSize * i, threadSize * (i + 1));
			}
			Callable<String> task = new Callable<String>() {
				@Override
				public String call() throws Exception {
					for (Map<String, Object> map : subList) {
						// key gaodeline存高德lineid key matchMap 存线网线路站点 与高德线路站点匹配 map
						String gaodelineid = map.get("gaodeline")+"";
						Map matchMap=(Map)map.get("matchMap");
						getGaodeTime2Cache1(gaodelineid,matchMap);
						/*Map etaMap=gaodeLineService.getEta(glinecode,downflag);//取出的线路都是肯定下发的
						String eta=etaMap.get("eta").toString();
						String etaKey = lineid+"-"+ flag;
						DataCache.lkMapTemp.put(etaKey, eta);*/
					}
					endTime = System.currentTimeMillis();
					System.out.println("endtime:" + endTime);
					
					float excTime = (float) (endTime - startTime) / 1000;
					System.out.println("alltime:"+excTime);
					
					Thread.sleep(200); 
					return "";
				}
			};
			executorService.submit(task);
		}
		executorService.shutdown();
	
	}
	
	//调用高德接口 到站时间数据入缓存  用的 线网线路-方向-线网站点为key value 为 一个map 存车辆自编号 到站时间
		public /* Map<String,Integer> */void getGaodeTime2Cache1(String lineid, Map matchMap) {

			String param = "key=399cb4297d4acb776786f1d77e70aea8&frm=7bb7cbab20003a15d370b03c95b905db";
			// String
			// resultStr=SendGET("http://118.190.113.32:17070/v4/opendata/pubtransit/lineeta",param);
			Map<String, String> map = new HashMap<String, String>();
			// 106 上行 230100010169 106 下行 230100010170
			// 59 上行 230100010088 下行 230100010089
			//
			map.put("city", "0451");
			map.put("lines", lineid);
			param = param + CommonUtils.Map2Param(map);
			// System.out.println(param);
			String resultStr = CommonUtils.SendGET("http://restapi.amap.com/v4/bus/scheduling", param);
			//System.out.println(resultStr);
			com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(resultStr);
			String errcode = jsonObject.get("errcode").toString();
			//String errorDetail = jsonObject.get("errdetail").toString();// 报错的话 ，具体报错原因
			// 存到站时间的map
			//ConcurrentHashMap<String, Integer> dzTimeMap = new ConcurrentHashMap<String, Integer>();

			if ("0".equals(errcode)) {
				com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject) jsonObject.get("data");
				com.alibaba.fastjson.JSONArray items = data.getJSONArray("items");
				String itemStr = items.toString();
				List<Map> mapResult = com.alibaba.fastjson.JSONObject.parseArray(itemStr, Map.class);
				if (!mapResult.isEmpty()) {
					//System.out.println(mapResult);
					// trip
					String trip = mapResult.get(0).get("trip").toString();
					// 存 gpsid 车辆自编号 跟 index的map
					Map carTempMap = new HashMap();
					List<Map> carMapList = com.alibaba.fastjson.JSONObject.parseArray(trip, Map.class);
					for (Map m : carMapList) {
						String gpsid = (String) m.get("gpsId");
						String index = m.get("index") + "";
						carTempMap.put(index, gpsid);
					}
					//System.out.println("trip:" + trip);
					// station
					String station = mapResult.get(0).get("station").toString();// 高德站点
					//System.out.println("station:" + station);
					List<Map> stationMapList = com.alibaba.fastjson.JSONObject.parseArray(station, Map.class);
					//System.out.println(stationMapList);
					for (Map sm : stationMapList) {
						Object stationid = sm.get("stationId");
						Object sTrip = sm.get("trip");
						if (stationid != null && sTrip != null) {// 都不为空
							String stationStr = stationid + "";
							String sTripStr = sTrip + "";
							List<Map> jsonArrayTrip = com.alibaba.fastjson.JSONObject.parseArray(sTripStr, Map.class);
							// 从传来的 高德线路-高德站点 线网线路-方向-线网站点 map 取 线网线路-方向-线网站点
							String gaodeLineStation = lineid + "-" + stationStr;
							String xlineFxStation = matchMap.get(gaodeLineStation) + "";
							if (xlineFxStation == null || xlineFxStation.length() == 0) {// 传来的map没取出 线网线路id-方向-站点id
								// 没取到线网的线路，方向，站点字符串
								System.out.println("no  match xiwang line fx station");
								System.out.println("gaodeLineStation:" + gaodeLineStation);
							} else {
								// 存车辆到站时间
								Map timeMap = new HashMap();
								String minTime=null;
								for (Map mTrip : jsonArrayTrip) {
									String indexStr = mTrip.get("index") + "";// index
									// System.out.println(indexStr);

									String carid = carTempMap.get(indexStr) + "";// 车辆自编号
									int eta = (Integer) mTrip.get("eta");// 到站时间
									int stationTime=0;//到站时间初始化
									if(eta%60 == 0) {// 正好是整数分钟
										stationTime = eta / 60;//到站时间变成分钟
									}else {
										stationTime = (eta / 60)+1;// 有小数分钟 就+1分钟
									}
									if(minTime == null) {//排序第一个到站时间
										minTime=stationTime+"";
									}else {
										if(Integer.parseInt(minTime)>stationTime) {
											minTime=stationTime+"";
										}
										
									}
									timeMap.put(carid, stationTime);
								}
								timeMap.put("minTime", Integer.parseInt(minTime));
								// dzTimeMap.put(xlineFxStation+"-"+carid, stationTime);
								DataCache.dzTimeMap.put(xlineFxStation, timeMap);
							}
						}
					}
				}

			}else {
				logger.info("error gaodeline:"+lineid);
				logger.info("error code:"+errcode);
				//logger.info("error errordetail:"+errorDetail);
			}
			System.out.println("end car time");
			System.out.println(DataCache.dzTimeMap);

		}
	
}