package com.hz.maiku.maikumodule.modules.cpucooler.cpucoolersuccess;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.util.AdUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.DigitalRollingTextView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 降温成功Fragment
 * @date 2018/9/14
 * @email 252774645@qq.com
 */
public class CpuCoolerSuccessFragment extends Fragment implements CpuCoolerSuccessContract.View {

    @BindView(R2.id.cpucooler_success_tv)
    DigitalRollingTextView cpucoolerSuccessTv;
    @BindView(R2.id.cpucooler_success_iv)
    ImageView cpucoolerSuccessIv;
    @BindView(R2.id.cpucooler_success_rotate_iv)
    ImageView cpucoolerSuccessRotateIv;
    @BindView(R2.id.cpucooler_time_tv)
    TextView cpucoolerTimeTv;
    private CpuCoolerSuccessContract.Presenter presenter;
    private  String mTemp =null;
    private int sum = 60;
    public CpuCoolerSuccessFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CpuCoolerSuccessFragment newInstance() {
        CpuCoolerSuccessFragment fragment = new CpuCoolerSuccessFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle mBundle =getArguments();
            if(mBundle!=null){
                mTemp =mBundle.getString("BUNDLE");
            }
        }
        new CpuCoolerSuccessPresenter(this);



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
        View root = inflater.inflate(R.layout.cpucooler_success_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        startScan();
        return root;
    }

    @Override
    public void setPresenter(CpuCoolerSuccessContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {

        Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_chean_anim);
        cpucoolerSuccessRotateIv.startAnimation(rotate);
        cpucoolerSuccessTv.setDuration(2000);
        cpucoolerSuccessTv.setModleType(DigitalRollingTextView.ModleType.COOLER_TYPE);

        //广告
        AdUtil.showAds(getActivity(), "CpuCoolerSuccessFragment.initView()");
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }

    @Override
    public void showTemperature(float temper) {

        if(cpucoolerSuccessRotateIv!=null){
            cpucoolerSuccessRotateIv.clearAnimation();
            cpucoolerSuccessRotateIv.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(mTemp)){
            float temp =Float.parseFloat(mTemp)+1.0f;
            BigDecimal bigDecimal= new BigDecimal(Math.abs(temper-temp));
            float dropped =bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            if(cpucoolerSuccessTv!=null){
                cpucoolerSuccessTv.setContent(dropped+"");
            }
            triggerCountDown();
        }

    }

    @Override
    public void startScan() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(presenter!=null){
                    presenter.startScanProcess();
                }
            }
        },5000);



    }

    @Override
    public void startRotateAnimation() {

    }


    @Override
    public void triggerCountDown() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (sum >= 0) {
                    if (getActivity() == null) {
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sum--;
                    if(mHandler!=null){
                        mHandler.sendEmptyMessage(sum);
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mHandler!=null){
            mHandler = null;
        }
    }



    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                return;
            }
            if (getActivity() == null) {
                return;
            }
            if (msg.what > 0) {
                cpucoolerTimeTv.setText(msg.what + "s to reach the optimal cooling effect");
            } else {
                sum =0;
               if(getActivity()!=null){
                   getActivity().finish();
               }
            }
        }
    };

}
