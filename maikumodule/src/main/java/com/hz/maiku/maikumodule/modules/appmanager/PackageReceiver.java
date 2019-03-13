package com.hz.maiku.maikumodule.modules.appmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hz.maiku.maikumodule.event.UninstallEvent;
import com.hz.maiku.maikumodule.util.AdUtil;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.RxBus.RxBus;

public class PackageReceiver extends BroadcastReceiver {
    public PackageReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            //应用卸载以后通知App Manager列表刷新
            RxBus.getDefault().post(new UninstallEvent());

            if(!AppUtil.isAppBackground(context)){
                if (AdUtil.IS_SHOW_AD) {
                    //广告
                    AdUtil.showAds(context, "PackageReceiver.onReceive()");
                    //当应用程序图标被隐藏时使用下面代码启动FacebookAd
//                Intent emptyIntent = new Intent();
//                emptyIntent.setClass(context, EmptyActivity.class);
//                emptyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(emptyIntent);
                }
            }
        }
    }
}
