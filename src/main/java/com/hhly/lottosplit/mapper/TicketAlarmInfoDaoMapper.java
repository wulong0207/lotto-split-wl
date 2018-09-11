package com.hhly.lottosplit.mapper;

import com.hhly.lottosplit.persistence.ticket.po.TicketAlarmConfigPO;
import com.hhly.lottosplit.persistence.ticket.po.TicketAlarmInfoPO;


public interface TicketAlarmInfoDaoMapper {

	/**
     * 添加报警信息
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年5月9日 下午3:41:37
     * @param po
     * @return
     */
    int addAlarmInfo(TicketAlarmInfoPO po);
    
    /**
    * 
    * @Description: 获取报警配置
    * @param alarmConfigId
    * @return
    * @author wuLong
    * @date 2017年5月17日 上午11:56:17
    */
	TicketAlarmConfigPO getAlarmConfig(int alarmConfigId);
   
}