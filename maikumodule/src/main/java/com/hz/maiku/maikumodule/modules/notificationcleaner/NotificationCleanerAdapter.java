package com.hz.maiku.maikumodule.modules.notificationcleaner;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.NotificationMsgBean;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.TimeUtil;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/14
 * @email 252774645@qq.com
 */
public class NotificationCleanerAdapter extends BaseQuickAdapter<NotificationMsgBean,BaseViewHolder> {

    private static final String TAG =NotificationCleanerAdapter.class.getName();

    public NotificationCleanerAdapter() {
        super(R.layout.notificationcleaner_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, NotificationMsgBean item) {
        if(item!=null){
            if(!TextUtils.isEmpty(item.getTitleStr())){
                helper.setText(R.id.notification_item_name,item.getTitleStr());
            }else{
                if(!TextUtils.isEmpty(item.getTickerText())){
                    helper.setText(R.id.notification_item_name,item.getTickerText());
                }
            }

            helper.setText(R.id.notification_item_time,TimeUtil.getMDHMtime(Long.parseLong(item.getAddTime())));

            ImageView imageView =helper.getView(R.id.notification_item_icon);
            if(!TextUtils.isEmpty(item.getPackageName())){
                imageView.setImageDrawable(AppUtil.getIconByPkgname(mContext,item.getPackageName()));
            }else{
                imageView.setImageResource(R.mipmap.ic_launcher);
            }
            helper.setText(R.id.notification_item_msg,item.getContentStr());
        }
    }
}
