package com.hz.maiku.maikumodule.manager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.hz.maiku.maikumodule.util.FileUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 清理管理器
 * @date 2018/9/7
 * @email 252774645@qq.com
 */
public class CleanManager {

    private static final  String TAG  =CleanManager.class.getName();
    public static CleanManager mInstance;
    private Context mContext;
    private  ClearUserDataObserver mClearUserDataObserver=null;
    //私有构造方法
    private CleanManager(Context context){
        this.mContext =context;
    }

    /**
     * 获取当前清理管理器
     * @return
     */
    public static CleanManager getmInstance(){
        if(mInstance==null){
            throw  new IllegalStateException("CleanManager is not init...");
        }
        return mInstance;
    }

    /**
     * 初始化清理管理器
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (CleanManager.class) {
                if (mInstance == null) {
                    mInstance = new CleanManager(context);
                }
            }
        }
    }


    /**
     * 订阅 垃圾清理
     * @param junkLists
     * @return
     */
    public Observable<Boolean> cleanJunksUsingObservable(final List<String> junkLists) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(junksClean(junkLists));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }




    /**
     * 垃圾清理
     * @param junkCleanerList
     * @return
     */
    public boolean junksClean(List<String> junkCleanerList) {

        if(junkCleanerList==null)
            return false;

        for (int i = 0; i < junkCleanerList.size(); i++) {
            try {

                FileUtil.deleteTarget(junkCleanerList.get(i));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     *订阅 清理App缓存
     * @param packageNameLists
     * @return
     */
    public Observable<Boolean> cleanAppsCacheObservable(final List<String> packageNameLists) {

        return Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(ObservableEmitter<Boolean>subscriber) throws Exception {
                subscriber.onNext(cleanAppsCache(packageNameLists));
                subscriber.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 根据包名清理app下的 缓存  由于是对别的APP缓存进行处理 用到反射机制
     * @param packageNameLists
     * @return
     */
    public boolean cleanAppsCache(List<String> packageNameLists) {
        Log.i(TAG,"packageNameLists="+packageNameLists.toString());
        if(packageNameLists==null||packageNameLists.size()==0||mContext==null)
            return false;

        File externalDir = mContext.getExternalCacheDir();
        if (externalDir == null) {
            return true;
        }

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        if(am==null){
            return false;
        }
        PackageManager pm = mContext.getPackageManager();
        if(pm==null){
            return false;
        }

        try {
            for(String pgm : packageNameLists){
                String externalCacheDir = externalDir.getAbsolutePath()
                        .replace(mContext.getPackageName(),pgm);
                File externalCache = new File(externalCacheDir);
                Log.i(TAG,"delete fifle is ="+externalCache);
                if (externalCache.exists()) {
                    Log.i(TAG,"delete fifle is exists");
                    FileUtil.deleteTarget(externalCacheDir);
                }else{
                    Log.i(TAG,"delete fifle is no exists");
                }
            }
        }catch (Exception e){

        }


        //利用反射机制 删除三方缓存
        //由于系统版本 6.0 以上 权限受到限制 这里区别处理
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

            if (mClearUserDataObserver == null) {
                mClearUserDataObserver = new ClearUserDataObserver();
            }
            for(int j =0; j<packageNameLists.size();j++){
                deleteCacheFile(pm,packageNameLists.get(j));
            }

        }else{
            final boolean[] isSuccess = {false};
            Class[] arrayOfClass = new Class[2];
            Class localClass2 = Long.TYPE;
            arrayOfClass[0] = localClass2;
            arrayOfClass[1] = IPackageDataObserver.class;
            Method localMethod = null;
            try {
                localMethod = pm.getClass().getMethod("freeStorageAndNotify", arrayOfClass);
                Long localLong = Long.MAX_VALUE;
                Object[] arrayOfObject = new Object[2];
                arrayOfObject[0] = localLong;
                try {
                    localMethod.invoke(pm,localLong,new IPackageDataObserver.Stub(){
                        public void onRemoveCompleted(String packageName,boolean succeeded) throws RemoteException {
                            // TODO Auto-generated method stub
                            Log.d(TAG, "delete Completed:====="+packageName);
                            isSuccess[0] = succeeded;
                        }});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

       return true;
    }


    public class PackageDataObserver extends IPackageDataObserver.Stub {
        @Override
        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
            Log.d(TAG, "delete Completed: "+succeeded);
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    }


    private void deleteCacheFile(PackageManager pm,String packageName) {
        if(pm==null|| TextUtils.isEmpty(packageName)){
            Log.d(TAG, "deleteCacheFile is fail");
            return;
        }
        Log.d(TAG, "deleteCacheFile Start....");
        try {
            Method deleteApplicationCacheFiles = PackageManager.class.getDeclaredMethod("deleteApplicationCacheFiles", String.class, IPackageDataObserver.class);
            deleteApplicationCacheFiles.invoke(pm, packageName, new PackageDataObserver());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    private static long getEnvironmentSize()
    {
        File localFile = Environment.getDataDirectory();
        long l1;
        if (localFile == null)
            l1 = 0L;
        while (true)
        {

            String str = localFile.getPath();
            StatFs localStatFs = new StatFs(str);
            long l2 = localStatFs.getBlockSize();
            l1 = localStatFs.getBlockCount() * l2;
            return l1;
        }
    }


    class ClearUserDataObserver extends IPackageDataObserver.Stub {
        public void onRemoveCompleted(final String packageName, final boolean succeeded) {
            Log.d(TAG, "delete Completed: "+succeeded);
        }
    }

}
