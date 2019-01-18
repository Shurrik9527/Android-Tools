package com.hz.maiku.maikumodule.modules.deepclean.appdata.appaudio;

import android.content.Intent;
import android.media.MediaPlayer;
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
import com.hz.maiku.maikumodule.bean.AudioBean;
import com.hz.maiku.maikumodule.manager.AudioPlayManager;
import com.hz.maiku.maikumodule.modules.deepclean.cleansuccess.CleanSuccessActivity;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.StringUtil;
import com.hz.maiku.maikumodule.util.ToastUtil;
import com.hz.maiku.maikumodule.widget.dialog.AlertDoubleBtnDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/17
 * @email 252774645@qq.com
 */
public class AppAudiosFragment extends Fragment implements AppAudiosContract.View{

    @BindView(R2.id.select_audio_gv)
    GridView selectAudioGv;
    @BindView(R2.id.select_audio_btn)
    Button selectAudioBtn;
    private MediaPlayer player;
    private AppAudiosContract.Presenter presenter;
    private AppAudiosAdapter mAppAudiosAdapter;
    private List<AudioBean> mlist;
    private int selectPos=-1;
    private String temp;
    public AppAudiosFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AppAudiosFragment newInstance() {
        AppAudiosFragment fragment = new AppAudiosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppAudiosPresenter(this, getContext());
        Bundle args =getArguments();
        if(args!=null){
            temp =  args.getString("APPDATA");
        }
    }


    @Override
    public void setPresenter(AppAudiosContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        mlist = new ArrayList<>();
        mAppAudiosAdapter = new AppAudiosAdapter(getContext(), mlist, new AppAudiosAdapter.OnClick() {
            @Override
            public void callBack(int pos) {
                if (mlist != null && mlist.size() > 0) {
                    AudioBean mAudioBean = mlist.get(pos);
                    if (mAudioBean.isSelect()) {
                        mAudioBean.setSelect(false);
                    } else {
                        mAudioBean.setSelect(true);
                    }
                    mlist.set(pos,mAudioBean);
                    mAppAudiosAdapter.setData(mlist);
                    mAppAudiosAdapter.notifyDataSetChanged();
                    reflashView();
                }
            }
        });
        selectAudioGv.setAdapter(mAppAudiosAdapter);
        selectAudioGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mlist != null && mlist.size() > 0) {
                    AudioBean mAudioBean = mlist.get(position);
                    startAudios(mAudioBean.getmUrl(),position);
                }

            }
        });
        if(presenter!=null){
            if(!TextUtils.isEmpty(temp)){
                if(temp.equals(AppUtil.qq_name)){
                    presenter.getAudios("tencent/MobileQQ");
                }else if(temp.equals(AppUtil.weixing_name)){
                    presenter.getAudios("tencent/MicroMsg");
                }else if(temp.equals(AppUtil.instagram_name)){
                    presenter.getAudios("Rewords/Instagram");
                }else if(temp.equals(AppUtil.facebook_name)){
                    presenter.getAudios("Rewords/Facebook");
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
        View root = inflater.inflate(R.layout.select_audio_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void showAudiosData(List<AudioBean> mlists) {
        if (mlists != null && mlists.size() > 0 && mAppAudiosAdapter != null) {
            this.mlist = mlists;
            mAppAudiosAdapter.setData(mlist);
            mAppAudiosAdapter.notifyDataSetChanged();
        }
        if(selectAudioBtn!=null){
            selectAudioBtn.setText("Clean");
        }
    }

    @Override
    public void reflashView() {
        try {
            if (mlist != null && mlist.size() > 0) {
                long mSize = 0;
                for (AudioBean bean : mlist) {
                    if(bean.isSelect()){
                        mSize = mSize + bean.getSize();
                    }
                }
                if(selectAudioBtn!=null){
                    if(mSize>0){
                        selectAudioBtn.setText("Clean  "+FormatUtil.formatFileSize(mSize).toString() );
                    }else{
                        selectAudioBtn.setText("Clean");
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
    public void startAudios(String url,int pos) {
        if (AudioPlayManager.getInstance().isPlaying() && pos == selectPos) {//正在播放
            selectPos =-1;
        } else {
            selectPos = pos;
            if(url.contains(".amr")){
                ToastUtil.showToast(getContext(),getString(R.string.deep_clean_amr_noplay));
                return;
            }
            AudioPlayManager.getInstance().startPlay(url);
        }
    }


    @Override
    public void onDestroy() {
        AudioPlayManager.getInstance().stopPlay();
        super.onDestroy();
    }

    @OnClick(R2.id.select_audio_btn)
    public void onClick() {

        if(StringUtil.isFastDoubleClick()){
            return;
        }

        if(mlist!=null&&mlist.size()>0){
            if(presenter!=null){
                final List<AudioBean> list = new ArrayList<>();
                long mSize = 0;
                for (int i=0;i<mlist.size();i++){
                    AudioBean bean = mlist.get(i);
                    if(bean.isSelect()){
                        list.add(bean);
                        mSize = mSize + bean.getSize();
                    }
                }
                if(list.size()>0){
                    final long finalMSize = mSize;
                    AlertDoubleBtnDialog dialog = new AlertDoubleBtnDialog(getContext(), getResources().getString(R.string.common_warm_prompt), "Are sure you clear "+list.size()+" Audios", "Sure", new AlertDoubleBtnDialog.ConfirmListener() {
                        @Override
                        public void callback() {
                            if(presenter!=null){
                                presenter.delectAudiosList(list, finalMSize);
                            }
                        }
                    });
                    dialog.show();
                }
            }
        }

    }

}
