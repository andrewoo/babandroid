package com.hw.chineseLearn.model;

import java.util.HashMap;

import android.content.Context;

import com.hw.chineseLearn.interfaces.BaseParser;

public class RequestVo {
	public Boolean showDlg = true;
	public int requestUrl;
	public String fullUrl;
	public Context context;
	public Boolean useFullUrl = false;

	public Boolean useFullFilePath = false;
	public HashMap<String, String> requestDataMap;
	public HashMap<String, String> requestFileMap;
	public BaseParser<?> jsonParser;

	public RequestVo() {
	}

	public RequestVo(int requestUrl, Context context,
			HashMap<String, String> requestDataMap, BaseParser<?> jsonParser) {
		super();
		this.requestUrl = requestUrl;
		this.context = context;
		this.requestDataMap = requestDataMap;
		this.jsonParser = jsonParser;
	}
}
