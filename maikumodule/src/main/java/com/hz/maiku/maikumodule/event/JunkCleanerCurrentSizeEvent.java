package com.hz.maiku.maikumodule.event;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 当前垃圾数目 事件
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class JunkCleanerCurrentSizeEvent {

    private long totalSize;

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public JunkCleanerCurrentSizeEvent(long totalSize) {
        this.totalSize = totalSize;
    }
}
