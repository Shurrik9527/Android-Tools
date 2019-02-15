package com.ashelykzc.phonekeeper.box;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.modules.applock.AppLockActivity;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.createlock.GestureCreateActivity;
import com.hz.maiku.maikumodule.modules.appmanager.AppManagerActivity;
import com.hz.maiku.maikumodule.modules.chargebooster.ChargeBoosterActivity;
import com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan.CpuCoolerScanActivity;
import com.hz.maiku.maikumodule.modules.junkcleaner.JunkCleanerActivity;
import com.hz.maiku.maikumodule.modules.notificationcleaner.NotificationCleanerActivity;
import com.hz.maiku.maikumodule.modules.trafficstatistics.TrafficStatisticsActivity;
import com.hz.maiku.maikumodule.modules.wifimanager.WifiManagerActivity;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.ashelykzc.phonekeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/2/14
 * @email 252774645@qq.com
 */
public class BoxFragment extends Fragment implements BoxContract.View {

    @BindView(R.id.toolbox_appmanager_iv)
    ImageView toolboxAppmanagerIv;
    @BindView(R.id.toolbox_appmanger_tv)
    TextView toolboxAppmangerTv;
    @BindView(R.id.toolbox_appmanager_rl)
    RelativeLayout toolboxAppmanagerRl;
    @BindView(R.id.toolbox_junkclean_iv)
    ImageView toolboxJunkcleanIv;
    @BindView(R.id.toolbox_junkclean_tv)
    TextView toolboxJunkcleanTv;
    @BindView(R.id.toolbox_junkclean_rl)
    RelativeLayout toolboxJunkcleanRl;
    @BindView(R.id.toolbox_cpucooler_iv)
    ImageView toolboxCpucoolerIv;
    @BindView(R.id.toolbox_cpucooler_tv)
    TextView toolboxCpucoolerTv;
    @BindView(R.id.toolbox_cpucooler_rl)
    RelativeLayout toolboxCpucoolerRl;
    @BindView(R.id.toolbox_notice_iv)
    ImageView toolboxNoticeIv;
    @BindView(R.id.toolbox_notice_tv)
    TextView toolboxNoticeTv;
    @BindView(R.id.toolbox_notice_rl)
    RelativeLayout toolboxNoticeRl;
    @BindView(R.id.toolbox_applock_iv)
    ImageView toolboxApplockIv;
    @BindView(R.id.toolbox_applock_tv)
    TextView toolboxApplockTv;
    @BindView(R.id.toolbox_applock_rl)
    RelativeLayout toolboxApplockRl;
    @BindView(R.id.toolbox_chargebooster_iv)
    ImageView toolboxChargeboosterIv;
    @BindView(R.id.toolbox_chargebooster_tv)
    TextView toolboxChargeboosterTv;
    @BindView(R.id.toolbox_chargebooster_rl)
    RelativeLayout toolboxChargeboosterRl;
    @BindView(R.id.toolbox_wifi_iv)
    ImageView toolboxWifiIv;
    @BindView(R.id.toolbox_wifi_tv)
    TextView toolboxWifiTv;
    @BindView(R.id.toolbox_wifi_rl)
    RelativeLayout toolboxWifiRl;
    @BindView(R.id.toolbox_trffic_iv)
    ImageView toolboxTrfficIv;
    @BindView(R.id.toolbox_trffic_tv)
    TextView toolboxTrfficTv;
    @BindView(R.id.toolbox_trffic_rl)
    RelativeLayout toolboxTrfficRl;
    private BoxContract.Presenter presenter;

    public BoxFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BoxFragment newInstance() {
        BoxFragment fragment = new BoxFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_toolbox, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }


    @Override
    public void showJunkCleaner() {
        Intent intent = new Intent(getActivity(), JunkCleanerActivity.class);
        startActivity(intent);
    }

    @Override
    public void showAppManager() {
        Intent intent = new Intent(getActivity(), AppManagerActivity.class);
        startActivity(intent);
    }

    @Override
    public void showCpuCooler() {
        Intent intent = new Intent(getActivity(), CpuCoolerScanActivity.class);
        startActivity(intent);
    }

    @Override
    public void showNotifiBooster() {
        Intent intent = new Intent(getActivity(), NotificationCleanerActivity.class);
        startActivity(intent);
    }


    @Override
    public void showChargeBooster() {
        Intent intent = new Intent(getActivity(), ChargeBoosterActivity.class);
        startActivity(intent);
    }

    @Override
    public void showWifi() {
        Intent intent = new Intent(getActivity(), WifiManagerActivity.class);
        startActivity(intent);
    }

    @Override
    public void showAppLock() {
        boolean isFirstLock = (boolean) SpHelper.getInstance().get(Constant.LOCK_IS_FIRST_LOCK, true);
        if (isFirstLock) {
            startActivity(new Intent(getContext(), GestureCreateActivity.class));
        } else {
            startActivity(new Intent(getContext(), AppLockActivity.class));
        }
    }

    @Override
    public void showTraffic() {
        Intent intent = new Intent(getActivity(), TrafficStatisticsActivity.class);
        startActivity(intent);
    }


    @Override
    public void setPresenter(BoxContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void initView() {

    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.toolbox_appmanger_tv, R.id.toolbox_junkclean_tv, R.id.toolbox_cpucooler_tv, R.id.toolbox_notice_tv, R.id.toolbox_applock_tv, R.id.toolbox_chargebooster_tv, R.id.toolbox_wifi_tv, R.id.toolbox_trffic_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbox_appmanger_tv:
                showAppManager();
                break;
            case R.id.toolbox_junkclean_tv:
                showJunkCleaner();
                break;
            case R.id.toolbox_cpucooler_tv:
                showCpuCooler();
                break;
            case R.id.toolbox_notice_tv:
                showNotifiBooster();
                break;
            case R.id.toolbox_applock_tv:
                showAppLock();
                break;
            case R.id.toolbox_chargebooster_tv:
                showChargeBooster();
                break;
            case R.id.toolbox_wifi_tv:
                showWifi();
                break;
            case R.id.toolbox_trffic_tv:
                showTraffic();
                break;
        }
    }
}
