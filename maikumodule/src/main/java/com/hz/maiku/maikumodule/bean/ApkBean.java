package com.hz.maiku.maikumodule.bean;

import android.graphics.Bitmap;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/9
 * @email 252774645@qq.com
 */
public class ApkBean {

    private String mUrl;
    private String mName;
    private long   mSize;
    private boolean isSelect;
    private Bitmap mIcon;

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public Bitmap getmIcon() {
        return mIcon;
    }

    public void setmIcon(Bitmap mIcon) {
        this.mIcon = mIcon;
    }

    @Override
    public String toString() {
        return "ApkBean{" +
                "mUrl='" + mUrl + '\'' +
                ", mName='" + mName + '\'' +
                ", mSize=" + mSize +
                ", isSelect=" + isSelect +
                ", mIcon=" + mIcon +
                '}';
    }
}
