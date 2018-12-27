package com.hz.maiku.maikumodule.modules.notificationcleaner;

import com.hz.maiku.maikumodule.base.BaseView;
import com.hz.maiku.maikumodule.bean.NotificationMsgBean;

import java.util.List;

/**
 * @author heguogui
 * @version v 3.0.0
 * @describe
 * @date 2018/12/19
 * @email 252774645@qq.com
 */
public class NotificationCleanerContract {

    public interface View extends BaseView<NotificationCleanerContract.Presenter> {
        void  showAllMsg(List<NotificationMsgBean> lists);
        void showPermission();
        void checkPermission();
        void initData();
        void updataNotification(int position, List<NotificationMsgBean> mlist);
        void setViewState(boolean state);
        void setEmptyView();
    }

    public interface Presenter {
        void showAllApps();
    }
}
