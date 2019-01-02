package com.hz.maiku.maikumodule.modules.cpucooler.cpucoolersuccess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class CpuCoolerSuccessActivity extends BaseActivity {

    private static final String TAG = CpuCoolerSuccessActivity.class.getName();
    private CpuCoolerSuccessFragment cpuCoolerSuccessFragment = null;

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        cpuCoolerSuccessFragment = CpuCoolerSuccessFragment.newInstance();
        Intent mIntent = getIntent();
        if (mIntent != null) {
            String temp = mIntent.getStringExtra("BUNDLE");
            Bundle mBundle = new Bundle();
            mBundle.putString("BUNDLE", temp);
            cpuCoolerSuccessFragment.setArguments(mBundle);
        }
        return cpuCoolerSuccessFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.cpu_cooler));
    }
}
