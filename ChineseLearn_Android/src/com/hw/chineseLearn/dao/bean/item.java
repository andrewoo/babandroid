package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class item implements Serializable {
	
	@DatabaseField(columnName = "id", id = true)
	private int id;
	@DatabaseField(columnName = "cid")
	private int cid;
	@DatabaseField(columnName = "sid")
	private int sid;
	@DatabaseField(columnName = "eng_item")
	private String eng_item;
	@DatabaseField(columnName = "chn_item")
	private String chn_item;
	@DatabaseField(columnName = "tchn_item")
	private String tchn_item;
	@DatabaseField(columnName = "krn_item")
	private String krn_item;
	@DatabaseField(columnName = "jpn_item")
	private String jpn_item;
	@DatabaseField(columnName = "pinyin")
	private String pinyin;
	@DatabaseField(columnName = "pron_file_name")
	private String pron_file_name;
	@DatabaseField(columnName = "slow_pron_file_name")
	private String slow_pron_file_name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getEng_item() {
		return eng_item;
	}

	public void setEng_item(String eng_item) {
		this.eng_item = eng_item;
	}

	public String getChn_item() {
		return chn_item;
	}

	public void setChn_item(String chn_item) {
		this.chn_item = chn_item;
	}

	public String getTchn_item() {
		return tchn_item;
	}

	public void setTchn_item(String tchn_item) {
		this.tchn_item = tchn_item;
	}

	public String getKrn_item() {
		return krn_item;
	}

	public void setKrn_item(String krn_item) {
		this.krn_item = krn_item;
	}

	public String getJpn_item() {
		return jpn_item;
	}

	public void setJpn_item(String jpn_item) {
		this.jpn_item = jpn_item;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPron_file_name() {
		return pron_file_name;
	}

	public void setPron_file_name(String pron_file_name) {
		this.pron_file_name = pron_file_name;
	}

	public String getSlow_pron_file_name() {
		return slow_pron_file_name;
	}

	public void setSlow_pron_file_name(String slow_pron_file_name) {
		this.slow_pron_file_name = slow_pron_file_name;
	}
	
	
}
