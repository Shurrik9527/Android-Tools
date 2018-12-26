package com.down2588.phonemanager;

import android.app.Application;

/**
 * Created by Shurrik on 2018/12/26.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        initServices();
        initAppsFlyer();
        initBaiduCrab();
    }

    /**
     * 初始化服务
     */
    private void initServices() {

    }

    /**
     * 初始化AppsFlyer
     */
    private void initAppsFlyer() {

    }

    /**
     * 初始化百度Crab
     */
    private void initBaiduCrab() {

    }

}
