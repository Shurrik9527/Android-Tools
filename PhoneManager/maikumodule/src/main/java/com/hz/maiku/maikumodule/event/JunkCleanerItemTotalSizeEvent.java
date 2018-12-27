package com.hz.maiku.maikumodule.event;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 某项垃圾总数事件
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class JunkCleanerItemTotalSizeEvent {

    private int index;
    private String totalSize;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public JunkCleanerItemTotalSizeEvent(int index, String totalSize) {
        this.index = index;
        this.totalSize = totalSize;
    }
}
