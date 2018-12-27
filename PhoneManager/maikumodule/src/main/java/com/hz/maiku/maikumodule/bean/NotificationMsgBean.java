package com.hz.maiku.maikumodule.bean;

import android.app.Notification;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/19
 * @email 252774645@qq.com
 */
public class NotificationMsgBean extends DataSupport implements Comparable<NotificationMsgBean>{

//    private String titleStr;
//    private String contentStr;
//    private String AppId;
//    private String AppName;
//    private String AppIcon;
//    private String addTime;

    private String tickerText;
    private String titleStr;
    private String contentStr;
    private Notification mNotification;
    private String addTime;
    private String packageName;
    private String mid;
    private String mkey;
    private String  mtag;

    public String getTickerText() {
        return tickerText;
    }

    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getContentStr() {
        return contentStr;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }

    public Notification getmNotification() {
        return mNotification;
    }

    public void setmNotification(Notification mNotification) {
        this.mNotification = mNotification;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public String getMtag() {
        return mtag;
    }

    public void setMtag(String mtag) {
        this.mtag = mtag;
    }

    @Override
    public String toString() {
        return "NotificationMsgBean{" +
                "tickerText='" + tickerText + '\'' +
                ", titleStr='" + titleStr + '\'' +
                ", contentStr='" + contentStr + '\'' +
                ", mNotification=" + mNotification +
                ", addTime='" + addTime + '\'' +
                ", packageName='" + packageName + '\'' +
                ", mid='" + mid + '\'' +
                ", mkey='" + mkey + '\'' +
                ", mtag='" + mtag + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull NotificationMsgBean o) {

        try {
            long level1 =Long.parseLong(this.getAddTime());
            Long level2 = Long.parseLong(o.getAddTime());

            if(level1<level2){
                return 1;
            }else if(level1>level2){
                return -1;
            }else{
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }
}
