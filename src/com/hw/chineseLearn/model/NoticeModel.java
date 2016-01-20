package com.hw.chineseLearn.model;

import java.util.ArrayList;

public class NoticeModel extends BaseDataListObject {
    private ArrayList<MeiTuanGuessBaseModel> datas;

    public NoticeModel() {
        super();
        // TODO Auto-generated constructor stub
    }

    public NoticeModel(String statusCode, int state, String message,
            BaseObject map, int page, BaseObject queryBean) {
        super(statusCode, state, message, map, page, queryBean);
        // TODO Auto-generated constructor stub
    }

    public NoticeModel(ArrayList<MeiTuanGuessBaseModel> datas) {
        super();
        this.datas = datas;
    }

    public ArrayList<MeiTuanGuessBaseModel> getData() {
        return datas;
    }

    public void setData(ArrayList<MeiTuanGuessBaseModel> datas) {
        this.datas = datas;
    }
}