package com.hhly.lottosplit.service.ticket.biglottery;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.aware.SpringAware;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryEnum.LotteryPr;
import com.hhly.lottosplit.event.SplitHandlerEvent;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.mapper.LotteryIssueDaoMapper;
import com.hhly.lottosplit.persistence.issue.po.LotteryIssuePO;
import com.hhly.lottosplit.service.SportAgainstInfoService;
import com.hhly.lottosplit.service.ticket.AbstractCSportsSplitTicket;
import com.hhly.lottosplit.utils.FormatConversionJCUtil;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.SportsZsUtil;

/**
 * 
 * @ClassName: SportsSplitTicket 
 * @Description: 竞彩拆票
 * @author wuLong
 * @date 2017年4月6日 下午4:50:57 
 *
 */
@Component
public class SportsSplitTicket extends AbstractCSportsSplitTicket {
	
	@Autowired
	SportAgainstInfoService sportAgainstInfoService;
	@Autowired
	LotteryIssueDaoMapper lotteryIssueDaoMapper;

	
	@PostConstruct
	public void init(){
		SplitHandlerEvent event = new SplitHandlerEvent(this);
		SpringAware.getApplicationContext().publishEvent(event);
	}
	
	@Override
	public List<TicketInfoBO> excute(OrderInfoBO orderInfoBO,LotteryTypeBO lotteryTypeBO,Map<String,SportAgainstInfoBO> sportAgainstInfoMap) throws Exception{
		List<TicketInfoBO> ticketInfoBOs = new Vector<>();
		List<OrderDetailBO> detailBOs = orderInfoBO.getDetailBOs();
		Integer lotteryCode = orderInfoBO.getLotteryCode();
		String lotteryIssue = orderInfoBO.getLotteryIssue();
		LotteryIssuePO lotteryIssuePO = lotteryIssueDaoMapper.findLotteryIssue(lotteryCode, lotteryIssue);
		LotteryIssuePO nextLotteryIssuePO = lotteryIssueDaoMapper.findNextLotteryIssue(lotteryCode, lotteryIssue);
		if(lotteryIssuePO==null){
			throw new ServiceRuntimeException("竞彩彩票彩期："+lotteryIssue+",不存在");
		}
		for(OrderDetailBO orderDetailBO : detailBOs){
			super.handle(orderDetailBO, orderInfoBO, ticketInfoBOs, lotteryTypeBO, sportAgainstInfoMap,lotteryIssuePO,nextLotteryIssuePO);
		}
		try {
			matchSort(ticketInfoBOs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ticketInfoBOs;
	}

	/**
	 * 票的内容根据赛事排序
	 * @author wul687  
	 * @date 2018-05-18
	 * @param ticketInfoBOs
	 */
	private void matchSort(List<TicketInfoBO> ticketInfoBOs) {
		if(ticketInfoBOs!=null&&ticketInfoBOs.size()>0){
			ticketInfoBOs.forEach(ticketInfoBO -> {
				String ticketContent = ticketInfoBO.getTicketContent();
				String[] contenta = FormatConversionJCUtil.singleBetContentAnalysis(ticketContent);
				String content = contenta[SplitTicketUtil.NUM_ZERO];
				String passw = contenta[SplitTicketUtil.NUM_ONE];
				if(passw.split(SymbolConstants.UNDERLINE)[0].equals("1")){
					return;
				}
				String a[] = content.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
				Map<String, String> map = new HashMap<String, String>();
				for(String b : a){
					if(b.contains(SymbolConstants.UNDERLINE)){
						map.put(b.substring(0,b.indexOf(SymbolConstants.UNDERLINE)), b.substring(b.indexOf(SymbolConstants.UNDERLINE)));
					}else{
						map.put(b.substring(0,b.indexOf(SymbolConstants.PARENTHESES_LEFT)), b.substring(b.indexOf(SymbolConstants.PARENTHESES_LEFT)));
					}
				}
				Map<String, String> resultMap = SplitTicketUtil.sortMapByKey(map);
				StringBuffer sb = new StringBuffer("");
				resultMap.forEach((k,v)-> sb.append(k).append(v).append(SymbolConstants.VERTICAL_BAR));
				ticketInfoBO.setTicketContent(sb.deleteCharAt(sb.length()-1).append(SymbolConstants.UP_CAP).append(passw).toString());
				if(!ObjectUtil.isBlank(ticketInfoBO.getBuyScreen())){
					String bs[] = ticketInfoBO.getBuyScreen().split(SymbolConstants.COMMA);
					Collections.sort(Arrays.asList(bs));
					ticketInfoBO.setBuyScreen(StringUtils.join(bs,SymbolConstants.COMMA));
				}
			});
		}
	}
	
	@Override
	public void single(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO,Map<String,SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) {
		String[] contentMessage = analysisContent(orderDetailBO, orderInfoBO.getMultipleNum());
		//投注内容
		String content = contentMessage[0];
		//过关方式
		String passway = contentMessage[1];
		//当个方案详情的总倍数 = 详情倍数 * 订单倍数
		Integer multiple = Integer.valueOf(contentMessage[2]);
		//方案投注的选项内容，是手选 || 机选
		Integer contType = SplitTicketUtil.NUM_ONE;
		List<String> list = SplitTicketUtil.getByMultipleSplit(multiple, passway, content, lotteryTypeBO.getSplitMaxNum());
		if(!ObjectUtil.isBlank(list)){
			String[] s = (String[])list.toArray(new String[list.size()]);
			String appOrderDetailIdList = StringUtils.join(s, SymbolConstants.UP_CAP+orderDetailBO.getId()+SymbolConstants.AND)+(SymbolConstants.UP_CAP+orderDetailBO.getId());
			List<String> aList = Arrays.asList(appOrderDetailIdList.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.AND));
			super.assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, aList ,contType,sportAgainstInfoMap,lotteryTypeBO,lotteryIssuePO,nextLotteryIssuePO);
		}
	}

	@Override
	public void compound(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) {
		String[] contentMessage = analysisContent(orderDetailBO, orderInfoBO.getMultipleNum());
		//投注内容
		String content = contentMessage[0];
		//过关方式
		String passway = contentMessage[1];
		//当个方案详情的总倍数 = 详情倍数 * 订单倍数
		Integer multiple = Integer.valueOf(contentMessage[2]);
		//方案投注的选项内容，是手选 || 机选
		Integer contType = SplitTicketUtil.NUM_ONE;
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		compoundAndDantuoHandler(orderDetailBO, orderInfoBO, ticketInfoBOs, lotteryTypeBO, sportAgainstInfoMap,
				lotteryIssuePO, nextLotteryIssuePO, content, passway, multiple, contType, splitMaxAmount);
	}


	
	
	@Override
	public void dantuo(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) {
		String[] contentMessage = analysisContent(orderDetailBO, orderInfoBO.getMultipleNum());
		//投注内容
		String content = contentMessage[0];
		//过关方式
		String passway = contentMessage[1];
		//当个方案详情的总倍数 = 详情倍数 * 订单倍数
		Integer multiple = Integer.valueOf(contentMessage[2]);
		//方案投注的选项内容，是手选 || 机选
		String[] bs = orderDetailBO.getBuyScreen().split(SymbolConstants.COMMA);
		String[] passways = passway.split(SymbolConstants.COMMA);
		if(passways.length == 1){
			String[] a = passways[0].split(SymbolConstants.DOUBLE_SLASH +SymbolConstants.UNDERLINE);
			if(bs.length == Integer.valueOf(a[0])){
				content = content.replaceAll(SymbolConstants.NUMBER_SIGN, SymbolConstants.VERTICAL_BAR);
			}
		}
		Integer contType = SplitTicketUtil.NUM_ONE;
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		compoundAndDantuoHandler(orderDetailBO, orderInfoBO, ticketInfoBOs, lotteryTypeBO, sportAgainstInfoMap,
				lotteryIssuePO, nextLotteryIssuePO, content, passway, multiple, contType, splitMaxAmount);
	}
	
	/**
	 * 
	 * @Description: 解析投注内容、过关方式、投注倍数
	 * @param orderDetailBO
	 * @param multipleNum
	 * @return String[]  0：头注内容，1：过关方式，2：单个方案详情的总倍数 = 详情倍数 * 订单倍数
	 * @author wuLong
	 * @date 2017年11月30日 上午11:17:46
	 */
	private String[] analysisContent(OrderDetailBO orderDetailBO,Integer multipleNum){
		//原始投注内容已^分割
		String pc[] = orderDetailBO.getPlanContent().split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
		//投注内容
		String content = pc[SplitTicketUtil.NUM_ZERO];
		//过关方式
		String passway = pc[SplitTicketUtil.NUM_ONE];
		//当个方案详情的总倍数 = 详情倍数 * 订单倍数
		Integer multiple = orderDetailBO.getMultiple()*multipleNum;
		String[] contentMessage = new String[3];
		contentMessage[0] = content;
		contentMessage[1] = passway;
		contentMessage[2] = multiple+"";
		return contentMessage;
	}
	
	/** 
	 * @Description: 胆拖和复式处理
	 * @param orderDetailBO
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param lotteryTypeBO
	 * @param sportAgainstInfoMap
	 * @param lotteryIssuePO
	 * @param nextLotteryIssuePO
	 * @param content
	 * @param passway
	 * @param multiple
	 * @param contType
	 * @param splitMaxAmount
	 * @throws NumberFormatException
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年11月30日 上午11:23:41
	 */
	private void compoundAndDantuoHandler(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO,
			List<TicketInfoBO> ticketInfoBOs, LotteryTypeBO lotteryTypeBO,
			Map<String, SportAgainstInfoBO> sportAgainstInfoMap, LotteryIssuePO lotteryIssuePO,
			LotteryIssuePO nextLotteryIssuePO, String content, String passway, Integer multiple, Integer contType,
			int splitMaxAmount) throws NumberFormatException, ServiceRuntimeException {
		List<String> list;
		if(orderDetailBO.getAmount()>splitMaxAmount){
			list = SportsZsUtil.splitContentToSingle(content, passway, multiple,orderInfoBO.getLotteryCode());
		}else{
			int maxNum = splitMaxAmount / orderDetailBO.getBuyNumber() / 2;
			if(maxNum>lotteryTypeBO.getSplitMaxNum()){
				maxNum = lotteryTypeBO.getSplitMaxNum();
			}
			list = SportsZsUtil.splitMatchByPassWay(content, passway, multiple,lotteryTypeBO,maxNum);
			contType = null;
		}
		if(!ObjectUtil.isBlank(list)){
			String[] s = (String[])list.toArray(new String[list.size()]);
			String appOrderDetailIdList = StringUtils.join(s, SymbolConstants.UP_CAP+orderDetailBO.getId()+SymbolConstants.AND)+(SymbolConstants.UP_CAP+orderDetailBO.getId());
			List<String> aList = Arrays.asList(appOrderDetailIdList.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.AND));
			super.assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, aList ,contType,sportAgainstInfoMap,lotteryTypeBO,lotteryIssuePO,nextLotteryIssuePO);
		}
	}


	@Override
	public LotteryPr getLotteryPr() {
		return LotteryPr.JJC;
	}

}
