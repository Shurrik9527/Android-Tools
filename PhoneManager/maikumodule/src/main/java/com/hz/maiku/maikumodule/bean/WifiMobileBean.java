package com.hz.maiku.maikumodule.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/11/7
 * @email 252774645@qq.com
 */
public class WifiMobileBean {

    private long wifiSize;
    private long mobileSize;
    private long startTime;
    private long endTime;

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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "WifiMobileBean{" +
                "wifiSize=" + wifiSize +
                ", mobileSize=" + mobileSize +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
