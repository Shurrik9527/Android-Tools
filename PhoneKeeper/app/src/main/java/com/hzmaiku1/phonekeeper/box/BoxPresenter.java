package com.hzmaiku1.phonekeeper.box;

import com.hzmaiku1.phonekeeper.home.HomeContract;


public class BoxPresenter implements HomeContract.Presenter {
    private static final String TAG = BoxPresenter.class.getName();
    private HomeContract.View mView;

    public BoxPresenter(HomeContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

}
