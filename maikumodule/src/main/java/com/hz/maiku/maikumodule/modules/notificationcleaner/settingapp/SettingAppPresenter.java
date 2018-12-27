package com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp;

import android.content.Context;
import com.hz.maiku.maikumodule.bean.NotificationBean;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;
import com.hz.maiku.maikumodule.manager.NotificationsManager;
import com.hz.maiku.maikumodule.util.AppUtil;
import java.util.ArrayList;
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
 * @date 2018/12/21
 * @email 252774645@qq.com
 */
public class SettingAppPresenter implements SettingAppConstract.Presenter{

    private static final String TAG =SettingAppPresenter.class.getName();
    private Context mContext;
    private SettingAppConstract.View mView=null;

    public SettingAppPresenter(final SettingAppConstract.View view,Context context) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mContext =context;
    }

    @Override
    public void subscribe() {


        LoadingDialogManager.show(mContext);

        Observable.create(new ObservableOnSubscribe<List<NotificationBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NotificationBean>> e) throws Exception {
                e.onNext(NotificationsManager.getmInstance().getAllNotificationAppInfos());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<NotificationBean>>() {
            @Override
            public void accept(List<NotificationBean> mNotificationlists) throws Exception {
               LoadingDialogManager.hideProgressDialog();
                if(mView!=null&&mNotificationlists!=null&&mNotificationlists.size()>0){

                    List<NotificationBean> blockedNotication = new ArrayList<>();
                    List<NotificationBean> allowedNotication = new ArrayList<>();

                    for (NotificationBean mNotificationBean : mNotificationlists){
                        if(mNotificationBean.isSystem()){
                            allowedNotication.add(mNotificationBean);
                        }else{
                            blockedNotication.add(mNotificationBean);
                        }
                    }

                    if(blockedNotication.size()>0){
                        mView.showBlockData(blockedNotication);
                    }

                    if(allowedNotication.size()>0){
                        mView.showAllowData(allowedNotication);
                    }

                }
            }
        });

    }

    @Override
    public void unsubscribe() {

    }

}
