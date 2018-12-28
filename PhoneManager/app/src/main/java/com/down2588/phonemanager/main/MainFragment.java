package com.down2588.phonemanager.main;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.down2588.phonemanager.R;
import com.hz.maiku.maikumodule.modules.appmanager.appmanager1.AppManagerOneActivity;
import com.hz.maiku.maikumodule.modules.chargebooster.ChargeBoosterActivity;
import com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan.CpuCoolerScanActivity;
import com.hz.maiku.maikumodule.modules.junkcleaner.JunkCleanerActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        startActivity(new Intent(getActivity(), JunkCleanerActivity.class));
    }

    @OnClick(R.id.cv_appmanager)
    @Override
    public void showAppManager() {
        //应用管理
        startActivity(new Intent(getActivity(), AppManagerOneActivity.class));
    }

    @OnClick(R.id.cv_cpucooler)
    @Override
    public void showCpuCooler() {
        //手机降温
        startActivity(new Intent(getActivity(), CpuCoolerScanActivity.class));
    }

    @OnClick(R.id.lav_phonebooster)
    @Override
    public void showPhoneBooster() {
        //播放动画
        lavPhoneBooster.playAnimation();
        //停止动画
        //lavPhoneBooster.cancelAnimation();
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = Float.parseFloat(animation.getAnimatedValue().toString());
                if (value >= 1f) {
                    //优化加速
                }
            }
        });
        animator.start();
    }


    @OnClick(R.id.cv_chargebooster)
    @Override
    public void showChargeBooster() {
        //智能充电
        startActivity(new Intent(getActivity(), ChargeBoosterActivity.class));
    }


    @Override
    public void showPermissions() {
        //asking something
        RxPermissions rxPermission = new RxPermissions(getActivity());
        rxPermission.request(Manifest.permission.CLEAR_APP_CACHE,
                Manifest.permission.DELETE_CACHE_FILES
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new io.reactivex.functions.Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {

            }
        });

        RxPermissions rxPermission1 = new RxPermissions(getActivity());
        rxPermission1.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new io.reactivex.functions.Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (!aBoolean) {
                    showMessageTips("Sorry! no permission, some functions are not available");
                    showPermissions();
                }
            }
        });
    }


    @Override
    public void initView() {
        showPhoneBooster();
    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
