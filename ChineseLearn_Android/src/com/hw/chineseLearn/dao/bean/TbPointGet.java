package com.hw.chineseLearn.dao.bean;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class TbPointGet {

	// Id INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT NULL,
	// Point INTEGER,
	// CreateDate DATE DEFAULT NULL

	@DatabaseField(columnName = "Id", id = true)
	private int Id;
	@DatabaseField(columnName = "Point")
	private int Point;
	@DatabaseField(columnName = "CreateDate")
	private Date CreateDate;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public int getPoint() {
		return Point;
	}

	public void setPoint(int point) {
		Point = point;
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
	}

}
