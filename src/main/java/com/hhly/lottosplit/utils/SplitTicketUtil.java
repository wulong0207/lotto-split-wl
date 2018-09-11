package com.hhly.lottosplit.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryChildEnum;
import com.hhly.lottosplit.enums.LotteryEnum;
import com.hhly.lottosplit.enums.LotteryEnum.Lottery;
import com.hhly.lottosplit.enums.LotteryEnum.LotteryPr;
import com.hhly.lottosplit.enums.OrderEnum.BuyType;
import com.hhly.lottosplit.enums.OrderEnum.OrderStatus;
import com.hhly.lottosplit.enums.OrderEnum.PayStatus;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.persistence.issue.po.LotteryIssuePO;
import com.hhly.lottosplit.rabbitmq.provider.impl.SendTicketMessageProvider;

import net.sf.json.JSONObject;

/**
 * 
 * @ClassName: SportsSplitTicket 
 * @Description: 足彩拆票工具类 
 * @author wuLong
 * @date 2017年4月5日 下午5:25:51 
 *
 */
@Component
public class SplitTicketUtil {

	private static Logger logger = Logger.getLogger(SplitTicketUtil.class);
	/**
	 * 单式票最多5注可组合在一起
	 */
	public static final int SINGLE_TICKET_ZHUSHU = 5;
	
	
	/**   竞彩足球混投*/
	public static final int ID_FHT = 30001;//混投
	/**   竞彩足球胜平负 */
	public static final int ID_JCZQ = 30002;// 胜平负
	/**   竞彩足球让球胜平负*/
	public static final int ID_RQS = 30003;//让球胜平负
	/**   竞彩足球比分 */
	public static final int ID_FBF = 30004 ;//比分
	/**   竞彩足球总进球 */
	public static final int ID_FZJQ = 30005;//总进球
	/**   竞彩足球半全场 */
	public static final int ID_FBCQ = 30006;//半全场
	
	 /**竞彩篮球胜负**/
	public static final int ID_JCLQ_SF = 30101;
    /**竞彩篮球让分**/
	public static final int ID_JCLQ_RF = 30102;
    /**竞彩篮球大小分**/
	public static final int ID_JCLQ_DXF = 30103;
    /**竞彩篮球胜分差**/
	public static final int ID_JCLQ_SFC = 30104;
    /**竞彩篮球混合过关**/
	public static final int ID_JCLQ_HHGG = 30105;
	
	
	/**   北单让球胜平负*/
	public static final int ID_BD_RQS = 30601;
	/** 北单上下单双 */
	public static final int ID_BD_SXDX = 30602;
	/**   北单总进球 */
	public static final int ID_BD_FZJQ = 30603;
	/**   北单比分 */
	public static final int ID_BD_FBF = 30604 ;
	/**   北单半全场 */
	public static final int ID_BD_FBCQ = 30605;
	
	public static final String SYSTEM = "system";
	/**出票消息队列名**/
	public static final String SENDTICKET_QUEUE = "sendticket_queue";
	/**告警队列名**/
	public static final String ALARM_INFO="alarm_info";
	/**
	 * 竞彩足球彩种ID集
	 */
	private static final int[] SPORT_FOOTBAL_LOTTERYS = {ID_FHT, ID_RQS, ID_JCZQ, ID_FBF, ID_FZJQ, ID_FBCQ};
	
	/**竞彩篮球彩种ID集**/
	private static final int[] SPORT_BASKETBAL_LOTTERYS = {ID_JCLQ_SF, ID_JCLQ_RF, ID_JCLQ_DXF, ID_JCLQ_SFC, ID_JCLQ_HHGG};
	
	/**
	 * 北单足球彩种ID集
	 */
	public static final int[] PK_SPORT_FOOTBAL_LOTTERYS = {ID_BD_RQS, ID_BD_SXDX, ID_BD_FZJQ, ID_BD_FBF, ID_BD_FBCQ};
	/** 数字 2500 **/
	public static final int NUM_FOUR_THOUSAND = 2500;
	/**数字 2000**/
	public static final int NUM_TWO_THOUSAND = 2000;
	/**数字 200**/
	public static final int NUM_TWO_HUNDRED = 200;
	/**数字 50**/
	public static final int NUM_FIFTY = 50;
	/** 字符串 1**/
	public static final String STRING_ONE = "1";
	/** 数字 0**/
	public static final int NUM_ZERO = 0;
	/** 数字 10**/
	public static final int NUM_TEN = 10;
	/** 数字 1**/
	public static final int NUM_ONE = 1;
	/** 数字 9**/
	public static final int NUM_NINE = 9;
	/** 数字2**/
	public static final int NUM_TWO = 2;
	/** 数字6**/
	public static final int NUM_SIX = 6;
	/** 数字4**/
	public static final int NUM_FOUR = 4;
	/** 数字1**/
	public static final int NUM_ELEVEN = 11;
	/** 数字3**/
	public static final int NUM_THREE = 3;
	/** 数字5**/
	public static final int NUM_FIVE = 5;
	
	public static final int NUM_EIGHT = 8;
	
	public static ExecutorService FIXED_THREAD_POOL ; 
	static{
		int poolSize = Integer.parseInt(PropertyUtil.getPropertyValue("application.properties", "pool_size"));
		
		FIXED_THREAD_POOL = Executors.newFixedThreadPool(poolSize);
	}
	/**
	 * 判断彩种ID是否为竞彩足球
	 * @param Id
	 * @return
	 * true:是
	 * false:否
	 *
	 */
	public static boolean checkFBLotteryId(int lotteryId) {
		for(int tempI : SPORT_FOOTBAL_LOTTERYS){
			if(tempI == lotteryId)
				return true;
		}
		return false;
	}
	
	/**
	 * 判断彩种ID是否为竞彩篮球
	 * @param Id
	 * @return
	 * true:是
	 * false:否
	 *
	 */
	public static boolean checkBBLotteryId(int lotteryId) {
		for(int tempI : SPORT_BASKETBAL_LOTTERYS){
			if(tempI == lotteryId)
				return true;
		}
		return false;
	}
	/**
	 * @Description: 判断彩种ID是否为北京单场
	 * @param lotteryId
	 * @return
	 * @author wuLong
	 * @date 2017年6月14日 上午9:58:44
	 */
	public static boolean checkBDLotteryId(int lotteryId) {
		for(int tempI : PK_SPORT_FOOTBAL_LOTTERYS){
			if(tempI == lotteryId)
				return true;
		}
		return false;
	}
	/**
	 * 
	 * @Description: 根据投注的内容格式类型 ，获取竞彩足球子彩种ID
	 * @param type
	 * @return
	 * @author wuLong
	 * @date 2017年6月14日 上午9:56:17
	 */
	public static Integer getBBBySimpleCode(String type){
		switch(type){
		case "R":return ID_JCLQ_RF;
		case "S":return ID_JCLQ_SF;
		case "C":return ID_JCLQ_SFC;
		case "D":return ID_JCLQ_DXF;
		}
		return null;
	}
	/**
	 * @Description: 根据投注的内容格式类型 ，获取竞彩篮球子彩种ID
	 * @param type
	 * @return
	 * @author wuLong
	 * @date 2017年6月14日 上午9:56:02
	 */
	public static Integer getFBBySimpleCode(String type){
		switch(type){
		case "R":return ID_RQS;
		case "S":return ID_JCZQ;
		case "Z":return ID_FZJQ;
		case "Q":return ID_FBF;
		case "B":return ID_FBCQ;
		}
		return null;
	}
	/**
	 * @Description: 根据投注的内容格式类型 ，获取北京单场子彩种ID
	 * @param type
	 * @return
	 * @author wuLong
	 * @date 2017年6月14日 上午9:55:15
	 */
	public static Integer getBDBySimpleCode(String type){
		switch(type){
		case "R":return ID_BD_RQS;
		case "S":return ID_BD_SXDX;
		case "Z":return ID_BD_FZJQ;
		case "Q":return ID_BD_FBF;
		case "B":return ID_BD_FBCQ;
		}
		return null;
	}
	
	/**
	 * 检验需要拆票订单的基本信息
	 * @param orderInfoBO
	 * @throws Exception
	 */
	public static String checkBasicInfo(OrderInfoBO orderInfoBO)throws Exception{
		String result = null;
		try {
			if(ObjectUtil.isBlank(orderInfoBO)){
				throw new ServiceRuntimeException("订单为空");
			}
			List<OrderDetailBO> detailBOs = orderInfoBO.getDetailBOs();
			if(ObjectUtil.isBlank(detailBOs)){
				throw new ServiceRuntimeException("订单详情为空");
			}
			PayStatus payStatus = PayStatus.parsePayStatus(orderInfoBO.getPayStatus());
			OrderStatus orderStatus = OrderStatus.parseOrderStatus(orderInfoBO.getOrderStatus());
			BuyType buyType = BuyType.parseBuyType(orderInfoBO.getBuyType());
			logger.info("订单编号:"+orderInfoBO.getOrderCode()+",当前支付状态:"+payStatus.getDesc()+",当前订单状态:"+orderStatus.getDesc()+",当前订单购买类型:"+buyType.getDesc());
			//支付成功&&订单状态为待拆票&&是足球和篮球彩&&是自购或合买
			if(!PayStatus.SUCCESS_PAY.equals(payStatus)||
					orderStatus!=OrderStatus.WAITING_SPLIT_TICKET){
				throw new ServiceRuntimeException(orderInfoBO.getOrderCode()+",当前支付状态:"+payStatus.getDesc()+",当前订单状态:"+orderStatus.getDesc()+",当前订单购买类型:"+buyType.getDesc()+",不符合做拆票处理");
			}
		} catch (Exception e) {
			result = e.getMessage();
			logger.error(e.getMessage(),e);
		}
		return result;
	}
	
	
	/**
	 * 
	 * @Description: 根据倍数分票
	 * @param multiple 倍数  
	 * @param passway 过关方式 
	 * @param content 内容
	 * @param mp 最高倍数
	 * @return List String
	 * @throws NumberFormatException
	 * @author wuLong
	 * @date 2017年4月8日 上午11:40:38
	 */
	public static List<String> getByMultipleSplit(Integer multiple, String passway, String content, Integer mp) throws NumberFormatException {
		int a = multiple/mp;
		int modular = multiple%mp;
		if(modular > 0){
			a = a + 1;
		}
		List<String> list = new ArrayList<>();
		for(int i = 0; i<a ;i++){
			StringBuffer sb = new StringBuffer();
			sb.append(content);
			if(!ObjectUtil.isBlank(passway)){
				sb.append(SymbolConstants.UP_CAP).append(passway);
			}
			if(i==a-1 && modular>0){
				sb.append(SymbolConstants.UP_CAP).append(modular);
			}else{
				sb.append(SymbolConstants.UP_CAP).append(mp);
			}
			list.add(sb.toString());
		}
		return list;
	}
	
	/** 
     * 使用 Map按key进行排序 
     * @param map 
     * @return 
     */  
    public static Map<String, String> sortMapByKey(Map<String, String> map) {  
        if (map == null || map.isEmpty()) {  
            return null;  
        }  
  
        Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});  
  
        sortMap.putAll(map);  
  
        return sortMap;  
    }  
	
	/**
	 * 
	 * @Description: list拆分
	 * @param mb 多少条  为一份
	 * @param list 总记录
	 * @return
	 * @throws NumberFormatException
	 * @author wuLong
	 * @param <T>
	 * @date 2017年5月24日 下午2:51:59
	 */
	public static <T> List<List<T>> subList(Integer mb , List<T> list) throws NumberFormatException{
		int a = list.size();
		int b = a/mb;
		int modular = a%mb;
		if(modular > 0){
			b = b + 1;
		}
		List<List<T>> lists = new ArrayList<>();
		for(int i = 0; i<b ;i++){
			if(i==b-1 && modular>0){
				lists.add(list.subList(i*mb, i*mb+modular));
			}else{
				lists.add(list.subList(i*mb, (i+1)*mb));
			}
		}
		return lists;
	}
	public static List<String> getNumberHighColorByMultipleSplit(Integer multiple,String content,Integer mp)throws NumberFormatException {
		int a = multiple/mp;
		int modular = multiple%mp;
		if(modular > 0){
			a = a + 1;
		}
		List<String> list = new ArrayList<>();
		for(int i = 0; i<a ;i++){
			StringBuffer sb = new StringBuffer();
			sb.append(content);
			if(i==a-1 && modular>0){
				sb.append(SymbolConstants.UP_CAP).append(modular);
			}else{
				sb.append(SymbolConstants.UP_CAP).append(mp);
			}
			list.add(sb.toString());
		}
		return list;
	}
	
	/**
	 * 
	 * @Description: 多注打在一张票上面 String 以 “；”分号分隔
	 * @param list
	 * @param strings
	 * @param zs
	 * @author wuLong
	 * @date 2017年4月10日 上午11:57:42
	 */
	public static void joinToOneTicket(List<String> toList, List<String> oriList,Integer zs)throws ServiceRuntimeException  {
		int size = oriList.size();
		int c = size/zs;
		int d = size%zs;
		if(d>0){
			c = c+1;
		}
		List<String> abc;
		for(int i=0;i<c;i++){
			if(i==c-1){
				abc = oriList.subList(i*SINGLE_TICKET_ZHUSHU, size);
			}else{
				abc = oriList.subList(i*SINGLE_TICKET_ZHUSHU, (i+1)*SINGLE_TICKET_ZHUSHU);
			}
			toList.add(StringUtils.join(abc, SymbolConstants.SEMICOLON));
		}
	}

	/**
	 * 获取冠军竞猜投注赛事
	 *
	 * @param content
	 * @return
	 * @throws ServiceRuntimeException
	 */
	public static String getGjjcBuyScreen(String content) throws ServiceRuntimeException {
		if (ObjectUtil.isBlank(content)) {
			return null;
		}
		String[] ct = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		StringBuffer sb = new StringBuffer();
		for (String a : ct) {
			if (a.contains(SymbolConstants.AT)) {
				sb.append(a.substring(0, a.indexOf(SymbolConstants.AT)));
			}
			sb.append(SymbolConstants.COMMA);
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}


	/**
	 * 获取投注赛事系统编号
	 *
	 * @param content
	 * @return
	 * @throws ServiceRuntimeException
	 */
	public static String getBuyScreen(String content)throws ServiceRuntimeException {
		if(ObjectUtil.isBlank(content)){
			return null;
		}
		String[] ct = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		StringBuffer sb = new StringBuffer();
		for (String a : ct){
			if(a.contains(SymbolConstants.UNDERLINE)){
				sb.append(a.substring(0,a.indexOf(SymbolConstants.UNDERLINE)));
			}else{
				if(a.contains(SymbolConstants.MIDDLE_PARENTHESES_LEFT)){
					sb.append(a.substring(0,a.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT)));
				}else{
					sb.append(a.substring(0,a.indexOf(SymbolConstants.PARENTHESES_LEFT)));
				}
			}
			sb.append(SymbolConstants.COMMA);
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
	
	public static int getContentType(String content)throws ServiceRuntimeException {
		String[] ct = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		int contentType = 1;
		for (String a : ct){
			String b = a.substring(a.indexOf(SymbolConstants.PARENTHESES_LEFT)+1, a.indexOf(SymbolConstants.PARENTHESES_RIGHT));
			int c = b.split(SymbolConstants.COMMA).length;
			if(c>1){
				contentType = 2;
				break;
			}
		}
		return contentType;
	}

	/**
	 * 获取冠亚军竞猜解析
	 *
	 * @param content
	 * @return
	 * @throws ServiceRuntimeException
	 */
	public static int getGyjJcContentType(String content) throws ServiceRuntimeException {
		String[] ct = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		int contentType = 1;
		for (String a : ct) {
			String b = a.substring(a.indexOf(SymbolConstants.PARENTHESES_LEFT) + 1, a.indexOf(SymbolConstants.PARENTHESES_RIGHT));
			int c = b.split(SymbolConstants.COMMA).length;
			if (c > 1) {
				contentType = 2;
				break;
			}
		}
		return contentType;
	}


	public static void main(String[] args) {
		Lottery lottery = Lottery.getLottery(305);
		if(lottery.getName() == Lottery.FB.getName() || lottery.getName() == Lottery.BB.getName()){
			System.out.println("abc");
		}
	}
	
	/**
	 * 
	 * @Description: 得到订单方案的出票截止时间
	 * @param orderInfoBO 订单
	 * @param againstInfoBOs 对阵集合
	 * @return Date
	 * @author wuLong
	 * @date 2017年5月17日 上午10:19:13
	 */
	public static Date getSportsSendEndTicketTime(OrderInfoBO orderInfoBO,List<SportAgainstInfoBO> againstInfoBOs)throws ServiceRuntimeException {
		Date endTicketTime = null;
		LotteryPr lotteryPr =  LotteryEnum.getLottery(orderInfoBO.getLotteryCode());
		switch (lotteryPr) {
		case JJC:
		case BJDC:
			if(!ObjectUtil.isBlank(againstInfoBOs)){
				endTicketTime = againstInfoBOs.get(0).getStartTime();
			}
			break;
		default:
			endTicketTime = orderInfoBO.getEndTicketTime();
			break;
		}
		return endTicketTime;
	}
	/**
	 * 
	 * @Description: 得到所选的赛事最早开赛的时间
	 * @param buyScreen 所选的赛事  逗号分隔
	 * @param againstInfoBOs 对阵集合
	 * @return  Date
	 * @author wuLong
	 * @date 2017年5月17日 上午10:18:22
	 */
	public static SportAgainstInfoBO getSportsTicketFirstPlayTime(String buyScreen,Map<String,SportAgainstInfoBO> sportAgainstInfoMap)throws ServiceRuntimeException {
		List<String> bs = Arrays.asList(buyScreen.split(SymbolConstants.COMMA));
		SportAgainstInfoBO againstInfoBO = null;
		for(String s : bs){
			SportAgainstInfoBO againstInfoBO1 = sportAgainstInfoMap.get(s);
			if(againstInfoBO == null){
				againstInfoBO = againstInfoBO1;
			}else{
				if(againstInfoBO.getStartTime().after(againstInfoBO1.getStartTime())){
					againstInfoBO = againstInfoBO1;
				}
			}
		}
		return againstInfoBO;
	}
	
	public static boolean isRepeat(String content,Integer lotteryChildCode){
		boolean result = false;
    	String[] a = content.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.VERTICAL_BAR);
    	String[] b = a[0].split(SymbolConstants.COMMA);
    	String[] c = a[1].split(SymbolConstants.COMMA);
    	if(lotteryChildCode==LotteryChildEnum.LotteryChild.SD11X5_Q2_DIRECT.getValue()
    			||lotteryChildCode==LotteryChildEnum.LotteryChild.D11X5_Q2_DIRECT.getValue()
    			||lotteryChildCode==LotteryChildEnum.LotteryChild.JX11X5_Q2_DIRECT.getValue()
    			||lotteryChildCode==LotteryChildEnum.LotteryChild.XJ11X5_Q2_DIRECT.getValue()
    			||lotteryChildCode==LotteryChildEnum.LotteryChild.GX11X5_Q2_DIRECT.getValue()){
    		for (int i = 0,alen = b.length;i<alen;i++) {
                for (int j = 0,blen = c.length; j<blen;j++) {
                    if (b[i].equals(c[j])) {
                    	result = true;
                    	break;
                    }
                }                
            }  
    	}else{
    		String[] d = a[2].split(SymbolConstants.COMMA);
    		for (int i = 0,alen = b.length;i<alen;i++) {
    			for (int j = 0,blen = c.length; j<blen;j++) {
    				for (int k = 0,clen = d.length;k<clen;k++) {
    					if (b[i].equals(c[j]) || b[i].equals(d[k]) || d[k].equals(c[j])) {
    						result = true;
                        	break;
    					}
    				}
    			}                
    		}  
    	}
    	return result;
	}
	
	 /**
     * 
     * @Description: 获取11选5直选
     * @param content
     * @param lotteryChildCode
     * @return
     * @author wuLong
     * @date 2017年5月23日 上午11:28:09
     */
    public static List<String> getSyxwZx(String content,Integer lotteryChildCode)throws ServiceRuntimeException {
    	List<String> lists = new ArrayList<>();
    	String[] a = content.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.VERTICAL_BAR);
    	String[] b = a[0].split(SymbolConstants.COMMA);
    	String[] c = a[1].split(SymbolConstants.COMMA);
    	if(lotteryChildCode==LotteryChildEnum.LotteryChild.SD11X5_Q2_DIRECT.getValue()
    			||lotteryChildCode==LotteryChildEnum.LotteryChild.D11X5_Q2_DIRECT.getValue()
    			||lotteryChildCode==LotteryChildEnum.LotteryChild.XJ11X5_Q2_DIRECT.getValue()
    			||lotteryChildCode==LotteryChildEnum.LotteryChild.JX11X5_Q2_DIRECT.getValue()
    			||lotteryChildCode==LotteryChildEnum.LotteryChild.GX11X5_Q2_DIRECT.getValue()){
    		for (int i = 0,alen = b.length;i<alen;i++) {
                for (int j = 0,blen = c.length; j<blen;j++) {
                    if (!b[i].equals(c[j])) {
                       lists.add(b[i]+SymbolConstants.VERTICAL_BAR+c[j]);
                    }
                }                
            }  
    	}else{
    		String[] d = a[2].split(SymbolConstants.COMMA);
    		for (int i = 0,alen = b.length;i<alen;i++) {
    			for (int j = 0,blen = c.length; j<blen;j++) {
    				for (int k = 0,clen = d.length;k<clen;k++) {
    					if (!b[i].equals(c[j]) && !b[i].equals(d[k]) && !d[k].equals(c[j])) {
    						lists.add(b[i]+SymbolConstants.VERTICAL_BAR+c[j]+SymbolConstants.VERTICAL_BAR+d[k]);
    					}
    				}
    			}                
    		}  
    	}
    	return lists;
    }
    
    public static BigDecimal sumTicketAmount(List<TicketInfoBO> ticketInfoBOS,LotteryIssuePO lotteryIssuePO) throws ServiceRuntimeException{
    	BigDecimal a = BigDecimal.ZERO;
    	if(ObjectUtil.isBlank(lotteryIssuePO)){
    		throw new ServiceRuntimeException("彩期信息为空");
    	}
    	if(ObjectUtil.isBlank(lotteryIssuePO.getSaleTime())){
    		throw new ServiceRuntimeException("彩期"+lotteryIssuePO.getIssueCode()+"的本站开始送票时间为空");
    	}
    	try {
			Date saleTime = lotteryIssuePO.getSaleTime();
			Lottery lottery = Lottery.getLottery(lotteryIssuePO.getLotteryCode());
			if(lottery.getName() == Lottery.FB.getName() || lottery.getName() == Lottery.BB.getName()){
				saleTime = new Date();
			}
			for(TicketInfoBO infoBO : ticketInfoBOS){
				a = a.add(infoBO.getTicketMoney());
				infoBO.setSaleTime(Timestamp.valueOf(DateUtil.convertDateToStr(saleTime)));
			}
		} catch (Exception e) {
			a = BigDecimal.ZERO;
			logger.error(e.getMessage());
		}
    	return a;
    }
    
    public static boolean compareAmount(BigDecimal a,BigDecimal b){
    	if(a.compareTo(b) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 根据子玩法转换票详情
	 *
	 * @param content         原始内容
	 * @param lotteryChidCode 子玩法Code
	 * @return
	 * @throws ServiceRuntimeException
	 */
	public static Object[] convertTicketInfoContentLotteryChilCode(String content,Integer lotteryChidCode)throws ServiceRuntimeException {
		if(ObjectUtil.isBlank(content)||ObjectUtil.isBlank(lotteryChidCode)){
			return null;
		}
		Object[] result = new Object[2];
		//1705232301_S(0@5.80)|1705232302_R[-15.5](0@1.69)^2_1
		String[] b = content.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.VERTICAL_BAR);
		List<String> list = new ArrayList<String>();
		for(String c : b){
			if(c.contains(SymbolConstants.UNDERLINE)){
				String[] d = c.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UNDERLINE);
				String type = d[1].substring(0, 1);
				if(!list.contains(type)){
					list.add(type);
				}
			}
		}
		if(ObjectUtil.isBlank(list)){
			return null;
		}
		if(list.size()>1){
			result[0] = lotteryChidCode;
			result[1] = content;
		}else{
			String ch = list.get(0);
			if(SplitTicketUtil.checkBBLotteryId(lotteryChidCode)){
				result[0] = SplitTicketUtil.getBBBySimpleCode(ch);
			}else if(SplitTicketUtil.checkFBLotteryId(lotteryChidCode)){
				result[0] = SplitTicketUtil.getFBBySimpleCode(ch);
			}else if(SplitTicketUtil.checkBDLotteryId(lotteryChidCode)){
				result[0] = SplitTicketUtil.getBDBySimpleCode(ch);
			}
			result[1] = content.replace(SymbolConstants.UNDERLINE+ch, "");
		}
		return result;
	}
    
    
    /**
     * 
     * @Description: 得到大彩种的list集合和赛事systemCode set集合
     * @param orderInfoBOs 订单list
     * @param set  
     * @param lotteryCodes
     * @author wuLong
     * @date 2017年6月3日 下午2:44:27
     */
    public static void getLotteryCodeOrSystemCodes(List<OrderInfoBO> orderInfoBOs,Map<Integer,Set<String>> setMap,List<Integer> lotteryCodes){
    	if(!ObjectUtil.isBlank(orderInfoBOs)){
    		for(OrderInfoBO orderInfoBO : orderInfoBOs){
    			if(!lotteryCodes.contains(orderInfoBO.getLotteryCode())){
    				lotteryCodes.add(orderInfoBO.getLotteryCode());
    			}
    			LotteryEnum.LotteryPr lotteryPr =  LotteryEnum.getLottery(orderInfoBO.getLotteryCode());
    			if(lotteryPr == LotteryEnum.LotteryPr.BJDC || lotteryPr == LotteryEnum.LotteryPr.JJC || lotteryPr == LotteryEnum.LotteryPr.SFGG){
    				if(ObjectUtil.isBlank(orderInfoBO.getBuyScreen())){
    					throw new ServiceRuntimeException(orderInfoBO.getOrderCode()+"购买的场次编号为空");
    				}
    				Set<String> set = new HashSet<>();
    				if(setMap.containsKey(orderInfoBO.getLotteryCode())){
    					set = setMap.get(orderInfoBO.getLotteryCode());
    				}
    				String[] buyScreen = orderInfoBO.getBuyScreen().split(SymbolConstants.COMMA);
    				set.addAll(Arrays.asList(buyScreen));
    				setMap.put(orderInfoBO.getLotteryCode(), set);
    			}
    		}
    	}
    }

	/** 
	 * <li>1:支付完成,2:完成当前期追号订单</li>
	 * <li>格式:订单编号1,订单编号2#1</li>
	 * @Description: 
	 * @param orderCode
	 * @return
	 * @author wuLong
	 * @date 2017年6月3日 下午12:10:50
	 */
	public static String[] checkIn(String orderCode) {
		logger.info("接收到拆票消息队列消息订单编号:"+orderCode);
		if(orderCode.indexOf("\"")>-1){
			orderCode = orderCode.replaceAll("\"", "");
		}
		String[] orderCodes = null;
		String type = ",来源渠道：";
		if(orderCode.contains(SymbolConstants.NUMBER_SIGN)){
			String[] oc = orderCode.split(SymbolConstants.NUMBER_SIGN);
			orderCodes = oc[0].split(SymbolConstants.COMMA);
			String oca = oc[1];
			type += returnChannel(oca);
		}else{
			orderCodes = orderCode.split(SymbolConstants.COMMA);
		}
		logger.info("接收到拆票消息队列的订单数量："+(ObjectUtil.isBlank(orderCodes)?0:orderCodes.length)+type);
		return orderCodes;
	}
    
    private static String returnChannel(String oca){
    	String channel = null;
		switch (oca) {
		case "1":
			channel +="支付完成";
			break;
		case "2":
			channel +="完成当前期追号订单";
			break;
		default:
			channel +="未知来源";
			break;
		}
		return channel;
    }
    
    /**
	 * 添加报警信息
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年8月8日 下午12:18:05
	 * @param alarmType 报警类型
	 * @param alarmChild 报警子类型
	 * @param alarmLevel 报警等级
	 * @param alarmInfo 报警信息
	 * @param remark 备注
	 */
	public static void sendAlarmInfo(int alarmType,int alarmChild,int alarmLevel,String alarmInfo,String remark,SendTicketMessageProvider messageProvider){
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("alarmChild",alarmChild);
        jsonObject.put("alarmInfo",alarmInfo);
        jsonObject.put("alarmLevel",alarmLevel);
        jsonObject.put("alarmType",alarmType);
        jsonObject.put("remark",remark);
        jsonObject.put("alarmTime", DateUtil.getNow());
        messageProvider.sendMessage(ALARM_INFO, jsonObject.toString());
	}
	
}
