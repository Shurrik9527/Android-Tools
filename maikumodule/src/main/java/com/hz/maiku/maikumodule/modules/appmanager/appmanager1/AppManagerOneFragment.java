package com.hz.maiku.maikumodule.modules.appmanager.appmanager1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.AppManagerBean;
import com.hz.maiku.maikumodule.tasks.UninstallAppTask;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AppManagerOneFragment extends Fragment implements AppManagerOneContract.View {
    private static final String TAG = AppManagerOneFragment.class.getName();
    @BindView(R2.id.appmanagerone_rv)
    RecyclerView appmanageroneRv;
    @BindView(R2.id.appmanagerone_btn)
    Button appmanageroneBtn;
    private AppManagerOneContract.Presenter presenter;

    private List<AppManagerBean> data;
    private int count;
    private APPManagerOneAdapter adapter;
    private UninstallReceiver mUninstallReceiver;
    public AppManagerOneFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppManagerOneFragment newInstance() {
        AppManagerOneFragment fragment = new AppManagerOneFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        mUninstallReceiver = new UninstallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        getContext().registerReceiver(mUninstallReceiver, filter);
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
        View root = inflater.inflate(R.layout.appmanagerone_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void setPresenter(AppManagerOneContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        appmanageroneRv.setLayoutManager(layoutManager);
        adapter = new APPManagerOneAdapter(getActivity());
        appmanageroneRv.setAdapter(adapter);
        appmanageroneRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                data =adapter.getData();
                AppManagerBean managerBean =data.get(position);
                if(data!=null&&data.size()>0){
                    if(view.getId()==R.id.appmanager_item_rl){
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:"+managerBean.getApplicationInfo().packageName));
                        getContext().startActivity(intent);
                    }else if(view.getId()==R.id.appmanager_item_cb) {
                        if (managerBean.isSelect()) {
                            managerBean.setSelect(false);
                        } else {
                            managerBean.setSelect(true);
                        }
                        data.set(position, managerBean);
                        adapter.setNewData(data);
                        refreshView();
                    }
                }
            }
        });
        if (presenter != null) {
            presenter.loadData(getContext());
        }
    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(),msg);
    }

    @Override
    public void refresh(String packageName) {
        if(data!=null){

            if(packageName.contains(":")){
                packageName =packageName.split(":")[1];
            }
            for (AppManagerBean appManagerBean :data){
                if(appManagerBean.getApplicationInfo().packageName.equals(packageName)){
                    Log.i(TAG,"==="+appManagerBean.getApplicationInfo().packageName);
                    data.remove(appManagerBean);
                    break;
                }
            }
            adapter.setNewData(data);
            refreshView();
        }
    }

    @Override
    public void showAppData(List<AppManagerBean> lists) {
        if (lists != null && lists.size() > 0) {
            adapter.setNewData(lists);
        }
    }

    @Override
    public void guideView() {

    }

    @Override
    public void refreshView() {

        if(appmanageroneBtn!=null){
            if(data!=null){
                count =0;
                for (AppManagerBean mbean:data){
                    if(mbean.isSelect()){
                        count++;
                    }
                }
                if(count>0){
                    appmanageroneBtn.setText("Uninstall("+count+")");
                }else {
                    appmanageroneBtn.setText("Uninstall");
                }
            }else {
                appmanageroneBtn.setText("Uninstall");
            }
        }
    }


    /**
     * 广播
     */
    private class UninstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent!=null){
                String packageName = intent.getDataString();
                if(!TextUtils.isEmpty(packageName)){
                    refresh(packageName);
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(mUninstallReceiver);
        super.onDestroy();
    }

    @OnClick(R2.id.appmanagerone_btn)
    public void onClick() {
        if(count==0){
            showMessageTips("Please select need to uninstall App");
        }else{
            if(StringUtil.isFastDoubleClick()){
                return;
            }
            if(data!=null&&data.size()>0){
                new UninstallAppTask(getContext(),data).execute();
            }
        }

    }
}
