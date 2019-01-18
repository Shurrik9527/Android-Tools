package com.hz.maiku.maikumodule.modules.deepclean.appdata.appvideo;

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
import com.hz.maiku.maikumodule.bean.VideoBean;
import com.hz.maiku.maikumodule.util.FormatUtil;
import com.hz.maiku.maikumodule.util.ScreenUtils;
import com.hz.maiku.maikumodule.util.TimeUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/8
 * @email 252774645@qq.com
 */
public class AppVideoAdapter extends ArrayAdapter<VideoBean>{


    private Context mContext;
    public List<VideoBean> allPhotoList;
    private int destWidth, destHeight;
    int screenWidth;
    private OnClick mOnClick;
    public AppVideoAdapter(Context mContext, List<VideoBean> list, OnClick onClick) {
        super(mContext,R.layout.select_video_item_layout);
        this.mContext = mContext;
        this.allPhotoList = list;
        screenWidth = ScreenUtils.getScreenWidth();
        this.destWidth = (screenWidth - 20) / 3;
        this.destHeight = (screenWidth - 20) / 3;
        this.mOnClick =onClick;
    }


    public void  setData(List<VideoBean> list){
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_video_item_layout, parent, false);
            viewHolder.rlPhoto = (RelativeLayout) convertView.findViewById(R.id.select_video_rl);
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.select_video_iv);
            viewHolder.iv_select = (CheckBox) convertView.findViewById(R.id.select_video_cb);
            viewHolder.iv_time = (TextView) convertView.findViewById(R.id.select_video_time_tv);
            viewHolder.iv_size =(TextView)convertView.findViewById(R.id.select_video_size_tv);
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
        VideoBean mVideoBean = allPhotoList.get(position);
        if(mVideoBean.isSelect()){
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


        try {
            Glide.with(mContext)
                    .load(mVideoBean.getmUrl())
                    .into(viewHolder.iv_photo);

        }catch (Exception e){

        }
        viewHolder.iv_time.setText(TimeUtil.videoTime(mVideoBean.getmDuration())+"");
        FormatUtil.FileSize mFileSize=FormatUtil.formatFileSize(mVideoBean.getSize());
        String msizes =mFileSize.mSize;
        if (msizes.contains(",")) {
            msizes = msizes.replace(",", ".");
        }
        BigDecimal bigDecimal = new BigDecimal(msizes);
        float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        viewHolder.iv_size.setText(dropped+""+mFileSize.mUnit);

        return convertView;
    }

    class ViewHolder {
        public RelativeLayout rlPhoto;
        public ImageView iv_photo;
        public TextView iv_time;
        public TextView iv_size;
        public CheckBox iv_select;
    }
}
