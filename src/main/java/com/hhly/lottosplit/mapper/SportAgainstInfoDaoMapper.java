package com.hhly.lottosplit.mapper;



import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.SelectProvider;

import com.hhly.lottosplit.bo.SportAgainstInfoBO;


public interface SportAgainstInfoDaoMapper {

    /**
     * 
     * @Description: 根据list对阵Id查询对阵信息
     * @param systemCodes
     * @param lotteryCode
     * @return
     * @author wuLong
     * @date 2017年3月24日 上午11:09:17
     */
	@SelectProvider(type = SportAgainstInfoDaoProvider.class,method="findSportAgainstInfo")
	@MapKey("systemCode")
    Map<String,SportAgainstInfoBO> findSportAgainstInfoS (Set<String> set,Integer lotteryCode);

}