package com.hz.maiku.maikumodule.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/9
 * @email 252774645@qq.com
 */
public class VideoBean {


    private String mUrl;
    private boolean isSelect;
    private String mTime;
    private long size;
    private String mName;
    private long mDuration;

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public long getmDuration() {
        return mDuration;
    }

    public void setmDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    @Override
    public String toString() {
        return "VideoBean{" +
                "mUrl='" + mUrl + '\'' +
                ", isSelect=" + isSelect +
                ", mTime='" + mTime + '\'' +
                ", size=" + size +
                ", mName='" + mName + '\'' +
                ", mDuration=" + mDuration +
                '}';
    }
}
