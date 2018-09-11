package com.hhly.lottosplit.service.ticket.numberhighlottery.dp;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.aware.SpringAware;
import com.hhly.lottosplit.bo.CooperateCdkeyBO;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.enums.LotteryEnum.Lottery;
import com.hhly.lottosplit.event.SplitNumberHighHandlerEvent;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.mapper.CooperateCdkeyMapper;
import com.hhly.lottosplit.service.ticket.numberhighlottery.FcsdPlsAc;
import com.hhly.lottosplit.utils.ObjectUtil;
/**
 * 
 * @ClassName: FcsdAc 
 * @Description: 福彩3D拆票操作类
 * @author wuLong
 * @date 2017年12月8日 下午2:34:41 
 *
 */
@Component
public class FcsdAc extends FcsdPlsAc {
	@Autowired
	CooperateCdkeyMapper cooperateCdkeyMapper;
	
	@PostConstruct
	public void init(){
		SplitNumberHighHandlerEvent event = new SplitNumberHighHandlerEvent(this);
		SpringAware.getApplicationContext().publishEvent(event);
	}

	@Override
	public void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs)
			throws ServiceRuntimeException {
		List<CooperateCdkeyBO> cdkeyBOs = null;
    	if(!ObjectUtil.isBlank(orderInfoBO.getRedeemCode())){
    		cdkeyBOs = cooperateCdkeyMapper.selectByMyCdkey(orderInfoBO.getRedeemCode());
    	}
    	handleFc3d(orderInfoBO, lotteryTypeBO, ticketInfoBOs,cdkeyBOs);
	}

	@Override
	public Lottery getLottery() {
		return Lottery.F3D;
	}

}
