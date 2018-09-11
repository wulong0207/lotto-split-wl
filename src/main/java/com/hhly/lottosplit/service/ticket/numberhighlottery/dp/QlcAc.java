package com.hhly.lottosplit.service.ticket.numberhighlottery.dp;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.hhly.lottosplit.enums.LotteryEnum.Lottery;
import com.hhly.lottosplit.event.SplitNumberHighHandlerEvent;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.service.ticket.AbstractCSplitHanlde;
import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.Combine;
import com.hhly.lottosplit.utils.convert.ParseConvertTicketInfo;
/**
 * 
 * @ClassName: QlcAc 
 * @Description: 七乐彩 
 * @author wuLong
 * @date 2017年7月31日 下午4:19:10 
 *
 */
@Component
public class QlcAc extends AbstractCSplitHanlde implements AbstractINumberSplit {
	
	@PostConstruct
	public void init(){
		SplitNumberHighHandlerEvent event = new SplitNumberHighHandlerEvent(this);
		SpringAware.getApplicationContext().publishEvent(event);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs)
			throws ServiceRuntimeException {
		List<OrderDetailBO> orderDetailBOs = orderInfoBO.getDetailBOs();
		Map<String, List<String>> map = new HashMap<>();
		int splitMaxNum = lotteryTypeBO.getSplitMaxNum();
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		Combine combine = new Combine();
		for (OrderDetailBO orderDetailBO : orderDetailBOs) {
			String planContent = orderDetailBO.getPlanContent();
			int contentType = orderDetailBO.getContentType();
			
			int multiple = orderInfoBO.getMultipleNum()*orderDetailBO.getMultiple();
			int lotteryChildCode = orderDetailBO.getLotteryChildCode();
			String key = lotteryChildCode+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+orderDetailBO.getId();
			//单式
			if(SplitTicketUtil.NUM_ONE == contentType){
				List<String> list = new ArrayList<>();
				if(map.containsKey(key)){
					list = map.get(key);
				}
				list.add(planContent);
				map.put(key, list);
			}
			//复式
			else if(SplitTicketUtil.NUM_TWO == contentType){
				asso(orderInfoBO, ticketInfoBOs, splitMaxNum, splitMaxAmount, orderDetailBO, planContent, multiple,lotteryChildCode,combine);
			}
			//胆拖
			else if(SplitTicketUtil.NUM_THREE == contentType){
				if(orderDetailBO.getBuyNumber()==1){
					planContent = planContent.replace(SymbolConstants.NUMBER_SIGN, SymbolConstants.COMMA);
					List<String> list = new ArrayList<>();
					if(map.containsKey(key)){
						list = map.get(key);
					}
					list.add(planContent);
					map.put(key, list);
				}else{
					if(orderDetailBO.getAmount()>splitMaxAmount){
						String[] pc = planContent.split(SymbolConstants.NUMBER_SIGN);
						//01,02#03,04,05,06,08
						String d = pc[0];
						String[] tuo = pc[1].split(SymbolConstants.COMMA);
						int dlen = d.split(SymbolConstants.COMMA).length;
						List<String> lists = combine.mn(tuo, 7-dlen);
						for(String l : lists){
							l = l.replaceAll(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA);
							List<String> list = new ArrayList<>();
							if(map.containsKey(key)){
								list = map.get(key);
							}
							list.add(d.concat(SymbolConstants.COMMA).concat(l));
							map.put(key, list);
						}
					}else{
						int maxNum = splitMaxAmount/orderDetailBO.getBuyNumber()/2;
						if(maxNum>splitMaxNum){
							maxNum = splitMaxNum;
						}
						List<String> xlist = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, planContent, maxNum);
						for(String x : xlist){
							assembleDanTiaoData(orderInfoBO, ticketInfoBOs, x, orderDetailBO, orderDetailBO.getBuyNumber());
						}
					}
				}
			}
		}
		
		if(!ObjectUtil.isBlank(map)){
			assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_FIVE, SplitTicketUtil.NUM_ONE, 2);
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
	 * @param planContent
	 * @param multiple
	 * @throws NumberFormatException
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年6月13日 下午7:27:03
	 */
	@SuppressWarnings("unchecked")
	private void asso(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, int splitMaxNum, int splitMaxAmount,
			OrderDetailBO orderDetailBO, String planContent, int multiple,int lotteryChildCode,Combine combine)
			throws NumberFormatException, ServiceRuntimeException {
		int mrzs = splitMaxAmount/2;
		if(mrzs<orderDetailBO.getBuyNumber()){//拆单式
			int prize = 2;
			short lottoAdd = 0;
			if(orderInfoBO.getLottoAdd() == 1){
				prize = 3;
				lottoAdd = 1;
			}
			String[] array = planContent.split(SymbolConstants.COMMA);
			List<String> list = combine.mn(array, 7);
			List<List<String>> lists = SplitTicketUtil.subList(SplitTicketUtil.NUM_FIVE, list);
			for(List<String> ls : lists){
				int buyNumber = ls.size();
				int maxNum = splitMaxAmount/buyNumber/2;
				if(maxNum>splitMaxNum){
					maxNum = splitMaxNum;
				}
				String planC = StringUtils.join(ls,SymbolConstants.SEMICOLON);
				planC = planC.replaceAll(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA);
				List<String> res = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, planC, maxNum);
				for(String p : res){
					String[] m= p.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
					TicketInfoBO ticketInfoBO = new TicketInfoBO();
					ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO, SplitTicketUtil.NUM_ONE, lotteryChildCode, ticketInfoBO);
					ticketInfoBO.setTicketContent(m[0]);
					ticketInfoBO.setLottoAdd(lottoAdd);
					ticketInfoBO.setMultipleNum(Integer.valueOf(m[1]));
					ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(prize * ticketInfoBO.getMultipleNum() * buyNumber)));
					ticketInfoBO.setEndTicketTime(orderInfoBO.getEndTicketTime());
					ticketInfoBO.setOrderDetailId(orderDetailBO.getId());
					ticketInfoBOs.add(ticketInfoBO);
				}
			}
		}else{
			int maxNum = splitMaxAmount/orderDetailBO.getBuyNumber()/2;
			if(maxNum>splitMaxNum){
				maxNum = splitMaxNum;
			}
			List<String> xlist = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, planContent, maxNum);
			for(String x : xlist){
				assembleDanTiaoData(orderInfoBO, ticketInfoBOs, x, orderDetailBO, orderDetailBO.getBuyNumber());
			}
		}
	}

	@Override
	public Lottery getLottery() {
		return Lottery.QLC;
	}
}
	