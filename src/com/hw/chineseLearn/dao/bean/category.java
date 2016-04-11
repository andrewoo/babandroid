package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class category implements Serializable{

	@DatabaseField(columnName = "id", id = true)
	private int id;
	@DatabaseField(columnName = "eng_name")
	private String eng_name;
	@DatabaseField(columnName = "krn_name")
	private String krn_name;
	@DatabaseField(columnName = "jpn_name")
	private String jpn_name;
	@DatabaseField(columnName = "complete_dl")
	private int complete_dl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEng_name() {
		return eng_name;
	}
	public void setEng_name(String eng_name) {
		this.eng_name = eng_name;
	}
	public String getKrn_name() {
		return krn_name;
	}
	public void setKrn_name(String krn_name) {
		this.krn_name = krn_name;
	}
	public String getJpn_name() {
		return jpn_name;
	}
	public void setJpn_name(String jpn_name) {
		this.jpn_name = jpn_name;
	}
	public int getComplete_dl() {
		return complete_dl;
	}
	public void setComplete_dl(int complete_dl) {
		this.complete_dl = complete_dl;
	}
	
	
	
	
}
