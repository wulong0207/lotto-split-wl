package com.hhly.lottosplit.rabbitmq.consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.rabbitmq.provider.impl.SendTicketMessageProvider;
import com.hhly.lottosplit.service.LotteryTypeService;
import com.hhly.lottosplit.service.OrderService;
import com.hhly.lottosplit.service.SplitTicketService;
import com.hhly.lottosplit.service.TicketInfoService;
import com.hhly.lottosplit.thread.SplitThread;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.rabbitmq.client.Channel;

/**
 * 消息监听者
 *
 * @author wulong
 * @create 2017/5/11 17:06
 */
@Component("splitTicketMessageListener")
public class SplitTicketMessageListener implements ChannelAwareMessageListener  {
	private Logger logger = LoggerFactory.getLogger(SplitTicketMessageListener.class);
	@Autowired
    OrderService orderService;
	@Autowired
	SplitTicketService splitTicketService;
	@Autowired
	LotteryTypeService lotteryTypeService; 
	@Autowired
	SendTicketMessageProvider messageProvider;
	@Autowired
	TicketInfoService ticketInfoService;
	
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		try {
			//消息的标识，false只确认当前一个消息收到，true确认所有consumer获得的消息
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); 
        	//内容格式(1:支付完成,2:完成当前期追号订单,格式:订单编号1,订单编号2#1)
        	//渠道来源(1:支付完成,2:完成当前期追号订单,3:未知来源)
        	String orderCode = new String(message.getBody(),"UTF-8");
        	if(ObjectUtil.isBlank(orderCode)){
        		return;
        	}
        	//1.返回订单编号的数组
        	String[] orderCodes = SplitTicketUtil.checkIn(orderCode);
        	if(orderCodes!=null){
        		//2.根据订单编号查询订单集合信息
        		List<OrderInfoBO> orderInfoBOs = orderService.getOrderInfos(Arrays.asList(orderCodes));
        		Map<Integer,Set<String>> setMap = new HashMap<>();
        		List<Integer> lotteryCodes = new ArrayList<>();
        		SplitTicketUtil.getLotteryCodeOrSystemCodes(orderInfoBOs, setMap, lotteryCodes);
        		for(OrderInfoBO orderInfoBO : orderInfoBOs){
        			//3.查询赛事信息，返回map集合  map<赛事编号,赛事信息表>
        			Map<String,SportAgainstInfoBO> sportAgainstInfoMap = lotteryTypeService.findSportAgainstInfoS(setMap.get(orderInfoBO.getLotteryCode()),orderInfoBO.getLotteryCode());
        			//4.查询彩种信息，返回map集合 map<彩种ID,彩种信息表>
        			Map<Integer,LotteryTypeBO> map = lotteryTypeService.LotteryTypeBoMap(lotteryCodes);
        			//5.多线程
        			for(OrderInfoBO infoBO : orderInfoBOs){
        				SplitThread splitThread = new SplitThread(splitTicketService, infoBO, map, sportAgainstInfoMap, messageProvider, orderService, ticketInfoService);
        				SplitTicketUtil.FIXED_THREAD_POOL.execute(splitThread);
        			}
        		}
        	}
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
	}
}
