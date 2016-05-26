package com.hw.chineseLearn.model;

import java.io.Serializable;

public class CharacterBaseModel extends BaseObject implements Serializable {

	// CEE VARCHAR( 512 ),
	// CEJ VARCHAR( 512 ),
	// CEK VARCHAR( 512 ),
	// CES VARCHAR( 512 ),
	// CharId INT PRIMARY KEY,
	// CharPath VARCHAR( 4096 ),
	// LevelIndex INT,
	// PEE VARCHAR( 2048 ),
	// PEJ VARCHAR( 2048 ),
	// PEK VARCHAR( 2048 ),
	// PES VARCHAR( 2048 ),
	// Pinyin VARCHAR( 512 ),
	// Version INT,
	// WCharacter VARCHAR( 512 )

	private String CEE;
	private String CEJ;
	private String CEK;
	private String CES;
	private int CharId;//
	private String CharPath;
	private int LevelIndex;//
	private String PEE;
	private String PEJ;
	private String PEK;
	private String PES;
	private String Pinyin;//
	private int Version;
	private String WCharacter;

	public String getCEE() {
		return CEE;
	}

	public void setCEE(String cEE) {
		CEE = cEE;
	}

	public String getCEJ() {
		return CEJ;
	}

	public void setCEJ(String cEJ) {
		CEJ = cEJ;
	}

	public String getCEK() {
		return CEK;
	}

	public void setCEK(String cEK) {
		CEK = cEK;
	}

	public String getCES() {
		return CES;
	}

	public void setCES(String cES) {
		CES = cES;
	}

	public int getCharId() {
		return CharId;
	}

	public void setCharId(int charId) {
		CharId = charId;
	}

	public String getCharPath() {
		return CharPath;
	}

	public void setCharPath(String charPath) {
		CharPath = charPath;
	}

	public int getLevelIndex() {
		return LevelIndex;
	}

	public void setLevelIndex(int levelIndex) {
		LevelIndex = levelIndex;
	}

	public String getPEE() {
		return PEE;
	}

	public void setPEE(String pEE) {
		PEE = pEE;
	}

	public String getPEJ() {
		return PEJ;
	}

	public void setPEJ(String pEJ) {
		PEJ = pEJ;
	}

	public String getPEK() {
		return PEK;
	}

	public void setPEK(String pEK) {
		PEK = pEK;
	}

	public String getPES() {
		return PES;
	}

	public void setPES(String pES) {
		PES = pES;
	}

	public String getPinyin() {
		return Pinyin;
	}

	public void setPinyin(String pinyin) {
		Pinyin = pinyin;
	}

	public int getVersion() {
		return Version;
	}

	public void setVersion(int version) {
		Version = version;
	}

	public String getWCharacter() {
		return WCharacter;
	}

	public void setWCharacter(String wCharacter) {
		WCharacter = wCharacter;
	}

}
