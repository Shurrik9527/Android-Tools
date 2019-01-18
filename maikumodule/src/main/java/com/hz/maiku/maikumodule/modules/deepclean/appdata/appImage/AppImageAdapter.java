package com.hz.maiku.maikumodule.modules.deepclean.appdata.appImage;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.ImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/8
 * @email 252774645@qq.com
 */
public class AppImageAdapter extends BaseQuickAdapter<ImageBean,BaseViewHolder>{

    private static final String TAG =AppImageAdapter.class.getName();
    private Context mContext;
    public List<ImageBean> mlists =new ArrayList<>();
    private onClick mOnClick;
    private boolean[] flag;
    public AppImageAdapter(Context context,onClick onClick) {
        super(R.layout.select_image_item_layout);
        this.mContext =context;
        this.mOnClick =onClick;
    }


    @Override
    public void setNewData(@Nullable List<ImageBean> data) {
        super.setNewData(data);
        flag = new boolean[data.size()];
        for (int i=0;i<data.size();i++){
            flag[i]=false;
        }
    }

    @Override
    protected void convert(final BaseViewHolder helper, ImageBean item) {
        if(item==null){
            return;
        }

        ImageView imageView =helper.getView(R.id.select_image_iv);
        Glide.with(mContext)
                .load(item.getmUrl())
                .into(imageView);
        helper.addOnClickListener(R.id.select_image_iv);
        CheckBox checkBox = helper.getView(R.id.select_image_cb);
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(flag[helper.getLayoutPosition()]);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                flag[helper.getLayoutPosition()] = b;
                mOnClick.callBack(flag);
            }
        });
    }


    public interface onClick{
        void callBack(boolean[] list);
    }



}
