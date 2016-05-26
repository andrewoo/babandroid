package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Character")
public class Character implements Serializable  {
	@DatabaseField(columnName = "CEE")
	private String CEE;
	@DatabaseField(columnName = "CEJ")
	private String CEJ;
	@DatabaseField(columnName = "CEK")
	private String CEK;
	@DatabaseField(columnName = "CharId", id = true)
	private String CharId;
	@DatabaseField(columnName = "CharPath") 
	private String CharPath;
	@DatabaseField(columnName = "LevelIndex")
	private String LevelIndex;
	@DatabaseField(columnName = "PEE")
	private String PEE;
	@DatabaseField(columnName = "PEJ")
	private String PEJ;
	@DatabaseField(columnName = "PEK")
	private String PEK;
	@DatabaseField(columnName = "PES")
	private String PES;
	@DatabaseField(columnName = "Pinyin")
	private String Pinyin;
	@DatabaseField(columnName = "Version")
	private String Version;
	@DatabaseField(columnName = "WCharacter")
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
	public String getCharId() {
		return CharId;
	}
	public void setCharId(String charId) {
		CharId = charId;
	}
	public String getCharPath() {
		return CharPath;
	}
	public void setCharPath(String charPath) {
		CharPath = charPath;
	}
	public String getLevelIndex() {
		return LevelIndex;
	}
	public void setLevelIndex(String levelIndex) {
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
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}
	public String getWCharacter() {
		return WCharacter;
	}
	public void setWCharacter(String wCharacter) {
		WCharacter = wCharacter;
	}
	@Override
	public String toString() {
		return "Character [CEE=" + CEE + ", CEJ=" + CEJ + ", CEK=" + CEK
				+ ", CharId=" + CharId + ", CharPath=" + CharPath
				+ ", LevelIndex=" + LevelIndex + ", PEE=" + PEE + ", PEJ="
				+ PEJ + ", PEK=" + PEK + ", PES=" + PES + ", Pinyin=" + Pinyin
				+ ", Version=" + Version + ", WCharacter=" + WCharacter + "]";
	}

	
	
}
