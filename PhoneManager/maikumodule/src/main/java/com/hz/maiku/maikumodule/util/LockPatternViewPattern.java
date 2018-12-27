package com.hz.maiku.maikumodule.util;

import com.hz.maiku.maikumodule.widget.LockPatternView;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe  应用锁图案 监听
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class LockPatternViewPattern implements LockPatternView.OnPatternListener {

    private LockPatternView mLockPatternView;
    private onPatternListener mPatternListener;

    public interface onPatternListener {
        void onPatternDetected(List<LockPatternView.Cell> pattern);
    }

    public void setPatternListener(onPatternListener patternListener) {
        mPatternListener = patternListener;
    }

    public LockPatternViewPattern(LockPatternView lockPatternView) {
        mLockPatternView = lockPatternView;
    }

    @Override
    public void onPatternStart() {
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        patternInProgress();
    }

    private void patternInProgress() {

    }

    @Override
    public void onPatternCleared() {
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
        if (pattern == null)
            return;
        if (mPatternListener != null) {
            mPatternListener.onPatternDetected(pattern);
        }
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

}
