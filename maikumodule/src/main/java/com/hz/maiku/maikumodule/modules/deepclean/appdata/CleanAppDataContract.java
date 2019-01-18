package com.hz.maiku.maikumodule.modules.deepclean.appdata;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.AppBean;
import com.hz.maiku.maikumodule.bean.AppDataBean;
import com.hz.maiku.maikumodule.bean.AudioBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class CleanAppDataContract {

    public interface View extends BaseView<CleanAppDataContract.Presenter> {
        //显示当前运行的进行数据
        void showData(List<AppBean> mlists);
        void showAppData(List<AppDataBean> mlists);
    }

    public interface Presenter extends BasePresenter {
        void getAppDatas(List<AppBean> lists);
        void getData();
    }
}
