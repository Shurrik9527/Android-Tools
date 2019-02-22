package com.hz.maiku.maikumodule.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.MaiKuApp;
import com.hz.maiku.maikumodule.bean.AlbumBean;
import com.hz.maiku.maikumodule.bean.ApkBean;
import com.hz.maiku.maikumodule.bean.ApkInformBean;
import com.hz.maiku.maikumodule.bean.AppBean;
import com.hz.maiku.maikumodule.bean.AppDataBean;
import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.bean.BigFileBean;
import com.hz.maiku.maikumodule.bean.ImageBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerInformBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerMultiItemBean;
import com.hz.maiku.maikumodule.bean.VideoBean;
import com.hz.maiku.maikumodule.modules.deepclean.DeepCleanActivity;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.Icon;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 深度清理  相册中的图片 视频 及微信 QQ FACEBACK
 * @date 2019/1/4
 * @email 252774645@qq.com
 */
public class DeepCleanUtil {

    private static final String TAG =DeepCleanUtil.class.getName();

    /**
     * 获取所有的图片 png jpg
     * @param context
     * @return
     */
    public static List<AlbumBean> allAlbumFromLocalStorage(Context context){

        ArrayList<AlbumBean> albumList = new ArrayList<AlbumBean>();
        try {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            if(context == null||mImageUri==null)
                return albumList;
            ContentResolver mContentResolver = context.getContentResolver();
            String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
            Cursor countCursor = mContentResolver.query(mImageUri, new String[]{"COUNT(*) "}, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "+MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png","image/jpg"}, null);
            if(countCursor == null)
                return albumList;
            countCursor.moveToFirst();
            int photoCount = countCursor.getInt(0);//第一列
            countCursor.close();
            if(photoCount == 0)
                return albumList;
            HashMap<String,AlbumBean> albumMap = new HashMap<String, AlbumBean>();
            int index = 0;
            //获取手机的相册列表
            while (index < photoCount){//为了手机里有几万张照片OOM，500张为一组进行扫描相册列表
                String limit = " limit "+index+",500";
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, new String[]{MediaStore.Images.Media.DATA,MediaStore.Images.Media.DATE_ADDED,MediaStore.Images.Media.SIZE}, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "+MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png","image/jpg"}, sortOrder+limit);
                if(mCursor == null)
                    return albumList;
                int size = mCursor.getCount();
                index += size;
                if(size == 0)
                    continue;
                for (int i = 0; i < size; i++) {//遍历全部图片
                    mCursor.moveToPosition(i);
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String time = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    long mSize =mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                    ImageBean imageBean = new ImageBean();
                    imageBean.setmTime(time);
                    imageBean.setSelect(false);
                    imageBean.setmUrl(path);
                    imageBean.setSize(mSize);
                    //获取该图片的父路径名
                    String parentFolder = new File(path).getParentFile().getAbsolutePath();
                    //根据父路径名将图片放入到相册的HashMap中
                    if(albumMap.containsKey(parentFolder)){//这个相册已经被记录过了
                        AlbumBean album = albumMap.get(parentFolder);
                        album.imageCounts++;
                        if(album.getImageBeans()!=null){
                            album.getImageBeans().add(imageBean);
                        }
                    }else {//这个相册没有被记录过
                        File albumFolder = new File(parentFolder);
                        if(!albumFolder.exists())
                            continue;
                        AlbumBean mAlbumBean = new AlbumBean();
                        List<ImageBean> mlists = new ArrayList<>();
                        mlists.add(imageBean);
                        mAlbumBean.setImageBeans(mlists);
                        mAlbumBean.setAlbumFolder(albumFolder);
                        mAlbumBean.setFirstImagePath(path);
                        mAlbumBean.setImageCounts(1);
                        mAlbumBean.setFolderName(albumFolder.getName());
                        albumMap.put(parentFolder,mAlbumBean);
                    }
                }
                mCursor.close();
            }
            Set<String> keyset = albumMap.keySet();
            for(String albumPath:keyset)
                albumList.add(albumMap.get(albumPath));
            return albumList;
        }catch (Exception e){
            return albumList;
        }

    }


    /**
     * 删除文件
     * @param context
     */
    public static boolean deleteFile(final  Context context, final List<ImageBean> list){
        if(list==null||list.size()==0){
            return false;
        }
        try {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = context.getContentResolver();
            int flog =0;
            for (ImageBean bean :list){
                String where = MediaStore.Images.ImageColumns.DATA + " like '%" + bean.getmUrl() + "%'";
                flog =flog+mContentResolver.delete(uri, where, null);
            }
            if(flog>0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 更新媒体库
     * @param context
     * @param path
     */
    public static void updateMediaStore(final  Context context, final String path) {
        //版本号的判断  4.4为分水岭，发送广播更新媒体库
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            MediaScannerConnection.scanFile(context, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uri);
                    context.sendBroadcast(mediaScanIntent);
                }
            });
        } else {
            File file = new File(path);
            String relationDir = file.getParent();
            File file1 = new File(relationDir);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(file1.getAbsoluteFile())));
        }
    }


    /**
     * 获取所有录音
     * @param context
     * @return
     */
    public static ArrayList<VideoBean> getAllVideo(Context context) {
        ArrayList<VideoBean> videos = new ArrayList<VideoBean>();
        String[] mediaColumns = new String[]{
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.VideoColumns.DISPLAY_NAME,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.DATE_ADDED,
                MediaStore.Video.VideoColumns.SIZE
        };
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, MediaStore.Video.VideoColumns.DATE_ADDED + " DESC");
        if (cursor.moveToFirst()) {
            do {
                VideoBean fileItem = new VideoBean();
                fileItem.setmUrl(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                File file = new File(fileItem.getmUrl());
                boolean canRead = file.canRead();
                long length = file.length();
                if (!canRead || length == 0) {
                    continue;
                }
                fileItem.setmName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                long mSize =cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                if (duration < 0)
                    duration = 0;
                fileItem.setmDuration(duration);
                if(mSize>0)
                    fileItem.setSize(mSize);
                if (fileItem.getmName() != null && (fileItem.getmName().endsWith(".mp4")||fileItem.getmName().endsWith(".amr")||fileItem.getmName().endsWith(".3gp")||fileItem.getmName().endsWith(".aac"))) {
                    videos.add(fileItem);
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return videos;
    }


    /**
     * 获取录音截图
     * @param filePath
     * @return
     */
    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }




    /**
     * 删除录像文件
     * @param context
     */
    public static boolean deleteVideoFile(final  Context context, final List<VideoBean> list){
        if(list==null||list.size()==0){
            return false;
        }
        try {
            Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = context.getContentResolver();
            int flog =0;
            for (VideoBean bean :list){
                String where = MediaStore.Video.VideoColumns.DATA + " like '%" + bean.getmUrl() + "%'";
                flog =flog+mContentResolver.delete(uri, where, null);
            }
            if(flog>0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }




    public static List<ApkBean> getAllUnInstallApk(){
        if(!SDUtil.isSDCardAvailable()){
            return new ArrayList<>();
        }
        File externalDir = Environment.getExternalStorageDirectory();
        List<ApkBean> mLists = new ArrayList<>();
        return selectApk(externalDir,mLists);
    }

    public static List<ApkBean> selectApk(File externalDir,List<ApkBean> list){
        if(externalDir!=null&&externalDir.exists()){
            File[] files = externalDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (MimeTypes.isApk(file)) {
                            if(file.length()==0){
                                continue;
                            }
                            ApkBean mBean = new ApkBean();
                            mBean.setmSize(file.length());
                            mBean.setmUrl(file.getAbsolutePath());
                            if(mBean.getmUrl()!=null){
                                ApkFile mApkFile =AppUtil.readApkInform(mBean.getmUrl());
                                if(mApkFile!=null){
                                    try {
                                        ApkMeta mApkMeta =mApkFile.getApkMeta();
                                        if(mApkMeta!=null){
                                            if(!TextUtils.isEmpty(mApkMeta.getName())){
                                                mBean.setmName(mApkMeta.getName());
                                            }
                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        Icon mIcon =mApkFile.getIconFile();
                                        if(mIcon!=null&&mIcon.getData().length>0){
                                            Bitmap bitmap =BitmapFactory.decodeByteArray(mIcon.getData(),0,mIcon.getData().length);
                                            if(bitmap!=null){
                                               mBean.setmIcon(bitmap);
                                            }
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }

                            mBean.setSelect(false);
                            list.add(mBean);
                        }
                    }else{
                        selectApk(file,list);
                    }
                }
                return list;
            }else{
                return  new ArrayList<>();
            }
        }else{
            return  new ArrayList<>();
        }
    }


    /**
     * 删除sd卡apk文件
     * @param list
     * @return
     */
    public static boolean deleteApkFile(final List<ApkBean> list){

        if(list==null||list.size()==0){
            return false;
        }
        try {
            for (ApkBean bean :list){
               FileUtil.deleteTarget(bean.getmUrl());
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 删除sd卡zip文件
     * @param list
     * @return
     */
    public static boolean deleteBigFileFile(final List<BigFileBean> list){

        if(list==null||list.size()==0){
            return false;
        }
        try {
            for (BigFileBean bean :list){
                FileUtil.deleteTarget(bean.getmUrl());
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 删除sd卡app 图片文件
     * @param list
     * @return
     */
    public static boolean deleteImageFile(final List<ImageBean> list){

        if(list==null||list.size()==0){
            return false;
        }
        try {
            for (ImageBean bean :list){
                FileUtil.deleteTarget(bean.getmUrl());
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 获取所有录音
     * @param context
     * @return
     */
    public static ArrayList<AudioBean> getAllAudios(Context context) {
        ArrayList<AudioBean> audios = new ArrayList<AudioBean>();
        String[] mediaColumns = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.DURATION

        };
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, 				MediaStore.Audio.Media.MIME_TYPE + "=? or "
                        + MediaStore.Audio.Media.MIME_TYPE + "=?",
                new String[] { "audio/mpeg", "audio/x-ms-wma"}, null);

        if (cursor.moveToFirst()) {
            do {
                AudioBean fileItem = new AudioBean();
                fileItem.setmUrl(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                File file = new File(fileItem.getmUrl());
                boolean canRead = file.canRead();
                long length = file.length();
                if (!canRead || length == 0) {
                    continue;
                }
                fileItem.setmName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                long mSize =cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));

                if (duration < 0||duration==0)
                    continue;

                fileItem.setmDuration(duration);
                if(mSize>0)
                    fileItem.setSize(mSize);
                String type =cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
                if (!TextUtils.isEmpty(type)) {
                    if(type.equals("audio/mpeg")){
                        fileItem.setType("mp3");
                        audios.add(fileItem);
                    }
                    if(type.equals("audio/x-ms-wma")){
                        fileItem.setType("wma");
                        audios.add(fileItem);
                    }
                }
//                if (fileItem.getmName() != null && (fileItem.getmName().endsWith(".wav")||fileItem.getmName().endsWith(".amr"))) {
//                    audios.add(fileItem);
//                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return audios;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<AudioBean> getAllAudioSpecialApp(String content){
        if(!SDUtil.isSDCardAvailable()){
            return new ArrayList<>();
        }
        File externalDir = Environment.getExternalStorageDirectory();
        List<AudioBean> audiosLists = new ArrayList<>();
       // audiosLists.addAll(selectAudios(new File(externalDir.getAbsolutePath()+"/"+content),audiosLists));
        return selectAudios(new File(externalDir.getAbsolutePath()+"/"+content),audiosLists);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<AudioBean> getAllAudios(){

        if(!SDUtil.isSDCardAvailable()){
            return new ArrayList<>();
        }
        File externalDir = Environment.getExternalStorageDirectory();
        List<AudioBean> mLists = new ArrayList<>();
        return selectAudios(externalDir,mLists);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<AudioBean> selectAudios(File externalDir, List<AudioBean> list){
        try {
            if(externalDir!=null&&externalDir.exists()){
                File[] files = externalDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            if(file.length()==0){
                                continue;
                            }
                            AudioBean mBean = new AudioBean();
                            mBean.setSize(file.length());
                            mBean.setmUrl(file.getAbsolutePath());
                            mBean.setSelect(false);
                            if (MimeTypes.isAmr(file)) {
                                mBean.setType("amr");
                                mBean.setmName("audio.amr");
                                list.add(mBean);
                            }

                            if(MimeTypes.isWav(file)){
                                mBean.setType("wav");
                                mBean.setmName("audio.wav");
                                list.add(mBean);
                            }

                            if(MimeTypes.isMp3(file)){
                                mBean.setType("mp3");
                                mBean.setmName("audio.mp3");
                                list.add(mBean);
                            }

                            if(MimeTypes.isWma(file)){
                                mBean.setType("wma");
                                mBean.setmName("audio.wma");
                                list.add(mBean);
                            }

                        }else{
                            selectAudios(file,list);
                        }
                    }
                    return list;
                }else{
                    return  new ArrayList<>();
                }
            }else{
                return  new ArrayList<>();
            }
        }catch (Exception e){
            return  new ArrayList<>();
        }

    }

    /**
     * 获取数据库Audio的数据
     * @return
     */
    public static  Observable<List<AudioBean>> getAllAudiosObservable() {
        return Observable.create(new ObservableOnSubscribe<List<AudioBean>>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void subscribe(ObservableEmitter<List<AudioBean>> e) throws Exception {
                e.onNext(getAllAudios());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取SDAudio的数据
     * @return
     */
    public static Observable<List<AudioBean>> getAllAudiosObservable(final Context context) {
        return Observable.create(new ObservableOnSubscribe<List<AudioBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<AudioBean>> e) throws Exception {
                e.onNext(getAllAudios(context));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 删除语音数据
     * @return
     */
    public static Observable<Boolean> deleteAllAudiosObservable(final Context context, final List<AudioBean> list) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteAudiosFile(context,list));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 删除语音数据
     * @return
     */
    public static Observable<Boolean> deleteAllAudiosObservable(final List<AudioBean> list) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(deleteAudiosFile(list));
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 删除数据库语音文件
     */
    public static boolean deleteAudiosFile(final  Context context, final List<AudioBean> list){
        if(list==null||list.size()==0){
            return false;
        }
        try {
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = context.getContentResolver();
            int flog =0;
            for (AudioBean bean :list){
                String where = MediaStore.Audio.Media.DATA + " like '%" + bean.getmUrl() + "%'";
                flog =flog+mContentResolver.delete(uri, where, null);
            }
            if(flog>0){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 删除sd卡语音文件
     * @param list
     * @return
     */
    public static boolean deleteAudiosFile(final List<AudioBean> list){

        if(list==null||list.size()==0){
            return false;
        }
        try {
            for (AudioBean bean :list){
                FileUtil.deleteTarget(bean.getmUrl());
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<AppDataBean> getAllSpecialApk(List<AppBean> lists){
        if(!SDUtil.isSDCardAvailable()){
            return new ArrayList<>();
        }
        File externalDir = Environment.getExternalStorageDirectory();

        List<AppDataBean> mLists = new ArrayList<>();
        Log.i(TAG,"==externalDir="+externalDir.getAbsolutePath());

        for (AppBean mAppBean :lists){
            //微信
            if(mAppBean.getAppName().equals(AppUtil.weixing_name)){
                AppDataBean mBean = new AppDataBean();
                mBean.setAppName(mAppBean.getAppName());
                mBean.setAppSize(mAppBean.getAppSize());
                mBean.setAppIcon(mAppBean.getAppIcon());
                List<ImageBean> imagesLists = new ArrayList<>();
                List<AudioBean> audioiLists = new ArrayList<>();
                List<VideoBean> videoLists = new ArrayList<>();
                mBean.setImageBean(selectImageSpecialApk(new File(externalDir.getAbsolutePath()+"/tencent/MicroMsg"),imagesLists));
                mBean.setAudioBean(selectAudios(new File(externalDir.getAbsolutePath()+"/tencent/MicroMsg"),audioiLists));
                mBean.setVideoBean(selectVideoSpecialApk(new File(externalDir.getAbsolutePath()+"/tencent/MicroMsg"),videoLists));
                mLists.add(mBean);
            }

            //qq
            if(mAppBean.getAppName().equals(AppUtil.qq_name)){
                AppDataBean mBean = new AppDataBean();
                mBean.setAppName(mAppBean.getAppName());
                mBean.setAppSize(mAppBean.getAppSize());
                mBean.setAppIcon(mAppBean.getAppIcon());
                List<ImageBean> imagesLists = new ArrayList<>();
                List<AudioBean> audioiLists = new ArrayList<>();
                List<VideoBean> videoLists = new ArrayList<>();
                mBean.setImageBean(selectImageSpecialApk(new File(externalDir.getAbsolutePath()+"/tencent/MobileQQ"),imagesLists));
                mBean.setAudioBean(selectAudios(new File(externalDir.getAbsolutePath()+"/tencent/MobileQQ"),audioiLists));
                mBean.setVideoBean(selectVideoSpecialApk(new File(externalDir.getAbsolutePath()+"/tencent/MobileQQ"),videoLists));
                mLists.add(mBean);
            }
            //instagram
            if(mAppBean.getAppName().equals(AppUtil.instagram_name)){
                AppDataBean mBean = new AppDataBean();
                mBean.setAppName(mAppBean.getAppName());
                mBean.setAppSize(mAppBean.getAppSize());
                mBean.setAppIcon(mAppBean.getAppIcon());
                List<ImageBean> imagesLists = new ArrayList<>();
                List<AudioBean> audioiLists = new ArrayList<>();
                List<VideoBean> videoLists = new ArrayList<>();
                mBean.setImageBean(selectImageSpecialApk(new File(externalDir.getAbsolutePath()+"/Pictures/Instagram"),imagesLists));
                mBean.setAudioBean(selectAudios(new File(externalDir.getAbsolutePath()+"/Rewords/Instagram"),audioiLists));
                mBean.setVideoBean(selectVideoSpecialApk(new File(externalDir.getAbsolutePath()+"/Movies/Instagram"),videoLists));
                mLists.add(mBean);
            }

            if(mAppBean.getAppName().equals(AppUtil.facebook_name)){
                AppDataBean mBean = new AppDataBean();
                mBean.setAppName(mAppBean.getAppName());
                mBean.setAppSize(mAppBean.getAppSize());
                mBean.setAppIcon(mAppBean.getAppIcon());
                List<ImageBean> imagesLists = new ArrayList<>();
                List<AudioBean> audioiLists = new ArrayList<>();
                List<VideoBean> videoLists = new ArrayList<>();
                mBean.setImageBean(selectImageSpecialApk(new File(externalDir.getAbsolutePath()+"/Pictures/Facebook"),imagesLists));
                mBean.setAudioBean(selectAudios(new File(externalDir.getAbsolutePath()+"/Rewords/Facebook"),audioiLists));
                mBean.setVideoBean(selectVideoSpecialApk(new File(externalDir.getAbsolutePath()+"/Movies/Facebook"),videoLists));
                mLists.add(mBean);
            }


        }
        return mLists;
    }



    public static List<ImageBean> getAllSpecialApkImage(String content){
        if(!SDUtil.isSDCardAvailable()){
            return new ArrayList<>();
        }
        File externalDir = Environment.getExternalStorageDirectory();
        List<ImageBean> imagesLists = new ArrayList<>();
        return selectImageSpecialApk(new File(externalDir.getAbsolutePath()+"/"+content),imagesLists);
    }



    public static List<ImageBean> selectImageSpecialApk(File externalDir,List<ImageBean> imagesLists){
        if(externalDir!=null&&externalDir.exists()){
            File[] files = externalDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (MimeTypes.isPng(file)) {
                            ImageBean mImageBean = new ImageBean();
                            mImageBean.setSize(file.length());
                            mImageBean.setmUrl(file.getAbsolutePath());
                            mImageBean.setSelect(false);
                            imagesLists.add(mImageBean);
                        }

                        if (MimeTypes.isJpg(file)) {
                            ImageBean mImageBean = new ImageBean();
                            mImageBean.setSize(file.length());
                            mImageBean.setmUrl(file.getAbsolutePath());
                            mImageBean.setSelect(false);
                            imagesLists.add(mImageBean);
                        }

                        if (MimeTypes.isJpeg(file)) {
                            ImageBean mImageBean = new ImageBean();
                            mImageBean.setSize(file.length());
                            mImageBean.setmUrl(file.getAbsolutePath());
                            mImageBean.setSelect(false);
                            imagesLists.add(mImageBean);
                        }

                    }else{
                        selectImageSpecialApk(file,imagesLists);
                    }
                }
                return imagesLists;
            }else{
                return  new ArrayList<>();
            }
        }else{
            return  new ArrayList<>();
        }
    }


    public static List<VideoBean> getAllVideoSpecialApp(String content){
        if(!SDUtil.isSDCardAvailable()){
            return new ArrayList<>();
        }
        File externalDir = Environment.getExternalStorageDirectory();
        List<VideoBean> videoBeanLists = new ArrayList<>();
        //videoBeanLists.addAll(selectVideoSpecialApk(new File(externalDir.getAbsolutePath()+"/"+content),videoBeanLists));
        return selectVideoSpecialApk(new File(externalDir.getAbsolutePath()+"/"+content),videoBeanLists);
    }



    public static List<VideoBean> selectVideoSpecialApk(File externalDir,List<VideoBean> videoLists){
        if(externalDir!=null&&externalDir.exists()){
            File[] files = externalDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {

                        if (MimeTypes.isMp4(file)) {
                            VideoBean mVideoBean = new VideoBean();
                            mVideoBean.setSize(file.length());
                            mVideoBean.setmUrl(file.getAbsolutePath());
                            mVideoBean.setSelect(false);
                            videoLists.add(mVideoBean);
                        }
                        if (MimeTypes.is3gp(file)) {
                            VideoBean mVideoBean = new VideoBean();
                            mVideoBean.setSize(file.length());
                            mVideoBean.setmUrl(file.getAbsolutePath());
                            mVideoBean.setSelect(false);
                            videoLists.add(mVideoBean);
                        }


                    }else{
                        selectVideoSpecialApk(file,videoLists);
                    }
                }
                return videoLists;
            }else{
                return  new ArrayList<>();
            }
        }else{
            return  new ArrayList<>();
        }
    }




    public static List<BigFileBean> getAllBigFiles(){
        if(!SDUtil.isSDCardAvailable()){
            return new ArrayList<>();
        }
        File externalDir = Environment.getExternalStorageDirectory();
        List<BigFileBean> mLists = new ArrayList<>();
        return selectBigFile(externalDir,mLists);
    }

    public static List<BigFileBean> selectBigFile(File externalDir,List<BigFileBean> list){
        try {
            if(externalDir!=null&&externalDir.exists()){
                File[] files = externalDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            if (MimeTypes.isZipFile(file)) {
                                if(file.length()==0){
                                    continue;
                                }
                                BigFileBean mBigFileBean = new BigFileBean();
                                String path =file.getAbsolutePath();
                                int length =path.lastIndexOf("/");
                                mBigFileBean.setmName(path.substring(length+1,path.length()));
                                mBigFileBean.setSize(file.length());
                                mBigFileBean.setmUrl(path);
                                mBigFileBean.setSelect(false);
                                list.add(mBigFileBean);
                            }
                        }else{
                            selectBigFile(file,list);
                        }
                    }
                    return list;
                }else{
                    return  new ArrayList<>();
                }
            }else{
                return  new ArrayList<>();
            }
        }catch (Exception e){
            return  new ArrayList<>();
        }
    }

    /**
     * 删除sd卡 Video文件
     * @param list
     * @return
     */
    public static boolean deleteSpecialVideoFile(final List<VideoBean> list){

        if(list==null||list.size()==0){
            return false;
        }
        try {
            for (VideoBean bean :list){
                FileUtil.deleteTarget(bean.getmUrl());
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }




}
