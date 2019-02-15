package com.hzmaiku1.phonekeeper.mine;


import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;


public class MineContract {

    public interface View extends BaseView<Presenter> {
        void showRegistDay();
        void showUpdate();
        void showFacebook();
        void showAboutUs();
        void showSetting();

    }

    public interface Presenter extends BasePresenter {

    }
}
