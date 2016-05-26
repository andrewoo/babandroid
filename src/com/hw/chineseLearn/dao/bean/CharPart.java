package com.hw.chineseLearn.dao.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

public class CharPart {
	// CharId INT,
	// PartDirection VARCHAR( 1024 ),
	// PartId INT PRIMARY KEY,
	// PartIndex INT,
	// PartPath VARCHAR( 4096 ),
	// Version INT

	@DatabaseField(columnName = "CharId")
	private String CharId;
	
	@DatabaseField(columnName = "PartDirection")
	private String PartDirection;
	
	@DatabaseField(columnName = "PartId", id = true)
	private int PartId;
	
	@DatabaseField(columnName = "PartIndex")
	private int PartIndex;
	
	@DatabaseField(columnName = "PartPath")
	private String PartPath;
	
	@DatabaseField(columnName = "Version")
	private int Version;

	public String getCharId() {
		return CharId;
	}

	public void setCharId(String charId) {
		CharId = charId;
	}

	public String getPartDirection() {
		return PartDirection;
	}

	public void setPartDirection(String partDirection) {
		PartDirection = partDirection;
	}

	public int getPartId() {
		return PartId;
	}

	public void setPartId(int partId) {
		PartId = partId;
	}

	public int getPartIndex() {
		return PartIndex;
	}

	public void setPartIndex(int partIndex) {
		PartIndex = partIndex;
	}

	public String getPartPath() {
		return PartPath;
	}

	public void setPartPath(String partPath) {
		PartPath = partPath;
	}

	public int getVersion() {
		return Version;
	}

	public void setVersion(int version) {
		Version = version;
	}

}
