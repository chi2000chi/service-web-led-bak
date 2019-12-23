package com.framework.webClient.util;

import java.util.UUID;

public class UUIDTools {
	/**
	 * description：uuid生成
	 * author: liupg
	 * date: 2018-05-07
	 */
	public static String getUUID() {

		return UUID.randomUUID().toString().replace("-", "");
	}
}