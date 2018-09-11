/**
 * 
 */
package com.hhly.lottosplit.enums;

import java.util.Objects;


/**
 * @desc 彩种彩期枚举类
 * @author Bruce
 * @date 2017年1月19日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class LotteryEnum {

	/**
	 * @param lotteryChildCode
	 * @return
	 * @Description: 确认此订单是
	 * <li>GPC,高频彩</li>
	 * <li>SZC,数字彩</li>
	 * <li>BJDC,北单</li>
	 * <li>JJC,竞技彩</li>
	 * <li>ZC;足彩</li>
	 * @author wuLong
	 * @date 2017年3月25日 下午12:31:07
	 */
	public static LotteryPr getLottery(Integer lotteryChildCode) {
		LotteryEnum.Lottery lottery = LotteryEnum.Lottery.getLottery(lotteryChildCode);
		LotteryPr lott = null;
		switch (lottery) {
			case SSQ://双色球
			case QLC://七乐彩
			case DLT://大乐透
			case PL5://排列5
			case PL3://排列3
			case F3D://福彩3D
			case LHC://六合彩
			case QXC://七星彩
				lott = LotteryPr.SZC;
				break;
			case CQSSC://重庆时时彩
			case JXSSC://江西时时彩
			case XJSSC://新疆时时彩
			case YNSSC://云南时时彩
			case TJSSC://天津时时彩
			case D11X5://广东11选5
			case HB11X5://湖北11选5
			case JS11X5://江苏11选5
			case JX11X5://江西11选5
			case LN11X5://辽宁11选5
			case SD11X5://山东11选5
			case GX11X5://广西11选5
			case HNKL10://湖南快乐10分
			case DKL10://广东快乐10分
			case CQKL10://幸运农场(重庆快乐10分)
			case PK10://北京赛车
			case BJKL8://北京快乐8
			case GXK3://广西快3
			case AHK3://安徽块3
			case JLK3://吉林块3
			case JSK3://江苏快3
			case SHSSL://上海时时
			case POKER://山东快乐扑克3
			case JXK3://江西快3
			case XJ11X5://新疆11选5
				lott = LotteryPr.GPC;
				break;
			case FB://竞技彩足彩
			case BB://竞技彩足彩
				lott = LotteryPr.JJC;
				break;
			case JQ4://四场进球彩
			case SFC://十四场胜负彩
			case ZC_NINE://九场胜负彩
			case ZC6://六场半全场
				lott = LotteryPr.ZC;
				break;
			case BJDC://北京单场
				lott = LotteryPr.BJDC;
				break;
			case SFGG://胜负过关
				lott = LotteryPr.SFGG;
				break;
            case GJJC://冠军竞猜
            case GYJJC://冠亚军竞猜
                lott = LotteryPr.GYJJC;
                break;
            default:

				break;
		}
		return lott;
	}
	public enum Lottery {
		// 低频彩
		/**
		 * 双色球
		 */
		SSQ(100,"双色球"),
		/**
		 * 七乐彩
		 */
		QLC(101,"七乐彩"),
		/**
		 * 大乐透
		 */
		DLT(102,"大乐透"),

		/**
		 * 排列5
		 */
		PL5(103,"排列5"),
		/**
		 * 排列3
		 */
		PL3(104,"排列3"),
		/**
		 * 福彩3D
		 */
		F3D(105,"福彩3D"),
		/**
		 * 六合彩
		 */
		LHC(106,"六合彩"),
		/**
		 * 七星彩
		 */
		QXC(107,"七星彩"),
		// 高频

		/**
		 * 重庆时时彩
		 */
		CQSSC(201,"重庆时时彩"),
		/**
		 * 江西时时彩
		 */
		JXSSC(202,"江西时时彩"),
		/**
		 * 新疆时时彩
		 */
		XJSSC(203,"新疆时时彩"),
		/**
		 * 云南时时彩
		 */
		YNSSC(204,"云南时时彩"),
		/**
		 * 天津时时彩
		 */
		TJSSC(205,"天津时时彩"),
		/**
		 * 广东11选5
		 */
		D11X5(210,"广东11选5"),
		/**
		 * 湖北11选5
		 */
		HB11X5(211,"湖北11选5"),
		/**
		 * 江苏11选5
		 */
		JS11X5(212,"江苏11选5"),
		/**
		 * 江西11选5
		 */
		JX11X5(213,"江西11选5"),
		/**
		 * 辽宁11选5
		 */
		LN11X5(214,"辽宁11选5"),
		/**
		 * 山东11选5
		 */
		SD11X5(215,"山东11选5"),
		/**
		 * 新疆11选5
		 */
		XJ11X5(273,"新疆11选5"),
		/**
		 * 广西11选5
		 */
		GX11X5(271,"广西11选5"),
		/**
		 * 湖南快乐10分
		 */
		HNKL10(220,"湖南快乐10分"),
		/**
		 * 广东快乐10分
		 */
		DKL10(221,"广东快乐10分"),
		/**
		 * 幸运农场(重庆快乐10分)
		 */
		CQKL10(222,"幸运农场(重庆快乐10分)"),
		/**
		 * 北京赛车
		 */
		PK10(223,"北京赛车"),
		/**
		 * 北京快乐8
		 */
		BJKL8(224,"北京快乐8"),
		/**
		 * 广西快3
		 */
		GXK3(230,"广西快3"),
		/**
		 * 安徽块3
		 */
		AHK3(231,"安徽块3"),
		/**
		 * 吉林块3
		 */
		JLK3(232,"吉林块3"),
		/**
		 * 江苏快3
		 */
		JSK3(233,"江苏快3"),
		/**
		 * 江西快3
		 */
		JXK3(234,"江西快3"),	
		/**
		 * 上海时时乐
		 */
		SHSSL(240,"上海时时乐"),
		/**
		 * 山东快乐扑克3
		 */
		POKER(225,"山东快乐扑克3"),
		// 竞技彩
		/**
		 * 竞技彩足彩
		 */
		FB(300,"竞技彩足彩"),
		/**
		 * 竞彩篮球
		 */
		BB(301,"竞彩篮球"),
		/**
		 * 足彩:六场半全场
		 */
		ZC6(302,"六场半全场"),
		/**
		 * 足彩:四场进球彩
		 */
		JQ4(303,"四场进球彩"),
		/**
		 * 足彩:十四场胜负彩
		 */
		SFC(304,"十四场胜负彩"),
		/**
		 * 足彩:九场胜负彩
		 */
		ZC_NINE(305,"九场胜负彩"),
		/**
		 * 北京单场
		 */
		BJDC(306,"北京单场"),
		/**
		 * 胜负过关
		 */
		SFGG(307,"胜负过关"),
        /**
         * 冠军竞猜
         */
        GJJC(308, "冠军竞猜"),
        /**
         * 冠亚军竞猜
         */
		GYJJC(309, "冠亚军竞猜");

		private int name;

		private String desc;

		Lottery(int name, String desc) {
			this.name = name;
			this.desc  = desc;
		}

        /**
		 * @param lottery
		 * @return true/false
		 * @Desc 是否包含指定彩种
		 */
		public static boolean contain(Integer lotteryCode) {
			if (lotteryCode == null) {
				return false;
			}
			for (Lottery l : Lottery.values()) {
				if (Objects.equals(l.getName(), lotteryCode)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * @param lottery
		 *            彩种名
		 * @return 返回指定彩种结构
		 */
		public static Lottery getLottery(Integer lotteryCode) {
		    if (lotteryCode == null) {
                return null;
            }
			for (Lottery l : Lottery.values()){
				if (Objects.equals(l.getName(), lotteryCode)) {
					return l;
				}
			}
			return null;
		}

		/**
		 * 是否为十一选五
		 *
		 * @param lottery
		 * @return
		 */
		public static boolean is11X5(Integer lotteryCode) {
		    if (lotteryCode == null) {
				return false;
			}
			return Objects.equals(D11X5.getName(), lotteryCode) ||
					Objects.equals(HB11X5.getName(), lotteryCode) ||
					Objects.equals(JS11X5.getName(), lotteryCode) ||
					Objects.equals(JX11X5.getName(), lotteryCode) ||
					Objects.equals(LN11X5.getName(), lotteryCode) ||
					Objects.equals(SD11X5.getName(), lotteryCode);
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public int getName() {
			return name;
		}

		public void setName(int name) {
			this.name = name;
		}
	}

	/**
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2016-12-29 下午5:46:44
	 * @Desc 彩种类型
	 */
	public enum LotteryCategory {
		NUM("数字彩", 1), HIGH("高频彩", 2), SPORT("竞技彩", 3), ;
		/**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private int value;

		LotteryCategory(String desc, int value) {
			this.desc = desc;
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
		/**
		 * 检查类型是否存在
		 * @param ariValue
		 * @return
		 */
		public static boolean contain(int ariValue) {
			for (LotteryCategory l : LotteryCategory.values()) {
				if (ariValue==l.getValue()) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2016-12-5 上午10:59:48
	 * @Desc 是否是当前期
	 */
	public enum ConIssue {
		CURRENT("当前期", (short) 1), NOT_CURRENT("不是当前期", (short) 0), ;
		/**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private Short value;

		ConIssue(String desc, Short value) {
			this.desc = desc;
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public Short getValue() {
			return value;
		}

		public void setValue(Short value) {
			this.value = value;
		}

	}

	/**
	 * @desc 是否同步彩期
	 * @author jiangwei
	 * @date 2017-1-20
	 * @company 益彩网络科技公司
	 * @version 1.0
	 */
	public enum SynIssue {
		SYN("同步彩期", (short) 1), NO_SYN("不同步彩期", (short) 0), ;
		/**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private Short value;

		SynIssue(String desc, Short value) {
			this.desc = desc;
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public Short getValue() {
			return value;
		}

		public void setValue(Short value) {
			this.value = value;
		}

	}

	/**
	 * @author huangb
	 *
	 * @Date 2016年12月1日
	 *
	 * @Desc 彩期销售状态
	 */
	public enum LotIssueSaleStatus {

		SUSPEND_SALE("暂停销售", (short) 0),
		NOT_SALE("未开售", (short) 1),
		PRE_SALING("预售中", (short) 2),
		SALING("销售中", (short) 3),
		STOP_SALE("销售截止",(short) 4),
		APPROVED("已审核", (short) 7),
		OPEN_AWARD("已开奖", (short) 5),
		SEND_PRIZE("已派奖",(short) 6);
		/**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private short value;

		LotIssueSaleStatus(String desc, short value) {
			this.desc = desc;
			this.value = value;
		}

		/**
		 * 是否正常销售
		 *
		 * @param value
		 * @return
		 */
		public static boolean isSale(short value) {
			return PRE_SALING.getValue() == value || SALING.getValue() == value;
		}

		/**
		 * 是否正常销售
		 *
		 * @param value
		 * @return
		 */
		public static boolean isSalable(Short value) {
			if(value == null) return false;
			return  Objects.equals(PRE_SALING.getValue(), value) || Objects.equals(SALING.getValue(), value);
		}

		/**
		 * 获取状态
		 * @author jiangwei
		 * @Version 1.0
		 * @CreatDate 2017年3月8日 下午6:10:52
		 * @param status
		 * @return
		 */
		public static LotIssueSaleStatus getLotIssueSaleStatus(int status) {
			for (LotIssueSaleStatus l : LotIssueSaleStatus.values()){
				if (l.getValue() == status) {
					return l;
				}
			}
			return null;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public short getValue() {
			return value;
		}

		public void setValue(short value) {
			this.value = value;
		}
	}

	/**
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2016-12-29 下午5:46:44
	 * @Desc 彩种类型
	 */
	public enum AdminCategory {
		BLESS_LOTTERY("福彩", 1), SPROT_LOTTERY("体彩", 2), ;
		/**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private int value;

		AdminCategory(String desc, int value) {
			this.desc = desc;
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}

	/**
	 * @author huangb
	 *
	 * @Date 2016年12月1日
	 *
	 * @Desc 销售状态（包括彩种销售状态，子玩法销售状态等）
	 */
	public enum SaleStatus {

		SUSPEND_SALE("暂停销售", (short) 0), NORMAL_SALE("正常销售", (short) 1);

		/**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private short value;

		SaleStatus(String desc, short value) {
			this.desc = desc;
			this.value = value;
		}

		/**
		 * @param value
		 * @return
		 * @Desc 是否包含指定状态
		 */
		public static boolean contain(short value) {
			for (SaleStatus temp : SaleStatus.values()) {
				if (temp.getValue() == value) {
					return true;
				}
			}
			return false;
		}

		/**
		 * 是否正常销售
		 *
		 * @param value
		 * @return
		 */
		public static boolean isSalable(Short value) {
			return Objects.equals(NORMAL_SALE.getValue(), value);
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public short getValue() {
			return value;
		}

		public void setValue(short value) {
			this.value = value;
		}
	}
	
	/**
	 * @desc 彩种限号方式
	 * @author huangb
	 * @date 2017年2月16日
	 * @company 益彩网络
	 * @version v1.0
	 */
	public enum LotteryLimitWay {
		LIMIT_ISSUE("限号彩期", (short) 1), LIMIT_TIME("限号时间", (short) 2);
		/**
		 * 描述
		 */
		private String desc;
		/**
		 * 值
		 */
		private short value;

		LotteryLimitWay(String desc, short value) {
			this.desc = desc;
			this.value = value;
		}

		/**
		 * @desc 是否包含指定方式
		 * @author huangb
		 * @date 2017年2月16日
		 * @param value
		 * @return
		 */
		public static boolean contain(short value) {
			for (LotteryLimitWay temp : LotteryLimitWay.values()) {
				if (temp.getValue() == value) {
					return true;
				}
			}
			return false;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public short getValue() {
			return value;
		}

		public void setValue(short value) {
			this.value = value;
		}
	}
	
	/**
	 * @desc 彩种限号状态
	 * @author huangb
	 * @date 2017年2月16日
	 * @company 益彩网络
	 * @version v1.0
	 */
	public enum LotteryLimitStatus {

		ENABLE("启用", (short) 1), DISABLED("禁用", (short) 2), EXPIRED("过期", (short) 3);
		/**
		 * 描述
		 */
		private String desc;
		/**
		 * 值
		 */
		private short value;

		LotteryLimitStatus(String desc, short value) {
			this.desc = desc;
			this.value = value;
		}

		/**
		 * @desc 是否包含指定状态
		 * @author huangb
		 * @date 2017年2月16日
		 * @param value
		 * @return
		 */
		public static boolean contain(Short value) {
			if (value == null) {
				return false;
			}
			for (LotteryLimitStatus temp : LotteryLimitStatus.values()) {
				if (Objects.equals(temp.getValue(), value)) {
					return true;
				}
			}
			return false;
		}

		public static LotteryLimitStatus getStatus(Short value) {
			if (value == null) {
				return null;
			}
			for (LotteryLimitStatus temp : LotteryLimitStatus.values()) {
				if (Objects.equals(temp.getValue(), value)) {
					return temp;
				}
			}
			return null;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public short getValue() {
			return value;
		}

		public void setValue(short value) {
			this.value = value;
		}
	}
	
	/**
	 * 大彩种具体枚举分类
	 * @author longguoyou
	 * @date 2017年4月8日
	 * @compay 益彩网络科技有限公司
	 */
	public enum LotteryPr{
		GPC("高频彩"),//高频彩
		SZC("数字彩"),//数字彩
		BJDC("北单"),//北单
		JJC("竞技彩"),//竞技彩
        SFGG("胜负过关"),//胜负过关
        GYJJC("冠亚军竞猜"),//冠亚军竞猜
        ZC("足彩");//足彩
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		} 
		
		private LotteryPr(String name) {
			this.name = name;
		}
	}
	/**
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2016-12-29 下午5:46:44
	 * @Desc 彩种类型
	 */
	public enum TeamType {
		FOOT_BALL("足球", 1), BASKET_BALL("篮球", 2), ;
		/**
		 * 状态描述
		 */
		private String desc;
		/**
		 * 状态值
		 */
		private int value;

		TeamType(String desc, int value) {
			this.desc = desc;
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
}
