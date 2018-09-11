package com.hhly.lottosplit.thread;

import com.hhly.lottosplit.rabbitmq.provider.impl.SendTicketMessageProvider;
import com.hhly.lottosplit.utils.SplitTicketUtil;
/**
 * 
 * @ClassName: NoticeSendTicketThread 
 * @Description: 异步通知送票消息
 * @author wuLong
 * @date 2017年6月7日 上午10:40:18 
 *
 */
public class NoticeSendTicketThread implements Runnable{ 
    SendTicketMessageProvider messageProvider;
	String orderCode;
	public NoticeSendTicketThread(String orderCode,SendTicketMessageProvider messageProvider) {
		this.orderCode = orderCode;
		this.messageProvider = messageProvider;
	}
	@Override  
	public void run() {  
		this.messageProvider.sendMessage(SplitTicketUtil.SENDTICKET_QUEUE, orderCode);
	} 
}