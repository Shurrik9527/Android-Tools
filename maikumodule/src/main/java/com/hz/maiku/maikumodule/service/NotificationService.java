package com.hz.maiku.maikumodule.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.NotificationMsgBean;
import com.hz.maiku.maikumodule.manager.NotificationsManager;
import com.hz.maiku.maikumodule.util.NotificationsCleanerUtil;
import com.hz.maiku.maikumodule.util.SpHelper;

import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/19
 * @email 252774645@qq.com
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationService extends NotificationListenerService {
    private  static final String TAG =NotificationService.class.getName();

    @Override
    public void onNotificationPosted(final StatusBarNotification sbn) {

        Observable.create(new ObservableOnSubscribe<StatusBarNotification[]>() {
            @Override
            public void subscribe(ObservableEmitter<StatusBarNotification[]> e) throws Exception {
                e.onNext(getActiveNotifications());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<StatusBarNotification[]>() {
            @Override
            public void accept(final StatusBarNotification[] statusBarNotifications) throws Exception {
                if(statusBarNotifications!=null&&statusBarNotifications.length>0){
                    if(getApplicationContext()!=null){
                        boolean openstate = (boolean) SpHelper.getInstance().get(Constant.NOTIFICATION_OPEN_STATE,false);
                        if(openstate){
                            Observable.create(new ObservableOnSubscribe<Boolean>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
                                @Override
                                public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                    e.onNext(NotificationsCleanerUtil.saveAllNotification(getApplicationContext(),statusBarNotifications));
                                }
                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                                @Override
                                public void accept(Boolean mboolean) throws Exception {

                                }
                            });

                            if(NotificationsManager.getmInstance().isSettingApp(sbn.getPackageName())){
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    cancelNotification(sbn.getKey());
                                }else {
                                    cancelNotification(sbn.getPackageName(),sbn.getTag(),sbn.getId());
                                }
                            }
                        }
                    }
                }
            }
        });

    }



    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"removed"+sbn.getPackageName());
        super.onNotificationRemoved(sbn);
    }


}
