package com.hz.maiku.maikumodule.modules.chargebooster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.modules.screenlocker.ScreenLockerActivity;
import com.hz.maiku.maikumodule.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;


public class ChargeBoosterFragment extends Fragment implements ChargeBoosterContract.View {

    private ChargeBoosterContract.Presenter presenter;
    @BindView(R2.id.s_protectcharging)
    Switch sProtectCharging;

    @BindView(R2.id.s_chargealert)
    Switch sChargeAlert;

    public ChargeBoosterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChargeBoosterFragment newInstance() {
        ChargeBoosterFragment fragment = new ChargeBoosterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.chargebooster_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void setPresenter(ChargeBoosterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        presenter.loadData();
        sProtectCharging.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //开启充电保护
                    presenter.startProtectCharging();
                } else {
                    //关闭充电保护
                    presenter.closeProtectCharging();
                }
            }
        });

        sChargeAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    //开启充电提醒
                    presenter.startChargeAlert();
                } else {
                    //关闭充电提醒
                    presenter.closeChargeAlert();
                }
            }
        });
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }

    @Override
    public void showScreenLocker() {
        Intent intent = new Intent(getContext(), ScreenLockerActivity.class);
        startActivity(intent);
    }

    @Override
    public void switchProtectCharging(boolean checked) {
        sProtectCharging.setChecked(checked);
    }

    @Override
    public void chargeAlert(boolean enable) {
        sChargeAlert.setEnabled(enable);
        sChargeAlert.setChecked(enable);
    }

}
