package com.doro4028.iggcleaner;

import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.hz.maiku.maikumodule.base.MaiKuApp;

/**
 * Created by Shurrik on 2018/12/26.
 */

public class App extends MaiKuApp {


    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
