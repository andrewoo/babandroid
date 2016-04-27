package com.hw.chineseLearn.model;

import java.io.Serializable;

public class FlunetTextContentBaseModel extends BaseObject implements
		Serializable {

	// "groupName": "group1",
	// "fileName": "M00/00/02/wKgKY1b-C_6ABnrQAAAGYsMuKDQ337.zip"

	private String groupName;
	private String fileName;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
