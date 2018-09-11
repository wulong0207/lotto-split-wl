package com.hhly.lottosplit.persistence.order.po;

import java.util.Date;

/**
 * @author huangb
 *
 * @Date 2016年11月30日
 *
 * @Desc 订单明细
 */
public class OrderDetailPO {
	/**
	 * 编号
	 */
	private Long id;
	/**
	 * 订单编号
	 */
	private String orderCode;
	/**
	 * 上传的投注内容地址
	 */
	private String bettingContentUrl;
	/**
	 * 竞技彩购买的场次编号
	 */
	private String buyScreen;
	/**
	 * 数字彩，低频彩所选的投注号码；若为上传，需要保存选好方式
	 */
	private String planContent;
	/**
	 * 单个方案的购买倍数
	 */
	private Integer multiple;
	/**
	 * 单个方案的购买金额
	 */
	private Double amount;
	/**
	 * 玩法 ：彩种表里子玩法的名字
	 */
	private String playIntro;
	/**
	 * 选号方式 1：手选；2：机选；3：上传；
	 */
	private Integer codeWay;
	/**
	 * 内容类型 1：单式；2：复式；3：胆拖；4：混合；5：上传
	 */
	private Integer contentType;
	/**
	 * 子彩种ID
	 */
	private Integer lotteryChildCode;
	/**
	 * 注数
	 */
	private Integer buyNumber;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 创建时间
	 */
	private Date createTime;

	public OrderDetailPO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getBettingContentUrl() {
		return bettingContentUrl;
	}

	public void setBettingContentUrl(String bettingContentUrl) {
		this.bettingContentUrl = bettingContentUrl;
	}

	public String getBuyScreen() {
		return buyScreen;
	}

	public void setBuyScreen(String buyScreen) {
		this.buyScreen = buyScreen;
	}

	public String getPlanContent() {
		return planContent;
	}

	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}

	public Integer getMultiple() {
		return multiple;
	}

	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPlayIntro() {
		return playIntro;
	}

	public void setPlayIntro(String playIntro) {
		this.playIntro = playIntro;
	}

	public Integer getCodeWay() {
		return codeWay;
	}

	public void setCodeWay(Integer codeWay) {
		this.codeWay = codeWay;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getLotteryChildCode() {
		return lotteryChildCode;
	}

	public void setLotteryChildCode(Integer lotteryChildCode) {
		this.lotteryChildCode = lotteryChildCode;
	}

	public Integer getBuyNumber() {
		return buyNumber;
	}

	public void setBuyNumber(Integer buyNumber) {
		this.buyNumber = buyNumber;
	}

}
