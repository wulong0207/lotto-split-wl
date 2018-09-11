package com.hhly.lottosplit.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.mapper.LotteryIssueDaoMapper;
import com.hhly.lottosplit.mapper.LotteryTypeDaoMapper;
import com.hhly.lottosplit.mapper.SportAgainstInfoDaoMapper;
import com.hhly.lottosplit.service.LotteryTypeService;
import com.hhly.lottosplit.utils.ObjectUtil;

@Service
public class LotteryTypeServiceImpl implements LotteryTypeService {
	@Autowired
	LotteryTypeDaoMapper lotteryTypeDaoMapper;
	@Autowired
	SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;
	@Autowired
	LotteryIssueDaoMapper lotteryIssueDaoMapper; 
	
	@Override
	public Map<Integer, LotteryTypeBO> LotteryTypeBoMap(List<Integer> lotteryCodes) {
		if(ObjectUtil.isBlank(lotteryCodes)){
    		return null;
    	}
    	return lotteryTypeDaoMapper.findMap(lotteryCodes);
	}

	@Override
	public Map<String,SportAgainstInfoBO> findSportAgainstInfoS(Set<String> set,Integer lotteryCode) {
    	if(ObjectUtil.isBlank(set)){
    		return null;
    	}
    	return sportAgainstInfoDaoMapper.findSportAgainstInfoS(set,lotteryCode);
	}
}
