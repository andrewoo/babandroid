package com.hw.chineseLearn.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PinyinToneLessonExerciseModel implements Serializable {

	String voicePath;
	String py;
	String cn;
	String en;
	String right;

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

}
