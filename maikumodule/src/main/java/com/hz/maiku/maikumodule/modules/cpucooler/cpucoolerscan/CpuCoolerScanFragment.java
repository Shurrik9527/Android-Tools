package com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.base.MaiKuApp;
import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;
import com.hz.maiku.maikumodule.modules.cpucooler.CpuCoolerActivity;
import com.hz.maiku.maikumodule.modules.junkcleaner.optimized.OptimizedActivity;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.TimeUtil;
import com.hz.maiku.maikumodule.widget.dialog.AlertSingleDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CpuCoolerScanFragment extends Fragment implements CpuCoolerScanContract.View {
    private static final String TAG = CpuCoolerScanFragment.class.getName();
    @BindView(R2.id.cpucooler_thermometer_iv)
    ImageView cpucoolerThermometerIv;
    @BindView(R2.id.cpu_scan_iv)
    ImageView cpuScanIv;
    @BindView(R2.id.cpucooler_background_rl)
    RelativeLayout cpucoolerBackgroundRl;

    private CpuCoolerScanContract.Presenter presenter;

    private float temp;
    private static final int PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;
    public CpuCoolerScanFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CpuCoolerScanFragment newInstance() {
        CpuCoolerScanFragment fragment = new CpuCoolerScanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        new CpuCoolerScanPresenter(this);

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
        if(cpuScanIv!=null){
            cpuScanIv.clearAnimation();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.cpucooler_scan_dialo_layout, container, false);
        ButterKnife.bind(this, root);
        if (isOptimized()) {
            Intent mIntent = new Intent(getActivity(), OptimizedActivity.class);
            mIntent.putExtra("BUNDLE", getResources().getString(R.string.cpu_cooler));
            startActivity(mIntent);
            getActivity().finish();
        } else {
            initView();
            initData();
        }
        return root;
    }

    @Override
    public void setPresenter(CpuCoolerScanContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        int width =cpucoolerThermometerIv.getLayoutParams().width;
        int height =cpucoolerThermometerIv.getLayoutParams().height;
        RelativeLayout.LayoutParams param =new RelativeLayout.LayoutParams(5 * width / 8, 5 * height / 8);
        param.addRule(RelativeLayout.CENTER_IN_PARENT);
        cpucoolerThermometerIv.setLayoutParams(param);
        if(cpuScanIv!=null){
            ObjectAnimator animator = ObjectAnimator.ofFloat(cpuScanIv, "translationY", 0, 150, -200,0);
            animator.setDuration(2000);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.start();
        }

    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public void showProcessRunning(List<AppProcessInfornBean> mlists) {

        if(cpuScanIv!=null){
            cpuScanIv.clearAnimation();
        }
        Bundle mBundle = new Bundle();
        mBundle.putFloat("TEMP",temp);
        MaiKuApp.cpuLists =mlists;
        if(getActivity()!=null){
            Intent mIntent = new Intent(getActivity(), CpuCoolerActivity.class);
            mIntent.putExtra("BUNDLE",mBundle);
            startActivity(mIntent);
            getActivity().finish();
        }

    }

    @Override
    public void showTemperature(float temper) {
        this.temp = temper;
    }


    @Override
    public void initData() {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            if(!hasPermission()){
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent, PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }else{
                startScan();
            }
        }else{
            startScan();
        }
    }
    @Override
    public void startScan() {
        if (presenter != null) {
            presenter.startScanProcessRunningApp();
            if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
                presenter.runAppProcessInform();
            }else{
                presenter.getProcessRunningApp();
            }
        }
    }

    @Override
    public boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getContext().getPackageName());
        }
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        return false;
    }


    @Override
    public boolean isOptimized() {
        //获取上次清理保存时间
        String lastTime = (String) SpHelper.getInstance().get(Constant.SAVE_CPU_COOLER_TIME, "");
        if (!TextUtils.isEmpty(lastTime) && !TimeUtil.isTrue(lastTime, TimeUtil.currentTimeStr(), 1000 * 60 * 5)) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS) {
            if (!hasPermission()) {
                //startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
                AlertSingleDialog dialog = new AlertSingleDialog(getContext(), "PERMISSIONS", "Sorry! Permissions is not get,This modle can't use,Please try again。", "Sure", new AlertSingleDialog.ConfirmListener() {
                    @Override
                    public void callback() {
                        if(getActivity()!=null){
                            getActivity().finish();
                        }
                    }
                });
                dialog.show();
            }else{
                startScan();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
