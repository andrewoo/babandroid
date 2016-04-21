package com.hw.chineseLearn.model;


public class FluentModel  {
	
	private boolean success;
	private String message;
	private int error_code;
	private FluentListModel results;
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
	public FluentListModel getResults() {
		return results;
	}
	public void setResults(FluentListModel results) {
		this.results = results;
	}

	
}