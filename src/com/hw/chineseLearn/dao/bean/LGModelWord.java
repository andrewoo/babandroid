package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有word题的数据中转
 * 
 * @author zjv
 * 
 */
public class LGModelWord implements Serializable {

	private String title;// 所有题的题目
	private List<SubLGModel> subLGModelList = new ArrayList<SubLGModel>();// 选项类
																			// 集合
	private int answer;
	private String answerText;

	// ///以后用到字段

	private String slowVoicePath;// 慢速语音 已隐藏
	private String voicePath;// 语音路径
	private List<String> answerList = new ArrayList<String>();// 答案集合 对应tag
	private int SentenceId;
	private int WordId;
	private int CharId;
	private int LessonId;
	private int UnitId;

	@Override
	public String toString() {
		return "LGModelWord [title=" + title + ", subLGModelList="
				+ subLGModelList + ", answer=" + answer + ", answerText="
				+ answerText + ", slowVoicePath=" + slowVoicePath
				+ ", voicePath=" + voicePath + ", answerList=" + answerList
				+ ", SentenceId=" + SentenceId + ", WordId=" + WordId
				+ ", CharId=" + CharId + ", LessonId=" + LessonId + ", UnitId="
				+ UnitId + "]";
	}

	public String getSlowVoicePath() {
		return slowVoicePath;
	}

	public void setSlowVoicePath(String slowVoicePath) {
		this.slowVoicePath = slowVoicePath;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public List<String> getAnswerList() {
		return answerList;
	}

	public void setAnswerList(List<String> answerList) {
		this.answerList = answerList;
	}

	public int getSentenceId() {
		return SentenceId;
	}

	public void setSentenceId(int sentenceId) {
		SentenceId = sentenceId;
	}

	public int getWordId() {
		return WordId;
	}

	public void setWordId(int wordId) {
		WordId = wordId;
	}

	public int getCharId() {
		return CharId;
	}

	public void setCharId(int charId) {
		CharId = charId;
	}

	public int getLessonId() {
		return LessonId;
	}

	public void setLessonId(int lessonId) {
		LessonId = lessonId;
	}

	public int getUnitId() {
		return UnitId;
	}

	public void setUnitId(int unitId) {
		UnitId = unitId;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public List<SubLGModel> getSubLGModelList() {
		return subLGModelList;
	}

	public void setSubLGModelList(List<SubLGModel> subLGModelList) {
		this.subLGModelList = subLGModelList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getAnswer() {
		return answer;
	}

	public void setAnswer(int answer) {
		this.answer = answer;
	}

	/**
	 * 
	 * 选项类 包括图片名字 选项文字描述 和对应wordId
	 * 
	 * @author Administrator
	 * 
	 */
	public class SubLGModel implements Serializable {

		private String imageName;
		private String option;// 选项文字
		private int wordId;
		private String subVoicePath;
		private String StrRect;

		public String getStrRect() {
			return StrRect;
		}

		public void setStrRect(String strRect) {
			StrRect = strRect;
		}

		public String getSubVoicePath() {
			return subVoicePath;
		}

		public void setSubVoicePath(String subVoicePath) {
			this.subVoicePath = subVoicePath;
		}

		public String getImageName() {
			return imageName;
		}

		public void setImageName(String imageName) {
			this.imageName = imageName;
		}

		public String getOption() {
			return option;
		}

		public void setOption(String option) {
			this.option = option;
		}

		public int getWordId() {
			return wordId;
		}

		public void setWordId(int wordId) {
			this.wordId = wordId;
		}

		@Override
		public String toString() {
			return "SubLGModel [imageName=" + imageName + ", option=" + option
					+ ", wordId=" + wordId + ", voicePath=" + voicePath + "]";
		}

	}

}
