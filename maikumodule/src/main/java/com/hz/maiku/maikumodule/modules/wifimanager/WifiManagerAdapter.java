package com.hz.maiku.maikumodule.modules.wifimanager;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.WifiBean;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/11/14
 * @email 252774645@qq.com
 */
public class WifiManagerAdapter extends BaseQuickAdapter<WifiBean,BaseViewHolder> {


    public WifiManagerAdapter() {
        super(R.layout.wifi_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiBean item) {
        if(item!=null){

            helper.setText(R.id.wifi_item_name,item.getWifiName()+"");
            helper.addOnClickListener(R.id.wifi_item_rl);
            ImageView imageView =helper.getView(R.id.wifi_item_icon);

            int level =Integer.parseInt(item.getLevel());
            if(level==0||level==1){
                imageView.setImageResource(R.drawable.ic_wifi_1);
            }else if(level==2){
                imageView.setImageResource(R.drawable.ic_wifi_2);
            }else if(level==3){
                imageView.setImageResource(R.drawable.ic_wifi_3);
            }else if(level==4){
                imageView.setImageResource(R.drawable.ic_wifi_4);
            }

        }
    }
}
