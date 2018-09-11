/**
 * 
 */
package com.hhly.lottosplit.enums;

/**
 * @desc 竞技彩枚举类
 * @author Bruce
 * @date 2017年1月19日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class SportEnum {
	   /***
	    * 
	    * @author longguoyou
	   
	    * @date 2017年2月8日 上午10:27:23
	   
	    * @desc  竞彩足球玩法枚举
	    *
	    */
       public enum SportFbSubWay{
	    	/**胜平负：S */
	    	JCZQ_S("S"),
	    	/**让球胜平负：R */
	    	JCZQ_R("R"),
	    	/**全场比分：Q */
	    	JCZQ_Q("Q"),
	    	/**总进球数：Z*/ 
	    	JCZQ_Z("Z"),
	    	/**半全场胜平负：B */
	    	JCZQ_B("B"),
	    	/**混合*/
	    	JCZQ_M("M");

		   private String value;

		   SportFbSubWay(String _value) {
			   this.value = _value;
	    	}

		   public static SportFbSubWay getSportFbSubWay(String value) {
			   for (SportFbSubWay bbSubWay : SportFbSubWay.values()) {
				   if (bbSubWay.value.equals(value)) {
					   return bbSubWay;
				   }
			   }
			   return null;
		   }

			public String getValue() {
				return value;
			}

		   public void setValue(String value) {
				this.value = value;
			}
       }

	/***
	 *
	 * @author yuanshangbing
	 * @date 2017年2月16日 上午10:27:23
	 * @desc  竞彩篮球玩法枚举
	 * S:胜负，R-让分胜负，D-大小分，C-胜分差，M-混合
	 */
	public enum SportBBSubWay{
		/**胜负：S */
		JCLQ_S("S"),
		/**让分胜负：R */
		JCLQ_R("R"),
		/**大小分：D */
		JCLQ_D("D"),
		/**胜分差：C*/
		JCLQ_C("C"),
		/**混合*/
		JCLQ_M("M");

		private String value;

		SportBBSubWay(String _value) {
			this.value = _value;
		}

		public static SportBBSubWay getSportBBSubWay(String value) {
			for (SportBBSubWay bbSubWay : SportBBSubWay.values()) {
				if (bbSubWay.value.equals(value)) {
					return bbSubWay;
				}
			}
			return null;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	/**
	 * 竞彩过分方式enum
	 */
	public enum SportPassTypeEnum {
		/**
		 * 仅售过关
		 */
		PASS(0),
		/**
		 * 仅售单关
		 */
		SINGLE(1);
		private Integer value;

		SportPassTypeEnum(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	}

	/**
	 * 销售状态enum
	 */
	public enum SportSaleStatusEnum {
		/**
		 * 正常销售
		 */
		NORMAL_SALE(1),
		/**
		 * 仅售过关
		 */
		PASS_SALE(2),
		/**
		 *仅售单关
		 */
		SINGLE_SALE(3),
		/**
		 * 暂停销售
		 */
		STOP_SALE(4);

		private Integer value;

		SportSaleStatusEnum(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}
	}

	/**
	 * 竞技彩玩法
	 */
	public enum SportPayFlagEnum {
		/**
		 * 足球进球
		 */
		GOAL("goal"),
		/**
		 * 足球比分
		 */
		SCORE("score"),
		/**
		 * 足球胜平负
		 */
		WDF("wdf"),
		/**
		 * 足球让分胜平负
		 */
		LET_WDF("let_wdf"),
		/**
		 * 足球半全场
		 */
		HF("hf"),
		/**
		 * 足球混投
		 */
		MI("mi"),
		/**
		 * 篮球胜负
		 */
		WF("wf"),
		/**
		 * 篮球让分胜负
		 */
		LET_WF("let_wf"),
		/**
		 * 篮球比分差
		 */
		WS("ws"),
		/**
		 * 大小分
		 */
		BS("bs");

		private String value;

		SportPayFlagEnum(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * Created by lgs on 2017/1/9.
	 * 比赛类别枚举
	 */
	public enum MatchTypeEnum {

		FOOTBALL(1, "足球赛事"),
		BASKETBALL(2, "篮球赛事");

		private int code;

		private String name;

		MatchTypeEnum(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 胜负类别。例如让球非让球
	 */
	public enum WfTypeEnum {
		/**
		 * 非让球
		 */
		NOT_LET((short) 1),
		/**
		 * 让球
		 */
		LET((short) 2);
		private Short value;

		WfTypeEnum(Short value) {
			this.value = value;
		}

		public Short getValue() {
			return value;
		}

		public void setValue(Short value) {
			this.value = value;
		}
	}

	/**
	 * 比赛状态
	 */
	public enum JcMatchStatusEnum {
		TENTATIVE(6, "暂定赛程"),
		NOT_SALE(7, "未开售"),
		SALE(9, "已开售"),
		SALE_EN(9, "Selling"),
		GOING(12, "进行中");

		private int code;

		private String name;

		JcMatchStatusEnum(int code, String name) {
			this.code = code;
			this.name = name;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	/**竞技彩页面TAB标签分类
	 * @author longguoyou
	 * @date 2017年4月12日
	 * @compay 益彩网络科技有限公司
	 */
	public enum SportTabTypeEnum{
		/**
		 * 过关投注
		 */
		PASS_WAY_BET(1),
		/**
		 * 单关
		 */
		SINGLE_BET(2),
		/**
		 * 2选1
		 */
		TWO_AND_ONE(3),
		/**
		 * 单场致胜
		 */
		SINGLE_WIN(4)
		;
		private int code;
		SportTabTypeEnum(int code){
			this.code = code;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
	}
}
