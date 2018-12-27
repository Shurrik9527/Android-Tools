package com.hz.maiku.maikumodule.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.hz.maiku.maikumodule.base.MaiKuApp;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 手机屏幕工具
 * @date 2018/9/11
 * @email 252774645@qq.com
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽px
     */
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) MaiKuApp.getmContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) MaiKuApp.getmContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
        return dm.heightPixels;
    }

}
