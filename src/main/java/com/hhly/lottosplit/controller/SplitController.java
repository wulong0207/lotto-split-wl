package com.hhly.lottosplit.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.ResultBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.enums.OrderEnum;
import com.hhly.lottosplit.enums.OrderEnum.OrderStatus;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.persistence.order.po.OrderGroupPO;
import com.hhly.lottosplit.rabbitmq.provider.impl.SendTicketMessageProvider;
import com.hhly.lottosplit.service.LotteryTypeService;
import com.hhly.lottosplit.service.OrderService;
import com.hhly.lottosplit.service.SplitTicketService;
import com.hhly.lottosplit.service.TicketInfoService;
import com.hhly.lottosplit.thread.NoticeSendTicketThread;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.convert.MatchUtil;

@RestController
@RequestMapping(value = "/split")
public class SplitController {
	private Logger logger = LoggerFactory.getLogger(SplitController.class);
	
	@Autowired
    OrderService orderService;
	@Autowired
	SplitTicketService splitTicketService;
	@Autowired
	LotteryTypeService lotteryTypeService; 
	@Autowired
	TicketInfoService ticketInfoService;
	@Autowired
    SendTicketMessageProvider messageProvider;
	

	@RequestMapping(value = "/execute",method = RequestMethod.GET)
	public  Object execute(String orderCode) throws Exception{
		if(StringUtils.isBlank(orderCode)){
			throw new ServiceRuntimeException("参数错误！");
		}
		List<OrderInfoBO> orderInfoBos = orderService.getOrderInfos(Arrays.asList(orderCode));
		Map<Integer,Set<String>> setMap = new HashMap<>();
		List<Integer> lotteryCodes = new ArrayList<>();
		SplitTicketUtil.getLotteryCodeOrSystemCodes(orderInfoBos, setMap, lotteryCodes);
		Map<Integer,LotteryTypeBO> map = lotteryTypeService.LotteryTypeBoMap(lotteryCodes);
		for(OrderInfoBO orderInfoBO : orderInfoBos){
			try {
				Map<String,SportAgainstInfoBO> sportAgainstInfoMap = lotteryTypeService.findSportAgainstInfoS(setMap.get(orderInfoBO.getLotteryCode()),orderInfoBO.getLotteryCode());
				String checkMessage = SplitTicketUtil.checkBasicInfo(orderInfoBO);
    	    	if(!ObjectUtil.isBlank(checkMessage)){
    	    		throw new Exception(checkMessage);
    	    	}
    	    	//2.先验证票表是否已经存在票了，防止重复拆票
    	    	int orderInfoTicketCount = ticketInfoService.findCountByOrderCode(orderInfoBO.getOrderCode());
    	    	if(orderInfoTicketCount>0){
    	    		throw new Exception("票表已经存在该订单的票了，不能重复拆票操作");
    	    	}
    	    	OrderInfoBO inOrderInfoBO = orderService.getByOrderCode(orderInfoBO.getOrderCode());

                Short buyType = orderInfoBO.getBuyType();
                if (buyType.equals(OrderEnum.BuyType.BUY_TOGETHER.getValue())) {
					OrderGroupPO orderGroupPO = orderService.selectByOrderCode(orderInfoBO.getOrderCode());
                    double progress = MatchUtil.sum(orderGroupPO.getProgress(), orderGroupPO.getGuaranteeRatio());
                    if (progress < 90) {
                        return ResultBO.ok();
					}

                }

    	    	OrderStatus orderStatus = OrderStatus.parseOrderStatus(inOrderInfoBO.getOrderStatus());
    	    	if(orderStatus==OrderStatus.WAITING_SPLIT_TICKET){
    	    		orderService.updateOrderStatus(OrderEnum.OrderStatus.SPLITING_TICKET,orderInfoBO.getId(),SplitTicketUtil.SYSTEM,null);
    	    		
    	    		splitTicketService.handleEntranceSplitTicket(orderInfoBO,map,sportAgainstInfoMap);
    	    		//9.异步发送消息到送票消息队列
    	    		NoticeSendTicketThread noticeSendTicketThread = new NoticeSendTicketThread(orderInfoBO.getOrderCode(),messageProvider); 
    	    		noticeSendTicketThread.run();
    	    	}
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				if(e instanceof ServiceRuntimeException){
					orderService.updateOrderStatus(OrderEnum.OrderStatus.SPLITING_FAIL,orderInfoBO.getId(),SplitTicketUtil.SYSTEM,null);
					SplitTicketUtil.sendAlarmInfo(1, 15, 2, e.getMessage(), "[拆票失败]lotto-split",messageProvider);
				}
			}
		}
		return ResultBO.ok();
	}

}