package com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;
import com.hz.maiku.maikumodule.manager.ProcessManager;
import com.hz.maiku.maikumodule.util.CpuUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class CpuCoolerScanPresenter implements CpuCoolerScanContract.Presenter {
    private CpuCoolerScanContract.View view;


    public CpuCoolerScanPresenter(CpuCoolerScanContract.View view) {
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void getProcessRunningApp() {

        ProcessManager.getInstance().getRunningAppListObservable(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AppProcessInfornBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(List<AppProcessInfornBean> appProcessInfornBeans) {
                       view.showProcessRunning(appProcessInfornBeans);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void startScanProcessRunningApp() {

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void runAppProcessInform() {
        ProcessManager.getInstance().runAppProcessInformObservable().subscribe(new Consumer<List<AppProcessInfornBean>>() {
            @Override
            public void accept(List<AppProcessInfornBean> appProcessInfornBeans) throws Exception {
                view.showProcessRunning(appProcessInfornBeans);
            }
        });
    }

}
