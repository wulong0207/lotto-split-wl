package com.hhly.lottosplit.bo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @author jiangwei
 * @Version 1.0
 * @CreatDate 2016-12-14 上午11:01:22
 * @Desc 方案类容详情
 */
public class OrderDetailBO extends BaseBO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2190594084388247333L;
	private Integer id;
	private String planContent;
	private Integer multiple;
	private Double amount;
	private String playIntro;
	private Integer codeWay;
	private Integer contentType;
	
	private String bettingContentUrl;
		
	private String buyScreen;
	
	private Integer lotteryChildCode;
	
	private Integer buyNumber;
	
	private String maxBuyScreen;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	

	public Integer getBuyNumber() {
		return buyNumber;
	}

	public void setBuyNumber(Integer buyNumber) {
		this.buyNumber = buyNumber;
	}

	public Integer getLotteryChildCode() {
		return lotteryChildCode;
	}

	public void setLotteryChildCode(Integer lotteryChildCode) {
		this.lotteryChildCode = lotteryChildCode;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getMaxBuyScreen() {
		return maxBuyScreen;
	}

	public void setMaxBuyScreen(String maxBuyScreen) {
		this.maxBuyScreen = maxBuyScreen;
	}

	@Override
	public String toString() {
		return "OrderDetailBO [id=" + id + ", planContent=" + planContent + ", multiple=" + multiple + ", amount="
				+ amount + ", playIntro=" + playIntro + ", codeWay=" + codeWay + ", contentType=" + contentType
				+ ", bettingContentUrl=" + bettingContentUrl + ", buyScreen=" + buyScreen + ", lotteryChildCode="
				+ lotteryChildCode + ", buyNumber=" + buyNumber + ", maxBuyScreen=" + maxBuyScreen + ", updateTime="
				+ updateTime + ", createTime=" + createTime + "]";
	}


}
