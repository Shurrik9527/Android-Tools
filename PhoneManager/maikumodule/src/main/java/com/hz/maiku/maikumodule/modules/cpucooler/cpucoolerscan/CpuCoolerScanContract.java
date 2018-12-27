package com.hz.maiku.maikumodule.modules.cpucooler.cpucoolerscan;



import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;

import java.util.List;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class CpuCoolerScanContract {

    public interface View extends BaseView<Presenter> {
        //显示当前运行的进行数据
        void showProcessRunning(List<AppProcessInfornBean> mlists);
        void showTemperature(float temper);
        void initData();
        boolean hasPermission();
        //判断是否是最佳
        boolean isOptimized();
        void startScan();
    }

    public interface Presenter extends BasePresenter {
        void getProcessRunningApp();
        void startScanProcessRunningApp();
        void runAppProcessInform();
    }
}
