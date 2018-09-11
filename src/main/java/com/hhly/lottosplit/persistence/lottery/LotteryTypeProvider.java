package com.hhly.lottosplit.persistence.lottery;

import java.util.List;
import java.util.Map;

import com.hhly.lottosplit.constants.SymbolConstants;

public class LotteryTypeProvider {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String selectLotteryType(Map map){
		List<Integer> lotteryCodes = (List<Integer>)map.get("list");
		StringBuffer sb = new StringBuffer("SELECT ID id,LOTTERY_CODE lotteryCode,SPLIT_MAX_NUM splitMaxNum,SPLIT_MAX_AMOUNT splitMaxAmount FROM LOTTERY_TYPE WHERE LOTTERY_CODE IN (");
		for(Integer i : lotteryCodes){
			sb.append(i).append(SymbolConstants.COMMA);
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(SymbolConstants.PARENTHESES_RIGHT);
		return sb.toString();
	}
}
