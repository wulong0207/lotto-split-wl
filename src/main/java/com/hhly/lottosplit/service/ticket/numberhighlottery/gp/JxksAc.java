package com.hhly.lottosplit.service.ticket.numberhighlottery.gp;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.hhly.lottosplit.aware.SpringAware;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.enums.LotteryEnum.Lottery;
import com.hhly.lottosplit.event.SplitNumberHighHandlerEvent;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.service.ticket.numberhighlottery.KsPc;
/**
 * 
 * @ClassName: JsksAc 
 * @Description: 江西快三
 * @author wuLong
 * @date 2017年6月9日 上午10:37:26 
 *
 */
@Component
public class JxksAc extends KsPc {
	
	@PostConstruct
	public void init(){
		SplitNumberHighHandlerEvent event = new SplitNumberHighHandlerEvent(this);
		SpringAware.getApplicationContext().publishEvent(event);
	}

	@Override
	public void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs)
			throws ServiceRuntimeException {
		super.Handle(orderInfoBO, lotteryTypeBO, ticketInfoBOs);
	}

	@Override
	public Lottery getLottery() {
		return Lottery.JXK3;
	}
	
}
