package com.hz.maiku.maikumodule.bean;

import java.io.File;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/4
 * @email 252774645@qq.com
 */
public class AlbumBean implements Comparable<AlbumBean>{

    public String firstImagePath;//文件夹的第一张图片路径
    public String folderName;//文件夹名
    public int imageCounts;//文件夹中的图片数
    public File albumFolder;//文件
    public List<ImageBean> imageBeans;

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getImageCounts() {
        return imageCounts;
    }

    public void setImageCounts(int imageCounts) {
        this.imageCounts = imageCounts;
    }

    public File getAlbumFolder() {
        return albumFolder;
    }

    public void setAlbumFolder(File albumFolder) {
        this.albumFolder = albumFolder;
    }

    public List<ImageBean> getImageBeans() {
        return imageBeans;
    }

    public void setImageBeans(List<ImageBean> imageBeans) {
        this.imageBeans = imageBeans;
    }

    @Override
    public String toString() {
        return "AlbumBean{" +
                "firstImagePath='" + firstImagePath + '\'' +
                ", folderName='" + folderName + '\'' +
                ", imageCounts=" + imageCounts +
                ", albumFolder=" + albumFolder +
                ", imageBeans=" + imageBeans +
                '}';
    }

    @Override public int compareTo(AlbumBean o) {
        int level1 = this.getImageCounts();
        int level2 = o.getImageCounts();
        if(level1<level2){
            return 1;
        }else if(level1>level2){
            return -1;
        }else{
            return 0;
        }
    }
}
