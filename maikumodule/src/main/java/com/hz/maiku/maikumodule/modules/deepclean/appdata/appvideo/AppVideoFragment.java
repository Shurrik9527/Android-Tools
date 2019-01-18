package com.hz.maiku.maikumodule.modules.deepclean.appdata.appvideo;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.VideoBean;
import com.hz.maiku.maikumodule.modules.deepclean.cleansuccess.CleanSuccessActivity;
import com.hz.maiku.maikumodule.modules.deepclean.selectVideo.SelectVideoAdapter;
import com.hz.maiku.maikumodule.modules.deepclean.selectVideo.SelectVideoContract;
import com.hz.maiku.maikumodule.modules.deepclean.selectVideo.SelectVideoPresenter;
import com.hz.maiku.maikumodule.util.AppUtil;
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
 * @describe 降温成功Fragment
 * @date 2018/12/13
 * @email 252774645@qq.com
 */
public class AppVideoFragment extends Fragment implements AppVideoContract.View {

    private static final String TAG = AppVideoFragment.class.getName();
    @BindView(R2.id.select_iamge_gv)
    GridView selectIamgeGv;
    @BindView(R2.id.select_image_btn)
    Button selectImageBtn;

    private AppVideoContract.Presenter presenter;
    private AppVideoAdapter mAppVideoAdapter;
    private List<VideoBean> mlist;
    private String temp;
    public AppVideoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppVideoFragment newInstance() {
        AppVideoFragment fragment = new AppVideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppVideoPresenter(this, getContext());
        Bundle args =getArguments();
        if(args!=null){
            temp =  args.getString("APPDATA");
        }
    }


    @Override
    public void setPresenter(AppVideoContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        mlist = new ArrayList<>();
        mAppVideoAdapter = new AppVideoAdapter(getContext(), mlist, new AppVideoAdapter.OnClick() {
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
                    mAppVideoAdapter.setData(mlist);
                    mAppVideoAdapter.notifyDataSetChanged();
                    reflashView();
                }
            }
        });
        selectIamgeGv.setAdapter(mAppVideoAdapter);
        selectIamgeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mlist != null && mlist.size() > 0) {
                    VideoBean mVideoBean = mlist.get(position);
                    startVideos(mVideoBean.getmUrl());
                }
            }
        });


        if(presenter!=null){
            if(!TextUtils.isEmpty(temp)){
                if(temp.equals(AppUtil.qq_name)){
                    presenter.getVideos("tencent/MobileQQ");
                }else if(temp.equals(AppUtil.weixing_name)){
                    presenter.getVideos("tencent/MicroMsg");
                }else if(temp.equals(AppUtil.instagram_name)){
                    presenter.getVideos("Movies/Instagram");
                }else if(temp.equals(AppUtil.facebook_name)){
                    presenter.getVideos("Movies/Facbook");
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
        if (mlists != null && mlists.size() > 0 && mAppVideoAdapter != null) {
            this.mlist = mlists;
            mAppVideoAdapter.setData(mlist);
            mAppVideoAdapter.notifyDataSetChanged();
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

    @Override
    public void startVideos(String url) {
        if(!TextUtils.isEmpty(url)){
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setType("video/*");
            intent.setDataAndType(uri , "video/*");
            startActivity(intent);
        }
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
