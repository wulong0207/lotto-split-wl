package com.hhly.lottosplit.bo;

/**
 * 彩种
 */
public class LotteryTypeBO extends BaseBO{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 953123328733656372L;
    /**
     * 低频：100开始；高频：200；竞技彩：300
     */
    private Integer id;

    /**
     * 固定彩种code。
     */
    private Integer lotteryCode;

    /**
     * 彩票最大倍数
     */
    private Integer splitMaxNum;


    /**
     * 彩票最大金额
     */
    private Integer splitMaxAmount;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLotteryCode() {
        return lotteryCode;
    }

    public void setLotteryCode(Integer lotteryCode) {
        this.lotteryCode = lotteryCode;
    }


    public Integer getSplitMaxNum() {
        return splitMaxNum;
    }

    public void setSplitMaxNum(Integer splitMaxNum) {
        this.splitMaxNum = splitMaxNum;
    }

    public Integer getSplitMaxAmount() {
        return splitMaxAmount;
    }

    public void setSplitMaxAmount(Integer splitMaxAmount) {
        this.splitMaxAmount = splitMaxAmount;
    }

	@Override
	public String toString() {
		return "LotteryTypeBO [id=" + id + ", lotteryCode=" + lotteryCode + ", splitMaxNum=" + splitMaxNum
				+ ", splitMaxAmount=" + splitMaxAmount + "]";
	}
    
}