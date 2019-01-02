package com.hz.maiku.maikumodule.modules.wifimanager;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class WifiManagerActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return WifiManagerFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.wifi_manager));
        //兼容vector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
