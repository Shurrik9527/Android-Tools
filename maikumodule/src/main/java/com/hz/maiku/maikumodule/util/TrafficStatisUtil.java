package com.hz.maiku.maikumodule.util;

import android.Manifest;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.hz.maiku.maikumodule.bean.TrafficStatisBean;
import com.hz.maiku.maikumodule.bean.WifiMobileBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/31
 * @email 252774645@qq.com
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class TrafficStatisUtil {

    private static final String TAG = TrafficStatisUtil.class.getName();

    /**
     * 获取当月所有流量
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static WifiMobileBean getTotalTrafficStatistics(Context context, boolean isMonth) {
        if (context == null) {
            return null;
        }

        long time =0;
        if(isMonth){
            time =TimeUtil.getTimesMonthMorning();
        }else{
            time=TimeUtil.getTimesmorning();
        }

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        String subId = tm.getSubscriberId();

        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        try {
            NetworkStats.Bucket wifibucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, subId,time, System.currentTimeMillis());
            long wifiTotal = wifibucket.getRxBytes() + wifibucket.getTxBytes();
            NetworkStats.Bucket mobilebucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, subId, time, System.currentTimeMillis());
            long mobileTotal = mobilebucket.getRxBytes() + mobilebucket.getTxBytes();

            WifiMobileBean bean = new WifiMobileBean();
            bean.setMobileSize(mobileTotal);
            bean.setWifiSize(wifiTotal);
            bean.setStartTime(time);
            bean.setEndTime(System.currentTimeMillis());
            return bean;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static WifiMobileBean getMonthTotalTrafficStatistics(Context context,long startmonth,long endmonth) {
        if (context == null) {
            return null;
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        String subId = tm.getSubscriberId();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        try {
            //wlan
            NetworkStats.Bucket wifibucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, subId,startmonth,endmonth);
            long wifiTotal = wifibucket.getRxBytes() + wifibucket.getTxBytes();
            //mobile
            NetworkStats.Bucket mobilebucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, subId, startmonth,endmonth);
            long mobileTotal = mobilebucket.getRxBytes() + mobilebucket.getTxBytes();
            WifiMobileBean bean = new WifiMobileBean();
            bean.setMobileSize(mobileTotal);
            bean.setWifiSize(wifiTotal);

            return bean;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return  new WifiMobileBean();
    }







    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<TrafficStatisBean> getAppTrafficStatistics(Context context, boolean isMonth) {
        if (context == null)
            return null;

        long time =0;
        if(isMonth){
            time =TimeUtil.getTimesMonthMorning();
        }else{
            time =TimeUtil.getTimesmorning();
        }

        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        List<TrafficStatisBean> wifiLists = new ArrayList<>();
        List<TrafficStatisBean> mobileLists = new ArrayList<>();
        //wifi 使用
        try {
            NetworkStats.Bucket wifiBucket = new NetworkStats.Bucket();
            NetworkStats wifiStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, "", time, System.currentTimeMillis());
            do {
                wifiStats.getNextBucket(wifiBucket);
                int summaryUid = wifiBucket.getUid();
                long size = wifiBucket.getRxBytes() + wifiBucket.getTxBytes();
                TrafficStatisBean bean = new TrafficStatisBean();
                bean.setUid(String.valueOf(summaryUid));
                bean.setWifiSize(size);
                wifiLists.add(bean);

            } while (wifiStats.hasNextBucket());

            //mobile 使用

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            String subId = tm.getSubscriberId();

            NetworkStats.Bucket mobileBucket = new NetworkStats.Bucket();
            NetworkStats mobileStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE,subId,time, System.currentTimeMillis());
            if(mobileStats==null)
                return null;
            do {
                mobileStats.getNextBucket(mobileBucket);
                int summaryUid = mobileBucket.getUid();
                long size = mobileBucket.getRxBytes() + mobileBucket.getTxBytes();
                TrafficStatisBean bean = new TrafficStatisBean();
                bean.setUid(String.valueOf(summaryUid));
                bean.setMobileSize(size);
                mobileLists.add(bean);

            } while (mobileStats.hasNextBucket());


            //去重
            List<TrafficStatisBean> wifiList =removeDuplicate(wifiLists);
            List<TrafficStatisBean> mobileList =removeDuplicate(mobileLists);


            //合并
            List<TrafficStatisBean> lastLists =combine(wifiList,mobileList);
            //过滤未使用的流量app
            List<TrafficStatisBean> mlist =filterApp(context,lastLists);
            Collections.sort(mlist, new Comparator<TrafficStatisBean>() {
                @Override
                public int compare(TrafficStatisBean o1, TrafficStatisBean o2) {
                    if(o2.getTotalSize()-o1.getTotalSize()==0){
                        return 0;
                    }else if(o2.getTotalSize()-o1.getTotalSize()>0){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });
            return mlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<TrafficStatisBean> getMonthAppTrafficStatistics(Context context,long startmonth,long endmonth) {
        if (context == null)
            return null;

        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        List<TrafficStatisBean> wifiLists = new ArrayList<>();
        List<TrafficStatisBean> mobileLists = new ArrayList<>();
        //wifi 使用
        try {
            NetworkStats.Bucket wifiBucket = new NetworkStats.Bucket();
            NetworkStats wifiStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_WIFI, "",startmonth,endmonth);
            do {
                wifiStats.getNextBucket(wifiBucket);
                int summaryUid = wifiBucket.getUid();
                long size = wifiBucket.getRxBytes() + wifiBucket.getTxBytes();
                TrafficStatisBean bean = new TrafficStatisBean();
                bean.setUid(String.valueOf(summaryUid));
                bean.setWifiSize(size);
                wifiLists.add(bean);

            } while (wifiStats.hasNextBucket());

            //mobile 使用

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            String subId = tm.getSubscriberId();

            NetworkStats.Bucket mobileBucket = new NetworkStats.Bucket();
            NetworkStats mobileStats = networkStatsManager.querySummary(ConnectivityManager.TYPE_MOBILE,subId,startmonth,endmonth);
            if(mobileStats==null)
                return null;
            do {
                mobileStats.getNextBucket(mobileBucket);
                int summaryUid = mobileBucket.getUid();
                long size = mobileBucket.getRxBytes() + mobileBucket.getTxBytes();
                TrafficStatisBean bean = new TrafficStatisBean();
                bean.setUid(String.valueOf(summaryUid));
                bean.setMobileSize(size);
                mobileLists.add(bean);

            } while (mobileStats.hasNextBucket());

            //去重
            List<TrafficStatisBean> wifiList =removeDuplicate(wifiLists);
            List<TrafficStatisBean> mobileList =removeDuplicate(mobileLists);

            //合并
            List<TrafficStatisBean> lastLists =combine(wifiList,mobileList);
            //过滤未使用的流量app
            List<TrafficStatisBean> mlist =filterApp(context,lastLists);
            Collections.sort(mlist, new Comparator<TrafficStatisBean>() {
                @Override
                public int compare(TrafficStatisBean o1, TrafficStatisBean o2) {
                    if(o2.getTotalSize()-o1.getTotalSize()==0){
                        return 0;
                    }else if(o2.getTotalSize()-o1.getTotalSize()>0){
                        return 1;
                    }else{
                        return -1;
                    }
                }
            });
            return mlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<TrafficStatisBean> getTotalAppTrafficStatistics(Context context) {
        if (context == null)
            return null;

        List<PackageInfo> mList = AppUtil.getInstalledPackages(context);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        String subId = tm.getSubscriberId();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket wifiBucket = new NetworkStats.Bucket();
        List<TrafficStatisBean> mLists = new ArrayList<>();
        for (PackageInfo mbean : mList) {
            try {
                int uid = AppUtil.getUidByPackageName(context, mbean.packageName);
                NetworkStats wifiStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, subId, TimeUtil.getTimesMonthMorning(), System.currentTimeMillis(), uid);
                wifiStats.getNextBucket(wifiBucket);
                long size = wifiBucket.getRxBytes() + wifiBucket.getTxBytes();
                wifiStats.close();
                TrafficStatisBean bean = new TrafficStatisBean();
                bean.setmName(AppUtil.getApplicationName(context, mbean.applicationInfo));
                bean.setmDrawable(AppUtil.getIconByPkgname(context, mbean.packageName));
                bean.setmPackageName(mbean.packageName);
                bean.setWifiSize(size);
                mLists.add(bean);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }


        NetworkStats.Bucket mobileBucket = new NetworkStats.Bucket();
        for (int i = 0; i < mLists.size(); i++) {
            TrafficStatisBean mbean = mLists.get(i);
            try {
                int uid = AppUtil.getUidByPackageName(context, mbean.getmPackageName());
                NetworkStats mobileStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, subId, TimeUtil.getTimesMonthMorning(), System.currentTimeMillis(), uid);
                mobileStats.getNextBucket(mobileBucket);
                mobileStats.close();
                long size = mobileBucket.getRxBytes() + mobileBucket.getTxBytes();
                mbean.setMobileSize(size);
                mLists.set(i, mbean);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        return mLists;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static List<TrafficStatisBean> getAppMobileTrafficStatistics(Context context) {
        if (context == null)
            return null;

        List<PackageInfo> mList = AppUtil.getInstalledPackages(context);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        String subId = tm.getSubscriberId();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket wifiBucket = new NetworkStats.Bucket();
        List<TrafficStatisBean> mLists = new ArrayList<>();
        for (PackageInfo mbean : mList) {
            try {
                int uid = AppUtil.getUidByPackageName(context, mbean.packageName);
                NetworkStats wifiStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, subId, TimeUtil.getTimesMonthMorning(), System.currentTimeMillis(), uid);
                wifiStats.getNextBucket(wifiBucket);
                long size = wifiBucket.getRxBytes() + wifiBucket.getTxBytes();
                TrafficStatisBean bean = new TrafficStatisBean();
                bean.setmName(AppUtil.getApplicationName(context, mbean.applicationInfo));
                bean.setmDrawable(AppUtil.getIconByPkgname(context, mbean.packageName));
                bean.setmPackageName(mbean.packageName);
                bean.setWifiSize(size);
                mLists.add(bean);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        return null;

    }

    /**
     * 手机当天网络流量
     * @param context
     * @return
     */
    public long getAllTodayMobile(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context), TimeUtil.getTimesmorning(), System.currentTimeMillis());
        } catch (RemoteException e) {
            return 0;
        }
        return bucket.getTxBytes() + bucket.getRxBytes();
    }

    /**
     * 手机当天wifi流量
     * @param context
     * @return
     */
    public long getAllTodayWifi(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, getSubscriberId(context), TimeUtil.getTimesmorning(), System.currentTimeMillis());
        } catch (RemoteException e) {
            return 0;
        }
        return bucket.getTxBytes() + bucket.getRxBytes();
    }

    /**
     * 手机当月网络流量
     * @param context
     * @return
     */
    public long getAllMonthMobile(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context), TimeUtil.getTimesMonthMorning(), System.currentTimeMillis());
        } catch (RemoteException e) {
            return 0;
        }
        return bucket.getRxBytes() + bucket.getTxBytes();
    }


    /**
     * 手机当月wifi 流量
     * @param context
     * @return
     */
    public long getAllMonthWifi(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, getSubscriberId(context), TimeUtil.getTimesMonthMorning(), System.currentTimeMillis());
        } catch (RemoteException e) {
            return 0;
        }
        return bucket.getRxBytes() + bucket.getTxBytes();
    }

    /**
     * 开机到现在wifi s所有接受流量
     * @param context
     * @return
     */
    public static long getAllRxBytesWifi(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis());
            return bucket.getRxBytes();
        } catch (RemoteException e) {
            return 0;
        }
    }

    /**
     * 开机到现在wifi 所有发送流量
     * @param context
     * @return
     */
    public static long getAllTxBytesWifi(Context context) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis());
           long size =bucket.getTxBytes();
           return size;
        } catch (RemoteException e) {
            return 0;
        }
    }

    /**
     * 开机到现在网络 所有接受流量
     * @param context
     * @return
     */
    public static long getPackageRxBytesMobile(Context context,int packageUid) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context), 0, System.currentTimeMillis(), packageUid);
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            long size =bucket.getRxBytes();
            networkStats.close();
            return size;
        } catch (RemoteException e) {
            return 0;
        }
    }

    /**
     * 开机到现在网络 所有发送流量
     * @param context
     * @return
     */
    public static long getPackageTxBytesMobile(Context context,int packageUid) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, getSubscriberId(context), 0, System.currentTimeMillis(), packageUid);
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            long size =bucket.getTxBytes();
            networkStats.close();
            return size;
        } catch (RemoteException e) {
            return 0;
        }
    }

    /**
     * 开机到现在wifi 某个包所有接受流量
     * @param context
     * @return
     */
    public static long getPackageRxBytesWifi(Context context,int packageUid) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis(), packageUid);
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            long size =bucket.getRxBytes();
            networkStats.close();
            return size;
        } catch (RemoteException e) {
            return 0;
        }
    }



    /**
     * 开机到现在wifi 某个包所有发送流量
     * @param context
     * @return
     */
    public static long getPackageTxBytesWifi(Context context,int packageUid) {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats networkStats = null;
        try {
            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_WIFI, "", 0, System.currentTimeMillis(), packageUid);
            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
            networkStats.getNextBucket(bucket);
            long size =bucket.getTxBytes();
            networkStats.close();
            return size;
        } catch (RemoteException e) {
            return 0;
        }
    }


//    /**
//     * 开机到现在mobile 某个包所有发送流量
//     * @param context
//     * @return
//     */
//    public static long getPackageTxBytesMobile(Context context,int packageUid) {
//        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
//        NetworkStats networkStats = null;
//        try {
//            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, "", 0, System.currentTimeMillis(), packageUid);
//            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
//            networkStats.getNextBucket(bucket);
//            long size =bucket.getTxBytes();
//            networkStats.close();
//            return size;
//        } catch (RemoteException e) {
//            return 0;
//        }
//    }
//
//    /**
//     * 开机到现在mobile 某个包所有接受流量
//     * @param context
//     * @return
//     */
//    public static long getPackageRxBytesMobile(Context context,int packageUid) {
//        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(Context.NETWORK_STATS_SERVICE);
//        NetworkStats networkStats = null;
//        try {
//            networkStats = networkStatsManager.queryDetailsForUid(ConnectivityManager.TYPE_MOBILE, "", 0, System.currentTimeMillis(), packageUid);
//            NetworkStats.Bucket bucket = new NetworkStats.Bucket();
//            networkStats.getNextBucket(bucket);
//            long size =bucket.getRxBytes();
//            networkStats.close();
//            return size;
//        } catch (RemoteException e) {
//            return 0;
//        }
//    }



    private static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //
        //            final String actualSubscriberId = tm.getSubscriberId();
//            return SystemProperties.get(TEST_SUBSCRIBER_PROP, actualSubscriberId);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return tm.getSubscriberId();
    }

    /**
     * 排除重复的数据
     * @param TrafficStatisBeans
     * @return
     */
    private static List<TrafficStatisBean> removeDuplicate(List<TrafficStatisBean> TrafficStatisBeans){
        for (int i =0;i<TrafficStatisBeans.size()-1;i++){
            TrafficStatisBean o1 =TrafficStatisBeans.get(i);
            for (int j=TrafficStatisBeans.size()-1;j>i;j--){
                TrafficStatisBean o2 =TrafficStatisBeans.get(j);
                if(o1.getUid().compareTo(o2.getUid())==0){
                    long wifisize =o1.getWifiSize()+o2.getWifiSize();
                    long mobilesize =o1.getMobileSize()+o2.getMobileSize();
                    o1.setWifiSize(wifisize);
                    o1.setMobileSize(mobilesize);
                    TrafficStatisBeans.set(i,o1);
                    TrafficStatisBeans.remove(j);
                }
            }
        }
        return TrafficStatisBeans;
    }

    /**
     * 合并
     * @param wifibean
     * @param mobilebean
     * @return
     */
    private static List<TrafficStatisBean> combine(List<TrafficStatisBean> wifibean,List<TrafficStatisBean> mobilebean){

        if((wifibean==null||wifibean.size()==0)&&(mobilebean==null||mobilebean.size()==0)){
            return null;
        }

        if((wifibean!=null&&wifibean.size()>0)&&(mobilebean==null||mobilebean.size()==0)){
            return wifibean;
        }

        if((wifibean==null||wifibean.size()==0)&&(mobilebean!=null&&mobilebean.size()>0)){
            return mobilebean;
        }
        wifibean.addAll(mobilebean);
        return removeDuplicate(wifibean);

    }

    /**
     * 帅选 有流浪的APP
     * @param lists
     * @return
     */
    private static List<TrafficStatisBean> filterApp(Context context,List<TrafficStatisBean> lists){

        List<PackageInfo> mList = AppUtil.getInstalledPackages(context);
        if(mList==null||mList.size()==0){
            return null;
        }
        if(lists==null||lists.size()==0){
            return null;
        }

        for (int i =0;i<mList.size();i++){
            PackageInfo bean =mList.get(i);
            int uid = AppUtil.getUidByPackageName(context,bean.packageName);
            for (int j=0;j<lists.size();j++){
                TrafficStatisBean bean1 =lists.get(j);
                if(Integer.parseInt(bean1.getUid())==0){
                    lists.remove(j);
                    continue;
                }
                bean1.setTotalSize(bean1.getWifiSize()+bean1.getMobileSize());
                if(Integer.parseInt(bean1.getUid())==uid){
                    bean1.setmName(AppUtil.getApplicationName(context,bean.applicationInfo)+"");
                    bean1.setmDrawable(AppUtil.getIconByPkgname(context,bean.packageName));
                    bean1.setmPackageName(bean.packageName);
                    lists.set(j,bean1);
                }
            }
        }
        return lists;
    }



    public static List<WifiMobileBean> getYearTotalTrafficStatistics(Context context){

        long[] times =TimeUtil.getTimesYearMorning();
        long time =TimeUtil.getTimesMonthMorning();//当月
        List<WifiMobileBean> mlists = new ArrayList<>();
        for (int i =0 ;i<times.length;i++){
            WifiMobileBean bean =null;
            long temp =times[i];
            if(i==0){//当月
                bean =getMonthTotalTrafficStatistics(context,temp,System.currentTimeMillis());
                bean.setStartTime(temp);
                bean.setEndTime(System.currentTimeMillis());
                mlists.add(bean);
            }else {
                if(i<12){
                    long endtime =times[i-1];
                    bean =getMonthTotalTrafficStatistics(context,temp,endtime);
                    bean.setStartTime(temp);
                    bean.setEndTime(endtime);
                    mlists.add(bean);
                }
            }
        }
        return mlists;
    }

}
