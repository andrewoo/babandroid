package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class LGModel_Word_060 implements Serializable {

	@DatabaseField(columnName = "Id", id = true)
	private int Id;
	@DatabaseField(columnName = "WordId")
	private int WordId;
	@DatabaseField(columnName = "Stem")
	private int Stem;
	@DatabaseField(columnName = "Answer")
	private String Answer;
	@DatabaseField(columnName = "Options")
	private String Options;
	@DatabaseField(columnName = "TOptions")
	private String TOptions;
	@DatabaseField(columnName = "Version")
	private int Version;
	@DatabaseField(columnName = "DataUId")
	private int DataUId;

	
	public int getStem() {
		return Stem;
	}

	public void setStem(int stem) {
		Stem = stem;
	}

	public String getTOptions() {
		return TOptions;
	}

	public void setTOptions(String tOptions) {
		TOptions = tOptions;
	}

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

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String answer) {
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
		return "LGModel_Word_060 [Id=" + Id + ", WordId=" + WordId + ", Stem="
				+ Stem + ", Answer=" + Answer + ", Options=" + Options
				+ ", TOptions=" + TOptions + ", Version=" + Version
				+ ", DataUId=" + DataUId + "]";
	}
	
	
}
