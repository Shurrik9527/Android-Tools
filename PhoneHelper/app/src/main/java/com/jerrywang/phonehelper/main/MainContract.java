package com.jerrywang.phonehelper.main;


import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;

import java.util.List;

/**
 * Created by Shurrik on 2018/12/26.
 */

public class MainContract {

    public interface View extends BaseView<Presenter> {

        void showPhoneBooster();
    }

    public interface Presenter extends BasePresenter {
        List<MainFunctionItem> getFunctions();
    }
}
