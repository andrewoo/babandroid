package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class TbMyCategory implements Serializable{

	@DatabaseField(columnName = "id",id=true)
	private int id;
	@DatabaseField(columnName = "complete_dl")
	private int complete_dl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getComplete_dl() {
		return complete_dl;
	}
	public void setComplete_dl(int complete_dl) {
		this.complete_dl = complete_dl;
	}
	
	
	
	
}
