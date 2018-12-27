package com.hz.maiku.maikumodule.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.SystemClock;

import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.CommLockInfo;
import com.hz.maiku.maikumodule.bean.FaviterInfo;
import com.hz.maiku.maikumodule.manager.CommLockInfoManager;
import com.hz.maiku.maikumodule.util.SpHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 进入app 后后台加载所有app
 * @date 2018/10/11
 * @email 252774645@qq.com
 */
public class LoadAppListService extends IntentService {

    public static final String TAG = LoadAppListService.class.getName();
    private PackageManager mPackageManager;
    private CommLockInfoManager mLockInfoManager;
    long time = 0;

    public LoadAppListService() {
        super("LoadAppListService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPackageManager = getPackageManager();
        mLockInfoManager = new CommLockInfoManager(this);
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel("id","name", NotificationManager.IMPORTANCE_LOW);
                final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(getApplicationContext(),"id").build();
                startForeground(100, notification);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 延迟1s
                        SystemClock.sleep(1000);
                        stopForeground(true);
                        // 移除Service弹出的通知
                        manager.cancel(100);
                    }
                }).start();
            }
        }catch (Exception e) {

        }
    }

    @Override
    protected void onHandleIntent(Intent handleIntent) {

        time = System.currentTimeMillis();

        boolean isInitFaviter = (boolean) SpHelper.getInstance().get(Constant.LOCK_IS_INIT_FAVITER, false);
        boolean isInitDb = (boolean) SpHelper.getInstance().get(Constant.LOCK_IS_INIT_DB, false);

        if (!isInitFaviter) {
            SpHelper.getInstance().put(Constant.LOCK_IS_INIT_FAVITER, true);
            initFavoriteApps();
        }

        //每次都获取手机上的所有应用
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(intent, 0);
        //非第一次，对比数据
        if (isInitDb) {
            List<ResolveInfo> appList = new ArrayList<>();
            List<CommLockInfo> dbList = mLockInfoManager.getAllCommLockInfos(); //获取数据库列表
            if(dbList==null||dbList.size()==0){
                return;
            }
            //处理应用列表
            for (ResolveInfo resolveInfo : resolveInfos) {
                if (!resolveInfo.activityInfo.packageName.equals(Constant.APP_PACKAGE_NAME) &&
                        !resolveInfo.activityInfo.packageName.equals("com.android.settings")) {
                    appList.add(resolveInfo);
                }
            }
            if (appList.size() > dbList.size()) { //如果有安装新应用
                List<ResolveInfo> reslist = new ArrayList<>();
                HashMap<String, CommLockInfo> hashMap = new HashMap<>();
                for (CommLockInfo info : dbList) {
                    hashMap.put(info.getPackageName(), info);
                }
                for (ResolveInfo info : appList) {
                    if (!hashMap.containsKey(info.activityInfo.packageName)) {
                        reslist.add(info);
                    }
                }
                try {
                    if (reslist.size() != 0)
                        mLockInfoManager.instanceCommLockInfoTable(reslist); //将剩下不同的插入数据库
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (appList.size() < dbList.size()) { //如果有卸载应用
                List<CommLockInfo> commlist = new ArrayList<>();
                HashMap<String, ResolveInfo> hashMap = new HashMap<>();
                for (ResolveInfo info : appList) {
                    hashMap.put(info.activityInfo.packageName, info);
                }
                for (CommLockInfo info : dbList) {
                    if (!hashMap.containsKey(info.getPackageName())) {
                        commlist.add(info);
                    }
                }
                //Logger.d("有应用卸载，个数是 = " + dbList.size());
                if (commlist.size() != 0)
                    mLockInfoManager.deleteCommLockInfoTable(commlist);//将多的从数据库删除
            } else {
                //Logger.d("应用没多没少，正常");
            }
        } else {
            //数据库只插入一次
            SpHelper.getInstance().put(Constant.LOCK_IS_INIT_DB, true);
            try {
                mLockInfoManager.instanceCommLockInfoTable(resolveInfos);    //插入数据库
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLockInfoManager = null;
    }

    /**
     * 初始化推荐加锁的应用
     */
    public void initFavoriteApps() {
        List<String> packageList = new ArrayList<>();
        List<FaviterInfo> faviterInfos = new ArrayList<>();
        packageList.add("com.android.gallery3d");       //相册
        packageList.add("com.android.mms");             //短信
        packageList.add("com.tencent.mm");              //微信
        packageList.add("com.android.contacts");        //联系人和电话
        packageList.add("com.facebook.katana");         //facebook
        packageList.add("com.facebook.orca");           //facebook Messenger
        packageList.add("com.mediatek.filemanager");    //文件管理器
        packageList.add("com.sec.android.gallery3d");   //也是个相册
        packageList.add("com.android.email");           //邮箱
        packageList.add("com.sec.android.app.myfiles"); //三星的文件
        packageList.add("com.android.vending");         //应用商店
        packageList.add("com.google.android.youtube");  //youtube
        packageList.add("com.tencent.mobileqq");        //qq
        packageList.add("com.tencent.qq");              //qq
        packageList.add("com.android.dialer");          //拨号
        packageList.add("com.twitter.android");         //twitter
        for (String packageName : packageList) {
            FaviterInfo info = new FaviterInfo();
            info.setPackageName(packageName);
            faviterInfos.add(info);
        }
        try {
            DataSupport.deleteAll(FaviterInfo.class);
            DataSupport.saveAll(faviterInfos);
        }catch (Exception e){

        }

    }



}
