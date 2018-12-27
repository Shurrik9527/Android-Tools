package com.hz.maiku.maikumodule.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @describe  linux系统下文件内存管理
 * @date 2018/9/5
 * @email 252774645@qq.com
 */
public class MemoryManager {

    private static final  String TAG  =MemoryManager.class.getName();
    public static MemoryManager mInstance;
    private Context mContext;
    //私有构造方法
    private MemoryManager(Context context){
        this.mContext =context;
    }

    /**
     * 获取当前内存管理
     * @return
     */
    public static MemoryManager getmInstance(){
        if(mInstance==null){
            throw  new IllegalStateException("MemoryManager is not init...");
        }
        return mInstance;
    }

    /**
     * 初始化内存管理器
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (MemoryManager.class) {
                if (mInstance == null) {
                    mInstance = new MemoryManager(context);
                }
            }
        }
    }


    /**
     * 获取当前设备所有内存
     * @return
     */
    public long getDeviceTotalMemory(){

        long totalMemory = 0L;
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader("/proc/meminfo");
            bufferedReader = new BufferedReader(fileReader, 4096);
            String content = bufferedReader.readLine();
            if (!TextUtils.isEmpty(content)) {
                String[] arrays = content.split("\\s+");
                if (arrays.length > 1) {
                    totalMemory = Long.parseLong(arrays[1]);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i(TAG,"device total memory=:"+totalMemory);
        return totalMemory << 10;
    }

    /**
     * 获取当前设备可用的内存
     * @return
     */
    public long getAvaliableMemory() {

        ActivityManager activityManager= (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if(activityManager!=null){
            activityManager.getMemoryInfo(memoryInfo);
        }
        Log.i(TAG,"device avaliable memory=:"+memoryInfo.availMem);
        return memoryInfo.availMem ;

    }


    /**
     * 异步订阅设备所有内存
     * @return
     */
    public Observable<Long> getDeviceTotalMemoryObservable() {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                e.onNext(getDeviceTotalMemory());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 异步订阅当前可用内存
     * @return
     */
    public Observable<Long> getAvalibleMemoryObservale(){
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                e.onNext(getAvaliableMemory());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


}
