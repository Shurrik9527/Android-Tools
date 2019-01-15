package com.hz.maiku.maikumodule.modules.deepclean.cleansuccess;


import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class CleanSuccessContract {

    public interface View extends BaseView<Presenter> {
        void showData(String data);
        void showTitle(String content);
    }

    public interface Presenter extends BasePresenter {

    }
}
