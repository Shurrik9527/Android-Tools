package com.hz.maiku.maikumodule.modules.applock.gesturelock.setting;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 应用锁设置接口
 * @date 2018/10/14
 * @email 252774645@qq.com
 */
public interface SettingLockContract {

    interface View {
        void initData();
        void openAppLock(boolean state);
        void lockScreenAfterAddlock(boolean state);
        void resetPassword();
        void showTop(String msg);
    }

}