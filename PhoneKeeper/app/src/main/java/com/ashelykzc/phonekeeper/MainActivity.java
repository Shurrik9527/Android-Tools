package com.ashelykzc.phonekeeper;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hz.maiku.maikumodule.base.BaseActivity;
import com.hz.maiku.maikumodule.util.ActivityUtil;
import com.hz.maiku.maikumodule.util.AdUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.ashelykzc.phonekeeper.box.BoxFragment;
import com.ashelykzc.phonekeeper.home.HomeFragment;
import com.ashelykzc.phonekeeper.mine.MineFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    /**
     * 首页选项卡
     */
    public static final String HOME_TAB = "homeTab";

    /**
     * 工具箱选项卡
     */
    public static final String BOX_TAB = "boxTab";


    /**
     * 我的选项卡
     */
    public static final String MINE_TAB = "mineTab";

    @BindView(R.id.home_tab)
    TextView homeTab;
    @BindView(R.id.box_tab)
    TextView boxTab;
    @BindView(R.id.mine_tab)
    TextView mineTab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.contentFrame)
    FrameLayout contentFrame;

    private ActionBar actionBar;
    private HomeFragment mHomeFragment;
    private BoxFragment mBoxFragment;
    private MineFragment mMineFragment;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected Fragment getFragment() {
        return null;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.contentFrame;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        initMainToolbar();
        initTab(savedInstanceState);
    }

    @OnClick({R.id.home_tab, R.id.box_tab, R.id.mine_tab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_tab:
                changeTab(HOME_TAB);
                break;
            case R.id.box_tab:
                changeTab(BOX_TAB);
                break;
            case R.id.mine_tab:
                changeTab(MINE_TAB);
                break;
        }
    }


    @Override
    protected void init() {
        super.init();

        //读取最新广告配置并展示
        AdUtil.getAdTypeAndShow(this, "MainActivity.init()");
        mHomeFragment = HomeFragment.newInstance();
        mBoxFragment = BoxFragment.newInstance();
        mMineFragment = MineFragment.newInstance();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setMainTitle(getString(R.string.home_title));
    }

    public void initTab(Bundle savedInstanceState) {

        if (savedInstanceState == null) { // 如果没有有缓存,添加新的fragment, 如果有缓存从缓存读取,此操作已经在switchFragmentInMainPage中处理
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (transaction == null) {
                return;
            }


            Fragment homeFragment = getSupportFragmentManager().findFragmentByTag(MainActivity.HOME_TAB);
            Fragment boxFragment = getSupportFragmentManager().findFragmentByTag(MainActivity.BOX_TAB);
            Fragment mineFragment = getSupportFragmentManager().findFragmentByTag(MainActivity.MINE_TAB);
            if (homeFragment == null) {
                transaction.add(
                        getFragmentContentId(),
                        mHomeFragment,
                        HOME_TAB
                );
            }

            if (boxFragment == null) {
                transaction.add(
                        getFragmentContentId(),
                        mBoxFragment,
                        BOX_TAB
                ).hide(mBoxFragment);
            }

            if (mineFragment == null) {
                transaction.add(
                        getFragmentContentId(),
                        mMineFragment,
                        MINE_TAB
                ).hide(mMineFragment);
            }
            transaction.commitAllowingStateLoss();

            homeTab.setCompoundDrawables(
                    null,
                    handleDrawable(R.drawable.ic_home_selected_icon),
                    null,
                    null
            );
            homeTab.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            setMainTitle(getString(R.string.home_title));
        }


    }


    /**
     * 切换选项卡
     *
     * @param tab
     */
    public void changeTab(String tab) {
        homeTab.setTextColor(getResources().getColor(R.color.gray2));
        boxTab.setTextColor(getResources().getColor(R.color.gray2));
        mineTab.setTextColor(getResources().getColor(R.color.gray2));
        homeTab.setCompoundDrawables(
                null,
                handleDrawable(R.drawable.ic_home_unselected_icon),
                null,
                null
        );

        boxTab.setCompoundDrawables(
                null,
                handleDrawable(R.drawable.ic_toolbox_unselected_icon),
                null,
                null
        );
        mineTab.setCompoundDrawables(
                null,
                handleDrawable(R.drawable.ic_mine_unselected_icon),
                null,
                null
        );

        AppUtil.switchFragmentInMainPage(
                getSupportFragmentManager(),
                tab
        );
        switch (tab) {
            case HOME_TAB: {
                homeTab.setCompoundDrawables(
                        null,
                        handleDrawable(R.drawable.ic_home_selected_icon),
                        null,
                        null
                );
                homeTab.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                setMainTitle(getString(R.string.home_title));
            }
            break;
            case BOX_TAB: {

                boxTab.setCompoundDrawables(
                        null,
                        handleDrawable(R.drawable.ic_toolbox_selected_icon),
                        null,
                        null
                );
                boxTab.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                setMainTitle(getString(R.string.box_title));
            }
            break;
            case MINE_TAB: {
                mineTab.setCompoundDrawables(
                        null,
                        handleDrawable(R.drawable.ic_mine_selected_icon),
                        null,
                        null
                );
                mineTab.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                setMainTitle(getString(R.string.mine_title));
            }
            break;
        }
    }


    /**
     * 设置过边界的drawable才会在textview中生效
     *
     * @param id
     * @return
     */
    private Drawable handleDrawable(@DrawableRes int id) {
        Drawable drawable = getResources().getDrawable(id);
        if (drawable == null) {
            return null;
        }
        drawable.setBounds(
                0,
                0,
                drawable.getMinimumWidth(),
                drawable.getMinimumHeight()
        );
        return drawable;
    }

    private boolean mIsExit; // 是否退出App

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mIsExit) {
                ActivityUtil.getmInstance().exit();
            } else {
                ToastUtil.showToast(this, "再按一次退出");
                mIsExit = true;
                new Handler().postDelayed(() -> mIsExit = false, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setMainTitle(String title) {
        if(actionBar!=null){
            actionBar.setTitle(title);
        }
    }

    private void initMainToolbar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }
}
