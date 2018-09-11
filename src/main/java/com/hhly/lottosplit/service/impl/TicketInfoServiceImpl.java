package com.hhly.lottosplit.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.mapper.TicketInfoDaoMapper;
import com.hhly.lottosplit.service.TicketInfoService;
import com.hhly.lottosplit.utils.SplitTicketUtil;

/**
 * @author wuLong
 * @create 2017/5/9 14:20
 */
@Service("ticketInfoService")
public class TicketInfoServiceImpl implements TicketInfoService {
	
    @Autowired
    TicketInfoDaoMapper ticketInfoDaoMapper;

    @Override
    @Transactional(propagation =Propagation.REQUIRED,rollbackFor = RuntimeException.class)
    public void addTicketInfoList(List<TicketInfoBO> ticketInfos) throws SQLException {
		List<List<TicketInfoBO>> lists = SplitTicketUtil.subList(SplitTicketUtil.NUM_FOUR_THOUSAND, ticketInfos);
		for(List<TicketInfoBO> infoBOs : lists){
			ticketInfoDaoMapper.addTicketInfoList(infoBOs);
		}
    }
    
	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=true)
	public int findCountByOrderCode(String orderCode) throws SQLException {
		return ticketInfoDaoMapper.findCountByOrderCode(orderCode);
	}
    
}
