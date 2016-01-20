package com.hw.chineseLearn.model;

import java.io.Serializable;

public class MeiTuanGuessBaseModel extends BaseObject implements Serializable {
    private String id = "";// 7940470 ,
    private String categoryId = "";// 3 ,
    private String shopId = "";// 318 ,
    private String cityId = "";// 2266 ,
    private String title = "";// 仅售168元，市场价294元的阿龙饭店6-8人餐，不限时段，美味尽在这里 ,
    private String sortTitle = "";// 阿龙饭店：6-8人餐，不限时段 ,
    private String imgUrl = "";// http="";//s1.lashouimg.com/zt/2013/12/31/si_138848135566906.jpg
                               // ,
    private String startTime = "";// 1388851200 ,
    private String value = "";// 294.00 ,
    private String price = "";// 168.00 ,
    private String ribat = "";// 5.7 ,
    private String bought = "";// 0 ,
    private String maxQuota = "";// 9999999 ,
    private String post = "";// no ,
    private String soldOut = "";// no ,
    private String tip = "";// ,
    private String endTime = "";// 1396540800 ,
    private String detail = "";// 详情
    private String isRefund = "";// false,
    private String isOverTime = "";// false,
    private String minquota = "";// 1 ,
    private MeiTuanShopBaseModel shop;

    public MeiTuanShopBaseModel getShop() {
        return shop;
    }

    public void setShop(MeiTuanShopBaseModel shop) {
        this.shop = shop;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSortTitle() {
        return sortTitle;
    }

    public void setSortTitle(String sortTitle) {
        this.sortTitle = sortTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRibat() {
        return ribat;
    }

    public void setRibat(String ribat) {
        this.ribat = ribat;
    }

    public String getBought() {
        return bought;
    }

    public void setBought(String bought) {
        this.bought = bought;
    }

    public String getMaxQuota() {
        return maxQuota;
    }

    public void setMaxQuota(String maxQuota) {
        this.maxQuota = maxQuota;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getSoldOut() {
        return soldOut;
    }

    public void setSoldOut(String soldOut) {
        this.soldOut = soldOut;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    public String getIsOverTime() {
        return isOverTime;
    }

    public void setIsOverTime(String isOverTime) {
        this.isOverTime = isOverTime;
    }

    public String getMinquota() {
        return minquota;
    }

    public void setMinquota(String minquota) {
        this.minquota = minquota;
    }

}