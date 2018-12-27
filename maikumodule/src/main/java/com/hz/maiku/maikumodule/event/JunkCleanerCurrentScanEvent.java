package com.hz.maiku.maikumodule.event;


import com.hz.maiku.maikumodule.bean.JunkCleanerInformBean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 当前扫描事件
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class JunkCleanerCurrentScanEvent {
    public static final int SYS_CAHCE = 0;
    public static final int OVER_CACHE = 1;
    private int type;
    private JunkCleanerInformBean junkCleanerInformBean;

    public JunkCleanerCurrentScanEvent(int type, JunkCleanerInformBean junkCleanerInformBean) {
        this.junkCleanerInformBean = junkCleanerInformBean;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public JunkCleanerInformBean getJunkInfo() {
        return junkCleanerInformBean;
    }

    public void setJunkInfo(JunkCleanerInformBean junkCleanerInformBean) {
        this.junkCleanerInformBean = junkCleanerInformBean;
    }
}
