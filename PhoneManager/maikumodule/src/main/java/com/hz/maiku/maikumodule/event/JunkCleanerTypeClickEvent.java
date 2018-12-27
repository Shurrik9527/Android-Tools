package com.hz.maiku.maikumodule.event;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理点击事件
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class JunkCleanerTypeClickEvent {

    private boolean isExpand;
    private int position;

    public JunkCleanerTypeClickEvent(boolean isExpand, int position) {
        this.isExpand = isExpand;
        this.position = position;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
