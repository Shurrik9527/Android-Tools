package com.hz.maiku.maikumodule.modules.wifimanager;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.WifiBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class WifiManagerContract {

    public interface View extends BaseView<WifiManagerContract.Presenter> {
        void  showAllWifi(List<WifiBean> lists);
        void showPermission();
        void  initData();
        boolean checkPermission();
        void requestPermission();
        void initReceiver();//初始化wifi
        void wifiChange();
        void wifiListSet(String wifiName , int type);
        void configurationWifi(WifiBean wifiBean);
    }

    public interface Presenter extends BasePresenter {
        void scanAllWifis();
    }
}
