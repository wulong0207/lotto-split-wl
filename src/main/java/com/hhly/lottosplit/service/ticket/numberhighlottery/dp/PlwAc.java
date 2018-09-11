package com.hhly.lottosplit.service.ticket.numberhighlottery.dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.aware.SpringAware;
import com.hhly.lottosplit.bo.CooperateCdkeyBO;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryEnum.Lottery;
import com.hhly.lottosplit.event.SplitNumberHighHandlerEvent;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.mapper.CooperateCdkeyMapper;
import com.hhly.lottosplit.service.ticket.AbstractCSplitHanlde;
import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.BaseLottery;
/**
 * 
 * @ClassName: PlwAc 
 * @Description: 排列5实现拆票并封装成票信息 
 * @author wuLong
 * @date 2017年6月9日 下午3:07:10 
 *
 */
@Component
public class PlwAc extends AbstractCSplitHanlde implements AbstractINumberSplit {
	@Autowired
	CooperateCdkeyMapper cooperateCdkeyMapper;
	
	@PostConstruct
	public void init(){
		SplitNumberHighHandlerEvent event = new SplitNumberHighHandlerEvent(this);
		SpringAware.getApplicationContext().publishEvent(event);
	}

	@Override
	public void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs)
			throws ServiceRuntimeException {
		List<CooperateCdkeyBO> cdkeyBOs = null;
    	if(!ObjectUtil.isBlank(orderInfoBO.getRedeemCode())){
    		cdkeyBOs = cooperateCdkeyMapper.selectByMyCdkey(orderInfoBO.getRedeemCode());
    	}
		List<OrderDetailBO> orderDetailBOs = orderInfoBO.getDetailBOs();
		Map<String, List<String>> map = new HashMap<>();
		int splitMaxNum = lotteryTypeBO.getSplitMaxNum();
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		for (OrderDetailBO orderDetailBO : orderDetailBOs) {
			String planContent = orderDetailBO.getPlanContent();
			int contentType = orderDetailBO.getContentType();
			
			int multiple = orderInfoBO.getMultipleNum()*orderDetailBO.getMultiple();
			//单式
			if(SplitTicketUtil.NUM_ONE == contentType){
				int lotteryChildCode = orderDetailBO.getLotteryChildCode();
				String key = lotteryChildCode+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+orderDetailBO.getId();
				List<String> list = new ArrayList<>();
				if(map.containsKey(key)){
					list = map.get(key);
				}
				list.add(planContent);
				map.put(key, list);
			}
			//复式
			else if(SplitTicketUtil.NUM_TWO == contentType){
				if(orderDetailBO.getAmount()>lotteryTypeBO.getSplitMaxAmount()){
					asso(orderInfoBO, ticketInfoBOs, splitMaxNum, splitMaxAmount, orderDetailBO, planContent, multiple);
				}else{
					int maxNum = lotteryTypeBO.getSplitMaxAmount()/orderDetailBO.getBuyNumber()/2;
					if(maxNum>splitMaxNum){
						maxNum = splitMaxNum;
					}
					List<String> list = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, planContent, maxNum);
					for(String x : list){
						 assembleDanTiaoData(orderInfoBO, ticketInfoBOs, x, orderDetailBO, orderDetailBO.getBuyNumber());
					}
				}
			}
		}
		
		if(!ObjectUtil.isBlank(map)){
			if(cdkeyBOs!=null){
				assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE, 2 ,cdkeyBOs);
				map = null;
			}else{
				assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_FIVE, SplitTicketUtil.NUM_ONE, 2);
				map = null;
			}
			
		}
	}

	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param splitMaxNum
	 * @param splitMaxAmount
	 * @param orderDetailBO
	 * @param planContent
	 * @param multiple
	 * @throws NumberFormatException
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年6月13日 下午7:27:03
	 */
	private void asso(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, int splitMaxNum, int splitMaxAmount,
			OrderDetailBO orderDetailBO, String planContent, int multiple)
			throws NumberFormatException, ServiceRuntimeException {
		String[] p = planContent.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		List<List<String>> list = new ArrayList<>();
		for(String a : p){
			String[] b = a.split(SymbolConstants.COMMA);
			list.add(Arrays.asList(b));
		}
		String[][] e = new String[p.length][];
		int i = 0 ;
		for(List<String> c : list){
			int size = c.size();
			if(size<6){
				String[] y = new String[1];
				y[0] = StringUtils.join(c,SymbolConstants.COMMA);
				e[i] = y;
			}else{
				int subNum = c.size() / 2;
				String[] y = new String[1];
				y[0] = StringUtils.join(c,SymbolConstants.COMMA);
				if(subNum>1){
					y = new String[2];
					y[0] = StringUtils.join(c.subList(0, subNum),SymbolConstants.COMMA);
					y[1] = StringUtils.join(c.subList(subNum, c.size()),SymbolConstants.COMMA);
				}
				e[i] = y;
			}
			i++;
		}
		BaseLottery baseLottery = new BaseLottery();
		@SuppressWarnings("static-access")
		String f = baseLottery.getCombineArrToStrabc(e);
		String[] g = f.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.OBLIQUE_LINE);
		for(String h : g){
			String[] j = h.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
			int bet = 1;
			for(String k : j){
				bet *= k.split(SymbolConstants.COMMA).length;
			}
			int maxNum = splitMaxAmount/bet/2;
			if(maxNum>splitMaxNum){
				maxNum = splitMaxNum;
			}
			List<String> xlist = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, h, maxNum);
			for(String x : xlist){
				 assembleDanTiaoData(orderInfoBO, ticketInfoBOs, x, orderDetailBO, bet);
			 }
		}
	}

	@Override
	public Lottery getLottery() {
		return Lottery.PL5;
	}
}
