package com.hz.maiku.maikumodule.modules.deepclean.selectbigfile;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.R2;
import com.hz.maiku.maikumodule.bean.BigFileBean;
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
public class SelectBigFileFragment extends Fragment implements SelectBigFileContract.View {

    private static final String TAG = SelectBigFileFragment.class.getName();
    @BindView(R2.id.select_bigfile_gv)
    GridView selectIamgeGv;
    @BindView(R2.id.select_bigfile_btn)
    Button selectbigfileBtn;
    private SelectBigFileContract.Presenter presenter;
    private SelectBigFileAdapter mSelectBigFileAdapter;
    private List<BigFileBean> mlist;

    public SelectBigFileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SelectBigFileFragment newInstance() {
        SelectBigFileFragment fragment = new SelectBigFileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SelectBigFilePresenter(this, getContext());
    }


    @Override
    public void setPresenter(SelectBigFileContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initView() {
        mlist = new ArrayList<>();
        mSelectBigFileAdapter = new SelectBigFileAdapter(getContext(), mlist, new SelectBigFileAdapter.OnClick() {
            @Override
            public void callBack(int pos) {
                if (mlist != null && mlist.size() > 0) {
                    BigFileBean mBigFileBean = mlist.get(pos);
                    if (mBigFileBean.isSelect()) {
                        mBigFileBean.setSelect(false);
                    } else {
                        mBigFileBean.setSelect(true);
                    }
                    mlist.set(pos, mBigFileBean);
                    mSelectBigFileAdapter.setData(mlist);
                    mSelectBigFileAdapter.notifyDataSetChanged();
                    reflashView();
                }
            }
        });
        selectIamgeGv.setAdapter(mSelectBigFileAdapter);

        if (presenter != null) {
            presenter.getBigFiles();
        }

    }

    @Override
    public void showMessageTips(String msg) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.select_bigfile_fragment, container, false);
        ButterKnife.bind(this, root);
        initView();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showData(List<BigFileBean> mlists) {
        if (mlists != null && mlists.size() > 0 && mSelectBigFileAdapter != null) {
            this.mlist = mlists;
            mSelectBigFileAdapter.setData(mlist);
            mSelectBigFileAdapter.notifyDataSetChanged();
        }
        if(selectbigfileBtn!=null){
            selectbigfileBtn.setText("Clean");
        }
    }





    @Override
    public void reflashView() {
        try {
            if (mlist != null && mlist.size() > 0) {
                long mSize = 0;
                for (BigFileBean bean : mlist) {
                    if(bean.isSelect()){
                        mSize = mSize + bean.getSize();
                    }
                }
                if(selectbigfileBtn!=null){
                    if(mSize>0){
                        selectbigfileBtn.setText("Clean  "+FormatUtil.formatFileSize(mSize).toString() );
                    }else{
                        selectbigfileBtn.setText("Clean");
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

    @OnClick(R2.id.select_bigfile_btn)
    public void onClick() {

        if(StringUtil.isFastDoubleClick()){
            return;
        }

        if(mlist!=null&&mlist.size()>0){
            if(presenter!=null){
                final List<BigFileBean> list = new ArrayList<>();
                long mSize = 0;
                for (int i=0;i<mlist.size();i++){
                    BigFileBean bean = mlist.get(i);
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
                                presenter.delectBigFileList(list, finalMSize);
                            }
                        }
                    });
                    dialog.show();
                }
            }
        }

    }
}
