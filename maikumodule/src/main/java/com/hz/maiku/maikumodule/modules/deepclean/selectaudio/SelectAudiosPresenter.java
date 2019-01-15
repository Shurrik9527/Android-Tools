package com.hz.maiku.maikumodule.modules.deepclean.selectaudio;

import android.content.Context;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;
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
public class SelectAudiosPresenter implements SelectAudiosContract.Presenter{


    private static final String TAG =SelectAudiosPresenter.class.getName();
    private Context mContext;
    private SelectAudiosContract.View mView=null;

    public SelectAudiosPresenter(final SelectAudiosContract.View view, Context context) {
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
    public void getAudios() {

        LoadingDialogManager.show(mContext);
        final List<AudioBean> mLists = new ArrayList<>();
        DeepCleanUtil.getAllAudiosObservable().zipWith(DeepCleanUtil.getAllAudiosObservable(mContext), new BiFunction<List<AudioBean>, List<AudioBean>, List<AudioBean>>() {
            @Override
            public List<AudioBean> apply(List<AudioBean> audioBeans, List<AudioBean> audioBeans2) throws Exception {
                if(audioBeans!=null&&audioBeans.size()>0){
                    mLists.addAll(audioBeans);
                }
                if(audioBeans2!=null&&audioBeans2.size()>0){
                    mLists.addAll(audioBeans2);
                }
                return mLists;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<AudioBean>>() {
            @Override
            public void accept(List<AudioBean> audioBeans) throws Exception {
                LoadingDialogManager.hideProgressDialog();
                if(mView!=null){
                    mView.showAudiosData(audioBeans);
                }
            }
        });

    }
}
