package com.hw.chineseLearn.model;

import java.io.Serializable;
import java.util.ArrayList;

public class LearnSurvivalExpandBaseModel extends BaseObject implements
		Serializable {
	private String unitId = "";// 1 ,
	private String unitName = "";
	private String description;
	private String LessonList;// 1;2
	private int sortIndex;
	private int levelId;
	private String iconResSuffix;
	private int version;
	private int dataUId;
	private boolean isEnable;
	private ArrayList<LearnUnitBaseModel> childData = new ArrayList<LearnUnitBaseModel>();

	public ArrayList<LearnUnitBaseModel> getChildData() {
		return childData;
	}

	public void setChildData(ArrayList<LearnUnitBaseModel> childData) {
		this.childData = childData;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLessonList() {
		return LessonList;
	}

	public void setLessonList(String lessonList) {
		LessonList = lessonList;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public String getIconResSuffix() {
		return iconResSuffix;
	}

	public void setIconResSuffix(String iconResSuffix) {
		this.iconResSuffix = iconResSuffix;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getDataUId() {
		return dataUId;
	}

	public void setDataUId(int dataUId) {
		this.dataUId = dataUId;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

}