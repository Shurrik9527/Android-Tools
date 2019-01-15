package com.hz.maiku.maikumodule.modules.deepclean.uninstallapk;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.ApkBean;
import com.hz.maiku.maikumodule.modules.deepclean.cleansuccess.CleanSuccessActivity;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.widget.dialog.AlertDoubleBtnDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author heguogui
 * @version v 3.0.0
 * @describe 选择未安装ApkFragment
 * @date 2018/12/13
 * @email 252774645@qq.com
 */
public class SelectApkFragment extends Fragment implements SelectApkContract.View {

    private static final String TAG = SelectApkFragment.class.getName();
    @BindView(R2.id.select_apk_gv)
    GridView selectIamgeGv;
    @BindView(R2.id.select_apk_btn)
    Button selectImageBtn;

    private SelectApkContract.Presenter presenter;
    private SelectApkAdapter mSelectApkAdapter;
    private List<ApkBean> mlist;

    public SelectApkFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectApkFragment newInstance() {
        SelectApkFragment fragment = new SelectApkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SelectApkPresenter(this, getContext());
    }


    @Override
    public void setPresenter(SelectApkContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        mlist = new ArrayList<>();
        mSelectApkAdapter = new SelectApkAdapter(getContext(), mlist, new SelectApkAdapter.OnClick() {
            @Override
            public void callBack(int pos) {
                if (mlist != null && mlist.size() > 0) {
                    ApkBean mApkBean = mlist.get(pos);
                    if (mApkBean.isSelect()) {
                        mApkBean.setSelect(false);
                    } else {
                        mApkBean.setSelect(true);
                    }
                    mlist.set(pos,mApkBean);
                    mSelectApkAdapter.setData(mlist);
                    mSelectApkAdapter.notifyDataSetChanged();
                    reflashView();
                }
            }
        });
        selectIamgeGv.setAdapter(mSelectApkAdapter);
        selectIamgeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });




        if (presenter != null) {
            presenter.getUnInstallApk();
        }
    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.select_apk_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void showApkData(List<ApkBean> mlists) {
        if (mlists != null && mlists.size() > 0 && mSelectApkAdapter != null) {
            this.mlist = mlists;
            mSelectApkAdapter.setData(mlist);
            mSelectApkAdapter.notifyDataSetChanged();
        }
        if(selectImageBtn!=null){
            selectImageBtn.setText("Clean");
        }
    }

    @Override
    public void reflashView() {
        try {
            if (mlist != null && mlist.size() > 0) {
                long mSize = 0;
                for (ApkBean bean : mlist) {
                    if(bean.isSelect()){
                        mSize = mSize + bean.getmSize();
                    }
                }
                if(selectImageBtn!=null){
                    if(mSize>0){
                        selectImageBtn.setText("Clean  "+FormatUtil.formatFileSize(mSize).toString() );
                    }else{
                        selectImageBtn.setText("Clean");
                    }
                }
            }
        }catch (Exception e){

        }
    }

    @Override
    public void cleanSuccess(long mSize) {

        Intent mIntent = new Intent(getActivity(), CleanSuccessActivity.class);
        if (mIntent != null) {
            mIntent.putExtra("BUNDLE", mSize + "");
        }
        startActivity(mIntent);
        getActivity().finish();
    }

    @OnClick(R2.id.select_apk_btn)
    public void onClick() {

        if(StringUtil.isFastDoubleClick()){
            return;
        }

        if(mlist!=null&&mlist.size()>0){
            if(presenter!=null){
                final List<ApkBean> list = new ArrayList<>();
                long mSize = 0;
                for (int i=0;i<mlist.size();i++){
                    ApkBean bean = mlist.get(i);
                    if(bean.isSelect()){
                        list.add(bean);
                        mSize = mSize + bean.getmSize();
                    }
                }
                if(list.size()>0){
                    final long finalMSize = mSize;
                    AlertDoubleBtnDialog dialog = new AlertDoubleBtnDialog(getContext(), getResources().getString(R.string.common_warm_prompt), "Are sure you clear "+list.size()+" App", "Sure", new AlertDoubleBtnDialog.ConfirmListener() {
                        @Override
                        public void callback() {
                            if(presenter!=null){
                                presenter.delectApkList(list, finalMSize);
                            }
                        }
                    });
                    dialog.show();
                }
            }
        }

    }
}
