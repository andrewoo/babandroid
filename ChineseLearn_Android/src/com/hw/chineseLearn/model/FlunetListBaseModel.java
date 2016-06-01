package com.hw.chineseLearn.model;

import java.io.Serializable;

public class FlunetListBaseModel extends BaseObject implements Serializable {

	// "id": 1,
	// "diffLevel": 1,
	// "showOrder": 1,
	// "dirId": "1551",
	// "titleEn": "Buy a coat",
	// "titleCn": "买大衣",
	// "icon": null,
	// "txtContent": {
	// "groupName": "group1",
	// "fileName": "M00/00/02/wKgKY1b-C_6ABnrQAAAGYsMuKDQ337.zip"
	// },
	// "audioContent": {
	// "groupName": "group1",
	// "fileName": "M00/00/02/wKgKY1b-EASAI8-LAASm4-3QULI503.zip"
	// },
	// "createTime": 1459494261658,
	// "updateTime": 1459494261658

	private int id;//
	private int diffLevel;
	private int showOrder;
	private String dirId;
	private String titleEn;
	private String titleCn;
	private String icon;
	private FlunetTextContentBaseModel txtContent;
	private FlunetAudioContentBaseModel audioContent;
	private long createTime;//
	private long updateTime;
	private String positionTag="";

	public String getPositionTag() {
		return positionTag;
	}

	public void setPositionTag(String positionTag) {
		this.positionTag = positionTag;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDiffLevel() {
		return diffLevel;
	}

	public void setDiffLevel(int diffLevel) {
		this.diffLevel = diffLevel;
	}

	public int getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}

	public String getDirId() {
		return dirId;
	}

	public void setDirId(String dirId) {
		this.dirId = dirId;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}

	public String getTitleCn() {
		return titleCn;
	}

	public void setTitleCn(String titleCn) {
		this.titleCn = titleCn;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public FlunetTextContentBaseModel getTxtContent() {
		return txtContent;
	}

	public void setTxtContent(FlunetTextContentBaseModel txtContent) {
		this.txtContent = txtContent;
	}

	public FlunetAudioContentBaseModel getAudioContent() {
		return audioContent;
	}

	public void setAudioContent(FlunetAudioContentBaseModel audioContent) {
		this.audioContent = audioContent;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
