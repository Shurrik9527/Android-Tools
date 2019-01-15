package com.hz.maiku.maikumodule.modules.deepclean;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.VideoBean;
import com.hz.maiku.maikumodule.util.DeepCleanUtil;
import com.hz.maiku.maikumodule.util.TimeUtil;


/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class DeepCleanVideoAdapter extends BaseQuickAdapter<VideoBean,BaseViewHolder> {


    public DeepCleanVideoAdapter(Context context) {
        super(R.layout.deepclean_video_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean item) {

        try {
            ImageView mIcon = helper.getView(R.id.deepclean_video_icon_iv);
            helper.addOnClickListener(R.id.deepclean_video_icon_iv);
            if(!TextUtils.isEmpty(item.getmUrl())){
                Bitmap mBitmap =DeepCleanUtil.getVideoThumbnail(item.getmUrl());
                if(mBitmap!=null){
                    mIcon.setImageBitmap(mBitmap);
                }
            }
        }catch (Exception e){

        }
        helper.setText(R.id.deepclean_video_time_tv,TimeUtil.videoTime(item.getmDuration())+"");
    }
}
