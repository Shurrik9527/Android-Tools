package com.gochicken3.mobilehelper.main;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.hz.maiku.maikumodule.modules.appmanager.AppManagerActivity;
import com.hz.maiku.maikumodule.modules.chargebooster.ChargeBoosterActivity;
import com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan.CpuCoolerScanActivity;
import com.hz.maiku.maikumodule.modules.junkcleaner.JunkCleanerActivity;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.gochicken3.mobilehelper.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static java.util.Objects.requireNonNull;

/**
 * Created by Shurrik on 2018/12/26.
 */
public class MainFragment extends Fragment implements MainContract.View {

    private MainContract.Presenter presenter;

    @BindView(R.id.lav_phonebooster)
    LottieAnimationView lavPhoneBooster;

    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (presenter != null) {
            presenter.unsubscribe();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.main_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnClick(R.id.cv_junkcleaner)
    @Override
    public void showJunkCleaner() {
        //垃圾清理
        Intent intent = new Intent(getContext(), JunkCleanerActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.cv_appmanager)
    @Override
    public void showAppManager() {
        //应用管理
        Intent intent = new Intent(getContext(), AppManagerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cv_cpucooler)
    @Override
    public void showCpuCooler() {
        //手机降温
        Intent intent = new Intent(getContext(), CpuCoolerScanActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.lav_phonebooster)
    @Override
    public void showPhoneBooster() {
        //播放动画
        lavPhoneBooster.playAnimation();
        //停止动画
        //lavPhoneBooster.cancelAnimation();
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(3000);
        animator.addUpdateListener(animation -> {
            float value = Float.parseFloat(animation.getAnimatedValue().toString());
            if (value >= 1f) {
                if(getContext()!=null){
                    ToastUtil.showToast(getContext(), "Phone Boosted");
                }
            }
        });
        animator.start();
    }


    @OnClick(R.id.cv_chargebooster)
    @Override
    public void showChargeBooster() {
        //智能充电
        Intent intent = new Intent(getActivity(), ChargeBoosterActivity.class);
        startActivity(intent);
    }

    @SuppressLint("CheckResult")
    @Override
    public void showPermissions() {
        //asking something
        RxPermissions rxPermission = new RxPermissions(requireNonNull(getActivity()));
        rxPermission.request(Manifest.permission.CLEAR_APP_CACHE,
                Manifest.permission.DELETE_CACHE_FILES
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {

        });

        RxPermissions rxPermission1 = new RxPermissions(getActivity());
        rxPermission1.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
            if (!aBoolean) {
                showMessageTips("Sorry! no permission, some functions are not available");
                showPermissions();
            }
        });
    }


    @Override
    public void initView() {
        showPhoneBooster();
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(),msg);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
