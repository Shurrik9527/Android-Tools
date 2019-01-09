package com.hz.maiku.maikumodule.modules.trafficstatistics;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.TrafficStatisBean;
import com.hz.maiku.maikumodule.bean.WifiMobileBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/18
 * @email 252774645@qq.com
 */
public class TrafficStatisticsContract {

    public interface View extends BaseView<TrafficStatisticsContract.Presenter> {
        //显示当前运行的进行数据
        void showData(List<TrafficStatisBean> mlists);
        void showAllTraffic(WifiMobileBean bean);
        void showPermission();
        boolean hasPermissionToReadNetworkStats();
    }

    public interface Presenter extends BasePresenter {
        void getMonthTrafficStatistics();
        void getDayTrafficStatistics();
        void getMonthSize();
        void getTodaySize();
    }
}
