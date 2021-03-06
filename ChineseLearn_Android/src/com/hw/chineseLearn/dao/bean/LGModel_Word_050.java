package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class LGModel_Word_050 implements Serializable {

	@DatabaseField(columnName = "Id", id = true)
	private int Id;
	@DatabaseField(columnName = "WordId")
	private int WordId;
	@DatabaseField(columnName = "Answer")
	private int Answer;
	@DatabaseField(columnName = "Options")
	private String Options;
	@DatabaseField(columnName = "Version")
	private int Version;
	@DatabaseField(columnName = "DataUId")
	private int DataUId;

	
	public String getOptions() {
		return Options;
	}

	public void setOptions(String options) {
		Options = options;
	}
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

	public int getAnswer() {
		return Answer;
	}

	public void setAnswer(int answer) {
		Answer = answer;
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
		return "LGModel_Word_050 [Id=" + Id + ", WordId=" + WordId
				+ ", Answer=" + Answer + ", Options=" + Options + ", Version="
				+ Version + ", DataUId=" + DataUId + "]";
	}
}
