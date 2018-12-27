package com.hz.maiku.maikumodule.event;


import com.hz.maiku.maikumodule.bean.JunkCleanerGroupBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerTypeBean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理数据事件
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class JunkCleanerDataEvent {

    private JunkCleanerGroupBean junkCleanerGroupBean;
    private JunkCleanerTypeBean junkCleanerTypeBean;
    private int index;

    public JunkCleanerDataEvent(JunkCleanerTypeBean junkCleanerTypeBean, int index) {
        this.junkCleanerTypeBean = junkCleanerTypeBean;
        this.junkCleanerGroupBean = null;
        this.index = index;
    }

    public JunkCleanerDataEvent(JunkCleanerGroupBean junkCleanerGroupBean) {
        this.junkCleanerGroupBean = junkCleanerGroupBean;
        this.junkCleanerTypeBean = null;
        index = -1;
    }

    public JunkCleanerGroupBean getJunkCleanerGroupBean() {
        return junkCleanerGroupBean;
    }

    public void setJunkCleanerGroupBean(JunkCleanerGroupBean junkCleanerGroupBean) {
        this.junkCleanerGroupBean = junkCleanerGroupBean;
    }

    public JunkCleanerTypeBean getJunkCleanerTypeBean() {
        return junkCleanerTypeBean;
    }

    public void setJunkCleanerTypeBean(JunkCleanerTypeBean junkCleanerTypeBean) {
        this.junkCleanerTypeBean = junkCleanerTypeBean;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
