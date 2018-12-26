package com.down2588.phonemanager.aboutus;

/**
 * Created by Shurrik on 2018/12/26.
 */

public class AboutUsPresenter implements AboutUsContract.Presenter {
    private AboutUsContract.View view;

    public AboutUsPresenter(AboutUsContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
    }
}
