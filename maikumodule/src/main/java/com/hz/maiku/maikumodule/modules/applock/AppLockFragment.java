package com.hz.maiku.maikumodule.modules.applock;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.CommLockInfo;
import com.hz.maiku.maikumodule.modules.applock.gesturelock.setting.SettingLockActivity;
import com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp.SettingAppActivity;
import com.hz.maiku.maikumodule.util.LockUtil;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.DialogPermission;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 应用锁Fragment
 * @date 2018/10/11
 * @email 252774645@qq.com
 */
public class AppLockFragment extends Fragment implements AppLockContract.View {


    private static final String TAG = AppLockFragment.class.getName();
    @BindView(R2.id.applock_searchview)
    SearchView applockSearchview;
    @BindView(R2.id.applock_searchview_rl)
    RelativeLayout applockSearchviewRl;
    @BindView(R2.id.applock_rv)
    RecyclerView applockRv;

    private static  final  int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    private AppLockContract.Presenter presenter;
    private AppLockAdapter mAdapter;
    private List<CommLockInfo> mSavelist=null;



    public AppLockFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppLockFragment newInstance() {
        AppLockFragment fragment = new AppLockFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        new AppLockPresenter(this, getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.setting_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.setting_default){
            startActivity(new Intent(getContext(),SettingLockActivity.class));
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.applock_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        initData();
        return root;
    }

    @Override
    public void setPresenter(AppLockContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {

        //搜索框初始化
        SearchView.SearchAutoComplete textView = applockSearchview.findViewById(R.id.search_src_text);
        if (textView != null) {
            textView.setTextSize(14);
            textView.setTextColor(getResources().getColor(R.color.cardview_dark_background));
            textView.setHintTextColor(getResources().getColor(R.color.gray1));
        }

        applockSearchview.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                saveAppData();
                return false;
            }
        });
        //搜索监听
        StringUtil.fromSearchView(applockSearchview).debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String query) throws Exception {
                        searchApp(query);
                    }
                });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        applockRv.setLayoutManager(layoutManager);
        mAdapter = new AppLockAdapter(getActivity());
        applockRv.setAdapter(mAdapter);

    }

    @Override
    public void showMessageTips(String msg) {
        ToastUtil.showToast(getContext(), msg);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void initData() {
        showPermission();
    }

    @Override
    public void showAppData(List<CommLockInfo> mlist) {

        if(mlist!=null&&mlist.size()>0){
            if(mAdapter!=null){
                if(mSavelist==null){
                    mSavelist = new ArrayList<>();
                }else{
                    mSavelist.clear();
                }
                mSavelist.addAll(mlist);

                int num =0;
                for (CommLockInfo bean :mlist){
                    if(bean.isFaviterApp()){
                        num++;
                    }
                }
                if(num>0){
                    boolean mstate = (boolean) SpHelper.getInstance().get(Constant.LOCK_PERMISSION, false);
                    if(!mstate){
                        showMessageTips(num+getString(R.string.gesturecreate_recommend_msg));
                        SpHelper.getInstance().put(Constant.LOCK_PERMISSION,true);
                    }
                }
                mAdapter.setLockInfos(mlist);
            }
        }
    }

    @Override
    public void searchApp(String text) {
        if(TextUtils.isEmpty(text)){
            saveAppData();
        }else{
            List<CommLockInfo> searchApp = new ArrayList<>();
            for (int i = 0; i < mSavelist.size(); i++) {
                CommLockInfo mCommLockInfoBean = mSavelist.get(i);
                if(!TextUtils.isEmpty(mCommLockInfoBean.getAppName())){
                    if (mCommLockInfoBean.getAppName().contains(text)) {
                        searchApp.add(mCommLockInfoBean);
                    }
                }
            }
            if(mAdapter!=null&&searchApp.size()>0){
                mAdapter.setLockInfos(searchApp);
            }
        }
    }

    @Override
    public void saveAppData() {
        if(mAdapter!=null&&mSavelist!=null&&mSavelist.size()>0){
            //如果之前选择了 则保留
            if(mAdapter.getItemCount()>0){
                List<CommLockInfo> searchApp= mAdapter.getData();
                for(CommLockInfo bean :searchApp){
                    for(int i=0;i<mSavelist.size();i++ ){
                        CommLockInfo bean0=mSavelist.get(i);
                        if(bean.getPackageName().equals(bean0.getPackageName())){
                            mSavelist.set(i,bean);
                        }
                    }
                }
            }
            mAdapter.setLockInfos(mSavelist);
        }
    }

    @Override
    public void showPermission() {

        boolean isFirstLock = (boolean) SpHelper.getInstance().get(Constant.LOCK_IS_FIRST_LOCK, true);
        if (!isFirstLock) { //如果第一次
            //如果没有获得查看使用情况权限和手机存在查看使用情况这个界面
            if (!LockUtil.isStatAccessPermissionSet(getActivity()) && LockUtil.isNoOption(getActivity())) {
                DialogPermission dialog = new DialogPermission(getActivity());
                dialog.show();
                dialog.setOnClickListener(new DialogPermission.onClickListener() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent,RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                    }
                });
                dialog.setOnClickColseListener(new DialogPermission.onClickListener() {
                    @Override
                    public void onClick() {
                        if(getActivity()!=null){
                            getActivity().finish();
                        }
                    }
                });
            }else{
                if(presenter!=null){
                    presenter.getAppLists();
                }
            }
        }else{
            if(presenter!=null){
                presenter.getAppLists();
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (LockUtil.isStatAccessPermissionSet(getActivity())) {
                if(presenter!=null){
                    presenter.getAppLists();
                }
            } else {
                showMessageTips("No Permission");
                getActivity().finish();
            }
        }
    }
}
