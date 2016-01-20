package com.hw.chineseLearn.model;

import java.util.List;

/**
 * 基类List
 * 
 * @author
 */
public class BaseDataListObject extends BaseObject {
    private int state;//
    private int page;//

    public BaseDataListObject() {
        super();
    }

    public BaseDataListObject(String statusCode, int state, String message,
            BaseObject map, int page, BaseObject queryBean) {
        super();
        this.state = state;
        this.page = page;
    }

    public BaseDataListObject(String statusCode, int state, String message,
            BaseObject map, int page, BaseObject queryBean,
            List<BaseObject> data) {
        super();
        this.state = state;
        this.page = page;
    }

    public int getStatus() {
        return state;
    }

    public void setStatus(int state) {
        this.state = state;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
