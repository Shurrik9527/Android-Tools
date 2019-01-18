package com.hz.maiku.maikumodule.modules.deepclean.selectbigfile;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.AlbumBean;
import com.hz.maiku.maikumodule.bean.BigFileBean;
import com.hz.maiku.maikumodule.bean.ImageBean;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;
import com.hz.maiku.maikumodule.modules.deepclean.selectImage.SelectImageContract;
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
public class SelectBigFilePresenter implements SelectBigFileContract.Presenter{


    private static final String TAG =SelectBigFilePresenter.class.getName();
    private Context mContext;
    private SelectBigFileContract.View mView=null;

    public SelectBigFilePresenter(final SelectBigFileContract.View view, Context context) {
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
    public void delectBigFileList(final List<BigFileBean> mlists, final long mSize) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(DeepCleanUtil.deleteBigFileFile(mlists));
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
    public void getBigFiles() {
        LoadingDialogManager.show(mContext);
        Observable.create(new ObservableOnSubscribe<List<BigFileBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BigFileBean>> e) throws Exception {
                e.onNext(DeepCleanUtil.getAllBigFiles());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<BigFileBean>>() {
            @Override
            public void accept(List<BigFileBean> mBigFileBean) throws Exception {
                LoadingDialogManager.hideProgressDialog();
                if(mView!=null){
                    mView.showData(mBigFileBean);
                }
            }
        });
    }
}
