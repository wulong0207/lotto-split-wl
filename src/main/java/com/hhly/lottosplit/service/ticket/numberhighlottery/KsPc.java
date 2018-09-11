package com.hhly.lottosplit.service.ticket.numberhighlottery;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.hhly.lottosplit.enums.LotteryChildEnum.LotteryChild;
import com.hhly.lottosplit.enums.LotteryEnum;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.service.ticket.AbstractCSplitHanlde;
import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.Combine;
/**
 * 
 * @ClassName: KsPc 
 * @Description: 快三拆票抽象类
 * @author wuLong
 * @date 2017年11月3日 上午10:20:27 
 *
 */
public abstract class KsPc extends AbstractCSplitHanlde implements AbstractINumberSplit{
	
	/**票表格式转换map**/
	public static Map<String,String> FORMAT_TRANSFORM;
	public static Map<String, String> HZ_MAP;
	static{
		FORMAT_TRANSFORM = new HashMap<>();
		{
			//三同号通选
			FORMAT_TRANSFORM.put("3T", "A,A,A");
			//三连号通选
			FORMAT_TRANSFORM.put("3L", "A,B,C");
		}
		HZ_MAP = new HashMap<>();
		{
			HZ_MAP.put("3", "111");
			HZ_MAP.put("18", "666");
		}
	}

	@Override
	public void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs)
			throws ServiceRuntimeException {
		handleks(orderInfoBO, lotteryTypeBO, ticketInfoBOs);
	}

	/** 
	 * @Description: 江苏快3拆票并封装票信息 
	 * @param orderInfoBO 订单信息
	 * @param lotteryTypeBO 彩种信息
	 * @param ticketInfoBOs list返回封装的票
	 * @author wuLong
	 * @date 2017年6月8日 下午5:41:16
	 */
	@SuppressWarnings("unchecked")
	private static void handleks(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs) {
		List<OrderDetailBO> orderDetailBOs = orderInfoBO.getDetailBOs();
		Map<String, List<String>> map = new HashMap<>();
		Combine combine = new Combine();
		Integer splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		Integer splitMaxNum = lotteryTypeBO.getSplitMaxNum();
		int prize = 2;
		for (OrderDetailBO detailBO : orderDetailBOs) {
			int contentType = detailBO.getContentType();
			String planCotent = detailBO.getPlanContent();
			int multiple = orderInfoBO.getMultipleNum()*detailBO.getMultiple();
			int lotteryChildCode = detailBO.getLotteryChildCode();
			int detailId = detailBO.getId();
			String key = lotteryChildCode+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+detailId;
			List<String> list = new ArrayList<>();
			if(map.containsKey(key)){
				list = map.get(key);
			}
			//复式
			if(contentType == SplitTicketUtil.NUM_TWO){
				LotteryChild lotteryChild = LotteryChild.valueOf(lotteryChildCode);
				switch (lotteryChild) {
					case JSK3_S:
					case JXK3_S:
						String pcd[] = planCotent.split(SymbolConstants.COMMA);
						for(String p : pcd){
							if(!ObjectUtil.isBlank(HZ_MAP.get(p))){
								sumThreeSingle(orderInfoBO, lotteryTypeBO, ticketInfoBOs, multiple, detailId, p);
							}else{
//								assembleFcHzSingleData(orderInfoBO, ticketInfoBOs, p.split(SymbolConstants.COMMA), multiple, lotteryChildCode, lotteryTypeBO,detailId);
								Map<String, List<String>> mapList = new HashMap<>();
								mapList.put(key, Arrays.asList(p.split(SymbolConstants.COMMA)));
								assembleSingleData(orderInfoBO, ticketInfoBOs, mapList, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE,prize);
							}
						}
						break;
					case JSK3_TD3:
					case JXK3_TD3:
					case JSK3_TF2:
					case JXK3_TF2:
						String[] pc = planCotent.split(SymbolConstants.COMMA);
						list.addAll(Arrays.asList(pc));
						break;
					case JSK3_TD2:
					case JXK3_TD2:
						pc = planCotent.split(SymbolConstants.NUMBER_SIGN);
						String d[] = pc[0].split(SymbolConstants.COMMA);
						String t[] = pc[1].split(SymbolConstants.COMMA);
						for(String a : d){
							char b[] = a.toCharArray();
							String v = StringUtils.join(b, ',');
							for(String c : t){
								if(a.contains(c)){
									continue;
								}
								list.add(v+SymbolConstants.COMMA+c);
							}
						}
						break;
					case JSK3_BT3:
					case JXK3_BT3:
						String[] jskbt3 = planCotent.split(SymbolConstants.COMMA);
						List<String> listCombine = combine.mn(jskbt3, 3);
						for(String l : listCombine){
							list.add(l.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA));
						}
						break;
					case JSK3_BT2:
					case JXK3_BT2:
						String[] jskbt2 = planCotent.split(SymbolConstants.COMMA);
						listCombine = combine.mn(jskbt2, 2);
						for(String l : listCombine){
							String bb[] = l.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.OBLIQUE_LINE);
							if(Integer.valueOf(bb[0])>Integer.valueOf(bb[1])){
								list.add(bb[1]+SymbolConstants.COMMA+bb[0]);
							}else if(Integer.valueOf(bb[0])<Integer.valueOf(bb[1])){
								list.add(bb[0]+SymbolConstants.COMMA+bb[1]);
							}else{
								list.add(l.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA));
							}
						}
						break;
					default:
						break;
				}
			}
			//单式
			else if(contentType == SplitTicketUtil.NUM_ONE){
				if(lotteryChildCode == LotteryChildEnum.LotteryChild.JSK3_TD2.getValue()||lotteryChildCode == LotteryChildEnum.LotteryChild.JXK3_TD2.getValue()){
					//11,22#3,4
					String[] pc = planCotent.split(SymbolConstants.NUMBER_SIGN);
					String d[] = pc[0].split(SymbolConstants.COMMA);
					String t[] = pc[1].split(SymbolConstants.COMMA);
					for(String a : d){
						char b[] = a.toCharArray();
						String v = StringUtils.join(b, ',');
						for(String c : t){
							if(a.contains(c)){
								continue;
							}
							list.add(v+SymbolConstants.COMMA+c);
						}
					}
				}else if(lotteryChildCode == LotteryChildEnum.LotteryChild.JSK3_S.getValue()||lotteryChildCode == LotteryChildEnum.LotteryChild.JXK3_S.getValue()){
					if(!ObjectUtil.isBlank(HZ_MAP.get(planCotent))){
						sumThreeSingle(orderInfoBO, lotteryTypeBO, ticketInfoBOs, multiple, detailId, planCotent);
					}else{
//						assembleFcHzSingleData(orderInfoBO, ticketInfoBOs, planCotent.split(SymbolConstants.COMMA), multiple, lotteryChildCode, lotteryTypeBO,detailId);
						Map<String, List<String>> mapList = new HashMap<>();
						mapList.put(key, Arrays.asList(planCotent.split(SymbolConstants.COMMA)));
						assembleSingleData(orderInfoBO, ticketInfoBOs, mapList, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE,prize);
					}
				}else{
					if(FORMAT_TRANSFORM.containsKey(planCotent)){
						list.add(FORMAT_TRANSFORM.get(planCotent));
					}else{
						list.add(planCotent);
					}
				}
			}
			//胆拖
			else if(contentType == SplitTicketUtil.NUM_THREE){
				String[] pc = planCotent.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN);
				String dan = pc[0];
				int danlen = dan.split(SymbolConstants.COMMA).length;
				LotteryChild lotteryChild = LotteryChild.valueOf(lotteryChildCode);
				List<String> listCombine = new ArrayList<>();
				switch (lotteryChild) {
					case JSK3_BT3:
					case JXK3_BT3:
						String[] jskbt3 = pc[1].split(SymbolConstants.COMMA);
						listCombine = combine.mn(jskbt3, 3-danlen);
						break;
					case JSK3_BT2:
					case JXK3_BT2:
						String[] jskbt2 = pc[1].split(SymbolConstants.COMMA);
						listCombine = combine.mn(jskbt2, 2-danlen);
						break;
				}
				for(String t : listCombine){
					String a = dan.concat(SymbolConstants.COMMA).concat(t.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA));
					String b[] = a.split(SymbolConstants.COMMA);
					Arrays.sort(b);
					list.add(StringUtils.join(b, SymbolConstants.COMMA));
				}
			}
			//和值
			else if(contentType == SplitTicketUtil.NUM_SIX){
				String pcd[] = planCotent.split(SymbolConstants.COMMA);
				for(String p : pcd){
					if(!ObjectUtil.isBlank(HZ_MAP.get(p))){
						sumThreeSingle(orderInfoBO, lotteryTypeBO, ticketInfoBOs, multiple, detailId, p);
					}else{
//						assembleFcHzSingleData(orderInfoBO, ticketInfoBOs, p.split(SymbolConstants.COMMA), multiple, lotteryChildCode, lotteryTypeBO,detailId);
						Map<String, List<String>> mapList = new HashMap<>();
						mapList.put(key, Arrays.asList(p.split(SymbolConstants.COMMA)));
						assembleSingleData(orderInfoBO, ticketInfoBOs, mapList, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE,prize);
					}
				}
			}
			map.put(key, list);
		}
		if(!ObjectUtil.isBlank(map)){
//			assembleksSingleData(orderInfoBO, ticketInfoBOs, map, lotteryTypeBO);
			assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_FIVE, SplitTicketUtil.NUM_ONE,prize);
		}
	}

	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param orderInfoBO
	 * @param lotteryTypeBO
	 * @param ticketInfoBOs
	 * @param multiple
	 * @param detailId
	 * @param p
	 * @author wuLong
	 * @date 2017年11月9日 上午10:55:45
	 */
	private static void sumThreeSingle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO,
			List<TicketInfoBO> ticketInfoBOs, int multiple, int detailId, String p) {
		String ab[] = HZ_MAP.get(p).split(SymbolConstants.COMMA);
		int lcc = 0;
		if(orderInfoBO.getLotteryCode()==LotteryEnum.Lottery.JSK3.getName()){
			lcc = LotteryChildEnum.LotteryChild.JSK3_TD3.getValue();
		}else if(orderInfoBO.getLotteryCode()==LotteryEnum.Lottery.JXK3.getName()){
			lcc = LotteryChildEnum.LotteryChild.JXK3_TD3.getValue();
		} 
//		assembleFcHzSingleData(orderInfoBO, ticketInfoBOs, ab, multiple, lcc, lotteryTypeBO,detailId);
		String key = lcc+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+detailId;
		Map<String, List<String>> mapList = new HashMap<>();
		mapList.put(key, Arrays.asList(ab));
		assembleSingleData(orderInfoBO, ticketInfoBOs, mapList, lotteryTypeBO.getSplitMaxNum(), lotteryTypeBO.getSplitMaxAmount(), SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE,SplitTicketUtil.NUM_TWO);
	}
	
//	/**
//	 * 
//	 * @Description: 福彩3D和值封装票表信息
//	 * @param orderInfoBO 订单
//	 * @param ticketInfoBOs 生成的票
//	 * @param contents []原始内容
//	 * @param multiple 倍数
//	 * @param lotteryChildCode 子彩种id
//	 * @author wuLong
//	 * @date 2017年6月8日 上午11:32:34
//	 */
//	private static void assembleFcHzSingleData(OrderInfoBO orderInfoBO,List<TicketInfoBO> ticketInfoBOs,String[] contents,Integer multiple,Integer lotteryChildCode,LotteryTypeBO lotteryTypeBO,Integer detailId){
//		if(!ObjectUtil.isBlank(contents)){
//			for(String a : contents){
//				int buyNumber = 1;
//				int subNum = lotteryTypeBO.getSplitMaxAmount()/buyNumber/2;
//				if(subNum>lotteryTypeBO.getSplitMaxNum()){
//					subNum = lotteryTypeBO.getSplitMaxNum();
//				}
//				List<String> res = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, a, subNum);
//				for(String r : res){
//					TicketInfoBO ticketInfoBO = new TicketInfoBO();
//					String[] m= r.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
//					ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO, SplitTicketUtil.NUM_ONE, lotteryChildCode, ticketInfoBO);
//					ticketInfoBO.setTicketContent(m[0]);
//					ticketInfoBO.setMultipleNum(Integer.valueOf(m[1]));
//					ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(2 * ticketInfoBO.getMultipleNum() * 1)));
//					ticketInfoBO.setEndTicketTime(orderInfoBO.getEndTicketTime());
//					ticketInfoBO.setOrderDetailId(detailId);
//					ticketInfoBOs.add(ticketInfoBO);
//				}
//			}
//		}
//	}
	
//	/**
//	 * 
//	 * @Description: 江苏快3单式封装票表信息
//	 * @param orderInfoBO 订单
//	 * @param ticketInfoBOs 生成的票
//	 * @param fcHzMap 
//	 * @author wuLong
//	 * @date 2017年6月8日 上午10:40:58
//	 */
//	private static void assembleksSingleData(OrderInfoBO orderInfoBO,List<TicketInfoBO> ticketInfoBOs,Map<String, List<String>> fcHzMap,LotteryTypeBO lotteryTypeBO){
//		if(!ObjectUtil.isBlank(fcHzMap)){
//			int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
//			int splitMaxNum = lotteryTypeBO.getSplitMaxNum();
//			for(String k : fcHzMap.keySet()){
//				List<String> list = fcHzMap.get(k);
//				String[] j = k.split(SymbolConstants.COMMA);
//				int lotteryChildCode = Integer.valueOf(j[0]);
//				int multiple = Integer.valueOf(j[1]);
//				int detailId = Integer.valueOf(j[2]);
//				List<List<String>> lists = SplitTicketUtil.subList(SplitTicketUtil.NUM_FIVE, list);
//				for(List<String> ls : lists){
//					int buyNumber = ls.size();
//					int subNum = splitMaxAmount/buyNumber/2;
//					if(subNum>splitMaxNum){
//						subNum = splitMaxNum;
//					}
//					String planContent = StringUtils.join(ls,SymbolConstants.SEMICOLON);
//					List<String> res = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, planContent, subNum);
//					for(String p : res){
//						String[] m= p.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
//						TicketInfoBO ticketInfoBO = new TicketInfoBO();
//						ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO, SplitTicketUtil.NUM_ONE, lotteryChildCode, ticketInfoBO);
//						ticketInfoBO.setTicketContent(m[0]);
//						ticketInfoBO.setMultipleNum(Integer.valueOf(m[1]));
//						ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(2 * ticketInfoBO.getMultipleNum() * buyNumber)));
//						ticketInfoBO.setEndTicketTime(orderInfoBO.getEndTicketTime());
//						ticketInfoBO.setOrderDetailId(detailId);
//						ticketInfoBOs.add(ticketInfoBO);
//					}
//				}
//			}
//		}
//	}
}
