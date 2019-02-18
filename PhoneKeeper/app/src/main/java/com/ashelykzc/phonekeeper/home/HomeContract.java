package com.ashelykzc.phonekeeper.home;

import android.content.Context;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.DeviceInformBean;

public class HomeContract {

    public interface View extends BaseView<Presenter> {
        void showJunkCleaner();
        void showAppManager();
        void showCpuCooler();
        void showPhoneBooster();
        void showChargeBooster();
        void showPermissions();
        void uploadDeviceInform(DeviceInformBean deviceInformBean);
    }

    public interface Presenter extends BasePresenter {
        void deviceInform(Context context);
        void uploadDeviceInform(DeviceInformBean deviceInformBean);
    }
}
