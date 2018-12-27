package com.hz.maiku.maikumodule.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/17
 * @email 252774645@qq.com
 */
public class AddressListBean extends DataSupport implements Serializable {

    private String phoneNum;//号码
    private boolean isBack;//是否是黑户
    private String flog;//标记
    private String nick;//昵称
    private String icon;//头像
    private String location;//运营商

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    public String getFlog() {
        return flog;
    }

    public void setFlog(String flog) {
        this.flog = flog;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "AddressListBean{" +
                "phoneNum='" + phoneNum + '\'' +
                ", isBack=" + isBack +
                ", flog='" + flog + '\'' +
                ", nick='" + nick + '\'' +
                ", icon='" + icon + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
