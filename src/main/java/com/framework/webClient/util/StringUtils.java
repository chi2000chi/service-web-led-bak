package com.framework.webClient.util;

import java.util.ArrayList;

/**
 * 
 * 文件名  StringUtils
 * 描述  公用数据类型转换
 * @auther 吉庆
 * 创建日期  2018年5月28日
 */
public class StringUtils {

	/**
	 * 
	 *  ArrayList类型转成String类型
	 * @param arrayList
	 * @return
	 */
	public static String ArrayList2String(ArrayList<String> arrayList) {
		String result = "";
		if (arrayList != null && arrayList.size() > 0) {
			for (String item : arrayList) {
				// 把列表中的每条数据用逗号分割开来，然后拼接成字符串
				result += item + ",";
			}
			// 去掉最后一个逗号
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}
	
	/** 
	 *  
	 * 判断字符串是否为空，包括字符串"null"也会判断为空，  字符串 "undefined"的验证，全是空格的验证.
	 * @param target
	 * @return
	 */
	public static boolean isEmpty(String target){
		return target == null || "".equals(target.trim()) || "null".equals(target) || "undefined".equals(target);
	}
	/**
	 * 
	 *  字符串前面补充零
	 * @param str 目标字符串
	 * @param strLength 字符串补充零后到多长
	 * @return
	 */
	public static String addZeroForNum(String str, int strLength) {
		//字符串为null 转为 ""防止报错
		if(str == null) {
			str="";
		}
	    int strLen = str.length();
	    if (strLen < strLength) {
	        while (strLen < strLength) {
	            StringBuffer sb = new StringBuffer();
	            sb.append("0").append(str);// 左补0
	            // sb.append(str).append("0");//右补0
	            str = sb.toString();
	            strLen = str.length();
	        }
	    }
	 
	    return str;
	}    
}
