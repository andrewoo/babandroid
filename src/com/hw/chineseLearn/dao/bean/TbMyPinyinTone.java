package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.hw.chineseLearn.model.PinyinToneLessonExerciseModel;
import com.j256.ormlite.field.DatabaseField;

public class TbMyPinyinTone implements Serializable {
	@DatabaseField(columnName = "id", id = true)
	private int id;
	/**
	 * 是否可用0不可用1可用
	 */
	@DatabaseField(columnName = "status")
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private ArrayList<PinyinToneLessonExerciseModel> lessonModels = new ArrayList<PinyinToneLessonExerciseModel>();

	private String iconResSuffix;

	public String getIconResSuffix() {
		return iconResSuffix;
	}

	public void setIconResSuffix(String iconResSuffix) {
		this.iconResSuffix = iconResSuffix;
	}

	public ArrayList<PinyinToneLessonExerciseModel> getLessonModels() {
		return lessonModels;
	}

	public void setLessonModels(
			ArrayList<PinyinToneLessonExerciseModel> lessonModels) {
		this.lessonModels = lessonModels;
	}

}
