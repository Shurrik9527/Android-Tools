package com.quintinwus.deviceprotector;

import android.support.multidex.MultiDex;

import com.hz.maiku.maikumodule.base.MaiKuApp;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/20
 * @email 252774645@qq.com
 */
public class App extends MaiKuApp {


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

    }
}
