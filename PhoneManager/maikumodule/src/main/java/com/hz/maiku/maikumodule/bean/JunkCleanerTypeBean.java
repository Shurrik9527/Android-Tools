package com.hz.maiku.maikumodule.bean;


import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.util.AbstractExpandableListViewItem;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 类型基类
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerTypeBean extends AbstractExpandableListViewItem<JunkCleanerProcessInformBean> implements JunkCleanerMultiItemBean {
    public static final int PROCESS = 0;
    public static final int CACHE = 1;
    public static final int APK = 2;
    public static final int TEMP = 3;
    public static final int LOG = 4;


    private String title;
    private String totalSize;
    private int iconResourceId;
    private boolean isCheck;
    private boolean isProgressVisible;

    public JunkCleanerTypeBean() {

    }

    public JunkCleanerTypeBean(String title, String totalSize, int iconResourceId, boolean isCheck, boolean isProgressVisible) {
        this.title = title;
        this.totalSize = totalSize;
        this.iconResourceId = iconResourceId;
        this.isCheck = isCheck;
        this.isProgressVisible = isProgressVisible;
    }

    public String getTitle() {
        return title;
    }

    public JunkCleanerTypeBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public JunkCleanerTypeBean setTotalSize(String totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public JunkCleanerTypeBean setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public JunkCleanerTypeBean setCheck(boolean check) {
        isCheck = check;
        return this;
    }

    public boolean isProgressVisible() {
        return isProgressVisible;
    }

    public JunkCleanerTypeBean setProgressVisible(boolean progressVisible) {
        isProgressVisible = progressVisible;
        return this;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return Constant.TYPE_TITLE;
    }

    @Override
    public String toString() {
        return "JunkType{" +
                "title='" + title + '\'' +
                ", totalSize='" + totalSize + '\'' +
                ", iconResourceId=" + iconResourceId +
                ", isCheck=" + isCheck +
                ", isProgressVisible=" + isProgressVisible +
                '}';
    }
}
