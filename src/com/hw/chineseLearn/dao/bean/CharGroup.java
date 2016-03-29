package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class CharGroup implements Serializable {
	// PartGroupId INT PRIMARY KEY,
	// PartGroupIndex INT,
	// PartGroupList VARCHAR( 2048 ),
	// PartGroupName VARCHAR( 32 ),
	// Version INT
	@DatabaseField(columnName = "PartGroupId", id = true)
	private int PartGroupId;
	@DatabaseField(columnName = "PartGroupIndex")
	private String PartGroupIndex;
	@DatabaseField(columnName = "PartGroupList")
	private String PartGroupList;
	@DatabaseField(columnName = "PartGroupName")
	private String PartGroupName;
	@DatabaseField(columnName = "Version")
	private int Version;

	public int getPartGroupId() {
		return PartGroupId;
	}

	public void setPartGroupId(int partGroupId) {
		PartGroupId = partGroupId;
	}

	public String getPartGroupIndex() {
		return PartGroupIndex;
	}

	public void setPartGroupIndex(String partGroupIndex) {
		PartGroupIndex = partGroupIndex;
	}

	public String getPartGroupList() {
		return PartGroupList;
	}

	public void setPartGroupList(String partGroupList) {
		PartGroupList = partGroupList;
	}

	public String getPartGroupName() {
		return PartGroupName;
	}

	public void setPartGroupName(String partGroupName) {
		PartGroupName = partGroupName;
	}

	public int getVersion() {
		return Version;
	}

	public void setVersion(int version) {
		Version = version;
	}

}
