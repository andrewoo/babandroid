package com.hw.chineseLearn.dao.bean;

import com.j256.ormlite.field.DatabaseField;

public class LGCharacterPart {

	@DatabaseField(columnName = "PartId", id = true)
	private int PartId;
	@DatabaseField(columnName = "PartName")
	private String PartName;
	@DatabaseField(columnName = "Description")
	private String Description;
	@DatabaseField(columnName = "StrRect")
	private String StrRect;
	@DatabaseField(columnName = "ImageName")
	private String ImageName;
	@DatabaseField(columnName = "DirCode")
	private String DirCode;
	@DatabaseField(columnName = "Version")
	private int Version;
	@DatabaseField(columnName = "DataUId")
	private int DataUId;
	public int getPartId() {
		return PartId;
	}
	public void setPartId(int partId) {
		PartId = partId;
	}
	public String getPartName() {
		return PartName;
	}
	public void setPartName(String partName) {
		PartName = partName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getStrRect() {
		return StrRect;
	}
	public void setStrRect(String strRect) {
		StrRect = strRect;
	}
	public String getImageName() {
		return ImageName;
	}
	public void setImageName(String imageName) {
		ImageName = imageName;
	}
	public String getDirCode() {
		return DirCode;
	}
	public void setDirCode(String dirCode) {
		DirCode = dirCode;
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
		return "LGCharacterPart [PartId=" + PartId + ", PartName=" + PartName
				+ ", Description=" + Description + ", StrRect=" + StrRect
				+ ", ImageName=" + ImageName + ", DirCode=" + DirCode
				+ ", Version=" + Version + ", DataUId=" + DataUId + "]";
	}
	
	
	
	
	
}
