package com.sharkwang8.phoneassistant;

import android.support.multidex.MultiDex;

import com.hz.maiku.maikumodule.base.MaiKuApp;

/**
 * Created by Shurrik on 2018/12/26.
 */

public class App extends MaiKuApp {


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }

}
