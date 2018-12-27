package com.hz.maiku.maikumodule.bean;

import org.litepal.crud.DataSupport;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 推荐加锁的应用
 * @date 2018/10/11
 * @email 252774645@qq.com
 */
public class FaviterInfo extends DataSupport {
    public String packageName;

    public FaviterInfo() {
    }

    public FaviterInfo(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
