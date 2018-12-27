package com.hz.maiku.maikumodule.modules.applock.gesturelock.createlock;

import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 创建应用锁 界面
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class GestureCreateActivity extends BaseActivity {


    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return GestureCreateFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }


    @Override
    protected void init() {
        super.init();
        setTitle(R.string.gesturecreate_top_title);
    }
}
