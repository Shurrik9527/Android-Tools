package com.hz.maiku.maikumodule.base;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.appsflyer.AppsFlyerLib;
import com.baidu.crabsdk.CrabSDK;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.util.ActivityUtil;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author heguogui
 * @version v 1.0.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindColor(R2.color.colorPrimaryDark)
    int colorPrimaryDark;
    private ActionBar actionBar;


    /**
     * 页面间传值key常量
     */
    public static final String BUNDLE = "BUNDLE";

    /**
     * 布局文件Id
     */
    protected abstract int getContentViewId();

    /**
     * 获取fragment
     */
    protected abstract Fragment getFragment();

    /**
     * 布局中Fragment的ID
     */
    protected abstract int getFragmentContentId();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        //ButterKnife初始化
        ButterKnife.bind(this);
        //竖屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Set up the statusBar.
        initStatusBar();

        // Set up the toolBar.
        initToolbar();

        ActivityUtil.getmInstance().add(this);
        // 将fragment添加到activity
        addFragmentToActivity();
        // 后续初始化操作
        init();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        //AppsFlyer's Tracking Deep Linking
        AppsFlyerLib.getInstance().sendDeepLinkData(this);
    }

    /**
     * 初始化状态栏
     */
    protected void initStatusBar() {
        //设置通用的状态栏颜色
        StatusBarUtil.setColor(this, colorPrimaryDark);
    }

    /**
     * 初始化标题栏
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * 设置标题栏图标
     *
     * @param icon 设置的图标
     */
    public void setIcon(Drawable icon) {
        if(actionBar!=null){
            actionBar.setHomeAsUpIndicator(icon);
        }
    }

    /**
     * 设置标题栏文字
     *
     * @param title 要设置的标题
     */
    public void setTitle(String title) {
        if(actionBar!=null){
            actionBar.setTitle(title);
        }
    }

    /**
     * 隐藏标题栏
     */
    public void hideToolbar() {
        if(actionBar!=null){
            actionBar.hide();
        }
    }


    /**
     * 将Fragment添加到Activity
     */
    private void addFragmentToActivity() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(getFragmentContentId());
        if (fragment == null) {
            // create the fragment
            fragment = getFragment();
            ActivityUtil.getmInstance().addFragmentToActivity(getSupportFragmentManager(), fragment, getFragmentContentId());
        }
    }

    /**
     * 加载完fragment之后进行一些初始化操作
     */
    protected void init() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.getmInstance().removeActivity(this);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    /**
     * 返回上一个画面的功能
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        CrabSDK.onResume(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        CrabSDK.onPause(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
