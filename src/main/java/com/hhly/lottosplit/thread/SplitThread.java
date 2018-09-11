package com.hhly.lottosplit.thread;

import java.sql.SQLException;
import java.util.Map;

import com.hhly.lottosplit.bo.ResultBO;
import com.hhly.lottosplit.persistence.order.po.OrderGroupPO;
import com.hhly.lottosplit.utils.convert.MatchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.enums.OrderEnum;
import com.hhly.lottosplit.enums.OrderEnum.OrderStatus;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.rabbitmq.provider.impl.SendTicketMessageProvider;
import com.hhly.lottosplit.service.OrderService;
import com.hhly.lottosplit.service.SplitTicketService;
import com.hhly.lottosplit.service.TicketInfoService;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;

public class SplitThread implements Runnable{

	private Logger logger = LoggerFactory.getLogger(SplitThread.class);

	public static final int threshold = 10;  
	private SplitTicketService splitTicketService;
	private OrderInfoBO orderInfoBO;
	private Map<Integer,LotteryTypeBO> map;
	private Map<String,SportAgainstInfoBO> sportAgainstInfoMap;
	private SendTicketMessageProvider messageProvider;
	private OrderService orderService;
	private TicketInfoService ticketInfoService;
	
	public SplitThread(SplitTicketService splitTicketService,OrderInfoBO orderInfoBO,
			Map<Integer,LotteryTypeBO> map,Map<String,SportAgainstInfoBO> sportAgainstInfoMap,
			SendTicketMessageProvider messageProvider,OrderService orderService,TicketInfoService ticketInfoService) {  
        this.splitTicketService = splitTicketService;
        this.orderInfoBO = orderInfoBO;
        this.map = map;
        this.sportAgainstInfoMap = sportAgainstInfoMap;
        this.messageProvider = messageProvider;
        this.orderService = orderService;
        this.ticketInfoService = ticketInfoService;
    }  
	
	@Override
	public void run() {
		try {
			handleStepOne();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			if(e instanceof ServiceRuntimeException){
				try {
					orderService.updateOrderStatus(OrderEnum.OrderStatus.SPLITING_FAIL,orderInfoBO.getId(),SplitTicketUtil.SYSTEM,null);
				} catch (SQLException e1) {
					logger.error(e.getMessage(),e);
				}
			}
			SplitTicketUtil.sendAlarmInfo(1, 15, 2, e.getMessage(), "[拆票失败]lotto-split",messageProvider);
		}
	}

	/** 
	 * @Description: 第一步
	 * @throws Exception
	 * @throws SQLException
	 * @author wuLong
	 * @date 2017年6月26日 上午10:25:30
	 */
	private void handleStepOne() throws Exception, ServiceRuntimeException {
		checkInfo();
		OrderInfoBO inOrderInfoBO = orderService.getByOrderCode(orderInfoBO.getOrderCode());
		OrderStatus orderStatus = OrderStatus.parseOrderStatus(inOrderInfoBO.getOrderStatus());

		Short buyType = orderInfoBO.getBuyType();
		if (buyType.equals(OrderEnum.BuyType.BUY_TOGETHER.getValue())) {
			OrderGroupPO orderGroupPO = orderService.selectByOrderCode(orderInfoBO.getOrderCode());
			double progress = MatchUtil.sum(orderGroupPO.getProgress(), orderGroupPO.getGuaranteeRatio());
			if (progress < 90) {
				return;
			}
		}

		if(orderStatus==OrderStatus.WAITING_SPLIT_TICKET){
			//3.更新订单为拆票中
			int num = orderService.updateOrderStatusSplitting(orderInfoBO.getId());
			if(num == 0){
				logger.info("修改订单状态时订单正在拆票中，不能重复拆票："+inOrderInfoBO.toString());
				return;
			}
			//4.开始拆票
			splitTicketService.handleEntranceSplitTicket(orderInfoBO,map,sportAgainstInfoMap);
			//5.异步发送消息到送票消息队列
			NoticeSendTicketThread noticeSendTicketThread = new NoticeSendTicketThread(orderInfoBO.getOrderCode(),messageProvider); 
			noticeSendTicketThread.run();
		}
	}
	
	/** 
	 * @Description: 检查订单基本信息
	 * @throws Exception
	 * @throws SQLException
	 * @author wuLong
	 * @date 2017年6月26日 上午10:54:40
	 */
	private void checkInfo() throws Exception, SQLException {
		//1.验证基础信息
		String checkMessage = SplitTicketUtil.checkBasicInfo(orderInfoBO);
		if(!ObjectUtil.isBlank(checkMessage)){
			throw new Exception(checkMessage);
		}
		//2.先验证票表是否已经存在票了，防止重复拆票
		int orderInfoTicketCount = ticketInfoService.findCountByOrderCode(orderInfoBO.getOrderCode());
		if(orderInfoTicketCount>0){
			throw new Exception(orderInfoBO.getOrderCode()+"票表已经存在该订单的票了，不能重复拆票操作");
		}
	}
}
