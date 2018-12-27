package com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.NotificationBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/21
 * @email 252774645@qq.com
 */
public class AllowConstract {
    public interface View extends BaseView<Presenter> {
        void initData();
        void initRecycleView();
        void showData(List<NotificationBean> lists);
    }

    public interface Presenter extends BasePresenter {



    }
}
