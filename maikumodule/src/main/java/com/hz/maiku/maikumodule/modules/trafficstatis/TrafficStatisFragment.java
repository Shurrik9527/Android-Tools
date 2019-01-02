package com.hz.maiku.maikumodule.modules.trafficstatis;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AFInAppEventType;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.TrafficStatisBean;
import com.hz.maiku.maikumodule.bean.WifiMobileBean;
import com.hz.maiku.maikumodule.util.EventUtil;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.TimeUtil;
import com.hz.maiku.maikumodule.widget.DigitalRollingTextView;
import com.hz.maiku.maikumodule.widget.dialog.AlertSingleDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe 降温成功Fragment
 * @date 2018/12/13
 * @email 252774645@qq.com
 */
public class TrafficStatisFragment extends Fragment implements TrafficStatisContract.View {


    private static final String TAG = TrafficStatisFragment.class.getName();
    @BindView(R2.id.trafficstatistics_total_tv)
    DigitalRollingTextView trafficstatisticsTotalTv;
    @BindView(R2.id.trafficstatistics_total_dw_tv)
    TextView trafficstatisticsTotalDwTv;
    @BindView(R2.id.trafficstatistics_total_rl)
    RelativeLayout trafficstatisticsTotalRl;
    @BindView(R2.id.trafficstatistics_time_tv)
    TextView trafficstatisticsTimeTv;
    @BindView(R2.id.trafficstatistics_rv)
    RecyclerView trafficstatisticsRv;
    private TrafficStatisContract.Presenter presenter;
    private TrafficStatisAdapter mAdapter;
    private static final int REQUEST_CODE = 0X01;

    public TrafficStatisFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TrafficStatisFragment newInstance() {
        TrafficStatisFragment fragment = new TrafficStatisFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new TrafficStatisPresenter(this, getContext());
        EventUtil.sendEvent(getActivity(), AFInAppEventType.START_TRIAL, "trafficstatis");
    }

    @Override
    public void showData(List<TrafficStatisBean> mlists) {
        if(mlists!=null){
            mAdapter.setNewData(mlists);
        }
    }

    @Override
    public void showAllTraffic(WifiMobileBean bean) {
        if(bean==null){
            return;
        }

        FormatUtil.FileSize mFileSize=FormatUtil.formatSizeBy1024(bean.getMobileSize()+bean.getWifiSize());
        if(trafficstatisticsTotalTv!=null&&mFileSize!=null){
            trafficstatisticsTotalTv.setDuration(2000);
            trafficstatisticsTotalTv.setModleType(DigitalRollingTextView.ModleType.MONEY_TYPE);
            trafficstatisticsTotalTv.setContent(mFileSize.mSize+"");
            trafficstatisticsTotalDwTv.setText(mFileSize.mUnit.name()+"");
        }
    }

    @Override
    public void showPermission() {
        RxPermissions rxPermission = new RxPermissions(getActivity());
        rxPermission.requestEach(Manifest.permission.READ_PHONE_STATE).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if(permission.granted){//授权了
                    if(!hasPermissionToReadNetworkStats()){//去授权
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent, REQUEST_CODE);
                    }else{
                        if(presenter!=null){
                            presenter.getMonthSize();
                            presenter.getMonthTrafficStatistics();
                        }
                    }
                }else{
                    AlertSingleDialog dialog = new AlertSingleDialog(getContext(), "NOTICTION", "Sorry! Permissions is not get,This modle can't use,Please try again。", "Sure", new AlertSingleDialog.ConfirmListener() {
                        @Override
                        public void callback() {
                            if(getActivity()!=null){
                                getActivity().finish();
                            }
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

    @Override
    public boolean hasPermissionToReadNetworkStats() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getActivity().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getActivity().getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        return false;
    }


    @Override
    public void setPresenter(TrafficStatisContract.Presenter presenter) {
        this.presenter =presenter;
    }

    @Override
    public void initView() {

        trafficstatisticsTimeTv.setText(TimeUtil.currentMonthStr()+getString(R.string.trafficstatis_month)+"Used");
        mAdapter = new TrafficStatisAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        trafficstatisticsRv.setLayoutManager(layoutManager);
        trafficstatisticsRv.setAdapter(mAdapter);
        showPermission();
    }

    @Override
    public void showMessageTips(String msg) {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.trafficstatistics_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(hasPermissionToReadNetworkStats()){
                if(presenter!=null){
                    presenter.getMonthSize();
                    presenter.getMonthTrafficStatistics();
                }
            }else{
                AlertSingleDialog dialog = new AlertSingleDialog(getContext(), "PERMISSIONS", "Sorry! Permissions is not get,This modle can't use,Please try again。", "Sure", new AlertSingleDialog.ConfirmListener() {
                    @Override
                    public void callback() {
                        if(getActivity()!=null){
                            getActivity().finish();
                        }
                    }
                });
                dialog.show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
