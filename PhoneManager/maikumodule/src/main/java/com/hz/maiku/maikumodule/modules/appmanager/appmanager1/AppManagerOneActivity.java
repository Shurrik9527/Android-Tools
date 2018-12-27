package com.hz.maiku.maikumodule.modules.appmanager.appmanager1;

import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;
import com.hz.maiku.maikumodule.modules.appmanager.AppManagerFragment;
import com.hz.maiku.maikumodule.modules.appmanager.AppManagerPresenter;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/25
 * @email 252774645@qq.com
 */
public class AppManagerOneActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        AppManagerOneFragment appManagerOneFragment = AppManagerOneFragment.newInstance();
        // Create the presenter
        new AppManagerOnePresenter(appManagerOneFragment);
        return appManagerOneFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.appmanager_title));
    }

}
