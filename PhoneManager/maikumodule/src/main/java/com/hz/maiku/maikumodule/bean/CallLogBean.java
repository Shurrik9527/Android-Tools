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
public class CallLogBean extends DataSupport implements Serializable {

    private String phoneNum;//通话号码
    private String matched_number;
    private String time;//通话时间
    private boolean isBack;//是否是黑户
    private String flog;//标记
    private String num;//通话次数
    private String remacks;//描述
    private String nick;//昵称
    private String fromSource;//来源
    private String type;//类型 呼入 呼出 未接
    private String icon;//头像
    private String iswrite;
    private String mode;//模式
    private String location;//运营商
    private String duration;//通话时间

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getMatched_number() {
        return matched_number;
    }

    public void setMatched_number(String matched_number) {
        this.matched_number = matched_number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getRemacks() {
        return remacks;
    }

    public void setRemacks(String remacks) {
        this.remacks = remacks;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getFromSource() {
        return fromSource;
    }

    public void setFromSource(String fromSource) {
        this.fromSource = fromSource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIswrite() {
        return iswrite;
    }

    public void setIswrite(String iswrite) {
        this.iswrite = iswrite;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CallLogBean{" +
                "phoneNum='" + phoneNum + '\'' +
                ", matched_number='" + matched_number + '\'' +
                ", time='" + time + '\'' +
                ", isBack=" + isBack +
                ", flog='" + flog + '\'' +
                ", num='" + num + '\'' +
                ", remacks='" + remacks + '\'' +
                ", nick='" + nick + '\'' +
                ", fromSource='" + fromSource + '\'' +
                ", type='" + type + '\'' +
                ", icon='" + icon + '\'' +
                ", iswrite='" + iswrite + '\'' +
                ", mode='" + mode + '\'' +
                ", location='" + location + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
