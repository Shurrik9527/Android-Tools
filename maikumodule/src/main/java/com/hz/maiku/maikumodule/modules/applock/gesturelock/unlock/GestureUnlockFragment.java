package com.hz.maiku.maikumodule.modules.applock.gesturelock.unlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.manager.CommLockInfoManager;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.createlock.GestureCreateFragment;
import com.hz.maiku.maikumodule.service.LockService;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.LockPatternUtils;
import com.hz.maiku.maikumodule.util.LockPatternViewPattern;
import com.hz.maiku.maikumodule.util.LockUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.LockPatternView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 解锁 界面
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class GestureUnlockFragment extends Fragment implements GestureUnlockContract.View {

    private static String TAG = GestureCreateFragment.class.getName();

    @BindView(R2.id.gestureunlock_app_logo_icon)
    ImageView gestureunlockAppLogoIcon;
    @BindView(R2.id.gestureunlock_app_lable)
    TextView gestureunlockAppLable;
    @BindView(R2.id.gestureunlock_app_unlock_icon)
    ImageView gestureunlockAppUnlockIcon;
    @BindView(R2.id.gestureunlock_app_unlock_name)
    TextView gestureunlockAppUnlockName;
    @BindView(R2.id.gestureunlock_app_unlock_top)
    TextView gestureunlockAppUnlockTop;
    @BindView(R2.id.gestureunlock_app_unlock_lpv)
    LockPatternView mLockPatternView;
    @BindView(R2.id.gestureunlock_unlock_layout)
    RelativeLayout gestureunlockUnlockLayout;
    //图案锁相关
    private PackageManager packageManager;
    private String pkgName; //解锁应用的包名
    private String actionFrom;//按返回键的操作
    private LockPatternUtils mLockPatternUtils;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private LockPatternViewPattern mPatternViewPattern;
    private GestureUnlockReceiver mGestureUnlockReceiver;
    private ApplicationInfo appInfo;
    public static final String FINISH_UNLOCK_THIS_APP = "SUCCESS_UNLOCK_APP";
    private Drawable iconDrawable;
    private String appLabel;
    private CommLockInfoManager mLockInfoManager;

    public GestureUnlockFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle =getArguments();
        if(mBundle!=null){
            //获取解锁应用的包名
            pkgName = mBundle.getString(Constant.LOCK_PACKAGE_NAME);
            //获取按返回键的操作
            actionFrom = mBundle.getString(Constant.LOCK_FROM);
        }
        //初始化
        packageManager = getActivity().getPackageManager();
        mLockInfoManager = new CommLockInfoManager(getActivity());
        mGestureUnlockReceiver = new GestureUnlockReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(FINISH_UNLOCK_THIS_APP);
        getActivity().registerReceiver(mGestureUnlockReceiver, filter);

    }

    // TODO: Rename and change types and number of parameters
    public static GestureUnlockFragment newInstance() {
        GestureUnlockFragment fragment = new GestureUnlockFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gestureunlock_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        initLockPatternView();
        return root;
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    @Override
    public void initLockPatternView() {
        mLockPatternView.setLineColorRight(getResources().getColor(R.color.white));
        mLockPatternUtils = new LockPatternUtils(getActivity());
        mPatternViewPattern = new LockPatternViewPattern(mLockPatternView);
        mPatternViewPattern.setPatternListener(new LockPatternViewPattern.onPatternListener() {
            @Override
            public void onPatternDetected(List<LockPatternView.Cell> pattern) {
                if (mLockPatternUtils.checkPattern(pattern)) { //解锁成功,更改数据库状态
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
                    if (actionFrom.equals(Constant.LOCK_FROM_LOCK_MAIN_ACITVITY)) {//是自己？
                        if(!TextUtils.isEmpty(Constant.PACKAGENAMEURL)){
                            if(AppUtil.isInstalled(getContext(),Constant.PACKAGENAMEURL)){
                                Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(Constant.PACKAGENAMEURL);
                                startActivity(intent);
                            }
                        }
                        getActivity().finish();
                    } else {
                        SpHelper.getInstance().put(Constant.LOCK_CURR_MILLISENCONS, System.currentTimeMillis());
                        SpHelper.getInstance().put(Constant.LOCK_LAST_LOAD_PKG_NAME, pkgName);//记录解锁包名

                        //发送最后解锁的时间给应用锁服务
                        Intent intent = new Intent(LockService.UNLOCK_ACTION);
                        intent.putExtra(LockService.LOCK_SERVICE_LASTTIME, System.currentTimeMillis());
                        intent.putExtra(LockService.LOCK_SERVICE_LASTAPP, pkgName);
                        getActivity().sendBroadcast(intent);
                        mLockInfoManager.unlockCommApplication(pkgName);
                        getActivity().finish();
                    }
                } else {
                    mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                    if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                        mFailedPatternAttemptsSinceLastTimeout++;
                        int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
                        if (retry >= 0) {
                            showMessageTips(getResources().getString(R.string.lock_need_to_unlock_wrong));
                        }
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= 3) { //失败次数大于3次
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    }
                    if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) { //失败次数大于阻止用户前的最大错误尝试次数
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    } else {
                        mLockPatternView.postDelayed(mClearPatternRunnable, 500);
                    }
                }
            }
        });
        mLockPatternView.setOnPatternListener(mPatternViewPattern);
        mLockPatternView.setTactileFeedbackEnabled(true);
    }

    @Override
    public void onBackKey(){

        if (actionFrom.equals(Constant.LOCK_FROM_FINISH)) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            getActivity().startActivity(homeIntent);
            getActivity().finish();
        } else if (actionFrom.equals(Constant.LOCK_FROM_LOCK_MAIN_ACITVITY)) {
            getActivity().finish();
        } else {
            if(!TextUtils.isEmpty(Constant.PACKAGENAMEURL)){
                if(AppUtil.isInstalled(getContext(),Constant.PACKAGENAMEURL)){
                    Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(Constant.PACKAGENAMEURL);
                    startActivity(intent);
                }
            }
        }
    }


    @Override
    public void startActivity(Context mContext, Class<?> mclass, Bundle mBundle) {

        SpHelper.getInstance().put(Constant.LOCK_STATE, true);////开启应用锁开关
        getActivity().startService(new Intent(getActivity(), LockService.class));
        SpHelper.getInstance().put(Constant.LOCK_IS_FIRST_LOCK, false);//第一次设置成功
        startActivity(new Intent(mContext, mclass));
        getActivity().finish();
    }

    @Override
    public void setPresenter(GestureUnlockContract.Presenter presenter) {

    }

    @Override
    public void initView() {

        gestureunlockAppLable.setText(getString(R.string.app_name));
        try {
            appInfo = packageManager.getApplicationInfo(pkgName, PackageManager.GET_UNINSTALLED_PACKAGES);
            if (appInfo != null) {
                iconDrawable = packageManager.getApplicationIcon(appInfo);
                appLabel = packageManager.getApplicationLabel(appInfo).toString();
                gestureunlockAppUnlockIcon.setImageDrawable(iconDrawable);
                gestureunlockAppUnlockName.setText(appLabel);
                gestureunlockAppUnlockTop.setText(getString(R.string.password_gestrue_tips));
                final Drawable icon = packageManager.getApplicationIcon(appInfo);
                gestureunlockUnlockLayout.setBackgroundDrawable(icon);
                gestureunlockUnlockLayout.getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                gestureunlockUnlockLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                                gestureunlockUnlockLayout.buildDrawingCache();
                                Bitmap bmp = LockUtil.drawableToBitmap(icon, gestureunlockUnlockLayout);
                                LockUtil.blur(getActivity(), LockUtil.big(bmp), gestureunlockUnlockLayout);  //高斯模糊
                                return true;
                            }
                        });
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getActivity(), msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGestureUnlockReceiver!=null){
            getActivity().unregisterReceiver(mGestureUnlockReceiver);
        }
    }

    private class GestureUnlockReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(FINISH_UNLOCK_THIS_APP)) {
                if(getActivity()!=null){
                    getActivity().finish();
                }
            }
        }
    }




}
