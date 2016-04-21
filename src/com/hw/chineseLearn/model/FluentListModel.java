package com.hw.chineseLearn.model;

import java.util.ArrayList;

public class FluentListModel extends BaseDataObject {
	private ArrayList<FlunetListBaseModel> list;
	/**
	 * 总记录数
	 */
	private int totalCount;

	public FluentListModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param scuess
	 * @param message
	 * @param error_code
	 */
	public FluentListModel(boolean success, String message, int error_code) {
		super(success, message, error_code);
	}


	public ArrayList<FlunetListBaseModel> getList() {
		return list;
	}

	public void setList(ArrayList<FlunetListBaseModel> list) {
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}