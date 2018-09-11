package com.hhly.lottosplit.service.ticket.biglottery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.aware.SpringAware;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryEnum;
import com.hhly.lottosplit.enums.LotteryEnum.LotteryPr;
import com.hhly.lottosplit.event.SplitHandlerEvent;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.mapper.LotteryIssueDaoMapper;
import com.hhly.lottosplit.persistence.issue.po.LotteryIssuePO;
import com.hhly.lottosplit.service.ticket.AbstractCSportsSplitTicket;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.BaseLottery;
import com.hhly.lottosplit.utils.calcutils.Combine;
import com.hhly.lottosplit.utils.calcutils.SportsZsUtil;
import com.hhly.lottosplit.utils.convert.ParseConvertTicketInfo;


/**
 * 
 * @ClassName: OldZcSplitTicket 
 * @Description: 老足彩拆票
 * @author wuLong
 * @date 2017年4月6日 下午4:51:10 
 *
 */
@Component
public class OldZcSplitTicket extends AbstractCSportsSplitTicket {
	@Autowired
	LotteryIssueDaoMapper lotteryIssueDaoMapper;

	@PostConstruct
	public void init(){
		SplitHandlerEvent event = new SplitHandlerEvent(this);
		SpringAware.getApplicationContext().publishEvent(event);
	}
	
	@Override
	public List<TicketInfoBO> excute(OrderInfoBO orderInfoBO,LotteryTypeBO lotteryTypeBO,Map<String,SportAgainstInfoBO> sportAgainstInfoMap)throws Exception {
		List<TicketInfoBO> ticketInfoBOS = new ArrayList<>();
		List<OrderDetailBO> detailBOs = orderInfoBO.getDetailBOs();
		LotteryIssuePO lotteryIssuePO = lotteryIssueDaoMapper.findLotteryIssue(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue());
		LotteryIssuePO nextLotteryIssuePO = lotteryIssueDaoMapper.findNextLotteryIssue(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue());
		if(lotteryIssuePO==null){
			throw new ServiceRuntimeException("老足彩彩票彩期："+orderInfoBO.getLotteryIssue()+",不存在");
		}
		for(OrderDetailBO orderDetailBO : detailBOs){
			super.handle(orderDetailBO, orderInfoBO, ticketInfoBOS, lotteryTypeBO, sportAgainstInfoMap,lotteryIssuePO,nextLotteryIssuePO);
		}
		return ticketInfoBOS;
	}
	
	@Override
	public void single(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) {
		Integer multiple = orderInfoBO.getMultipleNum()*orderDetailBO.getMultiple();
		String content = orderDetailBO.getPlanContent();
		List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, content, lotteryTypeBO.getSplitMaxNum());
		if(!ObjectUtil.isBlank(strings)){
			assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, strings);
		}
	}

	@Override
	public void compound(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) {
		Integer multiple = orderInfoBO.getMultipleNum()*orderDetailBO.getMultiple();
		String content = orderDetailBO.getPlanContent();
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		int splitMaxNum = lotteryTypeBO.getSplitMaxNum();
		int maxZs = splitMaxAmount/2;
		int maxNum = splitMaxAmount/orderDetailBO.getBuyNumber()/2;
		if(maxNum>splitMaxNum){
			maxNum = splitMaxNum;
		}
		int lotteryCode = orderInfoBO.getLotteryCode();
		if(lotteryCode == LotteryEnum.Lottery.SFC.getName()){
			getSFC(orderDetailBO, orderInfoBO, ticketInfoBOs, multiple, content, splitMaxAmount, splitMaxNum, maxZs,
					maxNum);
		}else if(lotteryCode == LotteryEnum.Lottery.ZC_NINE.getName()){
			getNINE(orderDetailBO, orderInfoBO, ticketInfoBOs, multiple, content, splitMaxAmount, splitMaxNum, maxNum);
		}else{
			List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, content, maxNum);
			orderDetailBO.setContentType(2);
			assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, strings);
		}
	}

	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param orderDetailBO
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param multiple
	 * @param content
	 * @param splitMaxAmount
	 * @param splitMaxNum
	 * @param maxNum
	 * @throws ServiceRuntimeException
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年7月1日 下午5:05:19
	 */
	@SuppressWarnings("unchecked")
	private void getNINE(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			Integer multiple, String content, int splitMaxAmount, int splitMaxNum, int maxNum)
			throws ServiceRuntimeException, NumberFormatException {
		String[] pc = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		int matchNum = 0;
		for(String a : pc){
			if(!"_".equals(a)){
				matchNum++;
			}
		}
		if(matchNum>9){
			String[] a = new String[14];
			List<String> tuos =new ArrayList<>();
			List<String> dans = new ArrayList<>();
			getTuoDans(pc, a, tuos, dans);
			String[] arrayTuos = (String[])tuos.toArray(new String[tuos.size()]);
			Combine combine = new Combine();
			List<String> listCombine = combine.mn(arrayTuos, 9);
			for(String b : listCombine){
				String[] at = b.split(SymbolConstants.OBLIQUE_LINE);
				List<String> ats = Arrays.asList(at);
				StringBuffer sb = new StringBuffer();
				int contentType = 1;
				for(int i=0;i<14;i++){
					String istr = String.valueOf(i);
					if(ats.contains(istr)||dans.contains(istr)){
						int len = a[i].split(SymbolConstants.COMMA).length;
						if(len>1&&contentType!=2){
							contentType = 2;
						}
						sb.append(a[i]).append(SymbolConstants.VERTICAL_BAR);
					}else{
						sb.append("_").append(SymbolConstants.VERTICAL_BAR);
					}
				}
				sb.deleteCharAt(sb.length()-1);
				String con = sb.toString();
				int buyNumber = 1;
				if(contentType>1){
					buyNumber = SportsZsUtil.getSportsManyNote(con, LotteryEnum.Lottery.ZC_NINE.getName());
				}
				maxNum = splitMaxAmount/buyNumber/2;
				if(maxNum>splitMaxNum){
					maxNum = splitMaxNum;
				}
				List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, con, maxNum);
				orderDetailBO.setContentType(contentType);
				assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, strings);
			}
		}else{
			List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, content, maxNum);
			orderDetailBO.setContentType(2);
			assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, strings);
		}
	}

	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param orderDetailBO
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param multiple
	 * @param content
	 * @param splitMaxAmount
	 * @param splitMaxNum
	 * @param maxZs
	 * @param maxNum
	 * @throws ServiceRuntimeException
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年7月1日 下午5:05:05
	 */
	private void getSFC(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			Integer multiple, String content, int splitMaxAmount, int splitMaxNum, int maxZs, int maxNum)
			throws ServiceRuntimeException, NumberFormatException {
		if(orderDetailBO.getBuyNumber()>maxZs){
			String[] pc = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
			String[][] tl = new String[SplitTicketUtil.NUM_SIX][];
			int j = 0;
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<12;i++){
				if(i<SplitTicketUtil.NUM_SIX){
					sb.append(pc[i]).append(SymbolConstants.VERTICAL_BAR);
				}else{
					String[] a = pc[i].split(SymbolConstants.COMMA);
					tl[j] = a;
					j++;
				}
			}
			String t = sb.toString();
			BaseLottery baseLottery = new BaseLottery();
			@SuppressWarnings("static-access")
			String rs = baseLottery.getCombineArrToStr(tl);;
			String[] rss = rs.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
			for(String a : rss){
				String cf = t+a.replace(SymbolConstants.COMMA, SymbolConstants.VERTICAL_BAR);
				int buyNumber = SportsZsUtil.getSportsManyNote(cf, LotteryEnum.Lottery.SFC.getName());
				maxNum = splitMaxAmount/buyNumber/2;
				if(maxNum>splitMaxNum){
					maxNum = splitMaxNum;
				}
				List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, cf, maxNum);
				assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, strings);
			}
		}else{
			List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, content, maxNum);
			assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, strings);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void dantuo(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) {
		String content = orderDetailBO.getPlanContent();
		int multiple = orderDetailBO.getMultiple()*orderInfoBO.getMultipleNum();
		String[] pc = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		String[] a = new String[14];
		List<String> tuos =new ArrayList<>();
		List<String> dans = new ArrayList<>();
		getTuoDans(pc, a, tuos, dans);
		String[] arrayTuos = (String[])tuos.toArray(new String[tuos.size()]);
		Combine combine = new Combine();
		List<String> listCombine = combine.mn(arrayTuos, 9 - dans.size());
		List<String> fuShiList = new ArrayList<>();
		List<String> danShiList = new ArrayList<>();
		for(String lc : listCombine){
			String[] at = lc.split(SymbolConstants.OBLIQUE_LINE);
			List<String> ats = Arrays.asList(at);
			StringBuffer sb = new StringBuffer();
			boolean isDs = true;
			int havThree = 0;
			for(int i=0;i<14;i++){
				String istr = String.valueOf(i);
				int len = a[i].split(SymbolConstants.COMMA).length;
				if(ats.contains(istr)||dans.contains(istr)){
					sb.append(a[i]).append(SymbolConstants.VERTICAL_BAR);
					if(len==3){
						havThree++;
					}
					if(len>1&&isDs){
						isDs = false;
					}
				}else{
					sb.append("_").append(SymbolConstants.VERTICAL_BAR);
				}
			}
			sb.deleteCharAt(sb.length()-1);
			if(isDs){
				danShiList.add(sb.toString());
			}else{
				getFushiExceNum(fuShiList, sb, havThree);
			}
		}
		int maxNum = lotteryTypeBO.getSplitMaxAmount()/orderDetailBO.getBuyNumber()/2;
		if(maxNum>lotteryTypeBO.getSplitMaxNum()){
			maxNum = lotteryTypeBO.getSplitMaxNum();
		}
		if(!danShiList.isEmpty()){
			for(String ds : danShiList){
				List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, ds, maxNum);
				orderDetailBO.setContentType(1);
				assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, strings);
			}
		}
		if(!fuShiList.isEmpty()){
			for(String ds : fuShiList){
				List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, ds, maxNum);
				orderDetailBO.setContentType(2);
				assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, strings);
			}
		}
	}
	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param fuShiList
	 * @param sb
	 * @param havThree
	 * @author wuLong
	 * @date 2017年7月1日 上午10:42:59
	 */
	private static void getFushiExceNum(List<String> fuShiList, StringBuffer sb, int havThree) {
		String tst = sb.toString();
		if(havThree>8){
			String[] dy = tst.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
			int lastMcNum = 0;
			String lastMc = null;
			for(int i = dy.length-1;i>-1;i--){
				String dyp = dy[i];
				if(!"_".equals(dyp)){
					lastMcNum = i;
					lastMc = dyp;
					break;
				}
			}
			String[] ltm = lastMc.split(SymbolConstants.COMMA);
			for(int h=0;h<ltm.length;h++){
				StringBuffer sbf = new StringBuffer();
				for(int i = 0;i<dy.length;i++){
					if(i == lastMcNum){
						sbf.append(ltm[h]).append(SymbolConstants.VERTICAL_BAR);
					}else{
						sbf.append(dy[i]).append(SymbolConstants.VERTICAL_BAR);
					}
				}
				String acc = sbf.deleteCharAt(sbf.length()-1).toString();
				fuShiList.add(acc);
			}
		}else{
			fuShiList.add(tst);
		}
	}
	/** 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param pc
	 * @param a
	 * @param tuos
	 * @param dans
	 * @author wuLong
	 * @date 2017年7月1日 上午10:41:18
	 */
	private static void getTuoDans(String[] pc, String[] a, List<String> tuos, List<String> dans) {
		int j = 0;
		for(int i =0;i<pc.length;i++){
			String b = pc[i];
			if(b.contains(SymbolConstants.NUMBER_SIGN)){
				String[] c = b.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN,-1);
				int k = j;
				for(int h = 0;h<c.length;h++){
					if(k==14){
						break;
					}
					String d = c[h];
					a[k]=d;
					if(!ObjectUtil.isBlank(d)){
						if(h==c.length-1){
							if(!"_".equals(d)&&!ObjectUtil.isBlank(d)){
								tuos.add(j+"");								
							}
						}else{
							dans.add(j+"");
						}
					}
					j++;
					k++;
				}
			}else{
				a[j]=b;
				if(!"_".equals(b)&&!ObjectUtil.isBlank(b)){
					tuos.add(j+"");
				}
				j++;
			}
		}
	}
	
	private void assembleData(OrderDetailBO detailVO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			List<String> list) throws ServiceRuntimeException, NumberFormatException {
		for(String c : list){
			TicketInfoBO ticketInfoBO = new TicketInfoBO();
			ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO,detailVO,ticketInfoBO);
			String[] d = c.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
			String content = d[0];
			int multipleNum = Integer.valueOf(d[1]);
			int zs = SportsZsUtil.getSportsManyNote(content,orderInfoBO.getLotteryCode());
			ticketInfoBO.setTicketContent(content);
			ticketInfoBO.setMultipleNum(multipleNum);
			ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(zs*2*multipleNum)));
			ticketInfoBO.setEndTicketTime(orderInfoBO.getEndTicketTime());
			ticketInfoBO.setOrderDetailId(detailVO.getId());
			ticketInfoBOs.add(ticketInfoBO);
		}
	}

	@Override
	public LotteryPr getLotteryPr() {
		return LotteryPr.ZC;
	}

}
