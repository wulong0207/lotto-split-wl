package com.hhly.lottosplit.event;

import org.springframework.context.ApplicationEvent;

import com.hhly.lottosplit.service.ticket.AbstractCSplitTicket;
/**
 * 
 * @ClassName: SplitHandlerEvent 
 * @Description: 竞技彩拆票操作器事件
 * @author wuLong
 * @date 2017年12月7日 下午4:24:06 
 *
 */
@SuppressWarnings("serial")
public class SplitHandlerEvent extends ApplicationEvent{

	public SplitHandlerEvent(AbstractCSplitTicket source) {
		super(source);
	}

}
