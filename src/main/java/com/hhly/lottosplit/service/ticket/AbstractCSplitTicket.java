package com.hhly.lottosplit.service.ticket;

import java.util.List;
import java.util.Map;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.enums.LotteryEnum;
/**
 * @ClassName: AbstractCSplitTicket 
 * @Description: 拆票抽象类
 * @author wuLong
 * @date 2017年12月14日 下午4:25:06 
 */
public abstract class AbstractCSplitTicket {
	/**
	 * @Description: 拆票处理
	 * @param detailVOs
	 * @param orderInfoBO
	 * @return List
	 * @author wuLong
	 * @date 2017年4月6日 下午4:15:58
	 */
	public abstract List<TicketInfoBO> excute( OrderInfoBO orderInfoBO,LotteryTypeBO lotteryTypeBO,Map<String,SportAgainstInfoBO> sportAgainstInfoMap)throws Exception;
	/**
	 * @Description: 大彩种具体枚举分类
	 * @return LotteryEnum.LotteryPr
	 * @author wuLong
	 * @date 2017年12月7日 下午4:37:25
	 */
	public abstract LotteryEnum.LotteryPr getLotteryPr();
}
