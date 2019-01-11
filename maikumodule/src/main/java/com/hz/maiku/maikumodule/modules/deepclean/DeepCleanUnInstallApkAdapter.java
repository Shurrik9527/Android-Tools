package com.hz.maiku.maikumodule.modules.deepclean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.ApkBean;
import com.hz.maiku.maikumodule.util.AlxImageLoader;
import com.hz.maiku.maikumodule.util.AppUtil;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import net.dongliu.apk.parser.bean.Icon;
import java.io.IOException;


/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class DeepCleanUnInstallApkAdapter extends BaseQuickAdapter<ApkBean,BaseViewHolder> {

    private AlxImageLoader mLoader;
    public DeepCleanUnInstallApkAdapter(Context context) {
        super(R.layout.deepclean_uninstallapk_item_layout);
        mLoader = new AlxImageLoader(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void convert(BaseViewHolder helper, ApkBean item) {

        ImageView mIconIv = helper.getView(R.id.deepclean_uninstallapk_icon_iv);
        helper.addOnClickListener(R.id.deepclean_uninstallapk_icon_iv);

        if(!TextUtils.isEmpty(item.getmName())){
            helper.setText(R.id.deepclean_uninstallapk_name_tv,item.getmName());
        }
        if(item.getmIcon()!=null){
            mIconIv.setImageBitmap(item.getmIcon());
        }
//        if(!TextUtils.isEmpty(item.getmUrl())){
//            ApkFile mApkFile =AppUtil.readApkInform(item.getmUrl());
//            if(mApkFile!=null){
//                try {
//                    ApkMeta mApkMeta =mApkFile.getApkMeta();
//                    if(mApkMeta!=null){
//                        if(!TextUtils.isEmpty(mApkMeta.getName())){
//                            helper.setText(R.id.deepclean_uninstallapk_name_tv,mApkMeta.getName());
//                        }
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    Icon mIcon =mApkFile.getIconFile();
//                    if(mIcon!=null&&mIcon.getData().length>0){
//                        Bitmap bitmap =BitmapFactory.decodeByteArray(mIcon.getData(),0,mIcon.getData().length);
//                        if(bitmap!=null){
//                            mIconIv.setImageBitmap(bitmap);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
    }
}
