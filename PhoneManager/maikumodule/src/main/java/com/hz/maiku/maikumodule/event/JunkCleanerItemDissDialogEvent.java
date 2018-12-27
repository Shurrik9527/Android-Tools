package com.hz.maiku.maikumodule.event;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 item dialog 消失事件
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class JunkCleanerItemDissDialogEvent {

    private int index;

    public JunkCleanerItemDissDialogEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
