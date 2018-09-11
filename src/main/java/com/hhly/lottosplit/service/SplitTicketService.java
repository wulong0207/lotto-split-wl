package com.hhly.lottosplit.service;

import java.util.Map;

import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;

/**
 * @ClassName: SplitTicketService 
 * @Description: 拆票的入口接口类
 * @author wuLong
 * @date 2017年12月14日 下午4:29:04 
 *
 */
public interface SplitTicketService {
    /**
     * <li>1.先验证票表是否已经存在票了，防止重复拆票</li>
     * <li>2.数据基本验证</li>
     * <li>3.根据大彩种从拆票工厂获取拆票对象</li>
     * <li>4.执行拆票</li>
     * <li>5.验证票总金额与订单金额是否一致</li>
     * <li>6.添加票表记录</li>
     * <li>7.修改订单状态</li>
     * <li>8.发送消息到送票消息队列</li>
     * @Description: 进行拆票唯一入口方法
     * @param orderInfoBO 订单信息
     * @param map 彩种信息集合
     * @param againstInfoBOs 赛事集合
     * @throws Exception
     * @author wuLong
     * @date 2017年6月3日 下午2:49:04
     */
    void handleEntranceSplitTicket(OrderInfoBO orderInfoBO,Map<Integer,LotteryTypeBO> map,Map<String,SportAgainstInfoBO> sportAgainstInfoMap) throws Exception;
}