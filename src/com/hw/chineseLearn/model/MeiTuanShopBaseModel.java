package com.hw.chineseLearn.model;

import java.io.Serializable;

public class MeiTuanShopBaseModel extends BaseObject implements Serializable {
    private String id = "";// 318 ,
    private String name = "";// 阿龙饭店 ,
    private String address = "";// 富阳市后周路185号（阳光里对面） ,
    private String area = "";// 后周路 ,
    private String opentime = "";// 09="";00-13="";00,16="";00-20="";00 ,
    private String lon = "";// 119.93962 ,
    private String lat = "";// 30.05918 ,
    private String tel = "";// 0571-63362577

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

}
