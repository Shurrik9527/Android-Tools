package com.hz.maiku.maikumodule.modules.deepclean.uninstallapk;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.hz.maiku.maikumodule.bean.ApkBean;
import com.hz.maiku.maikumodule.bean.VideoBean;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;
import com.hz.maiku.maikumodule.modules.deepclean.selectVideo.SelectVideoContract;
import com.hz.maiku.maikumodule.util.DeepCleanUtil;

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
 * @date 2018/11/30
 * @email 252774645@qq.com
 */
public class SelectApkPresenter implements SelectApkContract.Presenter{


    private static final String TAG =SelectApkPresenter.class.getName();
    private Context mContext;
    private SelectApkContract.View mView=null;

    public SelectApkPresenter(final SelectApkContract.View view, Context context) {
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
    public void delectApkList(final List<ApkBean> mlists, final long mSize) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(DeepCleanUtil.deleteApkFile(mlists));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean state) throws Exception {
                if(state){
                    if(mView!=null){
                        mView.cleanSuccess(mSize);
                    }
                }
            }
        });
    }

    @Override
    public void getUnInstallApk() {
        LoadingDialogManager.show(mContext);
        Observable.create(new ObservableOnSubscribe<List<ApkBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void subscribe(ObservableEmitter<List<ApkBean>> e) throws Exception {
                e.onNext(DeepCleanUtil.getAllUnInstallApk());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ApkBean>>() {
            @Override
            public void accept(List<ApkBean> mApkBean) throws Exception {
                LoadingDialogManager.hideProgressDialog();
                if(mView!=null){
                    mView.showApkData(mApkBean);
                }
            }
        });
    }

}
