package com.hz.maiku.maikumodule.modules.junkcleaner;

import android.content.Context;
import android.util.Log;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.MaiKuApp;
import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerGroupBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerInformBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerMultiItemBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerProcessInformBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerTypeBean;
import com.hz.maiku.maikumodule.event.JunkCleanerCurrentScanEvent;
import com.hz.maiku.maikumodule.event.JunkCleanerCurrentSizeEvent;
import com.hz.maiku.maikumodule.event.JunkCleanerDataEvent;
import com.hz.maiku.maikumodule.event.JunkCleanerItemDissDialogEvent;
import com.hz.maiku.maikumodule.event.JunkCleanerItemTotalSizeEvent;
import com.hz.maiku.maikumodule.event.JunkCleanerShowDialogEvent;
import com.hz.maiku.maikumodule.event.JunkCleanerTotalSizeEvent;
import com.hz.maiku.maikumodule.event.JunkCleanerTypeClickEvent;
import com.hz.maiku.maikumodule.manager.CleanManager;
import com.hz.maiku.maikumodule.manager.JunkCleanerManager;
import com.hz.maiku.maikumodule.manager.MemoryManager;
import com.hz.maiku.maikumodule.manager.ProcessManager;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.MimeTypes;
import com.hz.maiku.maikumodule.util.RxBus.RxBus;
import com.hz.maiku.maikumodule.util.RxBus.RxBusHelper;
import com.hz.maiku.maikumodule.util.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 Presenter
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerPresenter implements JunkCleanerContract.Presenter {

    private static final String TAG = JunkCleanerPresenter.class.getName();
    private JunkCleanerContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private Context mContext;
    private JunkCleanerManager mJunkCleanerManager;//垃圾清理管理
    private CleanManager mCleanManager;//清理管理
    private ProcessManager mProcessManager;//进程管理
    private long mTotalJunkSize;
    private boolean mOverScanFinish;
    private MemoryManager mMemoryManager;
    public JunkCleanerPresenter(JunkCleanerContract.View view) {
        this.mView = view;
        this.mContext = MaiKuApp.getmContext();
        this.mView.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
        mJunkCleanerManager =JunkCleanerManager.getInstance();
        mCleanManager =CleanManager.getmInstance();
        mProcessManager =ProcessManager.getInstance();
        mMemoryManager = MemoryManager.getmInstance();
        //总垃圾数
        RxBusHelper.doOnMainThread(JunkCleanerTotalSizeEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerTotalSizeEvent>() {
            @Override
            public void onEvent(JunkCleanerTotalSizeEvent junkCleanerTotalSizeEvent) {
                if(mView!=null){
                   // mView.showTotalSize(junkCleanerTotalSizeEvent.getTotalSize());
                }
            }

        });

        RxBusHelper.doOnMainThread(JunkCleanerCurrentScanEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerCurrentScanEvent>() {
            @Override
            public void onEvent(JunkCleanerCurrentScanEvent junkCleanerCurrentScanEvent) {
                if(mView!=null){
                    mView.showCurrentScanJunkFileName(junkCleanerCurrentScanEvent.getJunkInfo().getmName());
                }
            }

        });


        //当前文件垃圾大小
        RxBusHelper.doOnMainThread(JunkCleanerCurrentSizeEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerCurrentSizeEvent>() {
            @Override
            public void onEvent(JunkCleanerCurrentSizeEvent junkCleanerCurrentSizeEvent) {
                if(mView!=null){
                    mTotalJunkSize = junkCleanerCurrentSizeEvent.getTotalSize();
                    mView.showTotalSize(FormatUtil.formatFileSize(mTotalJunkSize).toString());
                }
            }

        });

        //某项垃圾大小
        RxBusHelper.doOnMainThread(JunkCleanerItemTotalSizeEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerItemTotalSizeEvent>() {
            @Override
            public void onEvent(JunkCleanerItemTotalSizeEvent junkCleanerItemTotalSizeEvent) {
                if(mView!=null){
                    mView.showItemTotalJunkSize(junkCleanerItemTotalSizeEvent.getIndex(), junkCleanerItemTotalSizeEvent.getTotalSize());
                }
            }

        });

        //某项垃圾数据
        RxBusHelper.doOnMainThread(JunkCleanerDataEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerDataEvent>() {
            @Override
            public void onEvent(JunkCleanerDataEvent junkCleanerDataEvent) {
                if(mView!=null){
                    mView.showData(junkCleanerDataEvent.getJunkCleanerGroupBean());
                    mView.setBtnEnable(true);
                }
            }

        });

        //进度条显示
        RxBusHelper.doOnMainThread(JunkCleanerShowDialogEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerShowDialogEvent>() {
            @Override
            public void onEvent(JunkCleanerShowDialogEvent junkCleanerShowDialogEvent) {
                if(mView!=null){
                    mView.showDialog(junkCleanerShowDialogEvent.getIndex());
                }
            }

        });

        //进度条消失
        RxBusHelper.doOnMainThread(JunkCleanerItemDissDialogEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerItemDissDialogEvent>() {
            @Override
            public void onEvent(JunkCleanerItemDissDialogEvent junkCleanerItemDissDialogEvent) {
                if(mView!=null){
                    mView.dissDialog(junkCleanerItemDissDialogEvent.getIndex());
                }
            }

        });

        //点击
        RxBusHelper.doOnMainThread(JunkCleanerTypeClickEvent.class, mCompositeDisposable, new RxBusHelper.OnEventListener<JunkCleanerTypeClickEvent>() {
            @Override
            public void onEvent(JunkCleanerTypeClickEvent junkCleanerTypeClickEvent) {
                if(mView!=null){
                    mView.clickGroup(junkCleanerTypeClickEvent.isExpand(), junkCleanerTypeClickEvent.getPosition());
                }
            }

        });



    }

    @Override
    public void startScanTask() {

        mJunkCleanerManager.startScanTask();
        mJunkCleanerManager.setScanListener(new JunkCleanerManager.IScanListener() {

            @Override
            public void startScan() {
                RxBus.getDefault().post(new JunkCleanerShowDialogEvent(0));
                mTotalJunkSize = 0L;
                mOverScanFinish = false;
            }

            //扫描结束
            @Override
            public void isOverScanFinish(ArrayList<JunkCleanerInformBean> apkList, ArrayList<JunkCleanerInformBean> logList, ArrayList<JunkCleanerInformBean> tempList) {


                //通知apk结束进度条
                RxBus.getDefault().post(new JunkCleanerItemDissDialogEvent(JunkCleanerTypeBean.APK));
                //通知日志结束进度条
                RxBus.getDefault().post(new JunkCleanerItemDissDialogEvent(JunkCleanerTypeBean.LOG));
                //通知临时文件结束进度条
                RxBus.getDefault().post(new JunkCleanerItemDissDialogEvent(JunkCleanerTypeBean.TEMP));

                //通知apk数据更新
                RxBus.getDefault().post(new JunkCleanerItemTotalSizeEvent(JunkCleanerTypeBean.APK, getFilterJunkSize(apkList)));
                //通知日志数据更新
                RxBus.getDefault().post(new JunkCleanerItemTotalSizeEvent(JunkCleanerTypeBean.LOG, getFilterJunkSize(logList)));
                //通知临时文件数据更新
                RxBus.getDefault().post(new JunkCleanerItemTotalSizeEvent(JunkCleanerTypeBean.TEMP, getFilterJunkSize(tempList)));

                mOverScanFinish = true;

            }

            //系统缓存 扫描结束
            @Override
            public void isSysCacheScanFinish(ArrayList<JunkCleanerInformBean> sysCacheList) {
                Log.i(TAG,"扫描sysCacheList.size="+sysCacheList.size());
                RxBus.getDefault().post(new JunkCleanerItemDissDialogEvent(JunkCleanerTypeBean.CACHE));

                RxBus.getDefault().post(new JunkCleanerItemTotalSizeEvent(JunkCleanerTypeBean.CACHE, getFilterJunkSize(sysCacheList)));
                long size = 0L;
                for (JunkCleanerInformBean info : sysCacheList) {
                    size += info.getmSize();
                }
                mTotalJunkSize += size;
                RxBus.getDefault().post(new JunkCleanerTotalSizeEvent(FormatUtil.formatFileSize(mTotalJunkSize).toString()));

            }

            //进程扫描结束
            @Override
            public void isProcessScanFinish(ArrayList<AppProcessInfornBean> processList) {
                Log.i(TAG,"扫描processList.size="+processList.size());
                RxBus.getDefault().post(new JunkCleanerItemDissDialogEvent(JunkCleanerTypeBean.PROCESS));
                long size = 0L;
                for (AppProcessInfornBean info : processList) {
                    size += info.getMemory();
                }
                mTotalJunkSize += size;
                //刷新进程
                RxBus.getDefault().post(new JunkCleanerItemTotalSizeEvent(JunkCleanerTypeBean.PROCESS, FormatUtil.formatFileSize(size).toString()));
                //刷新总数
                RxBus.getDefault().post(new JunkCleanerTotalSizeEvent(FormatUtil.formatFileSize(mTotalJunkSize).toString()));
            }

            //扫描都结束
            @Override
            public void isAllScanFinish(JunkCleanerGroupBean junkCleanerGroupBean) {
                RxBus.getDefault().post(new JunkCleanerDataEvent(junkCleanerGroupBean));
                RxBus.getDefault().post(new JunkCleanerCurrentSizeEvent(mTotalJunkSize));

            }

            //当前正在扫描
            @Override
            public void currentOverScanJunk(JunkCleanerInformBean junkCleanerInformBean) {
                //大文件默认不勾选
                File file = new File(junkCleanerInformBean.getmPath());
                if (MimeTypes.isApk(file) || MimeTypes.isTempFile(file)) {
                    mTotalJunkSize += junkCleanerInformBean.getmSize();
                    RxBus.getDefault().post(new JunkCleanerTotalSizeEvent(FormatUtil.formatFileSize(mTotalJunkSize).toString()));
                }
                RxBus.getDefault().post(new JunkCleanerCurrentScanEvent(JunkCleanerCurrentScanEvent.OVER_CACHE, junkCleanerInformBean));
            }

            //扫描当前系统缓存
            @Override
            public void currentSysCacheScanJunk(JunkCleanerInformBean junkCleanerInformBean) {

                if (mOverScanFinish) {
                    RxBus.getDefault().post(new JunkCleanerCurrentScanEvent(JunkCleanerCurrentScanEvent.SYS_CAHCE, junkCleanerInformBean));
                }
            }
        });
    }

    @Override
    public void startCleanJunkTask(List<JunkCleanerMultiItemBean> list) {
        if(list==null||list.size()==0){
            return;
        }
        final List<String> junkList = new ArrayList<>();
        final List<String> appCacheList = new ArrayList<>();
        final List<String> processList =new ArrayList<>();


        getApkOrLogFileString(list).zipWith(getAppCacheFileString(list.get(1)), new BiFunction<List<String>, List<String>, Object>() {
            @Override
            public Object apply(List<String> strings, List<String> strings2) throws Exception {
                if(junkList!=null){
                    junkList.clear();
                }
                junkList.addAll(strings);
                if(appCacheList!=null){
                    appCacheList.clear();
                }
                appCacheList.addAll(strings2);
                return strings!=null&&strings2!=null;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).zipWith(getProcessFileString(list.get(0)), new BiFunction<Object, List<String>, Object>() {
            @Override
            public Object apply(Object o, List<String> strings) throws Exception {
                if(processList!=null){
                    processList.clear();
                }
                processList.addAll(strings);
                return o;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        cheanDataFile(processList,appCacheList,junkList);
                    }
                });


    }

    /**
     * 获取 apk log 数据
     * @param list
     * @return
     */
    public Observable<List<String>> getApkOrLogFileString(final List<JunkCleanerMultiItemBean> list) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                e.onNext(getJunkList(list));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取进程数据
     * @param junkCleanerMultiItemBean
     * @return
     */
    public Observable<List<String>> getProcessFileString(final JunkCleanerMultiItemBean junkCleanerMultiItemBean) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                e.onNext(getJunkProcessSet(junkCleanerMultiItemBean));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获取App缓存数据
     * @param junkCleanerMultiItemBean
     * @return
     */
    public Observable<List<String>> getAppCacheFileString(final JunkCleanerMultiItemBean junkCleanerMultiItemBean) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                e.onNext(getJunkTypeAppCacheList(junkCleanerMultiItemBean));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    private List<String> getJunkList(List<JunkCleanerMultiItemBean> list) {
        List<String> tempList = new ArrayList<>();
        for (int i = 2; i < 5; i++) {
            tempList.addAll(getApkOrLogJunkTypeList(list.get(i)));
        }

        return tempList;
    }


    private List<String> getJunkTypeAppCacheList(JunkCleanerMultiItemBean entity) {
        if(entity==null){
            return new ArrayList<>();
        }

        List<JunkCleanerProcessInformBean> appCacheList = ((JunkCleanerTypeBean) entity).getSubItems();
        if(appCacheList==null||appCacheList.size()==0){
            return new ArrayList<>();
        }
        List<String> tempList = new ArrayList<>();
        if (appCacheList != null) {
            for (JunkCleanerProcessInformBean info : appCacheList) {
                if (info.isCheck()) {
                    tempList.add(info.getJunkInfo().getmPackageName());
                }
            }
        }
        return tempList;
    }

    private List<String> getApkOrLogJunkTypeList(JunkCleanerMultiItemBean entity) {
        if(entity==null){
            return new ArrayList<>();
        }

        List<JunkCleanerProcessInformBean> appCacheList = ((JunkCleanerTypeBean) entity).getSubItems();

        if(appCacheList==null||appCacheList.size()==0){
            return new ArrayList<>();
        }
        List<String> tempList = new ArrayList<>();
        if (appCacheList != null) {
            for (JunkCleanerProcessInformBean info : appCacheList) {
                if (info.isCheck()) {
                    tempList.add(info.getJunkInfo().getmPath());
                }
            }
        }
        return tempList;
    }


    /**
     * 获取进程信息
     * @param entity
     * @return
     */
    private List<String> getJunkProcessSet(JunkCleanerMultiItemBean entity) {
        if(entity==null)
            return new ArrayList<>();

        List<JunkCleanerProcessInformBean> appCacheList = ((JunkCleanerTypeBean) entity).getSubItems();
        List<String> tempSet = new ArrayList<>();
        if (appCacheList != null) {
            for (JunkCleanerProcessInformBean info : appCacheList) {
                if (info.isCheck()) {
                    tempSet.add(info.getAppProcessInfo().getProcessName());
                }
            }
        }
        return tempSet;
    }

    /**
     * 计算总垃圾数
     * @param list
     * @return
     */
    private String getFilterJunkSize(ArrayList<JunkCleanerInformBean> list) {

        long size = 0L;
        for (JunkCleanerInformBean info : list) {
            size += info.getmSize();
        }
        return FormatUtil.formatFileSize(size).toString();
    }


    @Override
    public void initAdapterData() {
        ArrayList<JunkCleanerMultiItemBean> list = new ArrayList<>();
        int title[] = {R.string.junkcleaner_process_title, R.string.junkcleaner_system_cache_tiele, R.string.junkcleaner_unused_apk_tiele,
                R.string.junkcleaner_temporary_file_tiele, R.string.junkcleaner_log_file_tiele};
        int resourceId[] = {R.mipmap.junkcleaner_processing_icon,R.mipmap.junkcleaner_appcache_icon, R.mipmap.junkcleaner_usedapk_icon,R.mipmap.junkcleaner_temp_icon, R.mipmap.junkcleaner_log_icon};
        for (int i = 0; i < 5; i++) {
            JunkCleanerTypeBean junkType = new JunkCleanerTypeBean();
            junkType.setTitle(mContext.getString(title[i]))
                    .setCheck(true)
                    .setIconResourceId(resourceId[i])
                    .setTotalSize("")
                    .setProgressVisible(true);
            list.add(junkType);
        }
        mView.initAdapterData(list);
    }

    @Override
    public void cheanDataFile(List<String> processList,List<String> appCacheList,List<String> junkList) {

        //合并清理rx监听
        Log.i(TAG,"processList is"+processList.toString());
        Log.i(TAG,"appCacheList is"+appCacheList.toString());
        Log.i(TAG,"junkList is"+junkList.toString());

        mCleanManager.cleanJunksUsingObservable(junkList).zipWith(mCleanManager.cleanAppsCacheObservable(appCacheList), new BiFunction<Boolean, Boolean, Object>() {
            @Override
            public Object apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                return aBoolean&&aBoolean2;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).zipWith(mProcessManager.killListsRunningAppObservale(processList), new BiFunction<Object, Long, Object>() {
            @Override
            public Object apply(Object o, Long aLong) throws Exception {
                return o;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if(mView!=null){
                            mView.cleanFinish();
                        }
                    }
                });

    }

    @Override
    public void allMemorySpace() {

        mMemoryManager.getDeviceTotalMemoryObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if(mView!=null){
                    mView.allMemorySpace(aLong);
                }
            }
        });

        StorageUtil.SDCardInfo sdCardInfo = StorageUtil.getSDCardInfo(mContext);
        String storageInfo = FormatUtil.formatFileSize(sdCardInfo.mTotal - sdCardInfo.mFree).toString() + "/" +
                FormatUtil.formatFileSize(sdCardInfo.mTotal).toString();
        Log.i(TAG,"storageInfo==="+storageInfo);


    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        this.mView =null;
        if(mCompositeDisposable.isDisposed()){
            mCompositeDisposable.clear();
        }
        mCompositeDisposable =null;
    }

}
