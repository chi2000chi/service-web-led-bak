package com.framework.jt808.protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.framework.protocol.BSBCPBusState;
import com.framework.util.ByteUtils;

@Component
public class BSBCPLedDown extends BSBCPBusState {

	// 808协议长度
	public byte ledMsgLen;
	//808消息体
	public byte[] msg_808;

	public byte[] toBytes() {
		try {
			int size = 1+msg_808.length;
			List paramList = new ArrayList();
			paramList.add(ledMsgLen);
			paramList.add(msg_808);
			byte[] bytes = super.toBytes(paramList, size);

			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public BSBCPLedDown toObject(byte[] bytes) {
		Map paramMap = super.toMap();
		paramMap.put("ledMsgLen", ledMsgLen);
		paramMap.put("msg_808", msg_808);
		return (BSBCPLedDown) ByteUtils.getObject(paramMap, bytes, this.getClass());
	}


	public byte getLedMsgLen() {
		return ledMsgLen;
	}

	public void setLedMsgLen(byte ledMsgLen) {
		this.ledMsgLen = ledMsgLen;
	}

	public byte[] getMsg_808() {
		return msg_808;
	}

	public void setMsg_808(byte[] msg_808) {
		this.msg_808 = msg_808;
	}

}
