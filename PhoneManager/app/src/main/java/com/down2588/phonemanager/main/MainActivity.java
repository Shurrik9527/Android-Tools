package com.down2588.phonemanager.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.down2588.phonemanager.BaseActivity;
import com.down2588.phonemanager.R;
import com.down2588.phonemanager.aboutus.AboutUsActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;

/**
 * Created by Shurrik on 2018/12/26.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindColor(R.color.colorPrimaryDark)
    int colorPrimaryDark;
    @BindDrawable(R.drawable.ic_menu)
    Drawable menu;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

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
        StatusBarUtil.setColorForDrawerLayout(this, mDrawerLayout, colorPrimaryDark);
        // Set up the navigation drawer.
        setupDrawerContent(navigationView);
    }

    @Override
    protected void init() {
        super.init();
        setIcon(menu);
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
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.main_update_menu_item:
                                //检查更新
//                                ToastUtil.showToast(MainActivity.this, "This is the newest version!");
                                goToAppDetailPage("com.down2588.phonemanager");
                                //有人想更新程序
//                                EventUtil.sendEvent(MainActivity.this, AFInAppEventType.UPDATE, "Someone try to update!");
                                break;
                            case R.id.main_feedback_menu_item:
                                //意见反馈
                                feedback("down2588@gmail.com");
                                break;
                            case R.id.main_comment_menu_item:
                                //评论我们
                                goToAppDetailPage("com.down2588.phonemanager");
                                //有人想给我们评分
//                                EventUtil.sendEvent(MainActivity.this, AFInAppEventType.RATE, "Someone wants to rate us!");
                                break;
                            case R.id.main_aboutus_menu_item:
                                //关于我们
                                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                                startActivity(intent);
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        //mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    /**
     * 意见反馈
     */
    private void feedback(String email) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + email));
        data.putExtra(Intent.EXTRA_SUBJECT, "I want to say");
        data.putExtra(Intent.EXTRA_TEXT, "Hi, There!");
        startActivity(data);
    }

    /**
     * 跳转到应用详情页面
     */
    public void goToAppDetailPage(String packageName) {
//        if (AppUtil.isInstalled(this, "com.android.vending")) {
//            final String GOOGLE_PLAY = "com.android.vending";
//            Uri uri = Uri.parse("market://details?id=" + packageName);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            intent.setPackage(GOOGLE_PLAY);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else {
//            ToastUtil.showToast(MainActivity.this, "Please install GooglePlay first!");
//        }
    }
}
