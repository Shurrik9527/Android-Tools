package com.hz.maiku.maikumodule.modules.deepclean.selectVideo;


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
import com.hz.maiku.maikumodule.bean.VideoBean;
import com.hz.maiku.maikumodule.modules.deepclean.cleansuccess.CleanSuccessActivity;
import com.hz.maiku.maikumodule.modules.deepclean.selectImage.SelectImageAdapter;
import com.hz.maiku.maikumodule.modules.deepclean.selectImage.SelectImageContract;
import com.hz.maiku.maikumodule.modules.deepclean.selectImage.SelectImagePresenter;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.widget.dialog.AlertDoubleBtnDialog;

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
public class SelectVideoFragment extends Fragment implements SelectVideoContract.View {

    private static final String TAG = SelectVideoFragment.class.getName();
    @BindView(R2.id.select_iamge_gv)
    GridView selectIamgeGv;
    @BindView(R2.id.select_image_btn)
    Button selectImageBtn;

    private SelectVideoContract.Presenter presenter;
    private SelectVideoAdapter mSelectVideoAdapter;
    private List<VideoBean> mlist;

    public SelectVideoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectVideoFragment newInstance() {
        SelectVideoFragment fragment = new SelectVideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SelectVideoPresenter(this, getContext());
    }


    @Override
    public void setPresenter(SelectVideoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        mlist = new ArrayList<>();
        mSelectVideoAdapter = new SelectVideoAdapter(getContext(), mlist, new SelectVideoAdapter.OnClick() {
            @Override
            public void callBack(int pos) {
                if (mlist != null && mlist.size() > 0) {
                    VideoBean mVideoBean = mlist.get(pos);
                    if (mVideoBean.isSelect()) {
                        mVideoBean.setSelect(false);
                    } else {
                        mVideoBean.setSelect(true);
                    }
                    mlist.set(pos,mVideoBean);
                    mSelectVideoAdapter.setData(mlist);
                    mSelectVideoAdapter.notifyDataSetChanged();
                    reflashView();
                }
            }
        });
        selectIamgeGv.setAdapter(mSelectVideoAdapter);
        selectIamgeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });




        if (presenter != null) {
            presenter.getVideos();
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
    public void showVideoData(List<VideoBean> mlists) {
        if (mlists != null && mlists.size() > 0 && mSelectVideoAdapter != null) {
            this.mlist = mlists;
            mSelectVideoAdapter.setData(mlist);
            mSelectVideoAdapter.notifyDataSetChanged();
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
                for (VideoBean bean : mlist) {
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
            if(presenter!=null){
                final List<VideoBean> list = new ArrayList<>();
                long mSize = 0;
                for (int i=0;i<mlist.size();i++){
                    VideoBean bean = mlist.get(i);
                    if(bean.isSelect()){
                        list.add(bean);
                        mSize = mSize + bean.getSize();
                    }
                }
                if(list.size()>0){
                    final long finalMSize = mSize;
                    AlertDoubleBtnDialog dialog = new AlertDoubleBtnDialog(getContext(), getResources().getString(R.string.common_warm_prompt), "Are sure you clear "+list.size()+" Video", "Sure", new AlertDoubleBtnDialog.ConfirmListener() {
                        @Override
                        public void callback() {
                            if(presenter!=null){
                                presenter.delectVideoList(list, finalMSize);
                            }
                        }
                    });
                    dialog.show();
                }
            }
        }

    }
}
