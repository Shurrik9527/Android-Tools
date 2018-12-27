package com.hz.maiku.maikumodule.modules.trafficstatis;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.hz.maiku.maikumodule.bean.TrafficStatisBean;
import com.hz.maiku.maikumodule.bean.WifiMobileBean;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;
import com.hz.maiku.maikumodule.util.TrafficStatisUtil;
import com.hz.maiku.maikumodule.widget.dialog.LoadingDialog;

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
public class TrafficStatisPresenter implements TrafficStatisContract.Presenter{


    private static final String TAG =TrafficStatisPresenter.class.getName();
    private Context mContext;
    private TrafficStatisContract.View mView=null;
    @RequiresApi(api = Build.VERSION_CODES.M)
    public TrafficStatisPresenter(final TrafficStatisContract.View view, Context context) {
        this.mView = view;
        this.mContext =context;
        mView.setPresenter(this);
    }



    @Override
    public void getMonthTrafficStatistics() {

        LoadingDialogManager.show(mContext);
        Observable.create(new ObservableOnSubscribe<List<TrafficStatisBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void subscribe(ObservableEmitter<List<TrafficStatisBean>> e) throws Exception {

                List<TrafficStatisBean> mlist = TrafficStatisUtil.getAppTrafficStatistics(mContext,true);
                if(mlist!=null){
                    e.onNext(mlist);
                }else{
                    e.onError(new Throwable("List<TrafficStatisticsBean> is null"));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<TrafficStatisBean>>() {
            @Override
            public void accept(List<TrafficStatisBean> trafficStatisticsBeans) throws Exception {
                LoadingDialogManager.hideProgressDialog();
                if(mView!=null){
                    mView.showData(trafficStatisticsBeans);
                }
            }
        });

    }

    @Override
    public void getDayTrafficStatistics() {
        Observable.create(new ObservableOnSubscribe<List<TrafficStatisBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void subscribe(ObservableEmitter<List<TrafficStatisBean>> e) throws Exception {

                List<TrafficStatisBean> mlist = TrafficStatisUtil.getAppTrafficStatistics(mContext,false);
                if(mlist!=null){
                    e.onNext(mlist);
                }else{
                    e.onError(new Throwable("List<TrafficStatisticsBean> is null"));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<TrafficStatisBean>>() {
            @Override
            public void accept(List<TrafficStatisBean> trafficStatisticsBeans) throws Exception {
                if(mView!=null){
                    mView.showData(trafficStatisticsBeans);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void getMonthSize() {

        Observable.create(new ObservableOnSubscribe<WifiMobileBean>() {
            @Override
            public void subscribe(ObservableEmitter<WifiMobileBean> e) throws Exception {
               e.onNext(TrafficStatisUtil.getTotalTrafficStatistics(mContext,true));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<WifiMobileBean>() {
            @Override
            public void accept(WifiMobileBean bean) throws Exception {
                if(mView!=null){
                    mView.showAllTraffic(bean);
                }
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void getTodaySize() {
        Observable.create(new ObservableOnSubscribe<WifiMobileBean>() {
            @Override
            public void subscribe(ObservableEmitter<WifiMobileBean> e) throws Exception {
                e.onNext(TrafficStatisUtil.getTotalTrafficStatistics(mContext,false));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<WifiMobileBean>() {
            @Override
            public void accept(WifiMobileBean bean) throws Exception {
                if(mView!=null){
                    mView.showAllTraffic(bean);
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
