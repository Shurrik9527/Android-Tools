package com.hz.maiku.maikumodule.modules.cpucooler;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;


/**
 * @author heguogui
 * @version v 1.0.0
 * @describe cpu 降温适配器
 * @date 2018/9/13
 * @email 252774645@qq.com
 */
public class CpuCoolerAdapter extends BaseQuickAdapter<AppProcessInfornBean,BaseViewHolder> {

    public CpuCoolerAdapter(Context context) {
        super(R.layout.cpucooler_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, AppProcessInfornBean item) {

        if(item==null){
            return;
        }
        helper.setText(R.id.cpucooler_item_name_tv,item.getAppName()+"");
        if(item.getIcon()==null){
            helper.setImageResource(R.id.cpucooler_item_icon_iv,R.mipmap.ic_launcher);
        }else {
            helper.setImageDrawable(R.id.cpucooler_item_icon_iv,item.getIcon());
        }
    }

}
