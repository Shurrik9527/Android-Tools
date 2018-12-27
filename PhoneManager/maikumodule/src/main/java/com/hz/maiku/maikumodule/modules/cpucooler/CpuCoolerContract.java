package com.hz.maiku.maikumodule.modules.cpucooler;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.AppProcessInfornBean;

import java.util.List;

/**
 * Created by Shurrik on 2017/11/30.
 */

public class CpuCoolerContract {

    public interface View extends BaseView<Presenter> {
        //显示当前运行的进行数据
        void showProcessRunning(List<AppProcessInfornBean> mlists);
        void showTemperature(float temper);
        void startScanApp();
        void initData();
        void startHeartBeat();
        void playHeartbeatAnimation();
        void cleanFinish();
        void startCleanDialog();
        void startRocketAnimation();
        //判断是否是最佳
        boolean isOptimized();
        void  initProcessList(List<AppProcessInfornBean> list);
        void initTemp(float temp);

    }

    public interface Presenter extends BasePresenter {
        void getProcessRunningApp();
        void startScanProcessRunningApp();
        void startCleanApp(List<AppProcessInfornBean> appProcessInfornBeans);
    }
}
