package com.hhly.lottosplit.service.ticket.numberhighlottery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
 * @ClassName: SyxwPc 
 * @Description: 11选5
 * @author wuLong
 * @date 2017年6月12日 下午2:47:14 
 *
 */
public abstract class SyxwPc extends AbstractCSplitHanlde implements AbstractINumberSplit{
	
	@SuppressWarnings("unchecked")
	@Override
	public void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs)
			throws ServiceRuntimeException {
		List<OrderDetailBO> detailBOs = orderInfoBO.getDetailBOs();
		Map<String, List<String>> mapList = new HashMap<>();
		int splitMaxNum = lotteryTypeBO.getSplitMaxNum();
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		Combine combine = new Combine();
		for (OrderDetailBO orderDetailBO : detailBOs) {
			int contentType = orderDetailBO.getContentType();
			//山东十一选五前二直选，直三选需做去重处理
			int multiple = orderInfoBO.getMultipleNum()*orderDetailBO.getMultiple();
			int lotteryChildCode = orderDetailBO.getLotteryChildCode();
			int detailId = orderDetailBO.getId();
			String key = lotteryChildCode+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+detailId;
			if(orderDetailBO.getBuyNumber() == 1
					&&orderDetailBO.getLotteryChildCode()!=LotteryChildEnum.LotteryChild.SD11X5_L3.getValue()
					&&orderDetailBO.getLotteryChildCode()!=LotteryChildEnum.LotteryChild.SD11X5_L4.getValue()
					&&orderDetailBO.getLotteryChildCode()!=LotteryChildEnum.LotteryChild.SD11X5_L5.getValue()
					&&orderDetailBO.getLotteryChildCode()!=LotteryChildEnum.LotteryChild.XJ11X5_L3.getValue()
					&&orderDetailBO.getLotteryChildCode()!=LotteryChildEnum.LotteryChild.XJ11X5_L4.getValue()
					&&orderDetailBO.getLotteryChildCode()!=LotteryChildEnum.LotteryChild.XJ11X5_L5.getValue()
					&&orderDetailBO.getLotteryChildCode()!=LotteryChildEnum.LotteryChild.GX11X5_L3.getValue()
					&&orderDetailBO.getLotteryChildCode()!=LotteryChildEnum.LotteryChild.GX11X5_L4.getValue()
					&&orderDetailBO.getLotteryChildCode()!=LotteryChildEnum.LotteryChild.GX11X5_L5.getValue()){
				orderDetailBO.setPlanContent(orderDetailBO.getPlanContent().replaceAll(SymbolConstants.NUMBER_SIGN, SymbolConstants.COMMA));
			}
			if(orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.SD11X5_Q2_DIRECT.getValue()||
					orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.SD11X5_Q3_DIRECT.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.D11X5_Q2_DIRECT.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.D11X5_Q3_DIRECT.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.JX11X5_Q2_DIRECT.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.JX11X5_Q3_DIRECT.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.XJ11X5_Q2_DIRECT.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.XJ11X5_Q3_DIRECT.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.GX11X5_Q2_DIRECT.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.GX11X5_Q3_DIRECT.getValue()
					){
				boolean isRepeat = SplitTicketUtil.isRepeat(orderDetailBO.getPlanContent(), lotteryChildCode);
				if(isRepeat){
					List<String> list = SplitTicketUtil.getSyxwZx(orderDetailBO.getPlanContent(), orderDetailBO.getLotteryChildCode());
					List<List<String>> lists = SplitTicketUtil.subList(5, list);
					for(List<String> c : lists){
						int maxNum = splitMaxAmount/c.size()/2;
						if(maxNum>splitMaxNum){
							maxNum = splitMaxNum;
						}
						List<String> d = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, StringUtils.join(c,SymbolConstants.SEMICOLON), maxNum);
						for(String e : d){
							orderDetailBO.setContentType(1);
							assembleDanTiaoData(orderInfoBO, ticketInfoBOs, e+SymbolConstants.UP_CAP+detailId, orderDetailBO,e.split(SymbolConstants.SEMICOLON).length);
						}
					}
				}else{
					singleHandle(orderInfoBO, ticketInfoBOs, mapList, splitMaxNum, splitMaxAmount, orderDetailBO, multiple,key,combine);
				}
			}else if(orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.SD11X5_L3.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.XJ11X5_L3.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.GX11X5_L3.getValue()){
				/*[复式]05|05,06|07
				[复式]05,06|05,06|04,07*/
				String pc[] = orderDetailBO.getPlanContent().split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
				String a[] = pc[0].split(SymbolConstants.COMMA);
				String b[] = pc[1].split(SymbolConstants.COMMA);
				String c[] = pc[2].split(SymbolConstants.COMMA);
				Map<String, List<String>> map = new HashMap<>();
				List<String> singleList = new ArrayList<>();
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
				if(!ObjectUtil.isBlank(singleList)){
					map.put(key, singleList);
				}
				assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE, 6);
			}else if(orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.SD11X5_L4.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.SD11X5_L5.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.XJ11X5_L4.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.XJ11X5_L5.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.GX11X5_L4.getValue()
					||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.GX11X5_L5.getValue()){
				int prize = 0;
				int len = 0;
				if(orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.SD11X5_L4.getValue()
						||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.XJ11X5_L4.getValue()
						||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.GX11X5_L4.getValue()){
					len = 4;
					prize = 10;
				}else{
					len = 5;
					prize = 14;
				}
				Map<String, List<String>> map = new HashMap<>();
				/*[复式]05,06,07,08,09,10
				[胆拖]04,05,06,07#08,09*/
				List<String> singleList = new ArrayList<>();
				if(SplitTicketUtil.NUM_TWO == contentType){
					String[] pcz = orderDetailBO.getPlanContent().split(SymbolConstants.COMMA);
					List<String> listCombine = combine.mn(pcz, len);
					for(String l : listCombine){
						singleList.add(l.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA));
					}
				}else if(SplitTicketUtil.NUM_THREE== contentType){
					String[] pcz = orderDetailBO.getPlanContent().split(SymbolConstants.NUMBER_SIGN);
					String dans = pcz[0];
					String tuos[] = pcz[1].split(SymbolConstants.COMMA);
					List<String> listCombine = combine.mn(tuos, len-dans.split(SymbolConstants.COMMA).length);
					for(String l : listCombine){
						singleList.add(dans+SymbolConstants.COMMA+(l.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA)));
					}
				}else{
					//单注的真实倍数  =  详情倍数*方案总倍数
					//单式处理
					if(map.containsKey(key)){
						singleList = map.get(key);
					}
					singleList.add(orderDetailBO.getPlanContent());
				}
				if(!ObjectUtil.isBlank(singleList)){
					map.put(key, singleList);
				}
				assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE, prize);
			}
			//任8复式拆成单式
			else if(orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.D11X5_R8.getValue()&&SplitTicketUtil.NUM_TWO == contentType){
				String[] pcz = orderDetailBO.getPlanContent().split(SymbolConstants.COMMA);
				List<String> listCombine = combine.mn(pcz, 8);
				List<String> singleList = new ArrayList<>();
				for(String l : listCombine){
					singleList.add(l.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA));
				}
				Map<String, List<String>> map = new HashMap<>();
				if(!ObjectUtil.isBlank(singleList)){
					map.put(key, singleList);
				}
				assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE, 2);
			}//任8胆拖拆单式
			else if(orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.D11X5_R8.getValue()&&SplitTicketUtil.NUM_THREE == contentType){
				String a[] = orderDetailBO.getPlanContent().split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN);
				int lenDan = a[0].split(SymbolConstants.COMMA).length;
				int lenTuo = 8 - lenDan;
				List<String> list = combine.mn(a[1].split(SymbolConstants.COMMA), lenTuo);
				List<String> singleList = new ArrayList<>();
				for(String b : list){
					String con = a[0]+SymbolConstants.COMMA+b.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA);
					String cm[] = con.split(SymbolConstants.COMMA);
					List<String> cml = Arrays.asList(cm);
					Collections.sort(cml);
					singleList.add(StringUtils.join(cml.toArray(new String[cml.size()]), SymbolConstants.COMMA));
				}
				Map<String, List<String>> map = new HashMap<>();
				if(!ObjectUtil.isBlank(singleList)){
					map.put(key, singleList);
				}
				assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE, 2);
			}else{
				//单注的真实倍数  =  详情倍数*方案总倍数
				//单式处理
				singleHandle(orderInfoBO, ticketInfoBOs, mapList, splitMaxNum, splitMaxAmount, orderDetailBO, multiple,key,combine);
			}
		}
		if(!ObjectUtil.isBlank(mapList)){
			assembleSingleData(orderInfoBO, ticketInfoBOs, mapList, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_FIVE, SplitTicketUtil.NUM_ONE, 2);
			mapList = null;
		}
	}

	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param mapList
	 * @param splitMaxNum
	 * @param splitMaxAmount
	 * @param orderDetailBO
	 * @param multiple
	 * @param key
	 * @throws NumberFormatException
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年6月13日 下午5:43:01
	 */
	@SuppressWarnings("unchecked")
	private void singleHandle(OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			Map<String, List<String>> mapList, int splitMaxNum, int splitMaxAmount, OrderDetailBO orderDetailBO,
			int multiple, String key,Combine combine) throws NumberFormatException, ServiceRuntimeException {
		int contentType = orderDetailBO.getContentType();
		List<String> singleList = new ArrayList<>();
		if (SplitTicketUtil.NUM_ONE == orderDetailBO.getContentType() || orderDetailBO.getBuyNumber()==1) {
			if(mapList.containsKey(key)){
				singleList = mapList.get(key);
			}
			singleList.add(orderDetailBO.getPlanContent());
			mapList.put(key, singleList);
		} else if(!(SplitTicketUtil.NUM_ONE == orderDetailBO.getContentType() 
				|| orderDetailBO.getBuyNumber()==1)
				&&(orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.SD11X5_R8.getValue()
				||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.D11X5_R8.getValue()
				||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.JX11X5_R8.getValue()
				||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.XJ11X5_R8.getValue()
				||orderDetailBO.getLotteryChildCode()==LotteryChildEnum.LotteryChild.GX11X5_R8.getValue())){
			if(mapList.containsKey(key)){
				singleList = mapList.get(key);
			}
			if(SplitTicketUtil.NUM_TWO == contentType){
				String a[] = orderDetailBO.getPlanContent().split(SymbolConstants.COMMA);
				List<String> list = combine.mn(a, 8);
				for(String c : list){
					singleList.add(c.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA));
				}
			}else if(SplitTicketUtil.NUM_THREE== contentType){
				String a[] = orderDetailBO.getPlanContent().split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN);
				int lenDan = a[0].split(SymbolConstants.COMMA).length;
				int lenTuo = 8 - lenDan;
				List<String> list = combine.mn(a[1].split(SymbolConstants.COMMA), lenTuo);
				for(String b : list){
					String con = a[0]+SymbolConstants.COMMA+b.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA);
					String cm[] = con.split(SymbolConstants.COMMA);
					List<String> cml = Arrays.asList(cm);
					Collections.sort(cml);
					singleList.add(StringUtils.join(cml.toArray(new String[cml.size()]), SymbolConstants.COMMA));
				}
			}
		}else{
			assemble(orderInfoBO, ticketInfoBOs, splitMaxNum, splitMaxAmount, orderDetailBO, multiple);
		}
		if(!ObjectUtil.isBlank(singleList)){
			mapList.put(key, singleList);
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
