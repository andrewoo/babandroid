package com.hw.chineseLearn.dao.bean;

import com.j256.ormlite.field.DatabaseField;

public class TbSetting {

	// SettingName VARCHAR( 100 ) PRIMARY KEY,
	// SettingString VARCHAR( 100 ),
	// SettingValue INTEGER

	@DatabaseField(columnName = "SettingName", id = true)
	private String SettingName;
	@DatabaseField(columnName = "SettingString")
	private String SettingString;
	@DatabaseField(columnName = "SettingValue")
	private int SettingValue;

	public String getSettingName() {
		return SettingName;
	}

	public void setSettingName(String settingName) {
		SettingName = settingName;
	}

	public String getSettingString() {
		return SettingString;
	}

	public void setSettingString(String settingString) {
		SettingString = settingString;
	}

	public int getSettingValue() {
		return SettingValue;
	}

	public void setSettingValue(int settingValue) {
		SettingValue = settingValue;
	}

}
