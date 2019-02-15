package com.hzmaiku1.phonekeeper.box;


import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;


public class BoxContract {

    public interface View extends BaseView<Presenter> {
        void showJunkCleaner();
        void showAppManager();
        void showCpuCooler();
        void showNotifiBooster();
        void showChargeBooster();
        void showWifi();
        void showAppLock();
        void showTraffic();

    }

    public interface Presenter extends BasePresenter {

    }
}
