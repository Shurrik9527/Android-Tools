package com.hz.maiku.maikumodule.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/18
 * @email 252774645@qq.com
 */
public class SmsBean extends DataSupport implements Serializable {


    private String address;
    private String person;
    private String date;
    private String read;
    private String status;
    private String type;
    private String body;
    private boolean isBack;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    @Override
    public String toString() {
        return "SmsBean{" +
                "address='" + address + '\'' +
                ", person='" + person + '\'' +
                ", date='" + date + '\'' +
                ", read='" + read + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", body='" + body + '\'' +
                ", isBack=" + isBack +
                '}';
    }
}
