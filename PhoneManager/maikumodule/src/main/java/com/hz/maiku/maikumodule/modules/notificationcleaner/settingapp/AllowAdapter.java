package com.hz.maiku.maikumodule.modules.notificationcleaner.settingapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.NotificationBean;
import com.hz.maiku.maikumodule.util.AppUtil;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/21
 * @email 252774645@qq.com
 */
public class AllowAdapter extends BaseQuickAdapter<NotificationBean,BaseViewHolder> {

    private Context mContext =null;
    private boolean isTrue;
    private boolean isOpen =false;
    public AllowAdapter(Context context) {
        super(R.layout.settingapp_item_allow_layout);
        this.mContext =context;
    }
    public void  setOpenState(boolean state){
        this.isOpen =state;
    }
    @Override
    protected void convert(BaseViewHolder helper, final NotificationBean item) {

        if(item!=null){

            //名称为空 则默认填写手机号
            if(!TextUtils.isEmpty(item.getAppName())){
                helper.setText(R.id.settingapp_allow_name,item.getAppName()+"");
            }

            ImageView icons =helper.getView(R.id.settingapp_allow_icon);
            if(!TextUtils.isEmpty(item.getAppPackageName())){
                icons.setImageDrawable(AppUtil.getIconByPkgname(mContext,item.getAppPackageName()));
            }

            Switch selectApp =helper.getView(R.id.settingapp_allow_cb);
            if(isOpen){
//                selectApp.setEnabled(true);
//                selectApp.setFocusable(true);
                selectApp.setVisibility(View.VISIBLE);
            }else {
                selectApp.setVisibility(View.INVISIBLE);
            }
            helper.addOnClickListener(R.id.settingapp_allow_cb);
            if(item.isOpen()){
                selectApp.setChecked(true);
            }else{
                selectApp.setChecked(false);
            }

        }
    }


}
