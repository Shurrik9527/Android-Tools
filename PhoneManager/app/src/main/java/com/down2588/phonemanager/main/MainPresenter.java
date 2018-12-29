package com.down2588.phonemanager.main;

import com.down2588.phonemanager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shurrik on 2018/12/26.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View view;

    public MainPresenter(MainContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public List<MainFunctionItem> getFunctions() {
        int[] texts = {
                R.string.junk_cleaner,
                R.string.cpu_cooler,
                R.string.app_manager,
                R.string.charge_booster,
                R.string.notification_cleaner,
                R.string.wifi_manager
        };

        int[] backgroundColors = {
                R.color.junk_cleaner,
                R.color.cpu_cooler,
                R.color.app_manager,
                R.color.charge_booster,
                R.color.notification_cleaner,
                R.color.wifi_manager
        };

        int[] icons = {
                R.drawable.ic_junk_cleaner,
                R.drawable.ic_cpu_cooler,
                R.drawable.ic_app_manager,
                R.drawable.ic_charge_booster,
                R.drawable.ic_notification_cleaner,
                R.drawable.ic_wifi_manager
        };

        List<MainFunctionItem> dataList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            MainFunctionItem mainFunctionItem = new MainFunctionItem();
            mainFunctionItem.setText(texts[i]);
            mainFunctionItem.setBackgroundColor(backgroundColors[i]);
            mainFunctionItem.setIcon(icons[i]);
            dataList.add(mainFunctionItem);
        }
        return dataList;
    }
}
