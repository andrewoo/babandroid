package com.hw.chineseLearn.model;

import java.util.ArrayList;

public class FluentDetailListModel {
	// "STRE": "How about this one?",
	// "STRF": "",
	// "STRS": "",
	// "Words":{}
	// "STRJ": "こんにちは、コートを買いたいのですが。"
	// "STRK": "안녕하세요! 저는 외투 하나를 사고 싶은데요."
	private String STRE;
	private String STRF;
	private String STRS;
	private ArrayList<FluentDetailListWordsModel> Words;
	private String STRJ; 
	private String STRK;

	public String getSTRE() {
		return STRE;
	}

	public void setSTRE(String sTRE) {
		STRE = sTRE;
	}

	public String getSTRF() {
		return STRF;
	}

	public void setSTRF(String sTRF) {
		STRF = sTRF;
	}

	public String getSTRS() {
		return STRS;
	}

	public void setSTRS(String sTRS) {
		STRS = sTRS;
	}

	public ArrayList<FluentDetailListWordsModel> getWords() {
		return Words;
	}

	public void setWords(ArrayList<FluentDetailListWordsModel> words) {
		Words = words;
	}

	public String getSTRJ() {
		return STRJ;
	}

	public void setSTRJ(String sTRJ) {
		STRJ = sTRJ;
	}

	public String getSTRK() {
		return STRK;
	}

	public void setSTRK(String sTRK) {
		STRK = sTRK;
	}

}