package com.hz.maiku.maikumodule.bean;

import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe app信息基类
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class AppInformBean implements Comparable<AppInformBean>{

    private PackageInfo mPackageInfo;     //apk信息
    private String mName;                   //名称
    private String mPackageName;            //包名
    private boolean mIsSystem;            //是否系统app
    private Drawable mDrawable;            //图标
    private long mSize;                     //大小
    private long mInstallTime;              //安装时间

    public PackageInfo getmPackageInfo() {
        return mPackageInfo;
    }

    public void setmPackageInfo(PackageInfo mPackageInfo) {
        this.mPackageInfo = mPackageInfo;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public boolean ismIsSystem() {
        return mIsSystem;
    }

    public void setmIsSystem(boolean mIsSystem) {
        this.mIsSystem = mIsSystem;
    }

    public Drawable getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
    }

    public long getmSize() {
        return mSize;
    }

    public void setmSize(long mSize) {
        this.mSize = mSize;
    }

    public long getmInstallTime() {
        return mInstallTime;
    }

    public void setmInstallTime(long mInstallTime) {
        this.mInstallTime = mInstallTime;
    }

    @Override
    public String toString() {
        return "AppInformBean{" +
                "mPackageInfo=" + mPackageInfo +
                ", mName='" + mName + '\'' +
                ", mPackageName='" + mPackageName + '\'' +
                ", mIsSystem=" + mIsSystem +
                ", mDrawable=" + mDrawable +
                ", mSize=" + mSize +
                ", mInstallTime=" + mInstallTime +
                '}';
    }

    @Override
    public int compareTo(@NonNull AppInformBean another) {
        if ((this.ismIsSystem() && another.ismIsSystem()) ||
                (!this.ismIsSystem() && !another.ismIsSystem())) {
            return this.getmName().compareTo(another.getmName());
        } else if (this.ismIsSystem() && !another.ismIsSystem()) {
            return 1;
        } else if (!this.ismIsSystem() && another.ismIsSystem()) {
            return -1;
        }
        return 0;
    }


}
