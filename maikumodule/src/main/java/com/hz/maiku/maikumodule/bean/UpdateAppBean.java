package com.hz.maiku.maikumodule.bean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/5/13
 * @email 252774645@qq.com
 */
public class UpdateAppBean {

    private int isUpdate;
    private String updateInfo;
    private String newPackageName;
    private String appname;

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    public String getNewPackageName() {
        return newPackageName;
    }

    public void setNewPackageName(String newPackageName) {
        this.newPackageName = newPackageName;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    @Override
    public String toString() {
        return "UpdateAppBean{" +
                "isUpdate=" + isUpdate +
                ", updateInfo='" + updateInfo + '\'' +
                ", newPackageName='" + newPackageName + '\'' +
                ", appname='" + appname + '\'' +
                '}';
    }
}
