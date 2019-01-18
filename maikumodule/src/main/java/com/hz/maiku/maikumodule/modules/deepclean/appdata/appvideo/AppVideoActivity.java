package com.hz.maiku.maikumodule.modules.deepclean.appdata.appvideo;

import android.content.Intent;
import android.os.Bundle;
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
public class AppVideoActivity extends BaseActivity {

    private AppVideoFragment mAppVideoFragment;
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        mAppVideoFragment = AppVideoFragment.newInstance();
        return mAppVideoFragment;
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
            if(mAppVideoFragment!=null){
                mAppVideoFragment.setArguments(mBundle);
            }
        }
    }
}
