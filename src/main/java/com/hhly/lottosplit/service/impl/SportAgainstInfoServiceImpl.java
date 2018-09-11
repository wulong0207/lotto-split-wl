package com.hhly.lottosplit.service.impl;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.mapper.SportAgainstInfoDaoMapper;
import com.hhly.lottosplit.service.SportAgainstInfoService;

@Service("sportAgainstInfoService")
public class SportAgainstInfoServiceImpl implements SportAgainstInfoService {
	@Autowired
	SportAgainstInfoDaoMapper sportAgainstInfoDaoMapper;

	@Override
	public  Map<String,SportAgainstInfoBO> findBySystemCodeSLotteryCode(Set<String> set,Integer lotteryCode) {
		return sportAgainstInfoDaoMapper.findSportAgainstInfoS(set,lotteryCode);
	}

}
