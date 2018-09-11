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
import com.hhly.lottosplit.service.ticket.numberhighlottery.KlsfPc;
/**
 * 
 * @ClassName: CqklsfAc 
 * @Description: 重庆快乐十分
 * @author wuLong
 * @date 2017年8月16日 上午9:59:48 
 *
 */
@Component
public class CqklsfAc extends KlsfPc {

	/**
	 * @Description: 注入上下文事件
	 * @author wuLong
	 * @date 2017年12月14日 下午4:21:19
	 */
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
		return Lottery.CQKL10;
	}

}
