package com.hz.maiku.maikumodule.modules.chargebooster;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;

public class ChargeBoosterContract {

    public interface View extends BaseView<Presenter> {
        void showScreenLocker();
        void switchProtectCharging(boolean checked);
        void chargeAlert(boolean enable);
    }

    public interface Presenter extends BasePresenter {
        void loadData();

        void startProtectCharging();

        void closeProtectCharging();

        void startChargeAlert();

        void closeChargeAlert();
    }
}
