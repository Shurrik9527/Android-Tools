package com.hz.maiku.maikumodule.modules.applock.gesturelock.createlock;

import android.content.Context;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.enums.LockStage;
import com.hz.maiku.maikumodule.util.LockPatternUtils;
import com.hz.maiku.maikumodule.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

import static com.hz.maiku.maikumodule.bean.enums.LockStage.ChoiceConfirmed;
import static com.hz.maiku.maikumodule.bean.enums.LockStage.ChoiceTooShort;
import static com.hz.maiku.maikumodule.bean.enums.LockStage.ConfirmWrong;
import static com.hz.maiku.maikumodule.bean.enums.LockStage.FirstChoiceValid;
import static com.hz.maiku.maikumodule.bean.enums.LockStage.Introduction;
import static com.hz.maiku.maikumodule.bean.enums.LockStage.NeedToConfirm;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 创建应用锁P
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class GestureCreatePresenter implements GestureCreateContract.Presenter {

    private GestureCreateContract.View mView;
    private Context mContext;

    public GestureCreatePresenter(GestureCreateContract.View view, Context context) {
        mView = view;
        mContext = context;
        mView.setPresenter(this);
    }

    @Override
    public void updateStage(LockStage stage) {

        mView.updateUiStage(stage); //更新UiStage
        if (stage == ChoiceTooShort) { //如果少于4个点
            mView.updateLockTip(mContext.getResources().getString(stage.headerMessage, LockPatternUtils.MIN_LOCK_PATTERN_SIZE), true);
        } else {
            if (stage.headerMessage == R.string.lock_need_to_unlock_wrong) {
                mView.updateLockTip(mContext.getResources().getString(R.string.lock_need_to_unlock_wrong), true);
                mView.setHeaderMessage(R.string.lock_recording_intro_header);
            } else {
                mView.setHeaderMessage(stage.headerMessage); //
            }
        }
        // same for whether the patten is enabled
        mView.lockPatternViewConfiguration(stage.patternEnabled, LockPatternView.DisplayMode.Correct);

        switch (stage) {
            case Introduction:  //介绍
                mView.Introduction(); //第一步
                break;
            case HelpScreen: //帮助（错误多少次后可以启动帮助动画）
                mView.HelpScreen();
                break;
            case ChoiceTooShort: //锁屏路径太短
                mView.ChoiceTooShort();
                break;
            case FirstChoiceValid: //第一步提交成功
                updateStage(NeedToConfirm); //转跳到第二步
                mView.moveToStatusTwo();
                break;
            case NeedToConfirm:
                mView.clearPattern();  //第二步
                break;
            case ConfirmWrong:
                //第二步跟第一步不一样
                mView.ConfirmWrong();
                break;
            case ChoiceConfirmed:
                //第三步
                mView.ChoiceConfirmed();
                break;
        }
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern, List<LockPatternView.Cell> mChosenPattern, LockStage mUiStage) {
        if (mUiStage == NeedToConfirm) { //如果下一步
            if (mChosenPattern == null)
                throw new IllegalStateException("null chosen pattern in stage 'need to confirm");
            if (mChosenPattern.equals(pattern)) {
                updateStage(ChoiceConfirmed);
            } else {
                updateStage(ConfirmWrong);
            }
        } else if (mUiStage == ConfirmWrong) {
            if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                updateStage(ChoiceTooShort);
            } else {
                if (mChosenPattern.equals(pattern)) {
                    updateStage(ChoiceConfirmed);
                } else {
                    updateStage(ConfirmWrong);
                }
            }
        } else if (mUiStage == Introduction || mUiStage == ChoiceTooShort) {
            if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                updateStage(ChoiceTooShort);
            } else {
                mChosenPattern = new ArrayList<>(pattern);
                mView.updateChosenPattern(mChosenPattern);
                updateStage(FirstChoiceValid);
            }
        } else {
            throw new IllegalStateException("Unexpected stage " + mUiStage + " when " + "entering the pattern.");
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
