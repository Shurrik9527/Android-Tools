package com.hz.maiku.maikumodule.manager;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.airbnb.lottie.L;
import com.hz.maiku.maikumodule.bean.NotificationBean;
import com.hz.maiku.maikumodule.bean.NotificationMsgBean;
import com.hz.maiku.maikumodule.util.AppUtil;

import org.litepal.crud.DataSupport;
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
 * @version v 3.0.0
 * @describe 通知欄管理
 * @date 2018/12/20
 * @email 252774645@qq.com
 */
public class NotificationsManager {


    private static final  String TAG  =NotificationsManager.class.getName();
    public static NotificationsManager mInstance;
    private Context mContext;

    //私有构造方法
    private NotificationsManager(Context context){
        this.mContext =context;
    }

    /**
     * 获取当前内存管理
     * @return
     */
    public static NotificationsManager getmInstance(){
        if(mInstance==null){
            throw  new IllegalStateException("NotificationManager is not init...");
        }
        return mInstance;
    }

    /**
     * 初始化内存管理器
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (NotificationsManager.class) {
                if (mInstance == null) {
                    mInstance = new NotificationsManager(context);
                }
            }
        }
    }

    /**
     * 查找所有通知
     */
    public synchronized List<NotificationMsgBean> getNotificationInfos() {
        try {
            return DataSupport.findAll(NotificationMsgBean.class);
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    /**
     * 删除某些通知
     */
    public synchronized void deleteNotificationInfoTable(List<NotificationMsgBean> notifications) {
        try {
            for (NotificationMsgBean info : notifications) {
                DataSupport.deleteAll(NotificationMsgBean.class, "addTime = ?", info.getAddTime());
            }
        }catch (Exception e){

        }
    }

    /**
     * 删除所有通知
     */
    public synchronized void deleteAllNotificationInfo() {
        try {
            DataSupport.deleteAll(NotificationMsgBean.class);
        }catch (Exception e){

        }
    }

    /**
     * 将通知插入数据库
     */
    public synchronized void addNotificationToTable(List<NotificationMsgBean> mlists) throws PackageManager.NameNotFoundException{
        try{
            if(mlists!=null&&mlists.size()>0){
                DataSupport.saveAll(mlists);
            }
        }catch (Exception e){

        }
    }


    /**
     * 将通知插入数据库
     */
    public synchronized boolean isContainsByTickerText(String tickerText) throws PackageManager.NameNotFoundException{
        try{
            return  DataSupport.isExist(NotificationMsgBean.class,"tickerText = ?",tickerText);
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 是否有
     */
    public synchronized boolean isContainsByKey(String key) throws PackageManager.NameNotFoundException{
        try{
            return  DataSupport.isExist(NotificationMsgBean.class,"mkey = ?",key);
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 将通知插入数据库
     */
    public synchronized boolean isContainsById(String id) throws PackageManager.NameNotFoundException{
        try{
            return  DataSupport.isExist(NotificationMsgBean.class,"mid = ?",id);
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 将通知插入数据库
     */
    public synchronized boolean isContainsByTime(String time) throws PackageManager.NameNotFoundException{
        try{
            return  DataSupport.isExist(NotificationMsgBean.class,"addTime = ?",time);
        }catch (Exception e){
            return false;
        }
    }


    //通知app状态
    /**
     * 入数据库
     */
    public synchronized void addNotificationAppStateToTable(List<NotificationBean> mlists) throws PackageManager.NameNotFoundException{
        try{
            if(mlists!=null&&mlists.size()>0){
                DataSupport.saveAll(mlists);
            }
        }catch (Exception e){

        }
    }


    /**
     * 查找所有通知
     */
    public synchronized List<NotificationBean> getAllNotificationAppInfos() {
        try {
            return DataSupport.findAll(NotificationBean.class);
        }catch (Exception e){

        }
        return new ArrayList<>();
    }

    /**
     * 删除所有
     */
    public synchronized void deleteAllNotificationAppInfo() {
        try {
            DataSupport.deleteAll(NotificationBean.class);
        }catch (Exception e){

        }
    }


    /**
     * 保存
     */
    public  void initAppSelectState(){

        Observable.create(new ObservableOnSubscribe<List<NotificationBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NotificationBean>> e) throws Exception {
                e.onNext(AppUtil.getSettingAppHomesApp(mContext));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<NotificationBean>>() {
            @Override
            public void accept(List<NotificationBean> mNotificationlists) throws Exception {
                addNotificationAppStateToTable(mNotificationlists);
            }
        });

    }

    public  Integer updadeAppSelectState(NotificationBean bean){
        try {
            ContentValues values = new ContentValues();
            values.put("isOpen", bean.isOpen());
            int flog =DataSupport.updateAll(NotificationBean.class, values, "AppPackageName = ?", bean.getAppPackageName());
            return flog;
        }catch (Exception e){
            return -1;
        }

    }


    /**
     * 保存
     */
    public  void updadeAppSelectStateOb(final NotificationBean bean){

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(updadeAppSelectState(bean));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer flog) throws Exception {
                Log.i(TAG,"修改flog=="+flog);
            }
        });

    }

    /**
     * 查找所有通知
     */
    public synchronized boolean isSettingApp(String packageNameStr) {
        try {
            ContentValues values = new ContentValues();
            values.put("isOpen",true);
            return DataSupport.isExist(NotificationBean.class,"AppPackageName = ?",packageNameStr);
        }catch (Exception e){

        }
        return false;
    }





}
