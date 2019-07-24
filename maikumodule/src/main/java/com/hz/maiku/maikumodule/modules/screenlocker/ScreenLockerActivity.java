package com.hz.maiku.maikumodule.modules.screenlocker;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindColor;

public class ScreenLockerActivity extends BaseActivity {
    private BatteryReceiver receiver;
//    @BindView(R.id.fl_content)
//    FrameLayout flContent;
    @BindColor(R2.color.default_text)
    int bg_start;

    @Override
    protected int getContentViewId() {
        return R.layout.base_activity;
    }

    @Override
    protected Fragment getFragment() {
        ScreenLockerFragment chargeBoosterFragment =  ScreenLockerFragment.newInstance();
        // Create the presenter
        new ScreenLockerPresenter(chargeBoosterFragment);
        receiver = new BatteryReceiver(chargeBoosterFragment);
        return chargeBoosterFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    protected void initStatusBar() {
        //透明显示状态栏
        StatusBarUtil.setColor(this, bg_start);
    }

    @Override
    protected void init() {
        super.init();
        //隐藏导航栏
        hideToolbar();
        //获取桌面壁纸并设置为默认屏保
//        WallpaperManager mWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
//        flContent.setBackground(mWallpaperManager.getDrawable());
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(ScreenLockerActivity.this);
        if(receiver!=null&&manager!=null&&!isRegister(manager,Intent.ACTION_BATTERY_CHANGED)){
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(receiver, filter);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
    }


    /**
     * 判断是否注册广播
     * @param manager
     * @param action
     * @return
     */
    private boolean isRegister(LocalBroadcastManager manager, String action) {
        boolean isRegister = false;
        try {
            Field mReceiversField = manager.getClass().getDeclaredField("mReceivers");
            mReceiversField.setAccessible(true);
            HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = (HashMap<BroadcastReceiver, ArrayList<IntentFilter>>) mReceiversField.get(manager);
            for (BroadcastReceiver key : mReceivers.keySet()) {
                ArrayList<IntentFilter> intentFilters = mReceivers.get(key);
                for (int i = 0; i < intentFilters.size(); i++) {
                    IntentFilter intentFilter = intentFilters.get(i);
                    Field mActionsField = intentFilter.getClass().getDeclaredField("mActions");
                    mActionsField.setAccessible(true);
                    ArrayList<String> mActions = (ArrayList<String>) mActionsField.get(intentFilter);
                    for (int j = 0; j < mActions.size(); j++) {
                        if (mActions.get(i).equals(action)) {
                            isRegister = true;
                            break;
                        }
                    }
                }
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return isRegister;
    }


}
