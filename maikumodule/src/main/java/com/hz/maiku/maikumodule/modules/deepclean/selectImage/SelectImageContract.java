package com.hz.maiku.maikumodule.modules.deepclean.selectImage;

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
public class SelectImageContract {

    public interface View extends BaseView<SelectImageContract.Presenter> {
        //显示当前运行的进行数据
        void showData(List<AlbumBean> mlists);
        void reflashView();
        void cleanSuccess(long mSize);
    }

    public interface Presenter extends BasePresenter {
        void delectImage(String imageUrl);
        void delectImageList(List<ImageBean> mlists,long mSize);
        void getImages();
    }
}
