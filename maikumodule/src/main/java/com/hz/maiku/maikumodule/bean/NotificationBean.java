package com.hz.maiku.maikumodule.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe 通知栏基类
 * @date 2018/12/21
 * @email 252774645@qq.com
 */
public class NotificationBean extends DataSupport implements Serializable {

    private int AppId;
    private String AppName;
    private String AppPackageName;
    private boolean  isOpen;
    private boolean  isSystem;

    public int getAppId() {
        return AppId;
    }

    public void setAppId(int appId) {
        AppId = appId;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }


    public String getAppPackageName() {
        return AppPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        AppPackageName = appPackageName;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    @Override
    public String toString() {
        return "NotificationBean{" +
                "AppId=" + AppId +
                ", AppName='" + AppName + '\'' +
                ", AppPackageName='" + AppPackageName + '\'' +
                ", isOpen=" + isOpen +
                ", isSystem=" + isSystem +
                '}';
    }
}
