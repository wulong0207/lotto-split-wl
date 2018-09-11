package com.hhly.lottosplit.service.ticket;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hhly.lottosplit.bo.CooperateCdkeyBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.convert.ParseConvertTicketInfo;

public abstract class AbstractCSplitHanlde {

	/**
	 * 
	 * @Description:所有彩种的单式拼装
	 * @param orderInfoBO 订单信息
	 * @param ticketInfoBOs 所有的票集合
	 * @param fcHzMap
	 * @param splitMaxNum 最大票注数
	 * @param splitMaxAmount 最大票金额
	 * @param lotteryChildCode 子彩种ID
	 * @param subNum 切成N条为一张票
	 * @param contentType 票的投注方式   1：单式，2：复式，3：胆拖，6：和值
	 * @param prize 每一注的本金
	 * @author wuLong
	 * @date 2017年12月2日 下午12:03:58
	 */
	protected static void assembleSingleData(OrderInfoBO orderInfoBO,List<TicketInfoBO> ticketInfoBOs,
			Map<String, List<String>> fcHzMap,Integer splitMaxNum,
			Integer splitMaxAmount,Integer subNum,Integer contentType,Integer prize){
		if(!ObjectUtil.isBlank(fcHzMap)){
			for(String k : fcHzMap.keySet()){
				List<String> list = fcHzMap.get(k);
				String[] j = k.split(SymbolConstants.COMMA);
				int multiple = Integer.valueOf(j[1]);
				int detailId = Integer.valueOf(j[2]);
				int lotteryChildCode = Integer.valueOf(j[0]);
				List<List<String>> lists = SplitTicketUtil.subList(subNum, list);
				for(List<String> ls : lists){
					int buyNumber = ls.size();
					int maxNum = splitMaxAmount/buyNumber/2;
					if(maxNum>splitMaxNum){
						maxNum = splitMaxNum;
					}
					String planContent = StringUtils.join(ls,SymbolConstants.SEMICOLON);
					List<String> res = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, planContent, maxNum);
					for(String p : res){
						String[] m= p.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
						TicketInfoBO ticketInfoBO = new TicketInfoBO();
						ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO, contentType, lotteryChildCode, ticketInfoBO);
						ticketInfoBO.setTicketContent(m[0]);
						ticketInfoBO.setLottoAdd(orderInfoBO.getLottoAdd());
						ticketInfoBO.setMultipleNum(Integer.valueOf(m[1]));
						ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(prize * ticketInfoBO.getMultipleNum() * buyNumber)));
						ticketInfoBO.setEndTicketTime(orderInfoBO.getEndTicketTime());
						ticketInfoBO.setOrderDetailId(detailId);
						ticketInfoBOs.add(ticketInfoBO);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @Description:所有彩种的单式拼装
	 * @param orderInfoBO 订单信息
	 * @param ticketInfoBOs 所有的票集合
	 * @param fcHzMap
	 * @param splitMaxNum 最大票注数
	 * @param splitMaxAmount 最大票金额
	 * @param lotteryChildCode 子彩种ID
	 * @param subNum 切成N条为一张票
	 * @param contentType 票的投注方式   1：单式，2：复式，3：胆拖，6：和值
	 * @param prize 每一注的本金
	 * @author wuLong
	 * @date 2017年12月2日 下午12:03:58
	 */
	protected static void assembleSingleData(OrderInfoBO orderInfoBO,List<TicketInfoBO> ticketInfoBOs,
			Map<String, List<String>> fcHzMap,Integer splitMaxNum,
			Integer splitMaxAmount,Integer subNum,Integer contentType,Integer prize,List<CooperateCdkeyBO> cdkeyBOs){
		if(!ObjectUtil.isBlank(fcHzMap)){
			int i = 0;
			for(String k : fcHzMap.keySet()){
				List<String> list = fcHzMap.get(k);
				String[] j = k.split(SymbolConstants.COMMA);
				int multiple = Integer.valueOf(j[1]);
				int detailId = Integer.valueOf(j[2]);
				int lotteryChildCode = Integer.valueOf(j[0]);
				List<List<String>> lists = SplitTicketUtil.subList(subNum, list);
				for(List<String> ls : lists){
					int buyNumber = ls.size();
					int maxNum = splitMaxAmount/buyNumber/2;
					if(maxNum>splitMaxNum){
						maxNum = splitMaxNum;
					}
					String planContent = StringUtils.join(ls,SymbolConstants.SEMICOLON);
					List<String> res = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, planContent, maxNum);
					for(String p : res){
						String[] m= p.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
						TicketInfoBO ticketInfoBO = new TicketInfoBO();
						ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO, contentType, lotteryChildCode, ticketInfoBO);
						ticketInfoBO.setTicketContent(m[0]);
						ticketInfoBO.setLottoAdd(orderInfoBO.getLottoAdd());
						ticketInfoBO.setMultipleNum(Integer.valueOf(m[1]));
						ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(prize * ticketInfoBO.getMultipleNum() * buyNumber)));
						ticketInfoBO.setEndTicketTime(orderInfoBO.getEndTicketTime());
						ticketInfoBO.setOrderDetailId(detailId);
						ticketInfoBO.setRedeemCode(cdkeyBOs.get(i).getLottoCdkey());
						ticketInfoBO.setChannelId(cdkeyBOs.get(i).getTicketChannel());
						ticketInfoBOs.add(ticketInfoBO);
						i++;
					}
				}
			}
		}
	}
	
	public static void assembleSingleData(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, List<String> singleList,Integer multipleNum,OrderDetailBO orderDetailBO) throws ServiceRuntimeException {
        List<String> resultSingles = new ArrayList<>();
        SplitTicketUtil.joinToOneTicket(resultSingles,singleList,SplitTicketUtil.SINGLE_TICKET_ZHUSHU);
        int prize = 2;
        if(orderInfoBO.getLottoAdd() == 1){
        	prize = 3;
        }
        for(String a : resultSingles){
            TicketInfoBO ticketInfoBO = new TicketInfoBO();
            ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO,orderDetailBO,ticketInfoBO);
            ticketInfoBO.setContentType(1);
            ticketInfoBO.setTicketContent(a);
            ticketInfoBO.setMultipleNum(multipleNum);
            String[] b = a.split("\\;");
            ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(prize * multipleNum * b.length)));
            ticketInfoBO.setEndTicketTime(orderInfoBO.getEndCheckTime());
            ticketInfoBO.setLottoAdd(orderInfoBO.getLottoAdd());
            ticketInfoBO.setOrderDetailId(orderDetailBO.getId());
            ticketInfoBOs.add(ticketInfoBO);
        }
    }
	
	protected static void assembleDanTiaoData(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,String a,OrderDetailBO orderDetailBO,int buyNumber) throws ServiceRuntimeException {
        TicketInfoBO ticketInfoBO = new TicketInfoBO();
        String[] b =  a.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
        ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO,orderDetailBO,ticketInfoBO);
        ticketInfoBO.setTicketContent(b[0]);
        ticketInfoBO.setMultipleNum(Integer.valueOf(b[1]));
        int prize = 2;
        if(orderInfoBO.getLottoAdd() == 1){//大乐透追加则3元一注
        	prize = 3;
        }
        ticketInfoBO.setLottoAdd(orderInfoBO.getLottoAdd());
        ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(prize * ticketInfoBO.getMultipleNum() * buyNumber)));
        ticketInfoBO.setEndTicketTime(orderInfoBO.getEndTicketTime());
        ticketInfoBO.setOrderDetailId(orderDetailBO.getId());
        ticketInfoBOs.add(ticketInfoBO);
    }
	
	protected static void assembleNoSingleData(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, List<String> noSingleList,OrderDetailBO orderDetailBO) throws ServiceRuntimeException {
        for(String a : noSingleList){
            assembleDanTiaoData(orderInfoBO,ticketInfoBOs,a,orderDetailBO,(ObjectUtil.isBlank(orderDetailBO.getBuyNumber())?1:orderDetailBO.getBuyNumber()));
        }
    }
}
