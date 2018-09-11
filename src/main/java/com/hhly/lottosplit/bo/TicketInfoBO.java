package com.hhly.lottosplit.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @desc 票信息
 * @author huangb
 * @date 2017年2月21日
 * @company 益彩网络
 * @version v1.0
 */
@SuppressWarnings("serial")
public class TicketInfoBO extends BaseBO {
	/**
	 * 本地票号
	 */
	private Long id;
	/**
	 * 彩种ID
	 */
	private Integer lotteryCode;
	/**
	 * 彩种名称
	 */
	private String lotteryName;
	/**
	 * 彩期
	 */
	private String lotteryIssue;
	/**
	 * 会员ID
	 */
	private Long userId;
	/**
	 * 用户对象
	 */
	private LottoCustomerBO user;
	/**
	 * 票状态：-2出票失败;-1送票失败;0不出票;1待分配;2已分配;3已送票;4已出票
	 */
	private Short ticketStatus;
	/**
	 * 票金额
	 */
	private BigDecimal ticketMoney;
	/**
	 * 出票渠道ID
	 */
	private String channelId;
	/**
	 * 出票渠道
	 */
	private TicketChannelBO ticketChannel;
	/** 所属方案编号 **/
	private String orderCode;
	/** 中奖状态：1：未开奖；2：未中奖；3：已中奖；4：已派奖 **/
	private Short winningStatus;
	/** 税前金额 **/
	private Double preBonus;
	/** 税后金额 **/
	private Double aftBonus;
	/** 拆票生成时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	/** 出票截止时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTicketTime;
	/** 送票时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date sendChannelTime;
	/** 出票成功时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date comeOutTime;
	/** 票回执时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date receiptTime;
	/** 开奖的时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date lotteryTime;
	/** 竞彩方案最近一场比赛时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startMatchtime;
	/** 内容类型：1：单式；2：复式；3：胆拖；4：混合；5：上传 **/
	private Integer contentType;
	/** 完成派奖的时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date sendTime;
	/** 竞彩编号:竞技彩购买的场次编号 **/
	private String buyScreen;
	/** 大乐透追号:0否;1是 **/
	private Short lottoAdd;
	/** 活动表的活动ID **/
	private String activitySource;
	/** 批次号 **/
	private String batchNum;
	/** 官方编号 **/
	private String officialNum;
	/** 奖项;内容格式：三等奖_5注，四等奖_12注 **/
	private String winningDetail;
	/** 票倍数 **/
	private Integer multipleNum;
	/** 加奖奖金 **/
	private Double addedBonus;
	/** 开奖后生成的优惠券中的红包编号ID(系统自动发放的红包编号ID) **/
	private String redCode;
	/** 第三方票序号 **/
	private String thirdNum;
	/** 出票商返回备注 **/
	private String channelRemark;
	/** 切票记录 **/
	private String ticketChange;
	/** 投注内容 **/
	private String ticketContent;
	/** 回执内容 **/
	private String receiptContent;
	/** 票图片 **/
	private String ticketImg;
	/** 票图片内容 **/
	private String ticketImgTxt;
	/** 修改时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date modifyTime;
	/** 修改人 **/
	private String modifyBy;
	/** 操作备注 **/
	private String ticketRemark;
	/** 子彩种 **/
	private Integer lotteryChildCode;

	// 总条数
	private Integer count;
	/**本站开始送票时间**/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date saleTime;
	/**订单详情id（目前仅竞技彩使用）**/
	private Integer orderDetailId;
	/**
	 * 中心兑换码
	 */
	private String redeemCode;
	
	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getLotteryCode() {
		return lotteryCode;
	}

	public void setLotteryCode(Integer lotteryCode) {
		this.lotteryCode = lotteryCode;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public String getLotteryIssue() {
		return lotteryIssue;
	}

	public void setLotteryIssue(String lotteryIssue) {
		this.lotteryIssue = lotteryIssue;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LottoCustomerBO getUser() {
		return user;
	}

	public void setUser(LottoCustomerBO user) {
		this.user = user;
	}

	public Short getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(Short ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public BigDecimal getTicketMoney() {
		return ticketMoney;
	}

	public void setTicketMoney(BigDecimal ticketMoney) {
		this.ticketMoney = ticketMoney;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public TicketChannelBO getTicketChannel() {
		return ticketChannel;
	}

	public void setTicketChannel(TicketChannelBO ticketChannel) {
		this.ticketChannel = ticketChannel;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Short getWinningStatus() {
		return winningStatus;
	}

	public void setWinningStatus(Short winningStatus) {
		this.winningStatus = winningStatus;
	}

	public Double getPreBonus() {
		return preBonus;
	}

	public void setPreBonus(Double preBonus) {
		this.preBonus = preBonus;
	}

	public Double getAftBonus() {
		return aftBonus;
	}

	public void setAftBonus(Double aftBonus) {
		this.aftBonus = aftBonus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getSendChannelTime() {
		return sendChannelTime;
	}

	public void setSendChannelTime(Date sendChannelTime) {
		this.sendChannelTime = sendChannelTime;
	}

	public Date getComeOutTime() {
		return comeOutTime;
	}

	public void setComeOutTime(Date comeOutTime) {
		this.comeOutTime = comeOutTime;
	}

	public Date getReceiptTime() {
		return receiptTime;
	}

	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}

	public Date getLotteryTime() {
		return lotteryTime;
	}

	public void setLotteryTime(Date lotteryTime) {
		this.lotteryTime = lotteryTime;
	}

	public Integer getContentType() {
		return contentType;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getBuyScreen() {
		return buyScreen;
	}

	public void setBuyScreen(String buyScreen) {
		this.buyScreen = buyScreen;
	}

	public Short getLottoAdd() {
		return lottoAdd;
	}

	public void setLottoAdd(Short lottoAdd) {
		this.lottoAdd = lottoAdd;
	}

	public String getActivitySource() {
		return activitySource;
	}

	public void setActivitySource(String activitySource) {
		this.activitySource = activitySource;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getOfficialNum() {
		return officialNum;
	}

	public void setOfficialNum(String officialNum) {
		this.officialNum = officialNum;
	}

	public String getWinningDetail() {
		return winningDetail;
	}

	public void setWinningDetail(String winningDetail) {
		this.winningDetail = winningDetail;
	}

	public Integer getMultipleNum() {
		return multipleNum;
	}

	public void setMultipleNum(Integer multipleNum) {
		this.multipleNum = multipleNum;
	}

	public Double getAddedBonus() {
		return addedBonus;
	}

	public void setAddedBonus(Double addedBonus) {
		this.addedBonus = addedBonus;
	}

	

	public String getRedCode() {
		return redCode;
	}

	public void setRedCode(String redCode) {
		this.redCode = redCode;
	}

	public String getThirdNum() {
		return thirdNum;
	}

	public void setThirdNum(String thirdNum) {
		this.thirdNum = thirdNum;
	}

	public String getChannelRemark() {
		return channelRemark;
	}

	public void setChannelRemark(String channelRemark) {
		this.channelRemark = channelRemark;
	}

	public String getTicketChange() {
		return ticketChange;
	}

	public void setTicketChange(String ticketChange) {
		this.ticketChange = ticketChange;
	}

	public String getTicketContent() {
		return ticketContent;
	}

	public void setTicketContent(String ticketContent) {
		this.ticketContent = ticketContent;
	}

	public String getReceiptContent() {
		return receiptContent;
	}

	public void setReceiptContent(String receiptContent) {
		this.receiptContent = receiptContent;
	}

	public String getTicketImg() {
		return ticketImg;
	}

	public void setTicketImg(String ticketImg) {
		this.ticketImg = ticketImg;
	}

	public String getTicketImgTxt() {
		return ticketImgTxt;
	}

	public void setTicketImgTxt(String ticketImgTxt) {
		this.ticketImgTxt = ticketImgTxt;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	public String getTicketRemark() {
		return ticketRemark;
	}

	public void setTicketRemark(String ticketRemark) {
		this.ticketRemark = ticketRemark;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getLotteryChildCode() {
		return lotteryChildCode;
	}

	public void setLotteryChildCode(Integer lotteryChildCode) {
		this.lotteryChildCode = lotteryChildCode;
	}

	public Date getEndTicketTime() {
		return endTicketTime;
	}

	public void setEndTicketTime(Date endTicketTime) {
		this.endTicketTime = endTicketTime;
	}

	public Date getStartMatchtime() {
		return startMatchtime;
	}

	public void setStartMatchtime(Date startMatchtime) {
		this.startMatchtime = startMatchtime;
	}

	public Date getSaleTime() {
		return saleTime;
	}

	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}
	
	public String getRedeemCode() {
		return redeemCode;
	}

	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
	}

	@Override
	public String toString() {
		return "TicketInfoBO [id=" + id + ", lotteryCode=" + lotteryCode + ", lotteryName=" + lotteryName
				+ ", lotteryIssue=" + lotteryIssue + ", userId=" + userId + ", user=" + user + ", ticketStatus="
				+ ticketStatus + ", ticketMoney=" + ticketMoney + ", channelId=" + channelId + ", ticketChannel="
				+ ticketChannel + ", orderCode=" + orderCode + ", winningStatus=" + winningStatus + ", preBonus="
				+ preBonus + ", aftBonus=" + aftBonus + ", createTime=" + createTime + ", endTicketTime="
				+ endTicketTime + ", sendChannelTime=" + sendChannelTime + ", comeOutTime=" + comeOutTime
				+ ", receiptTime=" + receiptTime + ", lotteryTime=" + lotteryTime + ", startMatchtime=" + startMatchtime
				+ ", contentType=" + contentType + ", sendTime=" + sendTime + ", buyScreen=" + buyScreen + ", lottoAdd="
				+ lottoAdd + ", activitySource=" + activitySource + ", batchNum=" + batchNum + ", officialNum="
				+ officialNum + ", winningDetail=" + winningDetail + ", multipleNum=" + multipleNum + ", addedBonus="
				+ addedBonus + ", redCode=" + redCode + ", thirdNum=" + thirdNum + ", channelRemark=" + channelRemark
				+ ", ticketChange=" + ticketChange + ", ticketContent=" + ticketContent + ", receiptContent="
				+ receiptContent + ", ticketImg=" + ticketImg + ", ticketImgTxt=" + ticketImgTxt + ", modifyTime="
				+ modifyTime + ", modifyBy=" + modifyBy + ", ticketRemark=" + ticketRemark + ", lotteryChildCode="
				+ lotteryChildCode + ", count=" + count + ", saleTime=" + saleTime + ", orderDetailId=" + orderDetailId
				+ ", redeemCode=" + redeemCode + "]";
	}
	
	


}