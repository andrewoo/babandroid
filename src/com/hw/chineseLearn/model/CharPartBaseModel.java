package com.hw.chineseLearn.model;

import java.io.Serializable;

public class CharPartBaseModel extends BaseObject implements Serializable {
	private int CharId;//
	private String PartDirection;//
	private int PartId;
	private int PartIndex;//
	private String PartPath;
	private int Version;

	public int getCharId() {
		return CharId;
	}

	public void setCharId(int charId) {
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
