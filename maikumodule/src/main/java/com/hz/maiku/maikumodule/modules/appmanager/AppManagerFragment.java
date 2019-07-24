package com.hz.maiku.maikumodule.modules.appmanager;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.AppManagerBean;
import com.hz.maiku.maikumodule.modules.appmanager.appmanager1.AppManagerOneFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jaredrummler.android.processes.AndroidProcesses.TAG;


public class AppManagerFragment extends Fragment implements AppManagerContract.View {

    private AppManagerContract.Presenter presenter;

    @BindView(R2.id.lv_apps)
    ListView lvApps;
    private List<ApplicationInfo> data;

    private AppManagerAdapter adapter;
    private PackageReceiver mPackageReceiver;
    public AppManagerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppManagerFragment newInstance() {
        AppManagerFragment fragment = new AppManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        mPackageReceiver = new PackageReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        getContext().registerReceiver(mPackageReceiver, filter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(presenter!=null){
            presenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(presenter!=null){
            presenter.unsubscribe();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.appmanager_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void setPresenter(AppManagerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        data = new ArrayList<ApplicationInfo>();
        adapter = new AppManagerAdapter(getActivity(), data);
        lvApps.setAdapter(adapter);
        if(presenter!=null){
            presenter.loadData(getContext());
        }
    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void refresh() {
        if(presenter!=null){
            presenter.loadData(getContext());
        }
    }


    @Override
    public void onDestroy() {
        getContext().unregisterReceiver(mPackageReceiver);
        super.onDestroy();
    }

    @Override
    public void showAppData(List<ApplicationInfo> lists) {
        if(lists!=null&&lists.size()>0){
            if(data!=null&&data.size()>0){
                data.clear();
            }

            for (ApplicationInfo appManagerBean : lists) {
                if(!TextUtils.isEmpty(appManagerBean.packageName)&&getContext()!=null){
                    if (appManagerBean.packageName.equals(getContext().getPackageName())) {
                        lists.remove(appManagerBean);
                        break;
                    }
                }
            }

            data.addAll(lists);
            adapter.notifyDataSetChanged();
        }
    }

}
