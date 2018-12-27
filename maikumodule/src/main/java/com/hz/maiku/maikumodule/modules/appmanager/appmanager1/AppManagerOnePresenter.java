package com.hz.maiku.maikumodule.modules.appmanager.appmanager1;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.AppManagerBean;
import com.hz.maiku.maikumodule.event.UninstallEvent;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;
import com.hz.maiku.maikumodule.modules.appmanager.AppManagerContract;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.RxBus.RxBusHelper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class AppManagerOnePresenter implements AppManagerOneContract.Presenter {
    private AppManagerOneContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    public AppManagerOnePresenter(AppManagerOneContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

    @Override
    public void loadData(final Context context) {

        if(context==null){
            return;
        }
        LoadingDialogManager.show(context);
        Observable.create(new ObservableOnSubscribe<List<ApplicationInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<ApplicationInfo>> e) throws Exception {
                e.onNext(AppUtil.getUnInstalledApplicationInfo(context));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ApplicationInfo>>() {
            @Override
            public void accept(List<ApplicationInfo> applicationInfos) throws Exception {
                LoadingDialogManager.hideProgressDialog();
                if(mView!=null&&applicationInfos!=null&&applicationInfos.size()>0){
                    List<AppManagerBean> mlists = new ArrayList<>();
                    for (ApplicationInfo applicationInfo :applicationInfos){
                        if(applicationInfo.packageName.equals(context.getPackageName())){
                           continue;
                        }
                        AppManagerBean bean =new AppManagerBean();
                        bean.setApplicationInfo(applicationInfo);
                        bean.setSelect(false);
                        mlists.add(bean);
                    }
                    mView.showAppData(mlists);
                }
            }
        });

    }
}
