package com.hw.chineseLearn.dao.bean;

import com.j256.ormlite.field.DatabaseField;

public class LGWord {

	@DatabaseField(columnName = "WordId", id = true)
	private int WordId;
	@DatabaseField(columnName = "Word")
	private String Word;
	@DatabaseField(columnName = "TWord")
	private String TWord;
	@DatabaseField(columnName = "Pinyin")
	private String Pinyin;
	@DatabaseField(columnName = "Translations")
	private String Translations;
	@DatabaseField(columnName = "Explanation")
	private String Explanation;
	@DatabaseField(columnName = "Audio")
	private String Audio;
	@DatabaseField(columnName = "MainPic")
	private String MainPic;
	@DatabaseField(columnName = "Images")
	private String Images;
	@DatabaseField(columnName = "DirCode")
	private String DirCode;
	@DatabaseField(columnName = "Lessons")
	private String Lessons;
	@DatabaseField(columnName = "Version")
	private String Version;
	@DatabaseField(columnName = "DataUId")
	private String DataUId;

	public int getWordId() {
		return WordId;
	}

	public void setWordId(int wordId) {
		WordId = wordId;
	}

	public String getWord() {
		return Word;
	}

	public void setWord(String word) {
		Word = word;
	}

	public String getTWord() {
		return TWord;
	}

	public void setTWord(String tWord) {
		TWord = tWord;
	}

	public String getPinyin() {
		return Pinyin;
	}

	public void setPinyin(String pinyin) {
		Pinyin = pinyin;
	}

	public String getTranslations() {
		return Translations;
	}

	public void setTranslations(String translations) {
		Translations = translations;
	}

	public String getExplanation() {
		return Explanation;
	}

	public void setExplanation(String explanation) {
		Explanation = explanation;
	}

	public String getAudio() {
		return Audio;
	}

	public void setAudio(String audio) {
		Audio = audio;
	}

	public String getMainPic() {
		return MainPic;
	}

	public void setMainPic(String mainPic) {
		MainPic = mainPic;
	}

	public String getImages() {
		return Images;
	}

	public void setImages(String images) {
		Images = images;
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

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public String getDataUId() {
		return DataUId;
	}

	public void setDataUId(String dataUId) {
		DataUId = dataUId;
	}

	@Override
	public String toString() {
		return "LGWord [WordId=" + WordId + ", Word=" + Word + ", TWord="
				+ TWord + ", Pinyin=" + Pinyin + ", Translations="
				+ Translations + ", Explanation=" + Explanation + ", Audio="
				+ Audio + ", MainPic=" + MainPic + ", Images=" + Images
				+ ", DirCode=" + DirCode + ", Lessons=" + Lessons
				+ ", Version=" + Version + ", DataUId=" + DataUId + "]";
	}
	
	
}
