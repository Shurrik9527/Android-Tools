package com.hz.maiku.maikumodule.tasks;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.text.TextUtils;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.MaiKuApp;
import com.hz.maiku.maikumodule.bean.JunkCleanerInformBean;
import com.hz.maiku.maikumodule.tasks.callback.ISysScanCallBack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 系统缓存扫描
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class SysCacheScanTask extends AsyncTask<Void, Void, Void>{

    private static final  String TAG =SysCacheScanTask.class.getName();
    private ISysScanCallBack mCallBack;
    private int mScanCount;
    private int mTotalCount;
    private ArrayList<JunkCleanerInformBean> mSysCaches;
    private HashMap<String, String> mAppNames;
    private HashMap<String,Drawable> mAppIcons;
    private long mTotalSize = 0L;
    private boolean mIsOverTime = true;

    public SysCacheScanTask(ISysScanCallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Observable.timer(30*1000,TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }
            @Override
            public void onNext(Long aLong) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                if(mIsOverTime){
                    mCallBack.onOverTime();
                }
            }
        });
    }

    @Override
    protected Void doInBackground(Void... params) {
        mCallBack.onBegin();

        if (isCancelled()) {
            mCallBack.onCancel();
            return null;
        }

        PackageManager pm = MaiKuApp.getmContext().getPackageManager();
        @SuppressLint("WrongConstant") List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_GIDS);

        IPackageStatsObserver.Stub observer = new PackageStatsObserver();
        mTotalCount = installedPackages.size();
        mSysCaches = new ArrayList<>();
        mAppNames = new HashMap<>();
        mAppIcons = new HashMap<>();
        try{
            for (int i = 0; i < mTotalCount; i++) {
                ApplicationInfo info = installedPackages.get(i);
                mAppNames.put(info.packageName, pm.getApplicationLabel(info).toString());
                mAppIcons.put(info.packageName, info.loadIcon(pm));
                getPackageInfo(info.packageName, observer);
            }
        }catch (Exception e){

        }

        mIsOverTime = false;
        return null;
    }

    private void getPackageInfo(String packageName, IPackageStatsObserver.Stub observer) {

        if (isCancelled()) {
            return;
        }

        try {
            PackageManager pm = MaiKuApp.getmContext().getPackageManager();
            Method getPackageSizeInfo = pm.getClass()
                    .getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);

            getPackageSizeInfo.invoke(pm, packageName, observer);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private class PackageStatsObserver extends IPackageStatsObserver.Stub {

        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            mScanCount++;
            //正在扫描
            if (succeeded && pStats != null) {
                JunkCleanerInformBean info = new JunkCleanerInformBean();
                    String mName =pStats.packageName;
                    Drawable mDrawable =null;
                    if(mAppNames!=null&&mAppNames.size()>0){
                        if(!TextUtils.isEmpty(mAppNames.get(pStats.packageName))){
                            mName =mAppNames.get(pStats.packageName);
                        }
                    }

                    if(mAppIcons!=null&&mAppIcons.size()>0){
                        if(mAppIcons.get(pStats.packageName)!=null){
                            mDrawable =mAppIcons.get(pStats.packageName);
                        }
                    }
                    info.setmPackageName(pStats.packageName);
                    info.setmName(mName);
                    info.setmIsCheck(true);
                    info.setmDrawable(mDrawable);
                    info.setmSize(pStats.cacheSize + pStats.externalCacheSize);
                    if (info.getmSize() > 0) {
                        mSysCaches.add(info);
                        mTotalSize += info.getmSize();
                    }
                mCallBack.onProgress(info);
            }

            if (mScanCount == mTotalCount) {
                JunkCleanerInformBean junkCleanerInformBean = new JunkCleanerInformBean();
                junkCleanerInformBean.setmPackageName(pStats.packageName);
                junkCleanerInformBean.setmName(MaiKuApp.getmContext().getString(R.string.system_cache));
                junkCleanerInformBean.setmSize(mTotalSize);
                junkCleanerInformBean.setmIsCheck(true);
                junkCleanerInformBean.setmIsVisible(true);
                junkCleanerInformBean.setmIsChild(false);
                junkCleanerInformBean.setmChildren(mSysCaches);
                Collections.sort(mSysCaches);
                Collections.reverse(mSysCaches);
                ArrayList<JunkCleanerInformBean> list = new ArrayList<>();
                list.add(junkCleanerInformBean);
                mCallBack.onFinish(list);
            }
        }
    }
}
