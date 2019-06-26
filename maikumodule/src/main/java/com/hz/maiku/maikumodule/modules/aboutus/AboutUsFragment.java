package com.hz.maiku.maikumodule.modules.aboutus;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.util.AdUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AboutUsFragment extends Fragment implements AboutUsContract.View {
    @BindView(R2.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R2.id.tv_start)
    TextView tvStart;

    private AboutUsContract.Presenter presenter;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
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
        View root = inflater.inflate(R.layout.aboutus_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void setPresenter(AboutUsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        initVersionName();
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }

    @Override
    public void initVersionName() {
        String pkName = getActivity().getPackageName();
        String versionName = "1.0.0";
        try {
            versionName = getActivity().getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName = "Version: " + versionName;
        tvVersionName.setText(versionName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R2.id.tv_start)
    public void onClick() {
        boolean showbtn = (boolean) SpHelper.getInstance().get(Constant.ABOUT_SHOW_BTN,false);
        if(showbtn){
            SpHelper.getInstance().put(Constant.ABOUT_SHOW_BTN,false);
        }else {
            SpHelper.getInstance().put(Constant.ABOUT_SHOW_BTN,true);
        }
    }

    @OnClick(R2.id.about_iv)
    public void TestAd() {
        if(AdUtil.ABOUT_STATUS == 1) {
            AdUtil.showAds(getActivity(), "AboutUs");
        } else {
            ToastUtil.showToast(getActivity(), "Hello,World!");
        }
    }
}
