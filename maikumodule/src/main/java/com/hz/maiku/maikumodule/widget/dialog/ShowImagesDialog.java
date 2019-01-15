package com.hz.maiku.maikumodule.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Config;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.util.ScreenUtils;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2019/1/15
 * @email 252774645@qq.com
 */
public class ShowImagesDialog extends Dialog {


    private PhotoView mPhotoView;
    private String mUrl;
    private Context mContext;
    public ShowImagesDialog(@NonNull Context context,String url) {
        super(context,R.style.common_alert_dialog);
        this.mUrl =url;
        this.mContext =context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_layout);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = 0;
        wl.height = ScreenUtils.getScreenHeight();
        wl.width = ScreenUtils.getScreenWidth();
        wl.gravity = Gravity.CENTER;
        window.setAttributes(wl);

        mPhotoView =findViewById(R.id.show_images_pv);
        if(!TextUtils.isEmpty(mUrl)){
            SimpleTarget target = new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    mPhotoView.setImageDrawable(resource);
                }
            };
            mPhotoView.setOnPhotoTapListener(listener);
            Glide.with(mContext) .load(mUrl)
                    .into(target);
        }

    }



    PhotoViewAttacher.OnPhotoTapListener listener = new PhotoViewAttacher.OnPhotoTapListener() {
        @Override
        public void onPhotoTap(View view, float x, float y) {
            dismiss();
        }
    };

}
