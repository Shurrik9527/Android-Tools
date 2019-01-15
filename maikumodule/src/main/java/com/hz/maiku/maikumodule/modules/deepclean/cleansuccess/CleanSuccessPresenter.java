package com.hz.maiku.maikumodule.modules.deepclean.cleansuccess;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class CleanSuccessPresenter implements CleanSuccessContract.Presenter {

    private CleanSuccessContract.View view;

    public CleanSuccessPresenter(CleanSuccessContract.View view) {
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
