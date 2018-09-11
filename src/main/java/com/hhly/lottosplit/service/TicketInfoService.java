package com.hhly.lottosplit.service;/**
 * Created by wulong on 2017/5/9.
 */


import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.lottosplit.bo.TicketInfoBO;

/**
 * @ClassName: TicketInfoService 
 * @Description: 票信息接口类 
 * @author wuLong
 * @date 2017年12月14日 下午4:27:48 
 *
 */
public interface TicketInfoService {
    /**
     * 批量添加票
     * @param ticketInfos
     * @return
     * @throws SQLException
     */
    void addTicketInfoList(List<TicketInfoBO> ticketInfos) throws SQLException;
    /**
	 * 
	 * @Description: 根据订单编号查询票记录数 
	 * @param orderCode 订单编号
	 * @return int
	 * @author wuLong
	 * @date 2017年5月25日 上午9:05:43
	 */
    int findCountByOrderCode(@Param("orderCode")String orderCode) throws SQLException;
}
