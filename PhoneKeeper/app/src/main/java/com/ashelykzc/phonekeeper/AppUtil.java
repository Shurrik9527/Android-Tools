package com.ashelykzc.phonekeeper;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/14
 * @email 252774645@qq.com
 */
public class AppUtil {

    /**
     * 将fragment添加到容器中,由fragmentManager执行
     *
     * @param fragmentManager
     * @param fragment
     * @param frameId
     */
    public static void addFragmenttoActivity(
            @NonNull FragmentManager fragmentManager,
            @NonNull Fragment fragment,
            int frameId,
            String tag
    ) {
        if (fragmentManager == null || fragment == null) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment, tag);
//        transaction.commit();
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmenttoActivity(
            @NonNull FragmentManager fragmentManager,
            @NonNull Fragment fragment,
            int frameId
    ) {
        addFragmenttoActivity(
                fragmentManager,
                fragment,
                frameId,
                null
        );
    }


    /**
     * 替换fragment
     *
     * @param fragmentManager
     * @param fragment
     * @param frameId
     */
    public static void replaceFragment(
            @NonNull FragmentManager fragmentManager,
            @NonNull Fragment fragment,
            int frameId
    ) {
        if (fragment == null || fragmentManager == null) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }

    /**
     * 切换首页选项卡的显示
     *
     * @param fragmentManager
     * @param fragmentTag
     */
    public static void switchFragmentInMainPage(
            @NonNull FragmentManager fragmentManager,
            @NonNull String fragmentTag
    ) {
        if (fragmentManager == null || fragmentTag == null) {
            return;
        }
        Fragment homeFragment = fragmentManager.findFragmentByTag(MainActivity.HOME_TAB);
        Fragment boxFragment = fragmentManager.findFragmentByTag(MainActivity.BOX_TAB);
        Fragment mineFragment = fragmentManager.findFragmentByTag(MainActivity.MINE_TAB);
        if (homeFragment == null || boxFragment == null || mineFragment == null) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (transaction == null) {
            return;
        }
        switch (fragmentTag) {
            case MainActivity.HOME_TAB: {
                transaction.hide(boxFragment);
                transaction.hide(mineFragment);
                transaction.show(homeFragment);
                transaction.commitAllowingStateLoss();
            }
            break;
            case MainActivity.BOX_TAB: {
                transaction.hide(homeFragment);
                transaction.hide(mineFragment);
                transaction.show(boxFragment);
                transaction.commitAllowingStateLoss();
            }
            break;
            case MainActivity.MINE_TAB: {
                transaction.hide(homeFragment);
                transaction.hide(boxFragment);
                transaction.show(mineFragment);
                transaction.commitAllowingStateLoss();
            }
            break;
        }

    }
}
