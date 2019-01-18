package com.hz.maiku.maikumodule.modules.deepclean;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.AlbumBean;
import com.hz.maiku.maikumodule.bean.ApkBean;
import com.hz.maiku.maikumodule.bean.ApkInformBean;
import com.hz.maiku.maikumodule.bean.AppBean;
import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.bean.BigFileBean;
import com.hz.maiku.maikumodule.bean.VideoBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class DeepCleanContract {

    public interface View extends BaseView<DeepCleanContract.Presenter> {
        //显示当前运行的进行数据
        void showImageData(List<AlbumBean> mlists);
        void showVideos(List<VideoBean> mlists);
        void showApks(List<ApkBean> mlists);
        void showAudios(List<AudioBean> mLists);
        void showSpecialApk(List<AppBean> mLists);
        void showBigFile(List<BigFileBean> mList);
        void showAllSize(long size);
    }

    public interface Presenter extends BasePresenter {
        void getImages();
        void getVideos();
        void getUnInstallApk();
        void getAudios();
        void getSpecialApk();
        void getBigFile();
    }
}
