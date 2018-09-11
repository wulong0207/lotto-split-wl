package com.hhly.lottosplit.service.ticket;

import java.util.List;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.enums.LotteryEnum.Lottery;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
/**
 * 
 * @ClassName: INumberSplitEncap 
 * @Description: 数字彩所有彩种的抽象接口
 * @author wuLong
 * @date 2017年6月8日 下午5:54:07 
 *
 */
public abstract interface AbstractINumberSplit {
	/**
	 * 
	 * @Description: 拆票并封装数字彩票信息
	* @param orderInfoBO 订单信息
	 * @param lotteryTypeBO 彩种信息
	 * @param ticketInfoBOs list返回封装的票
	 * @author wuLong
	 * @date 2017年6月8日 下午5:54:30
	 */
	public abstract void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO,
			List<TicketInfoBO> ticketInfoBOs)throws ServiceRuntimeException;
	
	public abstract Lottery getLottery();
}
