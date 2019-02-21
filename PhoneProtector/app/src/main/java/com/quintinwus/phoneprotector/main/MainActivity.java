package com.quintinwus.phoneprotector.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.appsflyer.AFInAppEventType;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.modules.aboutus.AboutUsActivity;
import com.hz.maiku.maikumodule.modules.applock.AppLockActivity;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.createlock.GestureCreateActivity;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.setting.SettingLockActivity;
import com.hz.maiku.maikumodule.util.ActivityUtil;
import com.hz.maiku.maikumodule.util.AdUtil;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.EventUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.quintinwus.phoneprotector.BaseActivity;
import com.quintinwus.phoneprotector.R;

import butterknife.BindView;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/20
 * @email 252774645@qq.com
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.main_activity;
    }

    @Override
    protected Fragment getFragment() {
        MainFragment mainFragment = MainFragment.newInstance();
        new MainPresenter(mainFragment);
        return mainFragment;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fl_content;
    }

    @Override
    protected void initStatusBar() {
        StatusBarUtil.setColorForDrawerLayout(this, mDrawerLayout, getResources().getColor(R.color.colorPrimaryDark));
        // Set up the navigation drawer.
        setupDrawerContent(navigationView);
    }

    @Override
    protected void init() {
        super.init();
        setIcon(getResources().getDrawable(R.drawable.ic_menu));
        //读取最新广告配置并展示
        AdUtil.getAdTypeAndShow(this, "MainActivity.init()");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.main_update_menu_item:
                            goToAppDetailPage();
                            //有人想更新程序
                            EventUtil.sendEvent(MainActivity.this, AFInAppEventType.UPDATE, "Someone try to update!");
                            break;
                        case R.id.main_feedback_menu_item:
                            feedback();
                            break;
                        case R.id.main_comment_menu_item:
                            goToAppDetailPage();
                            //有人想给我们评分
                            EventUtil.sendEvent(MainActivity.this, AFInAppEventType.RATE, "Someone wants to rate us!");
                            break;
                        case R.id.main_aboutus_menu_item:
                            startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                            break;
                        case R.id.main_setting_menu_item:
                            startActivity(new Intent(MainActivity.this, SettingLockActivity.class));
                            break;

                    }
                    return true;
                });
    }

    /**
     * 意见反馈
     */
    private void feedback() {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + Constant.GMAIL));
        data.putExtra(Intent.EXTRA_SUBJECT, "I want to say");
        data.putExtra(Intent.EXTRA_TEXT, "Hi, There!");
        startActivity(data);
    }

    /**
     * 跳转到应用详情页面
     */
    public void goToAppDetailPage() {
        if (AppUtil.isInstalled(this, "com.android.vending")) {
            final String GOOGLE_PLAY = "com.android.vending";
            Uri uri = Uri.parse("market://details?id=" + Constant.PACKAGE_NAME);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(GOOGLE_PLAY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            ToastUtil.showToast(MainActivity.this, "Please install GooglePlay first!");
        }
    }


    private boolean mIsExit; // 是否退出App
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                ActivityUtil.getmInstance().exit();
            } else {
                ToastUtil.showToast(MainActivity.this,"Press exit again");
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                },2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
