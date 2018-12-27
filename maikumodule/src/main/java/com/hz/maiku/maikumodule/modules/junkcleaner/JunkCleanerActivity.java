package com.hz.maiku.maikumodule.modules.junkcleaner;


import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return JunkCleanerFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.junkcleaner_title));
    }
}
