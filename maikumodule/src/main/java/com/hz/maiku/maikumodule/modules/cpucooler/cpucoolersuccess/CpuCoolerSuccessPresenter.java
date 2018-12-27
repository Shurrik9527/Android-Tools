package com.hz.maiku.maikumodule.modules.cpucooler.cpucoolersuccess;

import com.hz.maiku.maikumodule.util.CpuUtil;

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
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class CpuCoolerSuccessPresenter implements CpuCoolerSuccessContract.Presenter {
    private CpuCoolerSuccessContract.View view;


    public CpuCoolerSuccessPresenter(CpuCoolerSuccessContract.View view) {
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void startScanProcess() {
        Observable.create(new ObservableOnSubscribe<Float>() {
            @Override
            public void subscribe(ObservableEmitter<Float> e) throws Exception {
                e.onNext(CpuUtil.cpuAverageTemperature());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Float>() {
            @Override
            public void accept(Float aFloat) throws Exception {
                if(view!=null){
                    view.showTemperature(aFloat);
                }
            }
        });
    }
}
