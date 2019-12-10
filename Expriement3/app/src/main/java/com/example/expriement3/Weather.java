package com.example.expriement3;

import java.util.UUID;

public class Weather {
    public UUID getmId() {
        return mId;
    }

    public Weather(){
        this(UUID.randomUUID());
    }
    public Weather(UUID id){
        mId=id;
    }

    private UUID mId;
    private String week,windsc,pre,hum,unit;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPre() {
        return pre;
    }

    public String getWindsc() {
        return windsc;
    }

    public void setWindsc(String windsc) {
        this.windsc = windsc;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    private String mdate;
    private String mfkind;
    private String mlkind;
    private String maxTmp;
    private String minTmp;
    private String imgkind;
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImgkind() {
        return imgkind;
    }

    public void setImgkind(String imgkind) {
        this.imgkind = imgkind;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getMfkind() {
        return mfkind;
    }

    public void setMfkind(String mfkind) {
        this.mfkind = mfkind;
    }

    public String getMlkind() {
        return mlkind;
    }

    public void setMlkind(String mlkind) {
        this.mlkind = mlkind;
    }

    public String getMaxTmp() {
        return maxTmp;
    }

    public void setMaxTmp(String maxTmp) {
        this.maxTmp = maxTmp;
    }

    public String getMinTmp() {
        return minTmp;
    }

    public void setMinTmp(String minTmp) {
        this.minTmp = minTmp;
    }
}
