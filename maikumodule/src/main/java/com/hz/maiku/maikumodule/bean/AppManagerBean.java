package com.hz.maiku.maikumodule.bean;

import android.content.pm.ApplicationInfo;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/25
 * @email 252774645@qq.com
 */
public class AppManagerBean {

    private ApplicationInfo applicationInfo;
    private boolean isSelect;

    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    public void setApplicationInfo(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "AppManagerBean{" +
                "applicationInfo=" + applicationInfo +
                ", isSelect=" + isSelect +
                '}';
    }
}
