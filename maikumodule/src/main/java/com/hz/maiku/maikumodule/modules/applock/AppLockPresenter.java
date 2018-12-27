package com.hz.maiku.maikumodule.modules.applock;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.hz.maiku.maikumodule.bean.CommLockInfo;
import com.hz.maiku.maikumodule.manager.CommLockInfoManager;
import com.hz.maiku.maikumodule.manager.LoadingDialogManager;

import java.util.ArrayList;
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
 * @describe 应用锁 接口平台
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class AppLockPresenter implements AppLockContract.Presenter {

    private static final String TAG = AppLockPresenter.class.getName();
    private Context mContext;
    private AppLockContract.View mView=null;
    private CommLockInfoManager mLockInfoManager;
    private PackageManager mPackageManager;
    public AppLockPresenter(final AppLockContract.View view, Context context) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mContext =context;
        this.mPackageManager = mContext.getPackageManager();
        this.mLockInfoManager = new CommLockInfoManager(mContext);
    }


    @Override
    public void getAppLists() {

        LoadingDialogManager.show(mContext);
        Observable.create(new ObservableOnSubscribe<List<CommLockInfo>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CommLockInfo>> e) throws Exception {
                e.onNext(getAllNeedLockApp());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<CommLockInfo>>() {
            @Override
            public void accept(List<CommLockInfo> commLockInfos) throws Exception {
                LoadingDialogManager.hideProgressDialog();
                if(commLockInfos!=null&&commLockInfos.size()>0){
                    if(mView!=null){
                        mView.showAppData(commLockInfos);
                    }
                }
            }
        });

    }

//    @Override
//    public void searchApp(final String text) {
//        if(!TextUtils.isEmpty(text)){
//            Observable.create(new ObservableOnSubscribe<List<CommLockInfo>>() {
//                @Override
//                public void subscribe(ObservableEmitter<List<CommLockInfo>> e) throws Exception {
//                    e.onNext(getSearchApp(text));
//                }
//            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<CommLockInfo>>() {
//                @Override
//                public void accept(List<CommLockInfo> commLockInfos) throws Exception {
//                    if(commLockInfos!=null&&commLockInfos.size()>0){
//                        if(mView!=null){
//                            mView.showSearchData(commLockInfos);
//                        }
//                    }
//                }
//            });
//        }
//    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


    public  List<CommLockInfo> getAllNeedLockApp(){
        //数据可中获取设置好的应用
        List<CommLockInfo> commLockInfos =  mLockInfoManager.getAllCommLockInfos();

        if(commLockInfos==null||commLockInfos.size()==0){
            return new ArrayList<>();
        }

        List<CommLockInfo> userLockInfos = new ArrayList<>();
        List<CommLockInfo> sysmLockInfos = new ArrayList<>();

        //移除可能卸载的应用 以及区分用户及系统应用
        try {
            for (CommLockInfo mbean :commLockInfos){
                ApplicationInfo appInfo = mPackageManager.getApplicationInfo(mbean.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
                if(appInfo==null||mPackageManager.getApplicationIcon(appInfo) == null){
                    commLockInfos.remove(mbean);
                    continue;
                }else {
                    mbean.setAppInfo(appInfo); //给列表ApplicationInfo赋值
                    if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) { //判断是否是系统应用 ApplicationInfo#isSystemApp()
                        sysmLockInfos.add(mbean);
                    } else {
                        userLockInfos.add(mbean);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        commLockInfos.clear();
        //做一个排序 先用户应用 在系统应用
        commLockInfos.addAll(userLockInfos);
        commLockInfos.addAll(sysmLockInfos);

        return  commLockInfos;
    }


    /**
     * 搜索App
     * @param text
     * @return
     */
    public  List<CommLockInfo> getSearchApp(String text){

        List<CommLockInfo> commLockInfos =  mLockInfoManager.queryBlurryList(text);

        List<CommLockInfo> userLockInfos = new ArrayList<>();
        List<CommLockInfo> sysmLockInfos = new ArrayList<>();

        //移除可能卸载的应用 以及区分用户及系统应用
        try {
            for (CommLockInfo mbean :commLockInfos){
                ApplicationInfo appInfo = mPackageManager.getApplicationInfo(mbean.getPackageName(), PackageManager.GET_UNINSTALLED_PACKAGES);
                if(appInfo==null||mPackageManager.getApplicationIcon(appInfo) == null){
                    commLockInfos.remove(mbean);
                    continue;
                }else {
                    mbean.setAppInfo(appInfo); //给列表ApplicationInfo赋值
                    if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) { //判断是否是系统应用 ApplicationInfo#isSystemApp()
                        sysmLockInfos.add(mbean);
                    } else {
                        userLockInfos.add(mbean);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        commLockInfos.clear();
        //做一个排序 先用户应用 在系统应用
        commLockInfos.addAll(userLockInfos);
        commLockInfos.addAll(sysmLockInfos);

        return  commLockInfos;
    }




}
