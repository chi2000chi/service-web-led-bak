package com.framework.webClient.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


public class CommonFunc {
	public static final String _CONFIG_FILE_ = "/application.properties";
	
	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowDateone() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
		
	public static String UUID(){
		return UUID.randomUUID().toString();
	}
	
	public static String getNowDate(){
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMMdd");
		String shijian = tempDate.format(new java.util.Date());
		return shijian;
	}
	
	public static String getNowMon(){
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyyMM");
		String shijian = tempDate.format(new java.util.Date());
		return shijian;
	}
	
	public static String getNowMon2(){
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM");
		String shijian = tempDate.format(new java.util.Date());
		return shijian;
	}
	
	public static String getNowDate2(){
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd");
		String date = tempDate.format(new java.util.Date());
		return date;
	}
	
	public static String getNowDate4(){
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM");
		String date = tempDate.format(new java.util.Date());
		return date;
	}
	
	public static String getNowDate3(){
		Calendar c = Calendar.getInstance();
		c.setTime(new java.util.Date());
		c.add(Calendar.YEAR, +1);
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd");
		String date = tempDate.format(c.getTime());
		return date;
	}
	
	public static String getNowTime(){
		SimpleDateFormat tempDate = new SimpleDateFormat("HH:mm:ss");
		String shijian = tempDate.format(new java.util.Date());
		return shijian;
	}
	
	public static InputStream getFileStream(String name){
		return CommonFunc.class.getResourceAsStream(name);
	}
	
	public static String getConfigString(String key) throws IOException{
		Properties pts = new Properties();
		pts.load(getFileStream(_CONFIG_FILE_));
		return pts.getProperty(key);
	}
	
	public static String getConfigString(String key,String dv) throws IOException{
		Properties pts = new Properties();
		pts.load(getFileStream(_CONFIG_FILE_));
		return pts.getProperty(key,dv);
	}
	
	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param date
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate() {
		Date date = new Date();
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
	/**
	 * 
	 * 获取中文年月日
	 * @return
	 */
	public static String getNowDateNYR(){
		Calendar c = Calendar.getInstance();
		c.setTime(new java.util.Date());
		SimpleDateFormat tempDate = new SimpleDateFormat("yyyy年MM月dd日");
		String date = tempDate.format(c.getTime());
		return date;
	}

	
//	public static List<String> getInitSqlList() throws IOException{
//		InputStream is = getFileStream(_INIT_SQL_);
//		BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"));
//		String str = "";
//		List<String> list = new ArrayList<String>(); 
//		int i=0;
//		if(in.ready()){
//			do {
//				if(!str.equals("") && !str.startsWith("--") && str!=null){
//					if(str.endsWith(";")){
//						str = str.substring(0, str.lastIndexOf(';'));
//						list.add(str);
//					}else{
//						str +=" " + in.readLine();
//						System.out.println(str);
//						continue;
//					}
//				}
//				str = in.readLine();
//			} while (str != null);
//		}
//		in.close();
//		return list;
//	}
	
	 /**
     * 通过HttpServletRequest返回IP客户端地址
     * @param request HttpServletRequest
     * @return ip String
     * @throws Exception
     */
    public static String getIpAddr(HttpServletRequest request) throws Exception {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
	public static <T> List<T> depCopy(List<T> srcList) {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = new ObjectOutputStream(byteOut);
			out.writeObject(srcList);
 
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ObjectInputStream inStream = new ObjectInputStream(byteIn);
			List<T> destList = (List<T>) inStream.readObject();
			return destList;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
