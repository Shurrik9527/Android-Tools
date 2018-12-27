package com.hz.maiku.maikumodule.modules.cpucooler;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;
import com.hz.maiku.maikumodule.manager.ProcessManager;
import com.hz.maiku.maikumodule.util.CpuUtil;

import java.util.ArrayList;
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

public class CpuCoolerPresenter implements CpuCoolerContract.Presenter {
    private CpuCoolerContract.View view;


    public CpuCoolerPresenter(CpuCoolerContract.View view) {
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

    @Override
    public void startCleanApp(List<AppProcessInfornBean> appProcessInfornBeans) {

        if(appProcessInfornBeans==null||appProcessInfornBeans.size()==0){
            return;
        }
        List<String> mLists = new ArrayList<>();
        for (int i=0;i<appProcessInfornBeans.size();i++){
            mLists.add(appProcessInfornBeans.get(i).getProcessName());
        }

        if(mLists.size()==0){
            return;
        }

        ProcessManager.getInstance().killListsRunningAppObservale(mLists).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if(view!=null){
                            view.cleanFinish();
                        }
                    }
                });
    }
}
