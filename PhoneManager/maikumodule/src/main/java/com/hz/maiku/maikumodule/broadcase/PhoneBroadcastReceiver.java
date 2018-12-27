package com.hz.maiku.maikumodule.broadcase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.hz.maiku.maikumodule.service.PhoneService;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 电话广播
 * @date 2018/10/19
 * @email 252774645@qq.com
 */
public class PhoneBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG =PhoneBroadcastReceiver.class.getName();
    private static final int flog =2;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG,"手机广播开启服务");
        if(!intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){ //去电操作
            Intent pit=new Intent(context,PhoneService.class);
            pit.putExtra("bool",flog);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(pit);
            }else {
                context.startService(pit);
            }
        }
    }
}
