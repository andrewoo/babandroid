package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class LGModel_Word_040 implements Serializable {

	@DatabaseField(columnName = "Id", id = true)
	private int Id;
	@DatabaseField(columnName = "WordId")
	private int WordId;
	@DatabaseField(columnName = "Answers")
	private String Answers;
	@DatabaseField(columnName = "Version")
	private int Version;
	@DatabaseField(columnName = "DataUId")
	private int DataUId;

	

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getWordId() {
		return WordId;
	}

	public void setWordId(int wordId) {
		WordId = wordId;
	}

	public String getAnswers() {
		return Answers;
	}

	public void setAnswer(String answers) {
		Answers = answers;
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
		return "LGModel_Word_040 [Id=" + Id + ", WordId=" + WordId
				+ ", Answer=" + Answers + ", Version=" + Version + ", DataUId="
				+ DataUId + "]";
	}

}
