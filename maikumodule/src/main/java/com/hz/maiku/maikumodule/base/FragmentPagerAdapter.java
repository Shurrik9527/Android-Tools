package com.hz.maiku.maikumodule.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/17
 * @email 252774645@qq.com
 */
public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    private ArrayList<Fragment> fragmentsList;

    public FragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
