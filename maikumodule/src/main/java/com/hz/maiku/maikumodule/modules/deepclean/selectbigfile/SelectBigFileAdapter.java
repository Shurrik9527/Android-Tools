package com.hz.maiku.maikumodule.modules.deepclean.selectbigfile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.BigFileBean;
import com.hz.maiku.maikumodule.bean.ImageBean;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.ScreenUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/8
 * @email 252774645@qq.com
 */
public class SelectBigFileAdapter extends ArrayAdapter<BigFileBean>{


    private Context mContext;
    public List<BigFileBean> allPhotoList;
    private int destWidth, destHeight;
    int screenWidth;
    private OnClick mOnClick;
    public SelectBigFileAdapter(Context mContext, List<BigFileBean> list, OnClick onClick) {
        super(mContext,R.layout.select_bigfile_item_layout);
        this.mContext = mContext;
        this.allPhotoList = list;
        screenWidth = ScreenUtils.getScreenWidth();
        this.destWidth = (screenWidth - 20) / 3;
        this.destHeight = (screenWidth - 20) / 3;
        this.mOnClick=onClick;
    }


    public void  setData(List<BigFileBean> list){

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_bigfile_item_layout, parent, false);
            viewHolder.rlPhoto = (RelativeLayout) convertView.findViewById(R.id.select_bigfile_rl);
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.select_bigfile_iv);
            viewHolder.iv_select = (CheckBox) convertView.findViewById(R.id.select_bigfile_cb);
            viewHolder.tv_size =(TextView) convertView.findViewById(R.id.select_bigfile_size_tv);
            viewHolder.tv_name =convertView.findViewById(R.id.select_bigfile_name_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        BigFileBean mBigFileBean = allPhotoList.get(position);
        if(mBigFileBean.isSelect()){
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

        FormatUtil.FileSize mFileSize=FormatUtil.formatFileSize(mBigFileBean.getSize());
        String msizes =mFileSize.mSize;
        if (msizes.contains(",")) {
            msizes = msizes.replace(",", ".");
        }
        BigDecimal bigDecimal = new BigDecimal(msizes);
        float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        viewHolder.tv_size.setText(dropped+""+mFileSize.mUnit);
        viewHolder.tv_name.setText(mBigFileBean.getmName()+"");


        return convertView;
    }

    class ViewHolder {
        public RelativeLayout rlPhoto;
        public ImageView iv_photo;
        public CheckBox iv_select;
        public TextView tv_size;
        public TextView tv_name;
    }
}
