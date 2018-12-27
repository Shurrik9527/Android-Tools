package com.hz.maiku.maikumodule.modules.applock;
import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.CommLockInfo;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 应用锁列表接口
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class AppLockContract {

    public interface View extends BaseView<Presenter> {
        void initData();
        void  showAppData(List<CommLockInfo> mlist);
        //搜索App
        void searchApp(String text);

        void saveAppData();

        void showPermission();
    }

    public interface Presenter extends BasePresenter {
        //获取所有App
        void getAppLists();

    }
}
