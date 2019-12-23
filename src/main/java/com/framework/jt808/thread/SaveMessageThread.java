package com.framework.jt808.thread;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.framework.jt808.common.DataCache;
import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.handler.LedMsgProcessService;
import com.framework.jt808.vo.PackageData;
import com.framework.webClient.service.ICommon808Service;
import com.framework.webClient.util.CommonFunc;

import oracle.jdbc.driver.OracleDriver;

public class SaveMessageThread implements Runnable {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	public Connection connect = null;
	public PreparedStatement pst = null;
	public static ReentrantLock dealLock = new ReentrantLock(true);
	private SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMdd");
	public static boolean runFlg = false;
	Driver driver = null;
	private String dburl= null;
	private String dbuser= null;
	private String dbpassword= null;
	public SaveMessageThread() throws SQLException, IOException {
		driver = new OracleDriver();
		DriverManager.deregisterDriver(driver);
		dburl = CommonFunc.getConfigString("spring.datasource.url");
		dbuser = CommonFunc.getConfigString("spring.datasource.username");
		dbpassword = CommonFunc.getConfigString("spring.datasource.password");
	}

	@Override
	public void run() {
		try {
			runFlg = true;
			while (true) {
				Connection connect = DriverManager.getConnection(dburl,dbuser, dbpassword);
				batchInsertData(connect);
				Thread.sleep(6 * 1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void batchInsertData(Connection conn) {
		try {
			conn.setAutoCommit(false);
			Long beginTime = System.currentTimeMillis();
			// 构造预处理statement
			PreparedStatement pst = conn.prepareStatement("INSERT INTO  /*+ append nologging */" + "LED_MSGLOG" + monthDateFormat.format(new Date())+ " (ID,FLOWID,MSGID,MSGINFO,LEDBH,FSSJ,MSGDETAIL) VALUES(RAWTOHEX (SYS_GUID()),?,?,?,?,SYSDATE,?)");
			boolean batchFlg = false;
			for (int i = 1; i <= 10000; i++) {
				Map<String, Object> paramMap = DataCache.saveMessageQueue.poll();
				if(paramMap == null) {
					if(batchFlg) {
						pst.executeBatch();
						conn.commit();
						pst.clearBatch();
					}
					break;
				}
				pst.setString(1, String.valueOf(paramMap.get("FlOWID")));
				pst.setString(2, String.valueOf(paramMap.get("MSGID")));
				pst.setString(3, String.valueOf(paramMap.get("MSGINFO")));
				pst.setString(4, String.valueOf(paramMap.get("LEDBH")));
				pst.setString(5, String.valueOf(paramMap.get("MSGDETAIL")));
				pst.addBatch();
				batchFlg = true;
				// 每1000次提交一次
				if (i % 1000 == 0) {
					pst.executeBatch();
					conn.commit();
					pst.clearBatch();
					batchFlg = false;
				}
			}
			Long endTime = System.currentTimeMillis();
			logger.info("batch execute:{}s", (endTime - beginTime) / 1000);
			pst.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
