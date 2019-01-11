package com.hz.maiku.maikumodule.modules.deepclean.selectImage;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.ImageBean;
import com.hz.maiku.maikumodule.util.AlxImageLoader;
import com.hz.maiku.maikumodule.util.ScreenUtils;
import java.io.File;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/8
 * @email 252774645@qq.com
 */
public class SelectImageAdapter extends ArrayAdapter<ImageBean>{


    private Context mContext;
    public List<ImageBean> allPhotoList;
    private int destWidth, destHeight;
    int screenWidth;
    private OnClick mOnClick;
    public SelectImageAdapter(Context mContext,List<ImageBean> list,OnClick onClick) {
        super(mContext,R.layout.select_image_item_layout);
        this.mContext = mContext;
        this.allPhotoList = list;
        screenWidth = ScreenUtils.getScreenWidth();
        this.destWidth = (screenWidth - 20) / 3;
        this.destHeight = (screenWidth - 20) / 3;
        this.mOnClick=onClick;
    }


    public void  setData(List<ImageBean> list){

        if(allPhotoList!=null&&allPhotoList.size()>0){
            allPhotoList.clear();
        }
        allPhotoList.addAll(list);
    }

    public interface  OnClick{
        void callBack(int pos);
    }

    @Override
    public int getCount() {
        return allPhotoList==null?0:allPhotoList.size();//加一是为了那个相机图标
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_image_item_layout, parent, false);
            viewHolder.rlPhoto = (RelativeLayout) convertView.findViewById(R.id.select_image_rl);
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.select_image_iv);
            viewHolder.iv_select = (CheckBox) convertView.findViewById(R.id.select_image_cb);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder.iv_photo.getLayoutParams() != null) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewHolder.iv_photo.getLayoutParams();
            lp.width = destWidth;
            lp.height = destHeight;
            viewHolder.iv_photo.setLayoutParams(lp);
        }
       ImageBean imageBean = allPhotoList.get(position);
        if(imageBean.isSelect()){
            viewHolder.iv_select.setChecked(true);
        }else {
            viewHolder.iv_select.setChecked(false);
        }

        viewHolder.iv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClick!=null){
                    mOnClick.callBack(position);
                }
            }
        });

        Glide.with(mContext)
                .load(imageBean.getmUrl())
                .into(viewHolder.iv_photo);


        return convertView;
    }

    class ViewHolder {
        public RelativeLayout rlPhoto;
        public ImageView iv_photo;
        public CheckBox iv_select;
    }
}
