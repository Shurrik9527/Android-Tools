package com.hz.maiku.maikumodule.bean;

import android.graphics.drawable.Drawable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe app 基类
 * @date 2018/12/19
 * @email 252774645@qq.com
 */
public class AppBean {
    private int AppId;
    private String AppName;
    private Drawable AppIcon;
    private String AppPackageName;
    private long AppSize;
    private long AppInstallTime;
    private long AppLastTimeUsedTime;
    private long AppTotalTimeInForeground;
    private String AppVersion;
    private boolean isSelect;

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

    public Drawable getAppIcon() {
        return AppIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        AppIcon = appIcon;
    }

    public String getAppPackageName() {
        return AppPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        AppPackageName = appPackageName;
    }

    public long getAppSize() {
        return AppSize;
    }

    public void setAppSize(long appSize) {
        AppSize = appSize;
    }

    public long getAppInstallTime() {
        return AppInstallTime;
    }

    public void setAppInstallTime(long appInstallTime) {
        AppInstallTime = appInstallTime;
    }

    public long getAppLastTimeUsedTime() {
        return AppLastTimeUsedTime;
    }

    public void setAppLastTimeUsedTime(long appLastTimeUsedTime) {
        AppLastTimeUsedTime = appLastTimeUsedTime;
    }

    public long getAppTotalTimeInForeground() {
        return AppTotalTimeInForeground;
    }

    public void setAppTotalTimeInForeground(long appTotalTimeInForeground) {
        AppTotalTimeInForeground = appTotalTimeInForeground;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "AppBean{" +
                "AppId=" + AppId +
                ", AppName='" + AppName + '\'' +
                ", AppIcon=" + AppIcon +
                ", AppPackageName='" + AppPackageName + '\'' +
                ", AppSize=" + AppSize +
                ", AppInstallTime=" + AppInstallTime +
                ", AppLastTimeUsedTime=" + AppLastTimeUsedTime +
                ", AppTotalTimeInForeground=" + AppTotalTimeInForeground +
                ", AppVersion='" + AppVersion + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }

}
