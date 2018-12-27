package com.hz.maiku.maikumodule.bean;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import com.hz.maiku.maikumodule.R;
import java.util.ArrayList;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理基类
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerInformBean implements Comparable<JunkCleanerInformBean> {

    private String mName;
    private long mSize;
    private String mPackageName;
    private String mPath;
    private Drawable mDrawable;
    private ArrayList<JunkCleanerInformBean> mChildren = new ArrayList<>();
    private boolean mIsVisible = false;
    private boolean mIsChild = true;
    private boolean mIsCheck = false;

    public Drawable getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(Drawable mDrawable) {
        this.mDrawable = mDrawable;
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

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getmPath() {
        return mPath;
    }

    public void setmPath(String mPath) {
        this.mPath = mPath;
    }

    public ArrayList<JunkCleanerInformBean> getmChildren() {
        return mChildren;
    }

    public void setmChildren(ArrayList<JunkCleanerInformBean> mChildren) {
        this.mChildren = mChildren;
    }

    public boolean ismIsVisible() {
        return mIsVisible;
    }

    public void setmIsVisible(boolean mIsVisible) {
        this.mIsVisible = mIsVisible;
    }

    public boolean ismIsChild() {
        return mIsChild;
    }

    public void setmIsChild(boolean mIsChild) {
        this.mIsChild = mIsChild;
    }

    public boolean ismIsCheck() {
        return mIsCheck;
    }

    public void setmIsCheck(boolean mIsCheck) {
        this.mIsCheck = mIsCheck;
    }

    @Override
    public String toString() {
        return "JunkCleanerInformBean{" +
                "mName='" + mName + '\'' +
                ", mSize=" + mSize +
                ", mPackageName='" + mPackageName + '\'' +
                ", mPath='" + mPath + '\'' +
                ", mDrawable=" + mDrawable +
                ", mChildren=" + mChildren +
                ", mIsVisible=" + mIsVisible +
                ", mIsChild=" + mIsChild +
                ", mIsCheck=" + mIsCheck +
                '}';
    }

    @Override
    public int compareTo(@NonNull JunkCleanerInformBean bean) {
        String systemCache = "System Cache";
        if (this.mName != null && this.mName.equals(systemCache)) {
            return 1;
        }

        if (bean.mName != null && bean.mName.equals(systemCache)) {
            return -1;
        }

        if (this.mSize > bean.mSize) {
            return 1;
        } else if (this.mSize < bean.mSize) {
            return -1;
        } else {
            return 0;
        }
    }
}
