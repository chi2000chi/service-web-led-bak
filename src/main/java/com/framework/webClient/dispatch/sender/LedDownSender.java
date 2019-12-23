package com.framework.webClient.dispatch.sender;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.framework.jt808.common.DataCache;
import com.framework.jt808.common.JT808Consts;
import com.framework.jt808.protocol.BSBCPLedDown;
import com.framework.jt808.thread.CommonMsgThread;
import com.framework.jt808.thread.SaveMessageThread;
import com.framework.jt808.thread.UpLoadParaThread;
import com.framework.jt808.util.HexStringUtils;
import com.framework.jt808.vo.PackageData;
import com.framework.protocol.DdMessage;
import com.framework.util.ByteUtils;
import com.framework.util.DateUtils;
import com.framework.webClient.service.ICommon808Service;

/***
 * 电子站牌上传数据MQ
 * 
 * @author ly
 *
 */
@Component
public class LedDownSender {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AmqpTemplate rabbitTemplate;
	@Autowired(required = true)
	private ICommon808Service common808Service;
	private BSBCPLedDown ledDown = new BSBCPLedDown();
	private SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMdd");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    @Resource
    private JdbcTemplate jdbcTemplate;
	private LedDownSender() {
		logger.info("----------LedDownSender Initializing ----------------");
	}
	
	public  synchronized void processDzzpDown(String ledNo, int flowID,int msgId,byte[] bs,String msgInfo) {

		try {
			Object[] objdata = splitAry(bs, JT808Consts.one_package_size);
			for (Object obj : objdata) {
				byte[] bytedata = (byte[]) obj;	
				ledDown.setBusNo(ByteUtils.String2Bytes(ledNo, 40));
				ledDown.setLineNo(ByteUtils.String2Bytes("", 40));
				ledDown.setDataType(JT808Consts.DATE_TYPE_LED_DOWN);
				ledDown.setLedMsgLen((byte) (bytedata.length & 0xFF));
				ledDown.setMsg_808(bytedata);
				DdMessage msg = new DdMessage(JT808Consts.BSBCP_CMD_LED_DOWN);
				msg.body = ledDown;
				byte[] commonBytes = msg.toBytes();
				String sendMsg = new String(commonBytes, "gbk");
				// 测试站牌 000000119999 下发的路况信息
				/*if(msgId ==JT808Consts.msg_id_0x0906 && "000000119999".equals(ledNo)) {
					logger.info("lkmq:"+sendMsg);
					logger.info("lkmqlength:"+sendMsg.length());
				}*/
				// 直接下发到mq 默认的exchange 上 direct 模式,改之前这么下发  以及目前 dzzp_down_data1 环境用这个来发送
				//rabbitTemplate.convertAndSend(JT808Consts.led_down_queueName, sendMsg);
				// dzzp_down_data 环境 连接了2千多块站牌的 用这个方式，下发到麻超建立的名叫 dzzp_down_data 的exchage fanout 模式
				//rabbitTemplate.convertAndSend("dzzp_down_data", "", sendMsg);
				sendMsg = null;
			}
			objdata = null;
			
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("FlOWID", flowID);
				//paramMap.put("FlOWID", 9999);//测试环境通过flowid 数据库里能看出来 是测试环境入库的数据
				paramMap.put("MSGID", msgId);
				paramMap.put("LEDBH", ledNo);
				paramMap.put("MSGINFO", HexStringUtils.bytesToHexFun1(bs));
				paramMap.put("MSGDETAIL", timeFormat.format(new Date())+"-"+msgInfo);
				paramMap.put("TABLENAME", "LED_MSGLOG"+monthDateFormat.format(new Date()));
				if(msgId != JT808Consts.msg_id_0x0906) {
					//common808Service.insertCommonMsgBack(paramMap);
				}else {				
					/*DataCache.saveMessageQueue.add(paramMap);
					if (!SaveMessageThread.runFlg) {
						SaveMessageThread dealQueue = new SaveMessageThread();
						new Thread(dealQueue).start();
					}*/
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public   Object[] splitAry(byte[] ary, int subSize) {
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
