package com.hz.maiku.maikumodule.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/11/20
 * @email 252774645@qq.com
 */
public class WifiBean implements Comparable<WifiBean>{

    private String wifiName; //名字
    private String level;//信号强度
    private String state;  //已连接  正在连接  未连接 三种状态
    private String capabilities;//加密方式
    private boolean isEnable;

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    @Override
    public String toString() {
        return "WifiBean{" +
                "wifiName='" + wifiName + '\'' +
                ", level='" + level + '\'' +
                ", state='" + state + '\'' +
                ", capabilities='" + capabilities + '\'' +
                ", isEnable=" + isEnable +
                '}';
    }

    @Override public int compareTo(WifiBean o) {
        int level1 = Integer.parseInt(this.getLevel());
        int level2 = Integer.parseInt(o.getLevel());
        if(level1<level2){
            return 1;
        }else if(level1>level2){
            return -1;
        }else{
            return 0;
        }
    }

}
