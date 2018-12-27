package com.hz.maiku.maikumodule.modules.screenlocker;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.SildingFinishLayout;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.NOTIFICATION_SERVICE;


public class ScreenLockerFragment extends Fragment implements ScreenLockerContract.View {

    private ScreenLockerContract.Presenter presenter;
    @BindView(R2.id.sfl_content)
    SildingFinishLayout sflContent;
    @BindView(R2.id.tv_time)
    TextView tvTime;
    @BindView(R2.id.tv_date)
    TextView tvDate;
//    @BindView(R.id.tv_chargestatus)
//    TextView tvChargestatus;
    @BindView(R2.id.tv_batteryinfo)
    TextView tvBatterInfo;
    @BindView(R2.id.iv_batterystatus)
    ImageView ivBatterStatus;
    @BindView(R2.id.stv_slidetounlock)
    ShimmerTextView stvSlideToUnLock;

    public ScreenLockerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ScreenLockerFragment newInstance() {
        ScreenLockerFragment fragment = new ScreenLockerFragment();
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
        if(presenter!=null){
            presenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(presenter!=null){
            presenter.unsubscribe();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.screenlocker_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void setPresenter(ScreenLockerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        Shimmer shimmer = new Shimmer();
        shimmer.start(stvSlideToUnLock);
        sflContent.setOnSildingFinishListener(new SildingFinishLayout.OnSildingFinishListener() {
            @Override
            public void onSildingFinish() {
                getActivity().finish();
            }
        });
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }


    @Override
    public void showTime(String time) {
        tvTime.setText(time);
    }

    @Override
    public void showDate(String date) {
        tvDate.setText(date);
    }

//    @Override
//    public void showChargeStatus(boolean isCharging) {
//        if (isCharging) {
//            tvChargestatus.setText("Charging");
//        } else {
//            tvChargestatus.setText("Charged");
//        }
//    }


    @Override
    public void showBatteryInfo(int percent) {
        tvBatterInfo.setText("Current Power: " + percent + "%");
        if(percent <= 20){
            ivBatterStatus.setBackgroundResource(R.mipmap.bg_screenlocker_chargestatus_1);
        } else if (percent <= 40){
            ivBatterStatus.setBackgroundResource(R.mipmap.bg_screenlocker_chargestatus_2);
        } else if (percent <= 60){
            ivBatterStatus.setBackgroundResource(R.mipmap.bg_screenlocker_chargestatus_3);
        } else if (percent <= 80){
            ivBatterStatus.setBackgroundResource(R.mipmap.bg_screenlocker_chargestatus_4);
        } else {
            ivBatterStatus.setBackgroundResource(R.mipmap.bg_screenlocker_chargestatus_5);
            if(percent == 100) {
                presenter.checkChargingCompleted();
            }
        }
    }

    @Override
    public void showNotification(String message) {
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(getActivity(), "default")
                .setContentTitle("Tips:")
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        manager.notify(1, notification);
    }
}
