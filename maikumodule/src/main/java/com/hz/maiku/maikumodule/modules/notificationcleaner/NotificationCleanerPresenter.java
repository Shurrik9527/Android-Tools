package com.hz.maiku.maikumodule.modules.notificationcleaner;


import android.content.Context;

import com.hz.maiku.maikumodule.bean.NotificationMsgBean;
import com.hz.maiku.maikumodule.manager.NotificationsManager;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/19
 * @email 252774645@qq.com
 */
public class NotificationCleanerPresenter implements NotificationCleanerContract.Presenter{

    private static final String TAG =NotificationCleanerPresenter.class.getName();
    private Context mContext;
    private NotificationCleanerContract.View mView=null;

    public NotificationCleanerPresenter(final NotificationCleanerContract.View view, Context context) {
        this.mView = view;
        this.mContext =context;
        mView.setPresenter(this);
    }


    @Override
    public void showAllApps() {

        Observable.create(new ObservableOnSubscribe<List<NotificationMsgBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NotificationMsgBean>> e) throws Exception {
                e.onNext(NotificationsManager.getmInstance().getNotificationInfos());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<NotificationMsgBean>>() {
            @Override
            public void accept(List<NotificationMsgBean> mNotificationMsgBean) throws Exception {
                if(mView!=null){
                    //排序
                    Collections.sort(mNotificationMsgBean);
                    mView.showAllMsg(mNotificationMsgBean);
                }
            }
        });

    }
}
