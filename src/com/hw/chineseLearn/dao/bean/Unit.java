package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class Unit  implements Comparable,Serializable {

	@DatabaseField(columnName = "UnitId", id = true)
	private int UnitId;
	@DatabaseField(columnName = "UnitName")
	private String UnitName;
	@DatabaseField(columnName = "Description")
	private String Description;
	@DatabaseField(columnName = "LessonList")
	private String LessonList;
	@DatabaseField(columnName = "SortIndex")
	private int SortIndex;
	 @DatabaseField(foreign = true, columnName = "LevelId")  
	private Level level;
	@DatabaseField(columnName = "IconResSuffix")
	private String IconResSuffix;
	@DatabaseField(columnName = "Version")
	private int Version;
	@DatabaseField(columnName = "DataUId")
	private int DataUId;
	
	

	public int getUnitId() {
		return UnitId;
	}

	public void setUnitId(int unitId) {
		UnitId = unitId;
	}

	public String getUnitName() {
		return UnitName;
	}

	public void setUnitName(String unitName) {
		UnitName = unitName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getLessonList() {
		return LessonList;
	}

	public void setLessonList(String lessonList) {
		LessonList = lessonList;
	}

	public int getSortIndex() {
		return SortIndex;
	}

	public void setSortIndex(int sortIndex) {
		SortIndex = sortIndex;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getIconResSuffix() {
		return IconResSuffix;
	}

	public void setIconResSuffix(String iconResSuffix) {
		IconResSuffix = iconResSuffix;
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
		return "Unit [UnitId=" + UnitId + ", UnitName=" + UnitName
				+ ", Description=" + Description + ", LessonList=" + LessonList
				+ ", SortIndex=" + SortIndex + ", level=" + level
				+ ", IconResSuffix=" + IconResSuffix + ", Version=" + Version
				+ ", DataUId=" + DataUId + "]";
	}

	@Override
	public int compareTo(Object obj) {
		
		Unit unit=(Unit) obj;
		
		if(spitIconName(this.IconResSuffix)<spitIconName(unit.getIconResSuffix())){
			return -1;
		}else if(spitIconName(this.IconResSuffix)>spitIconName(unit.getIconResSuffix())){
			return 1;
		}else{
			return 0;
		}
	}

	private Integer spitIconName(String IconResSuffix) {
		String[] split = IconResSuffix.split("_");
		String iconName=split[0]+split[1];
		return Integer.valueOf(iconName);
	}
}
