package com.hhly.lottosplit.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.enums.LotteryEnum.Lottery;
import com.hhly.lottosplit.event.SplitNumberHighHandlerEvent;
import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;
/**
 * 
 * @ClassName: SplitNumberHighHandlerFactory 
 * @Description: 数字彩，高频彩，低频彩拆票操作工厂类 
 * @author wuLong
 * @date 2017年12月8日 下午2:49:20 
 *
 */
@Component
public class SplitNumberHighHandlerFactory implements ApplicationListener<SplitNumberHighHandlerEvent>{
	
	private static Logger logger = LoggerFactory.getLogger(SplitNumberHighHandlerFactory.class);
	/**
	 * 保存拆票对象  map
	 */
	private static Map<Lottery, AbstractINumberSplit> handleMap = new ConcurrentHashMap<>();

	@Override
	public void onApplicationEvent(SplitNumberHighHandlerEvent event) {
		AbstractINumberSplit abstractINumberSplit = (AbstractINumberSplit)event.getSource();
		this.registerHandler(abstractINumberSplit);
	}
	/**
	 * @Description: 注册拆票对象到Map 
	 * @param abstractINumberSplit 注册对象
	 * @author wuLong
	 * @date 2017年12月7日 下午6:08:16
	 */
	private void registerHandler(AbstractINumberSplit abstractINumberSplit ){
		Lottery lottery =  abstractINumberSplit.getLottery();
		logger.info("开始绑定{}类型的活动处理器",lottery.getDesc());
		handleMap.put(lottery, abstractINumberSplit);
	} 
	/**
	 * 
	 * @Description: 得到拆票对象
	 * @param lotteryCode 大彩种ID
	 * @return AbstractINumberSplit
	 * @author wuLong
	 * @date 2017年12月7日 下午6:07:46
	 */
	public static AbstractINumberSplit getHandler(Integer lotteryCode){
		return handleMap.get(Lottery.getLottery(lotteryCode));
	}
}
