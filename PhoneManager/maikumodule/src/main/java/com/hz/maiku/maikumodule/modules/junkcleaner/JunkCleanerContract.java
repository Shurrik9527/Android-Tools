package com.hz.maiku.maikumodule.modules.junkcleaner;

import com.hz.maiku.maikumodule.base.BasePresenter;
import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.JunkCleanerGroupBean;
import com.hz.maiku.maikumodule.bean.JunkCleanerMultiItemBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 1.0.0
 * @describe 垃圾清理 约束接口
 * @date 2018/9/6
 * @email 252774645@qq.com
 */
public class JunkCleanerContract {

    public interface View extends BaseView<Presenter> {
        //初始化View
        void initView();
        //view监听
        void initClickListener();
        //初始数据
        void initData();
        //总共垃圾数目
        void showTotalSize(String size);
        //当前扫描垃圾文件名称
        void showCurrentScanJunkFileName(String path);
       //某项垃圾总数
        void showItemTotalJunkSize(int index, String junkSize);
        //填充数据
        void showData(JunkCleanerGroupBean junkCleanerGroupBean);
        //进度条显示
        void showDialog(int index);
        //进度条消失
        void dissDialog(int index);
        //点击Group
        void clickGroup(boolean isExpand, int index);
        //初始化适配器数据
        void initAdapterData(List<JunkCleanerMultiItemBean> mlists);
        //删除结束
        void cleanFinish();
        //开始动画
        void startHeartBeat();
        //初始化动画
        void playHeartbeatAnimation();
        //停止扫描
        void stopScan();
        //清除本地数据
        void cleanSpData();
        //开始扫描
        void startScan();

        void allMemorySpace(long num);

        void startAnimation(float progress, long time);

        void setProgress();

        void setBtnEnable(boolean enable);

        boolean hasPermission();

        void junkCleanerScan();

    }

    public interface Presenter extends BasePresenter {
        //开始任务
        void startScanTask();
        //开始清理来及任务
        void startCleanJunkTask(List<JunkCleanerMultiItemBean> list);
        //初始化适配器数据
        void initAdapterData();
        //获取数据结束
        void cheanDataFile(List<String> processList, List<String> appCacheList, List<String> junkList);

        void allMemorySpace();
    }
}
