package com.hz.maiku.maikumodule.modules.trafficstatis;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.TrafficStatisBean;
import com.hz.maiku.maikumodule.util.FormatUtil;


/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class TrafficStatisAdapter extends BaseQuickAdapter<TrafficStatisBean,BaseViewHolder> {


    public TrafficStatisAdapter() {
        super(R.layout.trafficstatis_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, TrafficStatisBean item) {

        ImageView mIcon = helper.getView(R.id.trafficstatistic_icon_iv);
        if(item.getmDrawable()!=null){
            mIcon.setImageDrawable(item.getmDrawable());
        }else{
            mIcon.setImageResource(R.mipmap.ic_launcher);
        }
        if(!TextUtils.isEmpty(item.getmName())){
            helper.setText(R.id.trafficstatistic_name_tv,item.getmName());
        }else{
            helper.setText(R.id.trafficstatistic_name_tv,"Uninstall App");
        }


        if(item.getWifiSize()==0){
            helper.setText(R.id.trafficstatistic_wifisize_tv,"wifi:0B");
        }else{
            FormatUtil.FileSize mFileSize=FormatUtil.formatSizeBy1024(item.getWifiSize());
            helper.setText(R.id.trafficstatistic_wifisize_tv,"wifi:"+ mFileSize.mSize+mFileSize.mUnit.name());
        }


        if(item.getMobileSize()==0){
            helper.setText(R.id.trafficstatistic_mobilesize_tv,"mobile:0B");
        }else{
            FormatUtil.FileSize mFileSize=FormatUtil.formatSizeBy1024(item.getMobileSize());
            helper.setText(R.id.trafficstatistic_mobilesize_tv,"mobile:"+ mFileSize.mSize+mFileSize.mUnit.name());
        }

        if(item.getTotalSize()==0){
            helper.setText(R.id.trafficstatistic_total_tv,"0B");
        }else{
            FormatUtil.FileSize mFileSize=FormatUtil.formatSizeBy1024(item.getTotalSize());
            helper.setText(R.id.trafficstatistic_total_tv,mFileSize.mSize+mFileSize.mUnit.name()+"");
        }


    }
}
