package com.hz.maiku.maikumodule.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.hz.maiku.maikumodule.bean.CallLogBean;
import com.hz.maiku.maikumodule.util.PhoneUtils;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 通讯记录管理
 * @date 2018/10/22
 * @email 252774645@qq.com
 */
public class CallLogManager {


    private static final  String TAG  =CallLogManager.class.getName();
    public static CallLogManager mInstance;
    private Context mContext;
    private  HarassInterceptManager manager;
    //私有构造方法
    private CallLogManager(Context context){
        this.mContext =context;
        this.manager = new HarassInterceptManager(context);
    }

    /**
     * 获取当前内存管理
     * @return
     */
    public static CallLogManager getmInstance(){
        if(mInstance==null){
            throw  new IllegalStateException("CallLogManager is not init...");
        }
        return mInstance;
    }

    /**
     * 初始化内存管理器
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (CallLogManager.class) {
                if (mInstance == null) {
                    mInstance = new CallLogManager(context);
                }
            }
        }
    }


    /**
     * 异步订阅手机本身通讯录
     * @return
     */
    public Observable<List<CallLogBean>> getMobileCallLogObservable() {
        try {
            Observable<List<CallLogBean>> observable =Observable.create(new ObservableOnSubscribe<List<CallLogBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<CallLogBean>> e) throws Exception {
                    List<CallLogBean> mlist = PhoneUtils.getCallLogLists(mContext);
                    if(mlist!=null&&mlist.size()>0){
                        e.onNext(mlist);
                    }
                }
            }).subscribeOn(Schedulers.io());
            return observable;
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 获取手机数据库的通讯录
     * @return
     */
    public Observable<List<CallLogBean>> getSqliteCallLogObservable() {
        return Observable.create(new ObservableOnSubscribe<List<CallLogBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CallLogBean>> e) throws Exception {
                e.onNext(manager.getAllPhoneInfos());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
    }


    public void updateCallLogSqliteData(){
        try {

            Observable<List<CallLogBean>> observable =getMobileCallLogObservable();
            if(observable==null){
                return;
            }

            Observable.zip(observable,getSqliteCallLogObservable(), new BiFunction<List<CallLogBean>, List<CallLogBean>, List<CallLogBean>>() {
                @Override
                public List<CallLogBean> apply(List<CallLogBean> mCallLogBean, List<CallLogBean> mCallLogBeanBeans2) throws Exception {
                    return PhoneUtils.updateCallLogSqlite(mCallLogBean,mCallLogBeanBeans2);
                }
            }).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<CallLogBean>>() {
                @Override
                public void accept(List<CallLogBean> mCallLogBean) throws Exception {
                    if(mCallLogBean!=null&&mCallLogBean.size()>0){
                        //先清理数据库
                        try {
                            if(manager!=null){
                                manager.deleteAllCallLogInfo();
                                manager.instanceAllCallLogInfoTable(mCallLogBean);
                            }
                            Log.e(TAG,"更新通讯记录成功...");
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.e(TAG,"更新通讯记录失败或者手机本地通讯录没有数据");
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG,"更新通讯记录失败或者手机本地通讯录没有数据");
        }

    }

    
}
