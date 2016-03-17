package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class Lesson implements Comparable,Serializable{
	
	@DatabaseField(columnName = "LessonId", id = true)
	private int LessonId;
	@DatabaseField(columnName = "LessonName")
	private String LessonName;
	@DatabaseField(columnName = "Description")
	private String Description;
	@DatabaseField(columnName = "TDescription")
	private  String TDescription;
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
	@DatabaseField(columnName = "UnitId",foreign = true)
	private Unit unit;//外键UnitId
	@DatabaseField(columnName = "SortIndex")
	private int SortIndex;
	@DatabaseField(columnName = "Version")
	private int Version;
	@DatabaseField(columnName = "DataUId")
	private int DataUId;
	
	public String getLessonName() {
		return LessonName;
	}

	public void setLessonName(String lessonName) {
		LessonName = lessonName;
	}

	public int getLessonId() {
		return LessonId;
	}

	public void setLessonId(int lessonId) {
		LessonId = lessonId;
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

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public int getSortIndex() {
		return SortIndex;
	}

	public void setSortIndex(int sortIndex) {
		SortIndex = sortIndex;
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
		return "Lesson [LessonId=" + LessonId + ", LesonName=" + LessonName
				+ ", Description=" + Description + ", TDescription="
				+ TDescription + ", WordList=" + WordList + ", SentenceList="
				+ SentenceList + ", CharacterList=" + CharacterList
				+ ", RepeatRegex=" + RepeatRegex + ", NormalRegex="
				+ NormalRegex + ", LevelId=" + LevelId + ", unit=" + unit
				+ ", SortIndex=" + SortIndex + ", Version=" + Version
				+ ", DataUId=" + DataUId + "]";
	}
	@Override
	public int compareTo(Object obj) {
		
		Lesson unit=(Lesson) obj;
		
		if(this.LessonId<unit.getLessonId()){
			return -1;
		}else if(this.LessonId>unit.getLessonId()){
			return 1;
		}else{
			return 0;
		}
	}

}
