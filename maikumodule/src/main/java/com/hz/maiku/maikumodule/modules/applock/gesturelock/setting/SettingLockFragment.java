package com.hz.maiku.maikumodule.modules.applock.gesturelock.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.resetlock.ResetPasswordActivity;
import com.hz.maiku.maikumodule.service.LockService;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 应用锁设置Fragment
 * @date 2018/10/11
 * @email 252774645@qq.com
 */
public class SettingLockFragment extends Fragment implements SettingLockContract.View {


    @BindView(R2.id.setting_lock_applock_state_tv)
    TextView settingLockApplockStateTv;
    @BindView(R2.id.setting_lock_cb)
    Switch settingLockCb;
    @BindView(R2.id.setting_lock_lockscreen_tv)
    TextView settingLockLockscreenTv;
    @BindView(R2.id.setting_lock_lockscreen_cb)
    Switch settingLockLockscreenCb;
    @BindView(R2.id.setting_lock_reset_password_tv)
    TextView settingLockResetPasswordTv;
    Unbinder unbinder;
    private static final int REQUEST_CHANGE_PWD = 2;
    public SettingLockFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingLockFragment newInstance() {
        SettingLockFragment fragment = new SettingLockFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settinglock_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        initData();
        return root;
    }


    @Override
    public void initData() {

        boolean isLockOpen = (boolean) SpHelper.getInstance().get(Constant.LOCK_STATE,false);
        settingLockCb.setChecked(isLockOpen);
        settingLockCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpHelper.getInstance().put(Constant.LOCK_STATE,isChecked);
                Intent intent = new Intent(getActivity(), LockService.class);
                if (isChecked) {
                    showTop(getString(R.string.setting_lock_applock_state_open_success));
                    getActivity().startService(intent);
                } else {
                    showTop(getString(R.string.setting_lock_applock_state_close_success));
                    getActivity().stopService(intent);
                }
            }
        });
        boolean isLockAutoScreen = (boolean) SpHelper.getInstance().get(Constant.LOCK_AUTO_SCREEN, false);
        settingLockLockscreenCb.setChecked(isLockAutoScreen);
        settingLockLockscreenCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    SpHelper.getInstance().put(Constant.LOCK_AUTO_SCREEN, true);
                } else {
                    SpHelper.getInstance().put(Constant.LOCK_AUTO_SCREEN, false);
                }
            }
        });
    }

    @Override
    public void openAppLock(boolean state) {

    }

    @Override
    public void lockScreenAfterAddlock(boolean state) {

    }

    @Override
    public void resetPassword() {

    }

    @Override
    public void showTop(String msg) {
        if(!TextUtils.isEmpty(msg)){
            ToastUtil.showToast(getContext(),msg);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R2.id.setting_lock_reset_password_tv)
    public void onClick() {
        Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
        startActivityForResult(intent, REQUEST_CHANGE_PWD);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK&&requestCode==REQUEST_CHANGE_PWD) {
            showTop(getString(R.string.resetpassword_success));
        }
    }
}
