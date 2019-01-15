package com.hz.maiku.maikumodule.modules.deepclean.selectImage;


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
import com.hz.maiku.maikumodule.bean.AlbumBean;
import com.hz.maiku.maikumodule.bean.ImageBean;
import com.hz.maiku.maikumodule.modules.cpucooler.cpucoolersuccess.CpuCoolerSuccessActivity;
import com.hz.maiku.maikumodule.modules.deepclean.cleansuccess.CleanSuccessActivity;
import com.hz.maiku.maikumodule.util.DeepCleanUtil;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.widget.dialog.AlertDoubleBtnDialog;
import com.hz.maiku.maikumodule.widget.dialog.AlertSingleDialog;
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
public class SelectImageFragment extends Fragment implements SelectImageContract.View {

    private static final String TAG = SelectImageFragment.class.getName();
    @BindView(R2.id.select_iamge_gv)
    GridView selectIamgeGv;
    @BindView(R2.id.select_image_btn)
    Button selectImageBtn;
    private SelectImageContract.Presenter presenter;
    private SelectImageAdapter mImageAdapter;
    private List<ImageBean> mlist;
    private String deleteUrl;
    public SelectImageFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectImageFragment newInstance() {
        SelectImageFragment fragment = new SelectImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SelectImagePresenter(this, getContext());
    }


    @Override
    public void setPresenter(SelectImageContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        mlist = new ArrayList<>();
        mImageAdapter = new SelectImageAdapter(getContext(), mlist, new SelectImageAdapter.OnClick() {
            @Override
            public void callBack(int pos) {
                if (mlist != null && mlist.size() > 0) {
                    ImageBean mImageBean = mlist.get(pos);
                    if (mImageBean.isSelect()) {
                        mImageBean.setSelect(false);
                    } else {
                        mImageBean.setSelect(true);
                    }
                    mlist.set(pos, mImageBean);
                    mImageAdapter.setData(mlist);
                    mImageAdapter.notifyDataSetChanged();
                    reflashView();
                }
            }
        });
        selectIamgeGv.setAdapter(mImageAdapter);
        selectIamgeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mlist != null && mlist.size() > 0) {
                    ImageBean mImageBean = mlist.get(position);
                    ShowImagesDialog imagesDialog = new ShowImagesDialog(getContext(),mImageBean.getmUrl());
                    imagesDialog.show();
                }
            }
        });

        if (presenter != null) {
            presenter.getImages();
        }
    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.select_image_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showData(List<AlbumBean> mlists) {
        if (mlists != null && mlists.size() > 0 && mImageAdapter != null) {
            Collections.sort(mlists);
            List<ImageBean> mlist = new ArrayList<>();
            for (int i = 0; i < mlists.size(); i++) {
                mlist.addAll(mlists.get(i).getImageBeans());
            }
            this.mlist = mlist;
            mImageAdapter.setData(mlist);
            mImageAdapter.notifyDataSetChanged();
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
//                    if(mSize>(1024*1024)){
//                        double size =mSize/(1024*1024);
//                        selectImageBtn.setText("Clean  "+ String.format("%.1f",size).toString()+"MB");
//                    }else if(mSize>10240&&mSize<(1024*1024)){
//                        double size =mSize/1024;
//                        selectImageBtn.setText("Clean  "+String.format("%.1f",size).toString()+"KB");
//                    } else if(mSize>0&&mSize<1024){
//                        selectImageBtn.setText("Clean  "+mSize+"B");
//                    }else {
//                        selectImageBtn.setText("Clean");
//                    }
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
            if(presenter!=null){
                final List<ImageBean> list = new ArrayList<>();
                long mSize = 0;
                for (int i=0;i<mlist.size();i++){
                    ImageBean bean = mlist.get(i);
                    if(bean.isSelect()){
                        list.add(bean);
                        mSize = mSize + bean.getSize();
                    }
                }

                if(list.size()>0){
                    final long finalMSize = mSize;
                    AlertDoubleBtnDialog dialog = new AlertDoubleBtnDialog(getContext(), getResources().getString(R.string.common_warm_prompt), "Are sure you clear "+list.size()+" pictures", "Sure", new AlertDoubleBtnDialog.ConfirmListener() {
                        @Override
                        public void callback() {
                            if(presenter!=null){
                                presenter.delectImageList(list, finalMSize);
                            }
                        }
                    });
                    dialog.show();
                }
            }
        }

    }
}
