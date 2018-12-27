package com.hz.maiku.maikumodule.bean;

import android.content.pm.ApplicationInfo;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 应用锁基类
 * @date 2018/10/11
 * @email 252774645@qq.com
 */
public class CommLockInfo extends DataSupport implements Serializable{

    private long id;
    private String packageName;
    private String appName;
    private boolean isLocked;  //是否已加锁
    private boolean isFaviterApp;  //是否是推荐加锁的app
    private ApplicationInfo appInfo;
    private String topTitle;
    private boolean isSetUnLock;


    public CommLockInfo() {
    }

    public CommLockInfo(String packageName, boolean isLocked, boolean isFaviterApp) {
        this.packageName = packageName;
        this.isLocked = isLocked;
        this.isFaviterApp = isFaviterApp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public boolean isFaviterApp() {
        return isFaviterApp;
    }

    public void setFaviterApp(boolean faviterApp) {
        isFaviterApp = faviterApp;
    }

    public ApplicationInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(ApplicationInfo appInfo) {
        this.appInfo = appInfo;
    }

    public String getTopTitle() {
        return topTitle;
    }

    public void setTopTitle(String topTitle) {
        this.topTitle = topTitle;
    }

    public boolean isSetUnLock() {
        return isSetUnLock;
    }

    public void setSetUnLock(boolean setUnLock) {
        isSetUnLock = setUnLock;
    }

    @Override
    public String toString() {
        return "CommLockInfo{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", isLocked=" + isLocked +
                ", isFaviterApp=" + isFaviterApp +
                ", appInfo=" + appInfo +
                ", topTitle='" + topTitle + '\'' +
                ", isSetUnLock=" + isSetUnLock +
                '}';
    }
}
