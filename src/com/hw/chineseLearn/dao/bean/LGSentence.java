package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class LGSentence implements Serializable {

	// LessonId INT PRIMARY KEY,
	// LessonName NVARCHAR( 3500 ) NOT NULL,
	// Description NVARCHAR( 3500 ) NOT NULL,
	// TDescription NVARCHAR( 3500 ) NOT NULL,
	// WordList NVARCHAR( 3500 ) NOT NULL,
	// SentenceList NVARCHAR( 3500 ) NOT NULL,
	// CharacterList NVARCHAR( 3500 ),
	// RepeatRegex NVARCHAR( 3500 ) NOT NULL,
	// NormalRegex NVARCHAR( 3500 ) NOT NULL,
	// LevelId INT NOT NULL,
	// UnitId INT NOT NULL,
	// SortIndex INT NOT NULL,
	// Version LONG NOT NULL,
	// DataUId NVARCHAR( 64 ) NOT NULL

	@DatabaseField(columnName = "LessonId", id = true)
	private int LessonId;

	@DatabaseField(columnName = "LessonName")
	private String LessonName;

	@DatabaseField(columnName = "Description")
	private String Description;

	@DatabaseField(columnName = "TDescription")
	private String TDescription;

	@DatabaseField(columnName = "WordList")
	private String WordList;

	@DatabaseField(columnName = "SentenceList")
	private String SentenceList;

	@DatabaseField(columnName = "CharacterList")
	private String CharacterList;

	@DatabaseField(columnName = "RepeatRegex")
	private String RepeatRegex;

	@DatabaseField(columnName = "NormalRegex")
	private String NormalRegex;

	@DatabaseField(columnName = "LevelId")
	private int LevelId;

	@DatabaseField(columnName = "UnitId")
	private int UnitId;

	@DatabaseField(columnName = "SortIndex")
	private int SortIndex;

	@DatabaseField(columnName = "Version")
	private Long Version;

	@DatabaseField(columnName = "DataUId")
	private String DataUId;

	public int getLessonId() {
		return LessonId;
	}

	public void setLessonId(int lessonId) {
		LessonId = lessonId;
	}

	public String getLessonName() {
		return LessonName;
	}

	public void setLessonName(String lessonName) {
		LessonName = lessonName;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getTDescription() {
		return TDescription;
	}

	public void setTDescription(String tDescription) {
		TDescription = tDescription;
	}

	public String getWordList() {
		return WordList;
	}

	public void setWordList(String wordList) {
		WordList = wordList;
	}

	public String getSentenceList() {
		return SentenceList;
	}

	public void setSentenceList(String sentenceList) {
		SentenceList = sentenceList;
	}

	public String getCharacterList() {
		return CharacterList;
	}

	public void setCharacterList(String characterList) {
		CharacterList = characterList;
	}

	public String getRepeatRegex() {
		return RepeatRegex;
	}

	public void setRepeatRegex(String repeatRegex) {
		RepeatRegex = repeatRegex;
	}

	public String getNormalRegex() {
		return NormalRegex;
	}

	public void setNormalRegex(String normalRegex) {
		NormalRegex = normalRegex;
	}

	public int getLevelId() {
		return LevelId;
	}

	public void setLevelId(int levelId) {
		LevelId = levelId;
	}

	public int getUnitId() {
		return UnitId;
	}

	public void setUnitId(int unitId) {
		UnitId = unitId;
	}

	public int getSortIndex() {
		return SortIndex;
	}

	public void setSortIndex(int sortIndex) {
		SortIndex = sortIndex;
	}

	public Long getVersion() {
		return Version;
	}

	public void setVersion(Long version) {
		Version = version;
	}

	public String getDataUId() {
		return DataUId;
	}

	public void setDataUId(String dataUId) {
		DataUId = dataUId;
	}
	
	
	

}
