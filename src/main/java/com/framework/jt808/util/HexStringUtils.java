package com.framework.jt808.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.framework.util.ByteUtils;

public class HexStringUtils {

	private static final char[] HEX_CHAR = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/**
	 * 方法一： byte[] to hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public synchronized static String bytesToHexFun1(byte[] bytes) {
		// 一个byte为8位，可用两个十六进制位标识
		char[] buf = new char[bytes.length * 2];
		int a = 0;
		int index = 0;
		for (byte b : bytes) { // 使用除与取余进行转换
			if (b < 0) {
				a = 256 + b;
			} else {
				a = b;
			}

			buf[index++] = HEX_CHAR[a / 16];
			buf[index++] = HEX_CHAR[a % 16];
		}

		return new String(buf);
	}

	/**
	 * 方法二： byte[] to hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public synchronized static String bytesToHexFun2(byte[] bytes) {
		char[] buf = new char[bytes.length * 2];
		int index = 0;
		for (byte b : bytes) { // 利用位运算进行转换，可以看作方法一的变种
			buf[index++] = HEX_CHAR[b >>> 4 & 0xf];
			buf[index++] = HEX_CHAR[b & 0xf];
		}

		return new String(buf);
	}

	/**
	 * 方法三： byte[] to hex string
	 * 
	 * @param bytes
	 * @return
	 */
	public synchronized static String bytesToHexFun3(byte[] bytes) {
		StringBuilder buf = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) { // 使用String的format方法进行转换
			buf.append(String.format("%02x", new Integer(b & 0xff)));
		}

		return buf.toString();
	}

	/**
	 * 将16进制字符串转换为byte[]
	 * 
	 * @param str
	 * @return
	 */
	public synchronized static byte[] toBytes(String str) {
		if (str == null || str.trim().equals("")) {
			return new byte[0];
		}

		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			String subStr = str.substring(i * 2, i * 2 + 2);// 按照字符串 0-2  2-4  4-6 两位一截取
			bytes[i] = (byte) Integer.parseInt(subStr, 16);//截取的 两位字符 转成16进制 int
		}

		return bytes;
	}
	
	/*public static void main(String[] args) {
		byte[]  bb=toBytes("7E0603000E00000011078500010203040501000100010001000100987E");
		System.out.println(bb);
	}*/
	

	 //public static void main(String[] args) throws Exception {
	// byte[] a = {126, 9, 8, 1, 63, 0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 62, 63, 5, 80,
	// 32, 48, 10, 2, 0, 21, 122, 49, 44, -77, -81, -46, -69, -42, -48, 59, 50, 44,
	// -71, -85, -79, -11, -62, -73, 59, 51, 44, -49, -29, -79, -11, -48, -95, -47,
	// -89, 59, 52, 44, -70, -20, -58, -20, -76, -13, -67, -42, -93, -88, -71, -85,
	// -79, -11, -62, -73, -65, -38, -93, -87, 59, 53, 44, -70, -31, -75, -64, -67,
	// -42, 59, 54, 44, -50, -9, -49, -29, -73, -69, 59, 55, 44, -54, -95, -46, -67,
	// -44, -70, 59, 56, 44, -49, -29, -79, -11, -62, -73, 59, 57, 44, -47, -57,
	// -62, -23, -77, -89, 59, 49, 48, 44, -70, -51, -58, -67, -57, -59, 59, 49, 49,
	// 44, -54, -95, -42, -48, -46, -67, -46, -87, -76, -13, -47, -89, 59, 49, 50,
	// 44, -56, -3, -76, -13, -74, -81, -63, -90, -62, -73, 59, 49, 51, 44, -63,
	// -42, -46, -75, -76, -13, -47, -89, 59, 49, 52, 44, -63, -42, -46, -75, -41,
	// -36, -46, -67, -44, -70, 59, 49, 53, 44, -63, -42, -48, -53, -62, -73, 59,
	// 49, 54, 44, -71, -2, -54, -90, -76, -13, -93, -88, -60, -49, -48, -93, -57,
	// -8, -93, -87, 59, 49, 55, 44, -70, -51, -48, -53, -62, -73, 59, 49, 56, 44,
	// -76, -13, -73, -94, -54, -48, -77, -95, 59, 49, 57, 44, -65, -75, -80, -78,
	// -62, -73, 59, 50, 48, 44, -71, -53, -49, -25, 59, 50, 49, 44, -54, -82, -53,
	// -60, -42, -48, -47, -89, 59, 50, 50, 44, -45, -83, -79, -10, -48, -95, -57,
	// -8, 59, 50, 51, 44, -63, -6, -67, -83, -65, -51, -77, -75, -77, -89, 59, 50,
	// 52, 44, -54, -48, -44, -53, -54, -28, -41, -36, -71, -85, -53, -66, 59, 50,
	// 53, 44, -58, -5, -77, -75, -77, -35, -62, -42, -77, -89, 59, 50, 54, 44, -59,
	// -87, -69, -6, -48, -34, -59, -28, -71, -85, -53, -66, 59, 50, 55, 44, -60,
	// -62, -53, -71, -63, -42, -48, -95, -57, -8, 59, 50, 56, 44, -71, -6, -68,
	// -54, -58, -5, -77, -75, -77, -57, 59, 50, 57, 44, -70, -93, -60, -2, -58,
	// -92, -72, -17, -77, -57, -74, -85, -61, -59, 59, 51, 48, 44, -70, -93, -60,
	// -2, -58, -92, -72, -17, -77, -57, 59, 0, 69, 126};

	// System.out.println("长度：" + new String(a));
	// System.out.println("原长度：" +
	// "ftp://admin:admin@111.42.74.35:21/ledUpLoad/111.txt".length());
	// byte[] bytes =
	// "ftp://admin:admin@111.42.74.35:21/ledUpLoad/111.txt".getBytes("gbk");
	// System.out.println("长度：" + bytes.length);
	// System.out.println("字节数组为：" + Arrays.toString(bytes));
	//
	//
	//
	// System.out.println("==================================");

	// String hexStr =
	// "7E09080187000001010101000300003EEA055020300A0200157A312CB3AFD2BBD6D03B322CB9ABB1F5C2B73B332CCFE3B1F5D0A1D1A73B342CBAECC6ECB4F3BDD6A3A8B9ABB1F5C2B7BFDAA3A93B352CBAE1B5C0BDD63B362CCEF7CFE3B7BB3B372CCAA1D2BDD4BA3B382CCFE3B1F5C2B73B392CD1C7C2E9B3A73B31302CBACDC6BDC7C53B31312CCAA1D6D0D2BDD2A9B4F3D1A73B31322CC8FDB4F3B6AFC1A6C2B73B31332CC1D6D2B5B4F3D1A73B31342CC1D6D2B5D7DCD2BDD4BA3B31352CC1D6D0CBC2B73B31362CB9FECAA6B4F3A3A8C4CFD0A3C7F8A3A93B31372CBACDD0CBC2B73B31382CB4F3B7A2CAD0B3A13B31392CBFB5B0B2C2B73B32302CB9CBCFE73B32312CCAAECBC4D6D0D1A73B32322CD3ADB1F6D0A1C7F83B32332CC1FABDADBFCDB3B5B3A73B32342CCAD0D4CBCAE4D7DCB9ABCBBE3B32352CC6FBB3B5B3DDC2D6B3A73B32362CC5A9BBFAD0DEC5E4B9ABCBBE3B32372CC4C2CBB9C1D6D0A1C7F83B32382CB9FABCCAC6FBB3B5B3C73B32392CBAA3C4FEC6A4B8EFB3C7B6ABC3C53B33302CBAA3C4FEC6A4B8EFB3C73B00457E";
	//// System.out.println("转换后的字节数组：" + Arrays.toString(toBytes(hexStr)));
	// String str = hexStr2Str(hexStr);
	// System.out.println("转换后的字节数组：" + Arrays.toString(str.getBytes("GBK")));
	// String str ="000001010101";
	// System.out.println("转换后的字节数组：" + toBytes(str).length);
//		 byte[] a = {0, 0, 1, 77};
//		 String str = bytesToHexFun1(a);
//		 System.out.println("转换后的字节数组：" + str);
	//}

	/**
	 * 16进制字符串转换为字符串
	 *
	 * @param charsetName
	 *            用于编码 String 的 Charset
	 * @throws UnsupportedEncodingException
	 */
	public synchronized static String hexStr2Str(String s) throws UnsupportedEncodingException {
		if (s == null || s.equals("")) {
			return null;
		}
		s = s.replace(" ", "");
		byte[] baKeyword = new byte[s.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			s = new String(baKeyword, "GBK");
			new String();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return s;
	}

	public synchronized static String ipToLong(String ipString) {
		if (StringUtils.isEmpty(ipString)) {
			return null;
		}
		String[] ip = ipString.split("\\.");
		StringBuffer sb = new StringBuffer();

		for (String str : ip) {
			String data = Integer.toHexString(Integer.parseInt(str));
			sb.append(data.length() == 1 ? "0" + data : data);
		}
		return sb.toString();
	}

	public synchronized static Object[] splitAry(byte[] ary, int subSize) {
		int count = ary.length % subSize == 0 ? ary.length / subSize : ary.length / subSize + 1;
		List<List<Byte>> subAryList = new ArrayList<List<Byte>>();

		for (int i = 0; i < count; i++) {
			int index = i * subSize;
			List<Byte> list = new ArrayList<Byte>();
			int j = 0;
			while (j < subSize && index < ary.length) {
				list.add(ary[index++]);
				j++;
			}
			subAryList.add(list);
		}
		Object[] subAry = new Object[subAryList.size()];
		for (int i = 0; i < subAryList.size(); i++) {
			List<Byte> subList = subAryList.get(i);
			byte[] subAryItem = new byte[subList.size()];
			for (int j = 0; j < subList.size(); j++) {
				subAryItem[j] = subList.get(j);
			}
			subAry[i] = subAryItem;
		}
		return subAry;
	}
}