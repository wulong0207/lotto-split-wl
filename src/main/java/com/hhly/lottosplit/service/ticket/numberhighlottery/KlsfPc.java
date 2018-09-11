package com.hhly.lottosplit.service.ticket.numberhighlottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryChildEnum;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.service.ticket.AbstractCSplitHanlde;
import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.Combine;
/**
 * 
 * @ClassName: KlsfPc 
 * @Description: 快乐十分
 * @author wuLong
 * @date 2017年8月16日 上午9:57:21 
 *
 */
public abstract class KlsfPc extends AbstractCSplitHanlde implements AbstractINumberSplit {

	@SuppressWarnings("unchecked")
	@Override
	public void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs)
			throws ServiceRuntimeException {
		List<OrderDetailBO> orderDetailBOs = orderInfoBO.getDetailBOs();
		Map<String, List<String>> map = new HashMap<>();
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		int splitMaxNum = lotteryTypeBO.getSplitMaxNum();
		Combine combine = new Combine();
		for(OrderDetailBO orderDetailBO : orderDetailBOs){
			int lotteryChildCode = orderDetailBO.getLotteryChildCode();
			LotteryChildEnum.LotteryChild lotteryChild = LotteryChildEnum.LotteryChild.valueOf(lotteryChildCode);
			String planContent = orderDetailBO.getPlanContent();
			int contentType = orderDetailBO.getContentType();
			
			int multiple = orderInfoBO.getMultipleNum()*orderDetailBO.getMultiple();
			String key = lotteryChildCode+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+orderDetailBO.getId();
			List<String> singleList = new ArrayList<>();
			if(map.containsKey(key)){
				singleList = map.get(key);
			}
			//单式
			if(SplitTicketUtil.NUM_ONE == contentType){
				singleList.add(planContent);
			}
			//复式
			else if(SplitTicketUtil.NUM_TWO == contentType){
				String[] pc = planContent.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
				if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_D2||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_D2){
					String a[] = pc[0].split(SymbolConstants.COMMA);
					String b[] = pc[1].split(SymbolConstants.COMMA);
					for(String c : a ){
						for(String d : b){
							if(c.equals(d)){
								continue;
							}
							singleList.add(c+SymbolConstants.VERTICAL_BAR+d);
						}
					}
					//直接拆成单式
				}else if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_D3||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_D3){
					String a[] = pc[0].split(SymbolConstants.COMMA);
					String b[] = pc[1].split(SymbolConstants.COMMA);
					String c[] = pc[2].split(SymbolConstants.COMMA);
					for(String d : a ){
						for(String e : b){
							for(String f : c){
								if(d.equals(e)||e.equals(f)||d.equals(f)){
									continue;
								}
								singleList.add(d+SymbolConstants.VERTICAL_BAR+e+SymbolConstants.VERTICAL_BAR+f);
							}
						}
					}
				}else{
					if(orderDetailBO.getAmount()>splitMaxAmount){//直接拆单式
						int len = 0;
						if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_R4||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_R4){
							len = 4;
						}else if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_R5||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_R5){
							len = 5;
						}else if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_G3||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_G3){
							len = 3;
						}else if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_G2||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_G2){
							len = 2;
						}
						String[] pcz = planContent.split(SymbolConstants.COMMA);
						List<String> listCombine = combine.mn(pcz, len);
						for(String l : listCombine){
							singleList.add(l.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA));
						}
					}else{
						assemble(orderInfoBO, ticketInfoBOs, splitMaxNum, splitMaxAmount, orderDetailBO, multiple);
					}
				}
			}
			//胆拖 暂时
			else if(SplitTicketUtil.NUM_THREE== contentType){
				if(orderDetailBO.getBuyNumber() == 1){
					singleList.add(planContent.replaceAll(SymbolConstants.NUMBER_SIGN, SymbolConstants.COMMA));
				}else{
					String pc[] = planContent.split(SymbolConstants.NUMBER_SIGN);
					String dan = pc[0];
					String tuo[] = pc[1].split(SymbolConstants.COMMA);
					int danLen = dan.split(SymbolConstants.COMMA).length;
					int tuoLen = 0;
					if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_R2||lotteryChild == LotteryChildEnum.LotteryChild.DKL10_G2||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_R2||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_G2){
						tuoLen = 2-danLen;
					}else if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_R3||lotteryChild == LotteryChildEnum.LotteryChild.DKL10_G3||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_R3||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_G3){
						tuoLen = 3-danLen;
					}else if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_R4||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_R4){
						tuoLen = 4-danLen;
					}else if(lotteryChild == LotteryChildEnum.LotteryChild.DKL10_R5||lotteryChild == LotteryChildEnum.LotteryChild.CQKL10_R5){
						tuoLen = 5-danLen;
					}
					List<String> listCombine = combine.mn(tuo, tuoLen);
					for(String l : listCombine){
						singleList.add(dan+SymbolConstants.COMMA+(l.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA)));
					}
				}
			}
			if(!ObjectUtil.isBlank(singleList)){
				map.put(key, singleList);
			}
		}
		if(!ObjectUtil.isBlank(map)){
			assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE,SplitTicketUtil.NUM_TWO);
			map = null;
		}
	}
	
	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param splitMaxNum
	 * @param splitMaxAmount
	 * @param orderDetailBO
	 * @param multiple
	 * @throws NumberFormatException
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年6月13日 下午5:15:32
	 */
	private void assemble(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, int splitMaxNum,
			int splitMaxAmount, OrderDetailBO orderDetailBO, int multiple)
			throws NumberFormatException, ServiceRuntimeException {
		int	maxNum = splitMaxAmount/orderDetailBO.getBuyNumber()/2;
		if(maxNum>splitMaxNum){
			maxNum = splitMaxNum;
		}
		List<String> m = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple,orderDetailBO.getPlanContent(),maxNum);
		assembleNoSingleData(orderInfoBO,ticketInfoBOs,m,orderDetailBO);
		m = null;
	}
}
