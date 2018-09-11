package com.hhly.lottosplit.quartz;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
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

@Configuration
@Component 
@EnableScheduling 
public class SplitOrderInfoQuartz {
	private Logger logger = LoggerFactory.getLogger(SplitOrderInfoQuartz.class);
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
	
	/**
	 * 4分钟一次
	 * @Description: 定时轮循处理所有可以进行拆票的订单 
	 * @author wuLong
	 * @throws SQLException 
	 * @date 2017年5月22日 下午3:17:53
	 */
	public void quartzMissOrderInfo() throws SQLException{
		List<String> list = orderService.getOrderInfoList(null);
		if(!ObjectUtil.isBlank(list)){
			logger.info("定时轮循处理所有可以进行拆票的订单开始,数量："+list.size());
			List<OrderInfoBO> orderInfoBOs = orderService.getOrderInfos(list);
			if(!ObjectUtil.isBlank(orderInfoBOs)){
				Map<Integer,Set<String>> setMap = new HashMap<>();
				List<Integer> lotteryCodes = new ArrayList<>();
				SplitTicketUtil.getLotteryCodeOrSystemCodes(orderInfoBOs, setMap, lotteryCodes);
				for(OrderInfoBO orderInfoBO : orderInfoBOs){
					Map<String,SportAgainstInfoBO> sportAgainstInfoMap = lotteryTypeService.findSportAgainstInfoS(setMap.get(orderInfoBO.getLotteryCode()),orderInfoBO.getLotteryCode());
					Map<Integer,LotteryTypeBO> map = lotteryTypeService.LotteryTypeBoMap(lotteryCodes);
					SplitThread splitThread = new SplitThread(splitTicketService, orderInfoBO, map, sportAgainstInfoMap, messageProvider, orderService, ticketInfoService);
					SplitTicketUtil.FIXED_THREAD_POOL.execute(splitThread);
				}
			}
		}else{
			logger.info("定时轮循处理所有可以进行拆票的订单开始,数量："+0);
		}
	}
}
