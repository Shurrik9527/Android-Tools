package com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp;

import android.content.Context;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/21
 * @email 252774645@qq.com
 */
public class BlockPresenter implements BlockConstract.Presenter{

    private static final String TAG =BlockConstract.class.getName();
    private Context mContext;
    private BlockConstract.View mView=null;

    public BlockPresenter(final BlockConstract.View view, Context context) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mContext =context;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }


}
