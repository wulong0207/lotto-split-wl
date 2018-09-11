package com.hhly.lottosplit.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.enums.OrderEnum;
import com.hhly.lottosplit.mapper.OrderGroupDaoMapper;
import com.hhly.lottosplit.mapper.OrderInfoDaoMapper;
import com.hhly.lottosplit.persistence.order.po.OrderGroupPO;
import com.hhly.lottosplit.service.OrderService;

/**
 * @author wulong
 * @create 2017/5/9 14:54
 */
@Service("orderInfoService")
@Transactional
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderInfoDaoMapper orderInfoDaoMapper;

	@Autowired
	private OrderGroupDaoMapper orderGroupDaoMapper;

    @Override
    @Transactional(propagation=Propagation.REQUIRED)
    public int updateOrderStatus(OrderEnum.OrderStatus orderStatus, Integer orderId, String modifyBy,Integer splitNum) throws SQLException{
       return  orderInfoDaoMapper.updateOrderStatus(orderId, orderStatus.getValue(),modifyBy,splitNum);
    }

    /**
	 * @param orderCodes 订单编号
	 * @return
     * @Description: 根据订单编号查询订单信息表
     * @author wuLong
     * @date 2017年3月27日 下午4:22:07
     */
    @Override
    public List<OrderInfoBO> getOrderInfos(List<String> orderCodes) {
    	//查询订单表
    	List<OrderInfoBO> infoBOs = orderInfoDaoMapper.getOrderInfoS(orderCodes);
        return infoBOs;
    }

	@Override
	public List<String> getOrderInfoList(Integer buyType) {
		return orderInfoDaoMapper.getOrderCodes(buyType);
	}

	@Override
	@Transactional(readOnly=true)
	public OrderInfoBO getByOrderCode(String orderCode) {
		return orderInfoDaoMapper.getByOrderCode(orderCode);
	}

	@Override
	public List<OrderDetailBO> findOrderDetail(String orderCode) {
		return orderInfoDaoMapper.findOrderDetail(orderCode);
	}

	@Override
    @Transactional(propagation=Propagation.REQUIRED)
	public int updateOrderStatusSplitting(int orderId) {
		return  orderInfoDaoMapper.updateOrderStatusSplitting(orderId);
	}

	/**
	 * 查询合买信息
	 *
	 * @param orderCode
	 * @return
	 */
	@Override
	public OrderGroupPO selectByOrderCode(String orderCode) {
		return orderGroupDaoMapper.selectByOrderCode(orderCode);
	}

}
