package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;

public class LessonRepeatRegex implements Serializable{

	private int lgTable;//对应LGWord或LGSentence
	private int lgTableId;//对应相应表的ID
	private int unknow;//暂时不知道怎么用
	private int count;//题目总数
	private int randomSubject;
	
	public int getLgTable() {
		return lgTable;
	}
	public void setLgTable(int lgTable) {
		this.lgTable = lgTable;
	}
	public int getLgTableId() {
		return lgTableId;
	}
	public void setLgTableId(int lgTableId) {
		this.lgTableId = lgTableId;
	}
	public int getUnknow() {
		return unknow;
	}
	public void setUnknow(int unknow) {
		this.unknow = unknow;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getRandomSubject() {
		return randomSubject;
	}
	public void setRandomSubject(int randomSubject) {
		this.randomSubject = randomSubject;
	}
	
	@Override
	public String toString() {
		return "LessonRepeatRegex [lgTable=" + lgTable + ", lgTableId="
				+ lgTableId + ", unknow=" + unknow + ", count=" + count
				+ ", randomSubject=" + randomSubject + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + lgTable;
		result = prime * result + lgTableId;
		result = prime * result + unknow;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LessonRepeatRegex other = (LessonRepeatRegex) obj;
		if (count != other.count)
			return false;
		if (lgTable != other.lgTable)
			return false;
		if (lgTableId != other.lgTableId)
			return false;
		if (unknow != other.unknow)
			return false;
		return true;
	}
	
	
	
}
