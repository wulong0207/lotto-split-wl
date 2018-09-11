package com.hhly.lottosplit.service.ticket;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.mapper.LotteryIssueDaoMapper;
import com.hhly.lottosplit.persistence.issue.po.LotteryIssuePO;
import com.hhly.lottosplit.utils.DateUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.calcutils.SportsZsUtil;
import com.hhly.lottosplit.utils.convert.ParseConvertTicketInfo;

public abstract class AbstractCSportsSplitTicket extends AbstractCSplitTicket{
	private static Logger logger = LoggerFactory.getLogger(AbstractCSportsSplitTicket.class);
	
	@Autowired
	LotteryIssueDaoMapper lotteryIssueDaoMapper;
	/**
	 * 
	 * @Description: 竞技彩拆票（单式，复式，胆拖）
	 * @param orderDetailBO                  订单详情
	 * @param orderInfoBO                    订单信息 
	 * @param ticketInfoBOs                  收集的票集合
	 * @param lotteryTypeBO                  彩种信息
	 * @param sportAgainstInfoMap            竞技彩赛事信息集合
	 * @param lotteryIssuePO                 当前彩期信息
	 * @param nextLotteryIssuePO             下一期彩期信息
	 * @author wuLong
	 * @date 2017年12月5日 下午4:24:44
	 */
	protected void handle(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO,Map<String,SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) {
		switch (orderDetailBO.getContentType()) {
		case 1:
			single(orderDetailBO, orderInfoBO, ticketInfoBOs, lotteryTypeBO, sportAgainstInfoMap,lotteryIssuePO,nextLotteryIssuePO);
			break;
		case 2:
		case 4:
			compound(orderDetailBO, orderInfoBO, ticketInfoBOs, lotteryTypeBO, sportAgainstInfoMap,lotteryIssuePO,nextLotteryIssuePO);
			break;
		case 3:
			dantuo(orderDetailBO, orderInfoBO, ticketInfoBOs, lotteryTypeBO, sportAgainstInfoMap,lotteryIssuePO,nextLotteryIssuePO);
			break;
		default:
			break;
		}
	}
	/**
	 * 
	 * @Description:  单式
	 * @param orderDetailBO
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param lotteryTypeBO
	 * @param sportAgainstInfoMap
	 * @author wuLong
	 * @date 2017年6月27日 下午4:05:27
	 */
	public abstract void single(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, 
			List<TicketInfoBO> ticketInfoBOs,LotteryTypeBO lotteryTypeBO,Map<String,SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO);
	/**
	 * 
	 * @Description: 复式
	 * @param orderDetailBO
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param lotteryTypeBO
	 * @param sportAgainstInfoMap
	 * @author wuLong
	 * @date 2017年6月27日 下午4:05:20
	 */
	public abstract void compound(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, 
			List<TicketInfoBO> ticketInfoBOs,LotteryTypeBO lotteryTypeBO,Map<String,SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO);
	
	/**
	 * 
	 * @Description: 胆拖
	 * @param orderDetailBO
	 * @param orderInfoBO
	 * @param ticketInfoBOs
	 * @param lotteryTypeBO
	 * @param sportAgainstInfoMap
	 * @author wuLong
	 * @date 2017年6月27日 下午4:05:33
	 */
	public abstract void dantuo(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			LotteryTypeBO lotteryTypeBO,Map<String,SportAgainstInfoBO> sportAgainstInfoMap,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO);
	/**
	 * 
	 * @Description: 按照子玩法和投注内容进行票的分类封装对象
	 * @param detailVO                      订单详情
	 * @param orderInfoBO                   订单信息
	 * @param ticketInfoBOs                 票集合
	 * @param list                          组装的每一注
	 * @param contType                      投注类型
	 * @param sportAgainstInfoMap           竞技彩赛事信息集合
	 * @param lotteryTypeBO                 彩种信息
	 * @param lotteryIssuePO                当前彩期信息
	 * @param nextLotteryIssuePO            下一期彩期信息
	 * @throws NumberFormatException
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年12月5日 下午4:28:11
	 */
	protected void assembleData(OrderDetailBO detailVO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			List<String> list,Integer contType,Map<String,SportAgainstInfoBO> sportAgainstInfoMap
			,LotteryTypeBO lotteryTypeBO,LotteryIssuePO lotteryIssuePO,LotteryIssuePO nextLotteryIssuePO) throws NumberFormatException, ServiceRuntimeException {
		for(String c : list){
			String a[] = c.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
			int multipleNum = Integer.valueOf(a[SplitTicketUtil.NUM_TWO]);
			String passway = a[SplitTicketUtil.NUM_ONE];
			String content = a[SplitTicketUtil.NUM_ZERO];
			int orderDetailId = Integer.valueOf(a[SplitTicketUtil.NUM_THREE]);
			if(multipleNum > lotteryTypeBO.getSplitMaxNum()){
				List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multipleNum, content + SymbolConstants.UP_CAP+ passway, lotteryTypeBO.getSplitMaxNum());
				for(String d : strings){
					TicketInfoBO ticketInfoBO = new TicketInfoBO();
					String e[] = d.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UP_CAP);
					multipleNum = Integer.valueOf(e[SplitTicketUtil.NUM_TWO]);
					passway = e[SplitTicketUtil.NUM_ONE];
					content = e[SplitTicketUtil.NUM_ZERO];
					assemleDateTwo(detailVO, orderInfoBO, ticketInfoBOs, sportAgainstInfoMap, ticketInfoBO, passway,
							multipleNum, content,contType,lotteryIssuePO,orderDetailId,nextLotteryIssuePO);
					ticketInfoBO = null;
				}
			}else{
				TicketInfoBO ticketInfoBO = new TicketInfoBO();
				assemleDateTwo(detailVO, orderInfoBO, ticketInfoBOs, sportAgainstInfoMap, ticketInfoBO, passway,
						multipleNum, content,contType,lotteryIssuePO,orderDetailId,nextLotteryIssuePO);
				ticketInfoBO = null;
			}
		}
	}
	
	/**
	 * 
	 * @Description:转换到单张票对象 
	 * @param detailVO                    订单详情
	 * @param orderInfoBO                 订单信息
	 * @param ticketInfoBOs               票集合
	 * @param sportAgainstInfoMap         竞技彩赛事信息集合
	 * @param ticketInfoBO                单张票
	 * @param passway                     票过关方式
	 * @param multipleNum                 票倍数
	 * @param content                     票投注内容
	 * @param contType                    票的投注类型
	 * @param lotteryIssuePO              当前彩期信息
	 * @param orderDetailId               订单详情ID  
	 * @param nextLotteryIssuePO          下一期彩期信息
	 * @throws ServiceRuntimeException
	 * @author wuLong
	 * @date 2017年12月5日 下午4:35:36
	 */
	protected void assemleDateTwo(OrderDetailBO detailVO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
			Map<String,SportAgainstInfoBO> sportAgainstInfoMap, TicketInfoBO ticketInfoBO, String passway,
			int multipleNum, String content,Integer contType,LotteryIssuePO lotteryIssuePO,Integer orderDetailId,LotteryIssuePO nextLotteryIssuePO) throws ServiceRuntimeException {
		ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO,detailVO,ticketInfoBO);
		if(contType!=null){
			ticketInfoBO.setContentType(contType);
		}else{
			ticketInfoBO.setContentType(SplitTicketUtil.getContentType(content));
		}
		ticketInfoBO.setOrderDetailId(orderDetailId);
		ticketInfoBO.setTicketContent(content+SymbolConstants.UP_CAP+passway);
		Object[] objects = SplitTicketUtil.convertTicketInfoContentLotteryChilCode(content,detailVO.getLotteryChildCode());
		if(objects!=null){
			ticketInfoBO.setLotteryChildCode((int)objects[SplitTicketUtil.NUM_ZERO]);
			ticketInfoBO.setTicketContent(objects[SplitTicketUtil.NUM_ONE].toString()+SymbolConstants.UP_CAP+passway);
		}
		int zs = SplitTicketUtil.NUM_ONE;
		if(ticketInfoBO.getContentType()==SplitTicketUtil.NUM_TWO){
			zs = SportsZsUtil.getSportsManyNote(ticketInfoBO.getTicketContent(),orderInfoBO.getLotteryCode());
		}
		ticketInfoBO.setBuyScreen(SplitTicketUtil.getBuyScreen(content));
		ticketInfoBO.setMultipleNum(multipleNum);
		ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(zs*SplitTicketUtil.NUM_TWO*multipleNum)));
		SportAgainstInfoBO sportAgainstInfoBO = SplitTicketUtil.getSportsTicketFirstPlayTime(ticketInfoBO.getBuyScreen(), sportAgainstInfoMap);
		Date firstPlayTime = sportAgainstInfoBO.getStartTime();
		Date endTicketTime = getEndTicketTime(firstPlayTime, lotteryIssuePO, sportAgainstInfoBO.getIssueCode(),nextLotteryIssuePO,orderInfoBO.getBuyTime());
//		logger.info("订单编号:{},出票截止时间:{}",orderInfoBO.getOrderCode(),DateUtil.convertDateToStr(endTicketTime, DateUtil.DEFAULT_FORMAT));
		ticketInfoBO.setEndTicketTime(endTicketTime);
		ticketInfoBO.setStartMatchtime(firstPlayTime);
		ticketInfoBOs.add(ticketInfoBO);
	}
	
	public static void main(String[] args) {
		LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
		lotteryIssuePO.setOfficialEndTime(DateUtil.convertStrToDate("2018-02-04 00:45:00"));
		lotteryIssuePO.setIssueCode("180203");
		lotteryIssuePO.setCurrentIssue((short)0);
		
		LotteryIssuePO nextLotteryIssuePO = new LotteryIssuePO();
		nextLotteryIssuePO.setOfficialStartTime(DateUtil.convertStrToDate("2018-02-04 09:00:00"));
		nextLotteryIssuePO.setIssueCode("180204");
		
		System.out.println(getEndTicketTime(DateUtil.convertStrToDate("2018-02-04 01:00:00"), 
				lotteryIssuePO, "180203", 
				nextLotteryIssuePO, DateUtil.convertStrToDate("2018-02-04 02:49:17")));
		
	}
	
	/**
	 * 
	 * @Description: 竞技彩票截止时间计算
	 * @param firstPlayTime 最先开打的赛事
	 * @param lotteryIssuePO 彩期信息
	 * @param nextLotteryIssuePO 下一期彩期信息
	 * @param issueCode 最先开打的赛事对应的期号
	 * @return
	 * @author wuLong
	 * @date 2017年8月10日 下午12:04:51
	 */
	public static Date getEndTicketTime(Date firstPlayTime,LotteryIssuePO lotteryIssuePO,String issueCode,LotteryIssuePO nextLotteryIssuePO,Date buyTime){
		Date officialEndTime = lotteryIssuePO.getOfficialEndTime();
		Date endTicketTime = null;
		Date now = new Date();
		Calendar cd = Calendar.getInstance();
		String nextOfficialStartTime = " 09:30:00";
		if(nextLotteryIssuePO!=null){
			Calendar nextLotteryIssueCalendar = Calendar.getInstance();
			nextLotteryIssueCalendar.setTime(nextLotteryIssuePO.getOfficialStartTime());
			nextLotteryIssueCalendar.add(Calendar.MINUTE, 30);
			nextOfficialStartTime = DateUtil.convertDateToStr(nextLotteryIssueCalendar.getTime(), " HH:mm:ss");
		}
		//当前期的截止时间处理
		if(lotteryIssuePO.getCurrentIssue() == 1 && lotteryIssuePO.getIssueCode().equals(issueCode)){
			cd.setTime(now);
			cd.add(Calendar.DATE, -1);
			Date sTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(cd.getTime(), DateUtil.DATE_FORMAT+" 12:00:00"));//7 12:00
			Date eTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(now, DateUtil.DATE_FORMAT+nextOfficialStartTime));// 8 09:30
			cd = Calendar.getInstance();
			cd.setTime(now);
			Date nowStime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(cd.getTime(), DateUtil.DATE_FORMAT+" 12:00:00"));//8 12:00
			cd.add(Calendar.DATE, 1);
			Date nowEtime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(cd.getTime(), DateUtil.DATE_FORMAT+nextOfficialStartTime));//9 09:30
//			logger.info("当前期订单最新开赛时间:{},彩期截止时间:{},比对的开始时间:{},比对的结束时间:{},当前时间:{}",
//					new String[]{DateUtil.convertDateToStr(firstPlayTime, DateUtil.DEFAULT_FORMAT)
//					,DateUtil.convertDateToStr(officialEndTime, DateUtil.DEFAULT_FORMAT)
//					,DateUtil.convertDateToStr(cd.getTime(), DateUtil.DATE_FORMAT+" 12:00:00")
//					,DateUtil.convertDateToStr(cd.getTime(), DateUtil.DATE_FORMAT+nextOfficialStartTime)
//					,DateUtil.convertDateToStr(now, DateUtil.DEFAULT_FORMAT)});
			
			if(firstPlayTime.compareTo(nowStime)>0&&firstPlayTime.compareTo(nowEtime)<0){
				sTime = nowStime;
				eTime = nowEtime;
			}
			if(firstPlayTime.compareTo(sTime)>0&&firstPlayTime.compareTo(eTime)<0){
				endTicketTime = firstPlayTime.compareTo(officialEndTime) < 0  ? firstPlayTime : officialEndTime;
				return endTicketTime;
			}
			if(endTicketTime==null){
				if(firstPlayTime.compareTo(eTime)> 0){
					endTicketTime = firstPlayTime;
				}
			}
			if(endTicketTime==null){
				endTicketTime = firstPlayTime;
			}
			if(endTicketTime.compareTo(buyTime)<0&&endTicketTime.compareTo(eTime)<0){
				endTicketTime = eTime;
			}
		}else{
			cd = Calendar.getInstance();
			cd.setTime(now);
			Date sTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(cd.getTime(), DateUtil.DATE_FORMAT+" 00:00:00"));
			Date eTime = DateUtil.convertStrToDate(DateUtil.convertDateToStr(now, DateUtil.DATE_FORMAT+nextOfficialStartTime));
			logger.info("不是当前期订单最新开赛时间:{},彩期截止时间:{},比对的开始时间:{},比对的结束时间:{},当前时间:{}",
					new String[]{DateUtil.convertDateToStr(firstPlayTime, DateUtil.DEFAULT_FORMAT)
					,DateUtil.convertDateToStr(officialEndTime, DateUtil.DEFAULT_FORMAT)
					,DateUtil.convertDateToStr(cd.getTime(), DateUtil.DATE_FORMAT+" 12:00:00")
					,DateUtil.convertDateToStr(cd.getTime(), DateUtil.DATE_FORMAT+nextOfficialStartTime)
					,DateUtil.convertDateToStr(now, DateUtil.DEFAULT_FORMAT)});
			if(firstPlayTime.compareTo(sTime)>=0&&firstPlayTime.compareTo(eTime)<0){
				endTicketTime = eTime;
				return endTicketTime;
			}
			if(endTicketTime==null){
				if(firstPlayTime.compareTo(eTime)> 0){
					endTicketTime = firstPlayTime;
				}
				if(endTicketTime==null){
					endTicketTime = officialEndTime;
				}
			}
			
			if(endTicketTime.compareTo(buyTime)<0&&endTicketTime.compareTo(eTime)<0){
				endTicketTime = eTime;
			}
		}
		return endTicketTime;
	}
}
