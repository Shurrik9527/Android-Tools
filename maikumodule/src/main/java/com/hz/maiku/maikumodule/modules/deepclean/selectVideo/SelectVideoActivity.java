package com.hz.maiku.maikumodule.modules.deepclean.selectVideo;

import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/4
 * @email 252774645@qq.com
 */
public class SelectVideoActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return SelectVideoFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getResources().getString(R.string.deepclean_top_title));
    }
}
