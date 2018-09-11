/**
 * 
 */
package com.hhly.lottosplit.enums;

/**
 * @desc 票枚举类
 * @author Bruce
 * @date 2017年1月19日
 * @company 益彩网络科技公司
 * @version 1.0
 */
public class TicketEnum {
	/**
	 * @desc 票状态
	 * @author jiangwei
	 * @date 2017年3月14日
	 * @company 益彩网络科技公司
	 * @version 1.0
	 */
	public enum TicketStatus{
		/**
		 * 出票失败
		 */
		OUT_FAIL(-2),
		/**
		 * 送票失败
		 */
		SEND_FAIL(-1),
		/**
		 * 不出票
		 */
		NO_OUT_TICKET(0),
		/**
		 * 等待分配
		 */
		WAITING_ALLOT(1),
		/**
		 * 已分配
		 */
		ALLOT(2),
		/**
		 * 已送票
		 */
		SEND_TICKET(3),
		/**
		 * 已出票
		 */
		OUT_TICKET(4)
		;
	    private int value;
	    TicketStatus(int value){
	    	this.value = value;
	    }
		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
    
}
