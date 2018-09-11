package com.hhly.lottosplit.mapper;


import com.hhly.lottosplit.persistence.order.po.OrderGroupPO;
import org.apache.ibatis.annotations.Param;

public interface OrderGroupDaoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(OrderGroupPO record);

    int insertSelective(OrderGroupPO record);

    OrderGroupPO selectByOrderCode(@Param("orderCode") String orderCode);

    int updateByPrimaryKeySelective(OrderGroupPO record);

    int updateByPrimaryKey(OrderGroupPO record);
}