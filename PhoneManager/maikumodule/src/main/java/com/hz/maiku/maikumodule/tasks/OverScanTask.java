package com.hz.maiku.maikumodule.tasks;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.hz.maiku.maikumodule.base.MaiKuApp;
import com.hz.maiku.maikumodule.bean.ApkInformBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerInformBean;
import com.hz.maiku.maikumodule.tasks.callback.IScanCallBack;
import com.hz.maiku.maikumodule.util.FileUtil;
import com.hz.maiku.maikumodule.util.MimeTypes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 异步扫描
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class OverScanTask extends AsyncTask<Void, Void, Void> {

    private IScanCallBack mCallBack;
    private final int SCAN_LEVEL = 5;
    private boolean mIsOverTime = true;
    private JunkCleanerInformBean mApkInfo;
    private JunkCleanerInformBean mLogInfo;
    private JunkCleanerInformBean mTempInfo;
    private JunkCleanerInformBean mBigFileInfo;
    private ArrayList<JunkCleanerInformBean> mList;
    private ArrayList<JunkCleanerInformBean> mApkList;//apk垃圾
    private ArrayList<JunkCleanerInformBean> mLogList;//log文件
    private ArrayList<JunkCleanerInformBean> mTempList;


    public OverScanTask(IScanCallBack scanCallBack) {
        mCallBack = scanCallBack;
        mApkInfo = new JunkCleanerInformBean();
        mLogInfo = new JunkCleanerInformBean();
        mTempInfo = new JunkCleanerInformBean();
        mBigFileInfo = new JunkCleanerInformBean();
        mList = new ArrayList<>();
        mApkList = new ArrayList<>();
        mLogList = new ArrayList<>();
        mTempList = new ArrayList<>();
    }

    
    private void scanPath(File root , int level) {

        if (root == null || !root.exists() || level > SCAN_LEVEL || isCancelled()) {
            return;
        }
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                if (isCancelled()) {
                    return;
                }
                if (file.isFile()) {
                    JunkCleanerInformBean info = null;
                    //APK文件
                    if (MimeTypes.isApk(file)) {
                        info = getJunkCleanerInformBean(file);
                        mApkInfo.getmChildren().add(info);
                        mApkInfo.setmSize(mApkInfo.getmSize() + info.getmSize());
                        //如果是已经安装的apk并且版本号>=当前扫描到的apk，则勾选 防止低版本未安装的
                        ApkInformBean apkInfo = FileUtil.getApkInfo(file.getAbsolutePath());
                        if (apkInfo != null && !TextUtils.isEmpty(apkInfo.getPackageName())) {
                            mApkInfo.setmIsCheck(compareToInstall(apkInfo.getVersionCode(), apkInfo.getPackageName()));
                        }
                    } else if (MimeTypes.isLog(file)) {//日志文件
                        info = getJunkCleanerInformBean(file);
                        mLogInfo.getmChildren().add(info);
                        mLogInfo.setmSize(mLogInfo.getmSize() + info.getmSize());
                    } else if (MimeTypes.isTempFile(file)) {//临时文件
                        info = getJunkCleanerInformBean(file);
                        mTempInfo.getmChildren().add(info);
                        mTempInfo.setmSize(mTempInfo.getmSize() + info.getmSize());
                    }

                    if (info != null) {
                        mCallBack.onProgress(info);
                    }
                } else {
                    if (level < SCAN_LEVEL) {
                        scanPath(file , level + 1);
                    }
                }
            }
        }
    }

    private JunkCleanerInformBean getJunkCleanerInformBean(File file) {
        JunkCleanerInformBean junkCleanerInformBean = new JunkCleanerInformBean();
            junkCleanerInformBean.setmSize(file.length());
            junkCleanerInformBean.setmName(file.getName());
            junkCleanerInformBean.setmPath(file.getAbsolutePath());
            junkCleanerInformBean.setmIsChild(false);
            junkCleanerInformBean.setmIsCheck(true);
            junkCleanerInformBean.setmIsVisible(true);
        return junkCleanerInformBean;
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
                if(mIsOverTime){
                    mCallBack.onOverTime();
                }
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
    protected Void doInBackground(Void... params) {
        mCallBack.onBegin();

        if (isCancelled()) {
            mCallBack.onCancel();
            return null;
        }

        File externalDir = Environment.getExternalStorageDirectory();
        if (externalDir != null) {
            scanPath(externalDir, 0);
        }

        if (mApkInfo.getmSize() > 0L) {
            Collections.sort(mApkInfo.getmChildren());
            Collections.reverse(mApkInfo.getmChildren());
            mList.add(mApkInfo);
            mApkList.add(mApkInfo);
        }

        if (mLogInfo.getmSize() > 0L) {
            Collections.sort(mLogInfo.getmChildren());
            Collections.reverse(mLogInfo.getmChildren());
            mList.add(mLogInfo);
            mLogList.add(mLogInfo);
        }

        if (mTempInfo.getmSize() > 0L) {
            Collections.sort(mTempInfo.getmChildren());
            Collections.reverse(mTempInfo.getmChildren());
            mList.add(mTempInfo);
            mTempList.add(mTempInfo);
        }



        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        mCallBack.onFinish(mApkList, mLogList, mTempList);
        mIsOverTime = false;
        super.onPostExecute(aVoid);
    }

    private boolean compareToInstall(int versionCode, String packageName) {
        PackageInfo info;
        try {
            info = MaiKuApp.getmContext().getPackageManager().getPackageInfo(packageName, 0);
            return versionCode <= info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
