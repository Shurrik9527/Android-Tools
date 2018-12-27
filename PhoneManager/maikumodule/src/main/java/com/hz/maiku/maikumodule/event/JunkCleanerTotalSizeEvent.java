package com.hz.maiku.maikumodule.event;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 暂用大小 事件
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class JunkCleanerTotalSizeEvent {

    private String totalSize;

    public JunkCleanerTotalSizeEvent(String totalSize) {
        this.totalSize = totalSize;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

}
