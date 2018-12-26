package com.down2588.phonemanager.main;

import com.down2588.phonemanager.BasePresenter;
import com.down2588.phonemanager.BaseView;

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
