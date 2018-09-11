package com.hhly.lottosplit.utils;

import com.hhly.lottosplit.constants.SymbolConstants;

/**
 * 竞彩格式转换工具
 * @author zhanglei
 *
 */
public class FormatConversionJCUtil{
	
	
	
	
	/**
	 * 根据分割符,获得字串数组
	 * @param content 内容
	 * @param parStrs 分割符
	 * @param b 是否需要转义
	 * @return
	 */
	public static String[] stringSplitArray(String content,String parStrs,boolean b){
		if(ObjectUtil.isBlank(content))
			return new String[]{""};
		
		if(content.indexOf(parStrs) != -1){
			if(!b)//不需要转义
				return content.split(parStrs);
			else//需要转义
				return content.split(SymbolConstants.DOUBLE_SLASH+parStrs);
		}
		return new String[]{content};
	}
	
	
	/**
	 * 根据前后缀内容截取内容
	 * @param content 内容
	 * @param pre 前缀
	 * @param suf 后缀
	 * @param b 是否需要转义
	 * @return
	 */
	public static String stringSubstringToString(String content,String pre,String suf,boolean b){
		if(ObjectUtil.isBlank(content))
			return "";
		
		if(content.indexOf(pre) != -1 || content.indexOf(suf) != -1 ){
//			if(!b)//不需要转义
				return content.substring(content.indexOf(pre)+1, content.indexOf(suf));
//			else//需要转义
//				return content.substring(content.indexOf(SymbolConstants.DOUBLE_SLASH+pre)+1, content.indexOf(SymbolConstants.DOUBLE_SLASH+suf));
		}
		return content;
	}
	
	
	/**
	 * 解析多项投注内容(第一层) 
	 * @param betContent 投注内容
	 * @return
	 */
	public static String[] multitermBetContentAnalysis (String betContent){
		return stringSplitArray(betContent, SymbolConstants.SEMICOLON,false);
	}
	
	/**
	 * 解析单项投注内容(第二层)
	 * @param betContent
	 * @return
	 * [0]投注的详细内容.包括 系统编号,玩法代号,让分数,投注选项,SP值(多组)<br>
	 * [1]过关方式(多个)<br>
	 * [2]倍数(单个)<br>
	 */
	public static String[] singleBetContentAnalysis (String betContent){
		return stringSplitArray(betContent, SymbolConstants.UP_CAP,true);
	}
	
	/**
	 * 解析投注的详细内容(第三层)
	 * @param details
	 * @return
	 * 各对阵投注内容
	 */
	public static String[] betContentDetailsAnalysis (String details){
		return stringSplitArray(details, SymbolConstants.VERTICAL_BAR,true);
	}
	
	/**
	 * 解析单个比赛内容(第四层,胆)
	 * @param matchBet
	 * @return
	 * [0]-[最后-1]胆投注内容(多个) 
	 * [最后]非胆投注内容(单个)
	 */
	public static String[] singleMatchDanBetContentAnalysis (String matchBet){
		return stringSplitArray(matchBet, SymbolConstants.NUMBER_SIGN,false);
	}
	
	/**
	 * 解析单个比赛内容(第五层)
	 * @param matchBet
	 * @return
	 * [0]系统编号
	 * [...]各玩法的投注内容
	 */
	public static String[] singleMatchBetContentAnalysis (String matchBet){
		return stringSplitArray(matchBet, SymbolConstants.UNDERLINE,false);
	}
	
	
	
	/**
	 * 截取单个玩法投注内容
	 * @param matchBet
	 * @return(投注选项,SP值)多个
	 */
	public static String singleGameBetContentSubstring (String matchBet){
		return stringSubstringToString(matchBet, SymbolConstants.PARENTHESES_LEFT,  SymbolConstants.PARENTHESES_RIGHT, false);
	}
	
	/**
	 * 解析多个选项内容(第六层)
	 * @param matchBet
	 * @return
	 */
	public static String[] optionBetContentAnalysis (String matchBet){
		return stringSplitArray(matchBet, SymbolConstants.COMMA,false);
	}
	
	
	/**
	 * 解析单个选项内容(第七层)
	 * @param matchBet
	 * @return 单个(投注选项,SP值)
	 */
	public static String[] singleOptionBetContentAnalysis (String matchBet){
		return stringSplitArray(matchBet, SymbolConstants.AT,false);
	}
	
	
	
	
	/**
	 * 解析过关方式
	 * @param passWay
	 * @return
	 */
	public static String[] passWayBetContentAnalysis (String passWay){
		return stringSplitArray(passWay, SymbolConstants.COMMA,false);
	}
	
	
	
	
	
/*	161128001_R[+11.5](3@1.57,0@2.27)_S(3@1.89,0@4.21)_C(11@1.78,16@5.21)_D[165.5] (99@3.45,00@2.34)#161128002_S(3@1.89,0@4.21)|
	161128003_C(06@4.21)|
	161128004_R[-2.5](3@3.33)_D[180.5] (99@4.21)
	^2_1,3_1
	^868*/
	
	
	
	
	
}
