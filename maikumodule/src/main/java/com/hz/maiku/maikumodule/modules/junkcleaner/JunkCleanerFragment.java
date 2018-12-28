package com.hz.maiku.maikumodule.modules.junkcleaner;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsflyer.AFInAppEventType;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.JunkCleanerGroupBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerMultiItemBean;
import com.hz.maiku.maikumodule.modules.junkcleaner.junkcleanersuccess.JunkCleanerSuccessActivity;
import com.hz.maiku.maikumodule.modules.junkcleaner.optimized.OptimizedActivity;
import com.hz.maiku.maikumodule.util.EventUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.util.TimeUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.DigitalRollingTextView;
import com.hz.maiku.maikumodule.widget.dialog.JunkCleanerDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 Fragment
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerFragment extends Fragment implements JunkCleanerContract.View {

    private static final String TAG = JunkCleanerFragment.class.getName();

    @BindView(R2.id.junkclearner_elv)
    ExpandableListView junkclearnerElv;
    @BindView(R2.id.junkclearner_start_iv)
    ImageView junkclearnerStartIv;
    @BindView(R2.id.junkclearner_current_scan_tv)
    TextView junkcleanerCurrentScanTv;
    Unbinder unbinder;
    @BindView(R2.id.junkclearner_point_iv)
    ImageView junkclearnerPointIv;
    @BindView(R2.id.junkclearner_num_tv)
    DigitalRollingTextView junkclearnerNumTv;
    @BindView(R2.id.junkclearner_num_dw_tv)
    TextView junkclearnerNumDwTv;
    @BindView(R2.id.junkclearner_Rl)
    RelativeLayout junkclearnerRl;
    private float mProgress = 0.0f;
    private JunkCleanerExpandAdapter mAdapter;
    private JunkCleanerContract.Presenter presenter;
    private JunkCleanerDialog mJunkCleanerDialog;
    private boolean isStart = true;
    private String mNum;
    private float mTemp = 0.0f;
    private static final Long totalNum = 3072L;
    private static final int PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1102;

    public JunkCleanerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static JunkCleanerFragment newInstance() {
        JunkCleanerFragment fragment = new JunkCleanerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.junkclearner_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        new JunkCleanerPresenter(this);
        initView();
        initData();
        initClickListener();
        return root;
    }

    @Override
    public void setPresenter(JunkCleanerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        junkclearnerNumTv.setText("0.00");
        junkclearnerNumTv.setModleType(DigitalRollingTextView.ModleType.MONEY_TYPE);
        startAnimation(-90.0f, 500);
        setBtnEnable(false);
    }


    @Override
    public void initClickListener() {

        junkclearnerStartIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isFastDoubleClick()) {
                    return;
                }
                setBtnEnable(false);
                isStart = false;
                if (presenter != null) {
                    presenter.startCleanJunkTask(mAdapter.getData());
                    //垃圾清理被点击了
                    EventUtil.sendEvent(getActivity(), AFInAppEventType.START_TRIAL, "JunkCleaner clicked!");
                }
            }
        });

    }

    @Override
    public void initData() {

        //8.0授权
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!hasPermission()) {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                startActivityForResult(intent, PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            } else {
                junkCleanerScan();
            }
        } else {
            junkCleanerScan();
        }

    }

    @Override
    public void junkCleanerScan() {
        //获取上次清理保存时间
        String lastTime = (String) SpHelper.getInstance().get(Constant.SAVE_JUNK_CLEANER_TIME, "");
        boolean isAll = (boolean) SpHelper.getInstance().get(Constant.SAVE_JUNK_CLEANER_ISALL, true);
        if (!TextUtils.isEmpty(lastTime) && !TimeUtil.isTrue(lastTime, TimeUtil.currentTimeStr(), 1000 * 60 * 5) && isAll) {
            //跳转 最佳页面
            Intent mIntent = new Intent(getActivity(), OptimizedActivity.class);
            mIntent.putExtra("BUNDLE", getResources().getString(R.string.junkcleaner_title));
            startActivity(mIntent);
            getActivity().finish();
        } else {
            startScan();
        }
    }

    @Override
    public void showTotalSize(String size) {

        if (TextUtils.isEmpty(size))
            return;
        Log.i(TAG, "size=" + size);

        if (size.contains(",")) {
            size = size.replace(",", ".");
        }

        if (junkclearnerNumTv != null) {
            if (size.contains("M")) {
                String[] sizes = size.split("M");
                mNum = sizes[0].trim();
                junkclearnerNumTv.setContent(mNum);
                junkclearnerNumDwTv.setText("MB");
                setProgress();
            } else if (size.contains("B")) {
                String[] sizes = size.split("B");
                mNum = sizes[0].trim();
                junkclearnerNumTv.setContent(mNum);
                junkclearnerNumDwTv.setText("B");
            } else if (size.contains("G")) {
                String[] sizes = size.split("G");
                mNum = sizes[0].trim();
                junkclearnerNumTv.setContent(mNum);
                junkclearnerNumDwTv.setText("G");
                try {
                    if (!TextUtils.isEmpty(mNum)) {
                        float number = Float.parseFloat(mNum) * 1024;
                        mNum = String.valueOf(number);
                        setProgress();
                    }
                } catch (Exception e) {

                }
            }

        }

    }

    @Override
    public void showCurrentScanJunkFileName(String path) {
        if (junkcleanerCurrentScanTv != null) {
            junkcleanerCurrentScanTv.setText(path);
        }
    }

    @Override
    public void showItemTotalJunkSize(int index, String junkSize) {
        if (mAdapter != null) {
            mAdapter.setItemTotalSize(index, junkSize);
        }
    }

    @Override
    public void showData(JunkCleanerGroupBean junkCleanerGroupBean) {
        if (junkCleanerGroupBean != null) {
            if (mAdapter != null) {
                mAdapter.setData(junkCleanerGroupBean);
            }
        }
    }

    @Override
    public void showDialog(int index) {
        if (mAdapter != null) {
            mAdapter.showItemDialog();
        }
    }

    @Override
    public void dissDialog(int index) {
        if (mAdapter != null) {
            mAdapter.dismissItemDialog(index);
        }
    }

    @Override
    public void clickGroup(boolean isExpand, int index) {
        if (junkclearnerElv != null) {
            if (!isExpand) {
                junkclearnerElv.expandGroup(index);
            } else {
                junkclearnerElv.collapseGroup(index);
            }
        }
    }

    @Override
    public void initAdapterData(List<JunkCleanerMultiItemBean> mlists) {

        mAdapter = new JunkCleanerExpandAdapter(getActivity(), mlists);
        junkclearnerElv.setGroupIndicator(null);
        junkclearnerElv.setChildIndicator(null);
        junkclearnerElv.setDividerHeight(0);
        junkclearnerElv.setAdapter(mAdapter);
    }

    @Override
    public void cleanFinish() {
        //保存此时清理状态
        SpHelper.getInstance().put(Constant.SAVE_JUNK_CLEANER_TIME, TimeUtil.currentTimeStr());
        mJunkCleanerDialog = new JunkCleanerDialog(getContext(), new JunkCleanerDialog.DismissListener() {
            @Override
            public void callBack() {
                if (mJunkCleanerDialog != null && mJunkCleanerDialog.isShowing()) {
                    mJunkCleanerDialog.dismiss();
                    mJunkCleanerDialog = null;
                }
                Intent mIntent = new Intent(getActivity(), JunkCleanerSuccessActivity.class);
                if (!TextUtils.isEmpty(mNum)) {
                    if (mIntent != null) {
                        mIntent.putExtra("BUNDLE", mNum + "");
                    }
                }
                startActivity(mIntent);
                getActivity().finish();
            }
        });
        mJunkCleanerDialog.show();
        mJunkCleanerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mJunkCleanerDialog != null && mJunkCleanerDialog.isShowing()) {
                    mJunkCleanerDialog.dismiss();
                }
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

    }

    @Override
    public void startHeartBeat() {

        new Thread() {
            public void run() {
                while (isStart) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                playHeartbeatAnimation();
                            }
                        });
                    }
                }
            }

            ;
        }.start();
    }

    @Override
    public void showMessageTips(String msg) {
        if (TextUtils.isEmpty(msg))
            return;
        ToastUtil.showToast(getActivity(), msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (presenter != null) {
            presenter.unsubscribe();
        }
    }


    @Override
    public void playHeartbeatAnimation() {

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f));
        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.4f));
        animationSet.setDuration(1000);
        animationSet.setInterpolator(new AccelerateInterpolator());
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.addAnimation(new ScaleAnimation(1.2f, 1.0f, 1.2f,
                        1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f));
                animationSet.addAnimation(new AlphaAnimation(0.4f, 1.0f));

                animationSet.setDuration(1000);
                animationSet.setInterpolator(new DecelerateInterpolator());
                animationSet.setFillAfter(false);
                // 实现心跳的View
                if (junkclearnerStartIv != null) {
                    junkclearnerStartIv.startAnimation(animationSet);
                }
            }
        });
        // 实现心跳的View
        if (junkclearnerStartIv != null) {
            junkclearnerStartIv.startAnimation(animationSet);
        }
    }

    @Override
    public void stopScan() {

    }

    @Override
    public void cleanSpData() {
        SpHelper.getInstance().remove(Constant.SAVE_JUNK_CLEANER_TIME);
        SpHelper.getInstance().remove(Constant.SAVE_JUNK_CLEANER_ISALL);
    }

    @Override
    public void startScan() {
        cleanSpData();
        if (presenter != null) {
            //初始化适配器数据
            presenter.initAdapterData();
            //开始雷达扫描任务
            presenter.startScanTask();

            presenter.allMemorySpace();
        }

        //心跳
        startHeartBeat();
    }

    @Override
    public void allMemorySpace(long num) {


    }

    @Override
    public void startAnimation(float progress, long time) {

        RotateAnimation rotateAnimation = new RotateAnimation(mTemp, progress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1);
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        rotateAnimation.setDuration(time);//设置动画持续时间
        rotateAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        if (junkclearnerPointIv != null) {
            junkclearnerPointIv.startAnimation(rotateAnimation);
        }
        mTemp = progress;

    }

    @Override
    public void setProgress() {

        float msize = Float.parseFloat(mNum);
        if (msize > 0) {
            if (msize < 100.0) {
                mProgress = -70.0f;
            } else if (msize >= 100 && msize < 150) {
                mProgress = -50.0f;
            } else if (msize >= 150 && msize < 200) {
                mProgress = -30.0f;
            } else if (msize >= 200 && msize < 250) {
                mProgress = -10.0f;
            } else if (msize >= 300 && msize < 400) {
                mProgress = 10.0f;
            } else if (msize >= 400 && msize < 600) {
                mProgress = 30.0f;
            } else if (msize >= 600 && msize < 1024) {
                mProgress = 50.0f;
            } else if (msize >= 1024 && msize < 2048) {
                mProgress = 60.0f;
            } else if (msize >= 2048) {
                mProgress = 70.0f;
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mHandler != null) {
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                }
            }, 100);
        }
    }

    @Override
    public void setBtnEnable(boolean enable) {
        if (junkclearnerStartIv != null) {
            if (enable) {
                junkclearnerStartIv.setEnabled(true);
            } else {
                junkclearnerStartIv.setEnabled(false);
            }
        }
    }

    @Override
    public boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager) getContext().getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getContext().getPackageName());
        }
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        return false;
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            startAnimation(mProgress, 5000);
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        if (junkclearnerPointIv != null) {
            junkclearnerPointIv.clearAnimation();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS) {
            if (!hasPermission()) {
                startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            } else {
                junkCleanerScan();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
