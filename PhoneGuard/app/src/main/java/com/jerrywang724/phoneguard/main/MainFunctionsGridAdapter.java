package com.jerrywang724.phoneguard.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hz.maiku.maikumodule.modules.appmanager.AppManagerActivity;
import com.hz.maiku.maikumodule.modules.chargebooster.ChargeBoosterActivity;
import com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan.CpuCoolerScanActivity;
import com.hz.maiku.maikumodule.modules.junkcleaner.JunkCleanerActivity;
import com.hz.maiku.maikumodule.modules.notificationcleaner.NotificationCleanerActivity;
import com.hz.maiku.maikumodule.modules.wifimanager.WifiManagerActivity;
import com.jerrywang724.phoneguard.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFunctionsGridAdapter extends RecyclerView.Adapter<MainFunctionsGridAdapter.ViewHolder> {
    private Context context;
    //数据源
    private List<MainFunctionItem> dataList;

    //构造函数
    MainFunctionsGridAdapter(Context context, List<MainFunctionItem> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //对于Body中的item，我们也返回所对应的ViewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_function, viewGroup, false);
        return new ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MainFunctionItem mainFunctionItem = dataList.get(position);

        String text = context.getResources().getString(mainFunctionItem.getText());
        viewHolder.tvMainFunction.setText(text);
        Drawable left = context.getResources().getDrawable(mainFunctionItem.getIcon());
        left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
        viewHolder.tvMainFunction.setCompoundDrawables(left, null, null, null);

        int backgroundColor = context.getResources().getColor(mainFunctionItem.getBackgroundColor());
        viewHolder.cvMainFunction.setCardBackgroundColor(backgroundColor);
        viewHolder.cvMainFunction.setOnClickListener(v -> {
            Intent intent = new Intent();
            switch (mainFunctionItem.getText()) {
                case R.string.junk_cleaner:
                    intent.setClass(context, JunkCleanerActivity.class);
                    break;
                case R.string.cpu_cooler:
                    intent.setClass(context, CpuCoolerScanActivity.class);
                    break;
                case R.string.app_manager:
                    intent.setClass(context, AppManagerActivity.class);
                    break;
                case R.string.charge_booster:
                    intent.setClass(context, ChargeBoosterActivity.class);
                    break;
                case R.string.notification_cleaner:
                    intent.setClass(context, NotificationCleanerActivity.class);
                    break;
                case R.string.wifi_manager:
                    intent.setClass(context, WifiManagerActivity.class);
                    break;
            }
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_main_function)
        CardView cvMainFunction;
        @BindView(R.id.tv_main_function)
        TextView tvMainFunction;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
