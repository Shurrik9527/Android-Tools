package com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TableRow;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.appsflyer.AFInAppEventType;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.base.FragmentPagerAdapter;
import com.hz.maiku.maikumodule.bean.NotificationBean;
import com.hz.maiku.maikumodule.util.EventUtil;
import com.hz.maiku.maikumodule.util.SpHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author heguogui
 * @version v 1.0.0
 */
public class SettingAppFragment extends Fragment implements SettingAppConstract.View {

    @BindView(R2.id.notification_blocked_rb)
    RadioButton notificationBlockedRb;
    @BindView(R2.id.notification_allowed_rb)
    RadioButton notificationAllowedRb;
    @BindView(R2.id.settingapp_iv)
    ImageView settingappIv;
    @BindView(R2.id.discount_tableRow2)
    TableRow discountTableRow2;
    @BindView(R2.id.settingapp_vp)
    ViewPager settingappVp;
    @BindView(R2.id.settingapp_openstate_sw)
    Switch settingappOpenstateSw;

    private SettingAppConstract.Presenter presenter;


    private SettingAppConstract.Presenter mPresenter;
    private ArrayList<Fragment> mLists;
    private BlockFragment mBlockFragment;
    private AllowFragment mAllowFragment;
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private Bundle mBundle;


    public SettingAppFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingAppFragment newInstance() {
        SettingAppFragment fragment = new SettingAppFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        new SettingAppPresenter(this, getActivity());

    }


    @Override
    public void initData() {

        notificationBlockedRb.setOnClickListener(new MyOnClickListener(0));
        notificationAllowedRb.setOnClickListener(new MyOnClickListener(1));


        boolean openstate = (boolean) SpHelper.getInstance().get(Constant.NOTIFICATION_OPEN_STATE, false);
        if (openstate) {
            settingappOpenstateSw.setChecked(true);
        } else {
            settingappOpenstateSw.setChecked(false);
        }
        settingappOpenstateSw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    EventUtil.sendEvent(getActivity(), AFInAppEventType.START_TRIAL, "Notification Cleaner clicked!");
                    SpHelper.getInstance().put(Constant.NOTIFICATION_OPEN_STATE, true);
                    presenter.subscribe();
                } else {
                    SpHelper.getInstance().put(Constant.NOTIFICATION_OPEN_STATE, false);
                    presenter.subscribe();
                }
            }
        });
    }

    @Override
    public void initViewPager() {

        Intent mIntent = getActivity().getIntent();
        if (mIntent != null) {
            mBundle = mIntent.getBundleExtra("BUNDLE");
        }
        mLists = new ArrayList<>();
        mBlockFragment = mBlockFragment.newInstance();
        if (mBundle != null) {
            mBlockFragment.setArguments(mBundle);
        }

        mAllowFragment = mAllowFragment.newInstance();
        if (mBundle != null) {
            mAllowFragment.setArguments(mBundle);
        }

        mLists.add(mBlockFragment);
        mLists.add(mAllowFragment);
        FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getFragmentManager(), mLists);
        settingappVp.setAdapter(mFragmentPagerAdapter);
        settingappVp.setOffscreenPageLimit(1);
        settingappVp.setCurrentItem(0);
        settingappVp.addOnPageChangeListener(new MyViewPagerOnChangeClickListener());
    }

    @Override
    public void initImageView() {

        bmpW = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.ic_common_tabimg).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        settingappIv.setImageMatrix(matrix);// 设置动画初始位置
    }

    @Override
    public void showBlockData(List<NotificationBean> lists) {
        if (lists != null && lists.size() > 0) {
            if (mBlockFragment != null) {
                mBlockFragment.showData(lists);
            }
        }
    }

    @Override
    public void showAllowData(List<NotificationBean> lists) {
        if (lists != null && lists.size() > 0) {
            if (mAllowFragment != null) {
                mAllowFragment.showData(lists);
            }
        }
    }


    @Override
    public void setPresenter(SettingAppConstract.Presenter presenter) {
        this.presenter = presenter;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        getActivity().getMenuInflater().inflate(R.menu.setting_menu,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==R.id.setting_default){
//            startActivity(new Intent(getActivity(),SettingAppActivity.class));
//        }
//        return true;
//    }

    @Override
    public void onResume() {
        super.onResume();
        NewbieGuide.with(this).setLabel("settingappOpenstateSw").addGuidePage(GuidePage.newInstance().addHighLight(settingappOpenstateSw, HighLight.Shape.CIRCLE).setLayoutRes(R.layout.guide_notification_open_layout)).setShowCounts(1).show();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settingapp_fragmen, container, false);
        ButterKnife.bind(this, root);
        initView();
        initData();
        return root;
    }

    @Override
    public void initView() {
        initViewPager();
        initImageView();
    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public class MyViewPagerOnChangeClickListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int one = offset * 2 + bmpW;
            Animation animation = new TranslateAnimation(one * currIndex, one * position, 0, 0);
            currIndex = position;
            animation.setFillAfter(true);// True:图片停在动画结束位置
            animation.setDuration(300);
            settingappIv.startAnimation(animation);
            switch (position) {
                case 0:
                    notificationBlockedRb.setChecked(true);
                    break;
                case 1:
                    notificationAllowedRb.setChecked(true);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            settingappVp.setCurrentItem(index);
        }
    }

}
