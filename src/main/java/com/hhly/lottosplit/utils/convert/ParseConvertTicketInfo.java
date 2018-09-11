package com.hhly.lottosplit.utils.convert;

import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.enums.TicketEnum.TicketStatus;

public class ParseConvertTicketInfo {
	
	public static TicketInfoBO parseTicketInfoBo(OrderInfoBO orderInfoBO, OrderDetailBO detailBO,TicketInfoBO ticketInfoBO) {
		ticketInfoBO.setLotteryCode(orderInfoBO.getLotteryCode());
		ticketInfoBO.setLotteryName(orderInfoBO.getLotteryName());
		ticketInfoBO.setLotteryIssue(orderInfoBO.getLotteryIssue());
		ticketInfoBO.setUserId(orderInfoBO.getUserId());
		ticketInfoBO.setTicketStatus((short)TicketStatus.WAITING_ALLOT.getValue());
		ticketInfoBO.setOrderCode(orderInfoBO.getOrderCode());
		ticketInfoBO.setContentType(detailBO.getContentType());
		ticketInfoBO.setLotteryChildCode(detailBO.getLotteryChildCode());
		return ticketInfoBO;
	}
	
	public static TicketInfoBO parseTicketInfoBo(OrderInfoBO orderInfoBO, Integer contentType,Integer lotteryChildCode,TicketInfoBO ticketInfoBO) {
		ticketInfoBO.setLotteryCode(orderInfoBO.getLotteryCode());
		ticketInfoBO.setLotteryName(orderInfoBO.getLotteryName());
		ticketInfoBO.setLotteryIssue(orderInfoBO.getLotteryIssue());
		ticketInfoBO.setUserId(orderInfoBO.getUserId());
		ticketInfoBO.setTicketStatus((short)TicketStatus.WAITING_ALLOT.getValue());
		ticketInfoBO.setOrderCode(orderInfoBO.getOrderCode());
		ticketInfoBO.setContentType(contentType);
		ticketInfoBO.setLotteryChildCode(lotteryChildCode);
		return ticketInfoBO;
	}
}
