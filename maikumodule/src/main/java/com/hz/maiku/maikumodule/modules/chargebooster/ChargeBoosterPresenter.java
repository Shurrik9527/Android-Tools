package com.hz.maiku.maikumodule.modules.chargebooster;

import com.hz.maiku.maikumodule.util.SpHelper;

/**
 * Created by Shurrik on 2017/11/22.
 */

public class ChargeBoosterPresenter implements ChargeBoosterContract.Presenter {
    private ChargeBoosterContract.View view;

    public ChargeBoosterPresenter(ChargeBoosterContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
    }


    @Override
    public void loadData() {
        boolean isProtect = (boolean) SpHelper.getInstance().get("isProtect", false);
        view.switchProtectCharging(isProtect);
        if(isProtect){
            view.setEnableAlert(true);
        }

    }

    @Override
    public void startProtectCharging() {
        view.showScreenLocker();
        view.chargeAlert(true);
        setProtectStatus(true);
    }

    @Override
    public void closeProtectCharging() {
        view.chargeAlert(false);
        setProtectStatus(false);
    }

    @Override
    public void startChargeAlert() {
        setAlertStatus(true);
    }

    @Override
    public void closeChargeAlert() {
        setAlertStatus(false);
    }

    public void setProtectStatus(boolean isProtect) {
        SpHelper.getInstance().put("isProtect", isProtect);
    }

    public void setAlertStatus(boolean isAlert){
        SpHelper.getInstance().put("isAlert", isAlert);
    }

}
