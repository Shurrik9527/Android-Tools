package com.hz.maiku.maikumodule.modules.junkcleaner.junkcleanersuccess;


import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class JunkCleanerSuccessContract {

    public interface View extends BaseView<Presenter> {
        void startScan();
        void startRotateAnimation();
        //总共垃圾数目
        void showTotalSize(String size);
    }

    public interface Presenter extends BasePresenter {
        void startScanTask();
    }
}
