package com.hhly.lottosplit.service.ticket.numberhighlottery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.hhly.lottosplit.bo.CooperateCdkeyBO;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryEnum;
import com.hhly.lottosplit.enums.OrderEnum.BetContentType;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.service.ticket.AbstractCSplitHanlde;
import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.BaseLottery;
import com.hhly.lottosplit.utils.calcutils.Combine;

public abstract class SsqDltPc extends AbstractCSplitHanlde implements AbstractINumberSplit{
	/** 
	 * @Description: 大乐透双色球拆票并封装票信息 
	 * @param orderInfoBO 订单信息
	 * @param lotteryTypeBO 彩种信息
	 * @param ticketInfoBOs list返回封装的票
	 * @param lottery 彩种彩期枚举类
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年6月8日 下午4:13:47
	 */
	protected static void handleDltSsq(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryEnum.Lottery lottery,List<CooperateCdkeyBO> cdkeyBOs) throws ServiceRuntimeException {
		List<OrderDetailBO> orderDetailBOs = orderInfoBO.getDetailBOs();
		Map<String, List<String>> mapList = new HashMap<>();
		int prize = 2;
		if(orderInfoBO.getLottoAdd() == 1){
			prize = 3;
		}
		for (OrderDetailBO orderDetailBO : orderDetailBOs) {
			int multiple = orderInfoBO.getMultipleNum()*orderDetailBO.getMultiple();
			int lotteryChildCode = orderDetailBO.getLotteryChildCode();
			int detailId = orderDetailBO.getId();
			String key = lotteryChildCode+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+detailId;
			//1.单式   情况1：倍数为1，情况2：倍数大于1
			//2.复式   当倍数大于99倍  需拆成多张票
			//3.单张票总金额不能超过20000
			String planContent = orderDetailBO.getPlanContent();
			//01,02,03,04,05,06+07
			planContent = planContent.replace(SymbolConstants.VERTICAL_BAR, SymbolConstants.ADD);
			Combine combine = new Combine();
			BetContentType betContentType = BetContentType.getContentType(orderDetailBO.getContentType());
			if (betContentType == BetContentType.SINGLE) {//单式处理
				List<String> singleList = new ArrayList<>();
				if(mapList.containsKey(key)){
					singleList = mapList.get(key);
				}
				singleList.add(planContent);
				mapList.put(key, singleList);
			}else if(betContentType == BetContentType.MULTIPLE){//复式处理
				List<String> singleList = new ArrayList<>();
				getCompoundHandle(orderInfoBO, ticketInfoBOs, singleList, orderDetailBO, planContent, combine,lotteryTypeBO);
				if(!ObjectUtil.isBlank(singleList)){
					if(!mapList.containsKey(key)){
						mapList.put(key, singleList);
					}else{
						List<String> singlel = mapList.get(key);
						singlel.addAll(singleList);
						mapList.put(key, singlel);
					}
				}
			}else if(betContentType == BetContentType.DANTUO){//胆拖处理
				List<String> singleList = new ArrayList<>();
				getDanTuoHandle(orderInfoBO, ticketInfoBOs, singleList, orderDetailBO, planContent, combine,lotteryTypeBO);
				if(!ObjectUtil.isBlank(singleList)){
					if(!mapList.containsKey(key)){
						mapList.put(key, singleList);
					}else{
						List<String> singlel = mapList.get(key);
						singlel.addAll(singleList);
						mapList.put(key, singlel);
					}
				}
			}
		}
		
		if(!ObjectUtil.isBlank(mapList)){
			if(cdkeyBOs!=null){
				assembleSingleData(orderInfoBO, ticketInfoBOs, mapList, lotteryTypeBO.getSplitMaxNum(),lotteryTypeBO.getSplitMaxAmount(),
						SplitTicketUtil.NUM_ONE,SplitTicketUtil.NUM_ONE,prize,cdkeyBOs);
				mapList = null;
			}else{
				assembleSingleData(orderInfoBO, ticketInfoBOs, mapList, lotteryTypeBO.getSplitMaxNum(),lotteryTypeBO.getSplitMaxAmount(),
						SplitTicketUtil.NUM_FIVE,SplitTicketUtil.NUM_ONE,prize);
				mapList = null;
			}
		}
	}
	
	
    /**
     * 复式处理
     * @param orderInfoBO
     * @param ticketInfoBOs
     * @param singleList
     * @param detailVO
     * @param planContent
     * @param combine
     */
    private static void getCompoundHandle(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, List<String> singleList, OrderDetailBO detailVO, String planContent, Combine combine,LotteryTypeBO lotteryTypeBO) throws ServiceRuntimeException {
        //单注的真实倍数  =  详情倍数*方案总倍数
        int multiple = orderInfoBO.getMultipleNum()*detailVO.getMultiple();
        double amount = detailVO.getAmount();
        int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
        int maxNum = lotteryTypeBO.getSplitMaxNum();
        int prize = 2;
        if(orderInfoBO.getLottoAdd() == 1){
        	prize = 3;
        }
		if(!ObjectUtil.isBlank(detailVO.getBuyNumber())&&detailVO.getBuyNumber()>1){
			maxNum = lotteryTypeBO.getSplitMaxAmount()/detailVO.getBuyNumber()/prize;
			if(maxNum>lotteryTypeBO.getSplitMaxNum()){
				maxNum = lotteryTypeBO.getSplitMaxNum();
			}
		}
        if(amount > splitMaxAmount){//金额>20000 直接拆成单注
			if(maxNum > 0){
				List<String> list = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple,planContent,maxNum);
				assembleNoSingleData(orderInfoBO,ticketInfoBOs,list,detailVO);
			}else{
				String[] pc = planContent.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.ADD);
				String[] tuos = pc[SplitTicketUtil.NUM_ZERO].split(SymbolConstants.COMMA);
				String[] dans = pc[SplitTicketUtil.NUM_ONE].split(SymbolConstants.COMMA);
				LotteryEnum.Lottery lottery = LotteryEnum.Lottery.getLottery(orderInfoBO.getLotteryCode());
				int tuoz = SplitTicketUtil.NUM_SIX,danz = SplitTicketUtil.NUM_ONE;
				if(lottery == LotteryEnum.Lottery.DLT){
					tuoz = SplitTicketUtil.NUM_FIVE;danz = SplitTicketUtil.NUM_TWO;
				}
				@SuppressWarnings("unchecked")
				List<String> tuoList = combine.mn(tuos, tuoz);
				@SuppressWarnings("unchecked")
				List<String> danList = combine.mn(dans, danz);
				String[] tuoLists = StringUtils.join(tuoList,SymbolConstants.NUMBER_SIGN).split(SymbolConstants.NUMBER_SIGN);
				String[] danLists = StringUtils.join(danList,SymbolConstants.NUMBER_SIGN).split(SymbolConstants.NUMBER_SIGN);
				String[][] td = new String[SplitTicketUtil.NUM_TWO][];
				td[SplitTicketUtil.NUM_ZERO] = tuoLists;
				td[SplitTicketUtil.NUM_ONE] = danLists;
				BaseLottery baseLottery = new BaseLottery();
				//  1/2/3/4/5/6+9/10$1/2/3/4/5/6+9/11$1/2/3/4/5/6+10/11
				String rs = baseLottery.getCombineArrToStr_plus(td).replace(SymbolConstants.OBLIQUE_LINE,SymbolConstants.COMMA);
				singleList.addAll(Arrays.asList(rs.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.DOLLAR)));
			}
        }else{//金额<=20000 && 倍数> 99 直接拆成99倍的多张票
            List<String> list = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple,planContent,maxNum);
            assembleNoSingleData(orderInfoBO,ticketInfoBOs,list,detailVO);
        }
    }
    
    /**
     * 胆拖
     * @param orderInfoBO
     * @param ticketInfoBOs
     * @param singleList
     * @param detailVO
     * @param planContent
     * @param combine
     */
    private static void getDanTuoHandle(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, List<String> singleList, OrderDetailBO detailVO, String planContent, Combine combine,LotteryTypeBO lotteryTypeBO) throws ServiceRuntimeException {
    	int multiple = orderInfoBO.getMultipleNum()*detailVO.getMultiple();
    	double amount = detailVO.getAmount();
    	int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
    	if(amount>splitMaxAmount||detailVO.getContentType()==3){
    		int redNum = 6;
    		int blueNum = 1;
    		if(orderInfoBO.getLotteryCode() == LotteryEnum.Lottery.DLT.getName()){
    			redNum = 5;
    			blueNum = 2;
    		}
    		String[] pc = planContent.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.ADD);
    		if(detailVO.getBuyNumber()==1){
    			planContent = planContent.replaceAll(SymbolConstants.NUMBER_SIGN, SymbolConstants.COMMA);
    			detailVO.setContentType(1);
    			singleList.add(planContent);
    		}else{
    			int renLen = 0,blueLen = 0;
    			if(planContent.indexOf(SymbolConstants.NUMBER_SIGN)!=-1){
    				if(pc[0].indexOf(SymbolConstants.NUMBER_SIGN)!=-1){
    					String[] reds = pc[0].split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN);
    					renLen = reds[0].split(SymbolConstants.COMMA).length+reds[1].split(SymbolConstants.COMMA).length;
    				}else{
    					renLen = pc[0].split(SymbolConstants.COMMA).length;
    				}
    				if(pc[1].indexOf(SymbolConstants.NUMBER_SIGN)!=-1){
    					String[] reds = pc[1].split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN);
    					blueLen = reds[0].split(SymbolConstants.COMMA).length+reds[1].split(SymbolConstants.COMMA).length;
    				}else{
    					blueLen = pc[1].split(SymbolConstants.COMMA).length;
    				}
    				if(renLen == redNum){
    					pc[0] = pc[0].replaceAll(SymbolConstants.NUMBER_SIGN, SymbolConstants.COMMA);
    				}
    				if(blueLen == blueNum){
    					pc[1] = pc[1].replaceAll(SymbolConstants.NUMBER_SIGN, SymbolConstants.COMMA);
    				}
    			}
    			if(pc[0].indexOf(SymbolConstants.NUMBER_SIGN)<0&&pc[1].indexOf(SymbolConstants.NUMBER_SIGN)<0&&renLen == redNum&&blueLen == blueNum){
    				detailVO.setContentType(1);
    				detailVO.setPlanContent(pc[0]+"+"+pc[1]);
    				planContent = pc[0]+"+"+pc[1];
    				singleList.add(planContent);
    			}else{
    				//19,27,28,33#13,14,20,21,26|04,05,13,14
    				List<String> redDanTuoList = getDantuo(combine, redNum, pc[0]);
    				List<String> blueDanTuoList = getDantuo(combine, blueNum, pc[1]);
    				String[][] td = new String[SplitTicketUtil.NUM_TWO][];
    				td[SplitTicketUtil.NUM_ZERO] = redDanTuoList.toArray(new String[redDanTuoList.size()]);
    				td[SplitTicketUtil.NUM_ONE] = blueDanTuoList.toArray(new String[blueDanTuoList.size()]);
    				BaseLottery baseLottery = new BaseLottery();
    				//  1/2/3/4/5/6+9/10$1/2/3/4/5/6+9/11$1/2/3/4/5/6+10/11
    				String rs = baseLottery.getCombineArrToStr_plus(td).replace(SymbolConstants.OBLIQUE_LINE,SymbolConstants.COMMA);
    				String[] rss = rs.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.DOLLAR);
    				if(multiple > lotteryTypeBO.getSplitMaxNum()){//大于99倍，单注要根据99倍拆成多张票
    					for(String r : rss){
    						List<String> list = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple,r,lotteryTypeBO.getSplitMaxNum());
    						detailVO.setBuyNumber(1);
    						detailVO.setContentType(1);
    						assembleNoSingleData(orderInfoBO,ticketInfoBOs,list,detailVO);
    					}
    				}else if(multiple < 20){//小于20倍，则可以5注放在一张票上出
    					List<String> list = Arrays.asList(rss);
    					assembleSingleData(orderInfoBO, ticketInfoBOs, list,multiple,detailVO);
    				}else{//如果是在>=20&&<=99则单注为一张票
    					for(String r : rss){
    						detailVO.setBuyNumber(1);
    						detailVO.setContentType(1);
    						assembleDanTiaoData(orderInfoBO,ticketInfoBOs,r+SymbolConstants.UP_CAP+multiple,detailVO,ObjectUtil.isBlank(detailVO.getBuyNumber())?1:detailVO.getBuyNumber());
    					}
    				}
    			}
    		}
    	}else{//金额<=20000 && 倍数> 99 直接拆成99倍的多张票
    		int prize = 2;
    		if(orderInfoBO.getLottoAdd() == 1){
    			prize = 3;
    		}
    		int maxNum = splitMaxAmount/detailVO.getBuyNumber()/prize;
    		if(maxNum>lotteryTypeBO.getSplitMaxNum()){
    			maxNum = lotteryTypeBO.getSplitMaxNum();
    		}
    		List<String> list = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple,planContent,maxNum);
    		assembleNoSingleData(orderInfoBO,ticketInfoBOs,list,detailVO);
    	}
    }

	/** 
	 * @Description: 一个原装胆拖，分成多个大的复式集合
	 * @param combine
	 * @param redNum
	 * @param pc
	 * @return
	 * @author wuLong
	 * @date 2017年6月29日 下午6:58:00
	 */
	private static List<String> getDantuo(Combine combine, int redNum, String pc) {
		//红球胆
		String[] reds = pc.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN);
		String redDans = "";
		String[] redTuos  = null;
		int lenRedDan = 0;
		if(reds.length>1){
			redDans = reds[0].replace(SymbolConstants.COMMA,SymbolConstants.OBLIQUE_LINE);
			redTuos = reds[1].split(SymbolConstants.COMMA);
			lenRedDan = redDans.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.OBLIQUE_LINE).length;
		}else{
			redTuos = reds[0].split(SymbolConstants.COMMA);
		}
		//红球拖
		@SuppressWarnings("unchecked")
		List<String> redTuoList = combine.mn(redTuos, redNum-lenRedDan);
		List<String> redDanTuoList = new ArrayList<>();
		for(String tl : redTuoList){
			if("".equals(redDans)){
				redDanTuoList.add(tl);
			}else{
				redDanTuoList.add(redDans+SymbolConstants.OBLIQUE_LINE+tl);
			}
		}
		return redDanTuoList;
	}
}
