package com.hw.chineseLearn.model;

/**
 * 基类
 * 
 * @author lp
 */
public class BaseDataObject extends BaseObject {
	private boolean success;
	private String message;
	private int error_code;

	public BaseDataObject() {
		super();
	}

	public BaseDataObject(boolean success, String message, int error_code) {
		super();
		this.success = success;
		this.message = message;
		this.error_code = error_code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
}