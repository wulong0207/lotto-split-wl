package com.hhly.lottosplit.enums;

import java.util.Objects;

/**
 * @author huangb
 *
 * @Date 2016年12月1日
 *
 * @Desc 彩种子玩法类型
 */
public class LotteryChildEnum {

	public enum LotteryChild {
		SSQ_PT(10001, "普通投注"), SSQ_DT(10002, "胆拖投注"),
		DLT_PT(10201, "普通投注"), DLT_DT(10202, "胆拖投注"),
		/**
		 * 山东十一选五子玩法
		 */
		// 山东十一选五任选二
		SD11X5_R2(21502,"山东十一选五任选二"),
		// 山东十一选五任选三
		SD11X5_R3(21503,"山东十一选五任选三"),
		// 山东十一选五任选四
		SD11X5_R4(21504,"山东十一选五任选四"),
		// 山东十一选五任选五
		SD11X5_R5(21505,"山东十一选五任选五"),
		// 山东十一选五任选六
		SD11X5_R6(21506,"山东十一选五任选六"),
		// 山东十一选五任选七
		SD11X5_R7(21507,"山东十一选五任选七"),
		// 山东十一选五任选八
		SD11X5_R8(21508,"山东十一选五任选八"),
		// 山东十一选五前一
		SD11X5_Q1(21509,"山东十一选五前一"),
		// 山东十一选五前二组选
		SD11X5_Q2_GROUP(21510,"山东十一选五前二组选"),
		// 山东十一选五前二直选
		SD11X5_Q2_DIRECT(21511,"山东十一选五前二直选"),
		// 山东十一选五前三组选
		SD11X5_Q3_GROUP(21512,"山东十一选五前三组选"),
		// 山东十一选五前三直选
		SD11X5_Q3_DIRECT(21513,"山东十一选五前三直选"),
		// 山东十一选五 乐二
		SD11X5_L2(21514,"山东十一选五乐二"),
		// 山东十一选五 乐三
		SD11X5_L3(21515,"山东十一选五乐三"),
		// 山东十一选五 乐四
		SD11X5_L4(21516,"山东十一选五乐四"),
		// 山东十一选五 乐五
		SD11X5_L5(21517,"山东十一选五乐五"),
		
		/**
		 * 广东十一选五子玩法
		 */
		D11X5_R2(21002,"广东十一选五任选二"),
		D11X5_R3(21003,"广东十一选五任选三"),
		D11X5_R4(21004,"广东十一选五任选四"),
		D11X5_R5(21005,"广东十一选五任选五"),
		D11X5_R6(21006,"广东十一选五任选六"),
		D11X5_R7(21007,"广东十一选五任选七"),
		D11X5_R8(21008,"广东十一选五任选八"),
		D11X5_Q1(21009,"广东十一选五前一"),
		D11X5_Q2_GROUP(21010,"广东十一选五前二组选"),
		D11X5_Q2_DIRECT(21011,"广东十一选五前二直选"),
		D11X5_Q3_GROUP(21012,"广东十一选五前三组选"),
		D11X5_Q3_DIRECT(21013,"广东十一选五前三直选"),
		
		
		/**
		 * 江西十一选五子玩法
		 */
		JX11X5_R2(21302,"江西十一选五任选二"),
		JX11X5_R3(21303,"江西十一选五任选三"),
		JX11X5_R4(21304,"江西十一选五任选四"),
		JX11X5_R5(21305,"江西十一选五任选五"),
		JX11X5_R6(21306,"江西十一选五任选六"),
		JX11X5_R7(21307,"江西十一选五任选七"),
		JX11X5_R8(21308,"江西十一选五任选八"),
		JX11X5_Q1(21309,"江西十一选五前一"),
		JX11X5_Q2_GROUP(21310,"江西十一选五前二组选"),
		JX11X5_Q2_DIRECT(21311,"江西十一选五前二直选"),
		JX11X5_Q3_GROUP(21312,"江西十一选五前三组选"),
		JX11X5_Q3_DIRECT(21313,"江西十一选五前三直选"),
		
		
		/**
		 * 新疆十一选五子玩法
		 */
		XJ11X5_R2(27302,"新疆十一选五任选二"),
		XJ11X5_R3(27303,"新疆十一选五任选三"),
		XJ11X5_R4(27304,"新疆十一选五任选四"),
		XJ11X5_R5(27305,"新疆十一选五任选五"),
		XJ11X5_R6(27306,"新疆十一选五任选六"),
		XJ11X5_R7(27307,"新疆十一选五任选七"),
		XJ11X5_R8(27308,"新疆十一选五任选八"),
		XJ11X5_Q1(27309,"新疆十一选五前一"),
		XJ11X5_Q2_GROUP(27310,"新疆十一选五前二组选"),
		XJ11X5_Q2_DIRECT(27311,"新疆十一选五前二直选"),
		XJ11X5_Q3_GROUP(27312,"新疆十一选五前三组选"),
		XJ11X5_Q3_DIRECT(27313,"新疆十一选五前三直选"),
		XJ11X5_L2(27314,"新疆十一选五乐二"),
		XJ11X5_L3(27315,"新疆十一选五乐三"),
		XJ11X5_L4(27316,"新疆十一选五乐四"),
		XJ11X5_L5(27317,"新疆十一选五乐五"),
		
		/**
		 * 广西十一选五子玩法
		 */
		GX11X5_R2(27102,"广西十一选五任选二"),
		GX11X5_R3(27103,"广西十一选五任选三"),
		GX11X5_R4(27104,"广西十一选五任选四"),
		GX11X5_R5(27105,"广西十一选五任选五"),
		GX11X5_R6(27106,"广西十一选五任选六"),
		GX11X5_R7(27107,"广西十一选五任选七"),
		GX11X5_R8(27108,"广西十一选五任选八"),
		GX11X5_Q1(27109,"广西十一选五前一"),
		GX11X5_Q2_GROUP(27110,"广西十一选五前二组选"),
		GX11X5_Q2_DIRECT(27111,"广西十一选五前二直选"),
		GX11X5_Q3_GROUP(27112,"广西十一选五前三组选"),
		GX11X5_Q3_DIRECT(27113,"广西十一选五前三直选"),
	    GX11X5_L2(27114,"广西十一选五乐二"),
	    GX11X5_L3(27115,"广西十一选五乐三"),
	    GX11X5_L4(27116,"广西十一选五乐四"),
	    GX11X5_L5(27117,"广西十一选五乐五"),
		
		
		/*
		 * 江苏快3
		 */
		/**和值*/
		JSK3_S(23301,"和值"),
		/**二同号单选*/
		JSK3_TD2(23302,"二同号单选"),
		/**二同号复选*/
		JSK3_TF2(23303,"二同号复选"),
		/**二不同号*/
		JSK3_BT2(23304,"二不同号"),
		/**三不同号*/
		JSK3_BT3(23305,"三不同号"),
		/**三同号单选*/
		JSK3_TD3(23306,"三同号单选"),
		/**三同号通选*/
		JSK3_TT3(23307,"三同号通选"),
		/**三连号通选*/
		JSK3_L3(23308,"三连号通选"),
		/**
		 * 一码包中
		 * 一码包中时，传到后台都是拆分好的单式
		 * */
		//JSK3_BZ1(23309),
		
		/**
		 * 江西快3
		 */
		/**和值*/
		JXK3_S(23401,"和值"),
		/**二同号单选*/
		JXK3_TD2(23402,"二同号单选"),
		/**二同号复选*/
		JXK3_TF2(23403,"二同号复选"),
		/**二不同号*/
		JXK3_BT2(23404,"二不同号"),
		/**三不同号*/
		JXK3_BT3(23405,"三不同号"),
		/**三同号单选*/
		JXK3_TD3(23406,"三同号单选"),
		/**三同号通选*/
		JXK3_TT3(23407,"三同号通选"),
		/**三连号通选*/
		JXK3_L3(23408,"三连号通选"),
		
		/**
		 * 山东快乐扑克3
		 */
		/**山东快乐扑克3任1**/
		POKER_R1(22501,"山东快乐扑克3任1"),
		/**山东快乐扑克3任2**/
		POKER_R2(22502,"山东快乐扑克3任2"),
		/**山东快乐扑克3任3**/
		POKER_R3(22503,"山东快乐扑克3任3"),
		/**山东快乐扑克3任4**/
		POKER_R4(22504,"山东快乐扑克3任4"),
		/**山东快乐扑克3任5**/
		POKER_R5(22505,"山东快乐扑克3任5"),
		/**山东快乐扑克3任6**/
		POKER_R6(22506,"山东快乐扑克3任6"),
		/**山东快乐扑克3同花**/
		POKER_TH(22507,"山东快乐扑克3同花"),
		/**山东快乐扑克3顺子**/
		POKER_SZ(22508,"山东快乐扑克3顺子"),
		/**山东快乐扑克3对子**/
		POKER_DZ(22509,"山东快乐扑克3对子"),
		/**山东快乐扑克3豹子**/
		POKER_BZ(22510,"山东快乐扑克3豹子"),
		/**山东快乐扑克3同花顺**/
		POKER_THS(22511,"山东快乐扑克3同花顺"),
		
		/**
		 * 重庆时时彩
		 */
		CQSSC_5(20101, "重庆时时彩五星直选"),
		CQSSC_5T(20102, "重庆时时彩五星通选"),
		CQSSC_3(20103, "重庆时时彩三星直选"),
		CQSSC_3Z3(20104, "重庆时时彩三星组三"),
		CQSSC_3Z6(20105, "重庆时时彩三星组六"),
		CQSSC_2(20106, "重庆时时彩二星直选"),
		CQSSC_2Z(20107, "重庆时时彩二星组选"),
		CQSSC_1(20108, "重庆时时彩一星"),
		CQSSC_DXDS(20109, "重庆时时彩大小单双"),
		
		/**
		 * 广东快乐10分
		 */
		DKL10_ST(22101,"前一数投"),
		DKL10_HT(22102,"前一红投"),
		DKL10_R2(22103,"任选二"),
		DKL10_G2(22104,"选二连组"),
		DKL10_D2(22105,"选二连直"),
		DKL10_R3(22106,"任选三"),
		DKL10_G3(22107,"选三前组"),
		DKL10_D3(22108,"选三前直"),
		DKL10_R4(22109,"任选四"),
		DKL10_R5(22110,"任选五"),
		
		/**
		 * 幸运农场(重庆快乐10分)
		 */
		CQKL10_ST(22201,"前一数投"),
		CQKL10_HT(22202,"前一红投"),
		CQKL10_R2(22203,"任选二"),
		CQKL10_G2(22204,"选二连组"),
		CQKL10_D2(22205,"选二连直"),
		CQKL10_R3(22206,"任选三"),
		CQKL10_G3(22207,"选三前组"),
		CQKL10_D3(22208,"选三前直"),
		CQKL10_R4(22209,"任选四"),
		CQKL10_R5(22210,"任选五"),
		
		
		/** 七星彩 */
		QXC_PT(10701, "普通投注"),
		
		/**福彩3D 直选 */
		D_DIRECT(10501),
		/**福彩3D 组三 */
		D_G3(10502),
		/**福彩3D 组六 */
		D_G6(10503),
		
		/** 排列3 直选 */
		PL3_DIRECT(10401, "直选"),
		/** 排列3 组三 */
		PL3_G3(10402, "组三 "),
		/** 排列3 组六 */
		PL3_G6(10403, "组六"),

		/** 排列5 直选 */
		PL5_DIRECT(10301, "直选"),
		
		/**
		 * 竞彩足球
		 */
		/**混投*/
		ID_FHT(30001, "竞彩足球混投"),
		/**胜平负*/
		ID_JCZQ(30002, "竞彩足球胜平负"),
		/**让胜平负*/
		ID_RQS(30003, "竞彩足球让球胜平负"),
		/**比分*/
		ID_FBF(30004, "竞彩足球比分"),
		/**总进球*/
		ID_FZJQ(30005, "竞彩足球总进球"),
		/**半全场*/
		ID_FBCQ(30006, "竞彩足球半全场"),
		
        /**
         * 竞彩篮球
         */
		/*竞彩篮球胜负*/
		ID_JCLQ_SF(30101, "竞彩篮球胜负"),
		/*竞彩篮球让分*/
		ID_JCLQ_RF(30102, "竞彩篮球让分胜负"),
		/*竞彩篮球大小分*/
		ID_JCLQ_DXF(30103, "竞彩篮球大小分"),
		/*竞彩篮球胜分差*/
		ID_JCLQ_SFC(30104, "竞彩篮球胜分差"),
		/**混投*/
		ID_JCLQ_HHGG(30105, "竞彩篮球混投"),

		/***老足彩9场胜平负普通投注*/
		ID_NINE_BET(30501, "9场胜平负普通投注"),
		/***老足彩9场胜平负胆拖投注*/
		ID_NINE_BANKERS_BET(30502, "9场胜平负胆拖投注"),

		/**
		 * 北单
		 */
		/**   让球胜平负*/
		ID_BD_RQS(30601, "北单让球胜平负"),
		/** 上下单双 */
		ID_BD_SXDX(30602, "北单上下单双"),
		/**   总进球 */
		ID_BD_FZJQ(30603, "北单总进球"),
		/**   比分 */
		ID_BD_FBF(30604, "北单比分"),
		/**   半全场 */
		ID_BD_FBCQ(30605, "北单半全场"),
		
		/**十四场*/
        ID_FOURTEEN(30401, "十四场普通投注"),
        /**
         * 冠军竞猜
         */
        ID_GJJC(30801, "冠亚军游戏冠军竞猜普通投注"),
        /**
         * 冠亚军竞猜
         */
        ID_GYJJC(30901, "冠亚军游戏冠亚军竞猜普通投注");

        /**
		 * 类型值
		 */
		private int value;

		/**
		 * 描述
		 */
		private String desc;

		LotteryChild(int value) {
			this.value = value;
		}

		LotteryChild(int value, String desc) {
			this(value);
			this.desc = desc;
		}

		public int getValue() {
			return value;
		}

		/**
		 * @param value
		 *            类型值
		 * @return true/false
		 * @Desc 是否包含指定类型
		 */
		public static boolean contain(Integer value) {
			if (value == null) {
				return false;
			}
			for (LotteryChild temp : LotteryChild.values()) {
				if (Objects.equals(temp.getValue(), value)) {
					return true;
				}
			}
			return false;
		}
		
		/**
		 * @param value
		 *            类型值
		 * @return true/false
		 * @Desc 是否是双色球玩法
		 */
		public static boolean isSsq(Integer value) {
			if (value == null) {
				return false;
			}
			// ssq-两种子玩法
            return LotteryChild.SSQ_PT.getValue() == value.intValue()
                    || LotteryChild.SSQ_DT.getValue() == value.intValue();
        }

		public static LotteryChild valueOf(Integer value) {
			if (null == value) {
				return null;
			}
			for (LotteryChild child : values()) {
				if (Objects.equals(value, child.getValue())) {
					return child;
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

		public void setValue(int value) {
			this.value = value;
		}
	}
	
	public enum SaleStatus {

		STOP("暂停销售", (short) 0), NORMAL("正常销售", (short) 1),
		END("截止销售",(short) 4);

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
		 * @param value
		 * @return
		 */
		public static boolean isSalable(Short value) {
			return Objects.equals(NORMAL.getValue(), value);
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

}
