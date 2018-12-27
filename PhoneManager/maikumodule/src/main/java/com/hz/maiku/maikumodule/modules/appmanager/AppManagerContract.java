package com.hz.maiku.maikumodule.modules.appmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;

import java.util.List;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class AppManagerContract {

    public interface View extends BaseView<Presenter> {
        void refresh();
        void  showAppData(List<ApplicationInfo> lists);
    }

    public interface Presenter extends BasePresenter {
        void loadData(Context context);
    }
}
