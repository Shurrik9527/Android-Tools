package com.hz.maiku.maikumodule.modules.screenlocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BatteryReceiver extends BroadcastReceiver {
    private ScreenLockerContract.View view;

    public BatteryReceiver(ScreenLockerContract.View view) {
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int current = intent.getExtras().getInt("level");// 获得当前电量
        int total = intent.getExtras().getInt("scale");// 获得总电量
        int percent = current * 100 / total;
        view.showBatteryInfo(percent);
//        int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
//        if(status == BatteryManager.BATTERY_STATUS_CHARGING) {
//            view.showChargeStatus(true);
//        } else {
//            view.showChargeStatus(false);
//        }
    }
}
