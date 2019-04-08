package com.ashelykzc.devicecleaner.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsflyer.AFInAppEventType;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.modules.aboutus.AboutUsActivity;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.setting.SettingLockActivity;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.EventUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.TimeUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.ashelykzc.devicecleaner.R;

import java.text.ParseException;

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
public class MineFragment extends Fragment implements MineContract.View {

    private MineContract.Presenter presenter;
    @BindView(R.id.mine_logo_iv)
    ImageView mineLogoIv;
    @BindView(R.id.mine_rigst_tv)
    TextView mineRigstTv;
    @BindView(R.id.mine_update_iv)
    ImageView mineUpdateIv;
    @BindView(R.id.mine_update_tv)
    TextView mineUpdateTv;
    @BindView(R.id.mine_about_iv)
    ImageView mineAboutIv;
    @BindView(R.id.mine_about_tv)
    TextView mineAboutTv;
    @BindView(R.id.mine_feedback_iv)
    ImageView mineFeedbackIv;
    @BindView(R.id.mine_feedback_tv)
    TextView mineFeedbackTv;
    @BindView(R.id.mine_setting_iv)
    ImageView mineSettingIv;
    @BindView(R.id.mine_setting_tv)
    TextView mineSettingTv;

    public MineFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void showRegistDay() {

    }

    @Override
    public void showUpdate() {
        if (AppUtil.isInstalled(getContext(), "com.android.vending")) {
            final String GOOGLE_PLAY = "com.android.vending";
            Uri uri = Uri.parse("market://details?id=" + Constant.PACKAGE_NAME);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(GOOGLE_PLAY);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            ToastUtil.showToast(getContext(), "Please install GooglePlay first!");
        }
    }

    @Override
    public void showFacebook() {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + Constant.GMAIL));
        data.putExtra(Intent.EXTRA_SUBJECT, "I want to say");
        data.putExtra(Intent.EXTRA_TEXT, "Hi, There!");
        startActivity(data);
    }

    @Override
    public void showAboutUs() {
        startActivity(new Intent(getActivity(), AboutUsActivity.class));
    }

    @Override
    public void showSetting() {
        startActivity(new Intent(getActivity(), SettingLockActivity.class));
    }

    @Override
    public void setPresenter(MineContract.Presenter presenter) {
        this.presenter =presenter;
    }

    @Override
    public void initView() {
        long mTime = (long) SpHelper.getInstance().get(Constant.REGIST_TIME, 0L);
        if (mTime > 0) {
            long newTime = System.currentTimeMillis();
            try {
                int days = TimeUtil.daysBetween(mTime, newTime);
                if (days == 0)
                    days = 1;
                String context = getString(R.string.mine_regist_time) + days + "days";
                SpannableString spanString = new SpannableString(context);
                AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(60);
                ForegroundColorSpan span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.yellow));
                spanString.setSpan(span, 31, context.length()-4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                spanString.setSpan(sizeSpan, 31, context.length() - 4, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                mineRigstTv.setText(spanString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.mine_update_tv, R.id.mine_about_tv, R.id.mine_feedback_tv, R.id.mine_setting_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_update_tv:
                showUpdate();
                //有人想更新程序
                EventUtil.sendEvent(getActivity(), AFInAppEventType.UPDATE, "Someone try to update!");
                break;
            case R.id.mine_about_tv:
                showAboutUs();
                break;
            case R.id.mine_feedback_tv:
                showFacebook();
                break;
            case R.id.mine_setting_tv:
                showSetting();
                break;
        }
    }
}
