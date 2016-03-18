package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class Level implements Serializable {
	
	@DatabaseField(columnName = "LevelId", id = true)
	private int LevelId;
	
	private String LevelName;
	
	private String Description;
	
	private int SortIndex;
	
	private String UnitList;
	
	private String TestOutList;
	
	private int Version;
	
	private int DataUId;

	public int getLevelId() {
		return LevelId;
	}

	public void setLevelId(int levelId) {
		LevelId = levelId;
	}

	public String getLevelName() {
		return LevelName;
	}

	public void setLevelName(String levelName) {
		LevelName = levelName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getSortIndex() {
		return SortIndex;
	}

	public void setSortIndex(int sortIndex) {
		SortIndex = sortIndex;
	}

	public String getUnitList() {
		return UnitList;
	}

	public void setUnitList(String unitList) {
		UnitList = unitList;
	}

	public String getTestOutList() {
		return TestOutList;
	}

	public void setTestOutList(String testOutList) {
		TestOutList = testOutList;
	}

	public int getVersion() {
		return Version;
	}

	public void setVersion(int version) {
		Version = version;
	}

	public int getDataUId() {
		return DataUId;
	}

	public void setDataUId(int dataUId) {
		DataUId = dataUId;
	}

	@Override
	public String toString() {
		return "Level [LevelId=" + LevelId + ", LevelName=" + LevelName
				+ ", Description=" + Description + ", SortIndex=" + SortIndex
				+ ", UnitList=" + UnitList + ", TestOutList=" + TestOutList
				+ ", Version=" + Version + ", DataUId=" + DataUId + "]";
	}
	
	
	
	

}
