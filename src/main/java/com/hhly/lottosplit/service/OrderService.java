package com.hhly.lottosplit.service;

import java.sql.SQLException;
import java.util.List;

import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.enums.OrderEnum;
import com.hhly.lottosplit.persistence.order.po.OrderGroupPO;
import org.apache.ibatis.annotations.Param;

/**
 * @author wulong
 * @create 2017/5/9 14:13
 */
public interface OrderService {
    /**
     * 更新订单状态
     * @param orderStatus 订单状态
     * @param orderId 订单id
     * @param modifyBy 修改人
     * @author wuLong
     * @date 2017-04-19
     */
    int updateOrderStatus(OrderEnum.OrderStatus orderStatus, Integer orderId, String modifyBy,Integer splitNum)throws SQLException;
    /**
     * @Description: 根据订单编号查询订单信息表
     * @param orderCodes 订单编号
     * @return
     * @author wuLong
     * @date 2017年3月27日 下午4:22:07
     */
    List<OrderInfoBO> getOrderInfos(List<String> orderCodes);
    /**
	 * 查询符合拆票的订单编号
	 * @Description: 根据购买类型   
	 * @param buyType   为null 查询所有购买类型   
	 * @return List<String>
	 * @author wuLong
	 * @date 2017年5月22日 下午12:04:20
	 */
    List<String> getOrderInfoList(Integer buyType);
    /**
     * 根据订单号查询订单基本信息
     * @Description: 
     * @param orderCode 根据订单编号 
     * @return OrderInfoBO
     * @author wuLong
     * @date 2017年6月6日 下午7:11:23
     */
    OrderInfoBO getByOrderCode(String orderCode);
    /**
     * 
     * @Description: 查询订单详情
     * @param orderCode
     * @return
     * @author wuLong
     * @date 2017年6月23日 下午6:42:42
     */
    List<OrderDetailBO> findOrderDetail(String orderCode);
    /**
     * 修改订单状态为拆票中
     * @author wuLong
     * @Version 1.0
     * @CreatDate 2017年11月14日 下午4:54:57
     * @param orderId
     * @return
     */
	int updateOrderStatusSplitting(int orderId);


    /**
     * 查询合买信息
     *
     * @param orderCode
     * @return
     */
    OrderGroupPO selectByOrderCode(String orderCode);
}
