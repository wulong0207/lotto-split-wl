package com.hhly.lottosplit.mapper;

import org.apache.ibatis.annotations.Param;

import com.hhly.lottosplit.persistence.issue.po.LotteryIssuePO;

/**
 * @desc 彩期
 * @author jiangwei
 * @date 2017-2-16
 * @company 益彩网络科技公司
 * @version 1.0
 */
public interface LotteryIssueDaoMapper {
	/**
	 * 
	 * @Description: 查询彩期信息
	 * @param lotteryCode 彩种ID
	 * @param issueCode 彩期
	 * @return 
	 * @author wuLong
	 * @date 2017年5月18日 下午3:18:15
	 */
	LotteryIssuePO findLotteryIssue(@Param("lotteryCode")int lotteryCode, @Param("issueCode")String issueCode);
	/**
	 * 
	 * @Description: 查询彩种当前期信息
	 * @param lotteryCode
	 * @return
	 * @author wuLong
	 * @date 2017年7月22日 上午10:22:07
	 */
	LotteryIssuePO findCurrentIssue(@Param("lotteryCode")int lotteryCode);
	/**
	 * 
	 * @Description: 查找下一期信息
	 * @param lotteryCode
	 * @param issueCode
	 * @return
	 * @author wuLong
	 * @date 2017年11月8日 上午11:11:41
	 */
	LotteryIssuePO findNextLotteryIssue(@Param("lotteryCode")int lotteryCode, @Param("issueCode")String issueCode);
}