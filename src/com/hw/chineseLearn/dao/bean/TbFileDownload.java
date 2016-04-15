package com.hw.chineseLearn.dao.bean;

import com.j256.ormlite.field.DatabaseField;

public class TbFileDownload {

	// id INTEGER PRIMARY KEY DEFAULT NULL,
	// cwsId INTEGER,
	// type INTEGER,
	// curFileContentSize INTEGER,
	// fileContentSize INTEGER,
	// fileName VARCHAR( 1000 ) DEFAULT NULL,
	// filePath VARCHAR( 1000 ) DEFAULT NULL,
	// fileExtent VARCHAR( 100 ) DEFAULT NULL,
	// fileURL VARCHAR( 1000 ) DEFAULT NULL,
	// dlStatus INTEGER

	@DatabaseField(columnName = "id", id = true)
	private int id;
	@DatabaseField(columnName = "cwsId")
	private int cwsId;
	@DatabaseField(columnName = "type")
	private int type;
	@DatabaseField(columnName = "curFileContentSize")
	private long curFileContentSize;
	@DatabaseField(columnName = "fileContentSize")
	private long fileContentSize;
	@DatabaseField(columnName = "fileName")
	private String fileName;
	@DatabaseField(columnName = "filePath")
	private String filePath;
	@DatabaseField(columnName = "fileExtent")
	private String fileExtent;
	@DatabaseField(columnName = "fileURL")
	private String fileURL;
	@DatabaseField(columnName = "dlStatus")
	private int dlStatus;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCwsId() {
		return cwsId;
	}

	public void setCwsId(int cwsId) {
		this.cwsId = cwsId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileExtent() {
		return fileExtent;
	}

	public void setFileExtent(String fileExtent) {
		this.fileExtent = fileExtent;
	}

	public String getFileURL() {
		return fileURL;
	}

	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}

	public int getDlStatus() {
		return dlStatus;
	}

	public void setDlStatus(int dlStatus) {
		this.dlStatus = dlStatus;
	}

	public long getCurFileContentSize() {
		return curFileContentSize;
	}

	public void setCurFileContentSize(long curFileContentSize) {
		this.curFileContentSize = curFileContentSize;
	}

	public long getFileContentSize() {
		return fileContentSize;
	}

	public void setFileContentSize(long fileContentSize) {
		this.fileContentSize = fileContentSize;
	}
	
}
