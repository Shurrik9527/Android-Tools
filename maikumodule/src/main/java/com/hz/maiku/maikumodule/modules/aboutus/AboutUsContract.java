package com.hz.maiku.maikumodule.modules.aboutus;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;

public class AboutUsContract {

    public interface View extends BaseView<Presenter> {
        void initVersionName();
    }

    public interface Presenter extends BasePresenter {

    }
}
