package com.hw.chineseLearn.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PinyinModel implements Serializable {

	private String pinyin;
	private String pinyinWithTone;
	private String voicePath;
	private List<String> yunmuWithToneList;
	private static final String[] YMWithToneNum = { "ang1", "ang2", "ang3",
			"ang4", "eng1", "eng2", "eng3", "eng4", "ing1", "ing2", "ing3",
			"ing4", "ong1", "ong2", "ong3", "ong4", "ai1", "ai2", "ai3", "ai4",
			"ei1", "ei2", "ei3", "ei4", "ui1", "ui2", "ui3", "ui4", "ao1",
			"ao2", "ao3", "ao4", "ou1", "ou2", "ou3", "ou4", "iu1", "iu2",
			"iu3", "iu4", "ie1", "ie2", "ie3", "ie4", "ue1", "ue2", "ue3",
			"ue4", "er1", "er2", "er3", "er4", "an1", "an2", "an3", "an4",
			"en1", "en2", "en3", "en4", "in1", "in2", "in3", "in4", "un1",
			"un2", "un3", "un4", "vn1", "vn2", "vn3 ", "vn4", "a1", "a2", "a3",
			"a4", "o1", "o2", "o3", "o4", "e1", "e2", " e3", "e4", "i1", "i2",
			"i3", "i4", "u1", "u2", "u3", "u4", "ü1", "ü2", "ü3", "ü4" };

	private static final String[] changeIToY = { "ia", "ian", "iang", "iao",
			"ie", "iong" };
	private static final String[] changeUToW = { "u", "ua", "uai", "uan",
			"uang", "uei", "uen", "ueng", "uo" };
	private static final String[] changeVToYu = { "ü", "üe", "üan", "ün" };

	public static final String[] YMWithTone = { "āng", "áng", "ǎng", "àng",
			"ēng", "éng", "ěng", "èng", "īng", "íng", "ǐng", "ìng", "ōng",
			"óng", "ǒng", "òng", "āi", "ái", "ǎi", "ài", "ēi", "éi", "ěi",
			"èi", "uī", "uí", "uǐ", "uì", "āo", "áo", "ǎo", "ào", "ōu", "óu",
			"ǒu", "òu", "iū", "iú", "iǔ", "iù", "iē", "ié", "iě", "iè", "uē",
			"ué", "uě", "uè", "ēr", "ér", "ěr", "èr", "ān", "án", "ǎn", "àn",
			"ēn", "én", "ěn", "èn", "īn", "ín", "ǐn", "ìn", "ūn", "ún", "ǔn",
			"ùn", "ǖn", "ǘn", "ǚn", "ǜn", "ā", "á", "ǎ", "à", "ō", "ó", "ǒ",
			"ò", "ē", "é", "ě", "è", "ī", "í", "ǐ", "ì", "ū", "ú", "ǔ", "ù",
			"ǖ", "ǘ", "ǚ", "ǜ" };

	public PinyinModel() {

	}

	public PinyinModel(String tempYunmu, String tempPinyin, int tone,
			String shengmu) {
		asList();
		String yunmu = "";
		if ("-".endsWith(shengmu)) {
			yunmu = getDealHeng(tempYunmu);
		} else {
			yunmu = getDealYu(shengmu, tempYunmu);
		}
		String yunmuTone = yunmu + tone;
		if (yunmuWithToneList.contains(yunmuTone)) {
			int index = yunmuWithToneList.indexOf(yunmuTone);
			String yunmuWithNumber = YMWithTone[index];
			if (tempPinyin.indexOf(yunmu) != -1) {
				String replace = tempPinyin.replace(yunmu, yunmuWithNumber);
				this.setPinyin(replace);
				this.setPinyinWithTone(tempPinyin + tone);
				this.setVoicePath("cpy-" + tempPinyin + tone + ".mp3");
			}
		} else {
			String subYunmu = yunmu.substring(1);
			String subYunmuTone = subYunmu + tone;
			if (yunmuWithToneList.contains(subYunmuTone)) {
				int index = yunmuWithToneList.indexOf(subYunmuTone);
				String yunmuWithNumber = YMWithTone[index];
				if (tempPinyin.indexOf(subYunmu) != -1) {
					String replace = tempPinyin.replace(subYunmu,
							yunmuWithNumber);
					this.setPinyin(replace);
					this.setPinyinWithTone(tempPinyin + tone);
					this.setVoicePath("cpy-" + tempPinyin + tone + ".mp3");
				}
			} else {
				if (subYunmu.length() > 0) {
					String subYunmu2 = subYunmu.substring(1);
					String subYunmuTone2 = subYunmu2 + tone;
					if (yunmuWithToneList.contains(subYunmuTone2)) {
						int index = yunmuWithToneList.indexOf(subYunmuTone2);
						String yunmuWithNumber = YMWithTone[index];
						if (tempPinyin.indexOf(subYunmu2) != -1) {
							String replace = tempPinyin.replace(subYunmu2,yunmuWithNumber);
							this.setPinyin(replace);
							this.setPinyinWithTone(tempPinyin + tone);
							this.setVoicePath("cpy-" + tempPinyin + tone+ ".mp3");
						}
					}
				}
			}
		}
	}

	private void asList() {
		yunmuWithToneList = new ArrayList<String>();
		for (int i = 0; i < YMWithToneNum.length; i++) {
			yunmuWithToneList.add(YMWithToneNum[i]);
		}
	}

	private String getDealYu(String shengmu, String tempYunmu) {

		String str;
		if ((!shengmu.equals("j")) && (!shengmu.equals("q"))
				&& (!shengmu.equals("x"))) {
			str = tempYunmu;
		} else {
			str = tempYunmu.replace("ü", "u");
		}
		return str;
	}

	private String getDealHeng(String tempYunmu) {

		String str = tempYunmu;
		if (tempYunmu.equals("iu")) {
			str = "you";
		}
		tempYunmu = str;
		if (str.equals("i")) {
			tempYunmu = "yi";
		}
		str = tempYunmu;
		if (tempYunmu.equals("in")) {
			str = "yin";
		}
		tempYunmu = str;
		if (str.equals("ing")) {
			tempYunmu = "ying";
		}
		int i = 0;
		while (i < changeIToY.length) {
			str = tempYunmu;
			if (tempYunmu.equals(changeIToY[i])) {
				str = tempYunmu.replace('i', 'y');
			}
			i += 1;
			tempYunmu = str;
		}
		str = tempYunmu;
		if (tempYunmu.equals("u")) {
			str = "wu";
		}
		i = 0;
		for (tempYunmu = str; i < changeUToW.length; tempYunmu = str) {
			str = tempYunmu;
			if (tempYunmu.equals(changeUToW[i])) {
				str = tempYunmu.replace('u', 'w');
			}
			i += 1;
		}
		str = tempYunmu;
		if (tempYunmu.equals("ü")) {
			str = "yu";
		}
		i = 0;
		while (i < changeVToYu.length) {
			tempYunmu = str;
			if (str.equals(changeVToYu[i])) {// van
				tempYunmu = str.replace('ü', 'u');
				tempYunmu = "y" + tempYunmu;
			}
			i += 1;
			str = tempYunmu;
		}
		return str;

	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPinyinWithTone() {
		return pinyinWithTone;
	}

	public void setPinyinWithTone(String pinyinWithTone) {
		this.pinyinWithTone = pinyinWithTone;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}
	
	public class PinyinListModel{
		String pinyin;
		public List<PinyinModel> pinyinList;
		
		public PinyinListModel(){};
		public PinyinListModel(String tempYunmu, String tempPinyin, List<Integer> checkToneList ,String shengmu){
			
			pinyinList=new ArrayList<PinyinModel>();
			for (int i = 0; i < checkToneList.size(); i++) {
				pinyin=tempPinyin;
				int tone=checkToneList.get(i);
				PinyinModel pinyinModel=new PinyinModel(tempYunmu, tempPinyin, tone, shengmu);
				pinyinList.add(pinyinModel);
			}
		}
		
		public String getPinyin() {
			return pinyin;
		}
		
		public List<PinyinModel> getPinyinList() {
			return pinyinList;
		}
	}

}

