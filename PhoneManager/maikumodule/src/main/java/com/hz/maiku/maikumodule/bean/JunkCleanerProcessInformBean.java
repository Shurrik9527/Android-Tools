package com.hz.maiku.maikumodule.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 进程信息基类
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerProcessInformBean implements JunkCleanerMultiItemBean{

    public static final int PROCESS = 0;
    public static final int CACHE = 1;
    public static final int APK = 2;
    public static final int TEMP = 3;
    public static final int LOG = 4;
    private int type;
    private JunkCleanerInformBean junkCleanerInformBean;
    private AppProcessInfornBean appProcessInform;
    private boolean isCheck;

    public JunkCleanerProcessInformBean(JunkCleanerInformBean junkInfo, int type) {
        this.junkCleanerInformBean = junkInfo;
        this.type = type;
    }

    public JunkCleanerProcessInformBean(AppProcessInfornBean appProcessInfo) {
        this.appProcessInform = appProcessInfo;
        this.isCheck = true;
        this.type = PROCESS;
    }

    public JunkCleanerProcessInformBean(JunkCleanerInformBean junkInfo, AppProcessInfornBean appProcessInfo) {
        this.junkCleanerInformBean = junkInfo;
        this.appProcessInform = appProcessInfo;
    }

    public JunkCleanerInformBean getJunkInfo() {
        return junkCleanerInformBean;
    }

    public JunkCleanerProcessInformBean setJunkInfo(JunkCleanerInformBean junkInfo) {
        this.junkCleanerInformBean = junkInfo;
        return this;
    }

    public AppProcessInfornBean getAppProcessInfo() {
        return appProcessInform;
    }

    public JunkCleanerProcessInformBean setAppProcessInfo(AppProcessInfornBean appProcessInfo) {
        this.appProcessInform = appProcessInfo;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public JunkCleanerProcessInformBean setCheck(boolean check) {
        isCheck = check;
        return this;
    }


    @Override
    public int getItemType() {
        return 0;
    }
}
