package com.hz.maiku.maikumodule.modules.deepclean.appdata.appaudio;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;
import com.hz.maiku.maikumodule.util.DeepCleanUtil;

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
public class AppAudiosPresenter implements AppAudiosContract.Presenter{


    private static final String TAG =AppAudiosPresenter.class.getName();
    private Context mContext;
    private AppAudiosContract.View mView=null;

    public AppAudiosPresenter(final AppAudiosContract.View view, Context context) {
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
    public void delectAudiosList(final List<AudioBean> mlists, final long mSize) {

        DeepCleanUtil.deleteAllAudiosObservable(mlists).zipWith(DeepCleanUtil.deleteAllAudiosObservable(mContext, mlists), new BiFunction<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                return true;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if(aBoolean){
                    if(mView!=null){
                        mView.cleanSuccess(mSize);
                    }
                }
            }
        });

    }

    @Override
    public void getAudios(final String content) {

        LoadingDialogManager.show(mContext);
        Observable.create(new ObservableOnSubscribe<List<AudioBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void subscribe(ObservableEmitter<List<AudioBean>> e) throws Exception {
                e.onNext(DeepCleanUtil.getAllAudioSpecialApp(content));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<AudioBean>>() {
            @Override
            public void accept(List<AudioBean> list) throws Exception {
                LoadingDialogManager.hideProgressDialog();
                if(mView!=null){
                    mView.showAudiosData(list);
                }
            }
        });
    }
}
