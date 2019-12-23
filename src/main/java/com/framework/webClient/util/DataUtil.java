/**
 * 
 */
package com.framework.webClient.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wk
 * 
 */
public class DataUtil {
	public static final String METHOD_GET = "GET";

	public static final String METHOD_POST = "POST";

	public static final String TOKEN_BRT = "WHBRT001";

	public static final String TOKEN_CONTROL_HTTP = "admin1";

	public static final String TOKEN_KEY_HTTP = "CIC123";

	public static String BRT_DATA_SOURCE = "";

	public static String TRANSFER_ADDR = "";

	public static String TRANSFER_ADDR_CONTROL = "";

	// 3.4.1 固定排班信息
	public static String BRT_ADDR_SCHEDULEINFO = "";
	// 3.4.2 实时调度信息
	public static String BRT_ADDR_DISPATCHINFO = "";
	// 3.4.3 车辆状态信息
	public static String BRT_ADDR_BUSSTATE = "";
	// 3.4.5 线路站点信息
	public static String BRT_ADDR_ROUTESTATIONDATA = "";
	// 3.4.6 车辆进出站信息
	public static String BRT_ADDR_BUSINOUTDATA = "";
	// 3.4.7 车辆GPS数据
	public static String BRT_ADDR_GPSBASEDATA = "";
	// 3.4.8 非日常运营调度信息
	public static String BRT_ADDR_DAILYDISPATCH = "";
	// 3.4.9 驾驶员考勤信息
	public static String BRT_ADDR_BUSDRIVERINFO = "";
	// 3.4.10 行车动态数据接口
	public static String BRT_ADDR_ROADLISTDATA = "";
	// 3.4.11 车辆报警
	public static String BRT_ADDR_BUSALARM = "";

	// 3.4.5 线路站点信息
	public static String BRT_ADDR_ROUTESTATIONDATA_HTTP = "";
	// 3.4.6 车辆进出站信息
	public static String BRT_ADDR_BUSINOUTDATA_HTTP = "";
	// 3.4.7 车辆GPS数据
	public static String BRT_ADDR_GPSBASEDATA_HTTP = "";
	// 3.5 获取BRT站点与公交站点对应关系信息
	public static String BRT_ADDR_STOPINFO_HTTP = "";
	// 3.6 获取运营统计信息
	public static String BRT_ADDR_OPRATIONDATA_HTTP = "";
	// 3.7 司机的分配比例
	public static String BRT_ADDR_DRIVERPERCERT_HTTP = "";
	public static String BRT_ADDR_DRIVERPERCERTEX_HTTP = "";
	// 3.4.11 获取线路实时调度数据
	public static String BRT_ADDR_LINEDISPATCH_HTTP = "";

	public static boolean LOG_FLAG = false;

	public static Map<String, String> LINE_BUS_REL = new HashMap<String, String>();

	//3.4.1	机构信息（新增）
	public static String BRT_ADDR_ORGAN_HTTP="";
	//3.4.2	线路基础数据（新增）
	public static String BRT_ADDR_BASEROUTEDATA_HTTP="";
	//3.4.3	运营车辆基础信息（新增）
	public static String BRT_ADDR_BUSBASEDATA_HTTP="";
	//3.4.4	司机基础信息（新增）
	public static String BRT_ADDR_DRIVERBASEDATA_HTTP="";
	//3.4.5	站台基础信息（新增）
	public static String BRT_ADDR_BUSSTATIONDATA_HTTP="";
}
