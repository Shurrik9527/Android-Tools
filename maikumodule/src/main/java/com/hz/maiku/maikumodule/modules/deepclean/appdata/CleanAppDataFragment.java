package com.hz.maiku.maikumodule.modules.deepclean.appdata;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.AppBean;
import com.hz.maiku.maikumodule.bean.AppDataBean;
import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.bean.ImageBean;
import com.hz.maiku.maikumodule.bean.VideoBean;
import com.hz.maiku.maikumodule.modules.deepclean.appdata.appImage.AppImageActivity;
import com.hz.maiku.maikumodule.modules.deepclean.appdata.appaudio.AppAudiosActivity;
import com.hz.maiku.maikumodule.modules.deepclean.appdata.appvideo.AppVideoActivity;
import com.hz.maiku.maikumodule.modules.deepclean.selectVideo.SelectVideoActivity;
import com.hz.maiku.maikumodule.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author heguogui
 * @version v 3.0.0
 * @describe 清理特殊app 数据Fragment
 * @date 2018/12/13
 * @email 252774645@qq.com
 */
public class CleanAppDataFragment extends Fragment implements CleanAppDataContract.View {

    private static final String TAG = CleanAppDataFragment.class.getName();
    @BindView(R2.id.appdata_clean_rv)
    RecyclerView appdataCleanRv;

    private CleanAppDataContract.Presenter presenter;
    private CleanAppDataAdapter mCleanAppDataAdapter;
    private List<AppDataBean> mlist;

    public CleanAppDataFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CleanAppDataFragment newInstance() {
        CleanAppDataFragment fragment = new CleanAppDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CleanAppDataPresenter(this, getContext());
    }


    @Override
    public void setPresenter(CleanAppDataContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.getData();
        }
    }

    @Override
    public void initView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        appdataCleanRv.setLayoutManager(layoutManager);
        mlist = new ArrayList<>();
        mCleanAppDataAdapter = new CleanAppDataAdapter();
        appdataCleanRv.setAdapter(mCleanAppDataAdapter);

        appdataCleanRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                AppDataBean mAppDataBean = (AppDataBean) adapter.getData().get(position);
                if (mAppDataBean != null) {
                    if (view.getId() == R.id.select_appdata_image_tv) {
                        if(getActivity()!=null&&mAppDataBean.getImageBean()!=null&&mAppDataBean.getImageBean().size()>0){
                            Intent mIntent = new Intent(getActivity(), AppImageActivity.class);
                            Bundle mBundle = new Bundle();
                            if(mAppDataBean.getAppName().equals(AppUtil.qq_name)){
                                mBundle.putString("APPDATA",AppUtil.qq_name);
                            }else if(mAppDataBean.getAppName().equals(AppUtil.weixing_name)){
                                mBundle.putString("APPDATA",AppUtil.weixing_name);
                            }else if(mAppDataBean.getAppName().equals(AppUtil.facebook_name)){
                                mBundle.putString("APPDATA",AppUtil.facebook_name);
                            }else if(mAppDataBean.getAppName().equals(AppUtil.instagram_name)){
                                mBundle.putString("APPDATA",AppUtil.instagram_name);
                            }
                            mIntent.putExtra("BUNDLE",mBundle);
                            startActivity(mIntent);
                        }
                    } else if (view.getId() == R.id.select_appdata_video_tv) {
                        if(getActivity()!=null&&mAppDataBean.getVideoBean()!=null&&mAppDataBean.getVideoBean().size()>0){
                            Intent mIntent = new Intent(getActivity(), AppVideoActivity.class);
                            Bundle mBundle = new Bundle();
                            if(mAppDataBean.getAppName().equals(AppUtil.qq_name)){
                                mBundle.putString("APPDATA",AppUtil.qq_name);
                            }else if(mAppDataBean.getAppName().equals(AppUtil.weixing_name)){
                                mBundle.putString("APPDATA",AppUtil.weixing_name);
                            }else if(mAppDataBean.getAppName().equals(AppUtil.facebook_name)){
                                mBundle.putString("APPDATA",AppUtil.facebook_name);
                            }else if(mAppDataBean.getAppName().equals(AppUtil.instagram_name)){
                                mBundle.putString("APPDATA",AppUtil.instagram_name);
                            }
                            mIntent.putExtra("BUNDLE",mBundle);
                            startActivity(mIntent);
                        }

                    }else if (view.getId() == R.id.select_appdata_audio_tv) {

                        if(getActivity()!=null&&mAppDataBean.getAudioBean()!=null&&mAppDataBean.getAudioBean().size()>0){
                            Intent mIntent = new Intent(getActivity(), AppAudiosActivity.class);
                            Bundle mBundle = new Bundle();
                            if(mAppDataBean.getAppName().equals(AppUtil.qq_name)){
                                mBundle.putString("APPDATA",AppUtil.qq_name);
                            }else if(mAppDataBean.getAppName().equals(AppUtil.weixing_name)){
                                mBundle.putString("APPDATA",AppUtil.weixing_name);
                            }else if(mAppDataBean.getAppName().equals(AppUtil.facebook_name)){
                                mBundle.putString("APPDATA",AppUtil.facebook_name);
                            }else if(mAppDataBean.getAppName().equals(AppUtil.instagram_name)){
                                mBundle.putString("APPDATA",AppUtil.instagram_name);
                            }
                            mIntent.putExtra("BUNDLE",mBundle);
                            startActivity(mIntent);
                        }

                    }
                }
            }
        });

    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.select_appdata_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void showData(List<AppBean> mlists) {
        if(mlists!=null&&mlists.size()>0){
            if(presenter!=null){
                presenter.getAppDatas(mlists);
            }
        }
    }

    @Override
    public void showAppData(List<AppDataBean> mlists) {
        if(mlists!=null&&mlists.size()>0){

            for (int i=0;i<mlists.size();i++) {
                long imagesize =0;
                long audiosize =0;
                long vodicesize =0;
                AppDataBean mAppDataBean =mlists.get(i);
                List<ImageBean> imageBean = mAppDataBean.getImageBean();
                List<AudioBean> audioBean = mAppDataBean.getAudioBean();
                List<VideoBean> videoBean = mAppDataBean.getVideoBean();
                for (ImageBean mImageBean : imageBean) {
                    imagesize = imagesize + mImageBean.getSize();
                }
                for (AudioBean mAudioBean : audioBean) {
                    audiosize = audiosize + mAudioBean.getSize();
                }

                for (VideoBean mVideoBean : videoBean) {
                    vodicesize = vodicesize + mVideoBean.getSize();
                }
                mAppDataBean.setImageSize(imagesize);
                mAppDataBean.setVideoSize(vodicesize);
                mAppDataBean.setAudioSize(audiosize);
                long allSize =imagesize+vodicesize+audiosize;
                mAppDataBean.setAppSize(allSize);
                mlists.set(i,mAppDataBean);
                if(allSize==0){
                    mlists.remove(i);
                }
            }
            mCleanAppDataAdapter.setNewData(mlists);
        }
    }
}
