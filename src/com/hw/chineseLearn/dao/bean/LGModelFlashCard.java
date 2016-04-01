package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

/**
 * flashCard的数据中转
 * 
 * @author yh
 * 
 */
public class LGModelFlashCard implements Serializable {

	private int Id;
	private int SentenceId;
	private int WordId;
	private int CharId;
	private int LessonId;
	private String chinese;
	private String pinyin;
	private String english;
	private int statue;// 记录熟练度
	private String slowVoicePath;// 慢速语音 已隐藏
	private String voicePath;// 语音路径

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getSentenceId() {
		return SentenceId;
	}

	public void setSentenceId(int sentenceId) {
		SentenceId = sentenceId;
	}

	public int getWordId() {
		return WordId;
	}

	public void setWordId(int wordId) {
		WordId = wordId;
	}

	public int getCharId() {
		return CharId;
	}

	public void setCharId(int charId) {
		CharId = charId;
	}

	public int getLessonId() {
		return LessonId;
	}

	public void setLessonId(int lessonId) {
		LessonId = lessonId;
	}

	public String getChinese() {
		return chinese;
	}

	public void setChinese(String chinese) {
		this.chinese = chinese;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public int getStatue() {
		return statue;
	}

	public void setStatue(int statue) {
		this.statue = statue;
	}

	public String getSlowVoicePath() {
		return slowVoicePath;
	}

	public void setSlowVoicePath(String slowVoicePath) {
		this.slowVoicePath = slowVoicePath;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

}
