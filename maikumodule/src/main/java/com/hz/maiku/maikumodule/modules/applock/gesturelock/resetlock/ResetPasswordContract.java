package com.hz.maiku.maikumodule.modules.applock.gesturelock.resetlock;

import android.os.Bundle;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.enums.LockStage;
import com.hz.maiku.maikumodule.widget.LockPatternView;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 重置密码接口
 * @date 2018/10/14
 * @email 252774645@qq.com
 */
public interface ResetPasswordContract {

    interface View extends BaseView<Presenter> {

        void initView(Bundle savedInstanceState);

        void updateUiStage(LockStage stage); //更新UI状态

        void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern); //更新密码

        void updateLockTip(String text, boolean isToast); //更新解锁提示

        void setHeaderMessage(int headerMessage);

        void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode);  //控件的一些配置

        void Introduction(); //控件状态（刚开始）

        void HelpScreen(); //帮助（错误多少次后可以启动帮助动画）

        void ChoiceTooShort(); //锁屏路径太短

        void moveToStatusTwo(); //转到第二步

        void clearPattern(); //清空控件状态

        void ConfirmWrong(); //两次的路径不一样

        void ChoiceConfirmed(); //成功绘制了2次路径
    }

    interface Presenter extends BasePresenter {

        void updateStage(LockStage stage);

        void onPatternDetected(List<LockPatternView.Cell> pattern, List<LockPatternView.Cell> mChosenPattern, LockStage mUiStage);

        void onDestroy();
    }
}