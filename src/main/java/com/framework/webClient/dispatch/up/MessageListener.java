package com.framework.webClient.dispatch.up;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import com.framework.jt808.common.DataCache;
import com.framework.webClient.dispatch.up.bean.ActionCMDType;
import com.framework.webClient.dispatch.up.bean.CJSJCCDTO;
import com.framework.webClient.dispatch.up.bean.KLSJDTO;
import com.framework.webClient.dispatch.up.bean.YJDSJDTO;
import com.rabbitmq.client.Channel;


@Component
public class MessageListener implements ChannelAwareMessageListener {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	/**
     * @param 
     * 1、处理成功，这种时候用basicAck确认消息；
     * 2、可重试的处理失败，这时候用basicNack将消息重新入列；
     * 3、不可重试的处理失败，这时候使用basicNack将消息丢弃。
     * 
     *  basicNack(long deliveryTag, boolean multiple, boolean requeue)
     *  deliveryTag:该消息的index
     *  multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
     *  requeue：被拒绝的是否重新入队列
     */
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		//logger.info("amqp received message = {}, channel = {}", new String(message.getBody()), channel);
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		
		byte[] data = message.getBody();
		TMemoryBuffer tmb = new TMemoryBuffer(32);
		
		
		try {
			tmb.write(data);
			TProtocol tp = new org.apache.thrift.protocol.TCompactProtocol(tmb);
			//新协议拥挤度
			CJSJCCDTO cjsjccDto = new CJSJCCDTO();
			cjsjccDto.read(tp);
			if (cjsjccDto.getActionCMDType() == ActionCMDType.YJD_INSERT) {
				//System.out.println("begin");
				YJDSJDTO dto = cjsjccDto.getYjdsj();
				if(dto !=null) {
					if(dto.getEmpId() !=null && !"".equals(dto.getEmpId())) {
						int cjd=(int)dto.getCnyjd();
						if(cjd ==0) {//0 为无效值  拥挤度设为 一般
							cjd=1;
						}else {// 过来的数据比 站牌上拥挤度 大了 1
							cjd=cjd-1;
						}
						String empid=dto.getEmpId();
						DataCache.yjdMap.put(empid, cjd);
					}
				}
				/*int cjd=(int)dto.getCnyjd();
				String empid=dto.getEmpId();
				String xlmc=dto.getXlmc();
				System.out.println("new-------");
				System.out.println("empid:"+empid);
				System.out.println("cjd:"+cjd);
				System.out.println("xlmc:"+xlmc);
				System.out.println("end");*/
			// 老协议拥挤度	
			}/*else if(cjsjccDto.getActionCMDType() == ActionCMDType.CLKLSC_INSERT) {
				KLSJDTO dto = cjsjccDto.getKlsj();
				//System.out.println("Clid:"+dto.getClid()+" "+"Xlbh:"+dto.getXlbh()+" "+"Cnyjd:"+dto.getCnyjd());
				System.out.println("old-------");
				int cjd=(int)dto.getCnyjd();
				String empid=dto.getEmpId();
				String xlmc=dto.getXlmc();
				System.out.println("empid:"+empid);
				System.out.println("cjd:"+cjd);
				System.out.println("xlmc:"+xlmc);
			}*/
			

		} catch (Exception e) {
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
			e.printStackTrace();
			logger.error("error!" + e);
		}
	}
	
	
	
	
}
