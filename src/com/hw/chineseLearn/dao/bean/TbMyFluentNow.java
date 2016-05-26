package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class TbMyFluentNow implements Serializable {

	@DatabaseField(columnName = "FluentID", id = true)
	private int FluentID;
	@DatabaseField(columnName = "AWS_ID")
	private int AWS_ID;
	@DatabaseField(columnName = "Title_EN")
	private String Title_EN;
	@DatabaseField(columnName = "Title_CN")
	private String Title_CN;
	@DatabaseField(columnName = "ICON")
	private String ICON;
	@DatabaseField(columnName = "Level")
	private int Level;
	@DatabaseField(columnName = "ShowOrder")
	private int ShowOrder;
	@DatabaseField(columnName = "Content")
	private String Content;
	@DatabaseField(columnName = "Resource")
	private String Resource;
	@DatabaseField(columnName = "Downloaded")
	private int Downloaded;
	public int getFluentID() {
		return FluentID;
	}
	public void setFluentID(int fluentID) {
		FluentID = fluentID;
	}
	public int getAWS_ID() {
		return AWS_ID;
	}
	public void setAWS_ID(int aWS_ID) {
		AWS_ID = aWS_ID;
	}
	public String getTitle_EN() {
		return Title_EN;
	}
	public void setTitle_EN(String title_EN) {
		Title_EN = title_EN;
	}
	public String getTitle_CN() {
		return Title_CN;
	}
	public void setTitle_CN(String title_CN) {
		Title_CN = title_CN;
	}
	public String getICON() {
		return ICON;
	}
	public void setICON(String iCON) {
		ICON = iCON;
	}
	public int getLevel() {
		return Level;
	}
	public void setLevel(int level) {
		Level = level;
	}
	public int getShowOrder() {
		return ShowOrder;
	}
	public void setShowOrder(int showOrder) {
		ShowOrder = showOrder;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getResource() {
		return Resource;
	}
	public void setResource(String resource) {
		Resource = resource;
	}
	public int getDownloaded() {
		return Downloaded;
	}
	public void setDownloaded(int downloaded) {
		Downloaded = downloaded;
	}
	
	
}
