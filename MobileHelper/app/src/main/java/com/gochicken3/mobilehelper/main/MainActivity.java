package com.gochicken3.mobilehelper.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.appsflyer.AFInAppEventType;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.modules.aboutus.AboutUsActivity;
import com.hz.maiku.maikumodule.modules.applock.AppLockActivity;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.createlock.GestureCreateActivity;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.setting.SettingLockActivity;
import com.hz.maiku.maikumodule.util.AdUtil;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.EventUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.gochicken3.mobilehelper.BaseActivity;
import com.gochicken3.mobilehelper.R;

import butterknife.BindView;

/**
 * Created by Shurrik on 2018/12/26.
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
        // Create the presenter
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


        //守护进程
//        if (Build.VERSION.SDK_INT >= 21) {
//            startJobScheduler();
//        } else {
//            startService(new Intent(getApplicationContext(), GrayService.class));
//        }

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
                            //检查更新
//                                ToastUtil.showToast(MainActivity.this, "This is the newest version!");
                            goToAppDetailPage();
                            //有人想更新程序
                            EventUtil.sendEvent(MainActivity.this, AFInAppEventType.UPDATE, "Someone try to update!");
                            break;
                        case R.id.main_feedback_menu_item:
                            //意见反馈
                            //ToastUtil.showToast(MainActivity.this, "意见反馈");
//                                feedback("jerrywang724@gmail.com");
                            feedback();
                            break;
                        case R.id.main_comment_menu_item:
                            //评论我们
//                                ToastUtil.showToast(MainActivity.this, "评论我们");
                            goToAppDetailPage();
                            //有人想给我们评分
                            EventUtil.sendEvent(MainActivity.this, AFInAppEventType.RATE, "Someone wants to rate us!");
                            break;
                        case R.id.main_aboutus_menu_item:
                            //关于我们
                            //ToastUtil.showToast(MainActivity.this, "关于我们");
                            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.main_applock_menu_item:
                            boolean isFirstLock = (boolean) SpHelper.getInstance().get(Constant.LOCK_IS_FIRST_LOCK, true);
                            if (isFirstLock) {
                                startActivity(new Intent(MainActivity.this, GestureCreateActivity.class));
                            } else {
                                startActivity(new Intent(MainActivity.this, AppLockActivity.class));
                            }
                            break;
                        case R.id.main_setting_menu_item:
                            startActivity(new Intent(MainActivity.this, SettingLockActivity.class));
                            break;
//                            case R.id.main_harassintercept_menu_item:
//                                startActivity(new Intent(MainActivity.this, HarassInterceptActivity.class));
//                                break;

                    }
                    // Close the navigation drawer when an item is selected.
                    //mDrawerLayout.closeDrawers();
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


//    /**
//     * 5.x以上系统启用 JobScheduler API 进行实现守护进程的唤醒操作
//     */
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void startJobScheduler() {
//        int jobId = 1;
//        JobInfo.Builder jobInfo = new JobInfo.Builder(jobId, new ComponentName(this, VMDaemonJobService.class));
//        jobInfo.setPeriodic(10000);
//        jobInfo.setPersisted(true);
//        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        jobScheduler.schedule(jobInfo.build());
//    }

}
