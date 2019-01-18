package com.hz.maiku.maikumodule.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/18
 * @email 252774645@qq.com
 */
public class BigFileBean {

    private String mUrl;
    private boolean isSelect;
    private long size;
    private String mName;

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

    @Override
    public String toString() {
        return "BigFileBean{" +
                "mUrl='" + mUrl + '\'' +
                ", isSelect=" + isSelect +
                ", size=" + size +
                ", mName='" + mName + '\'' +
                '}';
    }
}
