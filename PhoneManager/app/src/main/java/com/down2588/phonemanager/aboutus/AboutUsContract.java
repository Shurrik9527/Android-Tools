package com.down2588.phonemanager.aboutus;

import com.down2588.phonemanager.BasePresenter;
import com.down2588.phonemanager.BaseView;

public class AboutUsContract {

    public interface View extends BaseView<Presenter> {
        void initVersionName();
    }

    public interface Presenter extends BasePresenter {

    }
}
