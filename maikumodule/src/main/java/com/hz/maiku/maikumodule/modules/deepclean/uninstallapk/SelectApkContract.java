package com.hz.maiku.maikumodule.modules.deepclean.uninstallapk;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.ApkBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class SelectApkContract {

    public interface View extends BaseView<SelectApkContract.Presenter> {
        //显示当前运行的进行数据
        void showApkData(List<ApkBean> mlists);
        void reflashView();
        void cleanSuccess(long mSize);
    }

    public interface Presenter extends BasePresenter {
        void delectApkList(List<ApkBean> mlists, long mSize);
        void getUnInstallApk();
    }
}
