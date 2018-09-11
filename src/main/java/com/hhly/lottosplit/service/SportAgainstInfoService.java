package com.hhly.lottosplit.service;

import java.util.Map;
import java.util.Set;

import com.hhly.lottosplit.bo.SportAgainstInfoBO;

/**
 * 
 * @ClassName: SportAgainstInfoService 
 * @Description: 竞技彩赛事服务接口类
 * @author wuLong
 * @date 2017年12月14日 下午4:27:16 
 *
 */
public interface SportAgainstInfoService {
	/**
	 * 
	 * @Description: 查询对阵信息
	 * @param systemCodes 对阵编号
	 * @param lotteryCode 彩种id
	 * @return List
	 * @author wuLong
	 * @date 2017年5月17日 上午9:44:53
	 */
	public Map<String,SportAgainstInfoBO> findBySystemCodeSLotteryCode(Set<String> set,Integer lotteryCode);
}
