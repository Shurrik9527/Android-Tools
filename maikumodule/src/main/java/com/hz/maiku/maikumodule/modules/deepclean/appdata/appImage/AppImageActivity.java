package com.hz.maiku.maikumodule.modules.deepclean.appdata.appImage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/16
 * @email 252774645@qq.com
 */
public class AppImageActivity extends BaseActivity {

    private AppImageFragment mAppImageFragment;
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        mAppImageFragment =AppImageFragment.newInstance();
        return mAppImageFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getResources().getString(R.string.deepclean_top_title));
        Intent mIntent =getIntent();
        if(mIntent!=null){
            Bundle mBundle = mIntent.getBundleExtra("BUNDLE");
            if(mAppImageFragment!=null){
                mAppImageFragment.setArguments(mBundle);
            }
        }
    }
}
