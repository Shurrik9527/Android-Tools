package com.hz.maiku.maikumodule.modules.wifimanager;

import android.content.Context;

import com.hz.maiku.maikumodule.bean.WifiBean;
import com.hz.maiku.maikumodule.util.WifiHelper;

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
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class WifiManagerPresenter implements WifiManagerContract.Presenter{

    private static final String TAG =WifiManagerPresenter.class.getName();
    private Context mContext;
    private WifiManagerContract.View mView=null;

    public WifiManagerPresenter(final WifiManagerContract.View view, Context context) {
        this.mView = view;
        this.mContext =context;
        mView.setPresenter(this);
    }


    @Override
    public void scanAllWifis() {

        Observable.create(new ObservableOnSubscribe<List<WifiBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<WifiBean>> e) throws Exception {
                e.onNext(WifiHelper.wifiScanResult(mContext));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<WifiBean>>() {
            @Override
            public void accept(List<WifiBean> wifiBeans) throws Exception {
                if(mView!=null){
                    mView.showAllWifi(wifiBeans);
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
