package com.hz.maiku.maikumodule.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe apk基类
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class ApkInformBean {

    private String packageName;
    private String versionName;
    private int versionCode;

    public ApkInformBean() {
    }

    public ApkInformBean(String packageName, String versionName, int versionCode) {
        this.packageName = packageName;
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
