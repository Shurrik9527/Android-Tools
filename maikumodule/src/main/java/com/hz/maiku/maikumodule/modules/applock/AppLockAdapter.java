package com.hz.maiku.maikumodule.modules.applock;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.hz.maiku.maikumodule.R;
import com.hz.maiku.maikumodule.bean.CommLockInfo;
import com.hz.maiku.maikumodule.manager.CommLockInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/10/12
 * @email 252774645@qq.com
 */
public class AppLockAdapter extends RecyclerView.Adapter<AppLockAdapter.ViewHolder>{

    private List<CommLockInfo> mLockInfos = new ArrayList<>();
    private Context mContext;
    private PackageManager packageManager;
    private CommLockInfoManager mLockInfoManager;

    public AppLockAdapter(Context mContext) {
        this.mContext = mContext;
        packageManager = mContext.getPackageManager();
        mLockInfoManager = new CommLockInfoManager(mContext);
    }

    public void setLockInfos(List<CommLockInfo> lockInfos) {
        mLockInfos.clear();
        mLockInfos.addAll(lockInfos);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.applock_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CommLockInfo lockInfo = mLockInfos.get(position);
        initData(holder.mAppName, holder.mSwitchCompat, holder.mAppIcon, lockInfo);
        holder.mSwitchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeItemLockStatus(holder.mSwitchCompat, lockInfo, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLockInfos.size();
    }

    /**
     * 初始化数据
     */
    private void initData(TextView tvAppName, Switch switchCompat, ImageView mAppIcon, CommLockInfo lockInfo) {
        tvAppName.setText(packageManager.getApplicationLabel(lockInfo.getAppInfo()));
        switchCompat.setChecked(lockInfo.isLocked());
        ApplicationInfo appInfo = lockInfo.getAppInfo();
        mAppIcon.setImageDrawable(packageManager.getApplicationIcon(appInfo));
    }

    public void changeItemLockStatus(Switch checkBox, CommLockInfo info, int position) {
        if (checkBox.isChecked()) {
            info.setLocked(true);
            mLockInfoManager.lockCommApplication(info.getPackageName());
        } else {
            info.setLocked(false);
            mLockInfoManager.unlockCommApplication(info.getPackageName());
        }
        notifyItemChanged(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mAppIcon;
        private TextView mAppName;
        private Switch mSwitchCompat;

        public ViewHolder(View itemView) {
            super(itemView);
            mAppIcon = (ImageView) itemView.findViewById(R.id.applock_icon);
            mAppName = (TextView) itemView.findViewById(R.id.applock_name);
            mSwitchCompat = (Switch) itemView.findViewById(R.id.applock_cb);
        }
    }

    public List<CommLockInfo> getData(){
        return  mLockInfos;
    }


}
