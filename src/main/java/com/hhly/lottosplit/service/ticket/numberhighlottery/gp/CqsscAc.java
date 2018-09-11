package com.hhly.lottosplit.service.ticket.numberhighlottery.gp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import com.hhly.lottosplit.utils.calcutils.BaseLottery;
import com.hhly.lottosplit.utils.calcutils.Combine;
import com.hhly.lottosplit.utils.convert.ParseConvertTicketInfo;
/**
 * 
 * @ClassName: CqsscAc 
 * @Description: 重庆时时彩拆票并封装票信息 
 * @author wuLong
 * @date 2017年6月12日 上午11:34:02 
 *
 */
@Component
public class CqsscAc extends AbstractCSplitHanlde implements AbstractINumberSplit {
//	/**三星和值注数**/
	private static final int[] ZX3 = {1,3,6,10,15,21,28,36,45,55,63,69,73,75,75,73,69,63,55,45,36,28,21,15,10,6,3,1};
	/**二星组选和值注数**/
	private static final int[] XZX2 = {1,1,2,2,3,3,4,4,5,5,5,4,4,3,3,2,2,1,1};
	/**二星直选和值注数**/
	private static final int[] XZX2L = {1,2,3,4,5,6,7,8,9,10,9,8,7,6,5,4,3,2,1};
	private static Map<Integer, String> starNumMap ;
	
	static {
		starNumMap = new HashMap<>();
		{
			starNumMap.put(LotteryChildEnum.LotteryChild.CQSSC_3.getValue(), "-|-|");
			starNumMap.put(LotteryChildEnum.LotteryChild.CQSSC_2.getValue(), "-|-|-|");
			starNumMap.put(LotteryChildEnum.LotteryChild.CQSSC_1.getValue(), "-|-|-|-|");
		}
	}
	
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
		int splitMaxNum = lotteryTypeBO.getSplitMaxNum();
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		Combine combine = new Combine();
		for (OrderDetailBO orderDetailBO : orderDetailBOs) {
			int lotteryChildCode = orderDetailBO.getLotteryChildCode();
			String planContent = orderDetailBO.getPlanContent();
			int contentType = orderDetailBO.getContentType();
			
			int multiple = orderInfoBO.getMultipleNum()*orderDetailBO.getMultiple();
			String key = lotteryChildCode+SymbolConstants.COMMA+multiple+SymbolConstants.COMMA+orderDetailBO.getId();
			List<String> singleList = new ArrayList<>();
			//单式
			if(SplitTicketUtil.NUM_ONE == contentType){
				if(starNumMap.containsKey(lotteryChildCode)){
					planContent = starNumMap.get(lotteryChildCode).concat(planContent);
				}
				singleList.add(planContent);
			}
			//复式
			else if(SplitTicketUtil.NUM_TWO == contentType){
				handleFuShi(orderInfoBO, lotteryTypeBO, ticketInfoBOs, splitMaxNum, splitMaxAmount, orderDetailBO,
						lotteryChildCode, multiple, singleList ,combine);
			}
			//和值
			else if(SplitTicketUtil.NUM_SIX == contentType){
				String[] pc = planContent.split(SymbolConstants.COMMA);
				assembleFcHzData(orderInfoBO, ticketInfoBOs, pc, multiple, lotteryChildCode,splitMaxNum,splitMaxAmount,orderDetailBO.getId());
			}
			//胆拖 直接拆单式
			else if(SplitTicketUtil.NUM_THREE== contentType){
				HandleDanTuo(combine, lotteryChildCode, planContent, singleList);
			}
			
			if(!ObjectUtil.isBlank(singleList)){
				if(!map.containsKey(key)){
					map.put(key, singleList);
				}else{
					List<String> n = map.get(key);
					n.addAll(singleList);
					map.put(key, n);
				}
			}
			singleList = null;
		}
		
		if(!ObjectUtil.isBlank(map)){
			assembleSingleData(orderInfoBO, ticketInfoBOs, map, splitMaxNum, splitMaxAmount, SplitTicketUtil.NUM_FIVE, SplitTicketUtil.NUM_ONE, 2);
			map = null;
		}
	}

	/** 
	 * @Description: 处理复式
	 * @param orderInfoBO
	 * @param lotteryTypeBO
	 * @param ticketInfoBOs
	 * @param splitMaxNum
	 * @param splitMaxAmount
	 * @param orderDetailBO
	 * @param lotteryChildCode
	 * @param multiple
	 * @param singleList
	 * @throws NumberFormatException
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年6月16日 上午10:18:01
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	private void handleFuShi(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, List<TicketInfoBO> ticketInfoBOs,
			int splitMaxNum, int splitMaxAmount, OrderDetailBO orderDetailBO, int lotteryChildCode, 
			int multiple, List<String> singleList,Combine combine) throws NumberFormatException, ServiceRuntimeException {
		String planContent = orderDetailBO.getPlanContent();
		//五星直选 
		if(lotteryChildCode==LotteryChildEnum.LotteryChild.CQSSC_5.getValue()){
			if(orderDetailBO.getAmount()>lotteryTypeBO.getSplitMaxAmount()){
				asso(orderInfoBO, ticketInfoBOs, splitMaxNum, splitMaxAmount, orderDetailBO, planContent, multiple);
			}else{
				int maxNum = splitMaxAmount/orderDetailBO.getBuyNumber()/2;
				if(maxNum>splitMaxNum){
					maxNum = splitMaxNum;
				}
				List<String> list = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, planContent, maxNum);
				for(String x : list){
					assembleDanTiaoData(orderInfoBO, ticketInfoBOs, x, orderDetailBO, orderDetailBO.getBuyNumber());
				}
			}
		}
		//五星通选
		else if(lotteryChildCode==LotteryChildEnum.LotteryChild.CQSSC_5T.getValue()){
			String[] p = planContent.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
			String[][] a = new String[p.length][];
			for(int i = 0;i<p.length;i++){
				a[i] = p[i].split(SymbolConstants.COMMA);
			}
			BaseLottery baseLottery = new BaseLottery();
			String f = baseLottery.getCombineArrToStrabc(a);
			String[] g = f.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.OBLIQUE_LINE);
			List<String> list = Arrays.asList(g);
			for(String x : list){
				int maxNum = splitMaxAmount/1/2;
				if(maxNum>splitMaxNum){
					maxNum = splitMaxNum;
				}
				List<String> listx = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, x, maxNum);
				for(String y : listx){
					orderDetailBO.setContentType(1);
					assembleDanTiaoData(orderInfoBO, ticketInfoBOs, y, orderDetailBO, 1);
				}
			}
		}
		//大小单双
		else if(lotteryChildCode==LotteryChildEnum.LotteryChild.CQSSC_DXDS.getValue()){
			String[] pc = planContent.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
			String[] tenPro = pc[0].split(SymbolConstants.COMMA);
			String[] gePro = pc[1].split(SymbolConstants.COMMA);
			for(String a : tenPro){
				for(String b: gePro){
					singleList.add(a.concat(SymbolConstants.VERTICAL_BAR)+b);
				}
			}
		}
		//重庆时时彩一星
		else if(lotteryChildCode==LotteryChildEnum.LotteryChild.CQSSC_1.getValue()){
			String[] p = planContent.split(SymbolConstants.COMMA);
			for(String a : p){
				singleList.add(starNumMap.get(lotteryChildCode).concat(a));
			}
		}
		else{
			boolean tscl = false;
			if(lotteryChildCode==LotteryChildEnum.LotteryChild.CQSSC_2Z.getValue()){
				String pc[] = planContent.split(SymbolConstants.COMMA);
				if(pc.length>5){
					tscl = true;
				}
			}
			if(tscl){
				List<String> list = combine.mn(planContent.split(SymbolConstants.COMMA), 2);
				for(String t : list){
					String cont = t.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA);
					List<String> a = Arrays.asList(cont.split(SymbolConstants.COMMA));
					Collections.sort(a);
					singleList.add(StringUtils.join(a,SymbolConstants.COMMA));
				}
			}else{
				if(starNumMap.containsKey(lotteryChildCode)){
					planContent = starNumMap.get(lotteryChildCode).concat(planContent);
				}
				int maxNum = splitMaxAmount/orderDetailBO.getBuyNumber()/2;
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

	/** 
	 * @Description: 处理胆拖
	 * @param combine
	 * @param lotteryChildCode
	 * @param planContent
	 * @param singleList
	 * @author wuLong
	 * @date 2017年6月16日 上午10:17:32
	 */
	private void HandleDanTuo(Combine combine, int lotteryChildCode, String planContent, List<String> singleList) {
		String[] pc = planContent.split(SymbolConstants.NUMBER_SIGN);
		String[] dans = pc[0].split(SymbolConstants.COMMA);
		String[] tuos = pc[1].split(SymbolConstants.COMMA);
		int danTuoLen = dans.length+tuos.length;
		//三星组三
		if(lotteryChildCode==LotteryChildEnum.LotteryChild.CQSSC_3Z3.getValue()){
			for(String t : tuos){
				String cont = pc[0]+SymbolConstants.COMMA+pc[0]+SymbolConstants.COMMA+t;
				List<String> a = Arrays.asList(cont.split(SymbolConstants.COMMA));
				Collections.sort(a);
				String cont1 = pc[0]+SymbolConstants.COMMA+t+SymbolConstants.COMMA+t;
				List<String> a1 = Arrays.asList(cont1.split(SymbolConstants.COMMA));
				Collections.sort(a1);
				singleList.add(StringUtils.join(a,SymbolConstants.COMMA));
				singleList.add(StringUtils.join(a1,SymbolConstants.COMMA));
			}
		}
		//三星组六
		else if(lotteryChildCode==LotteryChildEnum.LotteryChild.CQSSC_3Z6.getValue()){
			//1#3,4,5  1,2#3,4,5
			if(danTuoLen == SplitTicketUtil.NUM_THREE){
				singleList.add(planContent.replace(SymbolConstants.NUMBER_SIGN, SymbolConstants.COMMA));
			}else{
				@SuppressWarnings("unchecked")
				List<String> tuoCfList = combine.mn(tuos, SplitTicketUtil.NUM_THREE - dans.length);
				for(String t : tuoCfList){
					String cont = pc[0]+SymbolConstants.COMMA+t.replace(SymbolConstants.OBLIQUE_LINE, SymbolConstants.COMMA);
					List<String> a = Arrays.asList(cont.split(SymbolConstants.COMMA));
					Collections.sort(a);
					singleList.add(StringUtils.join(a,SymbolConstants.COMMA));
				}
			}
		}
		//二星组选  1#2,3
		else if(lotteryChildCode==LotteryChildEnum.LotteryChild.CQSSC_2Z.getValue()){
			if(danTuoLen == SplitTicketUtil.NUM_TWO){
				singleList.add(planContent.replace(SymbolConstants.NUMBER_SIGN, SymbolConstants.COMMA));
			}else{
				for(String t : tuos){
					singleList.add(dans[0]+SymbolConstants.COMMA+t);
				}
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
				if(LotteryChildEnum.LotteryChild.CQSSC_2Z.getValue() != lotteryChildCode && LotteryChildEnum.LotteryChild.CQSSC_2.getValue() != lotteryChildCode
						&&LotteryChildEnum.LotteryChild.CQSSC_3.getValue() != lotteryChildCode){
					throw new ServiceRuntimeException("子彩种ID:"+lotteryChildCode+",投注类型错误,此子彩种不存在和值玩法");
				}
				int zs = 0;
				if(LotteryChildEnum.LotteryChild.CQSSC_2Z.getValue() == lotteryChildCode){
					zs = XZX2[Integer.valueOf(a)];
				}
				if(LotteryChildEnum.LotteryChild.CQSSC_2.getValue() == lotteryChildCode ){
					zs = XZX2L[Integer.valueOf(a)];
				}else if(LotteryChildEnum.LotteryChild.CQSSC_3.getValue() == lotteryChildCode){
					zs = ZX3[Integer.valueOf(a)];
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
		return Lottery.CQSSC;
	}
}
