package com.hhly.lottosplit.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;

public interface LotteryTypeService {
	/**
	 * 
	 * @Description: 返回一个彩种信息表map <id,LotteryTypeBO> 
	 * @param lotteryCodes 大彩种list集合
	 * @return
	 * @author wuLong
	 * @date 2017年6月3日 下午2:47:27
	 */
	Map<Integer,LotteryTypeBO> LotteryTypeBoMap(List<Integer> lotteryCodes);
	/**
	 * 
	 * @Description: 根据赛事集合查询所有赛事信息
	 * @param set 赛事systemCode集合
	 * @return
	 * @author wuLong
	 * @date 2017年6月3日 下午2:47:04
	 */
	Map<String,SportAgainstInfoBO> findSportAgainstInfoS(Set<String> set,Integer lotteryCode);
}
