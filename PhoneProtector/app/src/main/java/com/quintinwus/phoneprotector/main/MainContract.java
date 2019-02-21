package com.quintinwus.phoneprotector.main;


import android.content.Context;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.DeviceInformBean;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/20
 * @email 252774645@qq.com
 */
public class MainContract {

    public interface View extends BaseView<Presenter> {
        void showPhoneBooster();
        void showPermissions();
        void uploadDeviceInform(DeviceInformBean deviceInformBean);
        void showState(boolean state);
        void showMemory(float afloat);
    }

    public interface Presenter extends BasePresenter {
        void deviceInform(Context context);
        void uploadDeviceInform(DeviceInformBean deviceInformBean);
        void checkOpenState();
        void getMemory();
    }
}
