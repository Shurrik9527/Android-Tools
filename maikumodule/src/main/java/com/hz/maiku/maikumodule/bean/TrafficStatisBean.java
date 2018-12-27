package com.hz.maiku.maikumodule.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 流量统计基类
 * @date 2018/10/30
 * @email 252774645@qq.com
 */
public class TrafficStatisBean implements Serializable{

    private String mName;                   //名称
    private String mPackageName;            //包名
    private Drawable mDrawable;            //图标
    private long totalSize;                 //总大小
    private long wifiSize;                 //wifi大小
    private long mobileSize;                 //mobile总大小
    private String uid;
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public Drawable getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getWifiSize() {
        return wifiSize;
    }

    public void setWifiSize(long wifiSize) {
        this.wifiSize = wifiSize;
    }

    public long getMobileSize() {
        return mobileSize;
    }

    public void setMobileSize(long mobileSize) {
        this.mobileSize = mobileSize;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "TrafficStatisticsBean{" +
                "mName='" + mName + '\'' +
                ", mPackageName='" + mPackageName + '\'' +
                ", mDrawable=" + mDrawable +
                ", totalSize=" + totalSize +
                ", wifiSize=" + wifiSize +
                ", mobileSize=" + mobileSize +
                ", uid='" + uid + '\'' +
                '}';
    }
}
