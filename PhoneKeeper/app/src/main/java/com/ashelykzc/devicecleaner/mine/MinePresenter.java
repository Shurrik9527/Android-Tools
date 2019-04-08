package com.ashelykzc.devicecleaner.mine;

public class MinePresenter implements MineContract.Presenter {
    private static final String TAG = MinePresenter.class.getName();
    private MineContract.View mView;

    public MinePresenter(MineContract.View view) {
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
