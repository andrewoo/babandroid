package com.hw.chineseLearn.dao.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 所有word题的数据中转
 * @author zjv
 *
 */
public class LGModelWord implements Serializable {
	
	private String title;//所有题的题目
	private List<SubLGModel> subLGModelList=new ArrayList<SubLGModel>();//选项类 集合
	private List<String> options=new ArrayList<String>();//选项 
	private int answer;
	private int wordId;
	
	
	
	public int getWordId() {
		return wordId;
	}
	public void setWordId(int wordId) {
		this.wordId = wordId;
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
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}

	/**
	 * 
	 * 选项类  包括图片名字 选项文字描述 和对应wordId
	 * @author Administrator
	 *
	 */
	public class SubLGModel implements Serializable{
		
		private String imageName;
		private String option;
		private int wordId;
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
		
	}
	
}
