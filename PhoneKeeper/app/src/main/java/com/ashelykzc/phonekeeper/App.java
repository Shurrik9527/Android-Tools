package com.ashelykzc.phonekeeper;

import android.support.multidex.MultiDex;

import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.base.MaiKuApp;
import com.hz.maiku.maikumodule.util.SpHelper;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/14
 * @email 252774645@qq.com
 */
public class App extends MaiKuApp {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        //regist time
        long mTime = (long) SpHelper.getInstance().get(Constant.REGIST_TIME,0L);
        if(mTime==0L){
            SpHelper.getInstance().put(Constant.REGIST_TIME,System.currentTimeMillis());
        }
    }
}
