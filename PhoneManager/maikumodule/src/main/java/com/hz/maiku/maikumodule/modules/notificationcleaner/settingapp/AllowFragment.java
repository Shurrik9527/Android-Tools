package com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.base.Constant;
import com.hz.maiku.maikumodule.bean.NotificationBean;
import com.hz.maiku.maikumodule.manager.NotificationsManager;
import com.hz.maiku.maikumodule.util.SpHelper;
import com.hz.maiku.maikumodule.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/21
 * @email 252774645@qq.com
 */
public class AllowFragment extends Fragment implements BlockConstract.View {
    private static final String TAG = AllowFragment.class.getName();
    @BindView(R2.id.allow_rv)
    RecyclerView allowRv;


    private AllowAdapter mAllowAdapter;
    private BlockConstract.Presenter mPresenter;
    public List<NotificationBean> alllists;

    public AllowFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AllowFragment newInstance() {
        AllowFragment fragment = new AllowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.allow_fragment, container, false);
        ButterKnife.bind(this, root);
        initData();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new BlockPresenter(this, getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void initData() {
        initRecycleView();
    }


    @Override
    public void initRecycleView() {
        // RecycleView 布局类型设置
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        allowRv.setLayoutManager(layoutManager);
        mAllowAdapter = new AllowAdapter(getActivity());
        allowRv.setAdapter(mAllowAdapter);
        allowRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if(view.getId()==R.id.settingapp_allow_cb){
                    alllists =adapter.getData();
                    if (alllists != null && alllists.size() > 0) {
                        NotificationBean bean = alllists.get(position);
                        if (bean.isOpen()) {
                            bean.setOpen(false);
                        } else {
                            bean.setOpen(true);
                        }
                        alllists.set(position,bean);
                        //mAllowAdapter.setNewData(alllists);
                        NotificationsManager.getmInstance().updadeAppSelectStateOb(bean);
                        mAllowAdapter.notifyItemChanged(position);
                    }
                }
            }
        });
    }

    @Override
    public void showData(List<NotificationBean> lists) {

        if(lists==null||lists.size()==0){
            return;
        }
        if (mAllowAdapter != null) {
            boolean openstate = (boolean) SpHelper.getInstance().get(Constant.NOTIFICATION_OPEN_STATE,false);
            mAllowAdapter.setOpenState(openstate);
            mAllowAdapter.setNewData(lists);
        }
    }


    @Override
    public void setPresenter(BlockConstract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void initView() {

    }

    @Override
    public void showMessageTips(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtil.showToast(getActivity(), msg);
        }
    }

    public List<NotificationBean> getLists(){
        return  alllists;
    }

}
