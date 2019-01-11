package com.hz.maiku.maikumodule.modules.deepclean.cleansuccess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;
import com.hz.maiku.maikumodule.modules.cpucooler.cpucoolersuccess.CpuCoolerSuccessFragment;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class CleanSuccessActivity extends BaseActivity {

    private static final String TAG = CleanSuccessActivity.class.getName();
    private CleanSuccessFragment cleanSuccessFragment = null;

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        cleanSuccessFragment = CleanSuccessFragment.newInstance();
        Intent mIntent = getIntent();
        if (mIntent != null) {
            String temp = mIntent.getStringExtra("BUNDLE");
            Bundle mBundle = new Bundle();
            mBundle.putString("BUNDLE", temp);
            cleanSuccessFragment.setArguments(mBundle);
        }
        return cleanSuccessFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.deepclean_top_title));
    }
}
