package com.hw.chineseLearn.model;

import java.util.ArrayList;

public class FluentDetailModel {
	// "CATN": "SHOPPING",
	// "TRE": "Buy a coat",
	// "MD5C": "d9a525c7e1d9509df33127ef075bfc66",
	// "PUBD": "2014/12/1",
	// "Sents": {
	// "0": {},
	// "1": {
	// "STRE": "Welcome!",
	// "STRF": "",
	// "STRS": "",
	// "Words": {
	// "0": {},
	// "1": {
	// "SW": "A",
	// "TW": "A"
	// },
	// "2": {
	// "SW": "欢迎",
	// "PY": "huān_yíng",
	// "TW": "歡迎"
	// },
	// "3": {
	// "SW": "光临",
	// "PY": "guāng_lín",
	// "TW": "光臨"
	// },
	// "4": {
	// "SW": "！",
	// "TW": "！"
	// }
	// },
	// "STRJ": "いらっしゃいませ！",
	// "STRK": "어서 오세요!"
	// },
	// },
	// "CATT": "15",
	// "TRJ": "コートを買う",
	// "EID": "16",
	// "TRK": "외투 사기",
	// "TRF": "",
	// "CID": "1551",
	// "TRS": "",
	// "LVLT": "3",
	// "ST": "买大衣",
	// "TT": "買大衣"

	private String CATN;
	private String TRE;
	private String MD5C;
	private String PUBD;
	private ArrayList<FluentDetailListModel> Sents;
	private String CATT;
	private String TRJ;
	private String EID;
	private String TRK;
	private String TRF;
	private String CID;
	private String TRS;
	private String LVLT;
	private String ST;
	private String TT;

	public String getCATN() {
		return CATN;
	}

	public void setCATN(String cATN) {
		CATN = cATN;
	}

	public String getTRE() {
		return TRE;
	}

	public void setTRE(String tRE) {
		TRE = tRE;
	}

	public String getMD5C() {
		return MD5C;
	}

	public void setMD5C(String mD5C) {
		MD5C = mD5C;
	}

	public String getPUBD() {
		return PUBD;
	}

	public void setPUBD(String pUBD) {
		PUBD = pUBD;
	}

	public ArrayList<FluentDetailListModel> getSents() {
		return Sents;
	}

	public void setSents(ArrayList<FluentDetailListModel> sents) {
		Sents = sents;
	}

	public String getCATT() {
		return CATT;
	}

	public void setCATT(String cATT) {
		CATT = cATT;
	}

	public String getTRJ() {
		return TRJ;
	}

	public void setTRJ(String tRJ) {
		TRJ = tRJ;
	}

	public String getEID() {
		return EID;
	}

	public void setEID(String eID) {
		EID = eID;
	}

	public String getTRK() {
		return TRK;
	}

	public void setTRK(String tRK) {
		TRK = tRK;
	}

	public String getTRF() {
		return TRF;
	}

	public void setTRF(String tRF) {
		TRF = tRF;
	}

	public String getCID() {
		return CID;
	}

	public void setCID(String cID) {
		CID = cID;
	}

	public String getTRS() {
		return TRS;
	}

	public void setTRS(String tRS) {
		TRS = tRS;
	}

	public String getLVLT() {
		return LVLT;
	}

	public void setLVLT(String lVLT) {
		LVLT = lVLT;
	}

	public String getST() {
		return ST;
	}

	public void setST(String sT) {
		ST = sT;
	}

	public String getTT() {
		return TT;
	}

	public void setTT(String tT) {
		TT = tT;
	}

}