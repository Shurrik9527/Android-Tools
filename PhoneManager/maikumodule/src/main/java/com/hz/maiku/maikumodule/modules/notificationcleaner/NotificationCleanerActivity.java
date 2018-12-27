package com.hz.maiku.maikumodule.modules.notificationcleaner;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;
import com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp.SettingAppActivity;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 通知管理
 * @date 2018/12/19
 * @email 252774645@qq.com
 */
public class NotificationCleanerActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return NotificationCleanerFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.notification_title_top));
    }

}
