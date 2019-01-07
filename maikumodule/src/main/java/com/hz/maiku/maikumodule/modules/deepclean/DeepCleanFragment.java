package com.hz.maiku.maikumodule.modules.deepclean;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.AlbumBean;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.DigitalRollingTextView;
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
    @BindView(R2.id.deepclean_installationpackage_iv)
    ImageView deepcleanInstallationpackageIv;
    @BindView(R2.id.deepclean_installationpackage_title_tv)
    TextView deepcleanInstallationpackageTitleTv;
    @BindView(R2.id.deepclean_installationpackage_next_iv)
    ImageView deepcleanInstallationpackageNextIv;
    @BindView(R2.id.deepclean_installationpackage_rl)
    RelativeLayout deepcleanInstallationpackageRl;

    private DeepCleanContract.Presenter presenter;
    private DeepCleanAdapter mAdapter;

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
    public void showData(List<AlbumBean> mlists) {
        if (mlists != null) {
            // mAdapter.setNewData(mlists);
        }
    }


    @Override
    public void setPresenter(DeepCleanContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {

        GridLayoutManager gridLayoutIamgesManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutIamgesManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        deepcleanIamgesRv.setLayoutManager(gridLayoutIamgesManager);
        GridLayoutManager gridLayoutVideosManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutVideosManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        deepcleanIamgesRv.setLayoutManager(gridLayoutVideosManager);
        GridLayoutManager gridLayoutUninstallappManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutUninstallappManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        deepcleanIamgesRv.setLayoutManager(gridLayoutUninstallappManager);


        if(presenter!=null){
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
        View root = inflater.inflate(R.layout.deepclean_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    @OnClick({R2.id.deepclean_images_rl, R2.id.deepclean_videos_rl, R2.id.deepclean_uninstallapps_rl, R2.id.deepclean_audios_rl, R2.id.deepclean_appdatas_rl, R2.id.deepclean_largefiles_rl, R2.id.deepclean_installationpackage_rl})
    public void onClick(View view) {
        int m_id = view.getId();
        if(m_id==R2.id.deepclean_images_rl){
            ToastUtil.showToast(getContext(),"0");
        }else if(m_id==R2.id.deepclean_videos_rl){
            ToastUtil.showToast(getContext(),"1");
        }else if(m_id==R2.id.deepclean_uninstallapps_rl){
            ToastUtil.showToast(getContext(),"2");
        }else if(m_id==R2.id.deepclean_audios_rl){
            ToastUtil.showToast(getContext(),"3");
        }else if(m_id==R2.id.deepclean_appdatas_rl){
            ToastUtil.showToast(getContext(),"4");
        }else if(m_id==R2.id.deepclean_largefiles_rl){
            ToastUtil.showToast(getContext(),"5");
        }else if(m_id==R2.id.deepclean_installationpackage_rl){
            ToastUtil.showToast(getContext(),"6");
        }

    }
}
