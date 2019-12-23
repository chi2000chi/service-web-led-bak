/**  
 * @Title  FTPUtil.java
 * @Package com.framework.webClient.util
 * @author 吉庆
 * @date 2018年8月21日
 */
package com.framework.webClient.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
/**
 * 文件名  FTPUtil
 * 描述  FTP工具类
 * @auther 吉庆
 * 创建日期  2018年8月21日
 */
public class FtpUtil {

	// ftp服务器ip地址
	//old ftp
    private static String FTP_ADDRESS ="10.1.30.21";
	//new ftp
	//private static String FTP_ADDRESS ="10.1.30.208";
    // 端口号
    private static int FTP_PORT;
    // 用户名
    private static String FTP_USERNAME;
    // 密码
    private static String FTP_PASSWORD;
    // 图片路径
    private static String FTP_BASEPATH;

    private static boolean uploadFile(String originFileName, InputStream input) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("UTF-8");
        try {
            int reply;
            ftp.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器
            ftp.login(FTP_USERNAME, FTP_PASSWORD);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.makeDirectory(FTP_BASEPATH);
            ftp.changeWorkingDirectory(FTP_BASEPATH);
            ftp.storeFile(originFileName, input);
            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

    public static Boolean uploadFile(String filename, InputStream inputStream, int ftpPort,
            String ftpName, String ftpPassWord, String ftpBasePath) {
        FTP_PORT = ftpPort;
        FTP_USERNAME = ftpName;
        FTP_PASSWORD = ftpPassWord;
        FTP_BASEPATH = ftpBasePath;
        uploadFile(filename,inputStream);
        return true;
    }
	    public static String getFileContent(String filename, int ftpPort,
            String ftpName, String ftpPassWord, String ftpBasePath) {
    	
        FTP_PORT = ftpPort;
        FTP_USERNAME = ftpName;
        FTP_PASSWORD = ftpPassWord;
        FTP_BASEPATH = ftpBasePath;
        
        return getFileContent(filename);
    }

	private static String getFileContent(String filename) {
			InputStream ins = null;
			StringBuilder builder = null;
	        FTPClient ftp = new FTPClient();
	        ftp.setControlEncoding("UTF-8");
	        try {
	            int reply;
	            ftp.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器
	            ftp.login(FTP_USERNAME, FTP_PASSWORD);// 登录
	            reply = ftp.getReplyCode();
	            if (!FTPReply.isPositiveCompletion(reply)) {
	                ftp.disconnect();
	                return null;
	            }
	            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
	            ftp.makeDirectory(FTP_BASEPATH);
	            ftp.changeWorkingDirectory(FTP_BASEPATH);
	            // 从服务器上读取指定的文件
	            ftp.enterLocalPassiveMode(); 
	            ins = ftp.retrieveFileStream(filename);
	            
	            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
	            String line;
	            builder = new StringBuilder(150);
	            while ((line = reader.readLine()) != null) {
	                builder.append(line);
	            }
	            reader.close();
	            if (ins != null) {
	                ins.close();
	            }
	            // 主动调用一次getReply()把接下来的226消费掉. 这样做是可以解决这个返回null问题
	            ftp.getReply();
	            ftp.logout();
	            return builder.toString();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (ftp.isConnected()) {
	                try {
	                    ftp.disconnect();
	                } catch (IOException ioe) {
	                	ioe.printStackTrace();  
	                }
	            }
	        }
	        return null;
	}
}
