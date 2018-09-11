package com.hhly.lottosplit.event;

import org.springframework.context.ApplicationEvent;

import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;

/**
 * 
 * @ClassName: SplitHandlerEvent 
 * @Description: 数字彩拆票操作器事件
 * @author wuLong
 * @date 2017年12月7日 下午4:24:06 
 *
 */
@SuppressWarnings("serial")
public class SplitNumberHighHandlerEvent extends ApplicationEvent{

	public SplitNumberHighHandlerEvent(AbstractINumberSplit source) {
		super(source);
	}
	
}
