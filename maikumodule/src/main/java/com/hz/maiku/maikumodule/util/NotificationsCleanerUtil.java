package com.hz.maiku.maikumodule.util;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.AppBean;
import com.hz.maiku.maikumodule.bean.NotificationBean;
import com.hz.maiku.maikumodule.bean.NotificationMsgBean;
import com.hz.maiku.maikumodule.manager.NotificationsManager;
import com.hz.maiku.maikumodule.service.NotificationService;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/19
 * @email 252774645@qq.com
 */
public class NotificationsCleanerUtil {


    private static final String TAG =NotificationsCleanerUtil.class.getName();
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context, PackageInfo appInfo) {

        if(context==null||appInfo==null){
            return false;
        }

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);

        Class appOpsClass = null;
        /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, appInfo.applicationInfo.uid, appInfo.packageName) == AppOpsManager.MODE_ALLOWED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static List<NotificationBean> checkAllAppIsOpenNotification(Context context){

        if(context==null){
            return null;
        }
        List<AppBean> infos =AppUtil.getHomesApp(context);
        if(infos==null||infos.size()==0){
            return null;
        }

        List<NotificationBean> mlists = new ArrayList<>();
        for (int i=0;i<infos.size();i++){
            AppBean info =infos.get(i);
            NotificationBean bean =new NotificationBean();
            bean.setAppName(info.getAppName());
            bean.setAppPackageName(info.getAppPackageName());
            if(isNotificationEnable(context,info.getAppPackageName())){
                bean.setOpen(true);
            }else{
                bean.setOpen(false);
            }
            mlists.add(bean);
        }
        return mlists;
    }


    /**
     * 判断本应用是否拥有通知使用权
     * @param context
     * @return
     */
    public static boolean notificationListenerEnable(Context context) {
        boolean enable = false;
        String packageName =context.getPackageName();
        String flat= Settings.Secure.getString(context.getContentResolver(),"enabled_notification_listeners");
        if (flat != null) {
            enable= flat.contains(packageName);
        }
        return enable;
    }

    /**
     * 判断应用是否拥有通知使用权
     * @param context
     * @return
     */
    public static boolean notificationListenerEnable(Context context,String packageName) {
        boolean enable = false;
        String flat= Settings.Secure.getString(context.getContentResolver(),"enabled_notification_listeners");
        if (flat != null) {
            enable= flat.contains(packageName);
        }
        return enable;
    }







    public static boolean isNotificationEnable(Context context,String packageName){
        if(context==null){
            return false;
        }
        if(TextUtils.isEmpty(packageName)){
            return false;
        }
        try {
            String selectApp = (String) SpHelper.getInstance().get(Constant.NOTIFICATION_SELECT_APP,"");
            if(!TextUtils.isEmpty(selectApp)){
                String contents =selectApp.substring(0,selectApp.length()-1);
                String[] packageNames =null;
                if(contents.contains(":")){
                    packageNames =contents.split(":");
                    for (int i=0;i<packageNames.length;i++){
                        String mPgName =packageNames[i];
                        if(mPgName.equals(packageName)){
                            return true;
                        }
                    }
                    return false;
                }else{
                    if(contents.equals(packageName)){
                        return true;
                    }else {
                        return false;
                    }
                }

            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }


    }

    /**
     * 杀死APP 后重启需要 重新授权
     * @param context
     */
    public static void toggleNotificationListenerService(Context context) {
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, NotificationService.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context, NotificationService .class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    /**
     * 添加
     * @param packageName
     */
    public static void addNotificationData(String packageName){

        if(TextUtils.isEmpty(packageName)){
            return;
        }
        String selectApp = (String) SpHelper.getInstance().get(Constant.NOTIFICATION_SELECT_APP,"");
        if(TextUtils.isEmpty(selectApp)){
            StringBuffer buffer = new StringBuffer();
            buffer.append(packageName).append(":");
            SpHelper.getInstance().put(Constant.NOTIFICATION_SELECT_APP,buffer.toString());
        }else{
            StringBuffer buffer = new StringBuffer();
            buffer.append(selectApp).append(packageName).append(":");
            SpHelper.getInstance().put(Constant.NOTIFICATION_SELECT_APP,buffer.toString());
        }

    }

    /**
     * 移除
     * @param packageName
     */
    public static void removeNotificationData(String packageName){

        if(TextUtils.isEmpty(packageName)){
            return;
        }
        try {
            String selectApp = (String) SpHelper.getInstance().get(Constant.NOTIFICATION_SELECT_APP,"");
            if(!TextUtils.isEmpty(selectApp)){
                String contents =selectApp.substring(0,selectApp.length()-1);
                String[] packageNames =null;
                if(contents.contains(":")){
                    packageNames =contents.split(":");
                    StringBuffer buffer =new StringBuffer();
                    for (int i=0;i<packageNames.length;i++){
                        String mPgName =packageNames[i];
                        if(!mPgName.equals(packageName)){
                            buffer.append(mPgName).append(":");
                        }
                    }
                    SpHelper.getInstance().put(Constant.NOTIFICATION_SELECT_APP,buffer.toString());
                }else{
                    if(contents.equals(packageName)){
                        SpHelper.getInstance().put(Constant.NOTIFICATION_SELECT_APP,"");
                    }
                }
            }
        }catch (Exception e){

        }
    }


    public static void  stopSound(Context context){
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("有待办消息未读")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                .setVibrate(new long[]{0})
                .setSound(null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("to-do", "待办消息",
                    NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("to-do");
        }

        Notification notification = builder.build();
        notificationManager.notify(0, notification);

    }

    /**
     * 处理获取的通知
     * @param statusBarNotifications
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public static boolean saveAllNotification(Context context,StatusBarNotification[] statusBarNotifications){

        if(statusBarNotifications==null||statusBarNotifications.length==0){
            return false;
        }
        try {
            for (int i = 0; i < statusBarNotifications.length; i++) {
                StatusBarNotification sbn = statusBarNotifications[i];
                String packageName =sbn.getPackageName();
                if(!TextUtils.isEmpty(packageName)){
                    //是否设置了
                    if(NotificationsManager.getmInstance().isSettingApp(packageName)){
//                    if(NotificationsCleanerUtil.isNotificationEnable(context,packageName)){
                        CharSequence tickerText = sbn.getNotification().tickerText;
                        if (!TextUtils.isEmpty(tickerText)) {
                            //!NotificationsManager.getmInstance().isContainsByKey(sbn.getKey())
                            if (!NotificationsManager.getmInstance().isContainsByTime(String.valueOf(sbn.getPostTime()))) {
                                NotificationMsgBean msgBean = new NotificationMsgBean();
                                Notification notification =sbn.getNotification();
                                Bundle extras =notification.extras;
                                String mTitleStr = extras.getString(Notification.EXTRA_TITLE);
                                CharSequence charSequence =extras.getCharSequence(Notification.EXTRA_TEXT);
                                String mContentStr =null;
                                if(!TextUtils.isEmpty(charSequence)){
                                    mContentStr=charSequence.toString();
                                }
                                msgBean.setAddTime(String.valueOf(sbn.getPostTime()));
                                msgBean.setMid(String.valueOf(sbn.getId()));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    msgBean.setMkey(sbn.getKey());
                                    msgBean.setMtag(sbn.getTag());
                                }
                                msgBean.setmNotification(sbn.getNotification());
                                msgBean.setTickerText(tickerText.toString());
                                if(!TextUtils.isEmpty(mContentStr)){
                                    msgBean.setContentStr(mContentStr);
                                }
                                if(!TextUtils.isEmpty(mTitleStr)){
                                    msgBean.setTitleStr(mTitleStr);
                                }
                                msgBean.setPackageName(packageName);
                                msgBean.setmNotification(notification);
                                List<NotificationMsgBean> msgBeanList = new ArrayList<>();
                                msgBeanList.add(msgBean);
                                NotificationsManager.getmInstance().addNotificationToTable(msgBeanList);
                            }
                        }
                    }
                }

            }
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }



}
