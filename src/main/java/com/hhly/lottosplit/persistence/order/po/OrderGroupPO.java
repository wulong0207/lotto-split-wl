package com.hhly.lottosplit.persistence.order.po;

import java.util.Date;

public class OrderGroupPO {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 合买发起人；关联用户表
     */
    private Integer userId;

    /**
     * 合买订单编号
     */
    private String orderCode;

    /**
     * 合买状态;1：招募中；2：已满员；3：合买流产
     */
    private Integer grpbuyStatus;

    /**
     * 最低认购比例
     */
    private Double minBuyRatio;

    /**
     * 最低认购金额
     */
    private Double minBuyAmount;

    /**
     * 保底比例
     */
    private Double guaranteeRatio;

    /**
     * 保底金额
     */
    private Double guaranteeAmount;

    /**
     * 公开类型;1：完全公开；2：跟单后公开；3：开奖后公开；
     */
    private Integer visibleType;

    /**
     * 提成比例
     */
    private Double commissionRatio;

    /**
     * 提成金额
     */
    private Double commissionAmount;

    /**
     * 合买方式;1：所有人可认购；2：凭密码认购；
     */
    private Integer applyWay;

    /**
     * 认购密码；当合买方式为凭密码认购时，提供的认购码
     */
    private String applyCode;

    /**
     * 合买方案标题
     */
    private String title;

    /**
     * 合买方案宣言
     */
    private String description;

    /**
     * 是否置顶 0：不置顶；1：置顶；
     */
    private Integer isTop;

    /**
     * 是否加推荐 0：不推荐；1：推荐；
     */
    private Integer isRecommend;

    /**
     * 合买进度
     */
    private Double progress;

    /**
     * 进度金额
     */
    private Double progressAmount;

    /**
     * 合买总数
     */
    private Integer buyCount;

    /**
     * 创建时间/发起时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String modifyBy;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注说明
     */
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getGrpbuyStatus() {
        return grpbuyStatus;
    }

    public void setGrpbuyStatus(Integer grpbuyStatus) {
        this.grpbuyStatus = grpbuyStatus;
    }

    public Double getMinBuyRatio() {
        return minBuyRatio;
    }

    public void setMinBuyRatio(Double minBuyRatio) {
        this.minBuyRatio = minBuyRatio;
    }

    public Double getMinBuyAmount() {
        return minBuyAmount;
    }

    public void setMinBuyAmount(Double minBuyAmount) {
        this.minBuyAmount = minBuyAmount;
    }

    public Double getGuaranteeRatio() {
        return guaranteeRatio;
    }

    public void setGuaranteeRatio(Double guaranteeRatio) {
        this.guaranteeRatio = guaranteeRatio;
    }

    public Double getGuaranteeAmount() {
        return guaranteeAmount;
    }

    public void setGuaranteeAmount(Double guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    public Integer getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(Integer visibleType) {
        this.visibleType = visibleType;
    }

    public Double getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(Double commissionRatio) {
        this.commissionRatio = commissionRatio;
    }

    public Double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Integer getApplyWay() {
        return applyWay;
    }

    public void setApplyWay(Integer applyWay) {
        this.applyWay = applyWay;
    }

    public String getApplyCode() {
        return applyCode;
    }

    public void setApplyCode(String applyCode) {
        this.applyCode = applyCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public Double getProgressAmount() {
        return progressAmount;
    }

    public void setProgressAmount(Double progressAmount) {
        this.progressAmount = progressAmount;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}