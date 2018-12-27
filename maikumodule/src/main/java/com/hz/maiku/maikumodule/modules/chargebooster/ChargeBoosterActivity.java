package com.hz.maiku.maikumodule.modules.chargebooster;

import android.support.v4.app.Fragment;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;


public class ChargeBoosterActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        ChargeBoosterFragment chargeBoosterFragment = ChargeBoosterFragment.newInstance();
        // Create the presenter
        new ChargeBoosterPresenter(chargeBoosterFragment);
        return chargeBoosterFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle(getString(R.string.chargebooster_title));
    }
}
