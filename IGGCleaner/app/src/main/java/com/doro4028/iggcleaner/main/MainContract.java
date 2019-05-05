package com.doro4028.iggcleaner.main;


import android.content.Context;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.DeviceInformBean;

/**
 * Created by Shurrik on 2018/12/26.
 */

public class MainContract {

    public interface View extends BaseView<Presenter> {
        void showJunkCleaner();
        void showAppManager();
        void showCpuCooler();
        void startTv();
        void showChargeBooster();
        void uploadDeviceInform(DeviceInformBean deviceInformBean);
        void showState(boolean state);
        void playHeartbeatAnimation();
        void startHeartBeat();
    }

    public interface Presenter extends BasePresenter {
        void deviceInform(Context context);
        void uploadDeviceInform(DeviceInformBean deviceInformBean);
        void checkOpenState();
    }
}
