package com.framework.webClient.util;

import javax.servlet.http.HttpSession;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import com.framework.thrift.service.gen.JcsjService;
import com.framework.thrift.service.gen.JtlSysUser;
import com.zd.bean.TAuthUserInfo;
import com.zd.util.UserUtil;

/**
 * 文件名: WebDataUtil
 * 描述: Web数据存储工具
 * 作者: 崔吉永
 * 创建日期: 2018年7月17日

 */
public class WebDataUtil {
	/**
	 * session用户key
	 */
	private static final String SYS_USER_KEY = "JTL_SYS_USER";
	/**
	 * 远程调用IP
	 */
	private static String thriftIp = "";
	/**
	 * 远程调用端口
	 */
	private static String thriftPort = "";
	public static String getThriftIp() {
		return thriftIp;
	}
	public static void setThriftIp(String thriftIp) {
		WebDataUtil.thriftIp = thriftIp;
	}
	public static String getThriftPort() {
		return thriftPort;
	}
	public static void setThriftPort(String thriftPort) {
		WebDataUtil.thriftPort = thriftPort;
	}
	/**
	 * 在session中获取用户信息，如果获取不到，则使用thrift远程调用获取并存储至session
	 * @param session
	 * @return  
	 */
	public static JtlSysUser getSysUserInfo(HttpSession session) {
		if(session.getAttribute(SYS_USER_KEY) != null) {
			return (JtlSysUser) session.getAttribute(SYS_USER_KEY);
		}else {
			// 获取用户信息
			TAuthUserInfo tAuthUserInfo = UserUtil.getCurrentUser(session);
			try {
				TTransport transport = null;
				transport = new TFramedTransport(new TSocket(thriftIp, Integer.valueOf(thriftPort), Integer.MAX_VALUE));
				TProtocol protocol = new TBinaryProtocol(transport);
				TMultiplexedProtocol mp1 = new TMultiplexedProtocol(protocol, ConstantUtil.JCSJSERVICE_KEY);
				JcsjService.Client client1 = new JcsjService.Client(mp1);
				transport.open();
				JtlSysUser jtlSysUser = client1.getSysUser(tAuthUserInfo.getUserName(), tAuthUserInfo.getLoginPassword());
				transport.close();
				session.setAttribute(SYS_USER_KEY, jtlSysUser);
				return jtlSysUser;
			} catch (TTransportException e) {
				e.printStackTrace();
			} catch (TException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
