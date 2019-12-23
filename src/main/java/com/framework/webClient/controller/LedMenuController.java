package com.framework.webClient.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.framework.thrift.service.gen.JcsjService;
import com.framework.thrift.service.gen.JtlSysMenuTree;
import com.framework.thrift.service.gen.JtlSysUser;
import com.framework.webClient.util.ConstantUtil;
import com.framework.webClient.util.WebDataUtil;

/**
 * 
 * 文件名 PbMenuController 
 * 描述 调度排班菜单使用类
 * 
 * @author 吉庆 
 * 创建日期 2018年7月5日
 */
@RestController
public class LedMenuController {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Value("${thrift.ip}")
	public String thriftIp;
	
	@Value("${thrift.port}")
	public String thriftPort;
	
	@Value("${commons.led.systemId}")
	private String systemId;
	
	/**
	 * 
	 * 初始化首页页面
	 * 
	 * @return 返回页面
	 */
	@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView ledmanage(HttpSession session) {


		// 这两个值的设置只需要一次，以后只需要使用最后一行代码获取即可
		WebDataUtil.setThriftIp(thriftIp);
		WebDataUtil.setThriftPort(thriftPort);
		ModelAndView model = new ModelAndView("ledmenu");

		return model;
	}

	/**
	 * 
	 * 获取用户菜单和登录信息
	 * 
	 * @return 菜单信息和登录人员
	 * @throws TException
	 */
	@GetMapping(value = "/getSystemMenuTrees")
	public Map<String, Object>  getSystemMenuTrees(HttpSession session) {

		Map<String, Object> resultMap = new HashMap<>();
		// 尚未获取Session中的用户信息
		List<JtlSysMenuTree> menuTreeList = null;
		try {
			// 获取用户信息
			JtlSysUser jtlSysUser = WebDataUtil.getSysUserInfo(session);
			// 获取用户信息，暂时使用静态值
			String code = jtlSysUser.getUserId();
			String userName = jtlSysUser.getUserName();
			// 查询菜单
			TTransport transport = null;
			transport = new TFramedTransport(new TSocket(thriftIp, Integer.valueOf(thriftPort), 200000));
			TProtocol protocol = new TBinaryProtocol(transport);
			TMultiplexedProtocol mp1 = new TMultiplexedProtocol(protocol, ConstantUtil.JCSJSERVICE_KEY);
			JcsjService.Client client1 = new JcsjService.Client(mp1);
			transport.open();
			menuTreeList = client1.getSystemMenuTree(code, systemId);
			transport.close();
			resultMap.put(ConstantUtil.COMMON_USER_NAME, userName);
			resultMap.put(ConstantUtil.DATALIST, menuTreeList);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put(ConstantUtil.COMMON_USER_NAME, ConstantUtil.NULL_STRING);
			resultMap.put(ConstantUtil.DATALIST, menuTreeList);
			log.error("getSystemMenuTrees error");
		}

		return resultMap;
	}
}
