package com.hz.maiku.maikumodule.modules.deepclean;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.AlbumBean;
import com.hz.maiku.maikumodule.bean.ApkBean;
import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.bean.TrafficStatisBean;
import com.hz.maiku.maikumodule.bean.VideoBean;
import com.hz.maiku.maikumodule.bean.WifiMobileBean;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;
import com.hz.maiku.maikumodule.modules.trafficstatis.TrafficStatisContract;
import com.hz.maiku.maikumodule.util.DeepCleanUtil;
import com.hz.maiku.maikumodule.util.TrafficStatisUtil;

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
                    mView.showImageData(mAlbumBean);
                }
            }
        });

    }

    @Override
    public void getVideos() {
        Observable.create(new ObservableOnSubscribe<List<VideoBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void subscribe(ObservableEmitter<List<VideoBean>> e) throws Exception {
                e.onNext(DeepCleanUtil.getAllVideo(mContext));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<VideoBean>>() {
            @Override
            public void accept(List<VideoBean> mVideoBean) throws Exception {
                if(mView!=null){
                    mView.showVideos(mVideoBean);
                }
            }
        });
    }

    @Override
    public void getUnInstallApk() {
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
                if(mView!=null){
                    mView.showApks(mApkBean);
                }
            }
        });
    }

    @Override
    public void getAudios() {

//        Observable.create(new ObservableOnSubscribe<List<AudioBean>>() {
//            @Override
//            public void subscribe(ObservableEmitter<List<AudioBean>> e) throws Exception {
//                e.onNext(DeepCleanUtil.getAllAudios());
//                e.onComplete();
//            }
//        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<AudioBean>>() {
//            @Override
//            public void accept(List<AudioBean> mAudioBean) throws Exception {
//                if(mView!=null){
//                    mView.showAudios(mAudioBean);
//                }
//            }
//        });

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
                if(mView!=null){
                    mView.showAudios(audioBeans);
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
