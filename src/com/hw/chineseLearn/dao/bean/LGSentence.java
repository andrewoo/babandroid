package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class LGSentence implements Serializable {

	@DatabaseField(columnName = "SentenceId", id = true)
	private int SentenceId;

	@DatabaseField(columnName = "Sentence")
	private String Sentence;

	@DatabaseField(columnName = "TSentence")
	private String TSentence;

	@DatabaseField(columnName = "WordList")
	private String WordList;

	@DatabaseField(columnName = "Translations")
	private String Translations;

	@DatabaseField(columnName = "Audio")
	private String Audio;

	@DatabaseField(columnName = "DirCode")
	private String DirCode;

	@DatabaseField(columnName = "Lessons")
	private String Lessons;

	@DatabaseField(columnName = "Version")
	private int Version;

	@DatabaseField(columnName = "DataUId")
	private int DataUId;

	public int getSentenceId() {
		return SentenceId;
	}

	public void setSentenceId(int sentenceId) {
		SentenceId = sentenceId;
	}

	public String getSentence() {
		return Sentence;
	}

	public void setSentence(String sentence) {
		Sentence = sentence;
	}

	public String getTSentence() {
		return TSentence;
	}

	public void setTSentence(String tSentence) {
		TSentence = tSentence;
	}

	public String getWordList() {
		return WordList;
	}

	public void setWordList(String wordList) {
		WordList = wordList;
	}

	public String getTranslations() {
		return Translations;
	}

	public void setTranslations(String translations) {
		Translations = translations;
	}

	public String getAudio() {
		return Audio;
	}

	public void setAudio(String audio) {
		Audio = audio;
	}

	public String getDirCode() {
		return DirCode;
	}

	public void setDirCode(String dirCode) {
		DirCode = dirCode;
	}

	public String getLessons() {
		return Lessons;
	}

	public void setLessons(String lessons) {
		Lessons = lessons;
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

	
}
