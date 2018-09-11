package com.hhly.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.enums.LotteryChildEnum;
import com.hhly.lottosplit.mapper.LotteryIssueDaoMapper;
import com.hhly.lottosplit.mapper.LotteryTypeDaoMapper;
import com.hhly.lottosplit.mapper.OrderInfoDaoMapper;
import com.hhly.lottosplit.mapper.SportAgainstInfoDaoMapper;
import com.hhly.lottosplit.mapper.TicketAlarmInfoDaoMapper;
import com.hhly.lottosplit.persistence.issue.po.LotteryIssuePO;
import com.hhly.lottosplit.rabbitmq.consumer.SplitTicketMessageListener;
import com.hhly.lottosplit.service.OrderService;
import com.hhly.lottosplit.service.SplitTicketService;
import com.hhly.lottosplit.service.TicketInfoService;
import com.hhly.lottosplit.service.ticket.biglottery.NumberHighColorSplitTicket;
import com.hhly.lottosplit.service.ticket.biglottery.OldZcSplitTicket;
import com.hhly.lottosplit.service.ticket.biglottery.SportsSplitTicket;
import com.hhly.lottosplit.service.ticket.biglottery.WinSplitTicket;
import com.hhly.lottosplit.utils.DateUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;

import net.sf.json.JSONObject;


/**
 * 测试类
 * @author
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class OrderServiceTest extends AbstractJUnit4SpringContextTests{
    @Autowired
    SplitTicketService splitTicketService;
    @Autowired
    OrderService orderService;
    @Autowired
    TicketInfoService ticketInfoService;
    @Autowired
    SplitTicketMessageListener listener;
    @Autowired
    OrderInfoDaoMapper orderInfoDaoMapper;
    @Autowired
    LotteryIssueDaoMapper lotteryIssueDaoMapper;
    @Autowired
    LotteryTypeDaoMapper lotteryTypeDaoMapper;
    @Autowired
    SportAgainstInfoDaoMapper againstInfoDaoMapper;
    @Autowired
    TicketAlarmInfoDaoMapper ticketAlarmInfoDaoMapper;
    @Autowired
    NumberHighColorSplitTicket numberHighColorSplitTicket;
    @Autowired
    SportsSplitTicket sportsSplitTicket;
    @Autowired
    OldZcSplitTicket oldZcSplitTicket;

	@Autowired
	WinSplitTicket winSplitTicket;

    @Test
    public void testSplitTicket() throws Exception{
    	List<OrderInfoBO> list = orderService.getOrderInfos(Arrays.asList("O2016121916233800086"));
    	for(OrderInfoBO a : list){
    		System.out.println(JSONObject.fromObject(a));
    	}
    	
    }
    
    @Test
    public void testCountByOrderCode()throws Exception{
    	System.out.println(ticketInfoService.findCountByOrderCode("D1705231225230100053"));
    }
    
    @Test
    public void testMapper(){
    	orderInfoDaoMapper.findOrderDetail("D1705231225230100053");
    	orderInfoDaoMapper.getOrderCodes(1);
    	lotteryIssueDaoMapper.findLotteryIssue(302, "170406");
    	lotteryTypeDaoMapper.findMap(Arrays.asList(302));
    	Set<String> set = new HashSet<>();
    	set.addAll(Arrays.asList("1703234003"));
    	againstInfoDaoMapper.findSportAgainstInfoS(set,300);
    	ticketAlarmInfoDaoMapper.getAlarmConfig(4);
    }
    @Test
    public void TestSsq() throws Exception{
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(100);
    	orderInfoBO.setMultipleNum(1);
    	orderInfoBO.setOrderAmount(40d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.SSQ_DT.getValue());
    	orderDetailBO.setContentType(3);
    	orderDetailBO.setMultiple(20);
    	orderDetailBO.setAmount(40d);
    	orderDetailBO.setBuyNumber(1);
    	orderDetailBO.setPlanContent("06#03,13,20,27,34+06");
    	bos.add(orderDetailBO);
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(100);
    	orderInfoBO.setDetailBOs(bos);
    	long s = System.currentTimeMillis();
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	System.out.println((System.currentTimeMillis()-s));
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-08 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    @Test
    public void TestSsqFushi() throws Exception{
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(100);
    	orderInfoBO.setMultipleNum(1);
    	orderInfoBO.setOrderAmount(17160d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.SSQ_PT.getValue());
    	orderDetailBO.setContentType(2);
    	orderDetailBO.setMultiple(1);
    	orderDetailBO.setAmount(17160d);
    	orderDetailBO.setBuyNumber(8580);
    	orderDetailBO.setPlanContent("02,07,08,09,10,14,15,23,24,26,31,32,33|03,04,05,11,13");
    	bos.add(orderDetailBO);
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(100);
    	orderInfoBO.setDetailBOs(bos);
    	long s = System.currentTimeMillis();
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	System.out.println((System.currentTimeMillis()-s));
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-08 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    @Test
    public void TestSsqDanShi() throws Exception{
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(100);
    	orderInfoBO.setMultipleNum(1);
    	orderInfoBO.setOrderAmount(12d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.SSQ_PT.getValue());
    	orderDetailBO.setContentType(1);
    	orderDetailBO.setMultiple(1);
    	orderDetailBO.setAmount(2d);
    	orderDetailBO.setBuyNumber(1);
    	orderDetailBO.setPlanContent("02,03,14,16,25,28|08");
    	bos.add(orderDetailBO);
    	
    	OrderDetailBO orderDetailBO1 = new OrderDetailBO();
    	orderDetailBO1.setLotteryChildCode(LotteryChildEnum.LotteryChild.SSQ_PT.getValue());
    	orderDetailBO1.setContentType(1);
    	orderDetailBO1.setMultiple(1);
    	orderDetailBO1.setAmount(2d);
    	orderDetailBO1.setBuyNumber(1);
    	orderDetailBO1.setPlanContent("03,16,23,25,27,31|01");
    	bos.add(orderDetailBO1);
    	
    	OrderDetailBO orderDetailBO2 = new OrderDetailBO();
    	orderDetailBO2.setLotteryChildCode(LotteryChildEnum.LotteryChild.SSQ_PT.getValue());
    	orderDetailBO2.setContentType(1);
    	orderDetailBO2.setMultiple(1);
    	orderDetailBO2.setAmount(2d);
    	orderDetailBO2.setBuyNumber(1);
    	orderDetailBO2.setPlanContent("01,10,15,24,25,28|16");
    	bos.add(orderDetailBO2);
    	
    	
    	OrderDetailBO orderDetailBO3 = new OrderDetailBO();
    	orderDetailBO3.setLotteryChildCode(LotteryChildEnum.LotteryChild.SSQ_PT.getValue());
    	orderDetailBO3.setContentType(1);
    	orderDetailBO3.setMultiple(1);
    	orderDetailBO3.setAmount(2d);
    	orderDetailBO3.setBuyNumber(1);
    	orderDetailBO3.setPlanContent("01,03,04,11,23,30|06");
    	bos.add(orderDetailBO3);
    	
    	
    	OrderDetailBO orderDetailBO4 = new OrderDetailBO();
    	orderDetailBO4.setLotteryChildCode(LotteryChildEnum.LotteryChild.SSQ_PT.getValue());
    	orderDetailBO4.setContentType(1);
    	orderDetailBO4.setMultiple(1);
    	orderDetailBO4.setAmount(2d);
    	orderDetailBO4.setBuyNumber(1);
    	orderDetailBO4.setPlanContent("09,19,23,24,31,33|15");
    	bos.add(orderDetailBO4);
    	
    	OrderDetailBO orderDetailBO5 = new OrderDetailBO();
    	orderDetailBO5.setLotteryChildCode(LotteryChildEnum.LotteryChild.SSQ_PT.getValue());
    	orderDetailBO5.setContentType(1);
    	orderDetailBO5.setMultiple(1);
    	orderDetailBO5.setAmount(2d);
    	orderDetailBO5.setBuyNumber(1);
    	orderDetailBO5.setPlanContent("05,15,17,26,28,33|11");
    	bos.add(orderDetailBO5);
    	
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(100);
    	orderInfoBO.setDetailBOs(bos);
    	long s = System.currentTimeMillis();
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	System.out.println((System.currentTimeMillis()-s));
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-13 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    @Test
    public void TestJc() throws Exception{
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(300);
    	orderInfoBO.setMultipleNum(1);
    	orderInfoBO.setOrderAmount(10d);
    	orderInfoBO.setLotteryIssue("171130");
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setId(10088071);
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.ID_JCZQ.getValue());
    	orderDetailBO.setContentType(3);
    	orderDetailBO.setMultiple(1);
    	orderDetailBO.setAmount(8d);
    	orderDetailBO.setBuyNumber(4);
    	orderDetailBO.setCodeWay(1);
    	orderDetailBO.setBuyScreen("1711304003,1711304004,1711304005");
    	orderDetailBO.setPlanContent("1711304003(3@5.70,1@4.00)#1711304004(3@7.50)|1711304004(3@9.65)^2_1^1");
    	bos.add(orderDetailBO);
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(20000);
    	lotteryTypeBO.setSplitMaxNum(99);
    	orderInfoBO.setDetailBOs(bos);
    	long s = System.currentTimeMillis();
    	List<TicketInfoBO> ticketInfoBOs = sportsSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	System.out.println((System.currentTimeMillis()-s));
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-13 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    
    @Test
    public void testFc3d() throws Exception{
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(105);
    	orderInfoBO.setMultipleNum(20);
    	orderInfoBO.setOrderAmount(3960d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO.setContentType(1);
    	orderDetailBO.setMultiple(20);
    	orderDetailBO.setAmount(40d);
    	orderDetailBO.setBuyNumber(1);
    	orderDetailBO.setPlanContent("4|5");
    	bos.add(orderDetailBO);
    	OrderDetailBO orderDetailBO1 = new OrderDetailBO();
    	orderDetailBO1.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO1.setContentType(1);
    	orderDetailBO1.setMultiple(15);
    	orderDetailBO1.setAmount(30d);
    	orderDetailBO1.setBuyNumber(1);
    	orderDetailBO1.setPlanContent("2|4");
    	bos.add(orderDetailBO1);
    	OrderDetailBO orderDetailBO2 = new OrderDetailBO();
    	orderDetailBO2.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO2.setContentType(1);
    	orderDetailBO2.setMultiple(3);
    	orderDetailBO2.setAmount(6d);
    	orderDetailBO2.setBuyNumber(1);
    	orderDetailBO2.setPlanContent("2|8");
    	bos.add(orderDetailBO2);
    	OrderDetailBO orderDetailBO3 = new OrderDetailBO();
    	orderDetailBO3.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO3.setContentType(1);
    	orderDetailBO3.setMultiple(10);
    	orderDetailBO3.setAmount(20d);
    	orderDetailBO3.setBuyNumber(1);
    	orderDetailBO3.setPlanContent("8|2");
    	bos.add(orderDetailBO3);
    	
    	OrderDetailBO orderDetailBO4 = new OrderDetailBO();
    	orderDetailBO4.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO4.setContentType(1);
    	orderDetailBO4.setMultiple(1);
    	orderDetailBO4.setAmount(2d);
    	orderDetailBO4.setBuyNumber(1);
    	orderDetailBO4.setPlanContent("1|4");
    	bos.add(orderDetailBO4);
    	
    	OrderDetailBO orderDetailBO5 = new OrderDetailBO();
    	orderDetailBO5.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO5.setContentType(1);
    	orderDetailBO5.setMultiple(10);
    	orderDetailBO5.setAmount(20d);
    	orderDetailBO5.setBuyNumber(1);
    	orderDetailBO5.setPlanContent("1|9");
    	bos.add(orderDetailBO5);
    	
    	OrderDetailBO orderDetailBO6 = new OrderDetailBO();
    	orderDetailBO6.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO6.setContentType(1);
    	orderDetailBO6.setMultiple(15);
    	orderDetailBO6.setAmount(30d);
    	orderDetailBO6.setBuyNumber(1);
    	orderDetailBO6.setPlanContent("5|8");
    	bos.add(orderDetailBO6);
    	
    	OrderDetailBO orderDetailBO7 = new OrderDetailBO();
    	orderDetailBO7.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO7.setContentType(1);
    	orderDetailBO7.setMultiple(5);
    	orderDetailBO7.setAmount(10d);
    	orderDetailBO7.setBuyNumber(1);
    	orderDetailBO7.setPlanContent("4|3");
    	bos.add(orderDetailBO7);
    	
    	OrderDetailBO orderDetailBO8 = new OrderDetailBO();
    	orderDetailBO8.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO8.setContentType(1);
    	orderDetailBO8.setMultiple(10);
    	orderDetailBO8.setAmount(20d);
    	orderDetailBO8.setBuyNumber(1);
    	orderDetailBO8.setPlanContent("9|3");
    	bos.add(orderDetailBO8);
    	
    	OrderDetailBO orderDetailBO9 = new OrderDetailBO();
    	orderDetailBO9.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO9.setContentType(1);
    	orderDetailBO9.setMultiple(5);
    	orderDetailBO9.setAmount(10d);
    	orderDetailBO9.setBuyNumber(1);
    	orderDetailBO9.setPlanContent("9|2");
    	bos.add(orderDetailBO9);
    	
    	OrderDetailBO orderDetailBO10 = new OrderDetailBO();
    	orderDetailBO10.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
    	orderDetailBO10.setContentType(1);
    	orderDetailBO10.setMultiple(5);
    	orderDetailBO10.setAmount(10d);
    	orderDetailBO10.setBuyNumber(1);
    	orderDetailBO10.setPlanContent("0|4");
    	bos.add(orderDetailBO10);
//    	OrderDetailBO orderDetailBO1 = new OrderDetailBO();
//    	orderDetailBO1.setLotteryChildCode(LotteryChildEnum.LotteryChild.D_G3.getValue());
//    	orderDetailBO1.setContentType(6);
//    	orderDetailBO1.setMultiple(1);
//    	orderDetailBO1.setAmount(60d);
//    	orderDetailBO1.setBuyNumber(30);
//    	orderDetailBO1.setPlanContent("3,5,6,7,9,12,19,23,24,25");
//    	bos.add(orderDetailBO1);
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(100);
    	orderInfoBO.setDetailBOs(bos);
    	long s = System.currentTimeMillis();
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	System.out.println((System.currentTimeMillis()-s));
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-08 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    @Test
    public void testJsks() throws Exception{
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(233);
    	orderInfoBO.setMultipleNum(1);
    	orderInfoBO.setOrderAmount(560d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.JSK3_S.getValue());
    	orderDetailBO.setContentType(6);
    	orderDetailBO.setMultiple(20);
    	orderDetailBO.setAmount(400d);
    	orderDetailBO.setBuyNumber(10);
    	orderDetailBO.setPlanContent("4,5,6,7,8,9,13,14,15,16");
    	bos.add(orderDetailBO);
    	
    	OrderDetailBO orderDetailBO1 = new OrderDetailBO();
    	orderDetailBO1.setLotteryChildCode(LotteryChildEnum.LotteryChild.JSK3_TT3.getValue());
    	orderDetailBO1.setContentType(1);
    	orderDetailBO1.setMultiple(20);
    	orderDetailBO1.setAmount(40d);
    	orderDetailBO1.setBuyNumber(1);
    	orderDetailBO1.setPlanContent("3T");
    	bos.add(orderDetailBO1);
    	
    	OrderDetailBO orderDetailBO2 = new OrderDetailBO();
    	orderDetailBO2.setLotteryChildCode(LotteryChildEnum.LotteryChild.JSK3_TD3.getValue());
    	orderDetailBO2.setContentType(2);
    	orderDetailBO2.setMultiple(10);
    	orderDetailBO2.setAmount(60d);
    	orderDetailBO2.setBuyNumber(3);
    	orderDetailBO2.setPlanContent("333,444,555");
    	bos.add(orderDetailBO2);
    	
    	OrderDetailBO orderDetailBO3 = new OrderDetailBO();
    	orderDetailBO3.setLotteryChildCode(LotteryChildEnum.LotteryChild.JSK3_TF2.getValue());
    	orderDetailBO3.setContentType(2);
    	orderDetailBO3.setMultiple(10);
    	orderDetailBO3.setAmount(60d);
    	orderDetailBO3.setBuyNumber(3);
    	orderDetailBO3.setPlanContent("33,44,55");
    	bos.add(orderDetailBO3);
    	long s = System.currentTimeMillis();
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(100);
    	orderInfoBO.setDetailBOs(bos);
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	System.out.println((System.currentTimeMillis()-s));
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-09 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    @Test
    public void test3D11X5() throws Exception{
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(102);
    	orderInfoBO.setMultipleNum(100);
    	orderInfoBO.setOrderAmount(200d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.DLT_PT.getValue());
    	orderDetailBO.setContentType(1);
    	orderDetailBO.setMultiple(1);
    	orderDetailBO.setAmount(2d);
    	orderDetailBO.setBuyNumber(1);
    	orderDetailBO.setPlanContent("01,02,03,04,05|01,02");
    	bos.add(orderDetailBO);
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(99);
    	orderInfoBO.setDetailBOs(bos);
    	long s = System.currentTimeMillis();
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-12 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    @Test
    public void TestPlw() throws Exception{
//    	0,1,2,3,4,5,6|0,1,2,3,4,5,6|0,1,2,3,4,6|0,1,2,3|0,1,2,3,4
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(103);
    	orderInfoBO.setMultipleNum(1);
    	orderInfoBO.setOrderAmount(200000d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setContentType(2);
    	orderDetailBO.setMultiple(1);
    	orderDetailBO.setAmount(200000d);
    	orderDetailBO.setBuyNumber(100000);
    	orderDetailBO.setPlanContent("0,1,2,3,4,5,6,7,8,9|0,1,2,3,4,5,6,7,8,9|0,1,2,3,4,5,6,7,8,9|0,1,2,3,4,5,6,7,8,9|0,1,2,3,4,5,6,7,8,9");
    	bos.add(orderDetailBO);
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(99);
    	orderInfoBO.setDetailBOs(bos);
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-13 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }

    
    @Test
    public void TestCqssc() throws Exception{
//    	0,1,2,3,4,5,6|0,1,2,3,4,5,6|0,1,2,3,4,6|0,1,2,3|0,1,2,3,4
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(201);
    	orderInfoBO.setMultipleNum(1);
    	orderInfoBO.setOrderAmount(1308d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setContentType(2);
    	orderDetailBO.setMultiple(1);
    	orderDetailBO.setAmount(1152d);
    	orderDetailBO.setBuyNumber(576);
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.CQSSC_5T.getValue());
    	orderDetailBO.setPlanContent("3,4|4,5,6|1,2,5,7|2,3,5,8|2,3,4,5,6,8");
    	bos.add(orderDetailBO);
    	
    	OrderDetailBO orderDetailBO1 = new OrderDetailBO();
    	orderDetailBO1.setContentType(1);
    	orderDetailBO1.setMultiple(1);
    	orderDetailBO1.setAmount(2d);
    	orderDetailBO1.setBuyNumber(1);
    	orderDetailBO1.setLotteryChildCode(LotteryChildEnum.LotteryChild.CQSSC_3.getValue());
    	orderDetailBO1.setPlanContent("2|3|3");
    	bos.add(orderDetailBO1);
    	
    	OrderDetailBO orderDetailBO2 = new OrderDetailBO();
    	orderDetailBO2.setContentType(2);
    	orderDetailBO2.setMultiple(1);
    	orderDetailBO2.setAmount(16d);
    	orderDetailBO2.setBuyNumber(8);
    	orderDetailBO2.setLotteryChildCode(LotteryChildEnum.LotteryChild.CQSSC_3.getValue());
    	orderDetailBO2.setPlanContent("7,8|0,5|1,6");
    	bos.add(orderDetailBO2);
    	
    	OrderDetailBO orderDetailBO3 = new OrderDetailBO();
    	orderDetailBO3.setContentType(3);
    	orderDetailBO3.setMultiple(1);
    	orderDetailBO3.setAmount(20d);
    	orderDetailBO3.setBuyNumber(10);
    	orderDetailBO3.setLotteryChildCode(LotteryChildEnum.LotteryChild.CQSSC_3Z3.getValue());
    	orderDetailBO3.setPlanContent("3#2,4,5,6,7");
    	bos.add(orderDetailBO3);
    	
    	OrderDetailBO orderDetailBO4 = new OrderDetailBO();
    	orderDetailBO4.setContentType(3);
    	orderDetailBO4.setMultiple(1);
    	orderDetailBO4.setAmount(10d);
    	orderDetailBO4.setBuyNumber(5);
    	orderDetailBO4.setLotteryChildCode(LotteryChildEnum.LotteryChild.CQSSC_3Z6.getValue());
    	orderDetailBO4.setPlanContent("2,3#1,4,5,6,7");
    	bos.add(orderDetailBO4);
    	
    	OrderDetailBO orderDetailBO5 = new OrderDetailBO();
    	orderDetailBO5.setContentType(6);
    	orderDetailBO5.setMultiple(1);
    	orderDetailBO5.setAmount(76d);
    	orderDetailBO5.setBuyNumber(38);
    	orderDetailBO5.setLotteryChildCode(LotteryChildEnum.LotteryChild.CQSSC_2.getValue());
    	orderDetailBO5.setPlanContent("3,4,5,6,7,11");
    	bos.add(orderDetailBO5);
    	
    	OrderDetailBO orderDetailBO6 = new OrderDetailBO();
    	orderDetailBO6.setContentType(2);
    	orderDetailBO6.setMultiple(1);
    	orderDetailBO6.setAmount(32d);
    	orderDetailBO6.setBuyNumber(16);
    	orderDetailBO6.setLotteryChildCode(LotteryChildEnum.LotteryChild.CQSSC_DXDS.getValue());
    	orderDetailBO6.setPlanContent("2,1,3,4|2,1,3,4");
    	bos.add(orderDetailBO6);
    	
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(99);
    	orderInfoBO.setDetailBOs(bos);
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-07-11 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
    	System.out.println("sumticketAmount:"+sumticketAmount+",OrderAmount:"+orderInfoBO.getOrderAmount());
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    @Test
    public void testSdklpk3() throws Exception{
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(225);
    	orderInfoBO.setMultipleNum(1);
    	orderInfoBO.setOrderAmount(4d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setContentType(2);
    	orderDetailBO.setMultiple(1);
    	orderDetailBO.setAmount(4d);
    	orderDetailBO.setBuyNumber(2);
    	orderDetailBO.setPlanContent("AA,22");
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.POKER_DZ.getValue());
    	bos.add(orderDetailBO);
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(99);
    	orderInfoBO.setDetailBOs(bos);
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-20 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		System.out.println("sumticketAmount:"+sumticketAmount+",OrderAmount:"+orderInfoBO.getOrderAmount());
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    
    @Test
    public void testOldZc() throws Exception{
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(304);
    	orderInfoBO.setMultipleNum(1);
    	orderInfoBO.setOrderAmount(1062882d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setContentType(2);
    	orderDetailBO.setMultiple(1);
    	orderDetailBO.setAmount(1062882d);
    	orderDetailBO.setBuyNumber(531441);
    	orderDetailBO.setPlanContent("3,1,0|3,1,0|3,1,0|3,1,0|3,1,0|3,1,0|3,1,0|3,1,0|3,1,0|3,1,0|3,1,0|3,1,0|3,1,0|3,1,0");
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.POKER_DZ.getValue());
    	bos.add(orderDetailBO);
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(99);
    	orderInfoBO.setDetailBOs(bos);
    	List<TicketInfoBO> ticketInfoBOs = oldZcSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-21 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		System.out.println("sumticketAmount:"+sumticketAmount+",OrderAmount:"+orderInfoBO.getOrderAmount());
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }
    
    
    @Test
    public void TestSd11X5() throws Exception{
//    	OrderInfoBO orderInfoBO = new OrderInfoBO();
//    	orderInfoBO.setLotteryCode(215);
//    	orderInfoBO.setMultipleNum(1);
//    	orderInfoBO.setOrderAmount(2d);
//    	List<OrderDetailBO> bos = new ArrayList<>();
//    	OrderDetailBO orderDetailBO = new OrderDetailBO();
//    	orderDetailBO.setId(123456465);
//    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.SD11X5_L3.getValue());
//    	orderDetailBO.setContentType(1);
//    	orderDetailBO.setMultiple(1);
//    	orderDetailBO.setAmount(2d);
//    	orderDetailBO.setBuyNumber(1);
//    	orderDetailBO.setPlanContent("01|02|03");
//    	bos.add(orderDetailBO);
//    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
//    	lotteryTypeBO.setSplitMaxAmount(10000);
//    	lotteryTypeBO.setSplitMaxNum(100);
//    	orderInfoBO.setDetailBOs(bos);
    	
    	
    	OrderInfoBO orderInfoBO = new OrderInfoBO();
    	orderInfoBO.setLotteryCode(215);
    	orderInfoBO.setMultipleNum(10000);
    	orderInfoBO.setOrderAmount(20000d);
    	List<OrderDetailBO> bos = new ArrayList<>();
    	OrderDetailBO orderDetailBO = new OrderDetailBO();
    	orderDetailBO.setId(123456465);
    	orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.SD11X5_L5.getValue());
    	orderDetailBO.setContentType(3);
    	orderDetailBO.setMultiple(1);
    	orderDetailBO.setAmount(2d);
    	orderDetailBO.setBuyNumber(1);
    	orderDetailBO.setPlanContent("04,05,06,07#08");
    	bos.add(orderDetailBO);
    	LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
    	lotteryTypeBO.setSplitMaxAmount(10000);
    	lotteryTypeBO.setSplitMaxNum(100);
    	orderInfoBO.setDetailBOs(bos);
    	
    	long s = System.currentTimeMillis();
    	List<TicketInfoBO> ticketInfoBOs = numberHighColorSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
    	LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
    	lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2017-06-08 23:59:59"));
    	BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs,lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if(rs){
			System.out.println(ticketInfoBOs.size());
			for(TicketInfoBO ticketInfoBO : ticketInfoBOs){
				System.out.println(ticketInfoBO.toString());
			}
		}
    }

	@Test
	public void TestGyj() throws Exception {
		OrderInfoBO orderInfoBO = new OrderInfoBO();
		orderInfoBO.setLotteryCode(308);
		orderInfoBO.setMultipleNum(1);
		orderInfoBO.setOrderAmount(400d);
		orderInfoBO.setLotteryIssue("2018");
		List<OrderDetailBO> bos = new ArrayList<>();
		OrderDetailBO orderDetailBO = new OrderDetailBO();
		orderDetailBO.setId(53057);
		orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.ID_GJJC.getValue());
		orderDetailBO.setContentType(3);
		orderDetailBO.setMultiple(100);
		orderDetailBO.setAmount(200d);
		orderDetailBO.setBuyNumber(5);
		orderDetailBO.setCodeWay(1);
		orderDetailBO.setBuyScreen("01");
		orderDetailBO.setPlanContent("01@3.5|02@5.0^1_1^100");
		bos.add(orderDetailBO);
		LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
		lotteryTypeBO.setSplitMaxAmount(20000);
		lotteryTypeBO.setSplitMaxNum(99);
		orderInfoBO.setDetailBOs(bos);
		long s = System.currentTimeMillis();
//		Map<String,SportAgainstInfoBO> sportAgainstInfoBOMap = new HashMap<>();
//		sportAgainstInfoBOMap.put()
		List<TicketInfoBO> ticketInfoBOs = winSplitTicket.excute(orderInfoBO, lotteryTypeBO, null);
		System.out.println((System.currentTimeMillis() - s));
		LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
		lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2018-06-13 23:59:59"));
		BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs, lotteryIssuePO);
		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
		if (rs) {
			System.out.println(ticketInfoBOs.size());
			for (TicketInfoBO ticketInfoBO : ticketInfoBOs) {
				System.out.println(ticketInfoBO.toString());
			}
		}
	}


    @Test
    public void TestGYjInsert() throws Exception {
        OrderInfoBO orderInfoBO = new OrderInfoBO();
        orderInfoBO.setLotteryCode(308);
        orderInfoBO.setMultipleNum(1);
        orderInfoBO.setOrderAmount(400d);
        orderInfoBO.setLotteryIssue("2018");
        List<OrderDetailBO> bos = new ArrayList<>();
        OrderDetailBO orderDetailBO = new OrderDetailBO();
        orderDetailBO.setId(53057);
        orderDetailBO.setLotteryChildCode(LotteryChildEnum.LotteryChild.ID_GJJC.getValue());
        orderDetailBO.setContentType(3);
        orderDetailBO.setMultiple(100);
        orderDetailBO.setAmount(400d);
        orderDetailBO.setBuyNumber(5);
        orderDetailBO.setCodeWay(1);
        orderDetailBO.setBuyScreen("01");
        orderDetailBO.setPlanContent("01@3.5|02@5.0^1_1^100");
        bos.add(orderDetailBO);
        LotteryTypeBO lotteryTypeBO = new LotteryTypeBO();
        lotteryTypeBO.setSplitMaxAmount(20000);
        lotteryTypeBO.setSplitMaxNum(99);
        orderInfoBO.setDetailBOs(bos);
        long s = System.currentTimeMillis();
//		Map<String,SportAgainstInfoBO> sportAgainstInfoBOMap = new HashMap<>();
//		sportAgainstInfoBOMap.put()

//		splitTicketService.handleEntranceSplitTicket(orderInfoBO,);
//		System.out.println((System.currentTimeMillis() - s));
//		LotteryIssuePO lotteryIssuePO = new LotteryIssuePO();
//		lotteryIssuePO.setSaleTime(DateUtil.convertStrToDate("2018-06-13 23:59:59"));
//		BigDecimal sumticketAmount = SplitTicketUtil.sumTicketAmount(ticketInfoBOs, lotteryIssuePO);
//		boolean rs = SplitTicketUtil.compareAmount(BigDecimal.valueOf(orderInfoBO.getOrderAmount()), sumticketAmount);
//		if (rs) {
//			System.out.println(ticketInfoBOs.size());
//			for (TicketInfoBO ticketInfoBO : ticketInfoBOs) {
//				System.out.println(ticketInfoBO.toString());
//			}
//		}
    }

}
