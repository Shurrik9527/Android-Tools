package com.hz.maiku.maikumodule.modules.appmanager;

import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;


public class AppManagerActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        AppManagerFragment appManagerFragment = AppManagerFragment.newInstance();
        // Create the presenter
        new AppManagerPresenter(appManagerFragment);
        return appManagerFragment;
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
