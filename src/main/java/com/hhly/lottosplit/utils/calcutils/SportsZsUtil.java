package com.hhly.lottosplit.utils.calcutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryEnum;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.utils.FormatConversionJCUtil;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;


/**
 * @ClassName: SportsZsUtil 
 * @Description: 计算投注注数的工具类 
 * @author wuLong
 * @date 2017年3月6日 下午4:24:53 
 *
 */
public class SportsZsUtil {
	/**
	 * 日志
	 */
//	protected static Log logger = LogFactory.getLog(SportsZsUtil.class);

	public static void main(String[] args) {
//		testJczqBet();
//		testJclqBet();
		try {
			//老足彩，6场半全场
//						String content = "3#0#0#1#1#0|0#3#0|0|0|0|1#3";
//						String after   = "3|0|0#1|1|0|0#3#0#0#0#0#1#3";
//						System.out.println(getSportsManyNote(content,LotteryEnum.Lottery.ZC_NINE.getName()));
//						System.out.println(getSportsManyNote(after,LotteryEnum.Lottery.ZC_NINE.getName()));
//						System.out.println("0#3#0#0#0#0#1#3#".split(SymbolConstants.NUMBER_SIGN).length);
//						System.out.println(content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR).length);
//						System.out.println(after.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR).length);
			//			//老足彩，4场进球
			//			String content = "0,1|1,2|2,3|3|1|1|1|3";
			//			getSportsManyNote(content,LotteryEnum.Lottery.JQ4.getName());
			//			//老足彩14场胜负过关
			//			String content = "3,1,0|3,1,0|3,0|1,0|1,0|1|1|3|3|3|0|1|0|3";
			//			getSportsManyNote(content,LotteryEnum.Lottery.SFC.getName());
			//			//任九
			String content = "1801092003_S(3@2.82,1@3.35)_R[+1](1@3.95)|1801092004_S(3@2.90,1@3.05)|1801092005_R[-2](3@2.00,1@3.85)^3_1,3_3,3_4";//竞彩篮球
			getSportsManyNote(content, 30001);
//						String content = "161128001[+1](3@1.57,0@2.27)#161128002[+1](1@1.89,0@4.21)|161128003[+1](0@4.21)^3_7";//北单让球胜平负
//						String content = "161128001[+1](0@1.57,1@2.27,2@3.50)#161128002[+1](2@1.89,3@4.21,4@5.65)|161128003[+1](4@4.21,5@6.50,7@7.6)^2_1";//北单总进球
//						getSportsManyNote(content, LotteryEnum.Lottery.BJDC.getName());
//			String content = "161128001_S(3@3.20,0@2.55)_R[+1](3@1.40,0@5.65)_Q(00@8.00)#161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)_Q(20@9.00)_Z(2@3.00)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)_Z(1@3.90)_B(10@4.00)^2_1";//竞彩篮球
//			getSportsFootballBets(content);
//			List<String> a = splitMatchByPassWay("1611281001_S[+1.5](3@1.57,0@2.27)_R[+1.5](3@1.57,0@2.27)|1611281004_S[+1.5](3@1.57,0@2.27)|1611281002_S[+1.5](3@1.89,0@4.21)|1611281003_S[+1.5](0@4.21)", "3_1,3_4",50);
//			System.out.println(JSONArray.fromObject(a));
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	/**
	 * @Description: 测试竞彩篮球注数
	 * @author wuLong
	 * @date 2017年3月6日 下午2:39:57
	 */
	private static void testJclqBet(){
		String betContent = "161128301_R[+11.5](3@1.57,0@2.27)_S(3@1.89,0@4.21)_C(01@1.78,11@5.21)_D[165.5](99@3.45,00@2.34)|"
				+ "161128302_S(3@1.89,0@4.21)#161128003_D[205.5](99@4.21)|161128304_R[-2](3@3.33)_C[18.5](12@4.21)^3_1,4_1,3_4";
		getSportsManyNote(betContent, SplitTicketUtil.ID_JCLQ_HHGG);
	}

	/**
	 * @Description:测试竞彩足球注数 
	 * @author wuLong
	 * @date 2017年3月6日 下午2:39:20
	 */
	private static void testJczqBet(){
		String betContent = "1611281001_S[+1.5](3@1.57,0@2.27)_R[+1.5](3@1.57,0@2.27)|1611281004_S[+1.5](3@1.57,0@2.27)|1611281002_S[+1.5](3@1.89,0@4.21)|1611281003_S[+1.5](0@4.21)^3_1,3_4";

		getSportsManyNote(betContent, SplitTicketUtil.ID_FHT);
	}
	/**
	 * 
	 * @Description: 获取竞彩类的注数（竞彩足球、竞彩篮球）
	 * @param content ecai投注内容
	 * @param lotteryId 彩种id
	 * @return int 注数
	 * @author wuLong
	 * @date 2017年3月8日 下午3:11:05
	 */
	public static int getSportsManyNote(String content,Integer lotteryId)throws ServiceRuntimeException{
		int bets = 0;
		if(SplitTicketUtil.checkBBLotteryId(lotteryId)||SplitTicketUtil.checkFBLotteryId(lotteryId)||lotteryId==LotteryEnum.Lottery.BB.getName()||lotteryId==LotteryEnum.Lottery.FB.getName()){
			bets = getSportsFootballAndBasketBallBets(content);
//			logger.debug("竞彩注数:"+bets);
        } else {
        	LotteryEnum.Lottery lottery = LotteryEnum.Lottery.getLottery(lotteryId);
            switch (lottery) {
			case ZC6:
				bets = getFootballJQ4AndZC6(content,6);
				break;
			case JQ4:
				//内容格式:0,1|1,2|2,3|3|1|1|1|3
				bets = getFootballJQ4AndZC6(content,4);
				break;
			case SFC:
				//内容格式：3,1|1,0|0|0|0|1|1|3|3|3|0|1|0|3
				bets = getFootballSFC(content);
				break;
			case ZC_NINE:
				//内容格式:3,1#1,0|3,0|_|_|3,1|3,1|3,1#3|3|3,0|1|3,0|3
				bets = getFootballZCNINE(content);
				break;
                case BJDC:
                case SFGG:
                    //格式：161128001[+1.5](3@1.57,0@2.27)#161128002[+1.5](3@1.89,0@4.21)|161128003[+1.5](0@4.21)^2_1,3_1
                    bets = getFootballBJDC(content);
                    break;
                default:
				throw new ServiceRuntimeException(lottery.getDesc()+"此彩种不予计算,目前只支持足彩、竞彩、北京单场");
			}
//			logger.debug(lottery.getDesc()+"注数:"+bets);
		}
		return bets;
	}
	/**
	 * 
	 * @Description: 竞彩注数计算
	 * @param content
	 * @return
	 * @author wuLong
	 * @date 2017年3月13日 下午4:54:03
	 */
	private static int getSportsFootballAndBasketBallBets(String content){
		int bets = 0;
		//161128001_S(3@3.20,0@2.55)_R[+1](3@1.40,0@5.65)_Q(00@8.00)#161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)_Q(20@9.00)_Z(2@3.00)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)_Z(1@3.90)_B(10@4.00)"^2_1
		//^已这个分割
		String[] contenta = FormatConversionJCUtil.singleBetContentAnalysis(content);
		//过关方式
		String passway = contenta[1].replace(SymbolConstants.UNDERLINE, SymbolConstants.TRAVERSE_SLASH);
		//赛事投注内容
		String contents = contenta[0];
		String[] dans = null;
		String[] tuos = null;
		//胆的集合
		List<String> danList = null;
		//胆的个数
		int danNum = 0;
		//判断是否有胆
		if(contents.contains(SymbolConstants.NUMBER_SIGN)){
			//以#分割
			String[] splitStr = contents.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.NUMBER_SIGN);
			//胆原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			dans = splitStr[0].split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			//拖原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			tuos = splitStr[1].split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			danList = new ArrayList<String>(dans.length);
			danNum = dans.length;
		}else{
			tuos = contents.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
		}
		//普通投注集合
		List<String> tuoList = new ArrayList<String>(tuos.length);
		getSportsFootballAnalysisEncap(danList, dans);//[S-2, D-1, R-1,C-1]
		getSportsFootballAnalysisEncap(tuoList, tuos);//[S-2, D-1, R-1,C-1]
		Combine combine = new Combine();
		//逗号分割
		String[] passways = passway.split(SymbolConstants.COMMA);
		for(String pws : passways){
			String[] pws_a = pws.split(SymbolConstants.DOUBLE_SLASH +SymbolConstants.TRAVERSE_SLASH);
			int passMatchs = Integer.valueOf(pws_a[0]);
			
			String[] array = new String[tuoList.size()];
			//过关串的命中场次总数-去胆的场次数 = 拖得组合数，从而得到得到拖的组合
			@SuppressWarnings("unchecked")
			List<String> tuoCfList = combine.mn(tuoList.toArray(array), passMatchs - danNum);
			List<String> fushiList = new ArrayList<String>(tuoCfList.size());  
			for(String s : tuoCfList){
				StringBuilder sb = new StringBuilder(s);
				if(!ObjectUtil.isBlank(danList)){
					for(String d : danList){
						sb.append(SymbolConstants.OBLIQUE_LINE).append(d);
					}
				}
				fushiList.add(sb.toString());
			}
			//是串一就直接拆
			if(pws_a[1].equals("1")){
				bets = sportsCalculation(bets, fushiList);
			}
			//多串过关需要，再拆多串
			else{
				int passwayArr[] = FuShiBetsContent.passWayMapArr.get(pws);
				//[2,2/2/2, 2,2/2/1, 2,2/2/1, 2/2/1]
				//拖的拆分 ：比如8场4串1有2个胆，这里就是6个拖里取2个的组合拆分了
				BaseLottery baseLottery = new BaseLottery();
				for(String cb : fushiList){
					String[] ab = cb.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
					String[][] e = new String[ab.length][];
					int i = 0;
					for(String c : ab){
						String[] d = c.split(SymbolConstants.COMMA);
						e[i] = d;
						i++;
					}
					@SuppressWarnings("static-access")
					String ds[] = baseLottery.getCombineArrToStrabc(e).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
					for(String n : ds){
						for (int pway : passwayArr) {
							//过关串的命中场次总数-去胆的场次数 = 拖得组合数，从而得到得到拖的组合
							@SuppressWarnings("unchecked")
							List<String> listCombine = combine.mn(n.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR), pway);
							bets = sportsCalculation(bets, listCombine);
						}
					}
				}
			}
		}
		return bets;
	}
	/**
	 * 
	 * @Description: 拆分组合 
	 * @param content 投注内容
	 * @param passway 过关方式
	 * @param multiple 倍数
	 * @return List String
	 * @author wuLong
	 * @date 2017年4月7日 上午9:55:54
	 */
	@SuppressWarnings("static-access")
	public static List<String> splitMatchByPassWay(String content,String passway,Integer multiple){
		List<String> list = new ArrayList<>();
		//赛事投注内容
		String[] dans = null;
		String[] tuos = null;
		//胆的集合
		String[][] danLists = null;
		String[][] tuoLists = null;
		String tuo_s = null;
		int danNum = 0;
		//判断是否有胆
		if(content.contains(SymbolConstants.NUMBER_SIGN)){
			//以#分割
			String[] splitStr = content.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.NUMBER_SIGN);
			//胆原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			String dan_s = splitStr[0];
			dans = dan_s.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			//拖原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			tuo_s = splitStr[1];
			tuos = tuo_s.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			danNum = dans.length;
			danLists = new String[danNum][];
			getMixSplit(dans, danLists, dan_s);
			tuoLists = new String[tuos.length][];
			getMixSplit(tuos, tuoLists, tuo_s);
		}else{
			tuos = content.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			tuoLists = new String[tuos.length][];
			getMixSplit(tuos, tuoLists, content);
		}
		//普通投注集合
		Combine combine = new Combine();
		//逗号分割
		String[] passWays = passway.split(SymbolConstants.COMMA);
		BaseLottery baseLottery = new BaseLottery();
		for(String pws : passWays){
			String[] pws_a = pws.split(SymbolConstants.DOUBLE_SLASH +SymbolConstants.UNDERLINE);
			int passMatchs = Integer.valueOf(pws_a[0]);
			
			//过关串的命中场次总数-去胆的场次数 = 拖得组合数，从而得到得到拖的组合
			if(danLists==null){
				getSingleTicket(multiple, list, tuos, danNum, combine, baseLottery, pws, passMatchs, "");
			}else{
				String danString[] = baseLottery.getCombineArrToStrabc(danLists).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
				for(String d : danString){
					getSingleTicket(multiple, list, tuos, danNum, combine, baseLottery, pws, passMatchs, d + SymbolConstants.VERTICAL_BAR);
				}
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private static void getSingleTicket(Integer multiple, List<String> list, String[] tuos, int danNum, Combine combine, BaseLottery baseLottery, String pws, int passMatchs, String d) {
		List<String> tuoCfList = combine.mn(tuos, passMatchs - danNum);
		for(String s : tuoCfList){
            String[] y = s.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
            String[][] r = new String[y.length][];
            getMixSplit(y, r, s);
            String ds[] = BaseLottery.getCombineArrToStrabc(r).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
            for(String h : ds){
                list.add(d + h + SymbolConstants.UP_CAP + pws + SymbolConstants.UP_CAP + multiple);
            }
        }
	}


	private static void getMixSplit(String[] a, String[][] b, String c) {
		if(c.contains(SymbolConstants.UNDERLINE)){
			int ai = 0;
			for(String d : a){
				String e[] = d.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UNDERLINE);
				String dan[] = new String[e.length-1];
				if(e.length>2){
					String matchNo = null;
					for(int i = 0,len =e.length;i<len;i++){
						String g = e[i];
						if(i==0){
							matchNo = g;
						}else{
							dan[i-1] = matchNo+"_"+g;
						}
					}
				}else{
					dan[0] = d;
				}
				b[ai] = dan;
				ai++;
			}
		}else{
			int ai = 0;
			for(String d : a){
				String dan[] = new String[]{d};
				b[ai] = dan;
				ai++;
			}
		}
	}
	/**
	 * 
	 * @Description: 计算每一个大的组合的注数
	 * @param bets 
	 * @param listCombine
	 * @return
	 * @author wuLong
	 * @date 2017年3月15日 下午1:42:44
	 */
	private static int sportsCalculation(int bets, List<String> listCombine) {
		for(String c : listCombine){
			//c : S-2/R-2,S-2,C-2,D-2
			String[] d = c.split(SymbolConstants.OBLIQUE_LINE);
			String[][] sbs = new String[d.length][];
			int i =0;
			for(String e : d){
				sbs[i] = e.split(SymbolConstants.COMMA);
				i++;
			}
			String sbStr = BaseLottery.getCombineArrToStr(sbs);
			String[] sbStra = sbStr.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.VERTICAL_BAR);
			for(String sbStrb : sbStra){
				String[] sbStrc = sbStrb.split(SymbolConstants.COMMA);
				int num = 1;
				for(String sbStrd : sbStrc){
					num = num*Integer.valueOf(sbStrd);
				}
				bets += num;
			}
		}
		return bets;
	}
	
	/**
	 * 
	 * @Description: 竞彩足球解析并封装成数组集合，后续组合计算
	 * @param danList
	 * @param dans
	 * @author wuLong
	 * @date 2017年3月13日 下午3:57:35
	 */
	private static void getSportsFootballAnalysisEncap(List<String> danList, String[] dans) {
		if(ObjectUtil.isBlank(dans)){
			return;
		}
		for(String d : dans){
			//161128001_S(3@3.20,0@2.55)_R[+1](3@1.40,0@5.65)_Q(00@8.00)
			String[] e = null;
			int index = 0;
			if(d.contains(SymbolConstants.UNDERLINE)){
				index = 1;
				e = d.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UNDERLINE);
			}else{
				index = 0;
				e = new String[1];
				e[0] = d;
			}
			StringBuilder sb = new StringBuilder();
			for(int i=index,len=e.length;i<len;i++){
				String f = e[i];
				String dsa = f.substring(f.indexOf(SymbolConstants.PARENTHESES_LEFT)+1, f.indexOf(SymbolConstants.PARENTHESES_RIGHT));
				String[] dsb = dsa.split(SymbolConstants.COMMA);
				String v = dsb.length+"";
				sb.append(v).append(SymbolConstants.COMMA);
			}
			if(!ObjectUtil.isBlank(sb.toString())){
				sb.deleteCharAt(sb.length()-1);
				danList.add(sb.toString());
			}
		}
	}
	/**
	 * 
	 * @Description: 北京单场 
	 * @param content
	 * @return
	 * @author wuLong
	 * @date 2017年3月10日 上午9:00:28
	 */
	private static int getFootballBJDC(String content){
		int bets = 0;
		//161128001[+1](3@1.57,0@2.27)#161128002[+1](1@1.89,0@4.21)|161128003[+1](0@4.21)^2_1,3_1
		List<String> danList = new ArrayList<String>();
		//普通投注集合
		List<String> tuoList = new ArrayList<String>();
		//^已这个分割
		String[] contenta = FormatConversionJCUtil.singleBetContentAnalysis(content);
		//过关方式
		String passway = contenta[1].replace(SymbolConstants.UNDERLINE, SymbolConstants.TRAVERSE_SLASH);
		//赛事投注内容
		String contents = contenta[0];
		//以#分割
		//胆原始内容
		String[] dans = null;
		int danNum = 0;
		//拖原始内容
		String[] tuos = null;
		//判断是否有胆
		if (!contents.contains(SymbolConstants.NUMBER_SIGN)) {
			//拖原始内容
			tuos = contents.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
		} else {
			//以#分割
			String[] splitStr = contents.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.NUMBER_SIGN);
			//胆原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			dans = splitStr[0].split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			//拖原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			tuos = splitStr[1].split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			danList = new ArrayList<>(dans.length);
			danNum = dans.length;
		}
		//把胆码放入danList集合
		getBdAnalysisEncapsulation(danList, dans);
		//把拖放入tuoList集合
		getBdAnalysisEncapsulation(tuoList, tuos);
		//逗号分割
		String[] passways = passway.split(SymbolConstants.COMMA);
		//根据过关串，循环计算出每个串关的注数
		Combine combine = new Combine();
		for(String pws : passways){
			String[] pws_a = pws.split(SymbolConstants.DOUBLE_SLASH +SymbolConstants.TRAVERSE_SLASH);
			int passMatchs = Integer.valueOf(pws_a[0]);
			String[] array = new String[tuoList.size()];
			//过关串的命中场次总数-去胆的场次数 = 拖得组合数，从而得到得到拖的组合
			@SuppressWarnings("unchecked")
			List<String> listCombine = combine.mn(tuoList.toArray(array), passMatchs - danNum);
			List<String> fushiList = new ArrayList<String>(listCombine.size());  
			for(String s : listCombine){
				StringBuilder sb = new StringBuilder(s);
				for(String d : danList){
					sb.append(SymbolConstants.OBLIQUE_LINE).append(d);
				}
				fushiList.add(sb.toString());
			}
			//是串一就直接拆
			if(pws_a[1].equals("1")){
				bets = sportsCalculation(bets, fushiList);
			}
			//多串过关需要，再拆多串
			else{
				int passwayArr[] = FuShiBetsContent.bdPassWayMapArr.get(pws);
				//拖的拆分 ：比如8场4串1有2个胆，这里就是6个拖里取2个的组合拆分了
				for(String cb : fushiList){
					for (int pway : passwayArr) {
						//过关串的命中场次总数-去胆的场次数 = 拖得组合数，从而得到得到拖的组合
						@SuppressWarnings("unchecked")
						List<String> list = combine.mn(cb.split(SymbolConstants.OBLIQUE_LINE), pway);
						bets = sportsCalculation(bets, list);
					}
				}
			}
		}
		return bets;
	}
	/**
	 * 
	 * @Description: 北单的投注内容解析并封装到胆拖list集合中，用于后续的组合计算 
	 * @param list
	 * @param dans
	 * @author wuLong
	 * @date 2017年3月10日 下午2:30:07
	 */
	private static void getBdAnalysisEncapsulation(List<String> list, String[] dans) {
		if(ObjectUtil.isBlank(dans)){
			return;
		}
		for(String ds : dans){
			String dsa = ds.substring(ds.indexOf(SymbolConstants.PARENTHESES_LEFT)+1, ds.indexOf(SymbolConstants.PARENTHESES_RIGHT));
			String[] dsb = dsa.split(SymbolConstants.COMMA);
			list.add(dsb.length+"");
		}
	}

	/**
	 * 
	 * @Description: 计算足彩:九场胜负彩投注注数
	 * @param content
	 * @return
	 * @author wuLong
	 * @date 2017年3月10日 上午8:59:37
	 */
	private static int getFootballZCNINE(String content){
		boolean isLastSetGuts = false;
		if(content.indexOf(SymbolConstants.NUMBER_SIGN) != content.length() - 1){
			isLastSetGuts = true;
		}
		//胆码投注集合
		List<String> danList = new ArrayList<String>();
		//普通投注集合
		List<String> list    = new ArrayList<String>();
		String[] splitStr = content.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
		int count = 0;//计数器
		for(String splitStra : splitStr){
			if(splitStra.equals(SymbolConstants.UNDERLINE)){
				continue;
			}
			if(splitStra.contains(SymbolConstants.NUMBER_SIGN)){//3|0|0|1#1|0|0#3#0#0#0#0|1#3#
				String[] danTuo = splitStra.split(SymbolConstants.NUMBER_SIGN,-1);
				if(danTuo.length > 1){
					int j = (splitStra.lastIndexOf(SymbolConstants.NUMBER_SIGN) == splitStra.length() - 1) ? 0 : 1;
					for(int i = 0; i < danTuo.length-j; i++){
						if(!ObjectUtil.isBlank(danTuo[i])){
							danList.add(danTuo[i]);
						}
					}
					if(!ObjectUtil.isBlank(danTuo[danTuo.length-1]) && !danTuo[danTuo.length-1].equals(SymbolConstants.UNDERLINE)){
						list.add(danTuo[danTuo.length-1]);
					}
				}else{
					danList.add(danTuo[0]);
					if(!(isLastSetGuts && count == splitStr.length-1)){//最后一场设了胆，并且当前非最后一个元素
						if(!danTuo[1].equals(SymbolConstants.UNDERLINE)){//胆码后面可能为未选赛事
							list.add(danTuo[1]);
						}
					}
				}
			}else{
				list.add(splitStra);
			}
			count++;
		}
		//胆码组合注数
		Integer danCount = 1;
		for(String danl : danList){
			danCount *= danl.split(SymbolConstants.COMMA).length;
		}
		Combine combine = new Combine();
		Integer total = 0;
		String[] array = new String[list.size()];
		//普通投注中，减去胆码数后，无胆码组合
		@SuppressWarnings("unchecked")
		List<String> listCombine = combine.mn(list.toArray(array), 9 - danList.size());
		for(String lcb : listCombine){
			Integer eachCombine = danCount;
			//因为每个无胆码组合，必须与胆码组合注数相乘，故初始化为胆码注数
			for(String str : lcb.split(SymbolConstants.OBLIQUE_LINE)){
				eachCombine *=  str.split(SymbolConstants.COMMA).length;
			}
			total += eachCombine;
		}
		return total;
	}
	/**
	 * 
	 * @Description: 计算足彩:十四场胜负彩投注注数
	 * @param content 投注内容
	 * @return int 注数
	 * @author wuLong
	 * @date 2017年3月10日 上午8:56:12
	 */
	private static int getFootballSFC(String content){
		Integer total = 1;
		String[] splitStr = content.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
		for(String splitr:splitStr){
			if(!splitr.equals(SymbolConstants.UNDERLINE) && !splitr.equals(SymbolConstants.STAR)){
				total *= splitr.split(SymbolConstants.COMMA).length;
			}
		}
		return total;
	}
	/**
	 * @Description: 计算足彩:四场进球彩和六场半全场投注注数
	 * @param content
	 * @param num
	 * @return int
	 * @author wuLong
	 * @date 2017年3月10日 上午10:48:28
	 */
	private static int getFootballJQ4AndZC6(String content,int num){
		//ZC6内容格式:3,1,0|1|3|1|3|1|3|1|3|1|3|1
		//JQ4内容格式:0,1|1,2|2,3|3|1|1|1|3
		String a[] = content.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.VERTICAL_BAR);
		String[][] opt = new String[num][];
		String[] opts = new String[2];
		int j = 0;
		for(int i=0,len=a.length;i<len;i++){
			if(i%2==0){
				opts[0] = a[i];
			}else{
				opts[1] = a[i];
				opt[j] = opts;
				opts = new String[2];
				j++;
			}
		}
		//[[3,1,0, 1], [3, 1], [3, 1], [3, 1], [3, 1], [3, 1]]
		//1.先把每一场的 半场和全场组成多个2串1组合
		//2.再把把每一场的 2串1组合，再组合成多个6串1组合
		BaseLottery baseLottery = new BaseLottery();
		String[][] opta = new String[num][];
		for(int i=0,len=opt.length;i<len;i++){
			//arr形如:["3","1","0"],["3","1"]数组列表 拆分后为3,3|3,1|1,3|1,1|0,3|0,1 待取得组合拆分的数组(先后顺序)
			@SuppressWarnings("static-access")
			String optb = baseLottery.getCombineArrToStr(opt[i]);
			//待取得组合拆分的数组(先后顺序) 参数 str 形如:3,1,0|3,1的字符串 拆分后为3,3|3,1|1,3|1,1|0,3|0,1
			String optc = baseLottery.getCombineArrToStr(optb);
			opta[i] = optc.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.VERTICAL_BAR);
		}
		//参数 arr形如:["3","1","0"],["3","1"]数组列表 拆分后为3,3|3,1|1,3|1,1|0,3|0,1 待取得组合拆分的数组(先后顺序)
		@SuppressWarnings("static-access")
		String optc = baseLottery.getCombineArrToStr(opta);
		int bets = optc.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.VERTICAL_BAR).length;
		return bets;
	}
	
	public static List<String> splitContentToSingle(String content,String passway,Integer multiple,Integer lotteryId){
		List<String> result = new ArrayList<>();
		//161128001_S(3@3.20,0@2.55)_R[+1](3@1.40,0@5.65)_Q(00@8.00)#161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)_Q(20@9.00)_Z(2@3.00)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)_Z(1@3.90)_B(10@4.00)"^2_1
		//^已这个分割
		String[] contenta = FormatConversionJCUtil.singleBetContentAnalysis(content);
		//赛事投注内容
		String contents = contenta[SplitTicketUtil.NUM_ZERO];
		String[] dans = null;
		String[] tuos = null;
		//胆的集合
		List<String> danList = null;
		List<String> tuoList = null;
		//胆的个数
		int danNum = SplitTicketUtil.NUM_ZERO;
		//判断是否有胆
		if(contents.contains(SymbolConstants.NUMBER_SIGN)){
			//以#分割
			String[] splitStr = contents.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.NUMBER_SIGN);
			//胆原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			dans = splitStr[SplitTicketUtil.NUM_ZERO].split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			danList = Arrays.asList(dans);
			//拖原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			tuos = splitStr[SplitTicketUtil.NUM_ONE].split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			tuoList = Arrays.asList(tuos);
			danNum = dans.length;
		}else{
			tuos = contents.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			tuoList = Arrays.asList(tuos);
		}
		//普通投注集合
		Combine combine = new Combine();
		//逗号分割3_1,4_1
		String[] passways = passway.split(SymbolConstants.COMMA);
		BaseLottery baseLottery = new BaseLottery();
		for(String pws : passways){
			String[] pws_a = pws.split(SymbolConstants.DOUBLE_SLASH +SymbolConstants.UNDERLINE);
			//过关场次数
			int passMatchs = Integer.valueOf(pws_a[SplitTicketUtil.NUM_ZERO]);
			
			//过关串的命中场次总数-去胆的场次数 = 拖得组合数，从而得到得到拖的组合 
			@SuppressWarnings("unchecked")
			List<String> tuoCfList = combine.mn(tuoList.toArray( new String[tuoList.size()]), passMatchs - danNum);
			List<String> fushiList = new ArrayList<String>(tuoCfList.size());  
			if(ObjectUtil.isBlank(danList)){
				fushiList.addAll(tuoCfList);
				tuoCfList = null;
			}else{
				for(String s : tuoCfList){
					StringBuilder sb = new StringBuilder(s);
					if(!ObjectUtil.isBlank(danList)){
						for(String d : danList){
							sb.append(SymbolConstants.OBLIQUE_LINE).append(d);
						}
					}
					fushiList.add(sb.toString());
				}
				tuoCfList = null;
			}
			//是串一就直接拆
			if(SplitTicketUtil.STRING_ONE.equals(pws_a[SplitTicketUtil.NUM_ONE])){
				for(String c : fushiList){
					String[] w = c.split(SymbolConstants.OBLIQUE_LINE);
					String[][] s = new String[w.length][];
					handlePassWayOneReturnDateT(w, s, SplitTicketUtil.NUM_ZERO, w.length,lotteryId);
					@SuppressWarnings("static-access")
					String ds[] = baseLottery.getCombineArrToStrabc(s).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
					String ac = SymbolConstants.UP_CAP+pws+SymbolConstants.UP_CAP+multiple;
					String su = StringUtils.join(ds,ac+SymbolConstants.NUMBER_SIGN)+ac;
					//161128001_S(3@3.20,0@2.55)/16112802_S(3@3.20,0@2.55)
					result.addAll(Arrays.asList(su.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN)));
					ds = null;
					su = null;
				}
			}
			//多串过关需要，再拆多串
			else{
				int passwayArr[] = FuShiBetsContent.passWayMapArr.get(pws);
				for(String cb : fushiList){
					List<String> lists = splitContentToSingleData(Arrays.asList(cb), baseLottery);
					for(String n : lists){
						for (int pway : passwayArr) {
							//过关串的命中场次总数-去胆的场次数 = 拖得组合数，从而得到得到拖的组合
							@SuppressWarnings("unchecked")
							List<String> listCombine = combine.mn(n.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR), pway);
							String t = StringUtils.join(listCombine,SymbolConstants.NUMBER_SIGN).replaceAll(SymbolConstants.OBLIQUE_LINE, SymbolConstants.VERTICAL_BAR);
							List<String> strings = Arrays.asList(t.split(SymbolConstants.NUMBER_SIGN));
							for(String c : strings){
								String[] w = c.split(SymbolConstants.OBLIQUE_LINE);
								String[][] s = new String[w.length][];
								handlePassWayOneReturnDateT(w, s, SplitTicketUtil.NUM_ZERO, w.length,lotteryId);
								@SuppressWarnings("static-access")
								String ds[] = baseLottery.getCombineArrToStrabc(s).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
								String ac = SymbolConstants.UP_CAP+pway+"_1"+SymbolConstants.UP_CAP+multiple;
								String su = StringUtils.join(ds,ac+SymbolConstants.NUMBER_SIGN)+ac;
								//161128001_S(3@3.20,0@2.55)/16112802_S(3@3.20,0@2.55)
								result.addAll(Arrays.asList(su.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN)));
								ds = null;
								su = null;
							}
							listCombine = null;
						}
					}
					lists = null;
				}
			}
		}
		dans = null;
		tuos = null;
		danList = null;
		tuoList = null;
		return result;
	}
	
	public static List<String> splitContentToSingleData(List<String> fushiList,BaseLottery baseLottery){
		//[161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)_Q(20@9.00)_Z(2@3.00)/161128001_S(3@3.20,0@2.55)_R[+1](3@1.40,0@5.65)_Q(00@8.00), 
		//161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)_Z(1@3.90)_B(10@4.00)/161128001_S(3@3.20,0@2.55)_R[+1](3@1.40,0@5.65)_Q(00@8.00)]
		if(ObjectUtil.isBlank(fushiList)){
			return null;
		}
		List<String> result = new ArrayList<>();
		for(String a : fushiList){
			String[] b = a.split(SymbolConstants.OBLIQUE_LINE);
			String[][] c = new String[b.length][];
			int i = 0;
			for(String d : b){
				String matchNo = null;
				if(d.contains(SymbolConstants.UNDERLINE)){
					matchNo = d.substring(SplitTicketUtil.NUM_ZERO, d.indexOf(SymbolConstants.UNDERLINE)+1);
				}else if(d.contains(SymbolConstants.MIDDLE_PARENTHESES_LEFT)){
					matchNo = d.substring(SplitTicketUtil.NUM_ZERO, d.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT));
				}else{
					matchNo = d.substring(SplitTicketUtil.NUM_ZERO, d.indexOf(SymbolConstants.PARENTHESES_LEFT));
				}
				int index = SplitTicketUtil.NUM_ZERO;
				String[] e = null;
				if(d.contains(SymbolConstants.UNDERLINE)){
					index = SplitTicketUtil.NUM_ONE;
					e = d.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UNDERLINE);
				}else{
					index = SplitTicketUtil.NUM_ZERO;
					e = new String[1];
					e[SplitTicketUtil.NUM_ZERO] = d;
				}
				String[] as = new String[e.length-index];
				int j = SplitTicketUtil.NUM_ZERO;
				for(int l=index,len=e.length;l<len;l++){
					as[j] = matchNo + e[l];
					j++;
				}
				c[i] = as;
				i++;
			}
			@SuppressWarnings("static-access")
			String ds[] = baseLottery.getCombineArrToStrabc(c).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
			result.addAll(Arrays.asList(ds));
		}
		return result;
	}
	
	private static void handlePassWayOneReturnDateT(String[] w,String[][] s,int i,int len,Integer lotteryId){
		for(String e : w){
			int subIndex = 0;
			String matchNo = null;
			if(SplitTicketUtil.checkBBLotteryId(lotteryId)||SplitTicketUtil.checkFBLotteryId(lotteryId)||lotteryId==LotteryEnum.Lottery.BB.getName()||lotteryId==LotteryEnum.Lottery.FB.getName()){
				matchNo = e.substring(SplitTicketUtil.NUM_ZERO, SplitTicketUtil.NUM_TEN);
				subIndex = SplitTicketUtil.NUM_TEN;
			}else{
				if(e.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT)!=-1){
					matchNo = e.substring(SplitTicketUtil.NUM_ZERO, e.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT));
					subIndex = e.indexOf(SymbolConstants.MIDDLE_PARENTHESES_LEFT);
				}else{
					matchNo = e.substring(SplitTicketUtil.NUM_ZERO, e.indexOf(SymbolConstants.PARENTHESES_LEFT));
					subIndex = e.indexOf(SymbolConstants.PARENTHESES_LEFT);
				}
			}
			String[] content = e.substring(subIndex).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UNDERLINE);
			String dan[] = null;
			StringBuffer sb = new StringBuffer();
			if(e.contains(SymbolConstants.UNDERLINE)){
				matchNo += SymbolConstants.UNDERLINE;
			}
			//content : [,S(3@2.25,0@2.90),R(3@2.25,0@2.90)]
			for(String m : content){
				if(ObjectUtil.isBlank(m)){
					continue;
				}
				String singleContent = m.substring(m.indexOf(SymbolConstants.PARENTHESES_LEFT)+SplitTicketUtil.NUM_ONE, m.indexOf(SymbolConstants.PARENTHESES_RIGHT));
				String bp = m.substring(SplitTicketUtil.NUM_ZERO,m.indexOf(SymbolConstants.PARENTHESES_LEFT));
				String[] sc = singleContent.split(SymbolConstants.COMMA);
				for(String p : sc){
					sb.append(matchNo).append(bp).append(SymbolConstants.PARENTHESES_LEFT).append(p).append(SymbolConstants.PARENTHESES_RIGHT).append(SymbolConstants.NUMBER_SIGN);
				}
			}
			sb.deleteCharAt(sb.length()-SplitTicketUtil.NUM_ONE);
			dan = sb.toString().split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.NUMBER_SIGN);
			s[i] = dan;
			i++;
			sb = null;
		}
	}
	
	/**
	 * 
	 * @Description: 拆分组合 
	 * @param content 投注内容
	 * @param passway 过关方式
	 * @param multiple 倍数
	 * @return List String
	 * @author wuLong
	 * @date 2017年4月7日 上午9:55:54
	 */
	@SuppressWarnings("static-access")
	public static List<String> splitMatchByPassWay(String content,String passway,Integer multiple,LotteryTypeBO lotteryTypeBO,Integer maxNum){
		List<String> list = new ArrayList<>();
		//赛事投注内容
		String[] dans = null;
		String[] tuos = null;
		//胆的集合
		String[][] danLists = null;
		String[][] tuoLists = null;
		String tuo_s = null;
		int danNum = SplitTicketUtil.NUM_ZERO;
		//判断是否有胆
		if(content.contains(SymbolConstants.NUMBER_SIGN)){
			//以#分割
			String[] splitStr = content.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.NUMBER_SIGN);
			//胆原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			String dan_s = splitStr[SplitTicketUtil.NUM_ZERO];
			dans = dan_s.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			//拖原始内容161128002_S(3@2.25,0@2.90)_R[+1](3@5.05,0@1.49)|161128003_S(3@5.25,0@1.55)_R[+1](3@2.08,0@2.90)
			tuo_s = splitStr[SplitTicketUtil.NUM_ONE];
			tuos = tuo_s.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			danNum = dans.length;
			danLists = new String[danNum][];
			getMixSplit(dans, danLists, dan_s);
			tuoLists = new String[tuos.length][];
			getMixSplit(tuos, tuoLists, tuo_s);
		}else{
			tuos = content.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.VERTICAL_BAR);
			tuoLists = new String[tuos.length][];
			getMixSplit(tuos, tuoLists, content);
		}
		//普通投注集合
		Combine combine = new Combine();
		//逗号分割
		String[] passWays = passway.split(SymbolConstants.COMMA);
		BaseLottery baseLottery = new BaseLottery();
		boolean isSplit = getIsSplit(content, passWays);
		if(isSplit){
			for(String pws : passWays){
				String[] pws_a = pws.split(SymbolConstants.DOUBLE_SLASH +SymbolConstants.UNDERLINE);
				int passMatchs = Integer.valueOf(pws_a[SplitTicketUtil.NUM_ZERO]);
				if(pws_a[1].equals("1")){
					//过关串的命中场次总数-去胆的场次数 = 拖得组合数，从而得到得到拖的组合
					if(danLists==null){
						getSingleTicket(multiple, list, tuos, danNum, combine, baseLottery, pws, passMatchs, "");
					}else{
						String danString[] = baseLottery.getCombineArrToStrabc(danLists).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
						for(String d : danString){
							getSingleTicket(multiple, list, tuos, danNum, combine, baseLottery, pws, passMatchs, d + SymbolConstants.VERTICAL_BAR);
						}
					}
				}else{
					int passwayArr[] = null;
					if(lotteryTypeBO.getLotteryCode()==300||lotteryTypeBO.getLotteryCode()==301){
						passwayArr = FuShiBetsContent.passWayMapArr.get(pws.replace("_", "-"));
					}else if(lotteryTypeBO.getLotteryCode()==306||lotteryTypeBO.getLotteryCode()==307){
						passwayArr = FuShiBetsContent.bdPassWayMapArr.get(pws.replace("_", "-"));
					}
					List<String> tuoCfList = combine.mn(tuos, passMatchs - danNum);
					List<String> fushiList = new ArrayList<String>(tuoCfList.size());  
					if(danNum>0){
						String danString[] = baseLottery.getCombineArrToStrabc(danLists).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
						for(String tcfl : tuoCfList){
							for(String dansg : danString){
								fushiList.add(tcfl + SymbolConstants.OBLIQUE_LINE + dansg);
							}
						}
					}else{
						fushiList.addAll(tuoCfList);
					}
					for(String s : fushiList){
			            String[] y = s.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
	            		String[][] r = new String[y.length][];
	            		getMixSplit(y, r, s);
	            		String ds[] = BaseLottery.getCombineArrToStrabc(r).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
	            		for(String dsa : ds){
	            			String dsc[] = dsa.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
	            			for(int pway : passwayArr){
	            				List<String> listCombine = combine.mn(dsc, pway);
	            				for(String xs : listCombine){
	            					String[] yy = xs.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
	            					String[][] rr = new String[yy.length][];
	            					getMixSplit(yy, rr, xs);
	            					String dsb[] = BaseLottery.getCombineArrToStrabc(rr).split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.OBLIQUE_LINE);
	            					for(String hh : dsb){
	            						list.add("" + hh + SymbolConstants.UP_CAP + pway+"_1" + SymbolConstants.UP_CAP + multiple);
	            					}
	            				}
	            			}
	            		}
			        }
				}
			}
		}else{
			list = SplitTicketUtil.getNumberHighColorByMultipleSplit(multiple, content+SymbolConstants.UP_CAP+passway, maxNum);
		}
		danLists = null;
		tuoLists = null;
		dans = null; 
		tuos = null;
		return list;
	}
	
	/**
	 * <li>需要拆票的条件<li>
	 * <li>1.多个过关方式</li>
	 * <li>2.场次数大于过关数</li>
	 * <li>3.单场比赛玩法超过1个</li>
	 * @Description: 根据投注内容和过关方式判断是否要拆票
	 * @param content 投注内容
	 * @param passways 过关方式
	 * @return boolean true ： 要拆票，false：不需要拆票
	 * @author wuLong
	 * @date 2017年6月6日 下午7:00:42
	 */
	private static boolean getIsSplit(String content,String[] passways){
		//1.多个过关方式
		if(passways.length > SplitTicketUtil.NUM_ONE){
			return true;
		}
		String passway[] = passways[SplitTicketUtil.NUM_ZERO].split(SymbolConstants.DOUBLE_SLASH +SymbolConstants.UNDERLINE);
		int passMatchs = Integer.valueOf(passway[SplitTicketUtil.NUM_ZERO]);
		content = content.replaceAll(SymbolConstants.NUMBER_SIGN, SymbolConstants.VERTICAL_BAR);
		String contents[] = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
		int matchNum = contents.length;
		if(matchNum > passMatchs){
			return true;
		}
		boolean rs = false;
		for(String c : contents){
			if(c.contains(SymbolConstants.UNDERLINE)){
				int cl = c.split(SymbolConstants.DOUBLE_SLASH+SymbolConstants.UNDERLINE).length;
				if(cl>2){
					rs = true;
					break;
				}
			}
		}
		return rs;
	} 
}
