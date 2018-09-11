package com.hhly.lottosplit.persistence.sport;

import java.util.Map;
import java.util.Set;

import com.hhly.lottosplit.constants.SymbolConstants;

public class SportAgainstInfoDaoProvider {
	
	@SuppressWarnings("unchecked")
	public String findSportAgainstInfo(Map<String, Object> para){
		@SuppressWarnings("rawtypes")
		Set<String> set = (Set)para.get("0");
		Integer lotteryCode = Integer.valueOf(para.get("1").toString()); 
		StringBuffer sb = new StringBuffer("SELECT SYSTEM_CODE systemCode,START_TIME startTime,issue_code issueCode FROM SPORT_AGAINST_INFO WHERE LOTTERY_CODE = ").append(lotteryCode).append(" AND SYSTEM_CODE IN (");
		for(String systemCode : set){
			sb.append(SymbolConstants.SINGLE_QUOTE).append(systemCode).append(SymbolConstants.SINGLE_QUOTE).append(SymbolConstants.COMMA);
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(SymbolConstants.PARENTHESES_RIGHT);
		return sb.toString();
	}
}
