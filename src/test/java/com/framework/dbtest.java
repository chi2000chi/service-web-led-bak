package com.framework;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.framework.jt808.util.HexStringUtils;
import com.framework.jt808.util.JT808ProtocolUtils;
import com.framework.jt808.util.MsgDecoder;
import com.framework.jt808.vo.PackageData;
import com.framework.util.GpsDistanceUtils;

import oracle.jdbc.OracleDriver;

public class dbtest {
	private final static JT808ProtocolUtils jt808ProtocolUtil = new JT808ProtocolUtils();
	private final static MsgDecoder decoder = new MsgDecoder();
	private static GpsDistanceUtils utils = new GpsDistanceUtils();
	private static double  distance = 0;
	public static void main(String[] args) {
		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			distance = utils.earthDis(Double.valueOf("126.675167"),Double.valueOf ("45.783833"),Double.valueOf("126.683384"), Double.valueOf("45.78644"));
			System.out.println(distance);
//			Driver driver = new OracleDriver();
//			DriverManager.deregisterDriver(driver);
//			connect = DriverManager.getConnection("jdbc:oracle:thin:@//10.1.30.26:1521/zdcomora", "gzfw_dzzp", "heb_dzzp");
//			// 测试connect正确与否
//			
//			statement = connect.createStatement();
//			resultSet = statement.executeQuery("SELECT MSGINFO from LED_MSGLOG WHERE 1=1 and FSSJ > TO_DATE('2018-12-28 19:50:58', 'YYYY-MM-DD HH24:MI:SS')");
//			// 第五步：处理结果集
//			int count= 0;
//			while (resultSet.next()) {
//				String MSGINFO = resultSet.getString("MSGINFO");
//				boolean flg = test(MSGINFO); 
//				if(flg)count ++;
//			}
//			System.out.println(count);
			//test("7E090802AD00000011042100C800009F75055021000A02001F01029E312CCBC9B5E7D0C2B4E52C303B322CD0F1C9FDC4CFBDD62C303B332CC8FDBACFC2B7A3A8CBE7BBAFC2B7BFDAA3A92C303B342CC8FDBACFC2B7A3A8C4FEB0B2C2B7BFDAA3A92C303B352CC8FDBACFD4B0D0A1C7F82C303B362CBDA1BFB5C2B7A3A8C8FDBACFC2B7BFDAA3A92C303B372CBDA1BFB5C7C52C303B382CCAD0CEE5D4BA2C303B392CB5E7BDCCBDD62C303B31302CC0D6D4B0BDD62C303B31312CB9E3B6ABBCD2BEDFB9E3B3A12C303B31322CB9FEC6BDC2B72C303B31332CC8CEBCD2C7C52C303B31342CD6B2CEEFD4B02C303B31352CD4B6B4F3B6BCCAD0C2CCD6DED0A1C7F8A3A8C1D6BFC6D4BAA3A92C303B31362CD6D7C1F6D2BDD4BAA3A8C1D9CAB1D5BEA3A92C303B31372CBFC6D1D0C2B7A3A8B9FEC6BDC2B7BFDAA3A92C303B31382CBFC6D1D0C2B7A3A8D5F7D2C7C2B7BFDAA3A92C303B31392CD5F7D2C7BBA8D4B0D0A1C7F82C303B32302CB4F3D6DAD0C2B3C7D0A1C7F82C303B32312CBDA1BFB5BCD2D4B0D0A1C7F82C303B32322CB1A3BDA1C2B7A3A8B2E2BBE6C2B7BFDAA3A92C303B32332CD2BDB4F3B6FED4BA2C303B32342CB9FED2BDBFC6B4F3D1A72C303B32352CB7FED7B0B3C72C303B32362CBADAC1FABDADB4F3D1A72C303B32372CB9FEC0EDB9A4B4F3D1A72C303B32382CB9FECAA6B4F3B8BDD6D02C303B32392CBACDD0CBCAAED2BBB5C0BDD62C303B33302CBACDD0CBC8FDB5C0BDD62C303B33312CBACDD0CBC2B72C303B33322CB4F3B7A2CAD0B3A12C303B33332CCAD0D6D0D2BDD2BDD4BA2C303B33342CBDA8B9FAB9ABD4B02C303B33352CBDA8B9FABDD62C303B33362CC7B0BDF8C2B72C303B33372CB9ABC2B7B4F3C7C52C303B33382CC9E7BBE1BFC6D1A7D4BA2C303B33392CB1A8D2B5B4F3CFC32C303B34302CBEADCEB3CAAEB6FEB5C0BDD62C303B34312CBAECD7A8BDD62C303B00637E");
			//System.out.println("end");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 第六步：关闭资源
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private static  boolean test(String msg) {
		byte[] bytes = HexStringUtils.toBytes(msg);
		try {
			// 接收消息时转义
			bytes = jt808ProtocolUtil.doEscape4Receive(bytes, 1, bytes.length);
			byte[] data = bytes;
			// 解析消息
			int checkSumInPkg = data[data.length - 1];
			int calculatedCheckSum = decoder.bitOperator.getCheckSum4JT808(data, 0, data.length - 1);
	
			if (checkSumInPkg != calculatedCheckSum) {
				System.out.println(msg);
				return true;
			}else {
				return false;
			}

			//processPackageData(pkg);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}
}
