package com.hw.chineseLearn.model;

import java.io.Serializable;

public class SurvivalKitModel implements Serializable {

	private String imageName;
	
	private String itemName;
	
	private int iconName;
	
	private int state;
	
	private long count;
	
	private long currentSize;
	
	private float progress;
	
	private int positionTag;
	
	

	public int getPositionTag() {
		return positionTag;
	}

	public void setPositionTag(int positionTag) {
		this.positionTag = positionTag;
	}

	public float getProgress() {
		return (currentSize+0.0f)/count;
	}

	public void setProgress(float progress) {
		this.progress = progress;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(long currentSize) {
		this.currentSize = currentSize;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getIconName() {
		return iconName;
	}

	public void setIconName(int iconName) {
		this.iconName = iconName;
	}

	
	
	
}
