package com.hz.maiku.maikumodule.event;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理进度条事件
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class JunkCleanerShowDialogEvent {
    private int index;

    public JunkCleanerShowDialogEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
