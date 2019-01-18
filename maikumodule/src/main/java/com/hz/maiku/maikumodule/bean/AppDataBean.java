package com.hz.maiku.maikumodule.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/15
 * @email 252774645@qq.com
 */
public class AppDataBean implements Serializable {

    private String AppName;
    private Drawable AppIcon;
    private String AppPackageName;
    private long AppSize;
    private List<ImageBean> imageBean;
    private List<AudioBean> audioBean;
    private List<VideoBean> videoBean;
    private long ImageSize;
    private long videoSize;
    private long audioSize;
    private boolean isSelect;

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public Drawable getAppIcon() {
        return AppIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        AppIcon = appIcon;
    }

    public String getAppPackageName() {
        return AppPackageName;
    }

    public void setAppPackageName(String appPackageName) {
        AppPackageName = appPackageName;
    }

    public long getAppSize() {
        return AppSize;
    }

    public void setAppSize(long appSize) {
        AppSize = appSize;
    }

    public List<ImageBean> getImageBean() {
        return imageBean;
    }

    public void setImageBean(List<ImageBean> imageBean) {
        this.imageBean = imageBean;
    }

    public List<AudioBean> getAudioBean() {
        return audioBean;
    }

    public void setAudioBean(List<AudioBean> audioBean) {
        this.audioBean = audioBean;
    }

    public List<VideoBean> getVideoBean() {
        return videoBean;
    }

    public void setVideoBean(List<VideoBean> videoBean) {
        this.videoBean = videoBean;
    }

    public long getImageSize() {
        return ImageSize;
    }

    public void setImageSize(long imageSize) {
        ImageSize = imageSize;
    }

    public long getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(long videoSize) {
        this.videoSize = videoSize;
    }

    public long getAudioSize() {
        return audioSize;
    }

    public void setAudioSize(long audioSize) {
        this.audioSize = audioSize;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "AppDataBean{" +
                "AppName='" + AppName + '\'' +
                ", AppIcon=" + AppIcon +
                ", AppPackageName='" + AppPackageName + '\'' +
                ", AppSize=" + AppSize +
                ", imageBean=" + imageBean +
                ", audioBean=" + audioBean +
                ", videoBean=" + videoBean +
                ", ImageSize=" + ImageSize +
                ", videoSize=" + videoSize +
                ", audioSize=" + audioSize +
                ", isSelect=" + isSelect +
                '}';
    }
}
