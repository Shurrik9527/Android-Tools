package com.hz.maiku.maikumodule.modules.trafficstatistics;

import android.support.v4.app.Fragment;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.base.BaseActivity;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class TrafficStatisticsActivity extends BaseActivity {
    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        return TrafficStatisticsFragment.newInstance();
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void init() {
        super.init();
        setTitle("TrafficStatistics");
    }
}
