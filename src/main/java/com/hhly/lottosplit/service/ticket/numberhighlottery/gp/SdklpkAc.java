package com.hhly.lottosplit.service.ticket.numberhighlottery.gp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.aware.SpringAware;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryChildEnum;
import com.hhly.lottosplit.enums.LotteryEnum.Lottery;
import com.hhly.lottosplit.event.SplitNumberHighHandlerEvent;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.service.ticket.AbstractCSplitHanlde;
import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
/**
 * 
 * @ClassName: SdklpkAc 
 * @Description: 山东快乐扑克3
 * @author wuLong
 * @date 2017年6月16日 上午11:41:24 
 *
 */

@Component
public class SdklpkAc extends AbstractCSplitHanlde implements AbstractINumberSplit {
	
	@PostConstruct
	public void init(){
		SplitNumberHighHandlerEvent event = new SplitNumberHighHandlerEvent(this);
		SpringAware.getApplicationContext().publishEvent(event);
	}

	@Override
	public void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs)
			throws ServiceRuntimeException {
		List<OrderDetailBO> orderDetailBOs = orderInfoBO.getDetailBOs();
		Map<String, List<String>> map = new HashMap<>();
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		int splitMaxNum = lotteryTypeBO.getSplitMaxNum();
		for(OrderDetailBO orderDetailBO : orderDetailBOs){
			int lotteryChildCode = orderDetailBO.getLotteryChildCode();
			LotteryChildEnum.LotteryChild lotteryChild = LotteryChildEnum.LotteryChild.valueOf(lotteryChildCode);
			String planContent = orderDetailBO.getPlanContent();
			int contentType = orderDetailBO.getContentType();
			int multiple = orderInfoBO.getMultipleNum()*orderDetailBO.getMultiple();
			int detailId = orderInfoBO.getId();
			String key = lotteryChildCode+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+detailId;
			List<String> singleList = new ArrayList<>();
			if(map.containsKey(key)){
				singleList = map.get(key);
			}
			if(planContent.indexOf("10")!=-1){
				planContent = planContent.replaceAll("10", "P");
			}
			//单式
			if(SplitTicketUtil.NUM_ONE == contentType){
				if(lotteryChild == LotteryChildEnum.LotteryChild.POKER_DZ){
					if(planContent.equals("XX")){
						char b[] = planContent.toCharArray();
						planContent = StringUtils.join(b, ',');
					}else{
						char b[] = planContent.toCharArray();
						StringBuilder sb = new StringBuilder();
						for(char ac : b){
							if(ac == 'P'){
								sb.append("10");
							}else{
								sb.append(ac);
							}
							sb.append(",");
						}
						String bc = sb.deleteCharAt(sb.length()-1).toString();
						planContent = bc.concat(",*");
					}
				}else if(lotteryChild == LotteryChildEnum.LotteryChild.POKER_BZ){
					char b[] = planContent.toCharArray();
					StringBuilder sb = new StringBuilder();
					for(char ac : b){
						if(ac == 'P'){
							sb.append("10");
						}else{
							sb.append(ac);
						}
						sb.append(",");
					}
					String bc = sb.deleteCharAt(sb.length()-1).toString();
					planContent = bc;
				}
				if(lotteryChild == LotteryChildEnum.LotteryChild.POKER_SZ){
					String[] pc = planContent.split(SymbolConstants.COMMA);
					for(String a : pc) {
						char b[] = a.toCharArray();
						String planCon = StringUtils.join(b, ',').replaceAll("P", "10");
						singleList.add(planCon);
					}
				}else{
					singleList.add(planContent);
				}
			}
			//复式
			else if(SplitTicketUtil.NUM_TWO == contentType){
				if(lotteryChild == LotteryChildEnum.LotteryChild.POKER_TH || lotteryChild == LotteryChildEnum.LotteryChild.POKER_SZ ||
					lotteryChild == LotteryChildEnum.LotteryChild.POKER_THS){
					//直接拆成单式
					String[] pc = planContent.split(SymbolConstants.COMMA);
					if(lotteryChild == LotteryChildEnum.LotteryChild.POKER_SZ){
						for(String a : pc) {
							char b[] = a.toCharArray();
							String planCon = StringUtils.join(b, ',').replaceAll("P", "10");
							singleList.add(planCon);
						}
					}else{
						singleList.addAll(Arrays.asList(pc));
					}
				}else if(lotteryChild == LotteryChildEnum.LotteryChild.POKER_DZ){
					String[] pc = planContent.split(SymbolConstants.COMMA);
					char bp = 'P';
					for(String p : pc){
						String pcd = null;
						if(p.equals("XX")){
							char b[] = p.toCharArray();
							pcd = StringUtils.join(b, ',');
						}else{
							char b[] = p.toCharArray();
							StringBuilder sb = new StringBuilder();
							for(char ac : b){
								if(ac == bp){
									sb.append("10");
								}else{
									sb.append(ac);
								}
								sb.append(",");
							}
							String bc = sb.deleteCharAt(sb.length()-1).toString();
							pcd = bc.concat(",*");
						}
						singleList.add(pcd);
					}
				}else if(lotteryChild == LotteryChildEnum.LotteryChild.POKER_BZ){
					String pc[] = planContent.split(SymbolConstants.COMMA);
					for(String a : pc){
						char b[] = a.toCharArray();
						StringBuilder sb = new StringBuilder();
						for(char ac : b){
							if(ac == 'P'){
								sb.append("10");
							}else{
								sb.append(ac);
							}
							sb.append(",");
						}
						singleList.add(sb.deleteCharAt(sb.length()-1).toString());
					}
				}else{
					int	maxNum = splitMaxAmount/orderDetailBO.getBuyNumber()/2;
					if(maxNum>splitMaxNum){
						maxNum = splitMaxNum;
					}
					List<String> m = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple,orderDetailBO.getPlanContent(),maxNum);
					assembleNoSingleData(orderInfoBO,ticketInfoBOs,m,orderDetailBO);
					m = null;
				}
			}
			//胆拖 不需要拆
			else if(SplitTicketUtil.NUM_THREE== contentType){
				if(orderDetailBO.getBuyNumber() == 1){
					singleList.add(planContent.replaceAll(SymbolConstants.NUMBER_SIGN, SymbolConstants.COMMA));
				}else{
					int	maxNum = splitMaxAmount/orderDetailBO.getBuyNumber()/2;
					if(maxNum>splitMaxNum){
						maxNum = splitMaxNum;
					}
					List<String> m = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple,orderDetailBO.getPlanContent(),maxNum);
					assembleNoSingleData(orderInfoBO,ticketInfoBOs,m,orderDetailBO);
					m = null;
				}
			}
			if(!ObjectUtil.isBlank(singleList)){
				map.put(key, singleList);
			}
		}
		if(!ObjectUtil.isBlank(map)){
			assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE, 2);
			map = null;
		}
	}

	@Override
	public Lottery getLottery() {
		return Lottery.POKER;
	}
}
