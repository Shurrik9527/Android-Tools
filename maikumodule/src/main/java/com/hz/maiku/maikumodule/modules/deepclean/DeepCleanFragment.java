package com.hz.maiku.maikumodule.modules.deepclean;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.AlbumBean;
import com.hz.maiku.maikumodule.bean.ApkBean;
import com.hz.maiku.maikumodule.bean.AppBean;
import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.bean.BigFileBean;
import com.hz.maiku.maikumodule.bean.ImageBean;
import com.hz.maiku.maikumodule.bean.VideoBean;
import com.hz.maiku.maikumodule.modules.deepclean.appdata.CleanAppDataActivity;
import com.hz.maiku.maikumodule.modules.deepclean.selectImage.SelectImageActivity;
import com.hz.maiku.maikumodule.modules.deepclean.selectVideo.SelectVideoActivity;
import com.hz.maiku.maikumodule.modules.deepclean.selectaudio.SelectAudiosActivity;
import com.hz.maiku.maikumodule.modules.deepclean.selectbigfile.SelectBigFileActivity;
import com.hz.maiku.maikumodule.modules.deepclean.uninstallapk.SelectApkActivity;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.widget.DigitalRollingTextView;
import com.hz.maiku.maikumodule.widget.ProgressWheel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author heguogui
 * @version v 3.0.0
 * @describe 降温成功Fragment
 * @date 2018/12/13
 * @email 252774645@qq.com
 */
public class DeepCleanFragment extends Fragment implements DeepCleanContract.View {

    private static final String TAG = DeepCleanFragment.class.getName();

    @BindView(R2.id.deepclean_total_tv)
    DigitalRollingTextView deepcleanTotalTv;
    @BindView(R2.id.deepclean_total_dw_tv)
    TextView deepcleanTotalDwTv;
    @BindView(R2.id.deepclean_total_rl)
    RelativeLayout deepcleanTotalRl;
    @BindView(R2.id.deepclean_scan_state_tv)
    TextView deepcleanScanStateTv;
    @BindView(R2.id.deepclean_image_iv)
    ImageView deepcleanImageIv;
    @BindView(R2.id.deepclean_images_next_iv)
    ImageView deepcleanImagesNextIv;
    @BindView(R2.id.deepclean_image_rl)
    RelativeLayout deepcleanImageRl;
    @BindView(R2.id.deepclean_iamges_rv)
    RecyclerView deepcleanIamgesRv;
    @BindView(R2.id.deepclean_images_rl)
    RelativeLayout deepcleanImagesRl;
    @BindView(R2.id.deepclean_videos_iv)
    ImageView deepcleanVideosIv;
    @BindView(R2.id.deepclean_videos_next_iv)
    ImageView deepcleanVideosNextIv;
    @BindView(R2.id.deepclean_video_rl)
    RelativeLayout deepcleanVideoRl;
    @BindView(R2.id.deepclean_videos_rv)
    RecyclerView deepcleanVideosRv;
    @BindView(R2.id.deepclean_videos_rl)
    RelativeLayout deepcleanVideosRl;
    @BindView(R2.id.deepclean_uninstallapp_iv)
    ImageView deepcleanUninstallappIv;
    @BindView(R2.id.deepclean_uninstallapp_next_iv)
    ImageView deepcleanUninstallappNextIv;
    @BindView(R2.id.deepclean_uninstallapp_rl)
    RelativeLayout deepcleanUninstallappRl;
    @BindView(R2.id.deepclean_uninstallapp_rv)
    RecyclerView deepcleanUninstallappRv;
    @BindView(R2.id.deepclean_uninstallapps_rl)
    RelativeLayout deepcleanUninstallappsRl;
    @BindView(R2.id.deepclean_audio_iv)
    ImageView deepcleanAudioIv;
    @BindView(R2.id.deepclean_audio_title_tv)
    TextView deepcleanAudioTitleTv;
    @BindView(R2.id.deepclean_audio_next_iv)
    ImageView deepcleanAudioNextIv;
    @BindView(R2.id.deepclean_audios_rl)
    RelativeLayout deepcleanAudiosRl;
    @BindView(R2.id.deepclean_appdata_iv)
    ImageView deepcleanAppdataIv;
    @BindView(R2.id.deepclean_appdata_title_tv)
    TextView deepcleanAppdataTitleTv;
    @BindView(R2.id.deepclean_appdata_next_iv)
    ImageView deepcleanAppdataNextIv;
    @BindView(R2.id.deepclean_appdatas_rl)
    RelativeLayout deepcleanAppdatasRl;
    @BindView(R2.id.deepclean_largefiles_iv)
    ImageView deepcleanLargefilesIv;
    @BindView(R2.id.deepclean_largefiles_title_tv)
    TextView deepcleanLargefilesTitleTv;
    @BindView(R2.id.deepclean_largefiles_next_iv)
    ImageView deepcleanLargefilesNextIv;
    @BindView(R2.id.deepclean_largefiles_rl)
    RelativeLayout deepcleanLargefilesRl;
    @BindView(R2.id.deepclean_images_num_tv)
    TextView deepcleanImagesNumTv;
    @BindView(R2.id.deepclean_videos_num_tv)
    TextView deepcleanVideosNumTv;
    @BindView(R2.id.deepclean_uninstallapp_num_tv)
    TextView deepcleanUninstallappNumTv;
    @BindView(R2.id.deepclean_audio_num_tv)
    TextView deepcleanAudioNumTv;
    @BindView(R2.id.deepclean_appcache_num_tv)
    TextView deepcleanAppcacheNumTv;
    @BindView(R2.id.deepclean_largefile_num_tv)
    TextView deepcleanLargefileNumTv;
    @BindView(R2.id.deepclean_images_num_tv_pw)
    ProgressWheel deepcleanImagesNumTvPw;
    @BindView(R2.id.deepclean_videos_num_tv_pw)
    ProgressWheel deepcleanVideosNumTvPw;
    @BindView(R2.id.deepclean_uninstallapp_num_tv_pw)
    ProgressWheel deepcleanUninstallappNumTvPw;
    @BindView(R2.id.deepclean_audio_tv_pw)
    ProgressWheel deepcleanAudioTvPw;
    @BindView(R2.id.deepclean_appdata_tv_pw)
    ProgressWheel deepcleanAppdataTvPw;
    @BindView(R2.id.deepclean_largefile_tv_pw)
    ProgressWheel deepcleanLargefileTvPw;


    private DeepCleanContract.Presenter presenter;
    private DeepCleanImageAdapter mImageAdapter;
    private DeepCleanVideoAdapter mDeepCleanVideoAdapter;
    private DeepCleanUnInstallApkAdapter mDeepCleanUnInstallApkAdapter;
    public long mAllSize = 0;

    public DeepCleanFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DeepCleanFragment newInstance() {
        DeepCleanFragment fragment = new DeepCleanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DeepCleanPresenter(this, getContext());
    }



    @Override
    public void showImageData(List<AlbumBean> mlists) {
        if(deepcleanImagesNumTvPw!=null){
            deepcleanImagesNumTvPw.setVisibility(View.GONE);
        }
        if (mlists == null || mlists.size() == 0) {
            deepcleanIamgesRv.setVisibility(View.GONE);
        } else {
            deepcleanIamgesRv.setVisibility(View.VISIBLE);
            Collections.sort(mlists);
            List<ImageBean> mlist = new ArrayList<>();
            int msize = mlists.get(0).getImageBeans().size();
            if (msize > 4) {
                mlist.add(mlists.get(0).getImageBeans().get(0));
                mlist.add(mlists.get(0).getImageBeans().get(1));
                mlist.add(mlists.get(0).getImageBeans().get(2));
                mlist.add(mlists.get(0).getImageBeans().get(3));
                mlist.add(mlists.get(0).getImageBeans().get(4));

            } else if (msize == 4) {
                mlist.add(mlists.get(0).getImageBeans().get(0));
                mlist.add(mlists.get(0).getImageBeans().get(1));
                mlist.add(mlists.get(0).getImageBeans().get(2));
                mlist.add(mlists.get(0).getImageBeans().get(3));
            } else if (msize == 3) {
                mlist.add(mlists.get(0).getImageBeans().get(0));
                mlist.add(mlists.get(0).getImageBeans().get(1));
                mlist.add(mlists.get(0).getImageBeans().get(2));
            } else if (msize == 2) {
                mlist.add(mlists.get(0).getImageBeans().get(0));
                mlist.add(mlists.get(0).getImageBeans().get(1));
            } else if (msize == 1) {
                mlist.add(mlists.get(0).getImageBeans().get(0));
            }
            if (mImageAdapter != null) {
                mImageAdapter.setNewData(mlist);
            }
            if (deepcleanImagesNumTv != null) {
                List<ImageBean> mImageBeanlists = new ArrayList<>();
                for (int i = 0; i < mlists.size(); i++) {
                    mImageBeanlists.addAll(mlists.get(i).getImageBeans());
                }
                long mSize = 0;
                for (int i = 0; i < mImageBeanlists.size(); i++) {
                    ImageBean bean = mImageBeanlists.get(i);
                    mSize = mSize + bean.getSize();
                }
                if (mSize > 0) {
                    FormatUtil.FileSize mFileSize = FormatUtil.formatFileSize(mSize);
                    String msizes = mFileSize.mSize;
                    if (msizes.contains(",")) {
                        msizes = msizes.replace(",", ".");
                    }
                    BigDecimal bigDecimal = new BigDecimal(msizes);
                    float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                    if (deepcleanImagesNumTv != null) {
                        deepcleanImagesNumTv.setVisibility(View.VISIBLE);
                        deepcleanImagesNumTv.setText(dropped + " " + mFileSize.mUnit + "");
                    }

                    showAllSize(mSize);
                }
            }
        }
    }

    @Override
    public void showVideos(List<VideoBean> mlists) {
        if(deepcleanVideosNumTvPw!=null){
            deepcleanVideosNumTvPw.setVisibility(View.GONE);
        }

        if (mlists == null || mlists.size() == 0) {
            deepcleanVideosRv.setVisibility(View.GONE);
            if (deepcleanVideosNumTv != null) {
                deepcleanVideosNumTv.setVisibility(View.VISIBLE);
                deepcleanVideosNumTv.setText("");
            }
        } else {
            deepcleanVideosRv.setVisibility(View.VISIBLE);
            List<VideoBean> mlist = new ArrayList<>();
            int msize = mlists.size();
            if (msize > 4) {
                mlist.add(mlists.get(0));
                mlist.add(mlists.get(1));
                mlist.add(mlists.get(2));
                mlist.add(mlists.get(3));
                mlist.add(mlists.get(4));
            } else if (msize == 4) {
                mlist.add(mlists.get(0));
                mlist.add(mlists.get(1));
                mlist.add(mlists.get(2));
                mlist.add(mlists.get(3));
            } else if (msize == 3) {
                mlist.add(mlists.get(0));
                mlist.add(mlists.get(1));
                mlist.add(mlists.get(2));
            } else if (msize == 2) {
                mlist.add(mlists.get(0));
                mlist.add(mlists.get(1));
            } else if (msize == 1) {
                mlist.add(mlists.get(0));
            }
            if (mDeepCleanVideoAdapter != null) {
                mDeepCleanVideoAdapter.setNewData(mlist);
            }
            if (deepcleanVideosNumTv != null) {

                long mSize = 0;
                for (int i = 0; i < mlist.size(); i++) {
                    VideoBean bean = mlist.get(i);
                    mSize = mSize + bean.getSize();
                }
                if (mSize > 0) {
                    FormatUtil.FileSize mFileSize = FormatUtil.formatFileSize(mSize);
                    String msizes = mFileSize.mSize;
                    if (msizes.contains(",")) {
                        msizes = msizes.replace(",", ".");
                    }
                    BigDecimal bigDecimal = new BigDecimal(msizes);
                    float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                    if (deepcleanVideosNumTv != null) {
                        deepcleanVideosNumTv.setVisibility(View.VISIBLE);
                        deepcleanVideosNumTv.setText(dropped + " " + mFileSize.mUnit + "");
                    }
                    showAllSize(mSize);
                }
            }
        }


    }

    @Override
    public void showApks(List<ApkBean> mlists) {
        if(deepcleanUninstallappNumTvPw!=null){
            deepcleanUninstallappNumTvPw.setVisibility(View.GONE);
        }

        if (mlists == null || mlists.size() == 0) {
            deepcleanUninstallappRv.setVisibility(View.GONE);
            if (deepcleanUninstallappNumTv != null) {
                deepcleanUninstallappNumTv.setVisibility(View.VISIBLE);
                deepcleanUninstallappNumTv.setText("");
            }
        } else {
            deepcleanUninstallappRv.setVisibility(View.VISIBLE);
            List<ApkBean> mlist = new ArrayList<>();
            int msize = mlists.size();
            if (msize > 4) {
                mlist.add(mlists.get(0));
                mlist.add(mlists.get(1));
                mlist.add(mlists.get(2));
                mlist.add(mlists.get(3));
                mlist.add(mlists.get(4));
            } else if (msize == 4) {
                mlist.add(mlists.get(0));
                mlist.add(mlists.get(1));
                mlist.add(mlists.get(2));
                mlist.add(mlists.get(3));
            } else if (msize == 3) {
                mlist.add(mlists.get(0));
                mlist.add(mlists.get(1));
                mlist.add(mlists.get(2));
            } else if (msize == 2) {
                mlist.add(mlists.get(0));
                mlist.add(mlists.get(1));
            } else if (msize == 1) {
                mlist.add(mlists.get(0));
            }
            if (mDeepCleanUnInstallApkAdapter != null) {
                mDeepCleanUnInstallApkAdapter.setNewData(mlist);
            }
            if (deepcleanUninstallappNumTv != null) {

                long mSize = 0;
                for (int i = 0; i < mlist.size(); i++) {
                    ApkBean bean = mlist.get(i);
                    mSize = mSize + bean.getmSize();
                }
                if (mSize > 0) {
                    FormatUtil.FileSize mFileSize = FormatUtil.formatFileSize(mSize);
                    String msizes = mFileSize.mSize;
                    if (msizes.contains(",")) {
                        msizes = msizes.replace(",", ".");
                    }
                    BigDecimal bigDecimal = new BigDecimal(msizes);
                    float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                    if (deepcleanUninstallappNumTv != null) {
                        deepcleanUninstallappNumTv.setVisibility(View.VISIBLE);
                        deepcleanUninstallappNumTv.setText(dropped + " " + mFileSize.mUnit + "");
                    }
                    showAllSize(mSize);
                }
            }
        }


    }

    @Override
    public void showAudios(List<AudioBean> mLists) {
        if(deepcleanAudioTvPw!=null){
            deepcleanAudioTvPw.setVisibility(View.GONE);
        }

        if (mLists != null && mLists.size() > 0) {
            long mSize = 0;
            for (int i = 0; i < mLists.size(); i++) {
                AudioBean bean = mLists.get(i);
                mSize = mSize + bean.getSize();
            }
            if (mSize > 0) {
                FormatUtil.FileSize mFileSize = FormatUtil.formatFileSize(mSize);
                String msizes = mFileSize.mSize;
                if (msizes.contains(",")) {
                    msizes = msizes.replace(",", ".");
                }
                BigDecimal bigDecimal = new BigDecimal(msizes);
                float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                if (deepcleanAudioNumTv != null) {
                    deepcleanAudioNumTv.setVisibility(View.VISIBLE);
                    deepcleanAudioNumTv.setText(dropped + " " + mFileSize.mUnit + "");
                }
                showAllSize(mSize);
            }
        }else {
            if (deepcleanAudioNumTv != null) {
                deepcleanAudioNumTv.setVisibility(View.VISIBLE);
                deepcleanAudioNumTv.setText("");
            }
        }

    }

    @Override
    public void showSpecialApk(List<AppBean> mLists) {
        if(deepcleanAppdataTvPw!=null){
            deepcleanAppdataTvPw.setVisibility(View.GONE);
        }
        if (mLists != null && mLists.size() > 0) {
            long mSize = 0;
            for (int i = 0; i < mLists.size(); i++) {
                AppBean bean = mLists.get(i);
                mSize = mSize + bean.getAppSize();
            }
            if (mSize > 0) {
                FormatUtil.FileSize mFileSize = FormatUtil.formatFileSize(mSize);
                String msizes = mFileSize.mSize;
                if (msizes.contains(",")) {
                    msizes = msizes.replace(",", ".");
                }
                BigDecimal bigDecimal = new BigDecimal(msizes);
                float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                if (deepcleanAppcacheNumTv != null) {
                    deepcleanAppcacheNumTv.setVisibility(View.VISIBLE);
                    deepcleanAppcacheNumTv.setText(dropped + " " + mFileSize.mUnit + "");
                }
                showAllSize(mSize);
            }
        }else {
            if (deepcleanAppcacheNumTv != null) {
                deepcleanAppcacheNumTv.setVisibility(View.VISIBLE);
                deepcleanAppcacheNumTv.setText("");
            }
        }


    }

    @Override
    public void showBigFile(List<BigFileBean> mList) {
        if(deepcleanLargefileTvPw!=null){
            deepcleanLargefileTvPw.setVisibility(View.GONE);
        }
        if (mList != null && mList.size() > 0) {
            long mSize = 0;
            for (int i = 0; i < mList.size(); i++) {
                BigFileBean bean = mList.get(i);
                mSize = mSize + bean.getSize();
            }
            if (mSize > 0) {
                FormatUtil.FileSize mFileSize = FormatUtil.formatFileSize(mSize);
                String msizes = mFileSize.mSize;
                if (msizes.contains(",")) {
                    msizes = msizes.replace(",", ".");
                }
                BigDecimal bigDecimal = new BigDecimal(msizes);
                float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
                if (deepcleanLargefileNumTv != null) {
                    deepcleanLargefileNumTv.setVisibility(View.VISIBLE);
                    deepcleanLargefileNumTv.setText(dropped + " " + mFileSize.mUnit + "");
                }
                showAllSize(mSize);
            }
        }else {
            if(deepcleanLargefileNumTv!=null){
                deepcleanLargefileNumTv.setText("");
            }
        }
    }

    @Override
    public void showAllSize(long size) {
        mAllSize = mAllSize + size;
        if (mAllSize > 0) {
            FormatUtil.FileSize mFileSize = FormatUtil.formatFileSize(mAllSize);
            String msizes = mFileSize.mSize;
            if (msizes.contains(",")) {
                msizes = msizes.replace(",", ".");
            }
            BigDecimal bigDecimal = new BigDecimal(msizes);
            float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            if (deepcleanTotalTv != null) {
                deepcleanTotalTv.setText(dropped + "");
            }
            if (deepcleanTotalDwTv != null) {
                deepcleanTotalDwTv.setText(mFileSize.mUnit + "");
            }
        }else {
            if (deepcleanTotalTv != null) {
                deepcleanTotalTv.setText("");
            }
            if (deepcleanTotalDwTv != null) {
                deepcleanTotalDwTv.setText("B");
            }
        }
    }


    @Override
    public void setPresenter(DeepCleanContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {

        GridLayoutManager gridLayoutIamgesManager = new GridLayoutManager(getActivity(), 5);
        deepcleanIamgesRv.setLayoutManager(gridLayoutIamgesManager);
        mImageAdapter = new DeepCleanImageAdapter(getContext());
        deepcleanIamgesRv.setAdapter(mImageAdapter);
        deepcleanIamgesRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getContext(), SelectImageActivity.class));
            }
        });


        GridLayoutManager gridLayoutVideosManager = new GridLayoutManager(getActivity(), 5);
        deepcleanVideosRv.setLayoutManager(gridLayoutVideosManager);
        mDeepCleanVideoAdapter = new DeepCleanVideoAdapter(getContext());
        deepcleanVideosRv.setAdapter(mDeepCleanVideoAdapter);
        deepcleanVideosRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getContext(), SelectVideoActivity.class));
            }
        });


        GridLayoutManager gridLayoutUninstallappManager = new GridLayoutManager(getActivity(), 5);
        deepcleanUninstallappRv.setLayoutManager(gridLayoutUninstallappManager);
        mDeepCleanUnInstallApkAdapter = new DeepCleanUnInstallApkAdapter(getContext());
        deepcleanUninstallappRv.setAdapter(mDeepCleanUnInstallApkAdapter);
        deepcleanUninstallappRv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getContext(), SelectApkActivity.class));
            }
        });


        deepcleanImagesRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectImageActivity.class));
            }
        });

        deepcleanVideosRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectVideoActivity.class));
            }
        });

        deepcleanUninstallappRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectApkActivity.class));
            }
        });

        deepcleanAudiosRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectAudiosActivity.class));
            }
        });

        deepcleanAppdatasRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CleanAppDataActivity.class));
            }
        });

        deepcleanLargefilesRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectBigFileActivity.class));
            }
        });


    }

    @Override
    public void showMessageTips(String msg) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            mAllSize = 0;
            presenter.getImages();
            presenter.getVideos();
            presenter.getUnInstallApk();
            presenter.getAudios();
            presenter.getSpecialApk();
            presenter.getBigFile();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.deepclean_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
