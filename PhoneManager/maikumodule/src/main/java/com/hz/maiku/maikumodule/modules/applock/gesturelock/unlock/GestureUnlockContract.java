package com.hz.maiku.maikumodule.modules.applock.gesturelock.unlock;

import android.content.Context;
import android.os.Bundle;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 创建应用锁接口
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public interface GestureUnlockContract {

    interface View extends BaseView<Presenter> {

        void initLockPatternView();

        void onBackKey();

        void startActivity(Context mContext, Class<?> mclass, Bundle mBundle);

    }

    interface Presenter extends BasePresenter {

    }
}