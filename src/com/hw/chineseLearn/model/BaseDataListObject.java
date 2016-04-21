package com.hw.chineseLearn.model;

import java.util.List;

/**
 * 基类List
 * 
 * @author
 */
public class BaseDataListObject extends BaseObject {
	private boolean success;
	private String message;
	private int error_code;

	public BaseDataListObject() {
		super();
	}

	public BaseDataListObject(boolean success, String message, int error_code) {
		super();
		this.success = success;
		this.message = message;
		this.error_code = error_code;
	}

	public BaseDataListObject(boolean success, String message, int error_code,
			List<BaseObject> data) {
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
