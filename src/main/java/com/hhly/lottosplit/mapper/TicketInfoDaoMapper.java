package com.hhly.lottosplit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.lottosplit.bo.TicketInfoBO;

/**
 * @desc 票信息数据接口
 * @author huangb
 * @date 2017年2月20日
 * @company 益彩网络
 * @version v1.0
 */
public interface TicketInfoDaoMapper {

	/**
	 * 批量修改票状态
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017年3月20日 下午3:54:08
	 * @param ids 主键 
	 * @param status 状态
	 * @param modifyBy 修改人
	 */
	void updateTicketStatus(@Param("ids")List<Integer> ids, @Param("status")Short status, @Param("modifyBy")String modifyBy);


	/**
	 * 添加票记录
	 * @param list <TicketInfoBO>
	 * @return ID
	 */
	int addTicketInfoList(@Param("ticketInfoBOs") List<TicketInfoBO> ticketInfoBOs);
	/**
	 * 
	 * @Description: 根据订单编号查询票记录数 
	 * @param orderCode 订单编号
	 * @return int
	 * @author wuLong
	 * @date 2017年5月25日 上午9:05:43
	 */
	int findCountByOrderCode(@Param("orderCode")String orderCode);
}