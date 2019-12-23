package com.framework.jt808.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.vo.PackageData;
import com.framework.jt808.vo.resp.MsgBody0901;
import com.framework.jt808.vo.resp.MsgBody0902;
import com.framework.jt808.vo.resp.MsgBody0904;
import com.framework.jt808.vo.resp.MsgBody0905;
import com.framework.jt808.vo.resp.MsgBody0906;
import com.framework.jt808.vo.resp.MsgBody0907;
import com.framework.jt808.vo.resp.MsgBody0908;
import com.framework.jt808.vo.resp.MsgBody0909;
import com.framework.util.ByteUtils;

@Component
public class MsgEncoder {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private BitOperator bitOperator;
	@Autowired
	private JT808ProtocolUtils jt808ProtocolUtils;
	@Autowired
	private BCDOperater bCD8421Operater;
	Lock lock = new ReentrantLock(true);

	private MsgEncoder() {
		logger.info("----------MsgEncoder Initializing ----------------");
	}
	
	public byte[] encode0901Resp(PackageData req, MsgBody0901 respMsgBody, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo2Bytes(respMsgBody.getReplyFlowId()), // 应答流水号
				bitOperator.integerTo2Bytes(respMsgBody.getReplyId()), // 应答ID
				new byte[] { respMsgBody.getReplyCode() }// 结果
		));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x0901, flowId, msgBody, req.getMsgHeader().getLedNo());
		return generateBytes;
	}

	public byte[] encode0902Resp(PackageData packageData, MsgBody0902 respMsgBody, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_010A),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_010A),
				bCD8421Operater.string2Bcd((String) respMsgBody.paraMemo_010A),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_0101),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_0101),
				bCD8421Operater.string2Bcd((String) respMsgBody.paraMemo_0101),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_010B),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_010B),
				bCD8421Operater.string2Bcd((String) respMsgBody.paraMemo_010B),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_0108),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_0108),
				bCD8421Operater.string2Bcd((String) respMsgBody.paraMemo_0108),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_0103),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_0103),
				bitOperator.integerTo2Bytes(Integer.parseInt(respMsgBody.paraMemo_0103.toString())),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_0200),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_0200),
				bCD8421Operater.string2Bcd((String) respMsgBody.paraMemo_0200),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_0207),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_0207),
				bCD8421Operater.string2Bcd((String) respMsgBody.paraMemo_0207),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_0208),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_0208),
				bCD8421Operater.string2Bcd((String) respMsgBody.paraMemo_0208),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_0209),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_0209),
				respMsgBody.paraMemo_0209.toString().getBytes(JT808Consts.string_encoding),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_020A),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_020A),
				respMsgBody.paraMemo_020A.toString().getBytes(JT808Consts.string_encoding),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_020B),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_020B),
				respMsgBody.paraMemo_020B.toString().getBytes(JT808Consts.string_encoding),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_020C),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_020C),
				respMsgBody.paraMemo_020C.toString().getBytes(JT808Consts.string_encoding),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_020D),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_020D),
				bitOperator.integerTo1Bytes(Integer.parseInt(respMsgBody.paraMemo_020D.toString())),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_020E),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_020E),
				bitOperator.integerTo1Bytes(Integer.parseInt(respMsgBody.paraMemo_020E.toString())),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_020F),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_020F),
				respMsgBody.paraMemo_020F.toString().getBytes(JT808Consts.string_encoding),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_0210),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_0210),
				bitOperator.integerTo1Bytes(Integer.parseInt(respMsgBody.paraMemo_0210.toString())),

				bitOperator.integerTo2Bytes(respMsgBody.paraIdInt_0211),
				bitOperator.integerTo2Bytes(respMsgBody.paraLength_0211),
				bitOperator.integerTo1Bytes(Integer.parseInt(respMsgBody.paraMemo_0211.toString()))

		));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x0902, flowId, msgBody, packageData.getMsgHeader().getLedNo());
		return generateBytes;
	}
	
	public byte[] encode0903Resp(PackageData req, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo2Bytes(JT808Consts.para_search_0x0903)));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x0903, flowId, msgBody, req.getMsgHeader().getLedNo());
		return generateBytes;
	}
	
	public byte[] encode0904Resp(PackageData req, MsgBody0904 respMsgBody, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo4Bytes(respMsgBody.getLineNo()), // 线路编号
				bitOperator.integerTo1Bytes(respMsgBody.getMsgType()), // 线路公告类别
				bitOperator.integerTo1Bytes(respMsgBody.getFullScreenFlg())// 公告全屏显示
		));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x0904, flowId, msgBody, req.getMsgHeader().getLedNo());
		return generateBytes;
	}
	
	public byte[] encode0905Resp(PackageData req, MsgBody0905 respMsgBody, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo4Bytes(respMsgBody.getTemplateNo()), // 模板编码
				bitOperator.integerTo1Bytes(respMsgBody.getWjjym()), // 文件校验码
				bitOperator.integerTo1Bytes(respMsgBody.getFileLength()), // 配置xml文件信息长度
				respMsgBody.getFilePath().getBytes(JT808Consts.string_encoding), // 配置xml文件信息
				bCD8421Operater.string2Bcd(JT808Consts.string_end)
		));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x0905, flowId, msgBody, req.getMsgHeader().getLedNo());
		return generateBytes;
	}

	public byte[] encode0906Resp(String ledNo, MsgBody0906 respMsgBody, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(bitOperator.integerTo4Bytes(respMsgBody.getLineNo()),
				bitOperator.integerTo1Bytes(respMsgBody.getLineNameLength()),
				respMsgBody.getLineName().getBytes(JT808Consts.string_encoding),
				bCD8421Operater.string2Bcd(JT808Consts.string_end),

				bitOperator.integerTo1Bytes(respMsgBody.getBusCount()),

				bitOperator.integerTo2Bytes(respMsgBody.getArriveDistance1()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveTime1()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveStationCount1()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveYJD1()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveBusState1()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveBusNo1Length()),
				respMsgBody.getArriveBusNo1().getBytes(JT808Consts.string_encoding),
				bCD8421Operater.string2Bcd(JT808Consts.string_end),

				bitOperator.integerTo2Bytes(respMsgBody.getArriveDistance2()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveTime2()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveStationCount2()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveYJD2()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveBusState2()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveBusNo2Length()),
				respMsgBody.getArriveBusNo2().getBytes(JT808Consts.string_encoding),
				bCD8421Operater.string2Bcd(JT808Consts.string_end),

				bitOperator.integerTo2Bytes(respMsgBody.getArriveDistance3()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveTime3()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveStationCount3()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveYJD3()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveBusState3()),
				bitOperator.integerTo1Bytes(respMsgBody.getArriveBusNo3Length()),
				respMsgBody.getArriveBusNo3().getBytes(JT808Consts.string_encoding),
				bCD8421Operater.string2Bcd(JT808Consts.string_end),

				bitOperator.integerTo1Bytes(respMsgBody.getLastBusState()),

				bCD8421Operater.string2Bcd(respMsgBody.getNextFCSJ()),
				bitOperator.integerTo1Bytes(respMsgBody.getBusStateLength()),
				respMsgBody.getBusState().getBytes(JT808Consts.string_encoding),
				bCD8421Operater.string2Bcd(JT808Consts.string_end)));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x0906, flowId, msgBody, ledNo);
		return generateBytes;
	}
	
	public byte[] encode0907Resp(PackageData req, MsgBody0907 respMsgBody, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo1Bytes(respMsgBody.getLineInfoLength()), // 线路列表长度
				respMsgBody.getLineInfoList().getBytes(JT808Consts.string_encoding), // 线路列表
				bCD8421Operater.string2Bcd(JT808Consts.string_end)));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x0907, flowId, msgBody, req.getMsgHeader().getLedNo());
		return generateBytes;
	}

	public byte[] encode0908Resp(PackageData req, MsgBody0908 respMsgBody, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo4Bytes(respMsgBody.getLineNo()), // 线路编号
				bCD8421Operater.string2Bcd(respMsgBody.getStartTime()), // 首班时间
				bCD8421Operater.string2Bcd(respMsgBody.getEndTime()), // 末班时间
				bitOperator.integerTo1Bytes(respMsgBody.getFcjg()), // 发车间隔
				bitOperator.integerTo1Bytes(respMsgBody.getPj()), // 票价
				bitOperator.integerTo1Bytes(respMsgBody.getDirection()), // 上下行标识
				bitOperator.integerTo1Bytes(respMsgBody.getCurrentStationNo()), // 当前站号
				bitOperator.integerTo1Bytes(respMsgBody.getLineDirection()), // 线路方向
				bitOperator.integerTo2Bytes(respMsgBody.getStationLength()), // 站点列表长度
				respMsgBody.getStationInfoList().getBytes(JT808Consts.string_encoding), // 站点列表
				bCD8421Operater.string2Bcd(JT808Consts.string_end)));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x0908, flowId, msgBody, req.getMsgHeader().getLedNo());
		return generateBytes;
	}
	
	public byte[] encode0909Resp(PackageData req, MsgBody0909 respMsgBody, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				bitOperator.integerTo4Bytes(respMsgBody.getLineNo()), // 线路编号
				bitOperator.integerTo2Bytes(respMsgBody.getLkLength()), // 路况信息长度
				respMsgBody.getLkList().getBytes(JT808Consts.string_encoding), // 路况信息列表
				bCD8421Operater.string2Bcd(JT808Consts.string_end)));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x0909, flowId, msgBody, req.getMsgHeader().getLedNo());
		return generateBytes;
	}

	public byte[] encode090AResp(String ledNo, String systemTime, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(//
				systemTime.getBytes(JT808Consts.string_encoding), // 站点列表
				bCD8421Operater.string2Bcd(JT808Consts.string_end)));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x090A, flowId, msgBody, ledNo);
		return generateBytes;
	}

	public byte[] encode090BResp(String ledNo, String safeCodeStr, int flowId) throws Exception {
		// 消息体字节数组
		byte[] msgBody = null;
		msgBody = this.bitOperator.concatAll(Arrays.asList(
				bitOperator.integerTo1Bytes(safeCodeStr.getBytes(JT808Consts.string_encoding).length + 1),
				safeCodeStr.getBytes(JT808Consts.string_encoding), bCD8421Operater.string2Bcd(JT808Consts.string_end)));
		byte[] generateBytes = generateBytes(JT808Consts.msg_id_0x090B, flowId, msgBody, ledNo);
		return generateBytes;
	}

	private  synchronized byte[] generateBytes(int msgId, int flowId, byte[] msgBody, String ledNo) throws Exception {
		// 消息头
		int msgBodyProps = this.jt808ProtocolUtils.generateMsgBodyProps(msgBody.length, 0b000, false, 0);
		byte[] msgHeader = this.jt808ProtocolUtils.generateMsgHeader(ledNo, msgId, msgBody, msgBodyProps, flowId);
		byte[] headerAndBody = this.bitOperator.concatAll(msgHeader, msgBody);
		// 校验码
		int checkSum = this.bitOperator.getCheckSum4JT808(headerAndBody, 0, headerAndBody.length);
		// 连接并且转义
//		byte[] noEscapedBytes = this.bitOperator.concatAll(Arrays.asList(//
//				new byte[] { JT808Consts.pkg_delimiter }, // 0x7e
//				headerAndBody, // 消息头+ 消息体
//				bitOperator.integerTo1Bytes(checkSum), // 校验码
//				new byte[] { JT808Consts.pkg_delimiter }// 0x7e
//		));
		// 转义
		//return jt808ProtocolUtils.doEscape4Send(noEscapedBytes, 1, noEscapedBytes.length - 1);
			
		// 连接并且转义
		byte[] noEscapedBytes = this.bitOperator.concatAll(Arrays.asList(
				headerAndBody, 
				bitOperator.integerTo1Bytes(checkSum))); 
		//转义
		byte[] escapedBytes  = jt808ProtocolUtils.doEscape4Send(noEscapedBytes, 0, noEscapedBytes.length);

		return this.bitOperator.concatAll(Arrays.asList(
				new byte[] { JT808Consts.pkg_delimiter }, 
				escapedBytes,
				new byte[] { JT808Consts.pkg_delimiter }));
	}
}
