package com.hz.maiku.maikumodule.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerGroupBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerInformBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerProcessInformBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerTypeBean;
import com.hz.maiku.maikumodule.tasks.OverScanTask;
import com.hz.maiku.maikumodule.tasks.SysCacheScanTask;
import com.hz.maiku.maikumodule.tasks.SysCacheScanTwoTask;
import com.hz.maiku.maikumodule.tasks.callback.IScanCallBack;
import com.hz.maiku.maikumodule.tasks.callback.ISysScanCallBack;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理管理
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerManager {

    private static final String TAG =JunkCleanerManager.class.getName();

    private static JunkCleanerManager sInstance;
    private OverScanTask mOverScanTask;
    private SysCacheScanTask mSysCacheScanTask;
    private SysCacheScanTwoTask mSysCacheScanTwoTask;
    private ProcessManager mProcessManager;
    private ArrayList<JunkCleanerInformBean> mApkList;
    private ArrayList<JunkCleanerInformBean> mLogList;
    private ArrayList<JunkCleanerInformBean> mTempList;
    private ArrayList<JunkCleanerInformBean> mSysCacheList;
    private ArrayList<AppProcessInfornBean> mProcessList;
    private JunkCleanerGroupBean mJunkGroup;
    private boolean mIsOverScanFinish;
    private boolean mIsSystemScanFinish;
    private boolean mIsProcessScanFinsh;
    private IScanListener mScanListener;
    private Context mContext;

    private JunkCleanerManager(Context context) {

        mContext = context.getApplicationContext();
        mApkList = new ArrayList<>();
        mLogList = new ArrayList<>();
        mTempList = new ArrayList<>();
        mSysCacheList = new ArrayList<>();
        mProcessList = new ArrayList<>();
        mJunkGroup = new JunkCleanerGroupBean();
    }

    public static void init(Context context) {
        if (sInstance == null) {
            synchronized (JunkCleanerManager.class) {
                if (sInstance == null) {
                    sInstance = new JunkCleanerManager(context);
                }
            }
        }
    }

    public static JunkCleanerManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("JunkCleanerManager is not init...");
        }
        return sInstance;
    }

    public void startScanTask() {

        if (mScanListener != null) {
            mScanListener.startScan();
        }

        mOverScanTask = new OverScanTask(new IScanCallBack() {
            @Override
            public void onBegin() {
                mIsOverScanFinish = false;
            }

            @Override
            public void onProgress(JunkCleanerInformBean junkCleanerInformBean) {
                if (mScanListener != null) {
                    mScanListener.currentOverScanJunk(junkCleanerInformBean);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish(ArrayList<JunkCleanerInformBean> apkList, ArrayList<JunkCleanerInformBean> logList, ArrayList<JunkCleanerInformBean> tempList) {
                mIsOverScanFinish = true;
                mApkList = apkList;
                mLogList = logList;
                mTempList = tempList;
                if (mScanListener != null) {
                    mScanListener.isOverScanFinish(mApkList, mLogList, mTempList);
                    checkAllScanFinish();
                }
            }

            @Override
            public void onOverTime() {
                cancelScanTask();
                mIsOverScanFinish = true;
                checkAllScanFinish();
            }
        });
        mOverScanTask.execute();

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            mSysCacheScanTwoTask = new SysCacheScanTwoTask(new ISysScanCallBack() {
                @Override
                public void onBegin() {
                    mIsSystemScanFinish = false;
                }

                @Override
                public void onProgress(JunkCleanerInformBean JunkCleanerInformBean) {
                    if (mScanListener != null) {
                        mScanListener.currentSysCacheScanJunk(JunkCleanerInformBean);
                    }
                }
                @Override
                public void onCancel() {

                }

                @Override
                public void onFinish(ArrayList<JunkCleanerInformBean> children) {
                    mIsSystemScanFinish = true;
                    mSysCacheList = children;
                    if (mScanListener != null) {
                        mScanListener.isSysCacheScanFinish(children);
                        checkAllScanFinish();
                    }
                }

                @Override
                public void onOverTime() {
                    cancelScanTask();
                    mIsSystemScanFinish = true;
                    checkAllScanFinish();
                }
            });
            mSysCacheScanTwoTask.execute();


        }else{
            mSysCacheScanTask = new SysCacheScanTask(new ISysScanCallBack() {
                @Override
                public void onBegin() {
                    mIsSystemScanFinish = false;
                }

                @Override
                public void onProgress(JunkCleanerInformBean JunkCleanerInformBean) {
                    if (mScanListener != null) {
                        mScanListener.currentSysCacheScanJunk(JunkCleanerInformBean);
                    }
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onFinish(ArrayList<JunkCleanerInformBean> children) {
                    mIsSystemScanFinish = true;
                    mSysCacheList = children;
                    if (mScanListener != null) {
                        mScanListener.isSysCacheScanFinish(children);
                        checkAllScanFinish();
                    }
                }

                @Override
                public void onOverTime() {
                    cancelScanTask();
                    mIsSystemScanFinish = true;
                    checkAllScanFinish();
                }
            });
            mSysCacheScanTask.execute();
        }

        mIsProcessScanFinsh = false;
        mProcessManager = ProcessManager.getInstance();

        mProcessManager.getRunningAppListObservable(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<AppProcessInfornBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<AppProcessInfornBean> appProcessInfornBeans) {
                        mIsProcessScanFinsh = true;
                        mProcessList = (ArrayList<AppProcessInfornBean>) appProcessInfornBeans;
                        if (mScanListener != null) {
                            mScanListener.isProcessScanFinish(mProcessList);
                            checkAllScanFinish();
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

    public void setScanListener(IScanListener listener) {
        this.mScanListener = listener;
    }

    private void checkAllScanFinish() {
        if (mIsOverScanFinish && mIsSystemScanFinish && mIsProcessScanFinsh) {

            //添加进程list
            ArrayList<JunkCleanerProcessInformBean> list = new ArrayList<>();
            for (AppProcessInfornBean info : mProcessList) {
                JunkCleanerProcessInformBean junkProcessInfo = new JunkCleanerProcessInformBean(info);
                junkProcessInfo.setCheck(true);
                list.add(junkProcessInfo);
            }
            mJunkGroup.setProcessList(list);

            mJunkGroup.setSysCacheList(getJunkProcessList(mSysCacheList, JunkCleanerTypeBean.CACHE))
                    .setApkList(getJunkProcessList(mApkList, JunkCleanerTypeBean.APK))
                    .setLogList(getJunkProcessList(mLogList, JunkCleanerTypeBean.LOG))
                    .setTempList(getJunkProcessList(mTempList, JunkCleanerTypeBean.TEMP));
            mScanListener.isAllScanFinish(mJunkGroup);

        }
    }


    private ArrayList<JunkCleanerProcessInformBean> getJunkProcessList(ArrayList<JunkCleanerInformBean> list, int type) {

        ArrayList<JunkCleanerProcessInformBean> tempList = new ArrayList<>();
        for (JunkCleanerInformBean JunkCleanerInformBean : list) {
            for (int i = 0; i < JunkCleanerInformBean.getmChildren().size(); i++) {
                JunkCleanerInformBean info = JunkCleanerInformBean.getmChildren().get(i);
                JunkCleanerProcessInformBean junkProcessInfo = new JunkCleanerProcessInformBean(info, type);
                junkProcessInfo.setCheck(info.ismIsCheck());
                tempList.add(junkProcessInfo);
            }
        }
        return tempList;
    }


    public void cancelScanTask() {

        //判断当前的异步任务是否为空，并且判断当前的异步任务的状态是否是运行状态{RUNNING(运行),PENDING(准备),FINISHED(完成)}
        if (mOverScanTask != null && mOverScanTask.getStatus() == AsyncTask.Status.RUNNING) {
            /**
             *cancel(true) 取消当前的异步任务，传入的true,表示当中断异步任务时继续已经运行的线程的操作，
             *但是为了线程的安全一般为让它继续设为true
             * */
            mOverScanTask.cancel(true);
        }

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            if (mSysCacheScanTask != null && mSysCacheScanTask.getStatus() == AsyncTask.Status.RUNNING) {
                mSysCacheScanTask.cancel(true);
            }
        }else{
            if (mSysCacheScanTwoTask != null && mSysCacheScanTwoTask.getStatus() == AsyncTask.Status.RUNNING) {
                mSysCacheScanTwoTask.cancel(true);
            }
        }

    }

    public interface IScanListener {

        void startScan();

        void isOverScanFinish(ArrayList<JunkCleanerInformBean> apkList, ArrayList<JunkCleanerInformBean> logList, ArrayList<JunkCleanerInformBean> tempList);

        void isSysCacheScanFinish(ArrayList<JunkCleanerInformBean> sysCacheList);

        void isProcessScanFinish(ArrayList<AppProcessInfornBean> processList);

        void isAllScanFinish(JunkCleanerGroupBean junkGroup);

        void currentOverScanJunk(JunkCleanerInformBean JunkCleanerInformBean);

        void currentSysCacheScanJunk(JunkCleanerInformBean JunkCleanerInformBean);
    }


}
