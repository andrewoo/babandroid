package com.hw.chineseLearn.dao.bean;

import com.j256.ormlite.field.DatabaseField;

public class TbMyCharacter {

	// CharId INTEGER PRIMARY KEY DEFAULT NULL,
	// Status INTEGER,
	// Proficient INTEGER,
	// LessonId INTEGER

	@DatabaseField(columnName = "CharId", id = true)
	private int CharId;
	@DatabaseField(columnName = "Status")
	private int Status;
	@DatabaseField(columnName = "Proficient")
	private int Proficient;
	@DatabaseField(columnName = "LessonId")
	private int LessonId;

	public int getCharId() {
		return CharId;
	}

	public void setCharId(int charId) {
		CharId = charId;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public int getProficient() {
		return Proficient;
	}

	public void setProficient(int proficient) {
		Proficient = proficient;
	}

	public int getLessonId() {
		return LessonId;
	}

	public void setLessonId(int lessonId) {
		LessonId = lessonId;
	}

}
