package com.hz.maiku.maikumodule.modules.deepclean;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.ImageBean;
import com.hz.maiku.maikumodule.bean.TrafficStatisBean;
import com.hz.maiku.maikumodule.util.AlxImageLoader;
import com.hz.maiku.maikumodule.util.FormatUtil;


/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class DeepCleanImageAdapter extends BaseQuickAdapter<ImageBean,BaseViewHolder> {

    private AlxImageLoader mLoader;
    public DeepCleanImageAdapter(Context context) {
        super(R.layout.deepclean_image_item_layout);
        mLoader = new AlxImageLoader(context);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageBean item) {

        ImageView mIcon = helper.getView(R.id.deepcleanimage_icon_iv);
        helper.addOnClickListener(R.id.deepcleanimage_icon_iv);
        if(!TextUtils.isEmpty(item.getmUrl())){
            Glide.with(mContext)
                    .load(item.getmUrl())
                    .into(mIcon);
//            mLoader.setAsyncBitmapFromSD(item.getmUrl(),mIcon,300,false,true,true);
        }
    }
}
