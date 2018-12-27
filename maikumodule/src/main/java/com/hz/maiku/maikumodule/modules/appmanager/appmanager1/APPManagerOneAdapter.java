package com.hz.maiku.maikumodule.modules.appmanager.appmanager1;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.AppManagerBean;
import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;
import com.hz.maiku.maikumodule.util.AppUtil;
import com.hz.maiku.maikumodule.util.FormatUtil;


/**
 * @author heguogui
 * @version v 3.0.0
 * @describe app 管理适配器
 * @date 2018/9/13
 * @email 252774645@qq.com
 */
public class APPManagerOneAdapter extends BaseQuickAdapter<AppManagerBean,BaseViewHolder> {

    private Context mContext;
    private PackageManager packageManager;
    public APPManagerOneAdapter(Context context) {
        super(R.layout.appmanager_item_layout);
        this.mContext =context;
        this.packageManager =mContext.getPackageManager();
    }


    @Override
    protected void convert(BaseViewHolder helper, AppManagerBean item) {

        if(item==null){
            return;
        }
        ApplicationInfo mApplicationInfo=item.getApplicationInfo();
        if(AppUtil.getAppSize(mApplicationInfo)>0){
            FormatUtil.FileSize mFileSize=FormatUtil.formatSizeByM(AppUtil.getAppSize(mApplicationInfo));
            if(mFileSize!=null){
                helper.setText(R.id.appmanager_item_size,mFileSize.mSize+mFileSize.mUnit.name());
            }
        }

        helper.setText(R.id.appmanager_item_name,mApplicationInfo.loadLabel(packageManager));
        Drawable mDrawable =mApplicationInfo.loadIcon(packageManager);
        if(mDrawable==null){
            helper.setImageResource(R.id.appmanager_item_icon,R.mipmap.ic_launcher);
        }else {
            helper.setImageDrawable(R.id.appmanager_item_icon,mDrawable);
        }
        helper.addOnClickListener(R.id.appmanager_item_rl);
        helper.addOnClickListener(R.id.appmanager_item_cb);
        CheckBox checkBox = helper.getView(R.id.appmanager_item_cb);
        if(item.isSelect()){
            checkBox.setChecked(true);
        }else{
            checkBox.setChecked(false);
        }
    }

}
