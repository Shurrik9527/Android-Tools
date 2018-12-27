package com.hz.maiku.maikumodule.modules.applock.gesturelock.unlock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;
import com.hz.maiku.maikumodule.base.Constant;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 解锁界面
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class GestureUnlockActivity extends BaseActivity {

    private GestureUnlockFragment unlockFragment;

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        unlockFragment = GestureUnlockFragment.newInstance();
        if(getIntent()!=null){
            Intent mIntent =getIntent();
            String packageName =mIntent.getStringExtra(Constant.LOCK_PACKAGE_NAME);
            String backAction =mIntent.getStringExtra(Constant.LOCK_FROM);
            Bundle mBundle = new Bundle();
            mBundle.putString(Constant.LOCK_PACKAGE_NAME,packageName);
            mBundle.putString(Constant.LOCK_FROM,backAction);
            unlockFragment.setArguments(mBundle);
        }

        return unlockFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    public void onBackPressed() {
        if(unlockFragment!=null){
            unlockFragment.onBackKey();
        }
    }

    @Override
    protected void init() {
        super.init();
        hideToolbar();
    }
}
