package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class LGModel_Sentence_040 implements Serializable {

	// Id INT PRIMARY KEY,
	// SentenceId INT NOT NULL,
	// Options NVARCHAR( 3500 ) NOT NULL,
	// Answer NVARCHAR( 3500 ) NOT NULL,
	// Version LONG NOT NULL,
	// DataUId NVARCHAR( 64 ) NOT NULL

	@DatabaseField(columnName = "Id", id = true)
	private int Id;

	@DatabaseField(columnName = "SentenceId")
	private int SentenceId;

	@DatabaseField(columnName = "Options")
	private String Options;

	@DatabaseField(columnName = "Answer")
	private String Answer;

	@DatabaseField(columnName = "Version")
	private Long Version;

	@DatabaseField(columnName = "DataUId")
	private String DataUId;

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

	public String getOptions() {
		return Options;
	}

	public void setOptions(String options) {
		Options = options;
	}

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String answer) {
		Answer = answer;
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
