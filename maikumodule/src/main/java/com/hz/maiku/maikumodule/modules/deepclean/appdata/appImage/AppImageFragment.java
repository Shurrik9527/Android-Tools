package com.hz.maiku.maikumodule.modules.deepclean.appdata.appImage;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.AppManagerBean;
import com.hz.maiku.maikumodule.bean.ImageBean;
import com.hz.maiku.maikumodule.modules.deepclean.cleansuccess.CleanSuccessActivity;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.DeepCleanItemDecoration;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.SpaceItemDecoration;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.widget.dialog.AlertDoubleBtnDialog;
import com.hz.maiku.maikumodule.widget.dialog.ShowImagesDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author heguogui
 * @version v 3.0.0
 * @describe 降温成功Fragment
 * @date 2018/12/13
 * @email 252774645@qq.com
 */
public class AppImageFragment extends Fragment implements AppImageContract.View {

    private static final String TAG = AppImageFragment.class.getName();
    @BindView(R2.id.select_iamge_rv)
    RecyclerView selectIamgeGv;
    @BindView(R2.id.select_image_btn)
    Button selectImageBtn;
    private AppImageContract.Presenter presenter;
    private AppImageAdapter mImageAdapter;
    private List<ImageBean> mlist = new ArrayList<>();
    private List<ImageBean> mSelectlist = new ArrayList<>();
    private String temp;
    private long mSize = 0;
    public AppImageFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppImageFragment newInstance() {
        AppImageFragment fragment = new AppImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppImagePresenter(this, getContext());
        Bundle args =getArguments();
        if(args!=null){
            temp =  args.getString("APPDATA");
        }
    }


    @Override
    public void setPresenter(AppImageContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {

        mImageAdapter = new AppImageAdapter(getContext(), new AppImageAdapter.onClick() {
            @Override
            public void callBack(boolean[] list) {
                if(mSelectlist!=null){
                    mSelectlist.clear();
                }
                if(list!=null&&list.length>0){
                    mSize = 0;
                    for (int i=0;i<list.length;i++){
                        ImageBean mImageBean =mlist.get(i);
                        if(list[i]){
                            mSize =mSize+mImageBean.getSize();
                            mSelectlist.add(mImageBean);
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
            }
        });
        selectIamgeGv.setLayoutManager(new GridLayoutManager(getActivity(),3));
        selectIamgeGv.addItemDecoration(new DeepCleanItemDecoration(5));
        selectIamgeGv.setAdapter(mImageAdapter);
        selectIamgeGv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter.getData() != null && adapter.getData().size() > 0) {
                    ImageBean mImageBean = (ImageBean) adapter.getData().get(position);
                    if (view.getId() == R.id.select_image_iv) {
                        ShowImagesDialog imagesDialog = new ShowImagesDialog(getContext(),mImageBean.getmUrl());
                        imagesDialog.show();
                    }
                }
            }
        });

        if(presenter!=null){
            if(!TextUtils.isEmpty(temp)){
                if(temp.equals(AppUtil.qq_name)){
                    presenter.getImages("tencent/MobileQQ");
                }else if(temp.equals(AppUtil.weixing_name)){
                    presenter.getImages("tencent/MicroMsg");
                }else if(temp.equals(AppUtil.instagram_name)){
                    presenter.getImages("Pictures/Instagram");
                }else if(temp.equals(AppUtil.facebook_name)){
                    presenter.getImages("Pictures/Facebook");
                }
            }
        }
    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.select_app_image_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showData(List<ImageBean> mlists) {
        if (mlists != null && mlists.size() > 0 && mImageAdapter != null) {
            if(mlist!=null){
                mlist.clear();
            }
            mlist.addAll(mlists);
            mImageAdapter.setNewData(mlists);
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
                for (ImageBean bean : mlist) {
                    if(bean.isSelect()){
                        mSize = mSize + bean.getSize();
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

    @OnClick(R2.id.select_image_btn)
    public void onClick() {

        if(StringUtil.isFastDoubleClick()){
            return;
        }

        if(mlist!=null&&mlist.size()>0){
            if(presenter!=null) {
                if (mSelectlist.size() > 0) {
                    final long finalMSize = mSize;
                    AlertDoubleBtnDialog dialog = new AlertDoubleBtnDialog(getContext(), getResources().getString(R.string.common_warm_prompt), "Are sure you clear " + mSelectlist.size() + " pictures", "Sure", new AlertDoubleBtnDialog.ConfirmListener() {
                        @Override
                        public void callback() {
                            if (presenter != null) {
                                presenter.delectImageList(mSelectlist, finalMSize);
                            }
                        }
                    });
                    dialog.show();
                }
            }
        }

    }
}
