package com.hhly.lottosplit.service.ticket.biglottery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.SportsZsUtil;

/**
 * 
 * @ClassName: BeiJingDcSplitTicket 
 * @Description: 北京单场拆票
 * @author wuLong
 * @date 2017年4月6日 下午4:51:21 
 *
 */
@Component
public class BeiJingDcSplitTicket extends AbstractCSportsSplitTicket {
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
		List<TicketInfoBO> ticketInfoBOS = new ArrayList<>();
		List<OrderDetailBO> detailBOs = orderInfoBO.getDetailBOs();
		LotteryIssuePO lotteryIssuePO = lotteryIssueDaoMapper.findLotteryIssue(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue());
		LotteryIssuePO nextLotteryIssuePO = lotteryIssueDaoMapper.findNextLotteryIssue(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue());
		if(lotteryIssuePO==null){
			throw new ServiceRuntimeException("竞彩彩票彩期："+orderInfoBO.getLotteryIssue()+",不存在");
		}
		for(OrderDetailBO detailBO : detailBOs){
			super.handle(detailBO, orderInfoBO, ticketInfoBOS, lotteryTypeBO, sportAgainstInfoMap,lotteryIssuePO,nextLotteryIssuePO);
		}
		return ticketInfoBOS;
	}

	@Override
	public void single(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) {
		//原始投注内容已^分割
		String pc[] = orderDetailBO.getPlanContent().split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
		//投注内容
		String content = pc[SplitTicketUtil.NUM_ZERO];
		//过关方式
		String passway = pc[SplitTicketUtil.NUM_ONE];
		//当个方案详情的总倍数 = 详情倍数 * 订单倍数
		Integer multiple = orderDetailBO.getMultiple()*orderInfoBO.getMultipleNum();
		//方案投注的选项内容，是手选 || 机选
		int contType = SplitTicketUtil.NUM_ONE;
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
		//原始投注内容已^分割
		String pc[] = orderDetailBO.getPlanContent().split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
		//投注内容
		String content = pc[SplitTicketUtil.NUM_ZERO];
		//过关方式
		String passway = pc[SplitTicketUtil.NUM_ONE];
		//当个方案详情的总倍数 = 详情倍数 * 订单倍数
		Integer multiple = orderDetailBO.getMultiple()*orderInfoBO.getMultipleNum();
		//方案投注的选项内容，是手选 || 机选
		int contType = SplitTicketUtil.NUM_ONE;
		List<String> list = null;
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		if(orderDetailBO.getAmount()>splitMaxAmount){
			list = SportsZsUtil.splitContentToSingle(content, passway, multiple,orderInfoBO.getLotteryCode());
		}else{
			int maxNum = splitMaxAmount / orderDetailBO.getBuyNumber() / 2;
			if(maxNum>lotteryTypeBO.getSplitMaxNum()){
				maxNum = lotteryTypeBO.getSplitMaxNum();
			}
			list = SportsZsUtil.splitMatchByPassWay(content, passway, multiple,lotteryTypeBO,maxNum);
			contType = SplitTicketUtil.NUM_TWO;
		}
		if(!ObjectUtil.isBlank(list)){
			String[] s = (String[])list.toArray(new String[list.size()]);
			String appOrderDetailIdList = StringUtils.join(s, SymbolConstants.UP_CAP+orderDetailBO.getId()+SymbolConstants.AND)+(SymbolConstants.UP_CAP+orderDetailBO.getId());
			List<String> aList = Arrays.asList(appOrderDetailIdList.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.AND));
			super.assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, aList ,contType,sportAgainstInfoMap,lotteryTypeBO,lotteryIssuePO,nextLotteryIssuePO);
		}
	}

	@Override
	public void dantuo(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) {
		//原始投注内容已^分割
		String pc[] = orderDetailBO.getPlanContent().split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
		//投注内容
		String content = pc[SplitTicketUtil.NUM_ZERO];
		//过关方式
		String passway = pc[SplitTicketUtil.NUM_ONE];
		//当个方案详情的总倍数 = 详情倍数 * 订单倍数
		Integer multiple = orderDetailBO.getMultiple()*orderInfoBO.getMultipleNum();
		//方案投注的选项内容，是手选 || 机选
		int contType = SplitTicketUtil.NUM_ONE;
		List<String> list = null;
		String[] bs = orderDetailBO.getBuyScreen().split(SymbolConstants.COMMA);
		String[] passways = passway.split(SymbolConstants.COMMA);
		if(passways.length == 1){
			String[] a = passways[0].split(SymbolConstants.DOUBLE_SLASH +SymbolConstants.UNDERLINE);
			if(bs.length == Integer.valueOf(a[0])){
				content = content.replaceAll(SymbolConstants.NUMBER_SIGN, SymbolConstants.VERTICAL_BAR);
			}
		}
		int splitMaxAmount = lotteryTypeBO.getSplitMaxAmount();
		if(orderDetailBO.getAmount()>splitMaxAmount){
			list = SportsZsUtil.splitContentToSingle(content, passway, multiple,orderInfoBO.getLotteryCode());
		}else{
			int maxNum = splitMaxAmount / orderDetailBO.getBuyNumber() / 2;
			if(maxNum>lotteryTypeBO.getSplitMaxNum()){
				maxNum = lotteryTypeBO.getSplitMaxNum();
			}
			list = SportsZsUtil.splitMatchByPassWay(content, passway, multiple,lotteryTypeBO,maxNum);
			contType = SplitTicketUtil.NUM_TWO;
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
		return LotteryPr.BJDC;
	}
}
