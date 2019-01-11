package com.hz.maiku.maikumodule.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.hz.maiku.maikumodule.base.MaiKuApp;
import com.hz.maiku.maikumodule.bean.ApkInformBean;

import java.io.File;

/**
 * @author heguogui
 * @describe 文件操作工具
 * @date 2018/9/5
 * @email 252774645@qq.com
 */
public class FileUtil {

    private static final  String TAG =FileUtil.class.getName();

    /**
     * 获取全路径中的文件名
     *
     * @param file 文件
     * @return 文件名
     */
    public static String getFileName(File file) {
        if (file == null) return null;
        return getFileName(file.getPath());
    }

    /**
     * 获取全路径中的文件名
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) return filePath;
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
    }

    public static String getExtension(String name) {
        String ext;

        if (name.lastIndexOf(".") == -1) {
            ext = "";

        } else {
            int index = name.lastIndexOf(".");
            ext = name.substring(index + 1, name.length());
        }
        return ext;
    }

    /**
     * 根据路径 获取apk
     * @param path
     * @return
     */
    public static ApkInformBean getApkInfo(String path) {
        ApkInformBean apkInfo = new ApkInformBean();
        PackageManager pm = MaiKuApp.getmContext().getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);

        if (packageInfo != null) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            apkInfo.setPackageName(applicationInfo.packageName);
            apkInfo.setVersionName(packageInfo.versionName);
            apkInfo.setVersionCode(packageInfo.versionCode);
            return apkInfo;
        }
        return null;
    }

    /**
     *根据路径删除目标文件
     * @param path
     */
    public static void deleteTarget(String path) {

        if(TextUtils.isEmpty(path))
            return;
        Log.i(TAG,"delete path="+path);
        File target = new File(path);
        if (target.isFile() && target.canWrite()) {
            target.delete();
        } else if (target.isDirectory() && target.canRead() && target.canWrite()) {
            String[] fileList = target.list();
            if (fileList != null && fileList.length == 0) {
                target.delete();
                return;
            } else if (fileList != null && fileList.length > 0) {
                for (String aFile_list : fileList) {
                    File tempF = new File(target.getAbsolutePath() + "/"
                            + aFile_list);
                    if (tempF.isDirectory())
                        deleteTarget(tempF.getAbsolutePath());
                    else if (tempF.isFile()) {
                        tempF.delete();
                    }
                }
            }

            if (target.exists())
                target.delete();
        }

    }





}
