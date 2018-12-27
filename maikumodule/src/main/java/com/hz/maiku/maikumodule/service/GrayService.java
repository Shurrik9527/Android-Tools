package com.hz.maiku.maikumodule.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class GrayService extends Service {

    private final static String TAG = GrayService.class.getSimpleName();
    private final static int GRAY_SERVICE_ID = 1001;
    private final static String CHANNEL_ONE_ID = "com.hz.maiku.maikumodule.service";
    private final static String CHANNEL_ONE_NAME = "ChannelOne";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
            Log.d(TAG, "onStartCommand < 18");
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            //8.0 以后 通知、服务 需要单独处理
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //开启服务
                startForegroundService(innerIntent);
                //通知
                NotificationChannel channel = new NotificationChannel(CHANNEL_ONE_ID,CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_LOW);
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ONE_ID).build();
                startForeground(GRAY_SERVICE_ID, notification);
            } else {
                startService(innerIntent);
                startForeground(GRAY_SERVICE_ID, new Notification());
            }
            Log.d(TAG, "onStartCommand");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return null;
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ONE_ID,CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_LOW);
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ONE_ID).build();
                startForeground(GRAY_SERVICE_ID, notification);
            } else {
                startForeground(GRAY_SERVICE_ID, new Notification());
            }

            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

    }
}