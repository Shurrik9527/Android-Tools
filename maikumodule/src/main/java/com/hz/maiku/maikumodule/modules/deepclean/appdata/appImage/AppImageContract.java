package com.hz.maiku.maikumodule.modules.deepclean.appdata.appImage;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.AlbumBean;
import com.hz.maiku.maikumodule.bean.ImageBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class AppImageContract {

    public interface View extends BaseView<AppImageContract.Presenter> {
        //显示当前运行的进行数据
        void showData(List<ImageBean> mlists);
        void reflashView();
        void cleanSuccess(long mSize);
    }

    public interface Presenter extends BasePresenter {
        void delectImageList(List<ImageBean> mlists, long mSize);
        void getImages(String content);
    }
}
