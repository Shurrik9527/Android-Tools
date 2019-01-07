package com.hz.maiku.maikumodule.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/4
 * @email 252774645@qq.com
 */
public class ImageBean {

    private String mUrl;
    private boolean isSelect;
    private String mTime;

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

    @Override
    public String toString() {
        return "ImageBean{" +
                "mUrl='" + mUrl + '\'' +
                ", isSelect=" + isSelect +
                ", mTime='" + mTime + '\'' +
                '}';
    }
}
