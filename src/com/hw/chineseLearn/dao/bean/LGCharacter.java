package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class LGCharacter implements Serializable{

	@DatabaseField(columnName = "CharId", id = true)
	private int CharId;
	@DatabaseField(columnName = "Character")
	private String Character;
	public String getCharacter() {
		return Character;
	}
	public void setCharacter(String character) {
		Character = character;
	}
	@DatabaseField(columnName = "Pinyin")
	private String Pinyin;
	@DatabaseField(columnName = "Translation")
	private String Translation;
	@DatabaseField(columnName = "Explanation")
	private String Explanation;
	@DatabaseField(columnName = "Audio")
	private String Audio;
	@DatabaseField(columnName = "ImagePic")
	private String ImagePic;
	@DatabaseField(columnName = "DirCode")
	private String DirCode;
	@DatabaseField(columnName = "Lessons")
	private String Lessons;
	@DatabaseField(columnName = "PartOptions")
	private String PartOptions;
	@DatabaseField(columnName = "PartAnswer")
	private String PartAnswer;
	@DatabaseField(columnName = "TPartOptions")
	private String TPartOptions;
	@DatabaseField(columnName = "TPartAnswer")
	private String TPartAnswer;
	@DatabaseField(columnName = "TCharacter")
	private String TCharacter;
	@DatabaseField(columnName = "Version")
	private String Version;
	@DatabaseField(columnName = "DataUId")
	private String DataUId;
	public int getCharId() {
		return CharId;
	}
	public void setCharId(int charId) {
		CharId = charId;
	}
	public String getPinyin() {
		return Pinyin;
	}
	public void setPinyin(String pinyin) {
		Pinyin = pinyin;
	}
	public String getTranslation() {
		return Translation;
	}
	public void setTranslation(String translation) {
		Translation = translation;
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
	public String getImagePic() {
		return ImagePic;
	}
	public void setImagePic(String imagePic) {
		ImagePic = imagePic;
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
	public String getPartOptions() {
		return PartOptions;
	}
	public void setPartOptions(String partOptions) {
		PartOptions = partOptions;
	}
	public String getPartAnswer() {
		return PartAnswer;
	}
	public void setPartAnswer(String partAnswer) {
		PartAnswer = partAnswer;
	}
	public String getTPartOptions() {
		return TPartOptions;
	}
	public void setTPartOptions(String tPartOptions) {
		TPartOptions = tPartOptions;
	}
	public String getTPartAnswer() {
		return TPartAnswer;
	}
	public void setTPartAnswer(String tPartAnswer) {
		TPartAnswer = tPartAnswer;
	}
	public String getTCharacter() {
		return TCharacter;
	}
	public void setTCharacter(String tCharacter) {
		TCharacter = tCharacter;
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
		return "LGCharacter [CharId=" + CharId + ", Character=" + Character
				+ ", Pinyin=" + Pinyin + ", Translation=" + Translation
				+ ", Explanation=" + Explanation + ", Audio=" + Audio
				+ ", ImagePic=" + ImagePic + ", DirCode=" + DirCode
				+ ", Lessons=" + Lessons + ", PartOptions=" + PartOptions
				+ ", PartAnswer=" + PartAnswer + ", TPartOptions="
				+ TPartOptions + ", TPartAnswer=" + TPartAnswer
				+ ", TCharacter=" + TCharacter + ", Version=" + Version
				+ ", DataUId=" + DataUId + "]";
	}
	
	
	
}
