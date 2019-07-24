package com.doro4028.iggcleaner.main;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.doro4028.iggcleaner.WelcomeActivity;
import com.facebook.appevents.AppEventsLogger;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.DeviceInformBean;
import com.hz.maiku.maikumodule.modules.appmanager.AppManagerActivity;
import com.hz.maiku.maikumodule.modules.chargebooster.ChargeBoosterActivity;
import com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan.CpuCoolerScanActivity;
import com.hz.maiku.maikumodule.modules.junkcleaner.JunkCleanerActivity;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.doro4028.iggcleaner.R;
import com.hz.maiku.maikumodule.widget.dialog.UpdateAppDialog;
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

    @BindView(R.id.dunpai_iv)
    ImageView dunpaiIv;
    @BindView(R.id.start_tv)
    TextView startTv;
    @BindView(R.id.lav_phonebooster)
    LottieAnimationView lavPhoneBooster;
    @BindView(R.id.start_notice_tv)
    TextView startNoticeTv;

    public MainContract.Presenter presenter;
    private AppEventsLogger logger;

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
        logger =AppEventsLogger.newLogger(getActivity());
    }

    public void logSentRequestEvent (String event) {
        logger.logEvent(event);
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
        logSentRequestEvent("JunkCleanerEvent");
        showPermissions();

    }

    private void showPermissions() {
        if(getActivity()!=null){
            RxPermissions rxPermission1 = new RxPermissions(getActivity());
            rxPermission1.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(aBoolean -> {
                if (!aBoolean) {
                    ToastUtil.showToast(getActivity(), "Sorry! no permission, some functions are not available");
                    showPermissions();
                }else {
                    //垃圾清理
                    Intent intent = new Intent(getContext(), JunkCleanerActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @OnClick(R.id.cv_appmanager)
    @Override
    public void showAppManager() {
        logSentRequestEvent("AppManagerEvent");
        //应用管理
        Intent intent = new Intent(getContext(), AppManagerActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cv_cpucooler)
    @Override
    public void showCpuCooler() {
        logSentRequestEvent("CpuCoolerEvent");
        //手机降温
        Intent intent = new Intent(getContext(), CpuCoolerScanActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.dunpai_iv)
    @Override
    public void startTv() {
        startHeartBeat();
    }


    @OnClick(R.id.cv_chargebooster)
    @Override
    public void showChargeBooster() {
        logSentRequestEvent("ChargeBoosterEvent");
        //智能充电
        Intent intent = new Intent(getActivity(), ChargeBoosterActivity.class);
        startActivity(intent);
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
            if(presenter!=null){
                presenter.checkUpdate(getContext(),false);
            }
        }
    }

    @Override
    public void initView() {

        if(presenter!=null){
            presenter.checkOpenState();
        }

        lavPhoneBooster.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bigImageView();
                if(lavPhoneBooster!=null){
                    lavPhoneBooster.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }



    @Override
    public void playHeartbeatAnimation() {
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        animationSet.setDuration(500);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //播放动画
                if(lavPhoneBooster!=null){
                    lavPhoneBooster.setVisibility(View.VISIBLE);
                    lavPhoneBooster.playAnimation();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (dunpaiIv != null) {
            dunpaiIv.startAnimation(animationSet);
        }
    }


    private void  bigImageView(){
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        animationSet.setDuration(500);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                ToastUtil.showToast(getActivity(), "Your phone is much better now!");
                if(startTv!=null){
                    startTv.setVisibility(View.VISIBLE);
                }
                if(startNoticeTv!=null){
                    startNoticeTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (dunpaiIv != null) {
            dunpaiIv.startAnimation(animationSet);
        }
    }



    @Override
    public void startHeartBeat() {
        playHeartbeatAnimation();
        if(startTv!=null){
            startTv.setVisibility(View.GONE);
        }
        if(startNoticeTv!=null){
            startNoticeTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void showUpdateApp(String content, String type, String packageName,boolean isclick) {
        if(!TextUtils.isEmpty(type)){
            UpdateAppDialog dialog = null;
            if(type.equals("1")){
                dialog = new UpdateAppDialog(getContext(), content,false,new UpdateAppDialog.ConfirmListener() {
                    @Override
                    public void callback(){
                        if(!TextUtils.isEmpty(packageName)){
                            if (AppUtil.isInstalled(getContext(), "com.android.vending")) {
                                SpHelper.getInstance().put(Constant.SAVE_UPDATE_APP_ISINSTALL,true);
                                final String GOOGLE_PLAY = "com.android.vending";
                                Uri uri = Uri.parse("market://details?id=" +packageName);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setPackage(GOOGLE_PLAY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                ToastUtil.showToast(getContext(), "Please install GooglePlay first!");
                            }
                        }
                    }
                });
                dialog.show();
            }else if(type.equals("2")){
                dialog = new UpdateAppDialog(getContext(), content,true,new UpdateAppDialog.ConfirmListener() {
                    @Override
                    public void callback(){
                        if(!TextUtils.isEmpty(packageName)){
                            if (AppUtil.isInstalled(getContext(), "com.android.vending")) {
                                SpHelper.getInstance().put(Constant.SAVE_UPDATE_APP_ISINSTALL,true);
                                final String GOOGLE_PLAY = "com.android.vending";
                                Uri uri = Uri.parse("market://details?id=" +packageName);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setPackage(GOOGLE_PLAY);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                ToastUtil.showToast(getContext(), "Please install GooglePlay first!");
                            }
                        }
                    }
                });
                dialog.show();
            }else {
                //手动点击可以跳转
                if(isclick){
                    if (AppUtil.isInstalled(getContext(), "com.android.vending")) {
                        SpHelper.getInstance().put(Constant.SAVE_UPDATE_APP_ISINSTALL,true);
                        final String GOOGLE_PLAY = "com.android.vending";
                        Uri uri = Uri.parse("market://details?id=" +Constant.PACKAGE_NAME);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setPackage(GOOGLE_PLAY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        ToastUtil.showToast(getContext(), "Please install GooglePlay first!");
                    }
                }
            }

        }

    }

}
