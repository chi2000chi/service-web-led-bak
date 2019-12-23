package com.framework.jt808.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.vo.PackageData;
import com.framework.jt808.vo.PackageData.MsgHeader;
import com.framework.jt808.vo.resp.MsgBody0605;
import com.framework.webClient.service.ICommon808Service;
import com.framework.webClient.util.CommonUtils;



@Component
public class MsgDecoder {

	@Autowired
	public BitOperator bitOperator;
	@Autowired
	private BCDOperater bcd8421Operater;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired(required=true)
	private ICommon808Service common808Service;
	


	public PackageData bytes2PackageData(byte[] data) throws Exception {
		PackageData ret = new PackageData();

		// 1. 16byte 或 12byte 消息头
		MsgHeader msgHeader = this.parseMsgHeaderFromBytes(data);
		ret.setMsgHeader(msgHeader);

		int msgBodyByteStartIndex = 12;
		// 2. 消息体
		// 有子包信息,消息体起始字节后移四个字节:消息包总数(word(16))+包序号(word(16))
		if (msgHeader.isHasSubPackage()) {
			msgBodyByteStartIndex = 16;
		}

		byte[] tmp = new byte[msgHeader.getMsgBodyLength()];
		System.arraycopy(data, msgBodyByteStartIndex, tmp, 0, tmp.length);
		ret.setMsgBodyBytes(tmp);

		// 3. 去掉分隔符之后，最后一位就是校验码
		int checkSumInPkg = data[data.length - 1];
		int calculatedCheckSum = this.bitOperator.getCheckSum4JT808(data, 0, data.length - 1);
		ret.setCheckSum(checkSumInPkg);
		if (checkSumInPkg != calculatedCheckSum) {
			log.warn("检验码不一致,msgid:{},pkg:{},calculated:{}", msgHeader.getMsgId(), checkSumInPkg, calculatedCheckSum);
		}
		return ret;
	}

	private MsgHeader parseMsgHeaderFromBytes(byte[] data) {
		MsgHeader msgHeader = new MsgHeader();

		// 1. 消息ID word(16)
		msgHeader.setMsgId(this.parseIntFromBytes(data, 0, 2));

		// 2. 消息体属性 word(16)=================>
		int msgBodyProps = this.parseIntFromBytes(data, 2, 2);
		msgHeader.setMsgBodyPropsField(msgBodyProps);
		// [ 0-9 ] 0000,0011,1111,1111(3FF)(消息体长度)
		msgHeader.setMsgBodyLength(msgBodyProps & 0x3ff);
		// [10-12] 0001,1100,0000,0000(1C00)(加密类型)
		msgHeader.setEncryptionType((msgBodyProps & 0x1c00) >> 10);
		// [ 13_ ] 0010,0000,0000,0000(2000)(是否有子包)
		msgHeader.setHasSubPackage(((msgBodyProps & 0x2000) >> 13) == 1);
		// [14-15] 1100,0000,0000,0000(C000)(保留位)
		msgHeader.setReservedBit(((msgBodyProps & 0xc000) >> 14) + "");
		// 消息体属性 word(16)<=================

		// 3. 电子站牌编号 bcd[6]
		msgHeader.setLedNo((this.parseBcdStringFromBytes(data, 4, 6)));

		// 4. 消息流水号 word(16) 按发送顺序从 0 开始循环累加
		msgHeader.setFlowId(this.parseIntFromBytes(data, 10, 2));

		// 5. 消息包封装项
		// 有子包信息
		if (msgHeader.isHasSubPackage()) {
			// 消息包封装项字段
			msgHeader.setPackageInfoField(this.parseIntFromBytes(data, 12, 4));
			// byte[0-1] 消息包总数(word(16))
			msgHeader.setTotalSubPackage(this.parseIntFromBytes(data, 12, 2));

			// byte[2-3] 包序号(word(16)) 从 1 开始
			msgHeader.setSubPackageSeq(this.parseIntFromBytes(data, 12, 2));
		}
		return msgHeader;
	}



	protected String parseStringFromBytes(byte[] data, int startIndex, int lenth) {
		return this.parseStringFromBytes(data, startIndex, lenth, null);
	}

	private String parseStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
		try {
			byte[] tmp = new byte[lenth];
			System.arraycopy(data, startIndex, tmp, 0, lenth);
			return new String(tmp, JT808Consts.string_charset);
		} catch (Exception e) {
			log.error("解析字符串出错:{}", e.getMessage());
			e.printStackTrace();
			return defaultVal;
		}
	}

	private String parseBcdStringFromBytes(byte[] data, int startIndex, int lenth) {
		return this.parseBcdStringFromBytes(data, startIndex, lenth, null);
	}

	private String parseBcdStringFromBytes(byte[] data, int startIndex, int lenth, String defaultVal) {
		try {
			byte[] tmp = new byte[lenth];
			System.arraycopy(data, startIndex, tmp, 0, lenth);
			return this.bcd8421Operater.bcd2String(tmp);
		} catch (Exception e) {
			log.error("解析BCD(8421码)出错:{}", e.getMessage());
			e.printStackTrace();
			return defaultVal;
		}
	}

	private int parseIntFromBytes(byte[] data, int startIndex, int length) {
		return this.parseIntFromBytes(data, startIndex, length, 0);
	}

	private int parseIntFromBytes(byte[] data, int startIndex, int length, int defaultVal) {
		try {
			// 字节数大于4,从起始索引开始向后处理4个字节,其余超出部分丢弃
			final int len = length > 4 ? 4 : length;
			byte[] tmp = new byte[len];
			System.arraycopy(data, startIndex, tmp, 0, len);
			return bitOperator.byteToInteger(tmp);
		} catch (Exception e) {
			log.error("解析整数出错:{}", e.getMessage());
			e.printStackTrace();
			return defaultVal;
		}
	}

	
	@SuppressWarnings("unused")
	private float parseFloatFromBytes(byte[] data, int startIndex, int length) {
		return this.parseFloatFromBytes(data, startIndex, length, 0f);
	}

	private float parseFloatFromBytes(byte[] data, int startIndex, int length, float defaultVal) {
		try {
			// 字节数大于4,从起始索引开始向后处理4个字节,其余超出部分丢弃
			final int len = length > 4 ? 4 : length;
			byte[] tmp = new byte[len];
			System.arraycopy(data, startIndex, tmp, 0, len);
			return bitOperator.byte2Float(tmp);
		} catch (Exception e) {
			log.error("解析浮点数出错:{}", e.getMessage());
			e.printStackTrace();
			return defaultVal;
		}
	}

	/**
	 * 解析心跳包
	 * @param data
	 * @return
	 */
	public HashMap<String, Object> decodeHeartMsg(byte[] data) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("CPUWD", this.parseIntFromBytes(data, 0, 1));
		dataMap.put("BZWD", this.parseIntFromBytes(data, 1, 1));
		dataMap.put("YJWDCJZ", this.parseIntFromBytes(data, 2, 1));
		dataMap.put("LEDWDCJZ", this.parseIntFromBytes(data, 3, 1));
		dataMap.put("ZDCGQCJZ", this.parseIntFromBytes(data, 4, 1));
		dataMap.put("MJCGQ", this.parseIntFromBytes(data, 5, 1));
		dataMap.put("SWCGQ", this.parseIntFromBytes(data, 6, 1));
		dataMap.put("JWMKZT", this.parseIntFromBytes(data, 7, 1));
		dataMap.put("YJKQZT", this.parseIntFromBytes(data, 8, 1));
		dataMap.put("LEDKQZT", this.parseIntFromBytes(data, 9, 1));
		dataMap.put("FSKQZT", this.parseIntFromBytes(data, 10, 1));
		dataMap.put("ZMKQZT", this.parseIntFromBytes(data, 11, 1));
		dataMap.put("DVRKQZT", this.parseIntFromBytes(data, 12, 1));
		dataMap.put("GLYQKQZT", this.parseIntFromBytes(data, 13, 1));
		return dataMap;
	}
	
	/**
	 * 解析通用返回消息
	 * @param data
	 * @return
	 */
	public HashMap<String, Object> decodeCommonBackMsg(byte[] data) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("FLOWID", this.parseIntFromBytes(data, 0, 2));
		dataMap.put("MSGID", this.parseIntFromBytes(data, 2, 2));
		dataMap.put("RESULT", this.parseIntFromBytes(data, 4, 1));
		return dataMap;
	}
	
	/**
	 * 解析通用返回消息
	 * @param data
	 * @return
	 */
	public HashMap<String, Object> decodeCommonBackDetailMsg(byte[] data) {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("FLOWID", this.parseIntFromBytes(data, 0, 2));
		dataMap.put("MSGID", this.parseIntFromBytes(data, 2, 2));
		dataMap.put("RESULT", this.parseIntFromBytes(data, 4, 1));
		if(dataMap.get("RESULT") != null) {
			if("0".equals(dataMap.get("RESULT").toString())) {
				dataMap.put("ERRORMSG", "成功确认");
			}else {
				int errorMsgLength = this.parseIntFromBytes(data, 5, 1);
				dataMap.put("ERRORMSG", this.parseStringFromBytes(data, 6, errorMsgLength-1));	
			}	
		}
		return dataMap;
	}
	
	/**
	 * 解析参数信息
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	public HashMap<String, Object> decodeParaMsg(byte[] data) throws Exception {
		List<MsgBody0605> dataList  = new LinkedList<MsgBody0605>();
		byte[] tmp  = null;
		MsgBody0605 data_0605 = null;
		int count  = data.length;
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		for(int i = 0 ;i < count;) {		
			tmp = new byte[JT808Consts.msg_id_length];
			System.arraycopy(data, i, tmp, 0, JT808Consts.msg_id_length);
			String msgIdStr = HexStringUtils.bytesToHexFun1(tmp);
			int msgIdInt = bitOperator.byteToInteger(tmp);	
			data_0605 = new MsgBody0605();
			data_0605.setParaIdStr(msgIdStr);
			data_0605.setParaIdInt(msgIdInt);
			i = i+JT808Consts.msg_id_length;
			
			tmp = new byte[JT808Consts.msg_length];
			System.arraycopy(data, i, tmp, 0, JT808Consts.msg_length);
			data_0605.setParaLength(bitOperator.byteToInteger(tmp));
			i = i+JT808Consts.msg_length_length;
			
			switch (msgIdInt) {
				case JT808Consts.para_id_0x010A:
				case JT808Consts.para_id_0x0101:
				case JT808Consts.para_id_0x010B:
				case JT808Consts.para_id_0x0108:
					tmp = new byte[data_0605.getParaLength()];
					System.arraycopy(data, i, tmp, 0, data_0605.getParaLength());
					String ip = bitOperator.getStringIPValue(tmp);
					if(msgIdInt == JT808Consts.para_id_0x010A) {
						dataMap.put("BDSBIPDZ", ip);
					}
					if(msgIdInt == JT808Consts.para_id_0x0101) {
						dataMap.put("TCPYCFWQIPDZ", ip);
					}
					if(msgIdInt == JT808Consts.para_id_0x010B) {
						dataMap.put("ZWYM", ip);
					}
					if(msgIdInt == JT808Consts.para_id_0x0108) {
						dataMap.put("WG", ip);
					}					
					break;
					
				case JT808Consts.para_id_0x0200:
				case JT808Consts.para_id_0x0207:
				case JT808Consts.para_id_0x0208:
					tmp = new byte[data_0605.getParaLength()];
					System.arraycopy(data, i, tmp, 0, data_0605.getParaLength());
					String value = this.parseBcdStringFromBytes(tmp,0,data_0605.getParaLength());
					if(msgIdInt == JT808Consts.para_id_0x0200) {
						dataMap.put("LEDID", value);
					}
					if(msgIdInt == JT808Consts.para_id_0x0207) {
						dataMap.put("SBYXKSRQ", CommonUtils.strToDateFormat(value.substring(0, 8)));
						dataMap.put("SBYXJZRQ", CommonUtils.strToDateFormat(value.substring(8, 16)));
					}
					if(msgIdInt == JT808Consts.para_id_0x0208) {
						dataMap.put("XHSJDKSSJ", CommonUtils.strToTimeFormat(value.substring(0, 4)));
						dataMap.put("XHSJDJZSJ", CommonUtils.strToTimeFormat(value.substring(4, 8)));
					}
					break;
					
				case JT808Consts.para_id_0x0209:
				case JT808Consts.para_id_0x020A:
				case JT808Consts.para_id_0x020B:
				case JT808Consts.para_id_0x020C:
				case JT808Consts.para_id_0x020F:
					tmp = new byte[data_0605.getParaLength()];
					System.arraycopy(data, i, tmp, 0, data_0605.getParaLength());
					String[] strAry = new String(tmp).split(",");
					if(msgIdInt == JT808Consts.para_id_0x0209) {
						dataMap.put("SBWDFWKS", strAry[0]);
						dataMap.put("SBWDFWJZ", strAry[1]);
					
					}
					if(msgIdInt == JT808Consts.para_id_0x020A) {
						dataMap.put("WBFSKS", strAry[0]);
						dataMap.put("WBFSJZ", strAry[1]);
					}
					if(msgIdInt == JT808Consts.para_id_0x020B) {
						dataMap.put("HLFSKS", strAry[0]);
						dataMap.put("HLFSJZ", strAry[1]);
					}
					if(msgIdInt == JT808Consts.para_id_0x020C) {
						dataMap.put("JWMKKS", strAry[0]);
						dataMap.put("JWMKJZ", strAry[1]);
					}
					if(msgIdInt == JT808Consts.para_id_0x020F) {
						dataMap.put("SBGLBHKS", strAry[0]);
						dataMap.put("SBGLBHJZ", strAry[1]);
					}
					break;
				case JT808Consts.para_id_0x0103:
				case JT808Consts.para_id_0x020D:
				case JT808Consts.para_id_0x020E:
				case JT808Consts.para_id_0x0210:
				case JT808Consts.para_id_0x0211:
					tmp = new byte[data_0605.getParaLength()];
					System.arraycopy(data, i, tmp, 0, data_0605.getParaLength());
					int value2 = bitOperator.byteToInteger(tmp);
					if(msgIdInt == JT808Consts.para_id_0x0103) {
						dataMap.put("YCTCPFWDK", value2);
					}
					if(msgIdInt == JT808Consts.para_id_0x020D) {
						dataMap.put("KZMS", value2);
					}
					if(msgIdInt == JT808Consts.para_id_0x020E) {
						dataMap.put("DWFW", value2);
					}
					if(msgIdInt == JT808Consts.para_id_0x0210) {
						dataMap.put("SBSJBH", value2);
					}
					if(msgIdInt == JT808Consts.para_id_0x0211) {
						dataMap.put("SBFWZQ", value2);
					}
					break;
				default:
					break;
			}
			i = i+data_0605.getParaLength();	
			dataList.add(data_0605);
		}
		common808Service.insertUploadPara(dataMap);
		dataMap.put("dataList", JSONObject.toJSONString(dataList));
		return dataMap;
	}
	
	/**
	 * 解析通用返回消息
	 * @param data
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public HashMap<String, Object> test(byte[] data) throws UnsupportedEncodingException {
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("xlbh", this.parseIntFromBytes(data, 0, 4));
		dataMap.put("sbsj", this.parseBcdStringFromBytes(data, 4, 2));
		dataMap.put("mbsj", this.parseBcdStringFromBytes(data, 6,2));
		dataMap.put("fcjg", this.parseIntFromBytes(data, 8,1));
		dataMap.put("pj", this.parseIntFromBytes(data, 9,1));
		dataMap.put("fx", this.parseIntFromBytes(data, 10,1));
		dataMap.put("zh", this.parseIntFromBytes(data, 11,1));
		dataMap.put("xlfx", this.parseIntFromBytes(data, 12,1));
		dataMap.put("xlcd", this.parseIntFromBytes(data, 13,2));
		final int len = Integer.parseInt(dataMap.get("xlcd").toString());
		byte[] tmp = new byte[len];
		System.arraycopy(data, 15, tmp, 0, len);
		dataMap.put("xx", new String (tmp,"GBK"));
		return dataMap;
		
	}
}
