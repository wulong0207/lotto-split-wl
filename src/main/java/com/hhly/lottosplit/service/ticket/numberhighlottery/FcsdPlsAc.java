package com.hhly.lottosplit.service.ticket.numberhighlottery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhly.lottosplit.bo.CooperateCdkeyBO;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryChildEnum;
import com.hhly.lottosplit.enums.LotteryEnum;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.service.ticket.AbstractCSplitHanlde;
import com.hhly.lottosplit.service.ticket.AbstractINumberSplit;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.Combine;
import com.hhly.lottosplit.utils.convert.ParseConvertTicketInfo;
/**
 * 
 * @ClassName: FcsdPlsAc 
 * @Description: 福彩3D和排列3实现拆票并封装成票信息 
 * @author wuLong
 * @date 2017年6月8日 下午6:00:26 
 *
 */
public abstract class FcsdPlsAc extends AbstractCSplitHanlde implements AbstractINumberSplit {
	/**组3和值注数对照**/
	public static int[] ZX3 = {0,1,2,1,3,3,3,4,5,4,5,5,4,5,5,4,5,5,4,5,4,3,3,3,1,2,1,0};
	/**组3和值注数对照**/
	public static int[] ZX6 = {0,0,0,1,1,2,3,4,5,7,8,9,10,10,10,10,9,8,7,5,4,3,2,1,1,0,0,0};
	/**直选 和值注数对照**/
	public static int[] ZX =  {1,3,6,10,15,21,28,36,45,55,63,69,73,75,75,73,69,63,55,45,36,28,21,15,10,6,3,1};
	/**组3和值map**/
	public static Map<String, String> pl3SumMap ;
	/**组6和值map**/
	public static Map<String, String> pl6SumMap ;
	/**直选单选和值**/
	public static Map<String, String> dxSumMap;
	static{
		pl3SumMap = new HashMap<>();
		pl6SumMap = new HashMap<>();
		dxSumMap = new HashMap<>();
		{
			int[] hezhi = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27};
			int[] a = {0,1,2,3,4,5,6,7,8,9};
			int[] b = {0,1,2,3,4,5,6,7,8,9};
			int[] c = {0,1,2,3,4,5,6,7,8,9};
			for(int i = 0;i<hezhi.length;i++){
				int hezhival = hezhi[i];
				StringBuffer sb = new StringBuffer();
				StringBuffer sb3 = new StringBuffer();
				StringBuffer sb6 = new StringBuffer();
				for(int j =0;j<a.length;j++){
					int aj = a[j];
					for(int k=0;k<b.length;k++){
						int bk = b[k];
						for(int l=0;l<c.length;l++){
							int cl = c[l];
							if(cl>hezhival){
								break;
							}
							if(aj!=bk&&aj!=cl&&bk!=cl&&aj<bk&&bk<cl&&(aj+bk+cl)==hezhival){
								sb6.append(aj+SymbolConstants.COMMA+bk+SymbolConstants.COMMA+cl+SymbolConstants.VERTICAL_BAR);
							}
							if(aj==bk&&bk!=cl&&(aj+bk+cl)==hezhival){
								sb3.append(aj+SymbolConstants.COMMA+bk+SymbolConstants.COMMA+cl+SymbolConstants.VERTICAL_BAR);
							}
							if((aj+bk+cl)==hezhival){
								sb.append(aj+SymbolConstants.VERTICAL_BAR+bk+SymbolConstants.VERTICAL_BAR+cl+SymbolConstants.COMMA);
							}
						}
					}
				}
				if(sb.length()>0){
					String valu = sb.deleteCharAt(sb.length()-1).toString();
					dxSumMap.put(hezhival+"", valu);
				}
				if(sb3.length()>0){
					String valu = sb3.deleteCharAt(sb3.length()-1).toString();
					pl3SumMap.put(hezhival+"", valu);
				}
				if(sb6.length()>0){
					String valu = sb6.deleteCharAt(sb6.length()-1).toString();
					pl6SumMap.put(hezhival+"", valu);
				}
			}
		}
	}
	
	@Override
	public void Handle(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs)
			throws ServiceRuntimeException {
//		handleFc3d(orderInfoBO, lotteryTypeBO, ticketInfoBOs);
	}

	
	/** 
	 * @Description: 福彩3D拆票并封装票信息 
	 * @param orderInfoBO 订单信息
	 * @param lotteryTypeBO 彩种信息
	 * @param ticketInfoBOs list返回封装的票
	 * @throws NumberFormatException
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年6月8日 上午11:35:41
	 */
	@SuppressWarnings("unchecked")
	protected static void handleFc3d(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO,
			List<TicketInfoBO> ticketInfoBOs,List<CooperateCdkeyBO> cdkeyBOs) throws NumberFormatException, ServiceRuntimeException {
		List<OrderDetailBO> orderDetailBOs = orderInfoBO.getDetailBOs();
		Map<String, List<String>> map = new HashMap<>();
		int splitMaxNum = lotteryTypeBO.getSplitMaxNum();
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		Combine combine = new Combine();
		LotteryEnum.Lottery lottery = LotteryEnum.Lottery.getLottery(orderInfoBO.getLotteryCode());
		for (OrderDetailBO detailBO : orderDetailBOs) {
			int lotteryChildCode = detailBO.getLotteryChildCode();
			int contentType = detailBO.getContentType();
			String planCotent = detailBO.getPlanContent();
			int multiple = orderInfoBO.getMultipleNum()*detailBO.getMultiple();
			String key = lotteryChildCode+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+detailBO.getId();
			//和值，需要进行拆分14,15；票一：14票二：15一个和值为一张票
			if(contentType == SplitTicketUtil.NUM_SIX){
				String[] pc = planCotent.split(SymbolConstants.COMMA);
					if(lottery == LotteryEnum.Lottery.PL3||lottery == LotteryEnum.Lottery.F3D){
					List<String> list = new ArrayList<>();
					if(lotteryChildCode == LotteryChildEnum.LotteryChild.PL3_G3.getValue()||lotteryChildCode == LotteryChildEnum.LotteryChild.D_G3.getValue()){
						for(String a : pc){
							String apc[] = pl3SumMap.get(a).split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
							list.addAll(Arrays.asList(apc));
						}
					}else if(lotteryChildCode == LotteryChildEnum.LotteryChild.PL3_G6.getValue()||lotteryChildCode == LotteryChildEnum.LotteryChild.D_G6.getValue()){
						for(String a : pc){
							String apc[]  = pl6SumMap.get(a).split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
							list.addAll(Arrays.asList(apc));
						}
					}else if(lotteryChildCode == LotteryChildEnum.LotteryChild.PL3_DIRECT.getValue()||lotteryChildCode == LotteryChildEnum.LotteryChild.D_DIRECT.getValue()){
						for(String a : pc){
							String apc[]  = dxSumMap.get(a).split(SymbolConstants.COMMA);
							list.addAll(Arrays.asList(apc));
						}
					}
					if(!ObjectUtil.isBlank(list)){
						List<String> lists = new ArrayList<>();
						if(map.containsKey(key)){
							lists = map.get(key);
						}
						lists.addAll(list);
						map.put(key, lists);
					}
				}
			}
			//单式
			else if(contentType == SplitTicketUtil.NUM_ONE){
				if(lotteryChildCode == LotteryChildEnum.LotteryChild.D_G3.getValue() || lotteryChildCode == LotteryChildEnum.LotteryChild.PL3_G3.getValue()){
					String[] pc = planCotent.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
					planCotent = pc[0]+SymbolConstants.COMMA+pc[0]+SymbolConstants.COMMA+pc[1];
				}
				List<String> list = new ArrayList<>();
				if(map.containsKey(key)){
					list = map.get(key);
				}
				list.add(planCotent);
				map.put(key, list);
			}
			//胆拖
			else if(contentType == SplitTicketUtil.NUM_THREE){
				String[] pc = planCotent.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN);
				String[] ds = pc[1].split(SymbolConstants.COMMA);
				int dlen = pc[0].split(SymbolConstants.COMMA).length;
				if(dlen==2){
					for(String a : ds){
						String c = pc[0]+SymbolConstants.COMMA+a;
						List<String> list = new ArrayList<>();
						if(map.containsKey(key)){
							list = map.get(key);
						}
						list.add(c);
						map.put(key, list);
					}
				}else if(dlen==1){
					List<String> listCombine = combine.mn(ds, 2);
					for(String a : listCombine){
						String c = pc[0]+SymbolConstants.COMMA+a.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA);
						List<String> list = new ArrayList<>();
						if(map.containsKey(key)){
							list = map.get(key);
						}
						list.add(c);
						map.put(key, list);
					}
				}
			}
			//复式，其他
			else{
				if((lotteryChildCode == LotteryChildEnum.LotteryChild.D_G3.getValue() || lotteryChildCode == LotteryChildEnum.LotteryChild.PL3_G3.getValue())){
					if(planCotent.indexOf(SymbolConstants.VERTICAL_BAR)>-1){
						String[] pc = planCotent.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
						String[] dz = pc[0].split(SymbolConstants.COMMA);
						String[] ds = pc[1].split(SymbolConstants.COMMA);
						for(String a : dz){
							for(String b : ds){
								if(a.equals(b)){
									continue;
								}
								String c = a+SymbolConstants.COMMA+a+SymbolConstants.COMMA+b;
								List<String> list = new ArrayList<>();
								if(map.containsKey(key)){
									list = map.get(key);
								}
								list.add(c);
								map.put(key, list);
							}
						}
					}else{
						String[] ds = planCotent.split(SymbolConstants.COMMA);
						List<String> listCombine = combine.mn(ds, 2);
						for(String a : listCombine){
							String b[] = a.split(SymbolConstants.OBLIQUE_LINE);
							String c = b[0]+SymbolConstants.COMMA+b[1]+SymbolConstants.COMMA+b[1];
							String d = b[1]+SymbolConstants.COMMA+b[0]+SymbolConstants.COMMA+b[0];
							List<String> list = new ArrayList<>();
							if(map.containsKey(key)){
								list = map.get(key);
							}
							list.add(c);
							list.add(d);
							map.put(key, list);
						}
					}
				}
				else if(lotteryChildCode == LotteryChildEnum.LotteryChild.D_G6.getValue() || lotteryChildCode == LotteryChildEnum.LotteryChild.PL3_G6.getValue()){
					String[] pc = planCotent.split(SymbolConstants.COMMA);
					List<String> listCombine = combine.mn(pc, 3);
					for(String a : listCombine){
						String c = a.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA);
						List<String> list = new ArrayList<>();
						if(map.containsKey(key)){
							list = map.get(key);
						}
						list.add(c);
						map.put(key, list);
					}
				}
				else{
					int maxNum = splitMaxAmount/detailBO.getBuyNumber()/2;
					if(maxNum>splitMaxNum){
						maxNum = splitMaxNum;
					}
					List<String> m = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple,planCotent,maxNum);
					assembleNoSingleData(orderInfoBO,ticketInfoBOs,m,detailBO);
				}
			}
		}
		if(!ObjectUtil.isBlank(map)){
			if(cdkeyBOs!=null){
				assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_ONE, SplitTicketUtil.NUM_ONE, 2 ,cdkeyBOs);
			}else{
				assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_FIVE, SplitTicketUtil.NUM_ONE, 2);
			}
		}
	}
	
	/**
	 * 
	 * @Description: 福彩3D和值封装票表信息
	 * @param orderInfoBO 订单
	 * @param ticketInfoBOs 生成的票
	 * @param contents []原始内容
	 * @param multiple 倍数
	 * @param lotteryChildCode 子彩种id
	 * @author wuLong
	 * @date 2017年6月8日 上午11:32:34
	 */
	private static void assembleFcHzData(OrderInfoBO orderInfoBO,List<TicketInfoBO> ticketInfoBOs,String[] contents,Integer multiple,Integer lotteryChildCode,Integer splitMaxNum,Integer splitMaxAmount,Integer detailId){
		if(!ObjectUtil.isBlank(contents)){
			for(String a : contents){
				int zs = 0;
				if(LotteryChildEnum.LotteryChild.D_DIRECT.getValue() == lotteryChildCode || LotteryChildEnum.LotteryChild.PL3_DIRECT.getValue() == lotteryChildCode){
					zs = ZX[Integer.valueOf(a)];
				}else if(LotteryChildEnum.LotteryChild.D_G3.getValue() == lotteryChildCode || LotteryChildEnum.LotteryChild.PL3_G3.getValue() == lotteryChildCode){
					zs = ZX3[Integer.valueOf(a)];
				}else if(LotteryChildEnum.LotteryChild.D_G6.getValue() == lotteryChildCode || LotteryChildEnum.LotteryChild.PL3_G6.getValue() == lotteryChildCode){
					zs = ZX6[Integer.valueOf(a)];
				}
				if(zs == 0){
					throw new ServiceRuntimeException("子彩种ID:"+lotteryChildCode+",没有找到对应的和值子玩法");
				}
				int subNum = splitMaxAmount/zs/2;
				if(subNum>splitMaxNum){
					subNum = splitMaxNum;
				}
				List<String> res = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, a, subNum);
				for(String r : res){
					TicketInfoBO ticketInfoBO = new TicketInfoBO();
					String[] m= r.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
					ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO, SplitTicketUtil.NUM_SIX, lotteryChildCode, ticketInfoBO);
					ticketInfoBO.setTicketContent(m[0]);
					ticketInfoBO.setMultipleNum(Integer.valueOf(m[1]));
					ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(2 * ticketInfoBO.getMultipleNum() * zs)));
					ticketInfoBO.setEndTicketTime(orderInfoBO.getEndTicketTime());
					ticketInfoBO.setOrderDetailId(detailId);
					ticketInfoBOs.add(ticketInfoBO);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		String[] ds = "1,2,3,4".split(SymbolConstants.COMMA);
		Combine combine = new Combine();
		List<String> listCombine = combine.mn(ds, 2);
		for(String a : listCombine){
			String b[] = a.split(SymbolConstants.OBLIQUE_LINE);
			String c = b[0]+SymbolConstants.COMMA+b[1]+SymbolConstants.COMMA+b[1];
			System.out.println(c);
		}
	}
}
