package com.hhly.lottosplit.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.enums.LotteryEnum;
import com.hhly.lottosplit.enums.LotteryEnum.LotteryPr;
import com.hhly.lottosplit.event.SplitHandlerEvent;
import com.hhly.lottosplit.service.ticket.AbstractCSplitTicket;
/**
 * 
 * @ClassName: SplitHandlerFactory 
 * @Description: 拆票操作工厂类
 * @author wuLong
 * @date 2017年12月7日 下午4:48:45 
 *
 */
@Component
public class SplitHandlerFactory implements ApplicationListener<SplitHandlerEvent>{
	private static Logger logger = LoggerFactory.getLogger(SplitHandlerFactory.class);
	/**
	 * 保存拆票对象 map
	 */
	private static Map<LotteryPr, AbstractCSplitTicket> handleMap = new ConcurrentHashMap<>();
	/**
	 * 
	 * @Description: 得到拆票对象
	 * @param lotteryCode 大彩种ID
	 * @return AbstractCSplitTicket
	 * @author wuLong
	 * @date 2017年12月7日 下午6:07:46
	 */
	public static AbstractCSplitTicket getHandler(Integer lotteryCode){
		LotteryPr lotteryPr = LotteryEnum.getLottery(lotteryCode);
		if(lotteryPr.getName().equals(LotteryPr.SFGG.getName())){
			lotteryPr = LotteryPr.BJDC;
		}else if(lotteryPr.getName().equals(LotteryPr.GPC.getName())){
			lotteryPr = LotteryPr.SZC;
		} else if (lotteryPr.getName().equals(LotteryPr.GYJJC.getName())) {
			lotteryPr = LotteryPr.GYJJC;
		}
		return handleMap.get(lotteryPr);
	}
	
	/**
	 * @Description: 注册拆票对象到Map 
	 * @param abstractCSplitTicket 注册对象
	 * @author wuLong
	 * @date 2017年12月7日 下午6:08:16
	 */
	private void registerHandler(AbstractCSplitTicket abstractCSplitTicket){
		LotteryPr lotteryPr = abstractCSplitTicket.getLotteryPr();
		logger.info("开始绑定{}类型的活动处理器", lotteryPr.getName());
		handleMap.put(lotteryPr, abstractCSplitTicket);
	}

	@Override
	public void onApplicationEvent(SplitHandlerEvent event) {
		AbstractCSplitTicket abstractCSplitTicket = (AbstractCSplitTicket)event.getSource();
		this.registerHandler(abstractCSplitTicket);
	}
	
}
