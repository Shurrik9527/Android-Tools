package com.gochicken3.mobilehelper.main;


import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;

/**
 * Created by Shurrik on 2018/12/26.
 */

public class MainContract {

    public interface View extends BaseView<Presenter> {
        void showJunkCleaner();
        void showAppManager();
        void showCpuCooler();
        void showPhoneBooster();
        void showChargeBooster();
        void showPermissions();
    }

    public interface Presenter extends BasePresenter {

    }
}
