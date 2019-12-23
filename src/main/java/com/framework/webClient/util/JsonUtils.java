package com.framework.webClient.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 文件名: JsonUtils
 * 描    述: Json转化共通类
 * 作    者: 刘佩刚
 * 创建日期: 2018-5-22
 */
public class JsonUtils {
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String convertObj2String(Object object) {
		String s = null;
		try {
			s = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static <T> T convertString2Obj(String s, Class<T> clazz) {
		T t = null;
		try {
			t = objectMapper.readValue(s, clazz);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return t;
	}
}
