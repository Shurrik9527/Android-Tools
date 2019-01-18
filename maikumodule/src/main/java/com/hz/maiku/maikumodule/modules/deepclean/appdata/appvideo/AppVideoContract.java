package com.hz.maiku.maikumodule.modules.deepclean.appdata.appvideo;

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
public class AppVideoContract {

    public interface View extends BaseView<AppVideoContract.Presenter> {
        //显示当前运行的进行数据
        void showVideoData(List<VideoBean> mlists);
        void reflashView();
        void cleanSuccess(long mSize);
        void startVideos(String url);
    }

    public interface Presenter extends BasePresenter {
        void delectVideoList(List<VideoBean> mlists, long mSize);
        void getVideos(String content);
    }
}
