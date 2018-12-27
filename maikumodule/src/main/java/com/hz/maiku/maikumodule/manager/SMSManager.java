package com.hz.maiku.maikumodule.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.SmsBean;
import com.hz.maiku.maikumodule.event.RefreshSMSEvent;
import com.hz.maiku.maikumodule.util.RxBus.RxBus;
import com.hz.maiku.maikumodule.util.SMSUtils;

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
 * @describe 短信记录管理
 * @date 2018/10/22
 * @email 252774645@qq.com
 */
public class SMSManager {


    private static final  String TAG  =SMSManager.class.getName();
    public static SMSManager mInstance;
    private Context mContext;
    private  HarassInterceptManager manager;
    //私有构造方法
    private SMSManager(Context context){
        this.mContext =context;
        this.manager = new HarassInterceptManager(context);
    }

    /**
     * 获取当前内存管理
     * @return
     */
    public static SMSManager getmInstance(){
        if(mInstance==null){
            throw  new IllegalStateException("SMSManager is not init...");
        }
        return mInstance;
    }

    /**
     * 初始化内存管理器
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (SMSManager.class) {
                if (mInstance == null) {
                    mInstance = new SMSManager(context);
                }
            }
        }
    }



    /**
     * 异步订阅手机本身通短信
     * @return
     */
    public Observable<List<SmsBean>> getMobileSMSObservable() {

        try {
            Observable<List<SmsBean>> observable =Observable.create(new ObservableOnSubscribe<List<SmsBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<SmsBean>> e) throws Exception {
                    List<SmsBean> mlists = SMSUtils.getSmsInfo(mContext);
                    if(mlists!=null&&mlists.size()>0){
                        e.onNext(SMSUtils.getSmsInfo(mContext));
                    }
                }
            }).subscribeOn(Schedulers.io());
            return observable;
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 获取手机数据库的短信
     * @return
     */
    public Observable<List<SmsBean>> getSqliteSMSObservable() {
        try {
            Observable<List<SmsBean>> observable =Observable.create(new ObservableOnSubscribe<List<SmsBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<SmsBean>> e) throws Exception {
                    e.onNext(manager.getAllSMSInfos());
                    e.onComplete();
                }
            }).subscribeOn(Schedulers.io());
            return observable;
        }catch (Exception e){
            return null;
        }
    }


    public void updateSMSSqliteData(){


        try {

            Observable<List<SmsBean>> observable =getMobileSMSObservable();
            if(observable==null){
                return;
            }

            Observable.zip(observable,getSqliteSMSObservable(), new BiFunction<List<SmsBean>, List<SmsBean>, List<SmsBean>>() {
                @Override
                public List<SmsBean> apply(List<SmsBean> mSmsBean, List<SmsBean> mSmsBean2) throws Exception {
                    return SMSUtils.updateSMSSqlite(mSmsBean,mSmsBean2);
                }
            }).subscribeOn(Schedulers.io()).subscribe(new Consumer<List<SmsBean>>() {
                @Override
                public void accept(List<SmsBean> mSmsBean) throws Exception {
                    if(mSmsBean!=null&&mSmsBean.size()>0){
                        //先清理数据库
                        try {
                            if(manager!=null){
                                manager.deleteAllSMSInfo();
                                manager.instanceAllSMSInfoTable(mSmsBean);
                            }
                            Log.e(TAG,"更新短信成功...");
                            RxBus.getDefault().post(new RefreshSMSEvent());
                            return ;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.e(TAG,"更新短信记录失败或者手机本地短信没有数据");
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG,"更新通讯记录失败或者手机本地通讯录没有数据");
        }

    }


}
