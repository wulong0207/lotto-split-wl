package com.hhly.lottosplit.exception;

/**
 * @author wuLong
 * @CreatDate 2017-05-18 下午3:27:58
 * @Desc 服务异常
 */
public class ServiceRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -755444734708675361L;

	public ServiceRuntimeException(String msg) {
		super(msg);
		this.code = "";
		this.msg = msg;
	}

	public ServiceRuntimeException(String code, String msg) {
		super(code + "----" + msg);
		this.code = code;
		this.msg = msg;
	}

	public ServiceRuntimeException(String msg, Throwable t) {
		super(msg, t);
		this.code = "";
		this.msg = msg;
	}

	public ServiceRuntimeException(String code, String msg, Throwable t) {
		super(code + "----" + msg, t);
		this.code = code;
		this.msg = msg;
	}
	

	private String code;

	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}


}
