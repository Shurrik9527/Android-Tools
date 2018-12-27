package com.hz.maiku.maikumodule.modules.applock.gesturelock.resetlock;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.enums.LockStage;
import com.hz.maiku.maikumodule.util.LockPatternUtils;
import com.hz.maiku.maikumodule.util.LockPatternViewPattern;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.LockPatternView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 重设密码 界面
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class ResetPasswordFragment extends Fragment implements ResetPasswordContract.View {


    @BindView(R2.id.resetpassword_lock_icon)
    ImageView resetpasswordLockIcon;
    @BindView(R2.id.resetpassword_tip_tv)
    TextView resetpasswordTipTv;
    @BindView(R2.id.top_layout)
    RelativeLayout topLayout;
    @BindView(R2.id.resetpassword_lpv)
    LockPatternView mLockPatternView;
    //图案锁相关
    private LockStage mUiStage = LockStage.Introduction;
    protected List<LockPatternView.Cell> mChosenPattern = null; //密码
    private LockPatternUtils mLockPatternUtils;
    private LockPatternViewPattern mPatternViewPattern;
    private ResetPasswordContract.Presenter mPresenter;
    private static final String KEY_PATTERN_CHOICE = "chosenPattern";
    private static final String KEY_UI_STAGE = "uiStage";
    public ResetPasswordFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ResetPasswordPresenter(this, getActivity());
    }

    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance() {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.resetpassword_fragment, container, false);
        ButterKnife.bind(this, root);
        initView(savedInstanceState);
        return root;
    }

    @Override
    public void updateUiStage(LockStage stage) {
        mUiStage = stage;
    }

    @Override
    public void updateChosenPattern(List<LockPatternView.Cell> mChosenPattern) {
        this.mChosenPattern = mChosenPattern;
    }

    @Override
    public void updateLockTip(String text, boolean isToast) {
        if (resetpasswordTipTv != null) {
            resetpasswordTipTv.setText(text);
            showMessageTips(text);
        }
    }

    @Override
    public void setHeaderMessage(int headerMessage) {
        if (resetpasswordTipTv != null) {
            resetpasswordTipTv.setText(headerMessage);
        }
    }

    @Override
    public void lockPatternViewConfiguration(boolean patternEnabled, LockPatternView.DisplayMode displayMode) {
        if (mLockPatternView != null) {
            if (patternEnabled) {
                mLockPatternView.enableInput();
            } else {
                mLockPatternView.disableInput();
            }
            mLockPatternView.setDisplayMode(displayMode);
        }
    }

    @Override
    public void Introduction() {
        clearPattern();
    }

    @Override
    public void HelpScreen() {

    }

    @Override
    public void ChoiceTooShort() {
        showMessageTips(getString(R.string.gesturecreate_password_small));//路径太短
    }

    @Override
    public void moveToStatusTwo() {

    }

    @Override
    public void clearPattern() {
        mLockPatternView.clearPattern();
    }

    @Override
    public void ConfirmWrong() {
        showMessageTips(getString(R.string.gesturecreate_password_fail));//路径太短
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 100);
    }

    @Override
    public void ChoiceConfirmed() {

        showMessageTips(getString(R.string.gesturecreate_password_success));
        mLockPatternUtils.saveLockPattern(mChosenPattern); //保存密码
        clearPattern();
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }


    @Override
    public void setPresenter(ResetPasswordContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mLockPatternUtils = new LockPatternUtils(getActivity());
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                if (mPresenter != null) {
                    mPresenter.onPatternDetected(pattern, mChosenPattern, mUiStage);
                }
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);


        if (savedInstanceState == null) {
            mPresenter.updateStage(LockStage.Introduction);
        } else {
            final String patternString = savedInstanceState.getString(KEY_PATTERN_CHOICE);
            if (patternString != null) {
                mChosenPattern = LockPatternUtils.stringToPattern(patternString);
            }
            mPresenter.updateStage(LockStage.values()[savedInstanceState.getInt(KEY_UI_STAGE)]);
        }


    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getActivity(), msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }
}
