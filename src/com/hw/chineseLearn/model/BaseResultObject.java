package com.hw.chineseLearn.model;

public class BaseResultObject extends BaseDataObject{
	private String data;

	public BaseResultObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseResultObject(String statusCode, String status, String message,
			BaseObject map, BaseObject page, BaseObject queryBean,
			BaseObject data) {
		super(statusCode, status, message, map, page, queryBean, data);
		// TODO Auto-generated constructor stub
	}

	public BaseResultObject(String statusCode, String status, String message,
			BaseObject map, BaseObject page, BaseObject queryBean) {
		super(statusCode, status, message, map, page, queryBean);
		// TODO Auto-generated constructor stub
	}

	public BaseResultObject(String data) {
		super();
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}