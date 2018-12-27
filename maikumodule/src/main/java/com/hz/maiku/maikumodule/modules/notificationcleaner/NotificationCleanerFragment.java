package com.hz.maiku.maikumodule.modules.notificationcleaner;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.MaiKuApp;
import com.hz.maiku.maikumodule.bean.NotificationMsgBean;
import com.hz.maiku.maikumodule.manager.NotificationsManager;
import com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp.SettingAppActivity;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.NotificationsCleanerUtil;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.DialogPermission;
import com.hz.maiku.maikumodule.widget.SpaceItemDecoration;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/19
 * @email 252774645@qq.com
 */
public class NotificationCleanerFragment extends Fragment implements NotificationCleanerContract.View, SwipeItemClickListener {


    @BindView(R2.id.notification_smrv)
    SwipeMenuRecyclerView notificationcleanerRv;
    @BindView(R2.id.notification_btn)
    Button notificationBtn;
    @BindView(R2.id.notification_rl)
    RelativeLayout notificationRl;
    private NotificationCleanerContract.Presenter presenter;
    private static final int REQUEST_PRESSION_CODE = 0X05;
    private NotificationCleanerAdapter mAdapter;

    public NotificationCleanerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NotificationCleanerFragment newInstance() {
        NotificationCleanerFragment fragment = new NotificationCleanerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new NotificationCleanerPresenter(this, getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.setting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.setting_default){
            startActivity(new Intent(getContext(),SettingAppActivity.class));
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.notificationcleaner_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        initData();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        NotificationsCleanerUtil.toggleNotificationListenerService(getContext());
        checkPermission();
    }

    @Override
    public void showAllMsg(List<NotificationMsgBean> lists) {
        if (lists != null && lists.size() > 0) {
            mAdapter.setNewData(lists);
            if(notificationBtn!=null){
                notificationBtn.setText(getContext().getResources().getString(R.string.notification_cleaner_all)+" ("+lists.size()+")");
            }
            setViewState(true);
        } else {
            setEmptyView();
        }
    }


    @Override
    public void setEmptyView() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.view_empty_layout, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,R.id.view_empty_rl);
        mView.setLayoutParams(params);
        if(mAdapter!=null){
            mAdapter.setEmptyView(mView);
        }
        setViewState(false);
    }

    @Override
    public void showPermission() {
        try{
            startActivityForResult(new Intent(
                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), REQUEST_PRESSION_CODE);
        }catch (ActivityNotFoundException e){
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ComponentName cn = new ComponentName("com.android.settings","com.android.settings.Settings$NotificationAccessSettingsActivity");
                intent.setComponent(cn);
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings");
                startActivityForResult(intent, REQUEST_PRESSION_CODE);
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void checkPermission() {
        if (!NotificationsCleanerUtil.notificationListenerEnable(getContext())) {
            showPermission();
        } else {
            if (presenter != null) {
                presenter.showAllApps();
            }
        }
    }

    @Override
    public void initData() {
        mAdapter = new NotificationCleanerAdapter();
        if (notificationcleanerRv != null) {
            notificationcleanerRv.setAdapter(mAdapter);
        }
    }

    @Override
    public void setPresenter(NotificationCleanerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        // RecycleView 布局类型设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        notificationcleanerRv.setLayoutManager(layoutManager);
        //设置默认动画效果
        notificationcleanerRv.setItemAnimator(new DefaultItemAnimator());
        //上下拖拽开关 默认关闭
        notificationcleanerRv.setLongPressDragEnabled(true);
        //RecycleView item 的菜单创建
        notificationcleanerRv.setSwipeMenuCreator(swipeMenuCreator);
        //RecycleView item 监听
        notificationcleanerRv.setSwipeItemClickListener(this);
        //RecycleView item 的菜单监听
        notificationcleanerRv.setSwipeMenuItemClickListener(mMenuItemClickListener);
        notificationcleanerRv.addItemDecoration(new SpaceItemDecoration(20));
    }


    /**
     * RecycleView item中右侧 添加菜单
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.swipe_size_60);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem addItem = new SwipeMenuItem(getActivity())
                    .setBackground(R.color.red)
                    .setText(getString(R.string.common_delete))
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(addItem);// 添加菜单到右侧

        }
    };


    /**
     * RecyclerView的Item的Menu点击监听
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            List<NotificationMsgBean> lists = new ArrayList<>();
            if (mAdapter != null) {
                if (lists != null && lists.size() > 0) {
                    lists.clear();
                }
                lists.addAll(mAdapter.getData());
            }

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {//右侧菜单
                //防止双点击
                if (StringUtil.isFastDoubleClick()) {
                    return;
                }
                if (menuPosition == 0) {
                    updataNotification(adapterPosition, lists);
//                    //通知栏去除
//                    if(!TextUtils.isEmpty(mBean.getMkey())){
//                        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.cancel(Integer.parseInt(mBean.getMkey()));
//                    }
                }
            }
        }
    };


    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PRESSION_CODE) {
            if (!NotificationsCleanerUtil.notificationListenerEnable(getContext())) {
                showPermission();
            } else {
                NewbieGuide.with(this)
                        .setLabel("notificationBtn")
                        .addGuidePage(GuidePage.newInstance()
                                .setLayoutRes(R.layout.guide_notification_layout)
                        ).setShowCounts(1)
                        .show();
                if (presenter != null) {
                    presenter.showAllApps();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(View itemView, int position) {
        if (mAdapter != null) {
            List<NotificationMsgBean> mlist = mAdapter.getData();
            if(mlist==null||mlist.size()==0){
                return;
            }
            NotificationMsgBean mNotificationMsgBean = mlist.get(position);
            String packageName = mNotificationMsgBean.getPackageName();
            if (AppUtil.checkAppIsInstallToHome(getContext(), packageName)) {
                if (!TextUtils.isEmpty(packageName)) {

                    updataNotification(position, mlist);

                    PackageManager packageManager = getContext().getPackageManager();
                    Intent intent = new Intent();
                    intent = packageManager.getLaunchIntentForPackage(packageName);
                    startActivity(intent);
//                    //通知栏去除
//                    if(!TextUtils.isEmpty(mNotificationMsgBean.getMkey())){
//                        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                        notificationManager.cancel(Integer.parseInt(mNotificationMsgBean.getMkey()));
//                    }
                }
            } else {
                ToastUtil.showToast(getContext(), "Desktop is not App...");
            }

        }
    }

    @Override
    public void updataNotification(int position, List<NotificationMsgBean> mlist) {
        if(mlist==null||mlist.size()==0){
            return;
        }
        NotificationMsgBean mNotificationMsgBean = mlist.get(position);
        List<NotificationMsgBean> msgBeanList = new ArrayList<>();
        List<NotificationMsgBean> lastLists =new ArrayList<>();
        lastLists.addAll(mlist);
        for (NotificationMsgBean msgBean:mlist){
            if(msgBean.getPackageName().equals(mNotificationMsgBean.getPackageName())){
                msgBeanList.add(msgBean);
                lastLists.remove(msgBean);
            }
        }
        //数据库清除
        NotificationsManager.getmInstance().deleteNotificationInfoTable(msgBeanList);
        //界面更新
        mAdapter.setNewData(lastLists);
        if(lastLists.size()==0){
            setEmptyView();
        }

    }

    @Override
    public void setViewState(boolean state) {
        if(notificationBtn!=null){
            if(state){
                notificationBtn.setVisibility(View.VISIBLE);
                notificationRl.setVisibility(View.VISIBLE);
            }else {
                notificationBtn.setVisibility(View.GONE);
                notificationRl.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R2.id.notification_btn)
    public void onClick() {
        if(mAdapter!=null){

            if(StringUtil.isFastDoubleClick()){
                return;
            }
            List<NotificationMsgBean> mlist = mAdapter.getData();
            if(mlist!=null&&mlist.size()>0){
                mlist.clear();
                showAllMsg(mlist);
                NotificationsManager.getmInstance().deleteAllNotificationInfo();
                if(notificationBtn!=null){
                    notificationBtn.setText(getContext().getResources().getString(R.string.notification_cleaner_all));
                }
                setViewState(false);
            }
        }
    }
}
