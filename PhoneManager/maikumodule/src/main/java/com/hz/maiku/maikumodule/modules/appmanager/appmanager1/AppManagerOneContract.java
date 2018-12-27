package com.hz.maiku.maikumodule.modules.appmanager.appmanager1;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.AppManagerBean;
import com.hz.maiku.maikumodule.modules.appmanager.AppManagerContract;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/25
 * @email 252774645@qq.com
 */
public class AppManagerOneContract {

    public interface View extends BaseView<AppManagerOneContract.Presenter> {
        void refresh(String packageName);
        void  showAppData(List<AppManagerBean> lists);
        void  guideView();
        void refreshView();
    }

    public interface Presenter extends BasePresenter {
        void loadData(Context context);
    }
}
