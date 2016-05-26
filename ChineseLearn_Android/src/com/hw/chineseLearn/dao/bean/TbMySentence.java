package com.hw.chineseLearn.dao.bean;

import com.j256.ormlite.field.DatabaseField;

public class TbMySentence {

	// SentenceId INTEGER PRIMARY KEY DEFAULT NULL,
	// Status INTEGER,
	// Proficient INTEGER,
	// LessonId INTEGER

	@DatabaseField(columnName = "SentenceId", id = true)
	private int SentenceId;
	@DatabaseField(columnName = "Status")
	private int Status;
	@DatabaseField(columnName = "Proficient")
	private int Proficient;
	@DatabaseField(columnName = "LessonId")
	private int LessonId;

	public int getSentenceId() {
		return SentenceId;
	}

	public void setSentenceId(int sentenceId) {
		SentenceId = sentenceId;
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
