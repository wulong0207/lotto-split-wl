package com.hhly.lottosplit.service.ticket.biglottery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhly.lottosplit.aware.SpringAware;
import com.hhly.lottosplit.bo.LotteryTypeBO;
import com.hhly.lottosplit.bo.OrderDetailBO;
import com.hhly.lottosplit.bo.OrderInfoBO;
import com.hhly.lottosplit.bo.SportAgainstInfoBO;
import com.hhly.lottosplit.bo.TicketInfoBO;
import com.hhly.lottosplit.constants.SymbolConstants;
import com.hhly.lottosplit.enums.LotteryEnum;
import com.hhly.lottosplit.event.SplitHandlerEvent;
import com.hhly.lottosplit.exception.ServiceRuntimeException;
import com.hhly.lottosplit.mapper.LotteryIssueDaoMapper;
import com.hhly.lottosplit.persistence.issue.po.LotteryIssuePO;
import com.hhly.lottosplit.service.ticket.AbstractCSportsSplitTicket;
import com.hhly.lottosplit.utils.ObjectUtil;
import com.hhly.lottosplit.utils.SplitTicketUtil;
import com.hhly.lottosplit.utils.convert.ParseConvertTicketInfo;

/**
 * @author lgs on
 * @version 1.0
 * @desc 冠亚军竞猜 拆票
 * @date 2018/3/20.
 * @company 益彩网络科技有限公司
 */
@Component
public class WinSplitTicket extends AbstractCSportsSplitTicket {

    @Autowired
    private LotteryIssueDaoMapper lotteryIssueDaoMapper;


    @PostConstruct
    public void init() {
        SplitHandlerEvent event = new SplitHandlerEvent(this);
        SpringAware.getApplicationContext().publishEvent(event);
    }

    /**
     * @param orderInfoBO
     * @param lotteryTypeBO
     * @param sportAgainstInfoMap @return List
     * @Description: 拆票处理
     * @author wuLong
     * @date 2017年4月6日 下午4:15:58
     */
    @Override
    public List<TicketInfoBO> excute(OrderInfoBO orderInfoBO, LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap) throws Exception {
        List<TicketInfoBO> ticketInfoBOS = new ArrayList<>();
        List<OrderDetailBO> detailBOs = orderInfoBO.getDetailBOs();
        LotteryIssuePO lotteryIssuePO = lotteryIssueDaoMapper.findLotteryIssue(orderInfoBO.getLotteryCode(), orderInfoBO.getLotteryIssue());
        if (lotteryIssuePO == null) {
            throw new ServiceRuntimeException("冠亚军游戏彩期：" + orderInfoBO.getLotteryIssue() + ",不存在");
        }
        for (OrderDetailBO detailBO : detailBOs) {
            single(detailBO, orderInfoBO, ticketInfoBOS, lotteryTypeBO, sportAgainstInfoMap, lotteryIssuePO, null);
        }
        return ticketInfoBOS;
    }

    /**
     * @return LotteryEnum.LotteryPr
     * @Description: 大彩种具体枚举分类
     * @author wuLong
     * @date 2017年12月7日 下午4:37:25
     */
    @Override
    public LotteryEnum.LotteryPr getLotteryPr() {
        return LotteryEnum.LotteryPr.GYJJC;
    }

    /**
     * @param orderDetailBO
     * @param orderInfoBO
     * @param ticketInfoBOs
     * @param lotteryTypeBO
     * @param sportAgainstInfoMap
     * @param lotteryIssuePO
     * @param nextLotteryIssuePO  @author wuLong
     * @Description: 单式
     * @date 2017年6月27日 下午4:05:27
     */
    @Override
    public void single(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap, LotteryIssuePO lotteryIssuePO, LotteryIssuePO nextLotteryIssuePO) {
        //原始投注内容已^分割
        String pc[] = orderDetailBO.getPlanContent().split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UP_CAP);
        //投注内容
        String content = pc[SplitTicketUtil.NUM_ZERO];
        //过关方式
        String passway = pc[SplitTicketUtil.NUM_ONE];
        //当个方案详情的总倍数 = 详情倍数 * 订单倍数
        Integer multiple = orderDetailBO.getMultiple() * orderInfoBO.getMultipleNum();
        //方案投注的选项内容，是手选 || 机选
        int contType = SplitTicketUtil.NUM_ONE;
        List<String> list = SplitTicketUtil.getByMultipleSplit(multiple, passway, content, lotteryTypeBO.getSplitMaxNum());
        if (!ObjectUtil.isBlank(list)) {
            String[] s = list.toArray(new String[list.size()]);
            String appOrderDetailIdList = StringUtils.join(s, SymbolConstants.UP_CAP + orderDetailBO.getId() + SymbolConstants.AND) + (SymbolConstants.UP_CAP + orderDetailBO.getId());
            List<String> aList = Arrays.asList(appOrderDetailIdList.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.AND));
            assembleData(orderDetailBO, orderInfoBO, ticketInfoBOs, aList, contType, sportAgainstInfoMap, lotteryTypeBO, lotteryIssuePO, nextLotteryIssuePO);
        }
    }

    /**
     * @param orderDetailBO
     * @param orderInfoBO
     * @param ticketInfoBOs
     * @param lotteryTypeBO
     * @param sportAgainstInfoMap
     * @param lotteryIssuePO
     * @param nextLotteryIssuePO  @author wuLong
     * @Description: 复式
     * @date 2017年6月27日 下午4:05:20
     */
    @Override
    public void compound(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap, LotteryIssuePO lotteryIssuePO, LotteryIssuePO nextLotteryIssuePO) {

    }

    /**
     * @param orderDetailBO
     * @param orderInfoBO
     * @param ticketInfoBOs
     * @param lotteryTypeBO
     * @param sportAgainstInfoMap
     * @param lotteryIssuePO
     * @param nextLotteryIssuePO  @author wuLong
     * @Description: 胆拖
     * @date 2017年6月27日 下午4:05:33
     */
    @Override
    public void dantuo(OrderDetailBO orderDetailBO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs, LotteryTypeBO lotteryTypeBO, Map<String, SportAgainstInfoBO> sportAgainstInfoMap, LotteryIssuePO lotteryIssuePO, LotteryIssuePO nextLotteryIssuePO) {

    }

    /**
     * @param detailVO            订单详情
     * @param orderInfoBO         订单信息
     * @param ticketInfoBOs       票集合
     * @param list                组装的每一注
     * @param contType            投注类型
     * @param sportAgainstInfoMap 竞技彩赛事信息集合
     * @param lotteryTypeBO       彩种信息
     * @param lotteryIssuePO      当前彩期信息
     * @param nextLotteryIssuePO  下一期彩期信息
     * @throws NumberFormatException
     * @throws ServiceRuntimeException
     * @Description: 按照子玩法和投注内容进行票的分类封装对象
     * @author wuLong
     * @date 2017年12月5日 下午4:28:11
     */
    protected void assembleData(OrderDetailBO detailVO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
                                List<String> list, Integer contType, Map<String, SportAgainstInfoBO> sportAgainstInfoMap
            , LotteryTypeBO lotteryTypeBO, LotteryIssuePO lotteryIssuePO, LotteryIssuePO nextLotteryIssuePO) {

        for (String c : list) {
            String a[] = c.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UP_CAP);
            int multipleNum = Integer.valueOf(a[SplitTicketUtil.NUM_TWO]);
            String passway = a[SplitTicketUtil.NUM_ONE];
            String content = a[SplitTicketUtil.NUM_ZERO];
            String[] contents = content.split(SymbolConstants.DOUBLE_SLASH_VERTICAL_BAR);
            int orderDetailId = Integer.valueOf(a[SplitTicketUtil.NUM_THREE]);
            for (String str : contents) {
                if (multipleNum > lotteryTypeBO.getSplitMaxNum()) {
                    List<String> strings = SplitTicketUtil.getNumberHighColorByMultipleSplit(multipleNum, str + SymbolConstants.UP_CAP + passway, lotteryTypeBO.getSplitMaxNum());
                    for (String d : strings) {
                        TicketInfoBO ticketInfoBO = new TicketInfoBO();
                        String e[] = d.split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UP_CAP);
                        multipleNum = Integer.valueOf(e[SplitTicketUtil.NUM_TWO]);
                        passway = e[SplitTicketUtil.NUM_ONE];
                        content = e[SplitTicketUtil.NUM_ZERO];
                        assemleDateTwo(detailVO, orderInfoBO, ticketInfoBOs, sportAgainstInfoMap, ticketInfoBO, passway,
                                multipleNum, content, contType, lotteryIssuePO, orderDetailId, nextLotteryIssuePO);
                        ticketInfoBO = null;
                    }
                } else {
                    TicketInfoBO ticketInfoBO = new TicketInfoBO();
                    assemleDateTwo(detailVO, orderInfoBO, ticketInfoBOs, sportAgainstInfoMap, ticketInfoBO, passway,
                            multipleNum, str, contType, lotteryIssuePO, orderDetailId, nextLotteryIssuePO);
                    ticketInfoBO = null;
                }
            }
        }
    }

    /**
     * @param detailVO            订单详情
     * @param orderInfoBO         订单信息
     * @param ticketInfoBOs       票集合
     * @param sportAgainstInfoMap 竞技彩赛事信息集合
     * @param ticketInfoBO        单张票
     * @param passway             票过关方式
     * @param multipleNum         票倍数
     * @param content             票投注内容
     * @param contType            票的投注类型
     * @param lotteryIssuePO      当前彩期信息
     * @param orderDetailId       订单详情ID
     * @param nextLotteryIssuePO  下一期彩期信息
     * @throws ServiceRuntimeException
     * @Description:转换到单张票对象
     * @author wuLong
     * @date 2017年12月5日 下午4:35:36
     */
    protected void assemleDateTwo(OrderDetailBO detailVO, OrderInfoBO orderInfoBO, List<TicketInfoBO> ticketInfoBOs,
                                  Map<String, SportAgainstInfoBO> sportAgainstInfoMap, TicketInfoBO ticketInfoBO, String passway,
                                  int multipleNum, String content, Integer contType, LotteryIssuePO lotteryIssuePO, Integer orderDetailId, LotteryIssuePO nextLotteryIssuePO) throws ServiceRuntimeException {
        ParseConvertTicketInfo.parseTicketInfoBo(orderInfoBO, detailVO, ticketInfoBO);
        ticketInfoBO.setOrderDetailId(orderDetailId);
        ticketInfoBO.setTicketContent(content + SymbolConstants.UP_CAP + passway);

        int zs = SplitTicketUtil.NUM_ONE;
        ticketInfoBO.setBuyScreen(SplitTicketUtil.getGjjcBuyScreen(content));
        ticketInfoBO.setMultipleNum(multipleNum);
        ticketInfoBO.setTicketMoney(BigDecimal.valueOf(Double.valueOf(zs * SplitTicketUtil.NUM_TWO * multipleNum)));

        Date endTicketTime = lotteryIssuePO.getSaleEndTime();
        //logger.info("订单编号:{},出票截止时间:{}",orderInfoBO.getOrderCode(),DateUtil.convertDateToStr(endTicketTime, DateUtil.DEFAULT_FORMAT));
        ticketInfoBO.setEndTicketTime(endTicketTime);
        ticketInfoBOs.add(ticketInfoBO);
    }


    /**
     * @param orderDetailBO
     * @param multipleNum
     * @return String[]  0：头注内容，1：过关方式，2：单个方案详情的总倍数 = 详情倍数 * 订单倍数
     * @Description: 解析投注内容、过关方式、投注倍数
     * @author wuLong
     * @date 2017年11月30日 上午11:17:46
     */
    private String[] analysisContent(OrderDetailBO orderDetailBO, Integer multipleNum) {
        //原始投注内容已^分割
        String pc[] = orderDetailBO.getPlanContent().split(SymbolConstants.DOUBLE_SLASH + SymbolConstants.UP_CAP);
        //投注内容
        String content = pc[SplitTicketUtil.NUM_ZERO];
        //过关方式
        String passway = pc[SplitTicketUtil.NUM_ONE];
        //当个方案详情的总倍数 = 详情倍数 * 订单倍数
        Integer multiple = orderDetailBO.getMultiple() * multipleNum;
        String[] contentMessage = new String[3];
        contentMessage[0] = content;
        contentMessage[1] = passway;
        contentMessage[2] = multiple + "";
        return contentMessage;
    }
}
