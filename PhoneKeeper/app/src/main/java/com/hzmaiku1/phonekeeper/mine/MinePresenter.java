package com.hzmaiku1.phonekeeper.mine;

import com.hzmaiku1.phonekeeper.home.HomeContract;


public class MinePresenter implements HomeContract.Presenter {
    private static final String TAG = MinePresenter.class.getName();
    private HomeContract.View mView;

    public MinePresenter(HomeContract.View view) {
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
