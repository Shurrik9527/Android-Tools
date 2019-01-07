package com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/21
 * @email 252774645@qq.com
 */
public class SettingAppActivity extends BaseActivity {


    private static SettingAppFragment settingAppFragment;

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        settingAppFragment =SettingAppFragment.newInstance();
        return settingAppFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.notification_cleaner));
    }



    @Override
    public void onBackPressed() {
        finish();
    }
}
