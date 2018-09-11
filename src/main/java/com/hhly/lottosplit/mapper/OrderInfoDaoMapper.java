package com.hhly.lottosplit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;

/**
 * @author huangb
 *
 * @Date 2016年11月30日
 *
 * @Desc 订单处理数据接口
 */
public interface OrderInfoDaoMapper {
	
	/**************************** Used to LOTTO ******************************/
	
	
	/**
	 * 
	 * @Description: 根据订单编号查询订单信息表
	 * @param orderCode 订单编号
	 * @return
	 * @author wuLong
	 * @date 2017年3月27日 下午4:22:07
	 */
	List<OrderInfoBO> getOrderInfoS(@Param("orderCodes")List<String> orderCodes);
	
	/**
	 * 
	 * @param vo
	 * @return
	 */
	List<OrderDetailBO> findOrderDetail(@Param("orderCode")String orderCode);
	
	  /**
     * 修改订单信息
     * @author wuLong
     * @Version 1.0
     * @CreatDate 2017年3月14日 下午3:23:28
     * @param ids
     * @param orderStatus
     * @splitNum 拆票总数
     * @return
     */
	int updateOrderStatus(@Param("orderId")Integer orderId,@Param("orderStatus")Short orderStatus,@Param("modifyBy")String modifyBy,@Param("splitNum")Integer splitNum);
	/**
	 * 查询符合拆票的订单编号
	 * @Description: 根据购买类型   
	 * @param buyType   为null 查询所有购买类型   
	 * @return List<String>
	 * @author wuLong
	 * @date 2017年5月22日 下午12:04:20
	 */
	List<String> getOrderCodes(@Param("buyType")Integer buyType);
	
	 /**
     * 根据订单号查询订单基本信息
     * @Description: 
     * @param orderCode 根据订单编号 
     * @return OrderInfoBO
     * @author wuLong
     * @date 2017年6月6日 下午7:11:23
     */
    OrderInfoBO getByOrderCode(@Param("orderCode")String orderCode);
    /**
     * 修改订单状态为拆票中
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年11月14日 下午4:56:26
     * @param orderId
     * @return
     */
	int updateOrderStatusSplitting(@Param("orderId")int orderId);
	
}