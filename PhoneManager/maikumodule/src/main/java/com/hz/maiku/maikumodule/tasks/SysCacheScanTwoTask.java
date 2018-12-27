package com.hz.maiku.maikumodule.tasks;

import android.annotation.SuppressLint;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.support.annotation.RequiresApi;
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
 * @describe 8.0 系统缓存扫描
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class SysCacheScanTwoTask extends AsyncTask<Void, Void, Void>{

    private static final  String TAG =SysCacheScanTwoTask.class.getName();
    private ISysScanCallBack mCallBack;
    private int mScanCount;
    private int mTotalCount;
    private ArrayList<JunkCleanerInformBean> mSysCaches;
    private HashMap<String, String> mAppNames;
    private HashMap<String,Drawable> mAppIcons;
    private long mTotalSize = 0L;
    private boolean mIsOverTime = true;
    public SysCacheScanTwoTask(ISysScanCallBack callBack) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Void doInBackground(Void... params) {
        mCallBack.onBegin();

        if (isCancelled()) {
            mCallBack.onCancel();
            return null;
        }
        StorageStatsManager mStorageManager = (StorageStatsManager)  MaiKuApp.getmContext().getSystemService(Context.STORAGE_STATS_SERVICE);
        PackageManager pm = MaiKuApp.getmContext().getPackageManager();
        List<ApplicationInfo> installedPackages = pm.getInstalledApplications(0);
        try{
            mTotalCount = installedPackages.size();
            mSysCaches = new ArrayList<>();
            mAppNames = new HashMap<>();
            mAppIcons = new HashMap<>();
            UserHandle handler = UserHandle.getUserHandleForUid(-2);
            for (ApplicationInfo info :installedPackages){
                mScanCount++;
                mAppNames.put(info.packageName, pm.getApplicationLabel(info).toString());
                mAppIcons.put(info.packageName, info.loadIcon(pm));
                JunkCleanerInformBean bean = new JunkCleanerInformBean();
                bean.setmName(pm.getApplicationLabel(info).toString());
                bean.setmPackageName(info.packageName);
                bean.setmDrawable(info.loadIcon(pm));
                StorageStats stats = mStorageManager.queryStatsForPackage(StorageManager.UUID_DEFAULT, info.packageName, handler);
                bean.setmSize(stats.getCacheBytes());
                bean.setmIsCheck(true);
                if (bean.getmSize() > 0) {
                    mSysCaches.add(bean);
                    mTotalSize += bean.getmSize();
                }
                mCallBack.onProgress(bean);
                if (mScanCount == mTotalCount) {
                    JunkCleanerInformBean junkCleanerInformBean = new JunkCleanerInformBean();
                    junkCleanerInformBean.setmPackageName(info.packageName);
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

        }catch (Exception e){

        }
        mIsOverTime = false;
        return null;
    }

}
