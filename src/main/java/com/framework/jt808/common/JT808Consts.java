package com.framework.jt808.common;

import java.nio.charset.Charset;

public class JT808Consts {
	public static final String string_encoding = "GBK";
	public static final String gd_string_encoding = "UTF-8";
	public static final Charset string_charset = Charset.forName(string_encoding);
	// 字符串结尾
	public static final String string_end = "00";
	// 标识位
	public static final int pkg_delimiter = 0x7e;
	// 客户端发呆30分钟后,服务器主动断开连接
	public static final int tcp_client_idle_minutes = 30;
	// 通用应答
	public static final int msg_id_0x0601 = 0x0601;
	// 后台通用应答
	public static final int msg_id_0x0901 = 0x0901;
	// 上传设备登录
	public static final int msg_id_0x0602 = 0x0602;
	// 下发设置显示屏分区配置文件
	public static final int msg_id_0x0905 = 0x0905;
	// 下发线路列表
	public static final int msg_id_0x0907 = 0x0907;
	// 下发线路属性信息
	public static final int msg_id_0x0908 = 0x0908;
	// 下发线路公告信息
	public static final int msg_id_0x0904 = 0x0904;
	// 上传设备状态兼心跳包
	public static final int msg_id_0x0603 = 0x0603;
	// 下发电子站牌参数
	public static final int msg_id_0x0902 = 0x0902;
	// 下发电子站牌参数查询
	public static final int msg_id_0x0903 = 0x0903;
	// 上传电子站牌参数查询应答
	public static final int msg_id_0x0605 = 0x0605;
	// 实时线路指示
	public static final int msg_id_0x0906 = 0x0906;
	// 下发路况数据
	public static final int msg_id_0x0909 = 0x0909;
	
	// 下发系统时间
	public static final int msg_id_0x090A = 0x090A;
	

	public static final int msg_id_0x090B = 0x090B;
	
	//数据返回应答
	public static final int msg_id_0x0607 = 0x0607;

	// 电子站牌上传数据MQ
	public static final String led_up_queueName = "q_dzzp_up_data";
	// 给电子站牌发数据MQ
	public static final String led_down_queueName = "q_dzzp_down_data";

	public static final byte DATE_TYPE_LED_DOWN = 60;
	public static final byte BSBCP_CMD_LED_DOWN = 12;

	
	public static final String gd_identifier = "bus_tcp_client|";
	
	public static final String heb_citycode = "230100";
	public static final String gd_datasouce = "bus_harbin";
	public static final String gd_signal_type = "1";
	
	//消息id长度
	public static final int msg_id_length = 2;
	//消息体长度
	public static final int msg_length = 1;
	
	public static final int msg_length_length = 2;
	
	
	//主后台IP地址
	public static final int para_id_0x0101 = 0x0101;
	//主后台端口号
	public static final int para_id_0x0103 = 0x0103;
	//网关
	public static final int para_id_0x0108 = 0x0108;
	//本地设备IP地址
	public static final int para_id_0x010A = 0x010A;
	//子网掩码
	public static final int para_id_0x010B = 0x010B;
	//电子站牌ID号
	public static final int para_id_0x0200 = 0x0200;
	//设备有效时间
	public static final int para_id_0x0207 = 0x0207;
	//每天循环时间段
	public static final int para_id_0x0208 = 0x0208;
	//设备温度控制
	public static final int para_id_0x0209 = 0x0209;
	//外部风扇温度控制
	public static final int para_id_0x020A = 0x020A;
	//横流风扇温度控制
	public static final int para_id_0x020B = 0x020B;
	//加温模块温度控制
	public static final int para_id_0x020C = 0x020C;
	//控制模式
	public static final int para_id_0x020D = 0x020D;
	//断网复位
	public static final int para_id_0x020E = 0x020E;
	//设备过流保护
	public static final int para_id_0x020F = 0x020F;
	//设备水浸保护
	public static final int para_id_0x0210 = 0x0210;
	//设备复位重启
	public static final int para_id_0x0211 = 0x0211;
	
	
	public static final int dword_length = 4;
	public static final int word_length = 2;
	public static final int byte_length = 1;
	
	// 参数查询
	public static final int para_search_0x0903 = 0xffff;
	
	
	public static final int one_package_size =100;
}

