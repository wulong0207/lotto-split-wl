package com.hhly.lottosplit.mapper;


import java.util.List;

import com.hhly.lottosplit.bo.CooperateCdkeyBO;

public interface CooperateCdkeyMapper {

    int selectCount(CooperateCdkeyBO vo);

    int deleteByPrimaryKey(Integer id);

    int insert(CooperateCdkeyBO record);

    int insertSelective(CooperateCdkeyBO record);

    CooperateCdkeyBO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CooperateCdkeyBO record);

    int updateByPrimaryKey(CooperateCdkeyBO record);
    /**
     * 
     * @Description: 根据本站兑换码
     * @param myCdkey
     * @return 中心兑换码
     * @author wuLong
     * @date 2018年3月8日 上午10:28:45
     */
    List<CooperateCdkeyBO> selectByMyCdkey(String myCdkey);
}