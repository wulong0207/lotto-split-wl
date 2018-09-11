package com.hhly.lottosplit.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhly.lottosplit.utils.PropertyUtil;

/**
 * @author huangb
 *
 * @Date 2016年12月1日
 *
 * @Desc 结果对象(用于校验，判断等公共结果的输出)
 */

@SuppressWarnings("serial")
public class ResultBO<T> extends BaseBO {
	/**
	 * 成功
	 */
	private static final int OK = 1;
	/**
	 * 失败
	 */
	private static final int ERR = 0;
	/**
	 * 成功返回code
	 */
	public static final String SUCCESS_CODE = "10001";
	
	public static final String FAIL_CODE = "10002";
	

	/**
	 * 输出状态 1(成功)/0(失败)
	 */
	private int success;
	/**
	 * 错误信息代码
	 */
	private String errorCode;
	/**
	 * 输出消息
	 */
	private String message;
	/**
	 * 输出数据（附件信息）
	 */
	private T data;

	public ResultBO() {}

	/**
	 * 返回正确结果的BO
	 * @param data
     */
	public ResultBO(T data) {
		this(OK, SUCCESS_CODE, data);
	}
	
	/**
	 * @param success
	 * @param errorCode
	 * @param data
	 * @param arguments
	 *            多个参数，配置文件中的占位符
	 */
	public ResultBO(int success, String errorCode, T data, Object... arguments) {
		this.success = success;
		this.errorCode = errorCode;
		this.data = data;
		this.message = PropertyUtil.getConfigValue(errorCode, arguments);
	}
	

	/**
	 * 构建正确结果
	 * 
	 * @return
	 */
	public static ResultBO<?> ok() {
		return new ResultBO<>(OK, SUCCESS_CODE, null,PropertyUtil.getConfigValue(SUCCESS_CODE));
	}

	/**
	 * @desc 构建正确结果
	 * @author huangb
	 * @date 2017年1月21日
	 * @param data
	 *            数据
	 * @return
	 */
	public static <T> ResultBO<T> ok(T data) {
		return new ResultBO<T>(OK, SUCCESS_CODE, data);
	}
	/**
	 * 构建一个错误结果对象
	 * 
	 * @return
	 */
	public static ResultBO<?> err() {
		return new ResultBO<>(ERR, FAIL_CODE,null);
	}

	public static ResultBO<?> err(String errorCode) {
		return new ResultBO<>(ERR, errorCode,null);
	}

	/**
	 * 构建一个错误结果对象
	 * 
	 * @return
	 */
	public static ResultBO<?> err(String errorCode, Object... arguments) {
		return new ResultBO<>(ERR, errorCode, null, arguments);
	}

	/**
	 * @desc 构建一个错误结果对象
	 * @author huangb
	 * @date 2017年1月21日
	 * @param errorCode
	 *            编码
	 * @param data
	 *            数据
	 * @param arguments
	 *            消息参数(数组)，避免与上面一个方法参数冲突
	 * @return
	 */
	public static ResultBO<?> err(String errorCode, Object data, Object[] arguments) {
		if (arguments == null || arguments.length == 0) {
			return new ResultBO<>(ERR, errorCode, data);
		}
		return new ResultBO<>(ERR, errorCode, data, arguments);
	}
    /**
     * 构造错误信息message
     * @author jiangwei
     * @Version 1.0
     * @CreatDate 2017年3月14日 上午10:48:02
     * @param errorCode
     * @param message
     * @return
     */
	public static ResultBO<?> errMessage(String errorCode,String message){
		ResultBO<?> bo = new ResultBO<>();
		bo.setErrorCode(errorCode);
		bo.setMessage(message);
		return bo;
	}
	/**
	 * 获取提示信息
	 *
	 * @author jiangwei
	 * @Version 1.0
	 * @CreatDate 2017-1-21 上午10:41:24
	 * @param errorCode
	 * @param arguments
	 * @return
	 */
	public static String getMsg(String errorCode, Object... arguments) {
		return PropertyUtil.getConfigValue(errorCode, arguments);
	}
	/**
	 * 是否异常 (@JsonIgnore 不要去掉)
	 * @return
	 */
	@JsonIgnore
	public boolean isError() {
		return success != OK;
	}
	
	/**
	 * 是否正常(@JsonIgnore 不要去掉)
	 * @return
	 */
	@JsonIgnore
	public boolean isOK() {
		return success == OK;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public static int getOk() {
		return OK;
	}

	public static int getErr() {
		return ERR;
	}
	
	
	
}
