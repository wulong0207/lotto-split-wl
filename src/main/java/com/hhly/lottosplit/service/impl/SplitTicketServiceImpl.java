package com.hhly.lottosplit.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.enums.LotteryEnum;
import com.hhly.lottosplit.enums.OrderEnum;
import com.hhly.lottosplit.enums.OrderEnum.OrderStatus;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.factory.SplitHandlerFactory;
import com.hhly.lottosplit.mapper.CooperateCdkeyMapper;
import com.hhly.lottosplit.mapper.LotteryIssueDaoMapper;
import com.hhly.lottosplit.persistence.issue.po.LotteryIssuePO;
import com.hhly.lottosplit.service.OrderService;
import com.hhly.lottosplit.service.SplitTicketService;
import com.hhly.lottosplit.service.TicketInfoService;
import com.hhly.lottosplit.service.ticket.AbstractCSplitTicket;
import com.hhly.lottosplit.utils.SplitTicketUtil;

/**
 * @author wuLong
 * @create 2017/5/9 12:09
 */
@Service("splitTicketService")
@Transactional
public class SplitTicketServiceImpl implements SplitTicketService {

    private static Logger logger = LoggerFactory.getLogger(SplitTicketServiceImpl.class);
    @Autowired
    SplitHandlerFactory splitHandlerFactory;
    @Autowired
    OrderService orderService;
    @Autowired
    TicketInfoService ticketInfoService;
    @Autowired
    LotteryIssueDaoMapper lotteryIssueDaoMapper;
    @Autowired
    CooperateCdkeyMapper cooperateCdkeyMapper;
    @Autowired
    ApplicationContext ctx;

    /**
     * <li>1.数据基本验证</li>
     * <li>2.先验证票表是否已经存在票了，防止重复拆票</li>
     * <li>3.根据大彩种从拆票工厂获取拆票对象</li>
     * <li>4.执行拆票</li>
     * <li>5.验证票总金额与订单金额是否一致</li>
     * <li>6.添加票表记录</li>
     * <li>7.修改订单状态</li>
     * <li>8.发送消息到送票消息队列</li>
     * @Description: 进行拆票操作
     * @param orderInfoBO 订单信息
     * @param map 彩种信息集合
     * @param againstInfoBOs 赛事集合
     * @throws Exception
     * @author wuLong
     * @date 2017年6月3日 下午2:49:04
     */
    @SuppressWarnings("static-access")
	@Override
    @Transactional(propagation =Propagation.REQUIRED,rollbackFor = RuntimeException.class)
    public void handleEntranceSplitTicket(OrderInfoBO orderInfoBO,Map<Integer,LotteryTypeBO> map,Map<String,SportAgainstInfoBO> sportAgainstInfoMap) throws Exception,ServiceRuntimeException{
    	logger.info("订单信息:"+orderInfoBO.toString());
    	int orderId = orderInfoBO.getId();
    	String orderCode = orderInfoBO.getOrderCode();
    	Integer lotteryCode = orderInfoBO.getLotteryCode();
    	//3.根据大彩种从拆票工厂获取拆票对象
    	AbstractCSplitTicket splitTicket = splitHandlerFactory.getHandler(lotteryCode);
    	LotteryTypeBO lotteryTypeBO = map.get(lotteryCode);
    	if(lotteryTypeBO == null){
    		throw new Exception("订单编号:"+orderCode+",对应的彩种ID:"+orderInfoBO.getLotteryCode()+"基本配置信息为空");
    	}
    	//4.执行拆票
    	List<TicketInfoBO> ticketInfoBOS = splitTicket.excute(orderInfoBO,lotteryTypeBO,sportAgainstInfoMap);
    	LotteryIssuePO lotteryIssuePO = lotteryIssueDaoMapper.findLotteryIssue(lotteryCode, orderInfoBO.getLotteryIssue());
    	//5.竞彩篮球或竞彩足球取当前期信息
    	if(lotteryIssuePO.getCurrentIssue() != 1 && (lotteryCode == LotteryEnum.Lottery.BB.getName() || lotteryCode == LotteryEnum.Lottery.FB.getName())){
    		lotteryIssuePO = lotteryIssueDaoMapper.findCurrentIssue(lotteryCode);
    	}
    	//6.验证票总金额与订单金额是否一致
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOS,lotteryIssuePO);
    	boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
    	if(!rs){
    		throw new ServiceRuntimeException("订单编号:"+orderCode+",票拆出来的总金额:"+sumticketAmount+",跟订单金额:"+orderInfoBO.getOrderAmount()+"不匹配");
    	}
    	//7.再查一次订单状态
    	OrderInfoBO inOrderInfoBO = orderService.getByOrderCode(orderCode);
    	OrderStatus orderStatus = OrderStatus.parseOrderStatus(inOrderInfoBO.getOrderStatus());
    	if(orderStatus != OrderStatus.SPLITING_TICKET){
    		throw new Exception("订单编号:"+orderCode+",订单状态:"+orderStatus.getDesc()+",不能执行最后的票入库操作");
    	}
    	//8.修改订单状态
    	orderService.updateOrderStatus(OrderEnum.OrderStatus.WAITING_TICKET,orderId,SplitTicketUtil.SYSTEM,ticketInfoBOS.size());
    	//9.添加票表记录
    	ticketInfoService.addTicketInfoList(ticketInfoBOS);
    	ticketInfoBOS = null;
    }
}
