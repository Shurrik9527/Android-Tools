package com.ashelykzc.devicecleaner.box;


public class BoxPresenter implements BoxContract.Presenter {
    private static final String TAG = BoxPresenter.class.getName();
    private BoxContract.View mView;

    public BoxPresenter(BoxContract.View view) {
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
