package com.hz.maiku.maikumodule.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 隐藏图标 服务
 * @date 2018/11/30
 * @email 252774645@qq.com
 */
public class HideAppService extends IntentService {
    private static final String TAG = HideAppService.class.getName();

    public HideAppService() {
        super("HideAppService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_LOW);
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(getApplicationContext(), "id").build();
                startForeground(100, notification);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 延迟1s
                        SystemClock.sleep(1000);
                        stopForeground(true);
                        // 移除Service弹出的通知
                        manager.cancel(100);
                    }
                }).start();
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //12小时后
        long delay = 1000 * 60 * 60 * 12;
        SystemClock.sleep(delay);
//        try {
//            //影藏图标
//            PackageManager pm = getPackageManager();
//            pm.setComponentEnabledSetting(new ComponentName(Constant.PACKAGE_NAME,
//                    Constant.PACKAGE_NAME + ".main.MainActivity"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//
//        } catch (Exception e) {
//
//        }
        String message = "installed 12h later";
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put(AFInAppEventParameterName.CONTENT, message);
        AppsFlyerLib.getInstance().trackEvent(HideAppService.this, AFInAppEventType.LOGIN, eventValues);
        Log.d(TAG, message);
        //24小时后
        SystemClock.sleep(delay);
        message = "installed 24h later";
        eventValues.clear();
        eventValues.put(AFInAppEventParameterName.CONTENT, message);
        AppsFlyerLib.getInstance().trackEvent(HideAppService.this, AFInAppEventType.LOGIN, eventValues);
        Log.d(TAG, message);
        //48小时后
        SystemClock.sleep(delay * 2);
        message = "installed 48h later";
        eventValues.clear();
        eventValues.put(AFInAppEventParameterName.CONTENT, message);
        AppsFlyerLib.getInstance().trackEvent(HideAppService.this, AFInAppEventType.LOGIN, eventValues);
        Log.d(TAG, message);
    }
}
