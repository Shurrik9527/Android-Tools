package com.hz.maiku.maikumodule.modules.deepclean;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.AlbumBean;

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
        void showData(List<AlbumBean> mlists);

    }

    public interface Presenter extends BasePresenter {
        void getImages();
    }
}
