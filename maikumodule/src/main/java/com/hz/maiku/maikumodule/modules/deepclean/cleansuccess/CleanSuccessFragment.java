package com.hz.maiku.maikumodule.modules.deepclean.cleansuccess;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.util.AdUtil;
import com.hz.maiku.maikumodule.util.FormatUtil;
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
public class CleanSuccessFragment extends Fragment implements CleanSuccessContract.View {


    @BindView(R2.id.clean_success_iv)
    ImageView cleanSuccessIv;
    @BindView(R2.id.clean_success_rotate_iv)
    ImageView cleanSuccessRotateIv;
    @BindView(R2.id.clean_rotate_rl)
    RelativeLayout cleanRotateRl;
    @BindView(R2.id.clean_success_tv)
    DigitalRollingTextView cleanSuccessTv;
    @BindView(R2.id.clean_success_dw_tv)
    TextView cleanSuccessDwTv;
    @BindView(R2.id.clean_msg_tv)
    TextView cleanMsgTv;
    private CleanSuccessContract.Presenter presenter;
    private String mTemp = null;


    public CleanSuccessFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CleanSuccessFragment newInstance() {
        CleanSuccessFragment fragment = new CleanSuccessFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle mBundle = getArguments();
            if (mBundle != null) {
                mTemp = mBundle.getString("BUNDLE");
            }
        }
        new CleanSuccessPresenter(this);
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
        View root = inflater.inflate(R.layout.clean_success_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        showData(mTemp);
        return root;
    }

    @Override
    public void setPresenter(CleanSuccessContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {

        Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_chean_anim);
        cleanSuccessRotateIv.startAnimation(rotate);
        cleanSuccessTv.setDuration(2000);
        cleanSuccessTv.setModleType(DigitalRollingTextView.ModleType.COOLER_TYPE);

        //广告
        AdUtil.showAds(getActivity(), "CleanSuccessFragment.initView()");
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }

    @Override
    public void showData(String temper) {

        if (cleanSuccessRotateIv != null) {
            cleanSuccessRotateIv.clearAnimation();
            cleanSuccessRotateIv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(temper)) {

            FormatUtil.FileSize mFileSize=FormatUtil.formatFileSize(Long.parseLong(temper));
            String mSize =mFileSize.mSize;
            if (mSize.contains(",")) {
                mSize = mSize.replace(",", ".");
            }
            BigDecimal bigDecimal = new BigDecimal(mSize);
            float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            if (cleanSuccessTv != null) {
                cleanSuccessTv.setContent(dropped + "");
            }
            if(cleanSuccessDwTv !=null){
                cleanSuccessDwTv.setText(mFileSize.mUnit+"");
            }
        }

    }

    @Override
    public void showTitle(String content) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
