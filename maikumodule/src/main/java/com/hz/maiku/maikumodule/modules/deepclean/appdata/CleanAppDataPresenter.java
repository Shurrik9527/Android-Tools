package com.hz.maiku.maikumodule.modules.deepclean.appdata;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.AppBean;
import com.hz.maiku.maikumodule.bean.AppDataBean;
import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;
import com.hz.maiku.maikumodule.modules.deepclean.DeepCleanActivity;
import com.hz.maiku.maikumodule.modules.deepclean.selectaudio.SelectAudiosContract;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.DeepCleanUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/11/30
 * @email 252774645@qq.com
 */
public class CleanAppDataPresenter implements CleanAppDataContract.Presenter{


    private static final String TAG =CleanAppDataPresenter.class.getName();
    private Context mContext;
    private CleanAppDataContract.View mView=null;

    public CleanAppDataPresenter(final CleanAppDataContract.View view, Context context) {
        this.mView = view;
        this.mContext =context;
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    @Override
    public void getAppDatas(final List<AppBean> lists) {

        LoadingDialogManager.show(mContext);
        Observable.create(new ObservableOnSubscribe<List<AppDataBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void subscribe(ObservableEmitter<List<AppDataBean>> e) throws Exception {
                e.onNext(DeepCleanUtil.getAllSpecialApk(lists));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<AppDataBean>>() {
            @Override
            public void accept(List<AppDataBean> mAppDataBean) throws Exception {
                LoadingDialogManager.hideProgressDialog();
                if(mView!=null){
                    mView.showAppData(mAppDataBean);
                }
            }
        });

    }

    @Override
    public void getData() {
        Observable.create(new ObservableOnSubscribe<List<AppBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AppBean>> e) throws Exception {
                e.onNext(AppUtil.getSpecialApp(mContext));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<AppBean>>() {
            @Override
            public void accept(List<AppBean> appBeans) throws Exception {
                if(mView!=null){
                    mView.showData(appBeans);
                }
            }
        });
    }
}
