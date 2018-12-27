package com.hz.maiku.maikumodule.manager;

import android.content.Context;
import android.os.Environment;

import com.hz.maiku.maikumodule.util.FileUtil;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 本应用sd卡内存管理 ActivityManager.clearApplicationUserData() 清理本应用手机缓存
 * @date 2018/9/17
 * @email 252774645@qq.com
 */
public class StorageManager {

    private static StorageManager mInstance;
    private Context mContext;

    private StorageManager(Context context) {

        mContext = context.getApplicationContext();
    }

    public static void init(Context context) {

        if (mInstance == null) {
            synchronized (StorageManager.class) {
                if (mInstance == null) {
                    mInstance = new StorageManager(context);
                }
            }
        }
    }

    public StorageManager getInstance() {

        if (mInstance == null) {
            throw new IllegalStateException("You must be init StorageManager first");
        }
        return mInstance;
    }






    /**
     * 获取sd卡缓存大小
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (existsSdcard()) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清除缓存
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (existsSdcard()) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static void deleteDir(File dir) {
        if(dir != null||!dir.exists())
            return ;
        if (dir.isDirectory()) {
            FileUtil.deleteTarget(dir.getAbsolutePath());
        }
    }

    // 获取文件大小
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }


    public static Boolean existsSdcard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable();
    }

}
