package com.framework;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.junit4.SpringRunner;

import com.framework.jt808.common.DataCache;
import com.framework.jt808.util.HexStringUtils;
import com.framework.jt808.util.JT808ProtocolUtils;
import com.framework.jt808.util.MsgDecoder;
import com.framework.jt808.vo.BusState;
import com.framework.jt808.vo.PackageData;
import com.framework.util.GpsDistanceUtils;
import com.framework.util.SystemUtil;
import com.framework.webClient.dispatch.receiver.LedUpReceiver;

//7E0908010200000022225202210003BB4F000000000A02001400016E312CBBC6BAD3C2B7B6ABB6CB2C303B322CD0C5BAE3B9C5CDE6B3C72C303B332CBACDC6BDD0A1C7F82C303B342CBAEACEB0C2B7A3A8CFC8B7E6C2B7BFDAA3A92C303B352CBAECC6ECBCD2BEDFB3C72C303B362CBAECC6BDD0A1C7F82C303B372CB4F3D0C2D0ACB3C72C303B382CCAD0B9FACBB0BBFCB2E9BED62C303B392CCCABC6BDC7C52C303B31302CC4CFCDA8B4F3BDD62C303B31312CB9FEB9A4B3CCB4F3D1A72C303B31322CD1CCB3A72C303B31332CD2BDB4F3D2BBD4BA2C303B31342CC7EFC1D6B9ABCBBE2C303B31352CB2A9CEEFB9DD2C303B31362CBEADCEB3BDD62C303B31372CB1B1B0B2BDD62C303B31382CB0B2C9FDBDD6A3A8D0C2D1F4C2B7BFDAA3A92C303B31392CB0B2BAECBDD62C303B32302CBDA8B9FABDD62C303B32312CC7B0BDF8C2B72C303B32322CB9ABC2B7B4F3C7C52C303B32332CBAD3B9C4BDD62C303B32342CBAD3D5FED0A1C7F82C303B32352CBAD3CBC9BDD62C303B32362CBAD3CBC9D0A1C7F82C303B00AC7E
public class demo {
	LedUpReceiver re = new LedUpReceiver();
	private final static JT808ProtocolUtils jt808ProtocolUtil = new JT808ProtocolUtils();
	private final static MsgDecoder decoder = new MsgDecoder();
	static GpsDistanceUtils utils = new GpsDistanceUtils();
	public static void main(String[] args) {
		String msg = "7E0607000500000022160800005FAA09090000CD7E";
		test(msg);
//		double distance = utils.earthDis(Double.valueOf("45.72317"),Double.valueOf ("126.632838"),Double.valueOf("45721704") / 1000 / 1000, Double.valueOf("126637224") / 1000 / 1000);
//		System.out.println(distance);
//		int stationNo = 10;
//		BusState state = new BusState();
//		state.setBusState(BusState.onRoad);
//		state.setNextStationNo(10);
//		if(stationNo != state.getNextStationNo()) {
//			System.out.println(4);
//	}else {
//		if (state.getBusState() == BusState.onRoad) {
//			System.out.println(4);
//		}else {
//			System.out.println(0);
//		}												
//	}
	}
	private static  void test(String msg) {
		byte[] bytes = HexStringUtils.toBytes(msg);
		try {
			// 接收消息时转义
			bytes = jt808ProtocolUtil.doEscape4Receive(bytes, 1, bytes.length);
			// 解析消息
			PackageData pkg = decoder.bytes2PackageData(bytes);
			System.out.println(pkg);
			processPackageData(pkg);
		} catch (Exception e) {
			e.printStackTrace();

		}
	}
	private static void processPackageData(PackageData packageData) {
		try {
			HashMap<String, Object> dataMap = decoder.decodeCommonBackDetailMsg(packageData.getMsgBodyBytes());
			System.out.println(dataMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
