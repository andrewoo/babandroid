package com.hw.chineseLearn.model;


/**
 * 基类
 * @author lp
 */
public class BaseDataObject extends BaseObject{
	private String statusCode;//
	private String status;//
	private String message;//
	private BaseObject map;//
	private BaseObject page;//
	private BaseObject queryBean;//
	
//	private BaseObject data;
	public BaseDataObject() {
		super();
	}
	public BaseDataObject(String statusCode, String status, String message,
			BaseObject map, BaseObject page, BaseObject queryBean) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
		this.map = map;
		this.page = page;
		this.queryBean = queryBean;
	}
	public BaseDataObject(String statusCode, String status, String message,
			BaseObject map, BaseObject page, BaseObject queryBean, BaseObject data) {
		super();
		this.statusCode = statusCode;
		this.status = status;
		this.message = message;
		this.map = map;
		this.page = page;
		this.queryBean = queryBean;
//		this.data = data;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BaseObject getMap() {
		return map;
	}
	public void setMap(BaseObject map) {
		this.map = map;
	}
	public BaseObject getPage() {
		return page;
	}
	public void setPage(BaseObject page) {
		this.page = page;
	}
	public BaseObject getQueryBean() {
		return queryBean;
	}
	public void setQueryBean(BaseObject queryBean) {
		this.queryBean = queryBean;
	}
//	public BaseObject getData() {
//		return data;
//	}
//	public void setData(BaseObject data) {
//		this.data = data;
//	}
}