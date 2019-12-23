package com.framework.webClient.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 文件名 ConstantUtil 描述 常量工具类
 * 
 * @author 吉庆 创建日期 2018年5月28日
 */
public class ConstantUtil {

	/**
	 * 总数常量KEY
	 */
	public static final String TOTAL = "total";

	/**
	 * 数据常量KEY
	 */
	public static final String ROWS = "rows";

	/**
	 * 消息KEY常量
	 */
	public static final String MSG = "message";

	/**
	 * 消息状态KEY常量
	 */
	public static final String STATUS = "status";

	/**
	 * sheetName字符串常量
	 */
	public static final String SHEETNAME = "sheetName";

	/**
	 * 常量字符串0
	 */
	public static final String STATUS_ZERO = "0";

	/**
	 * 常量字符串1
	 */
	public static final String STATUS_ONE = "1";

	/**
	 * 常量字符串-1
	 */
	public static final String STATUS_FUONE = "-1";

	/**
	 * 常量整型0
	 */
	public static final int COMMONS_ZERO = 0;

	/**
	 * 常量整型1
	 */
	public static final int COMMONS_ONE = 1;

	/**
	 * 操作成功
	 */
	public static final String COMMON_SUCCESS = "操作成功";

	/**
	 * 操作失败
	 */
	public static final String COMMON_FAILURE = "操作失败";

	/**
	 * 未知原因导致的失败提示消息常量
	 */
	public static final String COMMON_WZYY_FAILURE = "由于未知原因导致操作失败，请联系管理员。";

	/**
	 * 模版名称字符串常量
	 */
	public static final String MBMC_KEY = "mbmc";

	/**
	 * 模版编号字符串常量
	 */
	public static final String MBBH_KEY = "mbbh";

	/**
	 * "模版名称存在，请更换"字符串常量
	 */
	public static final String MBMC_STRING_HAVE = "模版名称存在，请更换";

	/**
	 * "模版编号存在，请更换"字符串常量
	 */
	public static final String MBBH_STRING_HAVE = "模版编号存在，请更换";

	/**
	 * tag字符串常量
	 */
	public static final String TAG_KEY = "tag";

	/**
	 * dataList字符串常量
	 */
	public static final String DATALIST_KEY = "dataList";

	/**
	 * 上传文件失败字符串常量
	 */
	public static final String COMMON_UPLOAD_FAILE = "上传文件失败";

	/**
	 * 上传文件成功字符串常量
	 */
	public static final String COMMON_UPLOAD_SUCCESS = "上传文件成功";

	/**
	 * "text"字符串常量
	 */
	public static final String TEXT_KEY = "text";

	/**
	 * 文本分区类型： 0
	 */
	public static final String FQLX_TXT_ZERO = "0";

	/**
	 * 文本分区类型： 1
	 */
	public static final String FQLX_IMG_ONE = "1";

	/**
	 * 文本分区类型： 2
	 */
	public static final String FQLX_VOID_TWO = "2";

	/**
	 * 是否使用全部的标志tag
	 */
	public static final String COMMON_TAG = "tag";

	/**
	 * 全部的常量
	 */
	public static final String COMMON_QUANBU = "全部";

	/**
	 * 线路ID常量
	 */
	public static final String COMMON_XLID = "xlid";

	/**
	 * 线路名称常量
	 */
	public static final String COMMON_XLMC = "xlmc";

	/**
	 * 线路名称常量
	 */
	public static final String COMMON_LPMC = "lpmc";

	/**
	 * 空串常量
	 */
	public static final String NULL_STRING = "";

	/**
	 * led方向
	 */
	public static final String LEDFX_VALUE = "15";

	/**
	 * 方向
	 */
	public static final String FX_VALUE = "16";
	
	/**
	 * 站点停靠
	 */
	public static final String ZDTK_VALUE = "19";
	
	/**
	 * 启用状态
	 */
	public static final String QYZT_VALUE = "20";
	
	/**
	 * 站点启用
	 */
	public static final String QYZD_VALUE = "21";

	/**
	 * 属性类型对应字段编号
	 */
	public static final Map<String, Object> LED_ATTR_COMMONSET_MAP = new HashMap<>();
	static {
		LED_ATTR_COMMONSET_MAP.put("font-size", "5");
		LED_ATTR_COMMONSET_MAP.put("font-style", "6");
		LED_ATTR_COMMONSET_MAP.put("font-weight", "7");
		LED_ATTR_COMMONSET_MAP.put("show-style", "8");
		LED_ATTR_COMMONSET_MAP.put("move-speed", "9");
		LED_ATTR_COMMONSET_MAP.put("sound-flag", "10");
		LED_ATTR_COMMONSET_MAP.put("sound-volume", "11");
		LED_ATTR_COMMONSET_MAP.put("sound-speed", "12");
		LED_ATTR_COMMONSET_MAP.put("sound-count", "13");
		LED_ATTR_COMMONSET_MAP.put("replay-count", "14");

	}

	/**
	 * JcsjService字符串常量
	 */
	public static final String JCSJSERVICE_KEY = "JcsjService";

	/**
	 * 用户名常量
	 */
	public static final String COMMON_USER_NAME = "userName";

	/**
	 * dataList字符串常量KEY
	 */
	public static final String DATALIST = "dataList";

	/**
	 * 导入数据集合KEY常量
	 */
	public static final String IMPORTLIST = "importData";

	/**
	 * 数据key常量
	 */
	public static final String COMMON_DATA = "data";

	/**
	 * 导入Excel文件列标题变量名
	 */
	public static final String ANY_CONSTANT_IMPORT_TITLEVAR = "ledmc,ledbh,jd,wd,wg,bdsbipdz,zwym,csmbmc,fqmbmc,xh,xlmc,dqzdmc,fx";

	/**
	 * 导入Excel文件中数据起始行, 从0开始
	 */
	public static final int ANY_CONSTANT_IMPORT_DATAROW = 2	;

	/**
	 * 导入Excel文件中数据总列数
	 */
	public static final int ANY_CONSTANT_IMPORT_COUNTCOL = 13;

	/**
	 * 导入Excel文件列标题
	 */
	public static final String ANY_CONSTANT_IMPORT_TITLE = "站牌名称,站牌编号,经度,纬度,网关,本地设备IP地址,子网掩码,参数模板,分区模板,序号,线路名称,当前站点名称,方向";

	/**
	 * 导入成功提示消息
	 */
	public static final String IMPORTEXCEL_SUCCESS = "导入成功";

	/**
	 * 导入异常提示消息
	 */
	public static final String IMPORTEXCEL_FAILE = "导入异常";

	/**
	 * Excel的表头不能为空，请使用标准模版。
	 */
	public static final String IMPORTEXCEL_TITLE_NULL = "Excel的表头不能为空，请使用标准模版。";

	/**
	 * Excel的表头不能为空，请使用标准模版。
	 */
	public static final String IMPORTEXCEL_TITLE_ERROR = "Excel的表头不正确，请使用标准模版。";

	/**
	 * 导入电子站牌访问请求接口常量
	 */
	public static final String IMPORTLEDEXCEL = "/importLedExcel";

	/**
	 * 导入电子站牌参数常量
	 */
	public static final String FILE = "file";

	/**
	 * 逗号常量
	 */
	public static final String DOUHAO = ",";

	/**
	 * errorMsg字符串常量
	 */
	public static final String ERRORMSG = "errorMsg";

	/**
	 * titleString字符串常量
	 */
	public static final String TITLESTRING = "titleString";

	/**
	 * IP正则表达式字符串常量
	 */
	public static final String IPREG = "^((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))$";

	/**
	 * sheet页名称不能为空，请按照'线路名（运行方向）'的格式进行添加。例如：'801（上行）'常量
	 */
	public static final String SHEETNAME_CANNOT_NULL = "sheet页名称不能为空，请按照'线路名（运行方向）'的格式进行添加。例如：'801（上行）'";

	/**
	 * sheet页名称长度不符合规范，请保证sheet页名称长度在5-54之间'常量
	 */
	public static final String SHEETNAME_LENGTH_ERROR = "sheet页名称长度不符合规范，请保证sheet页名称长度在5-54之间'";

	/**
	 * sheet页获取的线路名称常量
	 */
	public static final String SHEETXLMC = "sheetXlmc";

	/**
	 * 上行常量
	 */
	public static final String SHANGXING = "上行";

	/**
	 * sheet页获取的运行方向常量
	 */
	public static final String SHEETYXFX = "sheetYxfx";

	/**
	 * sheet页获取的运行方向编码常量
	 */
	public static final String SHEETYXFXSTATUS = "sheetYxfxStatus";

	/**
	 * sheet页获取的运行方向上行编码值常量
	 */
	public static final String SHEETYXFX_SHANGXING = "0";

	/**
	 * 下行常量
	 */
	public static final String XIAXIGN = "下行";

	/**
	 * sheet页获取的运行方向下行编码值常量
	 */
	public static final String SHEETYXFX_XIAXING = "1";

	/**
	 * 导入错误消息文本字符串常量
	 */
	public static final String IMPORT_ERROR_MESSAGE = "cwxx";

	/**
	 * "filePath"字符串常量
	 */
	public static final String FILEPATH = "filePath";

	/**
	 * 的第字符串常量
	 */
	public static final String MESSAGE_DE_DI = "的第";

	/**
	 * 换行标记
	 */
	public static final String HUANHANG = "\r\n";

	/**
	 * 错误消息存储路径常量
	 */
	public static final String STATIC_FRONTEND_PROJECT_TEMPLATE = "/static/frontend/project/template/";

	/**
	 * UTF_8字符串常量
	 */
	public static final String UTF_8 = "UTF-8";

	/**
	 * .txt字符串常量
	 */
	public static final String WORD_TXT = ".txt";

	/**
	 * 导出错误文件
	 */
	public static final String DOWNERROEFILE = "/downErrorFile";

	/**
	 * user-agent字符串常量
	 */
	public static final String USER_AGENT = "user-agent";

	/**
	 * msie字符串常量
	 */
	public static final String MSIE = "msie";

	/**
	 * 错误信息
	 */
	public static final String ERRORFILENAME = "错误信息";

	/**
	 * like gecko字符串常量
	 */
	public static final String LIKE_GECKO = "like gecko";

	/**
	 *  ISO_8859_1字符串常量
	 */
	public static final String ISO_8859_1 = "iso-8859-1";
	
	/**
	 * 天气预报文件名
	 */
	public static final String WEATHERNAME22 = "weather22";
	
	/**
	 * 天气预报文件名11
	 */
	public static final String WEATHERNAME11 = "weather11";
	/**
	 * 天气预报文件名
	 */
	public static final String WEATHERXMLNAME = "weather.xml";
}
