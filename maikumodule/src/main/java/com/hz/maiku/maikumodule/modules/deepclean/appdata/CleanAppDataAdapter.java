package com.hz.maiku.maikumodule.modules.deepclean.appdata;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.AppDataBean;
import com.hz.maiku.maikumodule.util.DeepCleanUtil;
import com.hz.maiku.maikumodule.util.FormatUtil;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/8
 * @email 252774645@qq.com
 */
public class CleanAppDataAdapter extends BaseQuickAdapter<AppDataBean, BaseViewHolder> {

    public CleanAppDataAdapter() {
        super(R.layout.select_appdata_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppDataBean item) {
        try {
            helper.setText(R.id.select_appdata_name_tv,item.getAppName());
            FormatUtil.FileSize mFileSize=FormatUtil.formatFileSize(item.getAppSize());
            String msizes =mFileSize.mSize;
            if (msizes.contains(",")) {
                msizes = msizes.replace(",", ".");
            }
            BigDecimal bigDecimal = new BigDecimal(msizes);
            float dropped = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            helper.setText(R.id.select_appdata_size_tv,dropped+""+mFileSize.mUnit);



            FormatUtil.FileSize mImageFileSize=FormatUtil.formatFileSize(item.getImageSize());
            String mimagesizes =mImageFileSize.mSize;
            if (mimagesizes.contains(",")) {
                msizes = msizes.replace(",", ".");
            }
            BigDecimal bigiamgeDecimal = new BigDecimal(msizes);
            float iamgedropped = bigiamgeDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            helper.setText(R.id.select_appdata_image_size_tv,iamgedropped+""+mImageFileSize.mUnit);
            if(iamgedropped==0){
                helper.setVisible(R.id.select_appdata_image_size_tv,false);
            }else {
                helper.setVisible(R.id.select_appdata_image_size_tv,true);
            }



            FormatUtil.FileSize maudioFileSize=FormatUtil.formatFileSize(item.getAudioSize());
            String maudiosizes =maudioFileSize.mSize;
            if (maudiosizes.contains(",")) {
                maudiosizes = maudiosizes.replace(",", ".");
            }
            BigDecimal bigaudioDecimal = new BigDecimal(maudiosizes);
            float audioidropped = bigaudioDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            helper.setText(R.id.select_appdata_audio_size_tv,audioidropped+""+maudioFileSize.mUnit);
            if(audioidropped==0){
                helper.setVisible(R.id.select_appdata_audio_size_tv,false);
            }else {
                helper.setVisible(R.id.select_appdata_audio_size_tv,true);
            }


            FormatUtil.FileSize mvideoFileSize=FormatUtil.formatFileSize(item.getVideoSize());
            String mvideosizes =mvideoFileSize.mSize;
            if (mvideosizes.contains(",")) {
                mvideosizes = mvideosizes.replace(",", ".");
            }
            BigDecimal bigvideoDecimal = new BigDecimal(mvideosizes);
            float videodropped = bigvideoDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
            helper.setText(R.id.select_appdata_video_size_tv,videodropped+""+mvideoFileSize.mUnit);
            if(videodropped==0){
                helper.setVisible(R.id.select_appdata_video_size_tv,false);
            }else {
                helper.setVisible(R.id.select_appdata_video_size_tv,true);
            }



            if(item.getAppIcon()==null){
                helper.setImageResource(R.id.select_appdata_iv,R.mipmap.ic_launcher);
            }else {
                helper.setImageDrawable(R.id.select_appdata_iv,item.getAppIcon());
            }

            helper.addOnClickListener(R.id.select_appdata_image_tv);
            ImageView mIcon =helper.getView(R.id.select_appdata_image_tv);
            if(item.getImageBean()!=null&&item.getImageBean().size()>0){
                mIcon.setVisibility(View.VISIBLE);
                Glide.with(mContext)
                        .load(item.getImageBean().get(0).getmUrl())
                        .into(mIcon);
            }else {
                mIcon.setVisibility(View.GONE);
            }

            helper.addOnClickListener(R.id.select_appdata_video_tv);
            ImageView mvideoIcon =helper.getView(R.id.select_appdata_video_tv);
            if(item.getVideoBean()!=null&&item.getVideoBean().size()>0){
                Bitmap mBitmap =DeepCleanUtil.getVideoThumbnail(item.getVideoBean().get(0).getmUrl());
                if(mBitmap!=null){
                    mvideoIcon.setImageBitmap(mBitmap);
                }
                mvideoIcon.setVisibility(View.VISIBLE);
            }else {
                mvideoIcon.setVisibility(View.GONE);
            }

            helper.addOnClickListener(R.id.select_appdata_audio_tv);
            ImageView maduioIcon =helper.getView(R.id.select_appdata_audio_tv);
            if(item.getAudioBean()!=null&&item.getAudioBean().size()>0){
                maduioIcon.setVisibility(View.VISIBLE);
                maduioIcon.setImageResource(R.drawable.ic_audio_icon);
            }else {
                maduioIcon.setVisibility(View.GONE);
            }


        }catch (Exception e){

        }
    }


}
