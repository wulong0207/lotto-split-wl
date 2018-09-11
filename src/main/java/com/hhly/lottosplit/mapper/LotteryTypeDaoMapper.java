package com.hhly.lottosplit.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.SelectProvider;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.persistence.lottery.LotteryTypeProvider;

/**
 * 
 * @ClassName: LotteryTypeDaoMapper 
 * @Description: 彩种基本配置信息
 * @author wuLong
 * @date 2017年6月1日 下午5:36:55 
 *
 */
public interface LotteryTypeDaoMapper {
	
	@SelectProvider(type = LotteryTypeProvider.class , method = "selectLotteryType" )
	@MapKey("lotteryCode")
	Map<Integer, LotteryTypeBO> findMap(List<Integer> lotteryCodes);
	
}