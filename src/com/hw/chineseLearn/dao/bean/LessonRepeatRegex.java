package com.hw.chineseLearn.dao.bean;

public class LessonRepeatRegex {

	private int lgTable;//对应LGWord或LGSentence
	private int lgTableId;//对应相应表的ID
	private int unknow;//暂时不知道怎么用
	private int count;//题目总数
	
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
	@Override
	public String toString() {
		return "LessonRepeatRegex [lgTable=" + lgTable + ", lgTableId="
				+ lgTableId + ", unknow=" + unknow + ", count=" + count + "]";
	}
	
}
