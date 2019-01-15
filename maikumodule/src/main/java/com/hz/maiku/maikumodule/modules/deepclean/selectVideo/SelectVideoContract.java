package com.hz.maiku.maikumodule.modules.deepclean.selectVideo;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.VideoBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class SelectVideoContract {

    public interface View extends BaseView<SelectVideoContract.Presenter> {
        //显示当前运行的进行数据
        void showVideoData(List<VideoBean> mlists);
        void reflashView();
        void cleanSuccess(long mSize);
    }

    public interface Presenter extends BasePresenter {
        void delectVideoList(List<VideoBean> mlists, long mSize);
        void getVideos();
    }
}
