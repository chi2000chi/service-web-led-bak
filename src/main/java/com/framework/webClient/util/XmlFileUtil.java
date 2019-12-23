/**  
 * @Title  XmlUtil.java
 * @Package com.framework.webClient.util
 * @author 吉庆
 * @date 2018年8月22日
 */
package com.framework.webClient.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 文件名 XmlFileUtil 描述 xml文件生成导出工具类
 * 
 * @auther 吉庆 创建日期 2018年8月22日
 */
public class XmlFileUtil {
	
	// ftp服务器ip地址
    private static String FTP_ADDRESS;
    // 端口号
    private static int FTP_PORT;
    // 用户名
    private static String FTP_USERNAME;
    // 密码
    private static String FTP_PASSWORD;
    // ftp上传文件路径
    private static String FTP_BASEPATH;

	/**
	 * 生成xml文件
	 * @param dest
	 * @param ledAttr
	 * @param ledCommonset
	 * @param ledFile
	 * @param createStatus 
	 * @param ledWeather 
	 * @param dataMap  
	 */
	@SuppressWarnings("unchecked")
	public static boolean createXmlByDom(String ledAttr, String ledCommonset, String ledFile, String ledMsg, String ledWeather, boolean createStatus, Map<String, Object> dataMap) {
		// 创建DocumentBuilderFactory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			// 创建DocumentBuilder
			DocumentBuilder builder = factory.newDocumentBuilder();

			// 创建Document
			Document document = builder.newDocument();

			// 设置XML声明中standalone为yes，即没有dtd和schema作为该XML的说明文档，且不显示该属性
			document.setXmlStandalone(true);

			// 创建根节点
			Element configuration = document.createElement("configuration");
			
			
			// 判断是否创建显示屏切换时间、通用和天气标签
			if (createStatus) {
				
				// 创建电子站牌配置的线路和广告的显示屏切换时间 单位秒 默认
				Element ledConfig = document.createElement("ledConfig");
				Element ledLineShowTime = document.createElement("led_line_show_time");
				Element ledAdShowTime = document.createElement("led_ad_show_time");
				ledLineShowTime.setTextContent(String.valueOf(dataMap.get("ledLineShowTime")));
				ledAdShowTime.setTextContent(String.valueOf(dataMap.get("ledAdShowTime")));
				ledConfig.appendChild(ledLineShowTime);
				ledConfig.appendChild(ledAdShowTime);
				configuration.appendChild(ledConfig);
				
				// 通用消息设置
				Element commonMsg = document.createElement("CommonMsg");
				// 根据传来的数据创建通用消息设置下子节点
				List<Map<String, Object>> ledMsgList = (List<Map<String, Object>>) dataMap.get(ledMsg);
				if (ledMsgList != null && !ledMsgList.isEmpty()) {
					int i = 0;
					for (Map<String, Object> msgMap : ledMsgList ) {
						int timeTemplate = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(0, 10));
						int x= 1+(int)(Math.random()*10000);
						i = i + x;
						Element msgNo = document.createElement(String.valueOf(msgMap.get("msgcode")));
						msgNo.setAttribute("bgColor", String.valueOf(msgMap.get("bgcolor")));
						msgNo.setAttribute("fontcolor", String.valueOf(msgMap.get("fontcolor")));
						msgNo.setAttribute("fontsize", String.valueOf(msgMap.get("fontsize")));
						msgNo.setAttribute("serial-num", String.valueOf(timeTemplate + i));
						msgNo.setTextContent(String.valueOf(msgMap.get("msginfo")));
						commonMsg.appendChild(msgNo);
					}
					configuration.appendChild(commonMsg);
				}
				
				// 创建天气标签
				Element weatherArea = document.createElement("weatherArea");
				Element File = document.createElement("File");
				int timeTemplate = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(0, 10));
				int x= 1+(int)(Math.random()*10000);
				File.setAttribute("serial-num", String.valueOf(timeTemplate + x));
				File.setAttribute("file-path", String.valueOf(dataMap.get(ledWeather)));
				weatherArea.appendChild(File);
				configuration.appendChild(weatherArea);
			}
			
			// 根据传来的分区数据创建子节点
			List<Map<String, Object>> dataList = (List<Map<String, Object>>) dataMap.get("dataList");
			// 将数据写入
			if (dataList != null && !dataList.isEmpty()) {
				
				for (Map<String, Object> fqMap : dataList) {
					String fqlx = String.valueOf(fqMap.get("fqlx"));
					// 文本处理
					if (fqlx.equals("0")) {
						Element noticeArea = document.createElement("noticeArea");
						// 为节点添加属性
						List<Map<String, Object>> fileList = (List<Map<String, Object>>) fqMap.get(ledFile);
						int i = 0;
						for (Map<String, Object> fileMap : fileList) {
							int timeTemplate = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(0, 10));
							int x= 1+(int)(Math.random()*10000);
							i = i + x;
							Element File = document.createElement("File");
							if (!StringUtils.isEmpty(String.valueOf(fileMap.get("file-path")))) {
								File.setAttribute("serial-num", String.valueOf(timeTemplate + i));
								File.setAttribute("file-path", String.valueOf(fileMap.get("file-path")));
								File.setAttribute("fileSize", String.valueOf(fileMap.get("fileSize")));
							}
							noticeArea.appendChild(File);
						}
						configuration.appendChild(noticeArea);
					} 
					// 广告区域
					if (fqlx.equals("1")) {
						// fileAttr 属性 1：视频  2：图片
						Element adArea = document.createElement("adArea");
						// 为节点添加属性
						List<Map<String, Object>> fileList = (List<Map<String, Object>>) fqMap.get(ledFile);
						int i = 0;
						for (Map<String, Object> fileMap : fileList) {
							Element File = document.createElement("File");
							if (!StringUtils.isEmpty(String.valueOf(fileMap.get("file-path")))) {
								int timeTemplate = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(0, 10));
								int x= 1+(int)(Math.random()*10000);
								i = i + x;
								String fileAttrType = String.valueOf(fileMap.get("file-path")).substring(String.valueOf(fileMap.get("file-path")).length() - 4, String.valueOf(fileMap.get("file-path")).length());
								if (fileAttrType.equals(".mp4")) {
									File.setAttribute("fileAttr", "1");
								} else {
									File.setAttribute("fileAttr", "2");
								}
								File.setAttribute("serial-num", String.valueOf(timeTemplate + i));
								File.setAttribute("file-path", String.valueOf(fileMap.get("file-path")));
								File.setAttribute("fileSize", String.valueOf(fileMap.get("fileSize")));
								adArea.appendChild(File);
							}
						}
						configuration.appendChild(adArea);
					}
					// 视频区域
					if (fqlx.equals("2")) {
						// order 属性：视频播放次序
						// serial-num 属性：文件唯一码
						// file-path 属性：文件ftp路径
						Element videoArea = document.createElement("videoArea");
						// 为节点添加属性
						List<Map<String, Object>> fileList = (List<Map<String, Object>>) fqMap.get(ledFile);
						int i = 0;
						for (Map<String, Object> fileMap : fileList) {
							Element File = document.createElement("File");
							if (!StringUtils.isEmpty(String.valueOf(fileMap.get("file-path")))) {
								// 获取时间戳
								int timeTemplate = Integer.parseInt(String.valueOf(System.currentTimeMillis()).substring(0, 10));
								i = i + 1;
								File.setAttribute("order", String.valueOf(i));
								File.setAttribute("file-path", String.valueOf(fileMap.get("file-path")));
								File.setAttribute("serial-num", String.valueOf(timeTemplate + i));
								File.setAttribute("fileSize", String.valueOf(fileMap.get("fileSize")));
								videoArea.appendChild(File);
							}
						}
						configuration.appendChild(videoArea);
					}
				}
			}
			
			// 将根节点添加到document下
			document.appendChild(configuration);
			
			// 生成xml文件
			// 创建TransformerFactory对象
            TransformerFactory tff = TransformerFactory.newInstance();
			
            // 创建Transformer对象
            Transformer tf = tff.newTransformer();

            // 设置输出数据时换行
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            // 使用Transformer的transform()方法将DOM树转换成XML
//            tf.transform(new DOMSource(document), new StreamResult(dest));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            tf.transform(new DOMSource(document), new StreamResult(bos));
            ByteArrayInputStream swapStream = new ByteArrayInputStream(bos.toByteArray());
            
            boolean status = FtpUtil.uploadFile(String.valueOf(dataMap.get("template-id")) + ".xml", swapStream, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASEPATH);
            if (status) {
            	return true;
            } else {
            	return false;
            }
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (TransformerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 
	 * 创建xml文件
	 * @param ledAttr 分区属性 必填
	 * @param ledCommonset 分区通用设置 必填
	 * @param ledFile 分区文件 必填
	 * @param ledMsg 分区消息 必填
	 * @param ledWeather 天气 如不需要填null
	 * @param createStatus 是否创建显示屏切换时间、通用设置、天气标签（true:创建，false：不创建）
	 * @param dataMap 数据
	 * @param ftpAddress ftp地址
	 * @param ftpPort ftp端口
	 * @param ftpName ftp帐号
	 * @param ftpPassWord ftp密码
	 * @param ftpBasePath ftp路径
	 * @return
	 */
	 public static Boolean makeXml(String ledAttr, String ledCommonset, String ledFile,  String ledMsg, String ledWeather, boolean createStatus,
			 	Map<String, Object> dataMap,String ftpAddress, int ftpPort,
	            String ftpName, String ftpPassWord, String ftpBasePath) {
	        FTP_ADDRESS = ftpAddress;
	        FTP_PORT = ftpPort;
	        FTP_USERNAME = ftpName;
	        FTP_PASSWORD = ftpPassWord;
	        FTP_BASEPATH = ftpBasePath;
	        createXmlByDom(ledAttr, ledCommonset, ledFile, ledMsg, ledWeather, createStatus, dataMap);
	        return true;
	    }

}
