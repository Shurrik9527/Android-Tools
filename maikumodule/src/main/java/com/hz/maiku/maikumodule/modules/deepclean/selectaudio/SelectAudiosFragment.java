package com.hz.maiku.maikumodule.modules.deepclean.selectaudio;


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
import com.hz.maiku.maikumodule.bean.AudioBean;
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
 * @describe 降温成功Fragment
 * @date 2018/12/13
 * @email 252774645@qq.com
 */
public class SelectAudiosFragment extends Fragment implements SelectAudiosContract.View {

    private static final String TAG = SelectAudiosFragment.class.getName();
    @BindView(R2.id.select_audio_gv)
    GridView selectAudioGv;
    @BindView(R2.id.select_audio_btn)
    Button selectAudioBtn;

    private SelectAudiosContract.Presenter presenter;
    private SelectAudiosAdapter mSelectAudiosAdapter;
    private List<AudioBean> mlist;

    public SelectAudiosFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectAudiosFragment newInstance() {
        SelectAudiosFragment fragment = new SelectAudiosFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SelectAudiosPresenter(this, getContext());
    }


    @Override
    public void setPresenter(SelectAudiosContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        mlist = new ArrayList<>();
        mSelectAudiosAdapter = new SelectAudiosAdapter(getContext(), mlist, new SelectAudiosAdapter.OnClick() {
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
                    mSelectAudiosAdapter.setData(mlist);
                    mSelectAudiosAdapter.notifyDataSetChanged();
                    reflashView();
                }
            }
        });
        selectAudioGv.setAdapter(mSelectAudiosAdapter);
        selectAudioGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });




        if (presenter != null) {
            presenter.getAudios();
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
        if (mlists != null && mlists.size() > 0 && mSelectAudiosAdapter != null) {
            this.mlist = mlists;
            mSelectAudiosAdapter.setData(mlist);
            mSelectAudiosAdapter.notifyDataSetChanged();
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
