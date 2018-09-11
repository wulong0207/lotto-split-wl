package com.hhly.lottosplit.service.ticket.biglottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.aware.SpringAware;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.enums.LotteryEnum.LotteryPr;
import com.hhly.lottosplit.event.SplitHandlerEvent;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.factory.SplitNumberHighHandlerFactory;
import com.hhly.lottosplit.service.ticket.AbstractCSplitTicket;
import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;

/**
 * 数字彩和高频彩
 *
 * @author wuLong
 * @create 2017/5/10 11:14
 */
@Component
public class NumberHighColorSplitTicket extends AbstractCSplitTicket {
	@Autowired
	SplitNumberHighHandlerFactory numberHighHandlerFactory;
	
	@PostConstruct
	public void init(){
		SplitHandlerEvent event = new SplitHandlerEvent(this);
		SpringAware.getApplicationContext().publishEvent(event);
	}
	
    @SuppressWarnings("static-access")
	@Override
    public List<TicketInfoBO> excute(OrderInfoBO orderInfoBO,LotteryTypeBO lotteryTypeBO,Map<String,SportAgainstInfoBO> sportAgainstInfoMap) throws Exception {
        List<TicketInfoBO> ticketInfoBOs = new ArrayList<>();
        AbstractINumberSplit numberSplit = numberHighHandlerFactory.getHandler(orderInfoBO.getLotteryCode());
        if(numberSplit==null){
        	throw new ServiceRuntimeException("目前还没有开发此"+orderInfoBO.getLotteryCode()+"彩种的拆票服务");
        }
        numberSplit.Handle(orderInfoBO, lotteryTypeBO, ticketInfoBOs);
        return ticketInfoBOs;
    }

	@Override
	public LotteryPr getLotteryPr() {
		return LotteryPr.SZC;
	}
}
