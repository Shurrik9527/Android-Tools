package com.hz.maiku.maikumodule.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.hz.maiku.maikumodule.bean.AlbumBean;
import com.hz.maiku.maikumodule.bean.ImageBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 深度清理  相册中的图片 视频 及微信 QQ FACEBACK
 * @date 2019/1/4
 * @email 252774645@qq.com
 */
public class DeepCleanUtil {


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
                Cursor mCursor = mContentResolver.query(mImageUri, new String[]{MediaStore.Images.Media.DATA,MediaStore.Images.Media.DATE_ADDED}, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or "+MediaStore.Images.Media.MIME_TYPE + "=?", new String[]{"image/jpeg", "image/png","image/jpg"}, sortOrder+limit);
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
                    ImageBean imageBean = new ImageBean();
                    imageBean.setmTime(time);
                    imageBean.setSelect(false);
                    imageBean.setmUrl(path);
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


}
