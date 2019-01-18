package com.hz.maiku.maikumodule.modules.deepclean.appdata.appvideo;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

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
public class AppVideoPresenter implements AppVideoContract.Presenter{


    private static final String TAG =AppVideoPresenter.class.getName();
    private Context mContext;
    private AppVideoContract.View mView=null;

    public AppVideoPresenter(final AppVideoContract.View view, Context context) {
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
    public void delectVideoList(final List<VideoBean> mlists, final long mSize) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(DeepCleanUtil.deleteVideoFile(mContext,mlists));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean state) throws Exception {
                if(state){
                    if(mView!=null){
                        mView.cleanSuccess(mSize);
                    }
                    for (int i=0;i<mlists.size();i++){
                        VideoBean bean =mlists.get(i);
                        DeepCleanUtil.updateMediaStore(mContext,bean.getmUrl());
                    }
                }
            }
        });
    }

    @Override
    public void getVideos(final String content) {
        LoadingDialogManager.show(mContext);
        Observable.create(new ObservableOnSubscribe<List<VideoBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void subscribe(ObservableEmitter<List<VideoBean>> e) throws Exception {
                e.onNext(DeepCleanUtil.getAllVideoSpecialApp(content));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<VideoBean>>() {
            @Override
            public void accept(List<VideoBean> mVideoBean) throws Exception {
                LoadingDialogManager.hideProgressDialog();
                if(mView!=null){
                    mView.showVideoData(mVideoBean);
                }
            }
        });
    }
}
