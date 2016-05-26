package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

public class TbLessonMaterialStatus implements Serializable,Comparable{

	@DatabaseField(columnName = "LessonId", id = true)
	private int LessonId;
	@DatabaseField(columnName = "Status")
	private int Status;
	public int getLessonId() {
		return LessonId;
	}
	public void setLessonId(int lessonId) {
		LessonId = lessonId;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	@Override
	public String toString() {
		return "TbLessonMaterialStatus [LessonId=" + LessonId + ", Status="
				+ Status + "]";
	}
	@Override
	public int compareTo(Object arg0) {
		
		TbLessonMaterialStatus tb=(TbLessonMaterialStatus) arg0;
		if(this.LessonId<tb.getLessonId()){
			return -1;
		}else if(this.LessonId>tb.getLessonId()){
			return 1;
		}else{
			return 0;
		}
	}
}
