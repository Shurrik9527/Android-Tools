package com.hz.maiku.maikumodule.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 运行app进程基类
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class AppProcessInfornBean implements Serializable{

    private String appName;                 //app名字
    private String processName;            //进程名字
    private int u_id;                       //分配的id
    private int p_id;                       //进程id
    private Drawable icon;                 //图标
    private long memory;                  //占用内存
    private boolean isSystem;            //是否是系统


    public AppProcessInfornBean(String processName, int u_id, int p_id) {
        this.processName = processName;
        this.u_id = u_id;
        this.p_id = p_id;
    }

    public AppProcessInfornBean() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getU_id() {
        return u_id;
    }

    public void setU_id(int u_id) {
        this.u_id = u_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    @Override
    public String toString() {
        return "AppProcessInfornBean{" +
                "appName='" + appName + '\'' +
                ", processName='" + processName + '\'' +
                ", u_id=" + u_id +
                ", p_id=" + p_id +
                ", icon=" + icon +
                ", memory=" + memory +
                ", isSystem=" + isSystem +
                '}';
    }


}
