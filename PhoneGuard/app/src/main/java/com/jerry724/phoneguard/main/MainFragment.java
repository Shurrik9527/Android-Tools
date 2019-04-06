package com.jerry724.phoneguard.main;

import android.Manifest;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.jerry724.phoneguard.R;
import com.hz.maiku.maikumodule.util.SpaceItemDecoration;
import com.hz.maiku.maikumodule.util.ToastUtil;
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
    @BindView(R.id.rv_main_functions)
    RecyclerView rvMainFunctions;

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

    @OnClick({R.id.lav_phonebooster, R.id.b_phonebooster})
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
                //优化加速
                ToastUtil.showToast(getActivity(), "Your phone is much better now!");
            }
        });
        animator.start();
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



        //实例化Adapter并且给RecyclerView设上
        MainFunctionsGridAdapter adapter = new MainFunctionsGridAdapter(getActivity(), presenter.getFunctions());
        rvMainFunctions.setAdapter(adapter);
        // 如果我们想要一个GridView形式的RecyclerView，那么在LayoutManager上我们就要使用GridLayoutManager
        // 实例化一个GridLayoutManager，列数为2
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        //设置颜色分割线
        rvMainFunctions.addItemDecoration(new SpaceItemDecoration(30));
        //把LayoutManager设置给RecyclerView
        rvMainFunctions.setLayoutManager(layoutManager);

    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
