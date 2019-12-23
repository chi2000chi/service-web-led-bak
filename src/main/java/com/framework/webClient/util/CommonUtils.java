package com.framework.webClient.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommonUtils {
	/**
	 * 将字符串格式yyyyMMdd的字符串转为日期，格式"yyyy-MM-dd"
	 *
	 * @param date
	 *            日期字符串
	 * @return 返回格式化的日期
	 * @throws ParseException
	 *             分析时意外地出现了错误异常
	 */
	public static String strToDateFormat(String date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		formatter.setLenient(false);
		Date newDate = formatter.parse(date);
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(newDate);
	}

	/**
	 * 将字符串格式HHmm的字符串转为日期，格式"HH:mm"
	 *
	 * @param date
	 *            日期字符串
	 * @return 返回格式化的日期
	 * @throws ParseException
	 *             分析时意外地出现了错误异常
	 */
	public static String strToTimeFormat(String time) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
		formatter.setLenient(false);
		Date newDate = formatter.parse(time);
		formatter = new SimpleDateFormat("HH:mm");
		return formatter.format(newDate);
	}

	// get url 接口返回参数
	public static String SendGET(String url, String param) {
		String result = "";// 访问返回结果
		BufferedReader read = null;// 读取访问结果

		try {
			// 创建url
			URL realurl = new URL(url + "?" + param);
			// 打开连接
			URLConnection connection = realurl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段，获取到cookies等
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			read = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line;// 循环读取
			while ((line = read.readLine()) != null) {
				result += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (read != null) {// 关闭流
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
	
	//map copy paraMap 被copy 的map，resutlMap copy的结果map
	public static void mapCopy(Map paraMap,Map resutlMap) {
		if(resutlMap == null) {
			resutlMap=new HashMap();
		}
		if(paraMap == null) {
			return;
		}
		Iterator it=paraMap.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry entry=(Map.Entry) it.next();
			Object key=entry.getKey();
			resutlMap.put(key, paraMap.get(key)!=null?paraMap.get(key):"");
		}
	}
	
	//map里的数据 转为 &key1=value1&key2=value2
	public static String Map2Param(Map<String,String> map) {
		Map<String,String> mapTemp =map;
		String param="";
		if(mapTemp != null && mapTemp.size()>0) {
			  for (Map.Entry<String, String> m : mapTemp.entrySet()) {
				  param+="&"+m.getKey()+"="+m.getValue();
			  }
		}
		return param;
	}
}
