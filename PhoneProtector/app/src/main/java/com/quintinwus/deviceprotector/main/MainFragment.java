package com.quintinwus.deviceprotector.main;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.DeviceInformBean;
import com.hz.maiku.maikumodule.modules.applock.AppLockActivity;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.createlock.GestureCreateActivity;
import com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan.CpuCoolerScanActivity;
import com.hz.maiku.maikumodule.modules.deepclean.DeepCleanActivity;
import com.hz.maiku.maikumodule.modules.junkcleaner.JunkCleanerActivity;
import com.hz.maiku.maikumodule.modules.notificationcleaner.NotificationCleanerActivity;
import com.hz.maiku.maikumodule.modules.trafficstatistics.TrafficStatisticsActivity;
import com.hz.maiku.maikumodule.modules.wifimanager.WifiManagerActivity;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.TimeUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.DigitalRollingTextView;
import com.quintinwus.deviceprotector.R;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.util.Objects.requireNonNull;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/20
 * @email 252774645@qq.com
 */
public class MainFragment extends Fragment implements MainContract.View {

    @BindView(R.id.scan_tv)
    TextView scanTv;
    @BindView(R.id.junk_iv)
    ImageView junkIv;
    @BindView(R.id.cpucooler_iv)
    ImageView cpucoolerIv;
    @BindView(R.id.app_lock_iv)
    ImageView appLockIv;
    @BindView(R.id.notice_iv)
    ImageView noticeIv;
    @BindView(R.id.network_monitoring_iv)
    ImageView networkMonitoringIv;
    @BindView(R.id.wifi_iv)
    ImageView wifiIv;
    @BindView(R.id.name_tv)
    DigitalRollingTextView nameTv;


    private MainContract.Presenter presenter;
    private boolean isFirst =false;
    @BindView(R.id.lav_pp)
    LottieAnimationView lavPhoneBooster;


    @BindView(R.id.scan_data_tv)
    TextView scanTimeTv;

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
        isFirst =true;
        if (presenter != null) {
            presenter.checkOpenState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.subscribe();
        }
        if (presenter != null) {
            presenter.getMemory();
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
                if (getContext() != null) {
                    if(!isFirst){
                        startActivity(new Intent(getContext(),DeepCleanActivity.class));
                    }
                    isFirst =false;
                }
            }
        });
        animator.start();
        SpHelper.getInstance().put(Constant.SCAN_CLEAN_NUM,System.currentTimeMillis());

    }

    @Override
    public void uploadDeviceInform(DeviceInformBean deviceInformBean) {
        if (presenter != null) {
            presenter.uploadDeviceInform(deviceInformBean);
        }
    }

    @Override
    public void showState(boolean state) {
        if (state) {
            String mflog = (String) SpHelper.getInstance().get(Constant.UPLOAD_DEVICE_INFORM, "0");
            if (TextUtils.isEmpty(mflog) || mflog.equals("0")) {
                if (presenter != null) {
                    presenter.deviceInform(getContext());
                }
            }
        }
    }

    @Override
    public void showMemory(float afloat) {
        try {
            float mvalue =afloat*100;
            if(mvalue>0){
                String value =String.format("%.1f", mvalue);
                if (value.contains(",")) {
                    value = value.replace(",", ".");
                }
                if (nameTv != null) {
                    nameTv.setContent(value+"");
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void initView() {
        showPhoneBooster();
        long time = (long) SpHelper.getInstance().get(Constant.SCAN_CLEAN_NUM, 0L);
        if (time > 0) {
            long newTime = System.currentTimeMillis();
            int days = 0;
            try {
                days = TimeUtil.daysBetween(time, newTime);
                if (days == 0)
                    days = 1;
                if (scanTimeTv != null) {
                    scanTimeTv.setText("Haven't sacnned in " + days + "days");
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        if (nameTv != null) {
            nameTv.setDuration(2000);
            nameTv.setModleType(DigitalRollingTextView.ModleType.COOLER_TYPE);
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

    @OnClick({R.id.junk_iv, R.id.cpucooler_iv, R.id.app_lock_iv, R.id.notice_iv, R.id.network_monitoring_iv, R.id.wifi_iv,R.id.scan_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.junk_iv:
                startActivity(new Intent(getContext(), JunkCleanerActivity.class));
                break;
            case R.id.cpucooler_iv:
                startActivity(new Intent(getContext(), CpuCoolerScanActivity.class));
                break;
            case R.id.app_lock_iv:
                boolean isFirstLock = (boolean) SpHelper.getInstance().get(Constant.LOCK_IS_FIRST_LOCK, true);
                if (isFirstLock) {
                    startActivity(new Intent(getContext(), GestureCreateActivity.class));
                } else {
                    startActivity(new Intent(getContext(), AppLockActivity.class));
                }
                break;
            case R.id.notice_iv:
                startActivity(new Intent(getContext(), NotificationCleanerActivity.class));
                break;
            case R.id.network_monitoring_iv:
                startActivity(new Intent(getContext(), TrafficStatisticsActivity.class));
                break;
            case R.id.wifi_iv:
                startActivity(new Intent(getContext(), WifiManagerActivity.class));
                break;
            case R.id.scan_tv:
                showPhoneBooster();
                break;
        }
    }
}
