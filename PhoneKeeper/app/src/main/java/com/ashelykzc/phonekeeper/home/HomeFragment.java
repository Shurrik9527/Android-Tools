package com.ashelykzc.phonekeeper.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.modules.appmanager.AppManagerActivity;
import com.hz.maiku.maikumodule.modules.chargebooster.ChargeBoosterActivity;
import com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan.CpuCoolerScanActivity;
import com.hz.maiku.maikumodule.modules.junkcleaner.JunkCleanerActivity;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.TimeUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.ashelykzc.phonekeeper.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static java.util.Objects.requireNonNull;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/14
 * @email 252774645@qq.com
 */
public class HomeFragment extends Fragment implements HomeContract.View {

    @BindView(R.id.lav_phonebooster)
    LottieAnimationView lavPhonebooster;
    @BindView(R.id.home_top_rl)
    RelativeLayout homeTopRl;
    @BindView(R.id.home_center_risk_tv)
    TextView homeCenterRiskTv;
    @BindView(R.id.home_center_data_tv)
    TextView homeCenterDataTv;
    @BindView(R.id.home_center_rl)
    RelativeLayout homeCenterRl;
    @BindView(R.id.cv_appmanager)
    CardView cvAppmanager;
    @BindView(R.id.cv_chargebooster)
    CardView cvChargebooster;
    @BindView(R.id.cv_junkcleaner)
    CardView cvJunkcleaner;
    @BindView(R.id.cv_cpucooler)
    CardView cvCpucooler;
    @BindView(R.id.home_scan)
    Button homeScan;

    private HomeContract.Presenter presenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPermissions();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void showJunkCleaner() {

    }

    @Override
    public void showAppManager() {

    }

    @Override
    public void showCpuCooler() {

    }

    @Override
    public void showPhoneBooster() {

    }

    @Override
    public void showChargeBooster() {

    }

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
            } else {
//                String mflog = (String) SpHelper.getInstance().get(Constant.UPLOAD_DEVICE_INFORM, "0");
//                if (TextUtils.isEmpty(mflog) || mflog.equals("0")) {
//                    if (presenter != null) {
//                        presenter.deviceInform(getContext());
//                    }
//                }
            }

        });
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        lavPhonebooster.playAnimation();
        long time = (long) SpHelper.getInstance().get(Constant.SCAN_CLEAN_NUM,0L);
        if(time>0){
            long newTime = System.currentTimeMillis();
            int days = 0;
            try {
                days = TimeUtil.daysBetween(time, newTime);
                if (days == 0)
                    days = 1;
                if(homeCenterDataTv!=null){
                    homeCenterDataTv.setText("haven't sacnned in "+days+"day");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @OnClick({R.id.cv_appmanager, R.id.cv_chargebooster, R.id.cv_junkcleaner, R.id.cv_cpucooler,R.id.home_scan})
    public void onClick(View view) {
        Intent intent =null;
        switch (view.getId()) {
            case R.id.cv_appmanager:
                intent = new Intent(getContext(), AppManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_chargebooster:
                intent = new Intent(getActivity(), ChargeBoosterActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_junkcleaner:
                SpHelper.getInstance().put(Constant.SCAN_CLEAN_NUM,System.currentTimeMillis());
                intent = new Intent(getContext(), JunkCleanerActivity.class);
                startActivity(intent);
                break;
            case R.id.cv_cpucooler:
                //手机降温
                intent = new Intent(getContext(), CpuCoolerScanActivity.class);
                startActivity(intent);
                break;
            case R.id.home_scan:
//                intent = new Intent(getContext(), DeepCleanActivity.class);
//                startActivity(intent);
                break;
        }
    }
}
