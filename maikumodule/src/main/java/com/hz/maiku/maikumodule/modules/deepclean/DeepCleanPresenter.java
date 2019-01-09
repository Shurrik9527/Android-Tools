package com.hz.maiku.maikumodule.modules.deepclean;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.hz.maiku.maikumodule.bean.AlbumBean;
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
public class DeepCleanPresenter implements DeepCleanContract.Presenter{


    private static final String TAG =DeepCleanPresenter.class.getName();
    private Context mContext;
    private DeepCleanContract.View mView=null;

    public DeepCleanPresenter(final DeepCleanContract.View view, Context context) {
        this.mView = view;
        this.mContext =context;
        mView.setPresenter(this);
    }


    @Override
    public void getImages() {

        Observable.create(new ObservableOnSubscribe<List<AlbumBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void subscribe(ObservableEmitter<List<AlbumBean>> e) throws Exception {
                e.onNext(DeepCleanUtil.allAlbumFromLocalStorage(mContext));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<AlbumBean>>() {
            @Override
            public void accept(List<AlbumBean> mAlbumBean) throws Exception {
                if(mView!=null){
                    mView.showData(mAlbumBean);
                }
            }
        });

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
